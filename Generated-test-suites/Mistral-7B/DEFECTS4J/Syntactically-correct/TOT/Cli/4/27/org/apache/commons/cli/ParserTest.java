package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParsingArgumentType;
import org.apache.commons.cli.ParseOptions;

/**
 * Test class for Parser.
 */
public class ParserTest {

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with no options and no arguments.
     * @throws ParseException
     */
    @Test
    public void testParseNoOptionsNoArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, null);
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(cmd.getOptions().isEmpty());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with valid options and arguments.
     * @throws ParseException
     */
    @Test
    public void testParseValidOptionsArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"-a", "1", "--optionB=2", "--optionC=3"};
        CommandLine cmd = parser.parse(options, arguments, null);

        Option optionA = options.getOption("a");
        assertEquals("1", optionA.getValue()[0]);
        Option optionB = options.getOption("optionB");
        assertEquals("2", optionB.getValue()[0]);
        Option optionC = options.getOption("optionC");
        assertEquals("3", optionC.getValue()[0]);

        assertEquals(Arrays.asList("-a", "--optionB", "--optionC"), Arrays.asList(cmd.getOptionNames()));
        assertEquals(Arrays.asList("1", "2", "3"), Arrays.asList(cmd.getArgStrings()));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with valid options and no arguments.
     * @throws ParseException
     */
    @Test
    public void testParseValidOptionsNoArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--optionA", "--optionB=value"};
        CommandLine cmd = parser.parse(options, arguments, null);

        Option optionA = options.getOption("optionA");
        assertTrue(optionA.isSet());
        Option optionB = options.getOption("optionB");
        assertEquals("value", optionB.getValue()[0]);
        assertEquals(Arrays.asList("--optionA", "--optionB"), Arrays.asList(cmd.getOptionNames()));
        assertTrue(cmd.getArgs().isEmpty());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with invalid options.
     * @throws ParseException
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--invalidOption"};
        CommandLine cmd = parser.parse(options, arguments, null);
        fail();
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with missing required options.
     * @throws ParseException
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--optionA"};
        CommandLine cmd = parser.parse(options, arguments, null);
        options.getOption("optionB").isRequired();
        parser.parse(options, arguments, null);
        fail();
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with duplicate options.
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testParseDuplicateOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--optionA", "--optionA"};
        CommandLine cmd = parser.parse(options, arguments, null);
        fail();
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with long options.
     * @throws ParseException
     */
    @Test
    public void testParseLongOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        String[] arguments = {"--longOptionA=1", "--longOptionB=2"};
        CommandLine cmd = parser.parse(options, arguments, null);

        Option optionA = options.getOption("longOptionA");
        assertEquals("1", optionA.getValue()[0]);
        Option optionB = options.getOption("longOptionB");
        assertEquals("2", optionB.getValue()[0]);

        assertEquals(Arrays.asList("--longOptionA", "--longOptionB"), Arrays.asList(cmd.getOptionNames()));
        assertTrue(cmd.getArgs().isEmpty());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with options with arguments.
     * @throws ParseException
     */
    @Test
    public void testParseOptionsWithArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        ParseOptions parseOptions = new ParseOptions();
        String[] arguments = {"--optionA=valueA", "--optionB=valueB"};
        CommandLine cmd = parser.parse(options, arguments, null);

        Option optionA = options.getOption("optionA");
        ParsingArgumentType argType = optionA.getArgumentType();
        assertEquals(argType.getClass(), String.class);
        assertEquals("valueA", argType.parse(optionA.getValue()[0]));

        Option optionB = options.getOption("optionB");
        argType = optionB.getArgumentType();
        assertEquals(argType.getClass(), String[].class);
        String[] parsedArgs = argType.parse(optionB.getValue()[0]);
        assertArrayEquals(new String[]{"valueB1", "valueB2"}, parsedArgs);

        assertEquals(Arrays.asList("--optionA", "--optionB"), Arrays.asList(cmd.getOptionNames()));
        assertTrue(cmd.getArgs().isEmpty());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with options in OptionGroups.
     * @throws ParseException
     */
    @Test
    public void testParseOptionsInOptionGroups() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        group.addOption(optionA);
        group.addOption(optionB);
        options.addOptionGroup(group);
        String[] arguments = {"--group", "--a", "--b"};
        CommandLine cmd = parser.parse(options, arguments, null);

        assertEquals(Arrays.asList("--group", "--a", "--b"), Arrays.asList(cmd.getOptionNames()));
        assertTrue(cmd.getArgs().isEmpty());
        assertTrue(optionA.isSet());
        assertTrue(optionB.isSet());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with options that require arguments.
     * @throws ParseException
     */
    @Test(expected = MissingArgumentException.class)
    public void testParseMissingRequiredArgument() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("a", "optionA");
        option.hasArg();
        options.addOption(option);
        String[] arguments = {"--optionA"};
        CommandLine cmd = parser.parse(options, arguments, null);
        fail();
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with complex arg parsing.
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testParseComplexArgumentParsing() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("a", "optionA");
        option.getLongOpt();
        option.valueSeparator(':');
        option.hasArg();
        option.setArgParser(new ArgParser());
        options.addOption(option);
        String[] arguments = {"--optionA=valueA:valueB"};
        CommandLine cmd = parser.parse(options, arguments, null);
        fail();
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with long options with arguments.
     * @throws ParseException
     */
    @Test
    public void testParseLongOptionsWithArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("longOptionA", "long option A")
                .longOpt("longOptionA")
                .hasArg();
        options.addOption(option);
        String[] arguments = {"--longOptionA=valueA"};
        CommandLine cmd = parser.parse(options, arguments, null);

        assertEquals(Arrays.asList("--longOptionA"), Arrays.asList(cmd.getOptionNames()));
        assertEquals("valueA", cmd.getOptionValue("longOptionA"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with multiple options.
     * @throws ParseException
     */
    @Test
    public void testParseMultipleOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        options.addOption(optionA);
        options.addOption(optionB);
        String[] arguments = {"--optionA", "--optionB"};
        CommandLine cmd = parser.parse(options, arguments, null);

        assertTrue(optionA.isSet());
        assertTrue(optionB.isSet());
        assertEquals(Arrays.asList("--optionA", "--optionB"), Arrays.asList(cmd.getOptionNames()));
        assertTrue(cmd.getArgs().isEmpty());
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)} with required and optional options.
     * @throws ParseException
     */
    @Test
    public void testParseRequiredAndOptionalOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        Option optionA = new Option("a", "optionA");
        Option optionB = new Option("b", "optionB");
        optionB.isRequired();
        options.addOption(optionA);
        options.addOption(optionB);
        String[] arguments = {"--optionA", "--optionB"};
        CommandLine cmd = parser.parse(options, arguments, null);

        assertTrue(optionA.isSet());
        assertTrue(optionB.isSet());
        assertEquals(Arrays.asList("--optionA", "--optionB"), Arrays.asList(cmd.getOptionNames()));
        assertTrue(cmd.getArgs().isEmpty());

        String[] arguments2 = {"--optionA"};
        try {
            parser.parse(options, arguments2, null);
            fail();
        } catch (MissingOptionException e) {
            // expected
        }
    }
}