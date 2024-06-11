package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.UnrecognizedOptionException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    @org.junit.Test
    public void testParse() throws ParseException {
        String[] args = {"--option1", "optionValue1", "--option2", "optionValue2",
                "--option3", "optionValue3"};

        Options options = new Options();
        options.addOption("option1", true, "o1")
                .longOpt("longOption1")
                .valueSeparator(':')
                .numberOfValues(2)
                .desc("Option 1 description")
                .required(false);

        options.addOption("option2", false, "o2")
                .longOpt("longOption2")
                .valueSeparator(':')
                .numberOfValues(1)
                .desc("Option 2 description")
                .required(false);

        options.addOption("option3", true, "o3")
                .longOpt("longOption3")
                .valueSeparator(':')
                .numberOfValues(3)
                .desc("Option 3 description")
                .required(false);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true, true);

        assertEquals(3, cmd.getArgs().length);
        assertEquals("optionValue1", cmd.getOptionValue("option1"));
        assertEquals("optionValue2", cmd.getOptionValue("option2"));
        assertEquals("optionValue3", cmd.getOptionValue("option3"));
        assertEquals("value1", cmd.getOptionValue("option1", 1));
        assertEquals("value2", cmd.getOptionValue("option1", 2));
        assertNull(cmd.getOptionValue("option1", 3));
        assertEquals("value3", cmd.getOptionValue("option3", 0));
        assertEquals("value4", cmd.getOptionValue("option3", 1));
        assertEquals("value5", cmd.getOptionValue("option3", 2));
        assertNull(cmd.getOptionValue("nonexistentOption"));
        assertFalse(cmd.hasOption("nonexistentOption"));
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertTrue(cmd.hasOption("option3"));
        assertTrue(cmd.hasOption("longOption1"));
        assertTrue(cmd.hasOption("longOption2"));
        assertTrue(cmd.hasOption("longOption3"));
        assertEquals(3, cmd.getOptions().size());
    }

    @org.junit.Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"--option1", "--option2=value2", "--option3=value3"};

        Options options = new Options();
        options.addOption("option1", true, "o1")
                .longOpt("longOption1")
                .valueSeparator(':')
                .numberOfValues(2)
                .desc("Option 1 description")
                .required(false);

        options.addOption("option2", false, "o2")
                .longOpt("longOption2")
                .valueSeparator(':')
                .numberOfValues(1)
                .desc("Option 2 description")
                .required(false);

        options.addOption("option3", true, "o3")
                .longOpt("longOption3")
                .valueSeparator(':')
                .numberOfValues(3)
                .desc("Option 3 description")
                .required(false);

        Properties properties = new Properties();
        properties.setProperty("option1", "value1,value2");
        properties.setProperty("option2", "value3");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null, true);

        assertEquals(2, cmd.getArgs().length);
        assertEquals("value1", cmd.getOptionValue("option1", 0));
        assertEquals("value2", cmd.getOptionValue("option1", 1));
        assertEquals("value3", cmd.getOptionValue("option2"));
        assertNull(cmd.getOptionValue("nonexistentOption"));
        assertFalse(cmd.hasOption("nonexistentOption"));
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertTrue(cmd.hasOption("option3"));
        assertTrue(cmd.hasOption("longOption1"));
        assertTrue(cmd.hasOption("longOption2"));
        assertTrue(cmd.hasOption("longOption3"));
        assertEquals(3, cmd.getOptions().size());
    }

    @org.junit.Test
    public void testParseWithOptionalArg() throws ParseException {
        String[] args = {"--option1", "--option2=value2"};

        Options options = new Options();
        options.addOption("option1", true, "o1")
                .longOpt("longOption1")
                .desc("Option 1 description")
                .required(false);

        options.addOption("option2", true, "o2")
                .longOpt("longOption2")
                .desc("Option 2 description")
                .required(false)
                .hasArg(true);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true, true);

        assertEquals(0, cmd.getArgs().length);
        assertNull(cmd.getOptionValue("option2"));
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertTrue(cmd.getOption("option2").getValue().isEmpty());
        assertEquals(2, cmd.getOptions().size());
    }

    @org.junit.Test
    public void testParseWithRequiredArg() throws ParseException {
        String[] args = {"--option1", "--option2=value2"};

        Options options = new Options();
        options.addOption("option1", true, "o1")
                .longOpt("longOption1")
                .desc("Option 1 description")
                .required(false);

        options.addOption("option2", true, "o2")
                .longOpt("longOption2")
                .desc("Option 2 description")
                .required(true)
                .hasArg(true);

        Parser parser = new DefaultParser();
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, args, true, true));
    }

    @org.junit.Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = {"arg1", "--option1", "arg2", "--option2"};

        Options options = new Options();
        options.addOption("option1", true, "o1")
                .longOpt("longOption1")
                .desc("Option 1 description")
                .required(false);

        options.addOption("option2", true, "o2")
                .longOpt("longOption2")
                .desc("Option 2 description")
                .required(false)
                .hasArg(true);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false, true);

        assertEquals(3, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
        assertNull(cmd.getOptionValue("option1"));
        assertTrue(cmd.hasOption("option1"));
        assertNull(cmd.getOptionValue("option2"));
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
        assertTrue(cmd.getOption("option2").getValue().isEmpty());
        assertEquals(2, cmd.getOptions().size());
    }

    @org.junit.Test
    public void testParseWithHelp() throws ParseException {
        String[] args = {"--help"};

        Options options = new Options();
        options.addOption("help", false, null)
                .desc("Print this help message.");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true, true);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("parser", "Options:", options, false, false, false);

        String expectedHelpText = "parser (Options):\n" +
                "\n" +
                "optional arguments:\n" +
                "  --help\n" +
                "    Print this help message.\n";

        String helpText = new String(Arrays.stream(formatter.getHelpMessage().split("\n")).skip(1).limit(1).findFirst().orElse("")).trim();

        assertEquals(expectedHelpText, helpText);
    }

    @org.junit.Test
    public void testParseWithInvalidOption() throws ParseException {
        String[] args = {"--invalidOption"};

        Options options = new Options();

        Parser parser = new DefaultParser();
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, true, true));
    }
}