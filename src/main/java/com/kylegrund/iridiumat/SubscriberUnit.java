package com.kylegrund.iridiumat;

import com.kylegrund.iridiumat.atcommands.ModelIdentification;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides an interface to an Iridium subscriber unit.
 */
public abstract class SubscriberUnit {
    /**
     * Gets a list of commands the ISU supports.
     * @return A list of commands the ISU supports.
     */
    public abstract List<AtCommand> getCommands();

    /**
     * Returns Iridium subscriber units connected to the system.
     * @return A list of Iridium subscriber units connected to the system.
     */
    public static List<SubscriberUnit> getSubscriberUnits() {
        List<SubscriberUnit> found = new ArrayList<>();

        // gets an array of port names
        String[] toSearch = SerialPortList.getPortNames();

        // command to use to get identification string
        ModelIdentification idCommand = new ModelIdentification();

        // try all ports
        for (String portName : toSearch) {
            // variable to hold the unit which was made
            SubscriberUnit unit = null;

            // create the port
            SerialPort port = new SerialPort(portName);
            try {
                // open and set the port parameters
                port.openPort();
                port.setParams(
                        SerialPort.BAUDRATE_115200,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                // write command to get ID
                port.writeString(idCommand.getCommandString());

                // read ID
                StringBuilder response = new StringBuilder();
                try {
                    // response should end with OK
                    while (!response.toString().endsWith("OK\n")) {
                        // read a character at a time with a one second timeout
                        response.append(port.readString(1, 1000));
                    }

                    // try to build an ISU instance for the response with the "OK\n" removed
                    unit = buildISU(port, response.substring(0, response.length() - 3));
                } catch (SerialPortTimeoutException ex) {
                    ex.printStackTrace();
                }

                // close the port if an ISU could not be detected otherwise add it to the list to return
                if (unit == null) {
                    port.closePort();
                } else {
                    found.add(unit);
                }
            } catch (SerialPortException ex) {
                ex.printStackTrace();
            }
        }

        return found;
    }

    /**
     * Creates an ISU instance for the port and identification string.
     * @param port The port used to communicate with the ISU.
     * @param idString The identification response from the ISU.
     * @return An ISU instance or null if none could be created.
     */
    private static SubscriberUnit buildISU(SerialPort port, String idString) {
        if (idString.startsWith("9575")) {
            return new Motorola9575(port);
        }

        return null;
    }
}
