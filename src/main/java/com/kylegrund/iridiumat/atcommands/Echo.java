package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Implements the Echo command.
 */
public class Echo extends AtCommand{
    /**
     * The AT command String to send to the ISU.
     */
    private static final String COMMAND = "ATE";

    /**
     * The key in the properties associated with the enabled property.
     */
    private static final String ENABLED_KEY = "Enable";

    /**
     * Callback used to notify the ISU implementation of changes to the echo state.
     */
    private final Consumer<Boolean> setEchoEnabled;

    /**
     * Initializes a new instance of the AtCommand class.
     *
     * @param commEndpoint Callback used to have this command executed by the ISU.
     * @param getEchoEnabled A function which returns a boolean value indicating whether to expect a command echo from the ISU.
     */
    public Echo(CheckedFunction<CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException>, Map<String, String>, IOException> commEndpoint,
                Supplier<Boolean> getEchoEnabled,
                Consumer<Boolean> setEchoEnabledCallback) {
        super(commEndpoint, getEchoEnabled);
        this.setEchoEnabled = setEchoEnabledCallback;
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> toRet = new HashMap<>();
        toRet.put(ENABLED_KEY, Boolean.toString(true));
        return toRet;
    }

    @Override
    protected CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException> execute(Map<String, String> parameters) {
        return (CheckedConsumer<String, IOException> sendLine, CheckedSupplier<String, IOException> receiveLine) -> {
            // check for enable property
            if (!parameters.containsKey(ENABLED_KEY)) {
                throw new IOException("Could not execute Echo command, the " + ENABLED_KEY + " key wsa not defined.");
            }

            // parse enable property
            boolean toEnable = Boolean.parseBoolean(parameters.get(ENABLED_KEY));
            String enable = (toEnable) ? "1" : "0";

            // send command
            String toSend = COMMAND + enable + "\r\n";
            sendLine.accept(toSend);

            // check response from ISU
            String resp;

            if (this.getEchoEnabled()) {
                // check command echo was correct
                if (!toSend.startsWith(resp = receiveLine.get())) {
                    throw new IOException("Expected command echo: \"" + toSend + "\" not received, got: \"" + resp + "\" instead.");
                }
            }

            // get extra newline char
            if (!"".equals(resp = receiveLine.get())) {
                throw new IOException("Expected new line not received, got: \"" + resp + "\" instead.");
            }

            // response should simply be OK
            if (!"OK".equals(resp = receiveLine.get())) {
                throw new IOException("Expected OK response not received, got: \"" + resp + "\" instead.");
            }

            // update state in ISU implementation
            this.setEchoEnabled.accept(toEnable);

            return new HashMap<>();
        };
    }
}
