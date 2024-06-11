import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse_BasicUsage() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] arguments = {"-a", "--b", "1"};
        Parser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, arguments, false);
            Assert.assertEquals(true, cmd.hasOption("a"));
            Assert.assertEquals(true, cmd.hasOption("b"));
            Assert.assertEquals("1", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            // should not reach here
            Assert.fail();
        }
    }

    @Test
    public void testParse_StopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] arguments = {"--", "-a", "1"};
        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, arguments, true);
            Assert.assertTrue(cmd.getArgs().length > 0);
            Assert.assertEquals("1", cmd.getArgs()[0]);
        } catch (ParseException e) {
            // should not reach here
            Assert.fail();
        }
    }

    @Test
    public void testParse_MissingRequiredOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A (required)");

        String[] arguments = {};
        Parser parser = new DefaultParser();

        try {
            parser.parse(options, arguments, false);
            Assert.fail();
        } catch (MissingOptionException e) {
            // expected
        }
    }

    @Test
    public void testParse_InvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"--invalid"};
        Parser parser = new DefaultParser();

        try {
            parser.parse(options, arguments, false);
            Assert.fail();
        } catch (UnrecognizedOptionException e) {
            // expected
        }
    }

    @Test
    public void testParse_OptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(new Option("a", true, "Option A"));
        group.addOption(new Option("b", true, "Option B"));
        options.addOptionGroup(group);

        String[] arguments = {"--group", "--a", "--b"};
        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, arguments, false);
            Assert.assertTrue(group.isSelected());
            Assert.assertEquals(true, cmd.hasOption("a"));
            Assert.assertEquals(true, cmd.hasOption("b"));
        } catch (ParseException e) {
            // should not reach here
            Assert.fail();
        }
    }

    @Test(expected = ParseException.class)
    public void testParse_InvalidArgumentValue() {
        Options options = new Options();
        options.addOption("a", 1, "Option A with argument");

        String[] arguments = {"-a", "invalid"};
        Parser parser = new DefaultParser();

        parser.parse(options, arguments, false);
    }

    @Test
    public void testParse_Properties() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        Properties properties = new Properties();
        properties.setProperty("b", "value");

        String[] arguments = {};
        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, arguments, properties, false);
            Assert.assertEquals(true, cmd.hasOption("a"));
            Assert.assertFalse(cmd.hasOption("b"));
        } catch (ParseException e) {
            // should not reach here
            Assert.fail();
        }
    }

    @Test
    public void testParse_StopAtNonOptionWithProperties() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        Properties properties = new Properties();
        properties.setProperty("b", "value");

        String[] arguments = {"--", "-a", "1"};
        Parser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, arguments, properties, true);
            Assert.assertTrue(cmd.getArgs().length > 0);
            Assert.assertEquals("1", cmd.getArgs()[0]);
        } catch (ParseException e) {
            // should not reach here
            Assert.fail();
        }
    }
}