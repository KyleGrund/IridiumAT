package com.kylegrund.iridiumat;

import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;

public abstract class AtCommandTest {
    /**
     * Runs a test for the given command
     * @param command The command instance to test.
     */
    public abstract void testCommand(AtCommand command) throws Exception;

    /**
     * Dispatches the test to the appropriate class using reflection.
     * @param command The command to test.
     */
    static void Dispatch(AtCommand command) {
        // find the class
        String className = command.getClass().getName();
        Class found = null;
        try {
            found = Class.forName(className + "Test");
        } catch (ClassNotFoundException e) {
            Assert.fail("Could not find test class for command of type: \"" + className + "\".");
        }

        // try to execute test method
        try {
            found.getMethod("testCommand", AtCommand.class).invoke(found.getConstructor().newInstance(), command);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Assert.fail("Could not find testCommand method on test class type: \"" + found.getName() + "\".");
        } catch (Exception e) {
            Assert.fail("Test failed with Exception: \"" + e.getMessage() + "\".");
        }
    }
}
