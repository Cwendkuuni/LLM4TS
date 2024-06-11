import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        commandLine.addValue(option, "test.txt");

        List<Object> values = commandLine.getValues(option, null);
        Assert.assertNotNull(values);
        Assert.assertEquals("test.txt", values.get(0));

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertFalse(switchValue);

        commandLine.addSwitch(option, true);
        switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        commandLine.addProperty("testProperty", "testValue");
        String propertyValue = commandLine.getProperty("testProperty", "");
        Assert.assertEquals("testValue", propertyValue);

        List optionsList = commandLine.getOptions();
        Assert.assertEquals(1, optionsList.size());
        Option testOption = (Option) optionsList.get(0);
        Assert.assertEquals("output", testOption.getLongOpt());
        Assert.assertEquals("o", testOption.getOpt());
        Assert.assertEquals("Output file", testOption.getDescription());
        Assert.assertEquals(true, testOption.isRequired());

        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertEquals("o", optionTriggers.iterator().next());

        commandLine.setDefaultValues(option, new ArrayList<>());
        List defaultValues = commandLine.getDefaultValues(option, null);
        Assert.assertNull(defaultValues);

        commandLine.setDefaultValues(option, new ArrayList<String>() {{
            add("defaultOutput.txt");
        }});
        values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultOutput.txt", values.get(0));

        commandLine.setDefaultSwitch(option, true);
        switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        commandLine.setDefaultSwitch(option, false);
        switchValue = commandLine.getSwitch(option, false);
        Assert.assertFalse(switchValue);

        commandLine.setDefaultValues(option, null);
        Assert.assertNull(commandLine.getDefaultValues(option, null));

        commandLine.setDefaultSwitch(option, null);
        Assert.assertNull(commandLine.getSwitch(option, false));
    }
}