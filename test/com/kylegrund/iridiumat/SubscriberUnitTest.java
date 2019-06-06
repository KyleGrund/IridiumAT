package com.kylegrund.iridiumat;

import com.kylegrund.iridiumat.atcommands.DisplayRegisters;
import com.kylegrund.iridiumat.atcommands.ModelIdentification;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SubscriberUnitTest {
    /**
     * The list of subscriber units to test.
     */
    private List<SubscriberUnit> toTest;

    /**
     * Runs before unit tests to set up class.
     */
    @Before
    public void setUp() {
        toTest = SubscriberUnit.getSubscriberUnits();
        if (toTest.size() < 1) {
            Assert.fail("No subscriber units found to test.");
        }
    }

    /**
     * Runs after unit tests to tear down class.
     */
    @After
    public void cleanUp() {
        for (SubscriberUnit su : toTest) {
            su.close();
        }
    }

    /**
     * Tests the getCommands method on the subscriber units.
     */
    @org.junit.Test
    public void getCommands() {
        for (SubscriberUnit unit : toTest) {
            Assert.assertTrue("Subscriber has one or more commands.", unit.getCommands().size() > 0);
        }
    }

    /**
     * Tests the getCommands method on the subscriber units.
     */
    @org.junit.Test
    public void testAllCommands() {
        for (SubscriberUnit unit : toTest) {
            Assert.assertTrue("Subscriber unit must have one or more commands.", unit.getCommands().size() > 0);
        }

        for (SubscriberUnit unit : toTest) {
            Collection<AtCommand> commands = unit.getCommands().values();
            for (AtCommand command : commands) {
                AtCommandTest.Dispatch(command);
            }
        }
    }

    /**
     * Tests for interleaving errors by running long and short duration command simultaniously.
     */
    @org.junit.Test
    public void testForCommandInterleaving() {
        for (SubscriberUnit unit : toTest) {
            final AtCommand getID = unit.getCommands().get(ModelIdentification.class.getTypeName());
            final AtCommand dispRegisters = unit.getCommands().get(DisplayRegisters.class.getTypeName());

            final Map<String, Boolean> statuses = new HashMap<>();
            statuses.put("ID", true);
            statuses.put("REG", true);

            Thread idThread = new Thread(() -> {
                for (int i = 0; i < 15; i++) {
                    try {
                        System.out.println("ID: Started.");
                        Map<String, String> result = getID.executeCommand(getID.getParameters());
                        System.out.println("ID: Resulted, " + result.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                        statuses.put("ID", false);
                        break;
                    }
                }
            });
            idThread.setName("ID Command");

            Thread regThread = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    try {
                        System.out.println("REG: Started.");
                        Map<String, String> result = dispRegisters.executeCommand(dispRegisters.getParameters());
                        System.out.println("REG: Resulted, " + result.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                        statuses.put("REG", false);
                        break;
                    }
                }
            });
            regThread.setName("Reg Display Command");

            regThread.start();
            idThread.start();

            try {
                regThread.join();
                idThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Assert.fail("Interrupted exception waiting for test completion.");
            }

            Assert.assertTrue("Register read thread status.", statuses.get("REG"));
            Assert.assertTrue("ID read thread status.", statuses.get("ID"));
        }
    }
}