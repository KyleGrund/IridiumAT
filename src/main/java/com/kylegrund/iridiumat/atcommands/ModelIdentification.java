package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Implements the Display Registers command.
 */
public class ModelIdentification extends AtCommand{
    /**
     * The key used in the parsed map for the model identification string.
     */
    public static final String MODEL_IDENTIFICATION = "Model Identification";

    /**
     * The AT command String to send to the ISU.
     */
    private static final String COMMAND = "AT+CGMM\r\n";

    /**
     * Initializes a new instance of the ModelIdentification class.
     *
     * @param commEndpoint Callback used to have this command executed by the ISU.
     * @param getEchoEnabled A function which returns a boolean value indicating whether to expect a command echo from the ISU.
     */
    public ModelIdentification(CheckedFunction<CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException>, Map<String, String>, IOException> commEndpoint,
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
            Map<String, String> parsedData = new HashMap<>();

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

            // response is simply version string
            parsedData.put(MODEL_IDENTIFICATION, receiveLine.get());

            // response should simply be OK
            if (!"OK".equals(resp = receiveLine.get())) {
                throw new IOException("Expected OK response not received, got: \"" + resp + "\" instead.");
            }

            return parsedData;
        };
    }
}
