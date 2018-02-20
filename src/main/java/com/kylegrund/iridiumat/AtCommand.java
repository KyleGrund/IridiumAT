package com.kylegrund.iridiumat;

import java.util.Map;

/**
 * Abstract interface representing an Iridium ISU AT command.
 */
public abstract class AtCommand {
    /**
     * Gets the string which executes the command when sent to the ISU.
     * @return The string which executes the command when sent to the ISU.
     */
    public abstract String getCommandString();

    /**
     * Parses a response into a mapping of String names to String values.
     * @param response The response to parse.
     * @return A mapping of String names to String values.
     */
    public abstract Map<String, String> parseResponse(String response) throws ResponseParseError;
}
