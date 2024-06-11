package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.ParseException;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test case for {@link GroupImpl}.
 */
public class GroupImplTest {

    /**
     * Test group with options.
     */
    @org.junit.Test
    public void testGroupWithOptions() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("longOpt1")
                .withDescription("Test long option 1")
                .create());
        options.add(OptionBuilder.withLongOpt("longOpt2")
                .withDescription("Test long option 2")
                .withOptionalArg(true)
                .create());
        options.add(OptionBuilder.withLongOpt("longOpt3")
                .withDescription("Test long option 3")
                .isRequired()
                .create());

        final OptionGroup group = new OptionGroupImpl(options, "TestGroup", "Test group");

        final CommandLine cmd = new CommandLine();
        group.parse(cmd, new String[]{"--longOpt1", "--longOpt2", "test"});

        // Check parsed values
        assertTrue(cmd.hasOption("longOpt1"));
        assertFalse(cmd.hasOption("longOpt2"));
        assertFalse(cmd.hasOption("longOpt3"));
        assertEquals("Test", cmd.getOptionValue("longOpt2"));

        // Test getOptions() method
        final List<Option> groupOptions = group.getOptions();
        assertEquals(3, groupOptions.size());
        assertSame(options.get(0), groupOptions.get(0));
        assertSame(options.get(1), groupOptions.get(1));
        assertSame(options.get(2), groupOptions.get(2));

        // Test getAnonymous() method (empty in this case)
        assertEquals(0, group.getAnonymous().size());

        // Test findOption() method
        final Option longOpt1 = group.findOption("longOpt1");
        assertSame(options.get(0), longOpt1);

        // Test hasOptions() method
        assertTrue(group.hasOptions());

        // Test validate() method
        group.validate(cmd);

        // Test parse() method with invalid arguments
        try {
            group.parse(cmd, new String[]{"--longOpt3"});
            fail();
        } catch (ParseException e) {
            assertEquals("Missing required option: longOpt3", e.getMessage());
        }

        // Test parse() method with too many arguments
        try {
            group.parse(cmd, new String[]{"--longOpt1", "--longOpt1", "--longOpt2"});
            fail();
        } catch (ParseException e) {
            assertEquals("Duplicate option: longOpt1", e.getMessage());
        }

        // Test appendUsage() method
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, null, null);
        assertEquals("--TestGroup  (Test group)\n" +
                "  --longOpt1  Test long option 1\n" +
                "  --longOpt2  Test long option 2 [VALUE]\n" +
                "  --longOpt3  Test long option 3 (required)\n", buffer.toString());

        // Test helpLines() method
        final List<HelpLine> helpLines = group.helpLines(0, null, null);
        assertEquals(3, helpLines.size());
        final String expectedHelpLine1 = "--TestGroup\n" +
                "  Test group\n" +
                "\n" +
                "  --longOpt1\n" +
                "  Test long option 1\n";
        final String expectedHelpLine2 = "--longOpt2\n" +
                "  Test long option 2 [VALUE]\n" +
                "\n" +
                "    Usage: test [options]";
        final String expectedHelpLine3 = "--longOpt3\n" +
                "  Test long option 3 (required)\n" +
                "\n" +
                "    Usage: test [options]";
        assertEquals(expectedHelpLine1, helpLines.get(0).getHelp());
        assertEquals(expectedHelpLine2, helpLines.get(1).getHelp());
        assertEquals(expectedHelpLine3, helpLines.get(2).getHelp());

        // Test default values
        group.defaults(cmd);
        assertFalse(cmd.hasOption("longOpt1"));
        assertFalse(cmd.hasOption("longOpt2"));
        assertFalse(cmd.hasOption("longOpt3"));
    }

    /**
     * Test group with anonymous arguments.
     */
    @org.junit.Test
    public void testGroupWithAnonymousArguments() {
        final List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("longOpt1")
                .withDescription("Test long option 1")
                .create());
        options.add(OptionBuilder.valueOpt("value")
                .withDescription("Test value option")
                .create());

        final OptionGroup group = new OptionGroupImpl(options, "TestGroup", "Test group");
        group.setMinimum(1);
        group.setMaximum(2);

        final CommandLine cmd = new CommandLine();
        group.parse(cmd, new String[]{"--longOpt1", "--value", "test"});

        // Check parsed values
        assertTrue(cmd.hasOption("longOpt1"));
        assertTrue(cmd.hasOption("value"));
        assertEquals("test", cmd.getOptionValue("value"));

        // Test getOptions() method
        final List<Option> groupOptions = group.getOptions();
        assertEquals(2, groupOptions.size());
        assertSame(options.get(0), groupOptions.get(0));
        assertSame(options.get(1), groupOptions.get(1));

        // Test getAnonymous() method
        final List<Option> anonymous = group.getAnonymous();
        assertEquals(1, anonymous.size());
        assertSame(options.get(1), anonymous.get(0));

        // Test findOption() method
        final Option longOpt1 = group.findOption("longOpt1");
        assertSame(options.get(0), longOpt1);

        // Test hasOptions() method
        assertTrue(group.hasOptions());

        // Test validate() method
        group.validate(cmd);

        // Test parse() method with invalid arguments
        try {
            group.parse(cmd, new String[]{"--longOpt1", "--value"});
            fail();
        } catch (ParseException e) {
            assertEquals("Missing required option: value", e.getMessage());
        }

        // Test parse() method with too many arguments
        try {
            group.parse(cmd, new String[]{"--longOpt1", "--value", "--longOpt1", "test2"});
            fail();
        } catch (ParseException e) {
            assertEquals("Duplicate option: longOpt1", e.getMessage());
        }

        // Test appendUsage() method
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, null, null);
        assertEquals("--TestGroup  (Test group)\n" +
                "  --longOpt1  Test long option 1\n" +
                "  --value  Test value option [VALUE]\n" +
                "\n" +
                "    Usage: test [options] [VALUE]\n", buffer.toString());

        // Test helpLines() method
        final List<HelpLine> helpLines = group.helpLines(0, null, null);
        assertEquals(3, helpLines.size());
        final String expectedHelpLine1 = "--TestGroup\n" +
                "  Test group\n" +
                "\n" +
                "  --longOpt1\n" +
                "  Test long option 1\n";
        final String expectedHelpLine2 = "--value\n" +
                "  Test value option [VALUE]\n" +
                "\n" +
                "    Usage: test [options] [VALUE]\n";
        final String expectedHelpLine3 = "--TestGroup\n" +
                "  Test group\n" +
                "\n" +
                "    Usage: test [options]\n";
        assertEquals(expectedHelpLine1, helpLines.get(0).getHelp());
        assertEquals(expectedHelpLine2, helpLines.get(1).getHelp());
        assertEquals(expectedHelpLine3, helpLines.get(2).getHelp());

        // Test default values
        group.defaults(cmd);
        assertFalse(cmd.hasOption("longOpt1"));
        assertFalse(cmd.hasOption("value"));
    }
}