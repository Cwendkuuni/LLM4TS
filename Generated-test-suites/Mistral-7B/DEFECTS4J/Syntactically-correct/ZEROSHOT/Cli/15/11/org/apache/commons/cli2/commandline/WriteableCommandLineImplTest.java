import org.apache.commons.cli2.CommandLine;
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
        Option option = new Option("o", "output", true, "Output file");
        List arguments = Arrays.asList("test.txt");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, arguments);

        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");

        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertEquals(option, commandLine.getOption("output"));
        Assert.assertEquals("test.txt", commandLine.getValues(option, null).get(0).toString());

        Option option1 = new Option("i", "input", true, "Input file");
        commandLine.addOption(option1);
        commandLine.addValue(option1, "input.txt");

        Assert.assertEquals(2, commandLine.getOptions().size());
        Assert.assertEquals("input", commandLine.getOption("i").getLongOpt());
        Assert.assertEquals("input.txt", commandLine.getValues(option1, null).get(0).toString());

        Option propertyOption = new Option("p", "property", true, "Property", "propertyValue");
        commandLine.addProperty(propertyOption, "propertyName", "propertyValue");

        Map<String, String> properties = commandLine.getProperties(propertyOption);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("propertyValue", properties.get("propertyName"));

        Assert.assertEquals( Arrays.asList("output", "input"), commandLine.getOptionTriggers());
        Assert.assertEquals("output file\ninput file\n", commandLine.toString());
    }
}