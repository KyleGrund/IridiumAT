package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements tests for the Send SMS Message command.
 */
public class SendSMSMessageTest extends AtCommandTest{
    @Override
    public void testCommand(AtCommand command) throws Exception{
        command.executeCommand(command.getParameters());
    }
}
