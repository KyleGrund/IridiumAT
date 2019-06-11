package com.kylegrund.iridiumat.atcommands;

import com.kylegrund.iridiumat.AtCommand;
import com.kylegrund.iridiumat.AtCommandTest;

import java.util.Map;

/**
 * Implements test for the Radio Activity command.
 */
public class BatteryChargeTest extends AtCommandTest {
    @Override
    public void testCommand(AtCommand command) throws Exception{
        Map<String, String> params = command.executeCommand(command.getParameters());

        if (params.size() != 2 && params.size() != 4) {
            throw new Exception("Battery Charge command did not return two or four values.");
        }

        if (!params.containsKey("bcs")) {
            throw new Exception("Battery Charge command did not return a bcs value.");
        }

        String bcsVal = params.get("bcs");
        if (!bcsVal.equals("000") && !bcsVal.equals("001") && !bcsVal.equals("002") && !bcsVal.equals("003")) {
            throw new Exception("Battery Charge bcs value was not valid.");
        }

        if (!params.containsKey("bcl")) {
            throw new Exception("Battery Charge command did not return a bcl value.");
        }

        String bclVal = params.get("bcl");
        if (bclVal.length() != 3) {
            throw new Exception("Battery Charge bcl value was not valid length.");
        }

        try {
            int bclNum = Integer.parseInt(bclVal);
            if (bclNum < 0 || bclNum > 100) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception("Battery Charge bcl value was not valid.");
        }

        if (params.size() == 4) {
            if (!params.containsKey("ccs")) {
                throw new Exception("Battery Charge command did not return a ccs value.");
            }

            String ccsVal = params.get("ccs");
            if (!ccsVal.equals("001") && !ccsVal.equals("002") && !ccsVal.equals("003")) {
                throw new Exception("Battery Charge ccs value was not valid.");
            }

            if (!params.containsKey("bal")) {
                throw new Exception("Battery Charge command did not return a bal value.");
            }

            String balVal = params.get("bal");
            if (balVal.length() != 3) {
                throw new Exception("Battery Charge bal value was not valid length.");
            }

            try {
                int balNum = Integer.parseInt(bclVal);
                if (balNum < 0 || balNum > 100) {
                    throw new Exception();
                }
            } catch (Exception e) {
                throw new Exception("Battery Charge bal value was not valid.");
            }
        }
    }
}
