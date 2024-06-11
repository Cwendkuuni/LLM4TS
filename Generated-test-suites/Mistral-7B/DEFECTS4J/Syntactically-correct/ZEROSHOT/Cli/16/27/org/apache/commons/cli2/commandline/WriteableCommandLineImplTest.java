import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        List arguments = new ArrayList();
        arguments.add("--rootOption");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Test addOption method
        Option option1 = new Option("opt1", "option1", true, "Option 1");
        commandLine.addOption(option1);
        Assert.assertEquals(option1, commandLine.getOption("opt1"));
        Assert.assertTrue(commandLine.hasOption(option1));

        Option option2 = new Option("opt2", "option2", false, "Option 2");
        commandLine.addOption(option2);
        Assert.assertEquals(option2, commandLine.getOption("opt2"));
        Assert.assertTrue(commandLine.hasOption(option2));

        // Test addValue method
        commandLine.addValue(option1, "value1");
        List values = commandLine.getValues(option1, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        commandLine.addValue(option1, "value2");
        values = commandLine.getValues(option1, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        // Test addSwitch method
        commandLine.addSwitch(option2, true);
        Boolean switchValue = commandLine.getSwitch(option2, false);
        Assert.assertTrue(switchValue);

        // Test hasOption method
        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertFalse(commandLine.hasOption(new Option("nonexistent", "nonexistent", false, "Nonexistent option")));

        // Test getOption method
        Option retrievedOption1 = commandLine.getOption("opt1");
        Assert.assertEquals(option1, retrievedOption1);

        // Test getValues method
        List retrievedValues = commandLine.getValues(option1, null);
        Assert.assertEquals(2, retrievedValues.size());
        Assert.assertEquals("value1", retrievedValues.get(0));
        Assert.assertEquals("value2", retrievedValues.get(1));

        // Test getUndefaultedValues method
        List unfaultedValues = commandLine.getUndefaultedValues(option1);
        Assert.assertEquals(2, unfaultedValues.size());
        Assert.assertEquals("value1", unfaultedValues.get(0));
        Assert.assertEquals("value2", unfaultedValues.get(1));

        // Test getSwitch method
        Boolean retrievedSwitchValue = commandLine.getSwitch(option2, false);
        Assert.assertTrue(retrievedSwitchValue);

        // Test getProperty method
        commandLine.addProperty(option1, "property1", "propertyValue1");
        String propertyValue = commandLine.getProperty(option1, "propertyKey", "defaultValue");
        Assert.assertEquals("propertyValue1", propertyValue);

        // Test addProperty method
        commandLine.addProperty(option1, "property2", "propertyValue2");
        Set properties = commandLine.getProperties(option1);
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));

        // Test getProperties method
        Set allProperties = commandLine.getProperties();
        Assert.assertEquals(0, allProperties.size());

        // Test looksLikeOption method
        Assert.assertTrue(commandLine.looksLikeOption("--opt1"));
        Assert.assertFalse(commandLine.looksLikeOption("--nonexistent"));

        // Test toString method
        String commandLineString = commandLine.toString();
        Assert.assertEquals("--rootOption --opt1 value1 value2", commandLineString);

        // Test getOptions method
        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));

        // Test getOptionTriggers method
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("opt1"));
        Assert.assertTrue(optionTriggers.contains("opt2"));

        // Test setDefaultValues method
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option1, defaultValues);
        values = commandLine.getValues(option1, null);
        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("defaultValue1", values.get(2));

        // Test setDefaultSwitch method
        commandLine.setDefaultSwitch(option2, false);
        switchValue = commandLine.getSwitch(option2, true);
        Assert.assertFalse(switchValue);

        // Test getNormalised method
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("--rootOption", normalised.get(0).toString());
        Assert.assertEquals("--opt1", normalised.get(1).toString());
        Assert.assertEquals("value1", normalised.get(2).toString());
    }
}