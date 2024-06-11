package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.apache.commons.cli.util.ArgList;
import org.junit.Test;

/**
 * Test cases for Parser class.
 */
public class ParserTest {

    /**
     * Test parse method with valid options and arguments.
     */
    @Test
    public void testParseValidOptionsAndArguments() {
        String[] args = new String[]{"-a", "1", "--optionB", "2", "--optionC=3",
                "--optionD", "-e"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", null, "Option D");
        options.addOption("e", false, "Option E");

        Parser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertTrue(cmd.hasOption("c"));
            assertEquals("1", cmd.getOptionValue("a"));
            assertEquals("2", cmd.getOptionValue("b"));
            assertEquals("3", cmd.getOptionValue("c"));
            assertNull(cmd.getOptionValue("d"));
            assertFalse(cmd.hasOption("d"));
            assertTrue(cmd.hasOption("e"));
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test parse method with invalid options.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOptions() {
        String[] args = new String[]{"-x", "--optionA", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.parse(options, args);
        fail("Exception should be thrown");
    }

    /**
     * Test parse method with missing required options.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() {
        String[] args = new String[]{"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addRequiredOption("b", true, "Required Option B");

        Parser parser = new DefaultParser();
        parser.parse(options, args);
        fail("Exception should be thrown");
    }

    /**
     * Test parse method with conflicting long and short options.
     */
    @Test(expected = AmbiguousOptionException.class)
    public void testParseConflictingOptions() {
        String[] args = new String[]{"-a", "-b", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.parse(options, args);
        fail("Exception should be thrown");
    }

    /**
     * Test parse method with mixed long and short options.
     */
    @Test
    public void testParseMixedOptions() {
        String[] args = new String[]{"-a", "--optionB", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertFalse(cmd.getArgList().isEmpty());
            assertEquals("1", cmd.getArgList().get(0));
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test parse method with multiple values for an option.
     */
    @Test
    public void testParseMultipleValuesForAnOption() {
        String[] args = new String[]{"-a", "1", "-a", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            ArgList argList = cmd.getArgList();
            assertTrue(argList.size() == 1);
            List<String> values = argList.getAll();
            assertTrue(values.size() == 2);
            assertEquals("1", values.get(0));
            assertEquals("2", values.get(1));
            assertTrue(cmd.hasOption("a"));
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test parse method with properties.
     */
    @Test
    public void testParseWithProperties() {
        String[] args = new String[]{"--optionA=1", "--optionB=2"};
        Properties properties = new Properties();
        properties.setProperty("optionA", "1");
        properties.setProperty("optionB", "2");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args, properties, true);
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("1", cmd.getOptionValue("a"));
            assertEquals("2", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test parse method with stopAtNonOption set to true.
     */
    @Test
    public void testParseWithStopAtNonOption() {
        String[] args = new String[]{"--optionA", "--optionB=2", "arg1",
                "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args, null, true);
            ArgList argList = cmd.getArgList();
            assertTrue(argList.size() == 2);
            List<String> values = argList.getAll();
            assertEquals("arg1", values.get(0));
            assertEquals("arg2", values.get(1));
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("2", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test parse method with stopAtNonOption set to false.
     */
    @Test
    public void testParseWithStopAtNonOptionFalse() {
        String[] args = new String[]{"--optionA", "--optionB=2", "arg1",
                "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args, null, false);
            ArgList argList = cmd.getArgList();
            assertTrue(argList.size() == 0);
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("2", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    /**
     * Test parse method with empty argument list.
     */
    @Test
    public void testParseWithEmptyArgumentList() {
        String[] args = new String[0];
        Options options = new Options();

        Parser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args, null, false);
            ArgList argList = cmd.getArgList();
            assertTrue(argList.size() == 0);
            assertTrue(cmd.getArgList().isEmpty());
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }
}