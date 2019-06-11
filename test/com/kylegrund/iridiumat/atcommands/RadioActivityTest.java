package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements test for the Radio Activity command.
 */
public class RadioActivityTest extends AtCommandTest {
    @Override
    public void testCommand(AtCommand command) throws Exception{
        Map<String, String> params = command.getParameters();
        params.put("Enable", "1");
        command.executeCommand(params);
        params.put("Enable", "0");
        command.executeCommand(params);
        params.put("Enable", "1");
        command.executeCommand(params);
    }
}
