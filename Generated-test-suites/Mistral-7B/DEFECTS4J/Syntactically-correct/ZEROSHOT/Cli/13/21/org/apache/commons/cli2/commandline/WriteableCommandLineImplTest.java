import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Test constructor
        Option rootOption = new Option("t", "test", false, "Perform a test");
        List arguments = new ArrayList();
        arguments.add("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Test addOption
        Option option1 = new Option("i", "input", true, "Input file");
        commandLine.addOption(option1);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option1, commandLine.getOption(option1.getPreferredName()));

        // Test addValue
        commandLine.addValue(option1, "input.txt");
        List values = commandLine.getValues(option1, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("input.txt", values.get(0));

        // Test addSwitch
        Option option2 = new Option("d", "debug", false, "Debug mode");
        commandLine.addSwitch(option2, true);
        Assert.assertTrue(commandLine.getSwitch(option2, false));

        // Test hasOption
        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertFalse(commandLine.hasOption(new Option("notAnOption", false, false, "Not an option")));

        // Test getOption
        Option retrievedOption1 = commandLine.getOption(option1.getPreferredName());
        Assert.assertEquals(option1, retrievedOption1);

        // Test getValues
        List retrievedValues = commandLine.getValues(option1, null);
        Assert.assertEquals(1, retrievedValues.size());
        Assert.assertEquals("input.txt", retrievedValues.get(0));

        // Test getSwitch
        Assert.assertTrue(commandLine.getSwitch(option2, false));

        // Test addProperty
        commandLine.addProperty("test.property", "testValue");
        String property = commandLine.getProperty("test.property", "defaultValue");
        Assert.assertEquals("testValue", property);

        // Test getProperty
        property = commandLine.getProperty("nonexistent.property", "defaultValue");
        Assert.assertEquals("defaultValue", property);

        // Test getProperties
        Set properties = commandLine.getProperties();
        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.contains("test.property"));

        // Test looksLikeOption
        Assert.assertTrue(commandLine.looksLikeOption("--test"));
        Assert.assertFalse(commandLine.looksLikeOption("--nonexistent"));

        // Test toString
        String commandLineString = commandLine.toString();
        Assert.assertEquals("testOption test --input input.txt", commandLineString);

        // Test getOptions
        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Option option = (Option) options.get(0);
        Assert.assertEquals(option1, option);

        // Test getOptionTriggers
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains(option1.getPreferredName()));

        // Test setDefaultValues
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option1, defaultValues);
        values = commandLine.getValues(option1, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));

        // Test setDefaultSwitch
        commandLine.setDefaultSwitch(option2, false);
        Assert.assertFalse(commandLine.getSwitch(option2, false));

        // Test getNormalised
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("test", ((Option) normalised.get(0)).getLongOpt());
        Assert.assertEquals("--input", ((Option) normalised.get(1)).getLongOpt());
    }
}