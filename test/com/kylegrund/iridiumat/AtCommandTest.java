package com.kylegrund.iridiumat;

import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class providing testing dispatch for AtCommand implementing classes.
 */
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

        System.out.println("Dispatching for class: " + className);

        // try to get instantiate new instance
        AtCommandTest cmdInstance = null;
        try {
            cmdInstance = (AtCommandTest)found.getConstructor().newInstance();
        } catch (Exception e) {
            Assert.fail("Could not instantiate new instance of the class: \"" + className + "\".");
        }

        // try to find test method
        Method testMethod = null;
        try {
            testMethod = found.getMethod("testCommand", AtCommand.class);
        } catch (Exception e) {
            Assert.fail("Could not find testCommand method on test class type: \"" + found.getName() + "\".");
        }

        // try to execute test method
        try {
            testMethod.invoke(cmdInstance, command);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            Assert.fail("Exception executing test method for class: \"" + found.getName() + "\".");
        }
    }
}
