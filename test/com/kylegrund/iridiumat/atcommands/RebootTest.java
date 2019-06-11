package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.AtCommand;
import com.kylegrund.iridiumat.AtCommandTest;

/**
 * Implements tests for the Reboot command.
 */
public class RebootTest extends AtCommandTest {
    @Override
    public void testCommand(AtCommand command) throws Exception{
        command.executeCommand(command.getParameters());
    }
}
