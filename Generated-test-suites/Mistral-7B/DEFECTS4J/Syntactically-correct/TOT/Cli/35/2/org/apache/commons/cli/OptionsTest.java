import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testOptionsWithNoArguments() {
        Options options = new Options();
        options.addOption("a", "aliasA", false, "This is an option with no argument.");
        options.addOption("b", "aliasB", true, "This is an option with an argument.");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a"});
            assertTrue(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("b"));
            assertNull(cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionsWithArgument() {
        Options options = new Options();
        options.addOption("a", "aliasA", false, "This is an option with no argument.");
        options.addOption("b", "aliasB", true, "This is an option with an argument.");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a", "--aliasB=test"});
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("test", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionsWithLongArgument() {
        Options options = new Options();
        options.addOption("a", "aliasA", false, "This is an option with no argument.");
        options.addOption("b", "aliasB", true, "This is an option with a long argument.");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"--aliasB", "test value"});
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("test value", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionsWithRequiredOption() {
        Options options = new Options();
        options.addOption("a", "aliasA", true, "This is a required option.");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{});
            fail("Expected ParseException due to missing required option.");
        } catch (ParseException e) {
            // Expected
        }

        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a"});
            assertTrue(cmd.hasOption("a"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionsWithOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "This is an option group.");
        group.setRequired(false);
        options.addOptionGroup(group);
        group.addOption(new Option("g", "groupOption", true, "This is an option in the group."));

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"--groupOption", "test"});
            assertTrue(cmd.hasOption("g"));
            assertTrue(cmd.hasOption("group"));
            assertEquals("test", cmd.getOptionValue("groupOption"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionsWithMultipleOptions() {
        Options options = new Options();
        options.addOption("a", "aliasA", false, "This is an option with no argument.");
        options.addOption("b", "aliasB", true, "This is an option with an argument.");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a", "--aliasB=test"});
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("test", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionsWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", "aliasA", false, "This is an option with no argument.");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"--invalidOption"});
            fail("Expected ParseException due to invalid option.");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testOptionsWithMixedOptions() {
        Options options = new Options();
        options.addOption("a", "aliasA", false, "This is an option with no argument.");
        options.addOption("b", "aliasB", true, "This is an option with an argument.");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a", "--aliasB=test"});
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("test", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionsWithMixedShortAndLongOptions() {
        Options options = new Options();
        options.addOption("a", "aliasA", false, "This is an option with no argument.");
        options.addOption("b", "aliasB", true, "This is an option with an argument.");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a", "--aliasB=test"});
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("test", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"--aliasA", "-b", "test"});
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("test", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionsWithOptionGroupAndOptions() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "This is an option group.");
        group.setRequired(false);
        options.addOptionGroup(group);
        group.addOption(new Option("g", "groupOption", true, "This is an option in the group."));
        options.addOption("a", "aliasA", false, "This is an option with no argument.");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"--groupOption", "--aliasA"});
            assertTrue(cmd.hasOption("g"));
            assertTrue(cmd.hasOption("a"));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}