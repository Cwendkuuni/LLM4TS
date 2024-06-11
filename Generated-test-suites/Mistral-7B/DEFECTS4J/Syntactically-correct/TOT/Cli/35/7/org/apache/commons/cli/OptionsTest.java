import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testBasicOptions() throws ParseException {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file");
        options.addOption("o", "outputFile", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(), new String[]{"-i", "input.txt", "-o", "output.txt"});

        Assert.assertEquals("input.txt", cmd.getOptionValue("inputFile"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("outputFile"));
    }

    @Test
    public void testMissingRequiredOption() {
        Options options = new Options();
        options.addOptionGroup(new OptionGroup("g1", "group1")
                .required(true)
                .addOption("g", "group1Option", true, "group1 option")
        );

        CommandLineParser parser = new DefaultParser();

        try {
            parser.parse(options.toArray(), new String[0]);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Missing required option 'g1'", e.getMessage());
        }
    }

    @Test
    public void testOptionGroups() throws ParseException {
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("g1", "group1");
        group1.addOption(new Option("g1a", "group1a", true, "group1a option"));

        OptionGroup group2 = new OptionGroup("g2", "group2");
        group2.addOption(new Option("g2a", "group2a", true, "group2a option"));

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(), new String[]{"--group1", "group1aValue", "--group2", "group2aValue"});

        Assert.assertEquals("group1aValue", cmd.getOptionValue("g1a"));
        Assert.assertEquals("group2aValue", cmd.getOptionValue("g2a"));
    }

    @Test
    public void testLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(), new String[]{"--longOption", "longOptionValue"});

        Assert.assertEquals("longOptionValue", cmd.getOptionValue("longOption"));
    }

    @Test
    public void testMixedOptions() throws ParseException {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file");
        options.addOption("o", "outputFile", true, "output file");
        options.addOption("v", "verbose", false, "verbose output");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(), new String[]{"-i", "input.txt", "-o", "output.txt", "--verbose"});

        Assert.assertEquals("input.txt", cmd.getOptionValue("inputFile"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("outputFile"));
        Assert.assertTrue(cmd.hasOption("verbose"));
    }

    @Test
    public void testMissingShortOption() throws ParseException {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file");

        CommandLineParser parser = new DefaultParser();

        try {
            parser.parse(options.toArray(), new String[]{"-o", "output.txt"});
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: -o", e.getMessage());
        }
    }

    @Test
    public void testArgumentOptionalShortOption() throws ParseException {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(), new String[]{"-i"});

        Assert.assertNull(cmd.getOptionValue("inputFile"));
    }

    @Test
    public void testArgumentRequiredShortOption() throws ParseException {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file");
        options.addOption("o", "outputFile", true, "output file");

        CommandLineParser parser = new DefaultParser();

        try {
            parser.parse(options.toArray(), new String[]{"-i", "-o"});
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("The following options require an argument: [i, o]", e.getMessage());
        }
    }

    @Test
    public void testArgumentOptionalLongOption() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(), new String[0]);

        Assert.assertNull(cmd.getOptionValue("longOption"));
    }

    @Test
    public void testArgumentRequiredLongOption() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        CommandLineParser parser = new DefaultParser();

        try {
            parser.parse(options.toArray(), new String[0]);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("The following options require an argument: [longOption]", e.getMessage());
        }
    }
}