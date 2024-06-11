package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @ org.junit.Before
    public void setUp() {}

    @ org.junit.After
    public void tearDown() {}

    /**
     * Test constructor WriteableCommandLineImpl.
     */
    @ org.junit.Test
    public void testWriteableCommandLineImpl() {
        // Create an instance of WriteableCommandLineImpl
        Option rootOption = new Option("rootOption", true, true, "rootOption value");
        List arguments = new ArrayList();
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Test properties
        assertEquals("empty", cmdLine.getProperty("empty", "defaultValue"), "getProperty() returns defaultValue for empty property");
        cmdLine.addProperty("test", "testValue");
        assertEquals("testValue", cmdLine.getProperty("test"), "getProperty() returns testValue for test property");

        // Test options
        Option opt1 = new Option("opt1", true, true, "opt1 value");
        cmdLine.addOption(opt1);
        assertTrue(cmdLine.hasOption(opt1), "hasOption() returns true for opt1");
        assertEquals(opt1, cmdLine.getOption("opt1"), "getOption() returns opt1 for opt1");
        assertEquals(Collections.singletonList("opt1 value"), cmdLine.getValues(opt1, null), "getValues() returns opt1 value for opt1");
        assertEquals(Boolean.TRUE, cmdLine.getSwitch(opt1, null), "getSwitch() returns true for on opt1 switch");

        Option opt2 = new Option("opt2", false, false, null);
        cmdLine.addOption(opt2);
        assertFalse(cmdLine.hasOption(opt2), "hasOption() returns false for opt2");
        assertNull(cmdLine.getOption("opt2"), "getOption() returns null for opt2");
        assertNull(cmdLine.getValues(opt2, null), "getValues() returns null for opt2");
        assertNull(cmdLine.getSwitch(opt2, null), "getSwitch() returns null for off opt2 switch");

        // Test setting default values
        List defaults = new ArrayList();
        defaults.add("defaultValue1");
        cmdLine.setDefaultValues(opt1, defaults);
        assertEquals(defaults, cmdLine.getValues(opt1, null), "getValues() returns defaultValues for opt1");

        // Test setting default switch
        cmdLine.setDefaultSwitch(opt1, Boolean.FALSE);
        assertFalse(cmdLine.getSwitch(opt1, null), "getSwitch() returns false for default switch of opt1");

        // Test toString()
        cmdLine.addArgument(new Argument("arg1", "arg1 value"));
        cmdLine.addArgument(new Argument("arg2", "arg2 value"));
        String expected = "arg1 value arg2 value opt1 value";
        assertEquals(expected, cmdLine.toString(), "toString() returns expected string representation of cmdLine");

        // Test getOptions()
        List options = cmdLine.getOptions();
        assertEquals(2, options.size(), "getOptions() returns 2 options");
        assertTrue(options.contains(opt1), "getOptions() contains opt1");
        assertTrue(options.contains(opt2), "getOptions() contains opt2");

        // Test getOptionTriggers()
        Set optionTriggers = cmdLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size(), "getOptionTriggers() returns 2 option triggers");
        assertTrue(optionTriggers.contains("opt1"), "getOptionTriggers() contains opt1 trigger");
        assertTrue(optionTriggers.contains("opt2"), "getOptionTriggers() contains opt2 trigger");
    }
}