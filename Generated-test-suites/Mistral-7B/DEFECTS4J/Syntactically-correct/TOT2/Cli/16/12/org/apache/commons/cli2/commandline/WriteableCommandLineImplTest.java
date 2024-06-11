import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
        Option option = new Option("opt2", "option2");
        commandLine.addOption(option);

        CommandLine parsedCommandLine = commandLine.parse((String[]) null);
        Option parsedOption = parsedCommandLine.getOption("opt2");
        assertTrue(parsedCommandLine.hasOption(parsedOption));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
        commandLine.addValue(commandLine.getOption("arg"), "value");

        CommandLine parsedCommandLine = commandLine.parse((String[]) null);
        List<String> parsedValues = parsedCommandLine.getValues(commandLine.getOption("arg"), null);
        assertEquals(Collections.singletonList("value"), parsedValues);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), Collections.emptyList());
        commandLine.addSwitch(commandLine.getOption("switch"), true);

        CommandLine parsedCommandLine = commandLine.parse((String[]) null);
        Boolean parsedSwitch = parsedCommandLine.getSwitch(commandLine.getOption("switch"), false);
        assertTrue(parsedSwitch);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), Collections.emptyList());
        commandLine.addSwitch(commandLine.getOption("switch"), true);
        commandLine.addSwitch(commandLine.getOption("switch"), false);

        commandLine.parse((String[]) null);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());

        assertTrue(commandLine.hasOption(commandLine.getOption("opt")));
        assertFalse(commandLine.hasOption(new Option("opt2", "option2")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
        Option option = commandLine.getOption("opt");

        assertEquals(option, commandLine.getOption("opt"));
        assertNull(commandLine.getOption("opt2"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
        commandLine.addValue(commandLine.getOption("arg"), "value1");
        commandLine.addValue(commandLine.getOption("arg"), "value2");

        CommandLine parsedCommandLine = commandLine.parse((String[]) null);
        List<String> parsedValues = parsedCommandLine.getValues(commandLine.getOption("arg"), null);
        assertEquals(Arrays.asList("value1", "value2"), parsedValues);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
        commandLine.addValue(commandLine.getOption("arg"), "value1");

        CommandLine parsedCommandLine = commandLine.parse((String[]) null);
        List<String> parsedValues = parsedCommandLine.getUndefaultedValues(commandLine.getOption("arg"));
        assertEquals(Collections.singletonList("value1"), parsedValues);
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), Collections.emptyList());
        commandLine.addSwitch(commandLine.getOption("switch"), true);

        CommandLine parsedCommandLine = commandLine.parse((String[]) null);
        Boolean parsedSwitch = parsedCommandLine.getSwitch(commandLine.getOption("switch"), false);
        assertTrue(parsedSwitch);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption("prop", "property"), Collections.emptyList());
        commandLine.addProperty(commandLine.getOption("prop"), "value");

        CommandLine parsedCommandLine = commandLine.parse((String[]) null);
        String parsedProperty = parsedCommandLine.getProperty("prop");
        assertEquals("value", parsedProperty);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption("prop1", "property1"), Collections.emptyList());
        WriteableCommandLineImpl commandLine2 = new WriteableCommandLineImpl(new PropertyOption("prop2", "property2"), Collections.emptyList());

        commandLine.addProperty(commandLine.getOption("prop1"), "value1");
        commandLine2.addProperty(commandLine2.getOption("prop2"), "value2");
        commandLine.addOption(commandLine2.getOption("prop2"));

        CommandLine parsedCommandLine = commandLine.parse((String[]) null);
        Set<String> parsedProperties = parsedCommandLine.getProperties(commandLine.getOption("prop1"));
        assertEquals(Collections.singleton("value1"), parsedProperties);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.singletonList("o"));

        assertTrue(commandLine.looksLikeOption("o"));
        assertFalse(commandLine.looksLikeOption("option"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt", "option"), Collections.emptyList());
        commandLine.addValue(commandLine.getOption("opt"), "value");

        assertEquals("value option", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt1", "option1"), Collections.emptyList());
        commandLine.addOption(new Option("opt2", "option2"));

        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("opt1", "option1"), Collections.emptyList());
        commandLine.addOption(new Option("opt2", "option2"));

        assertEquals(2, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
        List<String> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(commandLine.getOption("arg"), defaults);

        CommandLine parsedCommandLine = commandLine.parse((String[]) null);
        List<String> parsedValues = parsedCommandLine.getValues(commandLine.getOption("arg"), null);
        assertEquals(defaults, parsedValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("switch", "switch"), Collections.emptyList());
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(commandLine.getOption("switch"), defaultSwitch);

        CommandLine parsedCommandLine = commandLine.parse((String[]) null);
        Boolean parsedSwitch = parsedCommandLine.getSwitch(commandLine.getOption("switch"), false);
        assertTrue(parsedSwitch);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("arg", "argument"), Collections.emptyList());
        commandLine.addValue(commandLine.getOption("arg"), "value1");
        commandLine.addValue(commandLine.getOption("arg"), "value2");

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getNormalised());
    }
}