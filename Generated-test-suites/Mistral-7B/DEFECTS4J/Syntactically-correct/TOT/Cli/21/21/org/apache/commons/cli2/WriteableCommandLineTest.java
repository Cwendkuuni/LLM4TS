import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for WriteableCommandLine interface.
 */
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new Options();
    }

    /**
     * Test method for {@link WriteableCommandLine#addOption(Option)}.
     */
    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);

        Option addedOption = commandLine.getOption("o");
        assertEquals(option, addedOption);
        assertEquals(true, addedOption.isRequired());
    }

    /**
     * Test method for {@link WriteableCommandLine#addValue(Option, Object)}.
     */
    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);

        commandLine.addValue(option, "test");

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("test", values.get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#getUndefaultedValues(Option)}.
     */
    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);

        commandLine.addValue(option, "test1");
        commandLine.addValue(option, "test2");

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("test1", values.get(0));
        assertEquals("test2", values.get(1));

        values = commandLine.getUndefaultedValues(Option.builder("nonexistent").build());
        assertTrue(values.isEmpty());
    }

    /**
     * Test method for {@link WriteableCommandLine#setDefaultValues(Option, List)}.
     */
    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);

        List defaults = new ArrayList();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);

        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());

        commandLine.addValue(option, "test1");
        commandLine.addValue(option, "test2");

        values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertFalse(values.contains("default1"));
        assertFalse(values.contains("default2"));
    }

    /**
     * Test method for {@link WriteableCommandLine#addSwitch(Option, boolean)}.
     */
    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addOption(option);

        try {
            commandLine.addSwitch(option, true);
            assertTrue(true);
        } catch (IllegalStateException e) {
            fail("Should not throw an exception, option doesn't have a value.");
        }

        assertFalse(option.getValue().isSet());

        commandLine.addSwitch(option, true);
        assertTrue(option.getValue().isSet());

        try {
            commandLine.addSwitch(option, true);
            fail("Should throw an exception, switch has already been added.");
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    /**
     * Test method for {@link WriteableCommandLine#setDefaultSwitch(Option, Boolean)}.
     */
    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, false);
        assertFalse(option.getValue().isSet());

        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.getValue().isSet());

        Option addedOption = commandLine.getOption("s");
        assertEquals(true, addedOption.getValue().isSet());
    }

    /**
     * Test method for {@link WriteableCommandLine#addProperty(Option, String, String)}.
     */
    @Test
    public void testAddProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        commandLine.addOption(option);

        commandLine.addProperty(option, "name", "value");

        Option propertyOption = commandLine.getOption("p");
        assertEquals("value", propertyOption.getValue().getStringValue());

        commandLine.addProperty(option, "name", "newValue");
        assertEquals("newValue", propertyOption.getValue().getStringValue());
    }

    /**
     * Test method for {@link WriteableCommandLine#addProperty(String, String)}.
     */
    @Test
    public void testAddPropertyGlobal() {
        commandLine.addProperty("name", "value");

        Option option = new Option("p", "property");
        commandLine.addOption(option);

        Option propertyOption = commandLine.getOption("p");
        assertEquals("value", propertyOption.getValue().getStringValue());
    }

    /**
     * Test method for {@link WriteableCommandLine#looksLikeOption(String)}.
     */
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("test"));
        assertFalse(commandLine.looksLikeOption(""));
    }

    /**
     * Test method for {@link WriteableCommandLine#getOptionCurrent()}.
     */
    @Test
    public void testGetOptionCurrent() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);

        Option currentOption = commandLine.getOptionCurrent();
        assertNull(currentOption);

        commandLine.parse("--option value");

        currentOption = commandLine.getOptionCurrent();
        assertEquals(option, currentOption);
    }

    /**
     * Test method for {@link WriteableCommandLine#setOptionCurrent(Option)}.
     */
    @Test
    public void testSetOptionCurrent() {
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        commandLine.parse("--option1 value1");

        commandLine.setOptionCurrent(option2);

        Option currentOption = commandLine.getOptionCurrent();
        assertEquals(option2, currentOption);
    }
}