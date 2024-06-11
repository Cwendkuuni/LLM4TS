import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionShort() {
        options.addOption("a", "optionA", false, "Option A");
        List<Option> optionsList = options.helpOptions();
        Option option = optionsList.get(0);
        Assert.assertEquals("Option A", option.getLongOpt());
        Assert.assertEquals(false, option.hasArg());
        Assert.assertEquals('a', option.getKey()[0]);
    }

    @Test
    public void testAddOptionLong() {
        options.addOption("optionB", true, "Option B", "Option B description");
        List<Option> optionsList = options.helpOptions();
        Option option = optionsList.get(1);
        Assert.assertEquals("Option B", option.getKey());
        Assert.assertEquals("Option B", option.getLongOpt());
        Assert.assertEquals(true, option.hasArg());
    }

    @Test
    public void testAddOptionShortWithArg() {
        options.addOption("b", "optionB", true, "Option B with argument");
        List<Option> optionsList = options.helpOptions();
        Option option = optionsList.get(2);
        Assert.assertEquals("Option B with argument", option.getArgName());
        Assert.assertEquals('b', option.getKey()[0]);
        Assert.assertEquals(true, option.hasArg());
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("group", "Group options");
        options.addOptionGroup(group);
        List<Option> optionsList = options.helpOptions();
        Option option = group.getOptions().get(0);
        Assert.assertEquals("group", group.getKey());
        Assert.assertEquals("Group options", group.getLongOpt());
        Assert.assertEquals(false, option.hasArg());
        Assert.assertEquals('g', option.getKey()[0]);
    }

    @Test
    public void testHasOption() {
        options.addOption("a", "optionA", false, "Option A");
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.hasOption("optionA"));
    }

    @Test
    public void testHasLongOption() {
        options.addOption("a", "optionA", false, "Option A");
        Assert.assertTrue(options.hasLongOption("optionA"));
        Assert.assertFalse(options.hasLongOption("optionB"));
    }

    @Test
    public void testGetOption() {
        options.addOption("a", "optionA", false, "Option A");
        Option option = options.getOption("a");
        Assert.assertEquals("Option A", option.getLongOpt());
        Assert.assertEquals(false, option.hasArg());
    }

    @Test
    public void testGetMatchingOptions() {
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        List<String> matchingOptions = options.getMatchingOptions("a");
        Assert.assertEquals(1, matchingOptions.size());
        Assert.assertEquals("optionA", matchingOptions.get(0));

        matchingOptions = options.getMatchingOptions("ab");
        Assert.assertEquals(1, matchingOptions.size());
        Assert.assertEquals("optionA", matchingOptions.get(0));

        matchingOptions = options.getMatchingOptions("b");
        Assert.assertEquals(1, matchingOptions.size());
        Assert.assertEquals("optionB", matchingOptions.get(0));

        matchingOptions = options.getMatchingOptions("c");
        Assert.assertEquals(0, matchingOptions.size());
    }

    @Test
    public void testParse() {
        options.addOption("a", "optionA", false, "Option A");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a"};
        try {
            CommandLine cmd = parser.parse(options, args);
            Assert.assertTrue(cmd.hasOption("a"));
        } catch (ParseException e) {
            Assert.fail();
        }

        args = new String[]{"--optionA"};
        try {
            CommandLine cmd = parser.parse(options, args);
            Assert.assertTrue(cmd.hasOption("optionA"));
        } catch (ParseException e) {
            Assert.fail();
        }

        args = new String[]{"--optionB"};
        try {
            CommandLine cmd = parser.parse(options, args);
            Assert.assertFalse(cmd.hasOption("a"));
            Assert.assertFalse(cmd.hasOption("optionA"));
            Assert.assertTrue(cmd.hasOption("optionB"));
        } catch (ParseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testHelp() {
        options.addOption("a", "optionA", false, "Option A");
        CommandLineParser parser = new DefaultParser();
        String usage = options.getOptions().toString();
        String help = "java org.apache.commons.cli.OptionsTest -h\n";
        help += "\n";
        help += "Options:\n";
        help += usage + "\n";

        try {
            CommandLine cmd = parser.parse(options, new String[0]);
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("OptionsTest", usage, true);
            String output = new String(new byte[System.in.available()]);
            Assert.assertEquals(help, output);
        } catch (ParseException e) {
            Assert.fail();
        } catch (IOException e) {
            Assert.fail();
        }
    }
}