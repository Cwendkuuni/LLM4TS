package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.commandline.WriteableCommandLineImplTest.DummyOption;
import org.apache.commons.cli2.exceptions.ParseException;

public class WriteableCommandLineImplTest {

    public static class DummyOption extends Option {
        private static final long serialVersionUID = 1L;

        public DummyOption() {
            super("d", "d", false, "dummy");
            setArgName("dummyArg");
        }
    }

    @org.junit.Test
    public void testWriteableCommandLineBasicUsage() throws ParseException {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withShortNames('t')
                .withDescription("Test Option")
                .create(false);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "arg1", "arg2"));
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addSwitch(option, true);

        WriteableCommandLine parsedCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "--testOption", "-t", "arg1", "arg2", "--dummy", "dummyArg1", "--dummy", "dummyArg2"));

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
        assertEquals(true, commandLine.getSwitch(option, false));
        assertEquals(parsedCommandLine, commandLine);
        assertEquals("test", commandLine.getCommandLineArgs()[0]);
        assertEquals("arg1", commandLine.getCommandLineArgs()[2]);
        assertEquals("arg2", commandLine.getCommandLineArgs()[3]);
    }

    @org.junit.Test
    public void testWriteableCommandLineMissingOption() {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withShortNames('t')
                .withDescription("Test Option")
                .create(false);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "arg1", "arg2"));

        try {
            commandLine.getValues(option, null);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testWriteableCommandLineEmptyOption() throws ParseException {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withShortNames('t')
                .withDescription("Test Option")
                .create(false);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));

        List values = commandLine.getValues(option, null);
        assertNull(values);
        assertFalse(commandLine.getSwitch(option, false));
    }

    @org.junit.Test
    public void testWriteableCommandLineMultipleOptions() throws ParseException {
        Option option1 = OptionBuilder.withLongOpt("testOption1")
                .withShortNames('t1')
                .withDescription("Test Option 1")
                .create(false);
        Option option2 = OptionBuilder.withLongOpt("testOption2")
                .withShortNames('t2')
                .withDescription("Test Option 2")
                .create(false);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test", "--testOption1", "-t1", "arg1"));
        commandLine.addValue(option1, "value1");

        commandLine.addOption(option2);
        commandLine.addValue(option2, "value2");

        WriteableCommandLine parsedCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test", "--testOption1", "-t1", "arg1", "--testOption2", "-t2", "arg2"));

        assertEquals(Arrays.asList("value1"), commandLine.getValues(option1, null));
        assertEquals(null, commandLine.getValues(option2, null));
        assertEquals(Collections.singletonMap(option2, Arrays.asList("arg2")), commandLine.getValues());
        assertEquals(true, commandLine.getSwitch(option1, false));
        assertEquals(false, commandLine.getSwitch(option2, false));
        assertEquals(parsedCommandLine, commandLine);
        assertEquals("test", commandLine.getCommandLineArgs()[0]);
        assertEquals("arg1", commandLine.getCommandLineArgs()[2]);
        assertEquals("arg2", commandLine.getCommandLineArgs()[6]);
    }

    @org.junit.Test
    public void testWriteableCommandLineOptionsWithArguments() throws ParseException {
        Option option1 = OptionBuilder.withLongOpt("testOption1")
                .withShortNames('t1')
                .withDescription("Test Option 1")
                .withArgName("testArg1")
                .withDescription("Test Argument 1")
                .create(true);
        Option option2 = OptionBuilder.withLongOpt("testOption2")
                .withShortNames('t2')
                .withDescription("Test Option 2")
                .withArgName("testArg2")
                .withDescription("Test Argument 2")
                .create(true);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test", "--testOption1=testArg11", "--testOption2=testArg21", "arg"));

        commandLine.addOption(option2);

        WriteableCommandLine parsedCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test", "--testOption1=testArg11", "--testOption2=testArg21", "arg", "--testOption2=testArg22"));

        Map<Option, List<String>> values = commandLine.getValues();
        assertEquals(Arrays.asList("testArg11"), commandLine.getValues(option1, null));
        assertEquals(Arrays.asList("testArg21", "testArg22"), commandLine.getValues(option2, null));
        assertEquals(values, commandLine.getValues());
        assertEquals(true, commandLine.getSwitch(option1, false));
        assertEquals(false, commandLine.getSwitch(option2, false));
        assertEquals(parsedCommandLine, commandLine);
        assertEquals("test", commandLine.getCommandLineArgs()[0]);
        assertEquals("arg", commandLine.getCommandLineArgs()[3]);
        assertEquals("testArg21", commandLine.getCommandLineArgs()[4]);
        assertEquals("testArg22", commandLine.getCommandLineArgs()[5]);
    }

    @org.junit.Test
    public void testWriteableCommandLineOptionsWithDefaultValues() throws ParseException {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withShortNames('t')
                .withDescription("Test Option")
                .withValueSeparator('=')
                .withValueSeparator(',')
                .create(true);

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addProperty(option, "defaultProp1", "defaultValue1");

        WriteableCommandLine parsedCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        parsedCommandLine.addProperty(option, "defaultProp1", "defaultValue2");

        commandLine.addProperty(option, "prop1", "value1");
        commandLine.addProperty(option, "prop2", "value2");

        commandLine.setDefaultValues(option, Arrays.asList("defaultValue3"));
        commandLine.setDefaultValues(option, Arrays.asList("defaultValue4"));

        parsedCommandLine.addProperty(option, "prop1", "value3");
        parsedCommandLine.setDefaultValues(option, null);

        assertEquals(Arrays.asList("defaultValue1"), commandLine.getValues(option, null));
        assertEquals(Arrays.asList("value1"), commandLine.getValues(option, null));
        assertEquals(Arrays.asList("defaultValue3", "defaultValue4"), commandLine.getDefaultValues(option));
        assertEquals(Arrays.asList("defaultValue1", "value1"), commandLine.getValues(option));
        assertEquals(Arrays.asList("defaultValue2", "value3"), parsedCommandLine.getValues(option));
        assertEquals(Arrays.asList("defaultValue1", "value1"), commandLine.getValues(option));
        assertEquals(Arrays.asList("defaultValue2", "value3"), parsedCommandLine.getValues(option));
        assertEquals(Collections.singletonMap(option, Arrays.asList("defaultProp1=defaultValue1", "prop1=value1", "prop2=value2")), commandLine.getProperties());
        assertEquals(Collections.singletonMap(option, Arrays.asList("defaultProp1=defaultValue2", "prop1=value3")), parsedCommandLine.getProperties());
        assertEquals(commandLine, parsedCommandLine);
    }

    @org.junit.Test
    public void testWriteableCommandLineOptionsWithSwitch() throws ParseException {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withShortNames('t')
                .withDescription("Test Option")
                .withValueSeparator('=')
                .withValueSeparator(',')
                .create(true);

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);

        WriteableCommandLine parsedCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        parsedCommandLine.addSwitch(option, false);

        commandLine.setDefaultSwitch(option, Boolean.TRUE);

        parsedCommandLine.setDefaultSwitch(option, null);

        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, false));
        assertEquals(Boolean.TRUE, commandLine.getDefaultSwitch(option));
        assertEquals(Boolean.FALSE, parsedCommandLine.getSwitch(option, false));
        assertEquals(Boolean.TRUE, commandLine.getDefaultSwitch(option));
        assertEquals(Boolean.FALSE, parsedCommandLine.getDefaultSwitch(option));
    }

    @org.junit.Test
    public void testWriteableCommandLineOptionsWithDifferentPrefixes() throws ParseException {
        Option option1 = OptionBuilder.withLongOpt("testOption1")
                .withShortNames('t1')
                .withDescription("Test Option 1")
                .create(false);
        Option option2 = OptionBuilder.withLongOpt("testOption2")
                .withShortNames('t2')
                .withDescription("Test Option 2")
                .withDescription("Test Option 2")
                .create(false);

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("--test1", "arg1"));
        commandLine.addOption(option2);

        WriteableCommandLine parsedCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList("--test1=arg1", "--test2"));

        assertEquals(Arrays.asList("arg1"), commandLine.getValues(option1, null));
        assertEquals(null, commandLine.getValues(option2, null));
        assertEquals(Collections.singletonMap(option2, Arrays.asList("")), commandLine.getValues());
        assertEquals(false, commandLine.getSwitch(option1, false));
        assertEquals(false, commandLine.getSwitch(option2, false));
        assertEquals(parsedCommandLine, commandLine);
        assertEquals("--test1", commandLine.getCommandLineArgs()[0]);
        assertEquals("arg1", commandLine.getCommandLineArgs()[1]);
        assertEquals("--test2", commandLine.getCommandLineArgs()[2]);
    }

    @org.junit.Test
    public void testWriteableCommandLineOptionsWithDifferentPrefixesAndSameName() throws ParseException {
        Option option1 = OptionBuilder.withLongOpt("testOption")
                .withShortNames('t1')
                .withDescription("Test Option 1")
                .create(false);
        Option option2 = OptionBuilder.withLongOpt("testOption")
                .withShortNames('t2')
                .withDescription("Test Option 2")
                .create(false);

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("--test=arg1"));
        commandLine.addOption(option2);

        WriteableCommandLine parsedCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList("--test=arg1", "--test2"));

        assertEquals(Arrays.asList("arg1"), commandLine.getValues(option1, null));
        assertEquals(null, commandLine.getValues(option2, null));
        assertEquals(Collections.singletonMap(option2, Arrays.asList("")), commandLine.getValues());
        assertEquals(false, commandLine.getSwitch(option1, false));
        assertEquals(false, commandLine.getSwitch(option2, false));
        assertEquals(parsedCommandLine, commandLine);
        assertEquals("--test", commandLine.getCommandLineArgs()[0]);
        assertEquals("arg1", commandLine.getCommandLineArgs()[1]);
        assertEquals("--test2", commandLine.getCommandLineArgs()[2]);
    }

    @org.junit.Test
    public void testWriteableCommandLineWithDummyOption() throws ParseException {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withShortNames('t')
                .withDescription("Test Option")
                .create(false);

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "--dummy=dummyArg1"));

        WriteableCommandLine parsedCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "--dummy=dummyArg1"));

        commandLine.addOption(new DummyOption());

        assertEquals(Collections.emptyMap(), commandLine.getValues());
        assertEquals(Arrays.asList("dummyArg1"), parsedCommandLine.getValues(new DummyOption(), null));
        assertEquals(parsedCommandLine, commandLine);

        assertEquals("test", commandLine.getCommandLineArgs()[0]);
        assertEquals("--dummy", commandLine.getCommandLineArgs()[1]);
        assertEquals("dummyArg1", commandLine.getCommandLineArgs()[2]);
    }
}