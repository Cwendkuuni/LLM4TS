import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testEmptyWriteableCommandLineImpl() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Assertions.assertNull(writeableCommandLine.getOptions());
        Assertions.assertFalse(writeableCommandLine.hasOption(null));
        Assertions.assertNull(writeableCommandLine.getOption(null));
        Assertions.assertEquals("", writeableCommandline.toString());
        Assertions.assertEquals(Collections.emptySet(), writeableCommandline.getOptionTriggers());
        Assertions.assertEquals(Collections.emptyMap(), writeableCommandline.getProperties());
    }

    @Test
    public void testWriteableCommandLineImplWithOptions() {
        Option option = new Option("o", "opt", false, "An option");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        writeableCommandLine.addOption(option);
        List options = writeableCommandLine.getOptions();
        Assertions.assertEquals(1, options.size());
        Assertions.assertEquals(option, options.get(0));
        Assertions.assertTrue(writeableCommandLine.hasOption(option));
        Assertions.assertEquals(option, writeableCommandLine.getOption("opt"));
        Assertions.assertEquals("", writeableCommandline.toString());
        Assertions.assertEquals(Collections.singleton("opt"), writeableCommandline.getOptionTriggers());
        Assertions.assertEquals(Collections.emptyMap(), writeableCommandline.getProperties());
    }

    @Test
    public void testWriteableCommandLineImplWithValues() {
        Option option = new Option("o", "opt", true, "An option with value");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List options = writeableCommandLine.getOptions();
        Assertions.assertEquals(1, options.size());
        Assertions.assertEquals(option, options.get(0));
        Assertions.assertTrue(writeableCommandLine.hasOption(option));
        Assertions.assertEquals(option, writeableCommandLine.getOption("opt"));
        List values = writeableCommandLine.getValues(option, null);
        Assertions.assertEquals(2, values.size());
        Assertions.assertEquals("value1", values.get(0));
        Assertions.assertEquals("value2", values.get(1));
        Assertions.assertEquals("", writeableCommandline.toString());
        Assertions.assertEquals(Collections.singleton("opt"), writeableCommandline.getOptionTriggers());
        Assertions.assertEquals(Collections.emptyMap(), writeableCommandline.getProperties());
    }

    @Test
    public void testWriteableCommandLineImplWithSwitches() {
        Option option = new Option("o", "opt", false, "An option with switch");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "-o"));

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        List options = writeableCommandLine.getOptions();
        Assertions.assertEquals(1, options.size());
        Assertions.assertEquals(option, options.get(0));
        Assertions.assertTrue(writeableCommandLine.hasOption(option));
        Assertions.assertEquals(option, writeableCommandLine.getOption("opt"));
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assertions.assertTrue(switchValue);
        Assertions.assertEquals("arg1 -o", writeableCommandline.toString());
        Assertions.assertEquals(Collections.singleton("opt"), writeableCommandline.getOptionTriggers());
        Assertions.assertEquals(Collections.emptyMap(), writeableCommandline.getProperties());
    }

    @Test
    public void testWriteableCommandLineImplWithProperties() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");

        Map<String, String> properties = writeableCommandLine.getProperties();
        Assertions.assertEquals(2, properties.size());
        Assertions.assertEquals("value1", properties.get("property1"));
        Assertions.assertEquals("value2", properties.get("property2"));
        Assertions.assertEquals("", writeableCommandline.toString());
        Assertions.assertEquals(Collections.emptySet(), writeableCommandline.getOptionTriggers());
        Assertions.assertEquals(Map.of("property1", "value1", "property2", "value2"), writeableCommandline.getProperties());
    }
}