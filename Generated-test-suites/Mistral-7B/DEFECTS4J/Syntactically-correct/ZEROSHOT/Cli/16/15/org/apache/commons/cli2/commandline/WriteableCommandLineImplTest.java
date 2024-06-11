import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("opt", "option", true, "Option description");
        List arguments = new ArrayList();
        arguments.add("--opt value1");
        arguments.add("--opt value2");
        arguments.add("--no-opt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Test addOption method
        Option option = new Option("t", "test", false, "Test option");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));

        // Test getOption method
        Option testOption = commandLine.getOption("t");
        Assert.assertNotNull(testOption);

        // Test getValues method for an argument
        Option argOption = new Option("a", "argument", true, "Argument option");
        commandLine.addOption(argOption);
        commandLine.addValue(argOption, "value3");
        List values = commandLine.getValues(argOption, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value3", values.get(0));

        // Test getValues method for a switch
        Option switchOption = new Option("s", "switch", false, "Switch option");
        commandLine.addOption(switchOption);
        Assert.assertFalse(commandLine.getSwitch(switchOption, Boolean.FALSE));
        commandLine.addSwitch(switchOption, true);
        Assert.assertTrue(commandLine.getSwitch(switchOption, Boolean.FALSE));

        // Test getProperty method for a property option
        Option propertyOption = new Option("p", "property", true, "Property option");
        commandLine.addOption(propertyOption);
        commandLine.addProperty(propertyOption, "property.name", "property.value");
        String propertyValue = commandLine.getProperty(propertyOption, "defaultValue");
        Assert.assertEquals("property.value", propertyValue);

        // Test getProperties method for a property option
        Map properties = commandLine.getProperties(propertyOption);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("property.name", properties.keySet().iterator().next());

        // Test looksLikeOption method
        Assert.assertTrue(commandLine.looksLikeOption("--opt"));
        Assert.assertFalse(commandLine.looksLikeOption("--no-opt"));

        // Test toString method
        String commandLineString = commandLine.toString();
        Assert.assertEquals("--opt value1 --opt value2 --no-opt --t", commandLineString);

        // Test getOptions method
        List options = commandLine.getOptions();
        Assert.assertEquals(3, options.size());
        Option optionFromList = (Option) options.get(1);
        Assert.assertEquals(option, optionFromList);

        // Test getOptionTriggers method
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(3, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("t"));

        // Test setDefaultValues method
        commandLine.setDefaultValues(argOption, Arrays.asList("defaultValue1"));
        values = commandLine.getValues(argOption, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));

        // Test setDefaultSwitch method
        commandLine.setDefaultSwitch(switchOption, Boolean.TRUE);
        Assert.assertTrue(commandLine.getSwitch(switchOption, Boolean.FALSE));

        // Test getNormalised method
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(4, normalised.size());
        Assert.assertEquals("--opt value1", normalised.get(0).toString());
        Assert.assertEquals("--opt value2", normalised.get(1).toString());
        Assert.assertEquals("--no-opt", normalised.get(2).toString());
        Assert.assertEquals("--t", normalised.get(3).toString());
    }
}