import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class OptionsTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();

        options.addOption("a", "alpha", true, "an option with a flag");
        options.addOption("b", "beta", false, "an option without a flag");
        options.addOptionGroup(new OptionGroup("group1", "Group 1")
                .addOption("g1a", "group1-option-a", true, "group option with flag")
                .addOption("g1b", "group1-option-b", false, "group option without flag")
                .addOption("g1c", "group1-option-c", false, "another group option")
                .required(true));

        options.addOptionGroup(new OptionGroup("group2", "Group 2")
                .addOption("g2a", "group2-option-a", true, "another group option with flag")
                .addOption("g2b", "group2-option-b", false, "another group option without flag")
                .required(false));
    }

    @Test
    public void testHasShortOption() {
        Assert.assertTrue(options.hasShortOption("a"));
        Assert.assertFalse(options.hasShortOption("z"));
    }

    @Test
    public void testHasLongOption() {
        Assert.assertTrue(options.hasLongOption("alpha"));
        Assert.assertFalse(options.hasLongOption("gamma"));
    }

    @Test
    public void testHasOption() {
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.hasOption("alpha"));
        Assert.assertFalse(options.hasOption("c"));
        Assert.assertFalse(options.hasOption("gamma"));
    }

    @Test
    public void testGetOption() {
        Option optionA = options.getOption("a");
        Option optionB = options.getOption("beta");
        Option optionG1A = options.getOption("g1a");

        Assert.assertNotNull(optionA);
        Assert.assertNotNull(optionB);
        Assert.assertNotNull(optionG1A);

        Assert.assertEquals("an option with a flag", optionA.getLongOpt());
        Assert.assertEquals(false, optionB.hasArg());
    }

    @Test
    public void testGetMatchingOptions() {
        List<String> optionsList = options.getMatchingOptions("g");

        Assert.assertEquals(2, optionsList.size());
        Assert.assertEquals("group1-option-a", optionsList.get(0));
        Assert.assertEquals("group2-option-a", optionsList.get(1));
    }

    @Test
    public void testHasOptionGroup() {
        OptionGroup group1 = options.getOptionGroup("group1");
        OptionGroup group2 = options.getOptionGroup("group2");

        Assert.assertNotNull(group1);
        Assert.assertNotNull(group2);

        Assert.assertTrue(group1.isRequired());
        Assert.assertFalse(group2.isRequired());
    }

    @Test
    public void testGetRequiredOptions() {
        List<Option> requiredOptions = options.getRequiredOptions();
        Map<String, Option> requiredOptionsMap = new HashMap<>();

        for (Option option : requiredOptions) {
            requiredOptionsMap.put(option.getLongOpt(), option);
        }

        Assert.assertEquals(2, requiredOptions.size());
        Assert.assertEquals("Group 1", requiredOptionsMap.get("group1-option-a").getLongOpt());
        Assert.assertEquals("Group 1", requiredOptionsMap.get("group1-option-b").getLongOpt());
    }

    @Test
    public void testParseOptions() throws ParseException {
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "--alpha", "-g1a", "--group1-option-a", "--group1-option-c"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("alpha"));
        Assert.assertTrue(cmd.hasOption("g1a"));
        Assert.assertFalse(cmd.hasOption("g1b"));
        Assert.assertFalse(cmd.hasOption("g1c"));
        Assert.assertFalse(cmd.hasOption("g2a"));
        Assert.assertFalse(cmd.hasOption("g2b"));
    }

    @Test(expected = ParseException.class)
    public void testParseOptionsWithMissingRequiredOptions() throws ParseException {
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "--alpha"};
        CommandLine cmd = parser.parse(options, args);

        // The test should throw an exception as 'group1-option-a' is required
    }
}