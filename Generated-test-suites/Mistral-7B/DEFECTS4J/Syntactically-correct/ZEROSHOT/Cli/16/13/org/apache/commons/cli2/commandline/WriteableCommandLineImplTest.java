import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
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
        Option rootOption = new Option("t", "test", true, "Test option")
                .required(false)
                .longOpt("test-long")
                .defaultValue("default")
                .addArgument("arg")
                .argName("argument")
                .triggers(new String[]{"tr1", "tr2"});

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1", "arg2"));

        // Test addOption
        Option opt1 = new Option("o1", "opt1")
                .required(false)
                .longOpt("opt1-long");
        writeableCommandLine.addOption(opt1);
        Assert.assertTrue(writeableCommandLine.hasOption(opt1));
        Assert.assertEquals(1, writeableCommandLine.getOptions().size());

        // Test addValue
        writeableCommandLine.addValue(rootOption, "value1");
        List values = writeableCommandLine.getValues(rootOption, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        // Test addSwitch
        writeableCommandLine.addSwitch(rootOption, true);
        Boolean switchValue = writeableCommandLine.getSwitch(rootOption, false);
        Assert.assertTrue(switchValue);

        // Test hasOption
        Option opt2 = new Option("o2", "opt2")
                .required(false)
                .longOpt("opt2-long");
        writeableCommandLine.addOption(opt2);
        Assert.assertFalse(writeableCommandLine.hasOption(new Option("o3", "opt3")));

        // Test getOption
        Option option = writeableCommandLine.getOption("opt1");
        Assert.assertNotNull(option);

        // Test getValues
        List values1 = writeableCommandLine.getValues(rootOption, null);
        Assert.assertEquals(2, values1.size());
        Assert.assertEquals("arg1", values1.get(0));
        Assert.assertEquals("arg2", values1.get(1));

        List defaultValues = writeableCommandLine.getDefaultValues(rootOption);
        Assert.assertNull(defaultValues);

        // Test getUndefaultedValues
        List unfaultedValues = writeableCommandLine.getUndefaultedValues(rootOption);
        Assert.assertEquals(2, unfaultedValues.size());
        Assert.assertEquals("arg1", unfaultedValues.get(0));
        Assert.assertEquals("arg2", unfaultedValues.get(1));

        // Test getSwitch
        Boolean switchValue1 = writeableCommandLine.getSwitch(rootOption, false);
        Assert.assertTrue(switchValue1);

        writeableCommandLine.addSwitch(rootOption, false);
        Boolean switchValue2 = writeableCommandLine.getSwitch(rootOption, true);
        Assert.assertFalse(switchValue2);

        // Test getProperty
        writeableCommandLine.addProperty(rootOption, "prop1", "value1");
        String propertyValue = writeableCommandLine.getProperty("prop1");
        Assert.assertEquals("value1", propertyValue);

        // Test addProperty
        writeableCommandLine.addProperty(rootOption, "prop2", "value2");
        Map<String, String> properties = writeableCommandLine.getProperties(rootOption);
        Assert.assertEquals("value1", properties.get("prop1"));
        Assert.assertEquals("value2", properties.get("prop2"));

        // Test getProperties
        Map<String, String> allProperties = writeableCommandLine.getProperties();
        Assert.assertEquals(2, allProperties.size());

        // Test looksLikeOption
        Assert.assertTrue(writeableCommandLine.looksLikeOption("t"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("test"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("test2"));

        // Test toString
        String commandLineString = writeableCommandLine.toString();
        String expected = "arg1 arg2";
        Assert.assertEquals(expected, commandLineString);

        // Test getOptions
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Option option1 = (Option) options.get(0);
        Assert.assertEquals("o1", option1.getLongOpt());
        Option option2 = (Option) options.get(1);
        Assert.assertEquals("opt1-long", option2.getLongOpt());

        // Test getOptionTriggers
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(3, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("tr1", "tr2", "test")));

        // Test setDefaultValues
        writeableCommandLine.setDefaultValues(rootOption, Arrays.asList("default1", "default2"));
        List newValues = writeableCommandLine.getValues(rootOption, null);
        Assert.assertEquals(4, newValues.size());
        Assert.assertEquals("arg1", newValues.get(0));
        Assert.assertEquals("arg2", newValues.get(1));
        Assert.assertEquals("default1", newValues.get(2));
        Assert.assertEquals("default2", newValues.get(3));

        // Test setDefaultSwitch
        writeableCommandLine.setDefaultSwitch(rootOption, Boolean.FALSE);
        Boolean newSwitchValue = writeableCommandLine.getSwitch(rootOption, null);
        Assert.assertFalse(newSwitchValue);

        // Test getNormalised
        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("arg1", normalised.get(0));
        Assert.assertEquals("arg2", normalised.get(1));
    }
}