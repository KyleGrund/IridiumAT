package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements tests for the Display Registers command.
 */
public class ModelIdentificationTest extends AtCommandTest {
    @Override
    public void testCommand(AtCommand command) throws Exception{
        command.executeCommand(command.getParameters());
    }
}
