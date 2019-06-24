package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Implements the Battery Charge command.
 */
public class BatteryCharge extends AtCommand{
    /**
     * The AT command String to send to the ISU.
     */
    private static final String COMMAND = "AT+CBC\r\n";

    /**
     * The expected response prefix from the ISU.
     */
    private static final String RESP = "+CBC";

    /**
     * Initializes a new instance of the BatteryCharge class.
     *
     * @param commEndpoint Callback used to have this command executed by the ISU.
     * @param getEchoEnabled A function which returns a boolean value indicating whether to expect a command echo from the ISU.
     */
    public BatteryCharge(CheckedFunction<CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException>, Map<String, String>, IOException> commEndpoint,
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
            // send command
            sendLine.accept(COMMAND);

            // check echo response from ISU
            String resp;
            if (!COMMAND.startsWith(resp = receiveLine.get())) {
                throw new IOException("Expected command echo: \"" + COMMAND + "\" not received, got: \"" + resp + "\" instead.");
            }

            // get extra newline char
            if (!"".equals(resp = receiveLine.get())) {
                throw new IOException("Expected new line not received, got: \"" + resp + "\" instead.");
            }

            // parse response
            String[] vals = (resp = receiveLine.get()).split(":");
            if (vals.length != 2) {
                throw new IOException("Expected response form of [cmd]:[params] not received, got: \"" + resp + "\" instead.");
            }

            String[] params = vals[1].split(",");
            if (params.length != 2 && params.length != 4) {
                throw new IOException("Expected response to contain two or four parameters, got: \"" + resp + "\" instead.");
            }

            Map<String, String> result = new HashMap<>();
            result.put("bcs", params[0]);
            result.put("bcl", params[1]);

            if (params.length == 4) {
                result.put("ccs", params[2]);
                result.put("bal", params[3]);
            }

            /*
            // get extra newline char
            if (!"".equals(resp = receiveLine.get())) {
                throw new IOException("Expected new line not received, got: \"" + resp + "\" instead.");
            }*/

            // response should simply be OK
            if (!"OK".equals(resp = receiveLine.get())) {
                throw new IOException("Expected OK response not received, got: \"" + resp + "\" instead.");
            }

            return result;
        };
    }
}
