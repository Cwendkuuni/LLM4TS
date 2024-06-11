package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

/**
 * JUnit test file for Option class.
 *
 * @author John Doe
 */
public class OptionTest {

    @org.junit.Test
    public void testOptionConstructionWithOneArgument() {
        Option option = new Option("t", "testOption", true, "Test option description");

        assertEquals("t", option.getOpt());
        assertEquals("testOption", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Test option description", option.getDescription());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasValueSeparator());

        assertEquals(Option.UNINITIALIZED, option.getId());
        assertNull(option.getType());
        assertNotNull(option.getValuesList());
        assertFalse(option.hasNoValues());
        assertNull(option.getValue());
        assertEquals(0, option.getValues().size());

        Option clone = option.clone();
        assertEquals(option, clone);
        assertEquals(option.hashCode(), clone.hashCode());

        option.clearValues();
        assertEquals(0, option.getValues().size());
    }

    @org.junit.Test
    public void testOptionConstructionWithNoArguments() {
        Option option = new Option("v", "verbose", false, "Verbose option description");

        assertEquals("v", option.getOpt());
        assertEquals("verbose", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Verbose option description", option.getDescription());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());

        assertEquals(Option.UNINITIALIZED, option.getId());
        assertNull(option.getType());
        assertNotNull(option.getValuesList());
        assertFalse(option.hasNoValues());
        assertNull(option.getValue());
        assertEquals(0, option.getValues().size());

        Option clone = option.clone();
        assertEquals(option, clone);
        assertEquals(option.hashCode(), clone.hashCode());

        option.clearValues();
        assertEquals(0, option.getValues().size());
    }

    @org.junit.Test
    public void testOptionConstructionWithLongOptionAndArgument() {
        Option option = OptionBuilder.withLongOpt("help")
                                      .hasArg()
                                      .isRequired()
                                      .desc("Print help message")
                                      .build();

        assertEquals("help", option.getOpt());
        assertEquals("help", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Print help message", option.getDescription());
        assertTrue(option.isRequired());
        assertTrue(option.hasLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasValueSeparator());

        assertEquals(Option.UNINITIALIZED, option.getId());
        assertNull(option.getType());
        assertNotNull(option.getValuesList());
        assertFalse(option.hasNoValues());
        assertNull(option.getValue());
        assertEquals(0, option.getValues().size());

        Option clone = option.clone();
        assertEquals(option, clone);
        assertEquals(option.hashCode(), clone.hashCode());

        option.clearValues();
        assertEquals(0, option.getValues().size());
    }

    @org.junit.Test
    public void testOptionWithMultipleValues() {
        Option option = new Option("i", "input", true, "Input option description")
                            .setArgName("FILE")
                            .setValueSeparator(';')
                            .setMultiValued(true);

        assertEquals("i", option.getOpt());
        assertEquals("input", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Input option description", option.getDescription());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertEquals("FILE", option.getArgName());
        assertTrue(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertTrue(option.hasValueSeparator());

        assertEquals(Option.UNINITIALIZED, option.getId());
        assertNull(option.getType());
        assertNotNull(option.getValuesList());
        assertFalse(option.hasNoValues());
        assertNull(option.getValue());
        assertEquals(0, option.getValues().size());

        option.addValueForProcessing("input1.txt");
        option.addValueForProcessing("input2.txt");
        assertEquals(2, option.getValues().size());
        assertArrayEquals(new String[]{"input1.txt", "input2.txt"}, option.getValues().toArray(new String[0]));
        assertEquals("input1.txt;input2.txt", option.getValue());

        Option clone = option.clone();
        assertEquals(option, clone);
        assertEquals(option.hashCode(), clone.hashCode());

        option.clearValues();
        assertEquals(0, option.getValues().size());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidOption() {
        new Option("", "testOption", true, "Test option description");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidLongOption() {
        new Option("t", "", true, "Test option description");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidArgument() {
        new Option("t", "testOption", -1, "Test option description");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidNumberOfArguments() {
        new Option("t", "testOption", 2, "Test option description");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidValueSeparator() {
        new Option("t", "testOption", true, "Test option description")
                .setValueSeparator('\0');
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithTooManyArguments() {
        Option option = new Option("i", "input", true, "Input option description")
                            .setArgName("FILE")
                            .setValueSeparator(';')
                            .setMultiValued(true);

        option.addValueForProcessing("input1.txt");
        option.addValueForProcessing("input2.txt");
        option.addValueForProcessing("input3.txt");
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseOptionWithInvalidArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("i", "input", true, "Input option description")
                            .setArgName("FILE")
                            .setValueSeparator(';')
                            .setMultiValued(true);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i", "invalid_input"});
        assertFalse(cmd.hasOption("input"));
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseOptionWithMissingArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("i", "input", true, "Input option description")
                            .setArgName("FILE")
                            .setValueSeparator(';')
                            .setMultiValued(true);

        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i"});
        assertFalse(cmd.hasOption("input"));
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseOptionWithTooManyArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option("i", "input", true, "Input option description")
                            .setArgName("FILE")
                            .setValueSeparator(';')
                            .setMultiValued(true);

        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i", "input1.txt", "input2.txt", "input3.txt"});
        assertFalse(cmd.hasOption("input"));
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseOptionWithMissingValueSeparator() throws ParseException {
        Options options = new Options();
        Option option = new Option("i", "input", true, "Input option description")
                            .setArgName("FILE")
                            .setValueSeparator(';')
                            .setMultiValued(true);

        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i", "input1.txt input2.txt input3.txt"});
        assertFalse(cmd.hasOption("input"));
    }

    @org.junit.Test(expected = ParseException.class)
    public void testParseOptionWithEmptyValue() throws ParseException {
        Options options = new Options();
        Option option = new Option("i", "input", true, "Input option description")
                            .setArgName("FILE")
                            .setValueSeparator(';')
                            .setMultiValued(true);

        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-i", ""});
        assertFalse(cmd.hasOption("input"));
    }
}