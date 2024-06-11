import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySetSwitch() {
        Option option = new Option("option", false);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("option");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetValues() {
        Option argOption = new Option("arg", true, "value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argOption, Collections.emptyList());
        commandLine.addValue(argOption, "value1");
        commandLine.addValue(argOption, "value2");

        List values = commandLine.getValues(argOption, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option argOption = new Option("arg", true, "value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argOption, Collections.emptyList());
        commandLine.addValue(argOption, "value1");

        List values = commandLine.getUndefaultedValues(argOption);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option", false);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "property", "property value");

        Properties expectedProperties = new Properties();
        expectedProperties.setProperty("property", "property value");
        Properties actualProperties = (Properties) commandLine.optionToProperties.get(option);
        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "property", "property value");

        String expectedPropertyValue = "property value";
        String actualPropertyValue = commandLine.getProperty(option, "property");
        assertEquals(expectedPropertyValue, actualPropertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "property1", "property1 value");
        commandLine.addProperty(option, "property2", "property2 value");

        Set expectedProperties = Collections.unmodifiableSet(new HashSet() {{
            add("property1");
            add("property2");
        }});
        Set actualProperties = commandLine.getProperties(option);
        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option", "option description"), Collections.emptyList());
        assertTrue(commandLine.looksLikeOption("option"));
    }

    @Test
    public void testToString() {
        Option argOption = new Option("arg", true, "value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(argOption, Arrays.asList("value1", "value2"));
        String expectedString = "--arg value1 --arg value2";
        String actualString = commandLine.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1", "option1 description");
        Option option2 = new Option("option2", "option2 description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option2);

        List expectedOptions = Arrays.asList(option1, option2);
        List actualOptions = commandLine.getOptions();
        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1", "option1 description");
        Option option2 = new Option("option2", "option2 description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option2);

        Set expectedTriggers = Collections.unmodifiableSet(new HashSet() {{
            add("option1");
            add("option2");
        }});
        Set actualTriggers = commandLine.getOptionTriggers();
        assertEquals(expectedTriggers, actualTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);

        List expectedDefaultValues = Collections.unmodifiableList(new ArrayList() {{
            add("defaultValue1");
            add("defaultValue2");
        }});
        List actualDefaultValues = (List) commandLine.defaultValues.get(option);
        assertEquals(expectedDefaultValues, actualDefaultValues);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option", false);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.setDefaultSwitch(option, true);

        Boolean expectedDefaultSwitch = Boolean.TRUE;
        Boolean actualDefaultSwitch = (Boolean) commandLine.defaultSwitches.get(option);
        assertEquals(expectedDefaultSwitch, actualDefaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("option", "option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("value1", "value2"));

        List expectedNormalised = Arrays.asList("value1", "value2");
        List actualNormalised = commandLine.getNormalised();
        assertEquals(expectedNormalised, actualNormalised);
    }
}