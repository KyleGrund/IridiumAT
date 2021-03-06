package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Implements the Power Phone command.
 */
public class PowerPhone extends AtCommand{
    /**
     * The AT command String to send to the ISU.
     */
    private static final String COMMAND = "AT*P";

    /**
     * The key in the properties associated with the enabled property.
     */
    private static final String ENABLED_KEY = "Enable";

    /**
     * Initializes a new instance of the PowerPhone class.
     *
     * @param commEndpoint Callback used to have this command executed by the ISU.
     * @param getEchoEnabled A function which returns a boolean value indicating whether to expect a command echo from the ISU.
     */
    public PowerPhone(CheckedFunction<CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException>, Map<String, String>, IOException> commEndpoint,
                      Supplier<Boolean> getEchoEnabled) {
        super(commEndpoint, getEchoEnabled);
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
                throw new IOException("Could not execute Power Phone command, the " + ENABLED_KEY + " key wsa not defined.");
            }

            // parse enable property
            int enable = 0;
            if (Boolean.parseBoolean(parameters.get(ENABLED_KEY))) {
                enable = 1;
            }

            // send command
            String toSend = COMMAND + enable + "\r\n";
            sendLine.accept(toSend);

            // check response from ISU
            String resp;
            if (!toSend.startsWith(resp = receiveLine.get())) {
                throw new IOException("Expected command echo: \"" + toSend + "\" not received, got: \"" + resp + "\" instead.");
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
