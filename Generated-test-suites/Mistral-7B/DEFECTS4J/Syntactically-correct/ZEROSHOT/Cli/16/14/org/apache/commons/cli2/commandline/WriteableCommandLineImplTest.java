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
import java.util.Properties;

import static org.apache.commons.cli2.commandline.WriteableCommandLineImpl.WriteableCommandLineImplBuilder.*;

/**
 * Test class for WriteableCommandLineImpl.
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = option("option", "o", false, "Option test").requiredElseValue("defaultValue");

        WriteableCommandLineImpl writeableCommandLine = newWriteableCommandLineImpl(new Option("test", "t", false, "Test"),
                Arrays.asList("--option", "value", "--test"));

        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "testValue");
        writeableCommandLine.addSwitch(option, true);

        Map<Option, List<String>> expectedValues = new HashMap<>();
        expectedValues.put(option, Arrays.asList("testValue"));

        Map<Option, Boolean> expectedSwitches = new HashMap<>();
        expectedSwitches.put(option, true);

        Assert.assertEquals(writeableCommandLine.getOptions(), Arrays.asList(option, new Option("test", "t", false, "Test")));
        Assert.assertEquals(writeableCommandLine.getOptionTriggers(), getSet(option.getPreferredName(), "test"));

        Assert.assertTrue(writeableCommandLine.hasOption(option));
        Assert.assertEquals(writeableCommandLine.getOption(option.getPreferredName()), option);

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(values, expectedValues.get(option));

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertEquals(switchValue, expectedSwitches.get(option));

        Assert.assertEquals(writeableCommandLine.getProperty(option, "property"), null);
        Assert.assertEquals(writeableCommandLine.getProperty(option, "property", "default"), "default");

        Map<Option, Properties> expectedProperties = new HashMap<>();
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        expectedProperties.put(option, properties);

        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        Properties actualProperties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(actualProperties, expectedProperties.get(option));

        Set<String> expectedPropertyNames = getSet("property1", "property2");
        Set<String> actualPropertyNames = writeableCommandLine.getProperties().keySet();

        Assert.assertEquals(actualPropertyNames, expectedPropertyNames);

        Assert.assertEquals(writeableCommandLine.toString(), "--test test --option value testValue");
        Assert.assertEquals(writeableCommandLine.getNormalised(), Arrays.asList("--test", "test", "--option", "value", "testValue"));

        Assert.assertFalse(writeableCommandLine.hasOption(new Option("other", "o", false, "Other option")));
    }

    private static <T> Set<T> getSet(T... values) {
        HashSet<T> set = new HashSet<>();
        for (T value : values) {
            set.add(value);
        }
        return set;
    }

    private static Option option(String longOpt, String opt, boolean hasArg, String argName, String description) {
        return Option.builder()
                .longOpt(longOpt)
                .opt(opt)
                .hasArg(hasArg)
                .argName(argName)
                .description(description)
                .build();
    }
}