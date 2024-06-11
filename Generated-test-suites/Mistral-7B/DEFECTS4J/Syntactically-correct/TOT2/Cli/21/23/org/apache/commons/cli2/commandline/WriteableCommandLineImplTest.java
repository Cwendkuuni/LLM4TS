import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption_BasicUsage() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addValue(option, "output.txt");
        Assert.assertEquals("output.txt", commandLine.getValues(option, null).get(0));

        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_addOption_MultipleArguments() {
        Option option = new Option("i", "input", true, "Input file paths", true);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addValue(option, "input1.txt", "input2.txt");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("input1.txt", "input2.txt"), values);

        commandLine.addSwitch(option, false);
        Assert.assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty_BasicUsage() {
        Option option = new Option("p", "property", true, "Property name");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addProperty(option, "property.name", "value1");
        Properties properties = commandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value1", properties.getProperty("property.name"));

        commandLine.addProperty(option, "property.name2", "value2");
        properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("value1", properties.getProperty("property.name"));
        Assert.assertEquals("value2", properties.getProperty("property.name2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteableCommandLineImpl_addOption_OptionAlreadyExists() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);

        commandLine.addOption(option);
    }

    @Test
    public void testWriteableCommandLineImpl_toString_BasicUsage() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");

        String result = commandLine.toString();
        String expected = "-o output.txt";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions_BasicUsage() {
        Option option1 = new Option("o", "output1", true, "Output file path 1");
        Option option2 = new Option("o", "output2", true, "Output file path 2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }
}