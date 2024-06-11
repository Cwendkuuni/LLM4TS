import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option rootOption = new Option("root", "root", false, "Root option");
        List<String> arguments = new ArrayList<>();
        arguments.add("--root");
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // When

        // Test adding options
        Option option1 = new Option("opt1", "opt1", true, "Option 1");
        commandLine.addOption(option1);
        Assert.assertTrue(commandLine.hasOption(option1));

        Option option2 = new Option("opt2", "opt2", false, "Option 2 with argument");
        Argument argument = new Argument(0, "arg");
        option2.setArgument(argument);
        commandLine.addOption(option2);
        Assert.assertTrue(commandLine.hasOption(option2));
        Assert.assertEquals(1, commandLine.getValues(option2, null).size());

        // Test adding values
        commandLine.addValue(option1, "value1");
        Assert.assertEquals("value1", commandLine.getValues(option1, null).get(0));

        commandLine.addValue(option2, "value2");
        Assert.assertEquals("value2", commandLine.getValues(option2, null).get(0));

        // Test adding switches
        commandLine.addSwitch(option1, true);
        Assert.assertTrue(commandLine.getSwitch(option1, false));

        // Test getting properties
        Option propertyOption = new Option("prop", "prop", false, "Property option");
        commandLine.addProperty(propertyOption, "property", "value");
        Assert.assertEquals("value", commandLine.getProperty(propertyOption, "defaultValue"));

        // Test getting option trigger
        Option triggerOption = new Option("trigger", "trigger", false, "Trigger option");
        commandLine.addOption(triggerOption);
        Assert.assertEquals(triggerOption, commandLine.getOption("trigger"));

        // Test getting options
        List options = commandLine.getOptions();
        Assert.assertEquals(3, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
        Assert.assertEquals(triggerOption, options.get(2));

        // Test getting option triggers
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(3, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("opt1"));
        Assert.assertTrue(optionTriggers.contains("opt2"));
        Assert.assertTrue(optionTriggers.contains("trigger"));

        // Test getting normalised arguments
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("--root", normalised.get(0));
        Assert.assertEquals("arg1", normalised.get(1));
        Assert.assertEquals("arg2", normalised.get(2));

        // Then
        // The test passed
    }
}