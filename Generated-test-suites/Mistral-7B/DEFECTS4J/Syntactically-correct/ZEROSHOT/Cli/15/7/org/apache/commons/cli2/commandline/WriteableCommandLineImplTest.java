import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file");
        List arguments = new ArrayList();
        arguments.add("test.txt");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);

        // test adding option
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Option optionFromCommandLine = commandLine.getOption("o");
        Assert.assertEquals(option, optionFromCommandLine);

        // test adding value
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals("test.txt", values.get(0));
        Assert.assertEquals(1, values.size());

        // test adding switch
        commandLine.addSwitch(option, true);
        boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertEquals(true, switchValue);

        // test hasOption
        Assert.assertTrue(commandLine.hasOption(option));

        // test getOption
        Option optionFromMap = commandLine.getOption("o");
        Assert.assertEquals(option, optionFromMap);

        // test getValues
        List defaultValues = new ArrayList();
        defaultValues.add("default.txt");
        commandLine.setDefaultValues(option, defaultValues);
        List valuesFromDefault = commandLine.getValues(option, defaultValues);
        Assert.assertEquals(1, valuesFromDefault.size());
        Assert.assertEquals("default.txt", valuesFromDefault.get(0));

        // test getUndefaultedValues
        List valuesFromCommandLine = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, valuesFromCommandLine.size());

        // test getSwitch
        boolean defaultSwitch = false;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean switchFromDefault = commandLine.getSwitch(option, defaultSwitch);
        Assert.assertEquals(defaultSwitch, switchFromDefault.booleanValue());

        // test getProperty
        Option propertyOption = new Option("p", "property", true, "Property name");
        commandLine.addOption(propertyOption);
        commandLine.addProperty(propertyOption, "color", "red");
        String propertyValue = commandLine.getProperty(propertyOption, "blue");
        Assert.assertEquals("red", propertyValue);

        // test getProperties
        commandLine.addProperty(propertyOption, "size", "medium");
        Set propertyNames = commandLine.getProperties(propertyOption);
        Assert.assertEquals(2, propertyNames.size());
        Assert.assertTrue(propertyNames.contains("color"));
        Assert.assertTrue(propertyNames.contains("size"));

        // test looksLikeOption
        Assert.assertTrue(commandLine.looksLikeOption("o"));
        Assert.assertFalse(commandLine.looksLikeOption("t"));

        // test toString
        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "o output test.txt";
        Assert.assertEquals(expectedCommandLineString, commandLineString);

        // test getOptionTriggers
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("o"));

        // test setDefaultValues
        List newDefaultValues = new ArrayList();
        newDefaultValues.add("newDefault.txt");
        commandLine.setDefaultValues(option, newDefaultValues);
        List newValuesFromDefault = commandLine.getValues(option, defaultValues);
        Assert.assertEquals("newDefault.txt", newValuesFromDefault.get(0));

        // test setDefaultSwitch
        boolean newDefaultSwitch = true;
        commandLine.setDefaultSwitch(option, newDefaultSwitch);
        Boolean newSwitchFromDefault = commandLine.getSwitch(option, defaultSwitch);
        Assert.assertEquals(newDefaultSwitch, newSwitchFromDefault.booleanValue());

        // test getNormalised
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(1, normalised.size());
        Assert.assertEquals("test.txt", normalised.get(0));
    }
}