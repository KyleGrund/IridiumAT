package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Implements the Display Registers command.
 */
public class DisplayRegisters extends AtCommand{
    /**
     * The AT command String to send to the ISU.
     */
    private static final String COMMAND = "AT%R\r\n";

    /**
     * Initializes a new instance of the DisplayRegisters class.
     *
     * @param commEndpoint Callback used to have this command executed by the ISU.
     * @param getEchoEnabled A function which returns a boolean value indicating whether to expect a command echo from the ISU.
     */
    public DisplayRegisters(CheckedFunction<CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException>, Map<String, String>, IOException> commEndpoint,
                     Supplier<Boolean> getEchoEnabled) {
        super(commEndpoint, getEchoEnabled);
    }

    @Override
    public Map<String, String> getParameters() {
        return new HashMap<>();
    }

    @Override
    protected CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException> execute(Map<String, String> parameters) {
        return (CheckedConsumer<String, IOException> sendLine, CheckedSupplier<String, IOException> receiveLine) -> {
            // parsed register values
            Map<String, String> registers = new HashMap<>();

            // send command
            sendLine.accept(COMMAND);

            String resp;

            // check response from ISU
            if (!COMMAND.startsWith(resp = receiveLine.get())) {
                throw new IOException("Expected command echo: \"" + COMMAND + "\" not received, got: \"" + resp + "\" instead.");
            }

            // get extra newline char
            if (!"".equals(resp = receiveLine.get())) {
                throw new IOException("Expected OK response not received, got: \"" + resp + "\" instead.");
            }

            List<String> lines = new ArrayList<>();
            String response;

            // final response should simply be OK
            while (!"OK".equals(response = receiveLine.get())) {
                if (!"".equals(response)) {
                    lines.add(response);
                }
            }

            // parses register values for the 9575
            for (String line : lines) {
                // format of output: "S000 000 00H  S001 000 00H"
                if (line.startsWith("S")) {
                    // handle multiple registers per line
                    for (String register : line.split(" {2}")) {
                        String[] values = register.split(" ");

                        // validate
                        if (values.length < 3) {
                            throw new IOException("Error parsing s register value.");
                        }

                        // get register number
                        String registerNumber = Integer.toString(Integer.parseInt(values[0].substring(1)));

                        // get value
                        String registerValue = Integer.toString(Integer.parseInt(values[1]));

                        // check that the register was not already parsed
                        if (registers.containsKey(registerNumber)) {
                            throw new IOException("Duplicate register found: " + registerNumber + ".");
                        }

                        // add parsed register
                        registers.put(registerNumber, registerValue);
                    }
                }
            }

            return registers;
        };
    }
}
