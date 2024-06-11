package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import java.util.Arrays;

public class OptionBuilderTest {

    @org.junit.Test
    public void testSimpleOption() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option simpleOption = optionBuilder.withLongOpt("testOption")
                .isRequired()
                .hasArg()
                .create('t');
        CommandLineParser parser = new DefaultParser();

        String[] args = new String[]{"-t", "testValue"};
        try {
            CommandLine cmd = parser.parse(Arrays.asList(args), new DefaultOptions());
            assertEquals("testValue", simpleOption.getValue(cmd));
        } catch (ParseException e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testOptionalArgument() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option optionalOption = optionBuilder.withLongOpt("testOption")
                .isRequired()
                .hasOptionalArg()
                .withArgName("testValue")
                .create('t');
        CommandLineParser parser = new DefaultParser();

        String[] args1 = new String[]{"-t"};
        try {
            CommandLine cmd1 = parser.parse(Arrays.asList(args1), new DefaultOptions());
            assertNull(optionalOption.getValue(cmd1));
        } catch (ParseException e) {
            fail("Test failed: " + e.getMessage());
        }

        String[] args2 = new String[]{"-t", "testValue1"};
        try {
            CommandLine cmd2 = parser.parse(Arrays.asList(args2), new DefaultOptions());
            assertEquals("testValue1", optionalOption.getValue(cmd2));
        } catch (ParseException e) {
            fail("Test failed: " + e.getMessage());
        }

        String[] args3 = new String[]{"-t", "testValue1", "testValue2"};
        try {
            CommandLine cmd3 = parser.parse(Arrays.asList(args3), new DefaultOptions());
            String[] optionValues = optionalOption.getValues(cmd3);
            assertEquals(2, optionValues.length);
            assertEquals("testValue1", optionValues[0]);
            assertEquals("testValue2", optionValues[1]);
        } catch (ParseException e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testValueSeparator() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option valueSeparatorOption = optionBuilder.withLongOpt("testOption")
                .isRequired()
                .hasArg()
                .withArgName("testValue")
                .withValueSeparator(':')
                .create('t');
        CommandLineParser parser = new DefaultParser();

        String[] args = new String[]{"-t", "testValue1:testValue2"};
        try {
            CommandLine cmd = parser.parse(Arrays.asList(args), new DefaultOptions());
            assertEquals("testValue1:testValue2", valueSeparatorOption.getValue(cmd));
        } catch (ParseException e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testUninitializedOption() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option uninitializedOption = optionBuilder.create('u');

        CommandLineParser parser = new DefaultParser();

        String[] args = new String[]{"-u"};
        try {
            parser.parse(Arrays.asList(args), new DefaultOptions());
            fail("Test failed: Option should not be initialized");
        } catch (ParseException e) {
            // expected
        }

        try {
            String value = uninitializedOption.getValue(null);
            fail("Test failed: Option should have no value");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testUninitializedRequiredOption() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option uninitializedRequiredOption = optionBuilder.isRequired()
                .create('u');

        CommandLineParser parser = new DefaultParser();

        String[] args = new String[0];
        try {
            parser.parse(Arrays.asList(args), new DefaultOptions());
            fail("Test failed: Option should require an argument");
        } catch (ParseException e) {
            // expected
        }

        try {
            String value = uninitializedRequiredOption.getValue(null);
            fail("Test failed: Option should have no value");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testInvalidOption() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option invalidOption = optionBuilder.create('i');

        CommandLineParser parser = new DefaultParser();

        String[] args = new String[0];
        try {
            parser.parse(Arrays.asList(args), new DefaultOptions());
            fail("Test failed: Option should not be recognized");
        } catch (ParseException e) {
            // expected
        }

        try {
            String value = invalidOption.getValue(null);
            fail("Test failed: Option should have no value");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}