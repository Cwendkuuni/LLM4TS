import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

/**
 * Test class for org.apache.commons.cli.Parser.
 */
public class ParserTest
{
    private static final String[] ARGS = new String[]{"--optionA", "valueA", "--optionB", "valueB", "--optionC", "--", "arg1", "arg2"};
    private static final String[] ARGS_WITHOUT_VALUE = new String[]{"--optionA", "--optionB", "--optionC", "--"};
    private static final String[] ARGS_WITH_LONG_OPTION = new String[]{"--longOption", "--"};
    private static final String[] ARGS_WITH_LONG_OPTION_AND_VALUE = new String[]{"--longOption", "value"};
    private static final String[] ARGS_WITH_INVALID_OPTION = new String[]{"--invalidOption", "--optionA"};
    private static final String[] ARGS_WITH_REQUIRED_OPTION_MISSING = new String[]{"--optionA"};

    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("optionA", true, "a");
        options.addOption("optionB", true, "b");
        options.addOption("optionC", true, "c");
        options.addOptionGroup(new OptionGroup("group", "Group options"));
        options.addOption(new Option("groupOption", "group", false, "g"));
        parser = new DefaultParser();
    }

    @After
    public void tearDown()
    {
        parser = null;
        options = null;
    }

    @Test
    public void testParse() throws ParseException
    {
        CommandLine commandLine = parser.parse(options, ARGS, false);

        assertEquals(3, commandLine.getArgs().size());
        assertEquals("valueA", commandLine.getOptionValue("optionA"));
        assertEquals("valueB", commandLine.getOptionValue("optionB"));
        assertEquals("value", commandLine.getOptionValue("optionC"));
        assertEquals("arg1", commandLine.getArgs().get(0));
        assertEquals("arg2", commandLine.getArgs().get(1));

        assertSame(options.getOption("optionA"), commandLine.getOption("optionA"));
        assertSame(options.getOption("optionB"), commandLine.getOption("optionB"));
        assertSame(options.getOption("optionC"), commandLine.getOption("optionC"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException
    {
        String[] expectedArgs = new String[ARGS.length - requiredOptions.size()];
        System.arraycopy(ARGS, requiredOptions.size(), expectedArgs, 0, expectedArgs.length);

        CommandLine commandLine = parser.parse(options, ARGS, false);

        assertEquals(ARGS.length, commandLine.getArgs().size());
        assertEquals(expectedArgs, commandLine.getArgs().toArray(expectedArgs));
        assertEquals(requiredOptions.size(), commandLine.getMissingOptions().size());

        for (Object missingOption : commandLine.getMissingOptions().toArray())
        {
            assertEquals(missingOption.toString(), ((Option) missingOption).getKey());
        }
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        CommandLine commandLine = parser.parse(options, ARGS_WITHOUT_VALUE, true);

        assertEquals(3 + ARGS_WITHOUT_VALUE.length, commandLine.getArgs().size());
        for (int i = 0; i < ARGS_WITHOUT_VALUE.length; i++)
        {
            assertEquals(ARGS_WITHOUT_VALUE[i], commandLine.getArgs().get(i + 2));
        }
    }

    @Test
    public void testParseWithLongOption() throws ParseException
    {
        CommandLine commandLine = parser.parse(options, ARGS_WITH_LONG_OPTION, false);

        assertNull(commandLine.getOptionValue("optionA"));
        assertNull(commandLine.getOptionValue("optionB"));
        assertNull(commandLine.getOptionValue("optionC"));
        assertFalse(commandLine.hasOption("optionA"));
        assertFalse(commandLine.hasOption("optionB"));
        assertFalse(commandLine.hasOption("optionC"));
    }

    @Test
    public void testParseWithLongOptionAndValue() throws ParseException
    {
        CommandLine commandLine = parser.parse(options, ARGS_WITH_LONG_OPTION_AND_VALUE, false);

        assertEquals("value", commandLine.getOptionValue("optionC"));
        assertSame(options.getOption("optionC"), commandLine.getOption("optionC"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        parser.parse(options, ARGS_WITH_INVALID_OPTION, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOption() throws ParseException
    {
        parser.parse(options, ARGS_WITH_REQUIRED_OPTION_MISSING, false);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException
    {
        Option option = options.getOption("optionC");
        option.setArgName("arg");

        parser.parse(options, ARGS, false);

        Option missingArgumentOption = parser.parse(options, new String[]{"--optionC"}, false).getMissingOptions().get(0);

        assertEquals("Missing argument for option: optionC", missingArgumentOption.getMessage());
    }
}