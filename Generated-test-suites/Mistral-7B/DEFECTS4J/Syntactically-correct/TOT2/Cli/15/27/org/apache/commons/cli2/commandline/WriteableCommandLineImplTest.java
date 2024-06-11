import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option = new Option("t", "test", false, "Test option");
        commandLine.addOption(option);

        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        Option optionFromCommandLine = (Option) options.get(0);
        assertEquals(option, optionFromCommandLine);
        assertTrue(optionFromCommandLine.isSettableFromCommandLine());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");

        List values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals("testValue", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option = new Option("d", "debug", false, "Debug option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, false);

        Boolean switchValue = commandLine.getSwitch(option, Boolean.FALSE);
        assertFalse(switchValue);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option = new Option("t", "test", false, "Test option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option = new Option("d", "debug", false, "Debug option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, false);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option = new Option("t", "test", false, "Test option");
        commandLine.addOption(option);

        Option optionFromCommandLine = commandLine.getOption("t");
        assertEquals(option, optionFromCommandLine);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue1");
        commandLine.addValue(option, "testValue2");

        List values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(2, values.size());
        assertEquals("testValue1", values.get(0));
        assertEquals("testValue2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addOption(option);

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option = new Option("d", "debug", false, "Debug option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, false);

        Boolean switchValue = commandLine.getSwitch(option, Boolean.FALSE);
        assertFalse(switchValue);

        commandLine.addSwitch(option, true);
        Boolean switchValue2 = commandLine.getSwitch(option, Boolean.FALSE);
        assertTrue(switchValue2);
    }

    @Test
    public void testgetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option = new Option("p", "property", true, "Property option");
        commandLine.addOption(option);
        commandLine.addProperty(option, "propertyName", "propertyValue");

        String propertyValue = commandLine.getProperty("propertyName");
        assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addOption(option);
        List defaultValues = Collections.singletonList("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals("defaultValue", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option = new Option("d", "debug", false, "Debug option");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, Boolean.TRUE);

        Boolean switchValue = commandLine.getSwitch(option, Boolean.FALSE);
        assertTrue(switchValue);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.singletonList("arg1"));
        assertEquals("arg1", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("t", "test2", false, "Test option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        Option option1FromCommandLine = (Option) options.get(0);
        Option option2FromCommandLine = (Option) options.get(1);
        assertEquals(option1, option1FromCommandLine);
        assertEquals(option2, option2FromCommandLine);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), Collections.emptyList());
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("t", "test2", false, "Test option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("t"));
        assertTrue(optionTriggers.contains("test1"));
        assertTrue(optionTriggers.contains("test2"));
    }
}