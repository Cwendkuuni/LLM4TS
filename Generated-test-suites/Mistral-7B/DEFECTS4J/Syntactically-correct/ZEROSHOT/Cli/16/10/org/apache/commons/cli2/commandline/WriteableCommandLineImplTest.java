import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option1 = new Option("o", "output", true, "output file");
        Option option2 = new Option("i", "input", true, "input file");
        Option rootOption = new Option("t", "test", noArg -> true, "run test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        // test addValue
        commandLine.addValue(option1, "output.txt");
        List<Object> values1 = commandLine.getValues(option1, null);
        Assert.assertEquals("output.txt", values1.get(0));

        commandLine.addValue(option1, "output2.txt");
        List<Object> values2 = commandLine.getValues(option1, null);
        Assert.assertEquals(2, values2.size());
        Assert.assertEquals("output.txt", values2.get(0));
        Assert.assertEquals("output2.txt", values2.get(1));

        // test addSwitch
        commandLine.addSwitch(option2, true);
        Boolean switchValue1 = commandLine.getSwitch(option2, false);
        Assert.assertTrue("Switch should be set", switchValue1);

        commandLine.addSwitch(option2, false);
        Boolean switchValue2 = commandLine.getSwitch(option2, true);
        Assert.assertFalse("Switch should not be set", switchValue2);

        // test hasOption
        Assert.assertTrue("Option should be present", commandLine.hasOption(option1));
        Assert.assertTrue("Option should be present", commandLine.hasOption(option2));

        // test getOption
        Option option = commandLine.getOption("o");
        Assert.assertEquals(option1, option);

        // test getValues
        List<Object> values = commandLine.getValues(option1, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("output.txt", values.get(0));
        Assert.assertEquals("output2.txt", values.get(1));

        // test getUndefaultedValues
        List<Object> undefaultedValues = commandLine.getUndefaultedValues(option1);
        Assert.assertEquals(0, undefaultedValues.size());

        // test getSwitch
        Boolean switchValue = commandLine.getSwitch(option2, false);
        Assert.assertFalse("Switch should not be set", switchValue);

        // test getProperty
        commandLine.addProperty(option1, "property1", "value1");
        String propertyValue = commandLine.getProperty(option1, "property1");
        Assert.assertEquals("value1", propertyValue);

        // test getProperties
        Map<String, String> properties = commandLine.getProperties(option1);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value1", properties.get("property1"));

        // test looksLikeOption
        Assert.assertTrue("Option trigger should look like option", commandLine.looksLikeOption("o"));
        Assert.assertFalse("Option trigger should not look like option", commandLine.looksLikeOption("x"));

        // test toString
        String commandLineString = commandLine.toString();
        Assert.assertTrue("Command line string should not be empty", commandLineString.length() > 0);

        // test getOptions
        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));

        // test getOptionTriggers
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue("Option trigger 'o' should be present", optionTriggers.contains("o"));
        Assert.assertTrue("Option trigger 'i' should be present", optionTriggers.contains("i"));

        // test setDefaultValues
        commandLine.setDefaultValues(option1, new ArrayList<>());
        List<Object> defaultValues = commandLine.getValues(option1, null);
        Assert.assertEquals(0, defaultValues.size());

        commandLine.setDefaultValues(option1, new ArrayList<>());
        commandLine.addValue(option1, "defaultValue1");
        commandLine.addValue(option1, "defaultValue2");
        List<Object> defaultValues2 = commandLine.getValues(option1, null);
        Assert.assertEquals(2, defaultValues2.size());
        Assert.assertEquals("defaultValue1", defaultValues2.get(0));
        Assert.assertEquals("defaultValue2", defaultValues2.get(1));

        // test setDefaultSwitch
        commandLine.setDefaultSwitch(option2, true);
        Boolean defaultSwitchValue = commandLine.getSwitch(option2, false);
        Assert.assertTrue("Default switch should be set", defaultSwitchValue);

        commandLine.setDefaultSwitch(option2, false);
        Boolean defaultSwitchValue2 = commandLine.getSwitch(option2, true);
        Assert.assertFalse("Default switch should not be set", defaultSwitchValue2);

        // test getNormalised
        List<String> normalised = commandLine.getNormalised();
        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("-o output.txt", normalised.get(0));
        Assert.assertEquals("-o output2.txt", normalised.get(1));
        Assert.assertEquals("-i", normalised.get(2));
    }
}