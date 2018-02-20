package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.AtCommand;
import com.kylegrund.iridiumat.ResponseParseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements the Display Registers command.
 */
public class ModelIdentification extends AtCommand{
    /**
     * The key used in the parsed map for the model identification string.
     */
    public static final String MODEL_IDENTIFICATION = "Model Identification";

    @Override
    public String getCommandString() {
        return "AT+CGMM\n";
    }

    @Override
    public Map<String, String> parseResponse(String response) {
        Map<String, String> parsedData = new HashMap<>();

        // response is simply version string
        parsedData.put(MODEL_IDENTIFICATION, response.trim());

        return parsedData;
    }
}
