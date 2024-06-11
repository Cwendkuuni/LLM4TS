import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList<>());
        Option opt = new Option.Builder("opt").build();
        cmd.addOption(opt);
        assertTrue(cmd.hasOption(opt));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList<>());
        Option opt = new Option.Builder("opt").build();
        cmd.addSwitch(opt, true);
        cmd.addSwitch(opt, false);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("arg").build(), new ArrayList<>());
        Option argOption = new Option.Builder("arg").build();
        cmd.addOption(argOption);
        cmd.addValue(argOption, "value1");
        List values = cmd.getValues(argOption, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        cmd.addValue(argOption, "value2");
        values = cmd.getValues(argOption, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("prop").build(), new ArrayList<>());
        Option propOption = new Option.Builder("prop").build();
        cmd.addOption(propOption);
        cmd.addProperty(propOption, "property", "value1");
        Properties properties = cmd.getProperties(propOption);
        assertEquals(1, properties.size());
        assertEquals("value1", properties.getProperty("property"));

        cmd.addProperty(propOption, "property", "value2");
        properties = cmd.getProperties(propOption);
        assertEquals(1, properties.size());
        assertEquals("value2", properties.getProperty("property"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("arg").build(), new ArrayList<>());
        Option argOption = new Option.Builder("arg").build();
        cmd.addOption(argOption);
        cmd.addValue(argOption, "value1");
        cmd.addValue(argOption, "value2");
        String output = cmd.toString();
        String expected = "'arg' 'value1' 'value2'";
        assertEquals(expected, output);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("test").build(), new ArrayList<>());
        assertTrue(cmd.looksLikeOption("test"));
        assertTrue(cmd.looksLikeOption("test-option"));
        assertFalse(cmd.looksLikeOption("another-option"));
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList<>());
        Option opt1 = new Option.Builder("opt1").build();
        Option opt2 = new Option.Builder("opt2").build();
        cmd.addOption(opt1);
        cmd.addOption(opt2);

        List options = cmd.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(opt1));
        assertTrue(options.contains(opt2));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList<>());
        Option opt1 = new Option.Builder("opt1").build();
        Option opt2 = new Option.Builder("opt2").build();
        cmd.addOption(opt1);
        cmd.addOption(opt2);

        Set optionTriggers = cmd.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("opt"));
        assertTrue(optionTriggers.contains("opt1"));
        assertTrue(optionTriggers.contains("opt2"));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList<>());
        Option opt1 = new Option.Builder("opt1").build();
        Option opt2 = new Option.Builder("opt2").build();
        cmd.addOption(opt1);
        cmd.addOption(opt2);

        assertTrue(cmd.hasOption(opt1));
        assertTrue(cmd.hasOption(opt2));
        assertFalse(cmd.hasOption(new Option.Builder("non-existent").build()));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList<>());
        Option opt1 = new Option.Builder("opt1").build();
        Option opt2 = new Option.Builder("opt2").build();
        cmd.addOption(opt1);
        cmd.addOption(opt2);

        Option option = cmd.getOption("opt1");
        assertEquals(opt1, option);
        option = cmd.getOption("opt2");
        assertEquals(opt2, option);
        assertNull(cmd.getOption("non-existent"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("arg").build(), new ArrayList<>());
        Option argOption = new Option.Builder("arg").build();
        cmd.addOption(argOption);
        cmd.addValue(argOption, "value1");
        cmd.addValue(argOption, "value2");

        List values = cmd.getValues(argOption, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        values = cmd.getValues(argOption, new ArrayList<>());
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        values = cmd.getValues(argOption, new ArrayList<String>(){ { add("value3"); } });
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("arg").build(), new ArrayList<>());
        Option argOption = new Option.Builder("arg").build();
        cmd.addOption(argOption);
        cmd.addValue(argOption, "value1");
        cmd.addValue(argOption, "value2");

        List values = cmd.getUndefaultedValues(argOption);
        assertEquals(0, values.size());

        cmd.setDefaultValues(argOption, new ArrayList<String>(){{ add("value3"); }});
        values = cmd.getUndefaultedValues(argOption);
        assertEquals(0, values.size());

        cmd.addValue(argOption, "valueX");
        values = cmd.getUndefaultedValues(argOption);
        assertEquals(1, values.size());
        assertEquals("valueX", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("switch").build(), new ArrayList<>());
        Option switchOption = new Option.Builder("switch").build();
        cmd.addOption(switchOption);
        cmd.addSwitch(switchOption, true);

        Boolean switchValue = cmd.getSwitch(switchOption, false);
        assertTrue(switchValue);

        cmd.addSwitch(switchOption, false);
        switchValue = cmd.getSwitch(switchOption, false);
        assertFalse(switchValue);

        cmd.setDefaultSwitch(switchOption, true);
        switchValue = cmd.getSwitch(switchOption, false);
        assertTrue(switchValue);

        cmd.setDefaultSwitch(switchOption, false);
        switchValue = cmd.getSwitch(switchOption, false);
        assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("prop").build(), new ArrayList<>());
        Option propOption = new Option.Builder("prop").build();
        cmd.addOption(propOption);
        cmd.addProperty(propOption, "property", "value1");

        String propertyValue = cmd.getProperty("property");
        assertEquals("value1", propertyValue);

        cmd.addProperty(propOption, "property", "value2");
        propertyValue = cmd.getProperty("property");
        assertEquals("value2", propertyValue);

        propertyValue = cmd.getProperty("property", "defaultValue");
        assertEquals("value2", propertyValue);

        cmd.setDefaultValues(propOption, null);
        propertyValue = cmd.getProperty("property");
        assertNull(propertyValue);

        cmd.setDefaultValues(propOption, new Properties(){{ put("property", "defaultValue"); }});
        propertyValue = cmd.getProperty("property");
        assertEquals("defaultValue", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option.Builder("prop").build(), new ArrayList<>());
        Option propOption = new Option.Builder("prop").build();
        cmd.addOption(propOption);
        cmd.addProperty(propOption, "property1", "value1");
        cmd.addProperty(propOption, "property2", "value2");

        Set properties = cmd.getProperties(propOption);
        assertEquals(2, properties.size());

        Set propertiesAll = cmd.getProperties();
        assertEquals(2, propertiesAll.size());
    }
}