import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option1 = new Option("o", "option1")
                .required(false)
                .longOpt("longOption1")
                .valueSeparator('=')
                .valuesSeparator(',')
                .argName("value1")
                .desc("Test option 1")
                .build();

        Option option2 = new Option("s", "switch")
                .required(false)
                .shortOpt("s")
                .desc("Test switch")
                .build();

        Option rootOption = new Option("t", "test")
                .required(true)
                .args(option1, option2)
                .build();

        List arguments = new ArrayList();
        arguments.add("--test");
        arguments.add("--option1=value1,value2");
        arguments.add("--longOption1=value3");
        arguments.add("--switch");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Test hasOption
        Assert.assertTrue(writeableCommandLine.hasOption(option1));
        Assert.assertTrue(writeableCommandLine.hasOption(option2));
        Assert.assertFalse(writeableCommandLine.hasOption(new Option("notAnOption", "notAnOption")));

        // Test getOption
        Option testOption = writeableCommandLine.getOption("test");
        Assert.assertEquals(option1, testOption.getValue(option1));
        Assert.assertEquals(option2, testOption.getValue(option2));

        // Test getValues
        List valuesOption1 = writeableCommandLine.getValues(option1, null);
        Assert.assertEquals(2, valuesOption1.size());
        Assert.assertEquals("value1", valuesOption1.get(0));
        Assert.assertEquals("value2", valuesOption1.get(1));

        // Test getUndefaultedValues
        List unfaultedValuesOption1 = writeableCommandLine.getUndefaultedValues(option1);
        Assert.assertEquals(1, unfaultedValuesOption1.size());
        Assert.assertEquals("value1", unfaultedValuesOption1.get(0));

        // Test getSwitch
        Boolean switchValue = writeableCommandLine.getSwitch(option2, null);
        Assert.assertTrue(switchValue);

        // Test getProperty
        Option propertyOption = new Option("p", "property")
                .required(false)
                .longOpt("longProperty")
                .build();
        writeableCommandLine.addProperty(propertyOption, "propertyName", "propertyValue");
        String propertyValue = writeableCommandLine.getProperty(propertyOption, "propertyName", null);
        Assert.assertEquals("propertyValue", propertyValue);

        // Test getProperties
        Option propertyOption2 = new Option("pp", "properties")
                .required(false)
                .longOpt("longProperties")
                .build();
        Map properties = writeableCommandLine.getProperties(propertyOption2);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("propertyName", properties.keySet().iterator().next());

        // Test looksLikeOption
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--test"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--option1"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("arg"));

        // Test toString
        String toString = writeableCommandLine.toString();
        Assert.assertEquals("--test --option1=value1,value2 --longOption1=value3", toString);

        // Test getOptions
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));

        // Test getOptionTriggers
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(3, optionTriggers.size());
        Assert.assertEquals(option1.getPreferredName(), optionTriggers.iterator().next());
        Assert.assertEquals(option2.getShortOpt(), optionTriggers.iterator().next());
        Assert.assertEquals(option1.getLongOpt(), optionTriggers.iterator().next());

        // Test setDefaultValues and getValues with defaults
        writeableCommandLine.setDefaultValues(option1, new ArrayList() {{
            add("defaultValue1");
            add("defaultValue2");
        }});
        List defaultValuesOption1 = writeableCommandLine.getValues(option1, null);
        Assert.assertEquals(4, defaultValuesOption1.size());
        Assert.assertEquals("value1", defaultValuesOption1.get(0));
        Assert.assertEquals("value2", defaultValuesOption1.get(1));
        Assert.assertEquals("defaultValue1", defaultValuesOption1.get(2));
        Assert.assertEquals("defaultValue2", defaultValuesOption1.get(3));

        // Test setDefaultSwitch and getSwitch with defaults
        writeableCommandLine.setDefaultSwitch(option2, null);
        Boolean defaultSwitchValue = writeableCommandLine.getSwitch(option2, null);
        Assert.assertNull(defaultSwitchValue);

        writeableCommandLine.setDefaultSwitch(option2, false);
        Boolean defaultSwitchValue2 = writeableCommandLine.getSwitch(option2, null);
        Assert.assertFalse(defaultSwitchValue2);

        writeableCommandLine.setDefaultSwitch(option2, true);
        Boolean defaultSwitchValue3 = writeableCommandLine.getSwitch(option2, null);
        Assert.assertTrue(defaultSwitchValue3);

        // Test getNormalised
        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("--test", normalised.get(0));
        Assert.assertEquals("--option1=value1,value2", normalised.get(1));
        Assert.assertEquals("--longOption1=value3", normalised.get(2));
    }
}