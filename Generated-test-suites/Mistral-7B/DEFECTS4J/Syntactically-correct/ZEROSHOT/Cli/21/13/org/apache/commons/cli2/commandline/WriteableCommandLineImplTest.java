import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("root", "rootOption", true, "Root option");
        List arguments = new ArrayList();
        arguments.add("--rootOption=test");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // test addOption
        Option option1 = new Option("opt1", "option1", false, "Option 1");
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
        assertEquals(option1, commandLine.getOption("opt1"));

        // test addValue
        Argument arg1 = new Argument("arg1", "arg1", true, "Arg 1");
        commandLine.addArgument(arg1);
        commandLine.addValue(option1, "value1");
        List values = commandLine.getValues(option1, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        // test addSwitch
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, false));

        // test hasOption
        Option option2 = new Option("opt2", "option2", false, "Option 2");
        commandLine.addOption(option2);
        assertFalse(commandLine.hasOption(new Option("opt3", "opt3", false, "Option 3")));

        // test getOption
        Option optRet = commandLine.getOption("opt1");
        assertEquals(option1, optRet);

        // test getValues
        List valuesRet = commandLine.getValues(option1, null);
        assertEquals(1, valuesRet.size());
        assertEquals("value1", valuesRet.get(0));

        // test getSwitch
        assertTrue(commandLine.getSwitch(option1, false));

        // test getProperty
        Option propertyOption = new Option("prop", "property", true, "Property option");
        commandLine.addProperty(propertyOption, "property.name", "property.value");
        assertEquals("property.value", commandLine.getProperty("property.name"));

        // test addProperty
        commandLine.addProperty("property.name2", "property.value2");
        Set properties = commandLine.getProperties(propertyOption);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property.name"));
        assertTrue(properties.contains("property.name2"));

        // test getProperties
        Set propertiesRet = commandLine.getProperties();
        assertEquals(2, propertiesRet.size());
        assertTrue(propertiesRet.contains("property.name"));
        assertTrue(propertiesRet.contains("property.name2"));

        // test looksLikeOption
        assertTrue(commandLine.looksLikeOption("--opt1"));
        assertFalse(commandLine.looksLikeOption("--arg1"));

        // test toString
        assertEquals("--rootOption=test --opt1", commandLine.toString());

        // test getOptions
        List optionsRet = commandLine.getOptions();
        assertEquals(2, optionsRet.size());
        assertTrue(optionsRet.contains(option1));
        assertTrue(optionsRet.contains(propertyOption));

        // test getOptionTriggers
        Set optionTriggersRet = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggersRet.size());
        assertTrue(optionTriggersRet.contains("opt1"));
        assertTrue(optionTriggersRet.contains("prop"));

        // test setDefaultValues
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option1, defaultValues);
        List valuesDefault = commandLine.getValues(option1, null);
        assertEquals(2, valuesDefault.size());
        assertEquals("value1", valuesDefault.get(0));
        assertEquals("defaultValue1", valuesDefault.get(1));

        // test setDefaultSwitch
        commandLine.setDefaultSwitch(option1, false);
        assertFalse(commandLine.getSwitch(option1, false));

        // test getNormalised
        List normalisedRet = commandLine.getNormalised();
        assertEquals(3, normalisedRet.size());
        assertEquals("--rootOption=test", normalisedRet.get(0).toString());
        assertEquals("--opt1=value1", normalisedRet.get(1).toString());
        assertEquals("--opt1", normalisedRet.get(2).toString());
    }

}