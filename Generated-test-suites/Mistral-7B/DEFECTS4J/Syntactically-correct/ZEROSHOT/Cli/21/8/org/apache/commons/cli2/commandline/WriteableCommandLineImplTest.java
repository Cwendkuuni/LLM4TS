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
        Option option = new Option("o", "output", true, "output directory");
        List arguments = Arrays.asList("test", "input.txt", "-o", "/tmp");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // test option existence
        Assert.assertTrue(commandLine.hasOption(option));

        // test get option
        Option optionRetrieved = commandLine.getOption("o");
        Assert.assertSame(option, optionRetrieved);

        // test get values for an argument
        List values = commandLine.getValues(option, null);
        Assert.assertEquals("/tmp", values.get(0));

        // test get switch
        Assert.assertFalse(commandLine.getSwitch(new Option("v", "verbose"), null));

        // test get property
        Assert.assertNull(commandLine.getProperty("unknown"));

        // test adding a property
        commandLine.addProperty(option, "output.directory", "/new/output/dir");
        Map properties = commandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("/new/output/dir", properties.get("output.directory"));

        // test set default values
        commandLine.setDefaultValues(option, Arrays.asList("/default/output/dir"));
        values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("/default/output/dir", values.get(0));

        // test set default switch
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, null));

        // test toString
        Assert.assertEquals("-test input.txt -o /tmp", commandLine.toString());

        // test get options
        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertSame(option, options.get(0));

        // test get option triggers
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("o"));
    }
}