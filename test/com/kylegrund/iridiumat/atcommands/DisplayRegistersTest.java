package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements test for the Display Registers command.
 */
public class DisplayRegistersTest extends AtCommandTest {
    @Override
    public void testCommand(AtCommand command) throws Exception{
        command.executeCommand(command.getParameters());
    }
}
