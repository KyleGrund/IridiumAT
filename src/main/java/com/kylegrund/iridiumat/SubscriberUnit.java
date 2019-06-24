package com.kylegrund.iridiumat;

import com.fazecast.jSerialComm.SerialPort;
import com.kylegrund.iridiumat.atcommands.FactoryReset;
import com.kylegrund.iridiumat.atcommands.ModelIdentification;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides an interface to an Iridium subscriber unit.
 */
public abstract class SubscriberUnit {
    /**
     * The timeout for reading from the serial port in milliseconds.
     */
    private static final int SERIAL_READ_TIMEOUT_MS = 1000;

    /**
     * The timeout for writing to the serial port in milliseconds.
     */
    private static final int SERIAL_WRITE_TIMEOUT_MS = 1000;

    /**
     * Gets a list of commands the ISU supports.
     * @return A list of commands the ISU supports.
     */
    public abstract Map<String, AtCommand> getCommands();

    /**
     * Closes the connection to the underlying ISU. Further attempts to utilize the device will throw an Exception.
     */
    public abstract void close();

    /**
     * Returns Iridium subscriber units connected to the system.
     * @return A list of Iridium subscriber units connected to the system.
     */
    public static List<SubscriberUnit> getSubscriberUnits() {
        List<SubscriberUnit> found = new ArrayList<>();

        // gets an array of port names
        SerialPort[] toSearch = com.fazecast.jSerialComm.SerialPort.getCommPorts();

        // try all ports
        for (SerialPort port : toSearch) {
            // variable to hold the unit which was made
            SubscriberUnit unit;

            // create the port
            try {
                // open and set the port parameters
                port.setBaudRate(115200);
                port.setNumDataBits(8);
                port.setNumStopBits(1);
                port.setParity(0);
                port.setComPortParameters(115200, 8, 0, 1);
                port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, SERIAL_READ_TIMEOUT_MS, SERIAL_WRITE_TIMEOUT_MS);
                port.openPort();

                // drain port
                int toRead;
                while ((toRead = port.getInputStream().available()) > 0) {
                    byte[] buff = new byte[toRead];
                    int result = port.getInputStream().read(buff, 0, toRead);
                }

                // create writer for communications to ISU
                CheckedConsumer<String, IOException> sendLine = (str) -> port.writeBytes(str.getBytes(), str.length());

                // create reader for communications to ISU
                CheckedSupplier<String, IOException> receiveLine = new BufferedReader(new InputStreamReader(port.getInputStream()))::readLine;

                // build dispose method for port
                Runnable dispose = port::closePort;

                // AT command which resets ISU to factory defaults
                AtCommand factoryReset = new FactoryReset((func) -> func.accept(sendLine, receiveLine), () -> false);

                // AT command which gets ISU model number
                AtCommand modelIdentification = new ModelIdentification((func) -> func.accept(sendLine, receiveLine), () -> true);

                factoryReset.executeCommand(factoryReset.getParameters());
                Map<String, String> response = modelIdentification.executeCommand(factoryReset.getParameters());

                // try to build an ISU instance for the response with the "OK\n" removed
                unit = buildISU(sendLine, receiveLine, dispose, response.get(ModelIdentification.MODEL_IDENTIFICATION));

                // close the port if an ISU could not be detected otherwise add it to the list to return
                if (unit == null) {
                    dispose.run();
                } else {
                    found.add(unit);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return found;
    }

    /**
     * Creates an ISU instance for the port and identification string.
     * @param sendLine    A function which sends the provided String to the associated ISU.
     * @param receiveLine A function which returns a String read from the associated ISU.
     * @param disposeMethod The method to call to dispose unmanaged resources.
     * @param idString The identification response from the ISU.
     * @return An ISU instance or null if none could be created.
     */
    private static SubscriberUnit buildISU(
            CheckedConsumer<String, IOException> sendLine,
            CheckedSupplier<String, IOException> receiveLine,
            Runnable disposeMethod,
            String idString) throws Exception {
        if (idString.startsWith("9575")) {
            return new Motorola9575(sendLine, receiveLine, disposeMethod);
        }

        return null;
    }
}
