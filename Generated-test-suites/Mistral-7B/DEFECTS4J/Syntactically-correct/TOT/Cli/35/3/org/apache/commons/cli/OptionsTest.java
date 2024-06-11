import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OptionsTest {

    @Test
    public void testOptionsWithShortOption() {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file");

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-i", "input.txt"};
        try {
            CommandLine cmd = parser.parse(options.toBuilder().build(), args);
            Assert.assertEquals("input.txt", cmd.getOptionValue("inputFile"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Failed to parse command line");
        }
    }

    @Test
    public void testOptionsWithLongOption() {
        Options options = new Options();
        options.addOption("inputFile", true, "i", "Input file");

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"--inputFile", "input.txt"};
        try {
            CommandLine cmd = parser.parse(options.toBuilder().build(), args);
            Assert.assertEquals("input.txt", cmd.getOptionValue("inputFile"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Failed to parse command line");
        }
    }

    @Test
    public void testOptionsWithRequiredOption() {
        Options options = new Options();
        options.addOption("b", true, "b", "boolean option");
        options.addOption("b", true, "boolean required option");
        options.getOptionGroup(0).setRequired(true);

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"--booleanOption"};
        try {
            CommandLine cmd = parser.parse(options.toBuilder().build(), args);
            Assert.assertNotNull(cmd.getOptionValue("boolean required option"));
            Assert.assertTrue(cmd.hasOption("boolean option"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Failed to parse command line");
        }

        args = new String[]{"--booleanOption", "--inputFile", "input.txt"};
        try {
            CommandLine cmd = parser.parse(options.toBuilder().build(), args);
            Assert.assertNull(cmd.getOptionValue("boolean required option"));
            Assert.assertFalse(cmd.hasOption("boolean required option"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Failed to parse command line");
        }
    }

    @Test
    public void testOptionsWithOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "group options");
        group.addOption(new Option("g1", "group1Option", true, "group1 option"));

        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"--group1Option", "--inputFile", "input.txt"};
        try {
            CommandLine cmd = parser.parse(options.toBuilder().build(), args);
            Assert.assertEquals("input.txt", cmd.getOptionValue("inputFile"));
            Assert.assertEquals("input.txt", cmd.getOptionValue("group1Option"));
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Failed to parse command line");
        }
    }

    @Test(expected = ParseException.class)
    public void testOptionsWithInvalidOption() {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file");

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"-a", "input.txt"};
        parser.parse(options.toBuilder().build(), args);
    }
}