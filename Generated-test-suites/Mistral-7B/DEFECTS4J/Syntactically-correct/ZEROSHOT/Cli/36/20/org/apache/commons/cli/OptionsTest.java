import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testOptions() {
        Options options = new Options();

        // Test adding a single short option
        options.addOption("a", "option-a", false, "Option A");

        // Test adding a single long option
        options.addOption("b", "option-b", false, "Option B");

        // Test adding a short option with an argument
        options.addOption("c", "option-c", true, "Option C with argument");

        // Test adding a long option with an argument
        options.addOption("d", "option-d", true, "Option D with argument");

        // Test adding an option group
        OptionGroup group = new OptionGroup("group", "Option Group");
        group.addOption(new Option("e", "option-e", false, "Option E"));
        group.addOption(new Option("f", "option-f", false, "Option F"));
        options.addOptionGroup(group);

        // Test parsing with valid arguments
        String[] args = {"-a", "1", "-c", "2", "--option-d", "3", "--option-group"};
        CommandLine parsed = null;
        try {
            parsed = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            Assert.fail("Parsing failed: " + e.getMessage());
        }

        // Test getting options from parsed arguments
        Assert.assertTrue(parsed.hasOption("a"));
        Assert.assertTrue(parsed.hasOption("b"));
        Assert.assertFalse(parsed.hasOption("z"));
        Assert.assertEquals("1", parsed.getOptionValue("c"));
        Assert.assertEquals("3", parsed.getOptionValue("d"));
        Assert.assertNull(parsed.getOptionValue("e")); // Option E is in a group
        Assert.assertNull(parsed.getOptionValue("f")); // Option F is in a group

        // Test getting option group from an option
        Option optionE = parsed.getOption("group");
        Assert.assertNotNull(optionE);
        Assert.assertEquals("Option Group", optionE.getLongOpt());
        Option optionG = optionE.getValue(new String[0]);
        Assert.assertEquals("Option Group", optionG.getLongOpt());

        // Test getting matching options
        List<String> matchingOptions = options.getMatchingOptions("d");
        Assert.assertEquals(1, matchingOptions.size());
        Assert.assertEquals("option-d", matchingOptions.get(0));
    }
}