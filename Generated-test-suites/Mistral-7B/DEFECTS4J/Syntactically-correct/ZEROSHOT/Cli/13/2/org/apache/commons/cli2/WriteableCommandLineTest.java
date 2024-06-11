import org.apache.commons.cli2.BaseParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for WriteableCommandLine interface.
 */
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = new DefaultWriteableCommandLine();
        option = new Option("a", "arg", true, "an argument");
    }

    @After
    public void tearDown() {
        commandLine = null;
        option = null;
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertSame(option, options.get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testAddOptionNullOption() {
        commandLine.addOption(null);
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getOptionValues("arg");
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getOptionValues("arg");
        assertEquals(2, values.size());
        assertTrue(Arrays.asList(values.toArray()).containsAll(Arrays.asList("value1", "value2")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullOption() {
        commandLine.addValue(null, "value");
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getOptionValues("arg");
        assertEquals(2, values.size());
        assertTrue(Arrays.asList(values.toArray()).containsAll(Arrays.asList("default1", "default2")));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(option.getValue().isPresent());
        assertEquals(true, option.getValue().get());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertFalse(option.getValue().isPresent());
        assertTrue(option.isSet());
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "propertyValue");
        assertEquals("propertyValue", commandLine.getOptionValue("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--arg"));
        assertTrue(commandLine.looksLikeOption("-a"));
        assertFalse(commandLine.looksLikeOption("value"));
        assertFalse(commandLine.looksLikeOption(""));
    }

    @Test
    public void testParse() {
        String[] args = {"--arg", "value1", "--anotherOption", "value2"};
        ParseContext parseContext = new ParseContext();
        BaseParser parser = new DefaultParser();

        try {
            commandLine = parser.parse(WriteableCommandLineTest.class.getDeclaredField("commandLine").getType(), args, parseContext);
        } catch (NoSuchFieldException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        List<String> values = commandLine.getOptionValues("arg");
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        values = commandLine.getOptionValues("anotherOption");
        assertEquals(0, values.size());
    }
}