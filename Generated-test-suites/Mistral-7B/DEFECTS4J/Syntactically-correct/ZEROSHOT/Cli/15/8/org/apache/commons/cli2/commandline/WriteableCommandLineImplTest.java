import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("b", "boo", true, "default");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("b", "boo", true, "default");
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "arg");
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals("value1", values.get(0));
        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "flag");
        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "arg");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "arg");
        commandLine.addOption(option);
        Option returnedOption = commandLine.getOption("a");
        assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "arg");
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals("value1", values.get(0));
        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "arg");
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "flag");
        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "prop");
        commandLine.addProperty(option, "propName", "propValue");
        assertEquals("propValue", commandLine.getProperty(option, "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "prop");
        commandLine.addProperty(option, "propName1", "propValue1");
        commandLine.addProperty(option, "propName2", "propValue2");
        Map properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertEquals("propValue1", properties.get("propName1"));
        assertEquals("propValue2", properties.get("propName2"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "prop");
        commandLine.addProperty(option, "propName1", "propValue1");
        commandLine.addProperty(option, "propName2", "propValue2");
        Set properties = commandLine.getProperties();
        assertEquals(1, properties.size());
        assertTrue(properties.containsAll(option.getProperties().keySet()));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        assertTrue(commandLine.looksLikeOption("a"));
        assertFalse(commandLine.looksLikeOption("b"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addOption(new Option("a", "opt1", true, "default1"));
        commandLine.addOption(new Option("a", "opt2", true, "default2"));
        commandLine.addValue(commandLine.getOption("opt1"), "value1");
        commandLine.addValue(commandLine.getOption("opt2"), "value2");
        String expected = "opt1 default1 value1 opt2 default2 value2";
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addOption(new Option("a", "opt1", true, "default1"));
        commandLine.addOption(new Option("a", "opt2", true, "default2"));
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(commandLine.getOption("opt1")));
        assertTrue(options.contains(commandLine.getOption("opt2")));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addOption(new Option("a", "opt1", true, "default1"));
        commandLine.addOption(new Option("a", "opt2", true, "default2"));
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(commandLine.getOption("opt1").getTriggers()));
        assertTrue(optionTriggers.containsAll(commandLine.getOption("opt2").getTriggers()));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "arg");
        List defaultValues = Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "flag");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addOption(new Option("a", "opt1", true, "default1"));
        commandLine.addOption(new Option("a", "opt2", true, "default2"));
        commandLine.addValue(commandLine.getOption("opt1"), "value1");
        commandLine.addValue(commandLine.getOption("opt2"), "value2");
        List normalised = commandLine.getNormalised();
        assertEquals(4, normalised.size());
        assertTrue(normalised.contains("opt1"));
        assertTrue(normalised.contains("default1"));
        assertTrue(normalised.contains("opt2"));
        assertTrue(normalised.contains("default2"));
    }
}