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
     * Gets a list of commands the ISU supports.
     * @return A list of commands the ISU supports.
     */
    public abstract Map<String, AtCommand> getCommands();

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
            SubscriberUnit unit = null;

            // create the port
            try {
                // open and set the port parameters
                port.setBaudRate(115200);
                port.setNumDataBits(8);
                port.setNumStopBits(1);
                port.setParity(0);
                port.setComPortParameters(115200, 8, 0, 1);
                port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 100, 100);
                port.openPort();

                // drain port
                int toRead = 0;
                while ((toRead = port.getInputStream().available()) > 0) {
                    byte[] buff = new byte[toRead];
                    port.getInputStream().read(buff, 0, toRead);
                }

                // create reader and writer for communications to ISU
                CheckedConsumer<String, IOException> sendLine = (str) -> port.writeBytes(str.getBytes(), str.length());

                CheckedSupplier<String, IOException> receiveLine = new BufferedReader(new InputStreamReader(port.getInputStream()))::readLine;

                // AT command which resets ISU to factory defaults
                AtCommand factoryReset = new FactoryReset((func) -> func.accept(sendLine, receiveLine));

                // AT command which gets ISU model number
                AtCommand modelIdentification = new ModelIdentification((func) -> func.accept(sendLine, receiveLine));

                factoryReset.executeCommand(factoryReset.getParameters());
                Map<String, String> response = modelIdentification.executeCommand(factoryReset.getParameters());

                // try to build an ISU instance for the response with the "OK\n" removed
                unit = buildISU(sendLine, receiveLine, response.get(ModelIdentification.MODEL_IDENTIFICATION));

                // close the port if an ISU could not be detected otherwise add it to the list to return
                if (unit == null) {
                    port.closePort();
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
     * @param idString The identification response from the ISU.
     * @return An ISU instance or null if none could be created.
     */
    private static SubscriberUnit buildISU(
            CheckedConsumer<String, IOException> sendLine,
            CheckedSupplier<String, IOException> receiveLine,
            String idString) {
        if (idString.startsWith("9575")) {
            return new Motorola9575(sendLine, receiveLine);
        }

        return null;
    }
}
