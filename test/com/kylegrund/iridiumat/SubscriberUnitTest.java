package com.kylegrund.iridiumat;

import org.junit.Assert;
import org.junit.Before;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class SubscriberUnitTest {
    /**
     * The list of subscriber units to test.
     */
    private List<SubscriberUnit> toTest;

    /**
     * Runs before unit tests to set up class.
     * @throws Exception if no subscriber units can be set up.
     */
    @Before
    public void setUp() throws Exception {
        toTest = SubscriberUnit.getSubscriberUnits();
        if (toTest.size() < 1) {
            Assert.fail("No subscriber units found to test.");
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
            Assert.assertTrue("Subscriber has one or more commands.", unit.getCommands().size() > 0);
        }

        for (SubscriberUnit unit : toTest) {
            Collection<AtCommand> commands = unit.getCommands().values();
            for (AtCommand command : commands) {
                AtCommandTest.Dispatch(command);
            }
        }
    }
}