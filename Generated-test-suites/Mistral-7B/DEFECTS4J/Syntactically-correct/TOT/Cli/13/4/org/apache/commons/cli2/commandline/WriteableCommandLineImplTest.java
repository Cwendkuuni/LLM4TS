import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.DefaultParser;
import org.apache.commons.cli2.exceptions.ExplicitlySetException;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    private CommandLine commandLine;
    private Properties properties;
    private List options;
    private Map<String, Option> nameToOption;

    @Before
    public void setUp() {
        DefaultParser parser = new DefaultParser();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2")
                .required(false)
                .argName("value")
                .valueSeparator('|');
        Option option3 = new Option("s", "switch", true, "Switch")
                .defaultValue("default");

        List args = Arrays.asList("--opt1", "value1", "--opt2=value2|value3", "--switch");

        try {
            commandLine = parser.parse(option1, option2, option3, args);
        } catch (ExplicitlySetException e) {
            // Ignore
        }

        properties = commandLine.getProperties();
        options = commandLine.getOptions();
        nameToOption = commandLine.getNameToOptionMap();
    }

    @Test
    public void testGetOption() {
        Option option1 = nameToOption.get("opt1");
        Option option2 = nameToOption.get("opt2");

        assertSame(option1, option1);
        assertSame(option2, option2);
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
        assertFalse(commandLine.hasOption(new Option("nonexistent", "nonexistent", false, "Nonexistent option")));
    }

    @Test
    public void testGetValues() {
        List values = commandLine.getValues(option2);

        assertEquals(Arrays.asList("value2", "value3"), values);
    }

    @Test
    public void testGetSwitch() {
        assertTrue(commandLine.getSwitch(option3, null));
        assertEquals("default", commandLine.getSwitch(option3, "default"));
        assertNull(commandLine.getSwitch(option3, "notdefault"));
    }

    @Test
    public void testGetProperty() {
        assertEquals("", commandLine.getProperty("nonexistent", ""));
        assertEquals("value1", commandLine.getProperty("opt1", ""));
    }

    @Test
    public void testGetProperties() {
        Properties actual = commandLine.getProperties();
        Properties expected = new Properties();
        expected.setProperty("opt1", "value1");
        expected.setProperty("opt2", "value2|value3");
        expected.setProperty("switch", "true");

        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        assertEquals("--opt1 value1 --opt2 value2|value3 --switch", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();

        assertEquals(Arrays.asList(option1, option2, option3), options);
    }

    @Test
    public void testGetOptionTriggers() {
        Set triggers = commandLine.getOptionTriggers();

        assertEquals(Arrays.asList("opt1", "opt2", "switch"), triggers);
    }
}