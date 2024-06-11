import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new DefaultCommandLine();
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);
        Option addedOption = commandLine.getOption("o");
        assertEquals(option, addedOption);
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').hasArg(true).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(values, new ArrayList() {{ add("value"); }});
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(values, new ArrayList() {{ add("value1"); add("value2"); }});
        assertNull(commandLine.getUndefaultedValues(Option.builder("nonexistent").build()));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        List defaults = new ArrayList() {{ add("default1"); add("default2"); }};
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(values, defaults);
        assertNull(commandLine.getUndefaultedValues(Option.builder("nonexistent").build()));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addSwitch(option, true);
        Option addedOption = commandLine.getOption("s");
        assertTrue(addedOption.isSetDefaultValue(false));
        assertTrue(addedOption.getValue().booleanValue());
        try {
            commandLine.addSwitch(option, true);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.setDefaultSwitch(option, true);
        Option addedOption = commandLine.getOption("s");
        assertFalse(addedOption.isSetDefaultValue(false));
        assertTrue(addedOption.getValue().booleanValue());
        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getOption("s").getValue().booleanValue());
    }

    @Test
    public void testAddProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        commandLine.addProperty(option, "name", "value");
        String propertyValue = commandLine.getOption("p").getValue().getString("name");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddPropertyDefault() {
        commandLine.addProperty("name", "defaultValue");
        String propertyValue = commandLine.getProperty("name");
        assertEquals("defaultValue", propertyValue);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertTrue(commandLine.looksLikeOption("-o"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testCurrentOption() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);
        commandLine.parse("--option value1");
        Option currentOption = commandLine.getCurrentOption();
        assertEquals(option, currentOption);
    }

    @Test
    public void testSetCurrentOption() {
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.parse("--option value1");
        commandLine.setCurrentOption(null);
        Option currentOption = commandLine.getCurrentOption();
        assertNull(currentOption);
    }
}