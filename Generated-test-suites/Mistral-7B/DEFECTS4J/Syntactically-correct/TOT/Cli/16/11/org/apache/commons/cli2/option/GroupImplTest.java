import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.List;

public class GroupImplTest {

    private Options options;

    @Before
    public void setUp() {
        List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(new Option("g", "group", false, "This is a group option"));
        groupOptions.add(new Option("a", "arg", 1, "This is an argument within the group"));
        groupOptions.add(new Option("g", "subgroup", 0, "This is a subgroup"));
        groupOptions.add(new Option("a", "subarg", 1, "This is an argument within the subgroup"));

        options = new Options();
        options.addGroup(new Group("My Group", "This is my group", groupOptions, 1, Integer.MAX_VALUE));
    }

    @Test
    public void testParsingValidCommandLine() {
        DefaultParser parser = new DefaultParser();
        CommandLine commandLine;

        try {
            commandLine = parser.parse(options, new String[]{"-g", "-a", "arg1", "-a", "arg2"});
            Assert.assertEquals("-g", commandLine.getOptionValue("group"));
            Assert.assertEquals("arg1", commandLine.getOptionValue("arg"));
            Assert.assertEquals("arg2", commandLine.getOptionValue("arg"));
        } catch (Exception e) {
            Assert.fail("Failed to parse valid command line: " + e.getMessage());
        }
    }

    @Test
    public void testParsingInvalidCommandLine() {
        DefaultParser parser = new DefaultParser();
        CommandLine commandLine;

        try {
            commandLine = parser.parse(options, new String[]{"-g", "-a", "arg1", "-a", "arg2", "-g"});
            Assert.fail("Expected OptionException to be thrown");
        } catch (OptionException e) {
            // Expected
        }

        try {
            commandLine = parser.parse(options, new String[]{"-g", "-a", "arg1", "-a", "arg2", "--"});
            Assert.fail("Expected OptionException to be thrown");
        } catch (OptionException e) {
            // Expected
        }

        try {
            commandLine = parser.parse(options, new String[]{"--", "-g", "arg", "-a", "arg2"});
            Assert.fail("Expected OptionException to be thrown");
        } catch (OptionException e) {
            // Expected
        }
    }

    @Test
    public void testHelp() {
        DefaultParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            commandLine = parser.parse(options, null, false, null);

            formatter.printHelp("GroupImplTest", options);
        } catch (Exception e) {
            Assert.fail("Failed to parse command line: " + e.getMessage());
        }

        String help = formatter.getHelpText();
        Assert.assertTrue(help.contains("This is my group"));
        Assert.assertTrue(help.contains("This is a group option"));
        Assert.assertTrue(help.contains("This is an argument within the group"));
    }
}