package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.AtCommand;
import com.kylegrund.iridiumat.ResponseParseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements the Display Registers command.
 */
public class DisplayRegisters extends AtCommand{
    @Override
    public String getCommandString() {
        return "AT%R\n";
    }

    @Override
    public Map<String, String> parseResponse(String response) throws ResponseParseError {
        // parsed register values
        Map<String, String> registers = new HashMap<>();

        // parses register values for the 9575
        String[] lines = response.split("\n");

        for (String line : lines) {
            // format of output: "S000 000 00H  S001 000 00H"
            if (line.startsWith("S")) {
                // handle multiple registers per line
                for (String register : line.split(" {2}")) {
                    String[] values = register.split(" ");

                    // validate
                    if (values.length < 3) {
                        throw new ResponseParseError("Error parsing s register value.");
                    }

                    // get register number
                    String registerNumber = Integer.toString(Integer.parseInt(values[0].substring(1)));

                    // get value
                    String registerValue = Integer.toString(Integer.parseInt(values[1]));

                    // check that the register was not already parsed
                    if (registers.containsKey(registerNumber)) {
                        throw new ResponseParseError("Duplicate register found: " + registerNumber + ".");
                    }

                    // add parsed register
                    registers.put(registerNumber, registerValue);
                }
            }
        }

        return registers;
    }
}
