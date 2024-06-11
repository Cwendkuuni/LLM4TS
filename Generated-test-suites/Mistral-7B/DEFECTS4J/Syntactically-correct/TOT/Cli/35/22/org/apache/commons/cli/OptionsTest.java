import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testSimpleOptions() {
        Options options = new Options();
        options.addOption("a", "annotation", true, "Add annotation to the JAR file");
        options.addOption("b", "boolean", false, "Boolean option");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a", "-b"});
            Assert.assertTrue(cmd.hasOption("a"));
            Assert.assertFalse(cmd.hasOption("b"));
            Assert.assertNull(cmd.getOptionValue("a"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parse failed: " + e.getMessage());
        }
    }

    @Test
    public void testOptionsWithValues() {
        Options options = new Options();
        options.addOption("c", "color", true, "Color option with a value");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"-c", "red"});
            Assert.assertTrue(cmd.hasOption("c"));
            Assert.assertEquals("red", cmd.getOptionValue("c"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parse failed: " + e.getMessage());
        }
    }

    @Test
    public void testLongOptions() {
        Options options = new Options();
        options.addOption("d", "debug", false, "Enable debug mode");
        options.addOption("e", "errors", true, "Display error messages");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"--debug", "--errors"});
            Assert.assertTrue(cmd.hasOption("d"));
            Assert.assertTrue(cmd.hasOption("e"));
            Assert.assertNull(cmd.getOptionValue("d"));
            Assert.assertNull(cmd.getOptionValue("e"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parse failed: " + e.getMessage());
        }
    }

    @Test
    public void testMixedOptions() {
        Options options = new Options();
        options.addOption("f", "force", false, "Force option");
        options.addOption("g", "group", true, "Group option with a value");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"-g", "test", "--force"});
            Assert.assertTrue(cmd.hasOption("g"));
            Assert.assertEquals("test", cmd.getOptionValue("g"));
            Assert.assertTrue(cmd.hasOption("force"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parse failed: " + e.getMessage());
        }
    }

    @Test
    public void testMissingOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "Display this help message");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"--version"});
            Assert.assertFalse(cmd.hasOptions());
            Assert.assertTrue(cmd.hasOption("h"));
            Assert.assertNull(cmd.getOptionValue("h"));
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: --version", e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testInvalidOptions() {
        Options options = new Options();
        options.addOption("i", "invalid", true, "Invalid option with a value");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options.toArray(), new String[]{"--invalid"});
    }

    @Test
    public void testGroups() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("testGroup", "Test group");
        group.addOption("j", "just", false, "Just test");
        group.addOption("k", "kill", false, "Kill test");

        options.addOption("l", "list", false, "List tests");
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options.toArray(), new String[]{"--testGroup", "--just"});
            Assert.assertTrue(cmd.hasOption("j"));
            Assert.assertFalse(cmd.hasOption("k"));
            Assert.assertFalse(cmd.hasOption("l"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parse failed: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testMixedGroups() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("testGroup", "Test group");
        group.addOption("m", "mixed", false, "Mixed option in group");

        options.addOption("n", "no", false, "No option");
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        parser.parse(options.toArray(), new String[]{"--mixed"});
    }
}