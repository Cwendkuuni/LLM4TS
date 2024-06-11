package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionsTest
{
    @Test
    public void testAddOption()
    {
        Options options = new Options();
        Option opt = new Option("a", "somearg", true, "Some option description");
        options.addOption(opt);

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("a", true));
        assertFalse(options.hasOption("z"));
        assertEquals("Some option description", options.getOption("a").getDescription());
    }

    @Test
    public void testAddOptionWithArgument()
    {
        Options options = new Options();
        Option opt = new Option("b", "somearg", true, "Some option description");
        options.addOption(opt);

        assertTrue(options.hasOption("b"));
        assertTrue(options.hasOption("b", true));
        assertNull(options.getOption("b", false));
    }

    @Test
    public void testAddOptionWithNoArgument()
    {
        Options options = new Options();
        Option opt = new Option("c", true, "Some option description");
        options.addOption(opt);

        assertTrue(options.hasOption("c"));
        assertTrue(options.hasOption("c", false));
        assertNull(options.getOption("c", true));
    }

    @Test
    public void testAddOptionGroup()
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "My option group");

        options.addOptionGroup(group);
        assertTrue(options.hasOptionGroup("group"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateOption()
    {
        Options options = new Options();
        Option opt = new Option("a", true, "Some option description");
        options.addOption(opt);
        options.addOption(opt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateLongOption()
    {
        Options options = new Options();
        Option opt = new Option(null, "somearg", true, "Some option description");
        options.addOption(opt);
        options.addOption(opt);
    }

    @Test
    public void testGetRequiredOptions()
    {
        Options options = new Options();
        Option opt1 = new Option("a", true, "Some option description");
        Option opt2 = new Option("b", true, "Another option description");
        options.addOption(opt1);
        options.addOption(opt2);

        List<String> requiredOptions = options.getRequiredOptions();

        assertEquals(2, requiredOptions.size());
        assertTrue(requiredOptions.contains("a"));
        assertTrue(requiredOptions.contains("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption()
    {
        Options options = new Options();
        Option opt = new Option("a", true, "Some option description");
        options.addOption(opt);

        String[] args = {"myprogram", "--b"};
        options.parse(args);

        // ParseException should be thrown because option 'a' is required but not present
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption()
    {
        Options options = new Options();
        Option opt = new Option("a", true, "Some option description");
        options.addOption(opt);

        String[] args = {"myprogram", "--z"};
        options.parse(args);

        // ParseException should be thrown because option 'z' does not exist
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidLongOption()
    {
        Options options = new Options();
        Option opt = new Option(null, "somearg", true, "Some option description");
        options.addOption(opt);

        String[] args = {"myprogram", "--longOption"};
        options.parse(args);

        // ParseException should be thrown because long option 'longOption' does not exist
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidShortOption()
    {
        Options options = new Options();
        Option opt = new Option("z", true, "Some option description");
        options.addOption(opt);

        String[] args = {"myprogram", "--x"};
        options.parse(args);

        // ParseException should be thrown because short option 'x' does not exist
    }

    @Test(expected = ParseException.class)
    public void testParseWithMixedArguments()
    {
        Options options = new Options();
        Option opt = new Option("a", true, "Some option description");
        Option optGroup = new OptionGroup("group", "My option group");
        Option optionInGroup = new Option("g", true, "Option in group", optGroup);

        options.addOption(opt);
        options.addOptionGroup(optGroup);
        options.addOption(optionInGroup);

        String[] args = {"myprogram", "--a", "value", "--g"};
        options.parse(args);

        // ParseException should be thrown because there are positional arguments present
    }

    @Test
    public void testParseWithValidArguments()
    {
        Options options = new Options();
        Option opt = new Option("a", true, "Some option description");
        Option optGroup = new OptionGroup("group", "My option group");
        Option optionInGroup = new Option("g", true, "Option in group", optGroup);

        options.addOption(opt);
        options.addOptionGroup(optGroup);
        options.addOption(optionInGroup);

        String[] args = {"myprogram", "--a", "value", "--g"};
        CommandLine parsedArgs = options.parse(args);

        assertEquals("value", parsedArgs.getOptionValue("a"));
        assertTrue(parsedArgs.hasOption("g"));
    }

    @Test
    public void testGetMatchingOptions()
    {
        Options options = new Options();
        Option opt1 = new Option("o", "opt1");
        Option opt2 = new Option("oo", "opt2");
        Option opt3 = new Option("app", "appOption");

        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);

        List<String> matching = options.getMatchingOptions("o");
        assertEquals(2, matching.size());
        assertTrue(matching.contains("o"));
        assertTrue(matching.contains("oo"));

        matching = options.getMatchingOptions("app");
        assertEquals(1, matching.size());
        assertTrue(matching.contains("app"));
    }
}