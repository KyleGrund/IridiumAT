package com.kylegrund.iridiumat;

import com.kylegrund.iridiumat.atcommands.*;

import java.io.IOException;
import java.util.*;

public class Motorola9575 extends SubscriberUnit {
    /**
     * The AT commands available for this ISU.
     */
    private final Map<String, AtCommand> commands;

    /**
     * The function which sends a line of text to the remote ISU.
     */
    private final CheckedConsumer<String, IOException> sendLine;

    /**
     * The function which receives a line of text from the ISU.
     */
    private final CheckedSupplier<String, IOException> receiveLine;

    /**
     * The function to call to dispose underlying unmanaged resources.
     */
    private final Runnable disposeMethod;

    /**
     * Initializes a new instance of the Motorola9575 class.
     *
     * @param sendLine    A function which sends the provided String to the associated ISU.
     * @param receiveLine A function which returns a String read from the associated ISU.
     * @param disposeMethod The method to call to dispose unmanaged resources.
     */
    Motorola9575(CheckedConsumer<String, IOException> sendLine, CheckedSupplier<String, IOException> receiveLine, Runnable disposeMethod) {
        this.sendLine = sendLine;
        this.receiveLine = receiveLine;
        this.disposeMethod = disposeMethod;

        Map<String, AtCommand> commands = new HashMap<>();

        // add all commands the 9575 radio supports
        commands.put(BatteryCharge.class.getName(), new BatteryCharge(this::execute));
        commands.put(DisplayRegisters.class.getName(), new DisplayRegisters(this::execute));
        commands.put(FactoryReset.class.getName(), new FactoryReset(this::execute));
        commands.put(ModelIdentification.class.getName(), new ModelIdentification(this::execute));
        // commands.put(PowerPhone.class.getName(), new PowerPhone(this::execute));
        // commands.put(RadioActivity.class.getName(), new RadioActivity(this::execute));
        commands.put(Reboot.class.getName(), new Reboot(this::execute));

        this.commands = Collections.unmodifiableMap(commands);
    }

    @Override
    public Map<String, AtCommand> getCommands() {
        return this.commands;
    }

    @Override
    public void close() {
        // run the dispose method to clean up unmanaged resources
        this.disposeMethod.run();
    }

    /**
     * Function providing the ability to synchronously access communications when executing commands.
     * @param func The function which actually executes the command.
     * @return The result of executing the command.
     * @throws IOException If an IOException occurs while executing the command.
     */
    private synchronized Map<String, String> execute(CheckedDoubleFunction<CheckedConsumer<String, IOException>, CheckedSupplier<String, IOException>, Map<String, String>, IOException> func) throws IOException {
        return func.accept(this.sendLine, this.receiveLine);
    }
}
