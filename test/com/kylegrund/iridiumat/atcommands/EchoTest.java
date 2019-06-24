package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.AtCommand;
import com.kylegrund.iridiumat.AtCommandTest;

import java.util.Map;

/**
 * Implements test for the Echo command.
 */
public class EchoTest extends AtCommandTest {
    @Override
    public void testCommand(AtCommand command) throws Exception{
        Map<String, String> params = command.getParameters();
        params.put("Enable", "true");
        command.executeCommand(params);
        params.put("Enable", "false");
        command.executeCommand(params);
        params.put("Enable", "true");
        command.executeCommand(params);
    }
}
