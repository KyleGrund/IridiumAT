import com.kylegrund.iridiumat.AtCommand;
import com.kylegrund.iridiumat.ResponseParseError;
import com.kylegrund.iridiumat.SubscriberUnit;
import com.kylegrund.iridiumat.atcommands.DisplayRegisters;
import com.kylegrund.iridiumat.atcommands.ModelIdentification;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    /***
     * Main entry point for the module.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        List<SubscriberUnit> found = SubscriberUnit.getSubscriberUnits();
        System.out.println("Found " + found.size() + " subscriber units.");

        for (SubscriberUnit unit : found) {
            try {
                System.out.println("---- ISU Information ----");

                System.out.println("-- Model --");
                AtCommand identification = unit.getCommands().get(ModelIdentification.class.getName());
                System.out.println(identification.executeCommand(identification.getParameters()));

                System.out.println("-- System Registers --");
                AtCommand registers = unit.getCommands().get(DisplayRegisters.class.getName());
                System.out.println(registers.executeCommand(registers.getParameters()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
