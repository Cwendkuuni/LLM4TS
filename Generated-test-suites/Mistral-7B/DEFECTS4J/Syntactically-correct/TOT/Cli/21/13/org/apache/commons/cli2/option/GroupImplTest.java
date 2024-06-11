package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {
    private Options options;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        // create some test options
        final List<Option> optionsList = new ArrayList<>();
        optionsList.add(new Option("a", "optionA", false, "A test option."));
        optionsList.add(new Option("b", "optionB", true, "An optional test option."));
        optionsList.add(new Option("c", "optionC", false, "Another test option."));
        optionsList.add(new Option("d", "optionD", false, "A required test option."));
        optionsList.add(new Option("e", "optionE", false, "Another test option."));
        optionsList.add(new Option("f", "optionF", false, "A test option with a value."));
        optionsList.add(new Option("g", "optionG", false, "A test group.").setAllowedValues(new String[]{"value1", "value2"}));

        options = new Options();
        options.addOptionGroup(new GroupImpl(optionsList, "group", "A test group.", 1, Integer.MAX_VALUE, false));
    }

    @Test
    public void testParse_ValidUsage() throws ParseException {
        final CommandLine parsed = new CommandLine();
        options.parse(parsed, new String[]{"-a", "-c", "-g", "value1"});
        final HelpLine helpLine = options.getHelpLine();

        // test the parsed CommandLine
        assertEquals(true, parsed.hasOption("a"));
        assertEquals(true, parsed.hasOption("c"));
        assertEquals(true, parsed.hasOption("g"));
        assertEquals("value1", parsed.getValue("g"));
        assertFalse(parsed.hasOption("b"));
        assertFalse(parsed.hasOption("d"));
        assertFalse(parsed.hasOption("e"));
        assertFalse(parsed.hasOption("f"));

        // test the HelpLine output
        final String output = formatter.printHelp("test", helpLine);
        assertTrue(output.contains("A test group:"));
        assertTrue(output.contains("optionA"));
        assertTrue(output.contains("optionC"));
        assertTrue(output.contains("group (value1)"));
        assertFalse(output.contains("optionB"));
        assertFalse(output.contains("optionD"));
        assertFalse(output.contains("optionE"));
        assertFalse(output.contains("optionF"));
    }

    @Test
    public void testParse_MinimumUsage() throws ParseException {
        final CommandLine parsed = new CommandLine();
        options.parse(parsed, new String[]{"-a", "-c", "-g"});
        final HelpLine helpLine = options.getHelpLine();

        // test the parsed CommandLine
        assertEquals(true, parsed.hasOption("a"));
        assertEquals(true, parsed.hasOption("c"));
        assertEquals(true, parsed.hasOption("g"));
        assertNull(parsed.getValue("g"));

        // test the HelpLine output
        final String output = formatter.printHelp("test", helpLine);
        assertTrue(output.contains("A test group:"));
        assertTrue(output.contains("optionA"));
        assertTrue(output.contains("optionC"));
        assertTrue(output.contains("group"));
        assertFalse(output.contains("value1"));
        assertFalse(output.contains("value2"));
    }

    @Test(expected = ParseException.class)
    public void testParse_TooFewOptions() throws ParseException {
        final CommandLine parsed = new CommandLine();
        options.parse(parsed, new String[]{"-a"});
    }

    @Test(expected = ParseException.class)
    public void testParse_TooManyOptions() throws ParseException {
        final CommandLine parsed = new CommandLine();
        options.parse(parsed, new String[]{"-a", "-b", "-c", "-g", "value1", "-g", "value2"});
    }

    @Test
    public void testParse_InvalidOptionValue() throws ParseException {
        final CommandLine parsed = new CommandLine();
        final CommandLineParser parser = new DefaultParser();
        options.parse(parsed, new String[]{"-a", "-c", "-g", "invalid"});

        // test the parsed CommandLine
        assertEquals(true, parsed.hasOption("a"));
        assertEquals(true, parsed.hasOption("c"));
        assertEquals(true, parsed.hasOption("g"));
        assertNull(parsed.getValue("g"));

        // test the errors
        final List<ParseError> errors = parser.getParseErrors();
        assertEquals(1, errors.size());
        assertEquals("Invalid value for option 'g': invalid", errors.get(0).getMessage());
    }

    @Test
    public void testParse_InvalidOptionName() throws ParseException {
        final CommandLine parsed = new CommandLine();
        final CommandLineParser parser = new DefaultParser();
        options.parse(parsed, new String[]{"-x", "-c"});

        // test the parsed CommandLine
        assertFalse(parsed.hasOption("x"));
        assertFalse(parsed.hasOption("c"));

        // test the errors
        final List<ParseError> errors = parser.getParseErrors();
        assertEquals(1, errors.size());
        assertEquals("Unrecognized option: -x", errors.get(0).getMessage());
    }

    @Test
    public void testParse_InvalidOptionType() {
        try {
            final Option option = new Option("", "invalid", false, "An invalid option.");
        } catch (final IllegalArgumentException e) {
            // test the exception message
            assertEquals("Invalid option definition", e.getMessage());
            return;
        }
        throw new AssertionError("An exception should have been thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_EmptyOptionName() {
        final Option option = new Option("", "empty", false, "An empty option name.");
    }

    @Test
    public void testHelp() {
        final String output = formatter.printHelp("test", options);
        assertTrue(output.contains("test (no arguments)"));
        assertTrue(output.contains("options:"));
        assertTrue(output.contains("optionA"));
        assertTrue(output.contains("optionB"));
        assertTrue(output.contains("optionC"));
        assertTrue(output.contains("optionD"));
        assertTrue(output.contains("optionE"));
        assertTrue(output.contains("optionF"));
        assertTrue(output.contains("group (value1|value2)"));
    }
}