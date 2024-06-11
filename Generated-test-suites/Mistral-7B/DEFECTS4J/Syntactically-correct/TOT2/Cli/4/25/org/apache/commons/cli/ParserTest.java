package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.junit.Test;

import java.util.*;

/**
 * Test cases for {@link Parser}.
 */
public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        // Test case for parsing simple options
        String[] args = {"-a", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertNull(cmd.getOptionValue("b"));
        assertNull(cmd.getOptionValue("c"));

        // Test case for parsing options with values
        args = new String[]{"-a", "--optionB=valueB", "--optionC=valueC"};
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        parser = new DefaultParser();
        cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertEquals("valueC", cmd.getOptionValue("c"));

        // Test case for parsing long options
        args = new String[]{"--longOptionA", "--longOptionB=valueB"};
        options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");

        parser = new DefaultParser();
        cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("longOptionA"));
        assertTrue(cmd.hasOption("longOptionB"));
        assertNull(cmd.getOptionValue("longOptionB"));
        assertNull(cmd.getOptionValue("longOptionC"));

        // Test case for parsing options with values
        args = new String[]{"--longOptionA", "--longOptionB=valueB", "--longOptionC=valueC"};
        options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");
        options.addOption("longOptionC", true, "Long Option C");

        parser = new DefaultParser();
        cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("longOptionA"));
        assertTrue(cmd.hasOption("longOptionB"));
        assertTrue(cmd.hasOption("longOptionC"));
        assertEquals("valueB", cmd.getOptionValue("longOptionB"));
        assertEquals("valueC", cmd.getOptionValue("longOptionC"));

        // Test case for parsing options with required arguments
        args = new String[]{"--requiredOption", "value"};
        options = new Options();
        options.addOption("requiredOption", true, "Required Option")
                .required(true)
                .argName("value");

        parser = new DefaultParser();
        cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("requiredOption"));
        assertEquals("value", cmd.getOptionValue("requiredOption"));

        // Test case for parsing options with optional arguments
        args = new String[]{"--optionalOption"};
        options = new Options();
        options.addOption("optionalOption", false, "Optional Option")
                .argName("value");

        parser = new DefaultParser();
        cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("optionalOption"));
        assertNull(cmd.getOptionValue("optionalOption"));

        // Test case for parsing multiple occurrences of the same option
        args = new String[]{"-optionA", "-optionA"};
        options = new Options();
        options.addOption("optionA", true, "Option A");

        parser = new DefaultParser();
        cmd = parser.parse(options, args, false);

        assertTrue(cmd.getNumOptions());
        assertEquals(2, cmd.getNumOptions());
        assertTrue(cmd.hasOption("optionA"));
        assertTrue(cmd.hasOption("optionA"));

        // Test case for parsing an empty args array
        args = new String[0];
        options = new Options();
        parser = new DefaultParser();
        cmd = parser.parse(options, args, false);
        assertEquals(0, cmd.getArgList().size());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        // Test case for parsing properties with simple options
        String[] args = {"-a", "--optionB=valueB"};
        Properties properties = new Properties();
        properties.setProperty("optionC", "valueC");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertNull(cmd.getOptionValue("b"));
        assertEquals("valueC", cmd.getOptionValue("optionC"));
        assertNull(cmd.getOptionValue("c"));

        // Test case for parsing properties with options with values
        args = new String[]{"-a", "--optionB=valueB", "--optionC=valueC"};
        properties = new Properties();
        properties.setProperty("optionC", "valueD");

        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        parser = new DefaultParser();
        cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertEquals("valueD", cmd.getOptionValue("c"));

        // Test case for parsing properties with long options
        args = new String[]{"--longOptionA", "--longOptionB=valueB"};
        properties = new Properties();
        properties.setProperty("longOptionC", "valueC");

        options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");

        parser = new DefaultParser();
        cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("longOptionA"));
        assertTrue(cmd.hasOption("longOptionB"));
        assertNull(cmd.getOptionValue("longOptionB"));
        assertNull(cmd.getOptionValue("longOptionC"));
        assertEquals("valueC", cmd.getOptionValue("longOptionC"));

        // Test case for parsing properties with options with required arguments
        args = new String[]{"--requiredOption", "value"};
        properties = new Properties();
        properties.setProperty("optionC", "valueC");

        options = new Options();
        options.addOption("requiredOption", true, "Required Option")
                .required(true)
                .argName("value");

        parser = new DefaultParser();
        cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("requiredOption"));
        assertEquals("value", cmd.getOptionValue("requiredOption"));
        assertEquals("valueC", cmd.getOptionValue("optionC"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = {"arg1", "--optionA", "arg2"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.getArgList().size() == 3);
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("--optionA", cmd.getArgList().get(1));
        assertEquals("arg2", cmd.getArgList().get(2));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A")
                .required(true);

        Parser parser = new DefaultParser();
        parser.parse(options, args, false);

        fail("MissingOptionException expected");
    }

    @Test
    public void testParseWithExtraArguments() throws ParseException {
        String[] args = {"arg1", "arg2", "arg3"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.getArgList().size() == 2);
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));

        List<String> remainingArgs = Arrays.asList(args).subList(2, args.length);
        assertEquals(remainingArgs, cmd.getArgs());
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        String[] args = {"--invalidOption"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");

        Parser parser = new DefaultParser();
        try {
            parser.parse(options, args, false);
            fail("UnrecognizedOptionException expected");
        } catch (UnrecognizedOptionException e) {
            // expected
        }
    }
}