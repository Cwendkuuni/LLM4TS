package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Parser.
 */
public class ParserTest {

    @Test
    public void testParseSimple() {
        String[] args = new String[]{"-a", "1", "--optionB", "2"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        Option optionA = new Option("a", true, true, "Prints test A");
        Option optionB = new Option("b", true, false, "Prints test B");
        options.addOption(optionA);
        options.addOption(optionB);

        try {
            CommandLine cmd = parser.parse(options, args);

            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertFalse(cmd.getArgs().isEmpty());
            assertEquals(Arrays.asList("1"), cmd.getOptionValues("a"));
            assertEquals(Arrays.asList("2"), cmd.getOptionValues("b"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithLongOptions() {
        String[] args = new String[]{"--longOptionA", "1", "--longOptionB=2"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        Option longOptionA = new Option("longOptionA", true, true, "Prints test A");
        Option longOptionB = new Option("longOptionB", true, false, "Prints test B");
        options.addOption(longOptionA);
        options.addOption(longOptionB);

        try {
            CommandLine cmd = parser.parse(options, args);

            assertTrue(cmd.hasOption("longOptionA"));
            assertTrue(cmd.hasOption("longOptionB"));
            assertFalse(cmd.getArgs().isEmpty());
            assertEquals(Arrays.asList("1"), cmd.getOptionValues("longOptionA"));
            assertEquals(Arrays.asList("2"), cmd.getOptionValues("longOptionB"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithMultipleValuesForOption() {
        String[] args = new String[]{"-a", "1", "-a", "3", "--optionB", "2", "--optionB", "4"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        Option optionA = new Option("a", true, true, "Prints test A");
        Option optionB = new Option("b", true, true, "Prints test B");
        options.addOption(optionA);
        options.addOption(optionB);

        try {
            CommandLine cmd = parser.parse(options, args);

            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertFalse(cmd.getArgs().isEmpty());
            assertEquals(Arrays.asList("1", "3"), cmd.getOptionValues("a"));
            assertEquals(Arrays.asList("2", "4"), cmd.getOptionValues("b"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithRequiredOptions() {
        String[] args = new String[]{"-a", "1"};
        Options options = new Options();
        Option optionA = new Option("a", true, true, "Prints test A");
        Option optionB = new Option("b", true, true, "Prints test B");
        options.addOption(optionA);
        options.addOption(optionB);
        List<Option> requiredOptions = options.getRequiredOptions();

        try {
            CommandLine cmd = parser.parse(options, args);

            assertTrue(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("b"));
            assertFalse(requiredOptions.isEmpty());

            // Missing required option should throw an exception
            Parser parser2 = new DefaultParser();
            try {
                CommandLine cmd2 = parser2.parse(options, new String[0]);
                fail("Unexpected result");
            } catch (MissingOptionException e) {
                assertEquals("All required Options are not present: [b]", e.getMessage());
            }
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithUnrecognizedOption() {
        String[] args = new String[]{"--unknownOption", "1"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        Option optionA = new Option("a", true, true, "Prints test A");
        Option optionB = new Option("b", true, false, "Prints test B");
        options.addOption(optionA);
        options.addOption(optionB);

        try {
            CommandLine cmd = parser.parse(options, args);

            assertFalse(cmd.hasOption("unknownOption"));
            assertTrue(cmd.getArgs().isEmpty());

            // Unrecognized option should be thrown as an exception
            Parser parser2 = new DefaultParser();
            try {
                CommandLine cmd2 = parser2.parse(options, new String[]{"--unknownOption2"});
                fail("Unexpected result");
            } catch (UnrecognizedOptionException e) {
                assertEquals("Unrecognized option: unknownOption", e.getMessage());
            }
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithInvalidBooleanOption() {
        String[] args = new String[]{"--booleanOption=invalid"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        Option booleanOption = new Option("booleanOption", true, false, "Prints test");
        options.addOption(booleanOption);

        try {
            CommandLine cmd = parser.parse(options, args);

            assertFalse(cmd.hasOption("booleanOption"));
            assertTrue(cmd.getArgs().isEmpty());

            // Invalid boolean value should be thrown as an exception
            Parser parser2 = new DefaultParser();
            try {
                CommandLine cmd2 = parser2.parse(options, new String[]{"--booleanOption=invalidValue"});
                fail("Unexpected result");
            } catch (RuntimeException e) {
                assertEquals("Invalid boolean value: invalid, true or false expected", e.getMessage());
            }
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}