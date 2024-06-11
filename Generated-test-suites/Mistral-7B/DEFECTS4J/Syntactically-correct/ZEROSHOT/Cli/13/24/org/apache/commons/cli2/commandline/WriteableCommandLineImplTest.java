import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file");
        List arguments = new ArrayList();
        arguments.add("test.txt");

        WriteableCommandLine cmd = new WriteableCommandLineImpl(option, arguments);

        // test addOption
        cmd.addOption(option);
        Assert.assertEquals(1, cmd.getOptions().size());
        Option optionFromCommandLine = cmd.getOption("o");
        Assert.assertEquals("output", optionFromCommandLine.getLongOpt());
        Assert.assertEquals("Output file", optionFromCommandLine.getDescription());
        Assert.assertFalse(optionFromCommandLine.isRequired());
        Assert.assertNull(optionFromCommandLine.getValue());

        // test addValue
        cmd.addValue(option, "test_output.txt");
        List values = cmd.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test_output.txt", values.get(0));

        // test addSwitch
        cmd.addSwitch(option, true);
        Boolean switchValue = cmd.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        // test hasOption
        Assert.assertTrue(cmd.hasOption(option));

        // test getOption
        Option optionFromMap = cmd.getOption("o");
        Assert.assertSame(option, optionFromMap);

        // test getValues
        List defaultValues = new ArrayList();
        defaultValues.add("default_output.txt");
        cmd.setDefaultValues(option, defaultValues);
        values = cmd.getValues(option, defaultValues);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test_output.txt", values.get(0));

        // test getSwitch
        Boolean defaultSwitch = Boolean.FALSE;
        cmd.setDefaultSwitch(option, defaultSwitch);
        switchValue = cmd.getSwitch(option, defaultSwitch);
        Assert.assertTrue(switchValue);

        // test addProperty
        cmd.addProperty("property1", "value1");
        Properties properties = cmd.getProperties();
        Set propertiesKeySet = properties.keySet();
        Assert.assertEquals(1, propertiesKeySet.size());
        Assert.assertEquals("property1", propertiesKeySet.iterator().next());
        Assert.assertEquals("value1", properties.getProperty("property1"));

        // test getProperty
        String defaultValue = "default_value";
        String propertyValue = cmd.getProperty("property1", defaultValue);
        Assert.assertEquals("value1", propertyValue);

        // test getProperties
        Set commandLineProperties = cmd.getProperties();
        Assert.assertEquals(1, commandLineProperties.size());

        // test looksLikeOption
        Assert.assertTrue(cmd.looksLikeOption("o"));

        // test toString
        String commandLineString = cmd.toString();
        String expectedCommandLineString = "output test_output.txt";
        Assert.assertEquals(expectedCommandLineString, commandLineString);

        // test getOptions
        List options = cmd.getOptions();
        Assert.assertEquals(1, options.size());

        // test getOptionTriggers
        Set optionTriggers = cmd.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());

        // test setDefaultValues
        List newDefaultValues = new ArrayList();
        newDefaultValues.add("new_default_output.txt");
        cmd.setDefaultValues(option, newDefaultValues);
        values = cmd.getValues(option, defaultValues);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test_output.txt", values.get(0));

        // test setDefaultSwitch
        Boolean newDefaultSwitch = Boolean.FALSE;
        cmd.setDefaultSwitch(option, newDefaultSwitch);
        switchValue = cmd.getSwitch(option, defaultSwitch);
        Assert.assertFalse(switchValue);

        // test getNormalised
        List normalised = cmd.getNormalised();
        Assert.assertEquals(1, normalised.size());
        Assert.assertEquals("test.txt", normalised.get(0));
    }
}