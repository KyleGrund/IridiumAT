package com.kylegrund.iridiumat;

import com.kylegrund.iridiumat.atcommands.DisplayRegisters;
import com.kylegrund.iridiumat.atcommands.ModelIdentification;
import jssc.SerialPort;

import java.util.Arrays;
import java.util.List;

public class Motorola9575 extends SubscriberUnit {
    /**
     * The serial port used to communicate with this device.
     */
    private final SerialPort port;

    /**
     * Initializes a new instance of the Motorola9575 class.
     * @param port The port used to communicate with the ISU hardware.
     */
    Motorola9575(SerialPort port) {
        this.port = port;
    }

    @Override
    public List<AtCommand> getCommands() {
        return Arrays.asList(
                new DisplayRegisters(),
                new ModelIdentification()
        );
    }
}
