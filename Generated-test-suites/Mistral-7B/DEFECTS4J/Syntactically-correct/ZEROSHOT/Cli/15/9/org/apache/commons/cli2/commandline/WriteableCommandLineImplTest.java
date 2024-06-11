import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("root", "rootOption", true, "root description");
        List arguments = new ArrayList();
        arguments.add("option1");
        arguments.add("value1");
        arguments.add("option2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // test adding option
        Option option1 = new Option("opt1", "option1", false, "option1 description");
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
        assertEquals(option1, commandLine.getOption("opt1"));

        // test adding value
        commandLine.addValue(option1, "value1");
        assertEquals("value1", commandLine.getValues(option1, null).get(0));

        // test adding switch
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));

        // test getOption
        Option option2 = new Option("opt2", "option2", true, "option2 description");
        commandLine.addOption(option2);
        assertEquals(option2, commandLine.getOption("opt2"));

        // test getValues
        List values = commandLine.getValues(option1, null);
        assertEquals("value1", values.get(0));
        assertNull(values.get(1));

        // test getUndefaultedValues
        List unfaultedValues = commandLine.getUndefaultedValues(option1);
        assertNull(unfaultedValues.get(0));

        // test getSwitch
        assertTrue(commandLine.getSwitch(option1, false));

        // test getProperty
        commandLine.addProperty(option1, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(option1, "property1", null));
        assertNull(commandLine.getProperty(option1, "property2", null));

        // test getProperties
        Set properties = commandLine.getProperties(option1);
        assertTrue(properties.contains("property1"));
        assertFalse(properties.contains("property2"));

        // test looksLikeOption
        assertTrue(commandLine.looksLikeOption("opt1"));
        assertFalse(commandLine.looksLikeOption("nonexistent"));

        // test toString
        assertEquals("option1 value1 option2", commandLine.toString());

        // test getOptions
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));

        // test getOptionTriggers
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("opt1"));
        assertTrue(optionTriggers.contains("opt2"));

        // test setDefaultValues
        List defaults = new ArrayList();
        defaults.add("defaultValue1");
        commandLine.setDefaultValues(option1, defaults);
        assertEquals("defaultValue1", commandLine.getValues(option1, null).get(0));

        // test setDefaultSwitch
        commandLine.setDefaultSwitch(option1, false);
        assertFalse(commandLine.getSwitch(option1, false));

        // test getNormalised
        List normalised = commandLine.getNormalised();
        assertEquals(3, normalised.size());
        assertEquals("option1", normalised.get(0));
        assertEquals("value1", normalised.get(1));
        assertEquals("option2", normalised.get(2));
    }
}