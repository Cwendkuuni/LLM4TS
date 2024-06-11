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
        Option option = new Option("opt", "option");
        List arguments = new ArrayList();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        // test adding option
        writeableCommandLine.addOption(option);
        Assert.assertEquals(option, writeableCommandLine.getOption("opt"));

        // test adding value
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        // test adding switch
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        // test hasOption
        Assert.assertTrue(writeableCommandLine.hasOption(option));

        // test getOption
        Option optionFromGetOption = writeableCommandLine.getOption("opt");
        Assert.assertEquals(option, optionFromGetOption);

        // test getValues
        List valuesFromGetValues = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, valuesFromGetValues.size());
        Assert.assertEquals("value1", valuesFromGetValues.get(0));

        // test getSwitch
        Boolean switchValueFromGetSwitch = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValueFromGetSwitch);

        // test getProperty
        writeableCommandLine.addProperty(option, "property", "value");
        String propertyValue = writeableCommandLine.getProperty(option, "property");
        Assert.assertEquals("value", propertyValue);

        // test getProperties
        Properties properties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("property", properties.stringPropertyNames().next());

        // test looksLikeOption
        Assert.assertTrue(writeableCommandLine.looksLikeOption("opt"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("arg"));

        // test toString
        String toString = writeableCommandLine.toString();
        Assert.assertEquals("option value1", toString);

        // test getOptions
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));

        // test getOptionTriggers
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertEquals("opt", optionTriggers.iterator().next());

        // test setDefaultValues
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue", values.get(0));

        // test setDefaultSwitch
        writeableCommandLine.setDefaultSwitch(option, false);
        switchValue = writeableCommandLine.getSwitch(option, true);
        Assert.assertFalse(switchValue);

        // test getNormalised
        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(1, normalised.size());
        Assert.assertEquals("option", normalised.get(0));
    }
}