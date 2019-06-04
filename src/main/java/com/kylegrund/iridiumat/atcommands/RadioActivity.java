package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements the Radio Activity command.
 */
public class RadioActivity extends AtCommand{
    /**
     * The AT command String to send to the ISU.
     */
    private static final String COMMAND = "AT*R";

    /**
     * Initializes a new instance of the AtCommand class.
     *
     * @param commEndpoint Callback used to have this command executed by the ISU.
     */
    public RadioActivity(CheckedFunction<CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException>, Map<String, String>, IOException> commEndpoint) {
        super(commEndpoint);
    }

    @Override
    public Map<String, String> getParameters() {
        return new HashMap<>();
    }

    @Override
    protected CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException> execute(Map<String, String> parameters) {
        return (CheckedConsumer<String, IOException> sendLine, CheckedSupplier<String, IOException> receiveLine) -> {
            // send command
            sendLine.accept(COMMAND);

            String resp;

            // check response from ISU
            if (!COMMAND.startsWith(resp = receiveLine.get())) {
                throw new IOException("Expected command echo: \"" + COMMAND + "\" not received, got: \"" + resp + "\" instead.");
            }

            // get extra newline char
            if (!"".equals(resp = receiveLine.get())) {
                throw new IOException("Expected new line not received, got: \"" + resp + "\" instead.");
            }

            // response should simply be OK
            if (!"OK".equals(resp = receiveLine.get())) {
                throw new IOException("Expected OK response not received, got: \"" + resp + "\" instead.");
            }

            return new HashMap<>();
        };
    }
}
