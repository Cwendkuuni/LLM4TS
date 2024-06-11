package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

/**
 * Test case for {@link Parser} class.
 */
public class ParserTest
{

    @Test
    public void testParse() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", 1, "option b");
        options.addOption("c", 1, "option c");
        options.addOption("d", 1, "option d");
        options.addOption("e", true, "option e");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"))
                .required(true).addOption("g1", true, "group 1 option g1")
                .addOption("g2", true, "group 1 option g2");
        options.addOptionGroup(new OptionGroup("group2", "Group 2"))
                .required(true).addOption("g3", true, "group 2 option g3")
                .addOption("g4", true, "group 2 option g4");

        Parser parser = new DefaultParser();
        String[] args = {"--a", "--b=3", "--c=4", "--d=5", "--e", "--group1", "--g1", "--g2", "--group2", "--g3", "--g4"};

        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("e"));
        assertTrue(cmd.hasOption("group1"));
        assertTrue(cmd.hasOption("g1"));
        assertTrue(cmd.hasOption("g2"));
        assertTrue(cmd.hasOption("group2"));
        assertTrue(cmd.hasOption("g3"));
        assertTrue(cmd.hasOption("g4"));
        assertEquals(3, cmd.getOptionValue("b", 0).intValue());
        assertEquals(4, cmd.getOptionValue("c", 0).intValue());
        assertEquals(5, cmd.getOptionValue("d", 0).intValue());
        assertNull(cmd.getOptionValue("e"));
        assertNull(cmd.getOptionValue("group1"));
        assertNull(cmd.getOptionValue("group2"));

        List<Option> group1Options = cmd.getOptions();
        Option g1 = null, g2 = null;
        for (Option option : group1Options)
        {
            if ("g1".equals(option.getKey()))
            {
                g1 = option;
            }
            if ("g2".equals(option.getKey()))
            {
                g2 = option;
            }
        }
        assertTrue(g1 != null);
        assertTrue(g2 != null);
        assertTrue(g1.isSelected());
        assertTrue(g2.isSelected());
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "option a");

        Parser parser = new DefaultParser();
        String[] args = {"--b"};

        parser.parse(options, args, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnknownOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "option a");

        Parser parser = new DefaultParser();
        String[] args = {"--x"};

        parser.parse(options, args, false);
    }

    @Test
    public void testParseStopAtNonOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "option a");

        Parser parser = new DefaultParser();
        String[] args = {"--a", "--", "non-option"};

        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.getArgs().size() > 0);
        assertTrue(cmd.getArgs().get(0).equals("non-option"));
    }

    @Test
    public void testParseRequiredOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");

        Parser parser = new DefaultParser();
        String[] args = {"--a"};

        try
        {
            parser.parse(options, args, false);
            fail();
        }
        catch (MissingOptionException e)
        {
            List<String> missingOptions = e.getMissingOptions();
            assertTrue(missingOptions.size() == 1);
            assertTrue(missingOptions.get(0).equals("b"));
        }
    }

    @Test
    public void testParseProperties() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");

        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("b", "value2");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties, false);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a").equals("value1"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.getOptionValue("b").equals("value2"));
    }

    @Test
    public void testParseOptionalProperty() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", false, "option a");

        Properties properties = new Properties();
        properties.setProperty("a", "value1");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0], properties, false);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a").equals("value1"));

        properties.clear();
        properties.setProperty("a", null);

        cmd = parser.parse(options, new String[0], properties, false);

        assertFalse(cmd.hasOption("a"));
    }
}

class DefaultParser extends Parser
{

    @Override
    protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption)
    {
        List<String> tokenList = new ArrayList<>();

        int i = 0;
        while (i < arguments.length)
        {
            String arg = arguments[i];

            if (arg.equals("--"))
            {
                if (stopAtNonOption)
                {
                    // do not add the remaining arguments if stopAtNonOption is true
                    return new String[0];
                }
                break;
            }

            if (arg.startsWith("-"))
            {
                Option option = options.findOption(arg.substring(1));
                if (option == null)
                {
                    throw new UnrecognizedOptionException("Unrecognized option: " + arg);
                }

                if (option.hasArg())
                {
                    // add the argument value if the option takes an argument
                    arg = arguments[++i];
                    tokenList.add(arg);
                }

                tokenList.add(arg);
                i++;
                continue;
            }

            tokenList.add(arg);
            i++;
        }

        return tokenList.toArray(new String[tokenList.size()]);
    }
}