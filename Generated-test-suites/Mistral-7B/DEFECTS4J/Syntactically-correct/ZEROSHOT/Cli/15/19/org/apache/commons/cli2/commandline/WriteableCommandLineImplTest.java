package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Test class for WriteableCommandLineImpl
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testCreateWriteableCommandLineImpl() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        assertEquals(rootOption, writeableCommandLine.getRootOption());
        assertEquals(arguments, writeableCommandLine.getArguments());
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option1 = new Option("o1", "option1", true, "option1");
        writeableCommandLine.addOption(option1);
        List options = writeableCommandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option1, options.get(0));
    }

    @Test
    public void testAddValue() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option = new Option("o", "option", false, "option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option = new Option("s", "switch", false, "switch");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option = new Option("o", "option", false, "option");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option = new Option("o", "option", false, "option");
        writeableCommandLine.addOption(option);
        Option retrievedOption = writeableCommandLine.getOption("o");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option = new Option("o", "option", false, "option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option = new Option("o", "option", false, "option");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option = new Option("s", "switch", false, "switch");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
        Boolean defaultSwitchValue = writeableCommandLine.getSwitch(option, false);
        assertFalse(defaultSwitchValue);
    }

    @Test
    public void testgetProperty() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option = new Option("p", "property", true, "property");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "propertyKey", "propertyValue");
        String propertyValue = writeableCommandLine.getProperty("propertyKey");
        assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option = new Option("p", "property", true, "property");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "propertyKey", "propertyValue");
        Properties properties = writeableCommandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertEquals("propertyValue", properties.getProperty("propertyKey"));
    }

    @Test
    public void testGetProperties() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option = new Option("p", "property", true, "property");
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("propertyKey1", "propertyValue1");
        properties.setProperty("propertyKey2", "propertyValue2");
        writeableCommandLine.addProperty(option, properties);
        Set propertiesSet = writeableCommandLine.getProperties(option);
        assertEquals(2, propertiesSet.size());
        assertTrue(propertiesSet.contains("propertyKey1"));
        assertTrue(propertiesSet.contains("propertyKey2"));
    }

    @Test
    public void testLooksLikeOption() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Set prefixes = writeableCommandLine.getPrefixes();
        assertTrue(prefixes.contains(""));
        assertTrue(prefixes.contains("-"));
        assertTrue(prefixes.contains("--"));
        String trigger = "test";
        assertTrue(writeableCommandLine.looksLikeOption(trigger));
        String arg = "test arg";
        assertFalse(writeableCommandLine.looksLikeOption(arg));
    }

    @Test
    public void testToString() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        writeableCommandLine.addOption(new Option("o", "option", false, "option"));
        writeableCommandLine.addOption(new Option("n", "option2", false, "option2"));
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        writeableCommandLine.setArguments(arguments);
        assertEquals("arg1 arg2 -o -n", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option1 = new Option("o1", "option1", true, "option1");
        Option option2 = new Option("o2", "option2", false, "option2");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        assertEquals(option1, options.get(0));
        assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option1 = new Option("o1", "option1", true, "option1");
        Option option2 = new Option("o2", "option2", false, "option2");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("o1"));
        assertTrue(optionTriggers.contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option = new Option("o", "option", false, "option");
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        Option option = new Option("s", "switch", false, "switch");
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option rootOption = new Option("root", "rootOption", false, "root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        List normalised = new ArrayList();
        normalised.add("arg1");
        normalised.add("arg2");
        writeableCommandLine.setNormalised(normalised);
        List actualNormalised = writeableCommandLine.getNormalised();
        assertEquals(2, actualNormalised.size());
        assertEquals("arg1", actualNormalised.get(0));
        assertEquals("arg2", actualNormalised.get(1));
    }
}