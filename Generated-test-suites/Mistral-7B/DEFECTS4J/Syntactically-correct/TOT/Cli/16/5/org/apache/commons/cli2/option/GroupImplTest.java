import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.options.GroupImpl;
import org.apache.commons.cli2.options.OptionBuilder;
import org.apache.commons.cli2.options.OptionFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroup() {
        final GroupImpl group = new GroupImpl(
                Arrays.asList(
                        OptionFactory.builder()
                                .longOpt("option1")
                                .hasArg(true)
                                .desc("Option 1 description")
                                .build(),
                        OptionFactory.builder()
                                .longOpt("option2")
                                .hasArg(false)
                                .desc("Option 2 description")
                                .build(),
                        OptionFactory.builder()
                                .longOpt("option3")
                                .hasArg(true)
                                .desc("Option 3 description")
                                .build(),
                        OptionFactory.builder()
                                .longOpt("option4")
                                .hasArg(false)
                                .desc("Option 4 description")
                                .build()),
                "MyGroup",
                "This is a group",
                1,
                4);

        final CommandLine cmd = new CommandLine();
        final HelpFormatter formatter = new HelpFormatter();

        try {
            group.parse(cmd, new String[]{"-option1", "value1", "-option2", "-option3", "-option4"});
            Assert.assertEquals("value1", cmd.getOptionValue("option1"));
            Assert.assertNull(cmd.getOptionValue("option2"));
            Assert.assertNull(cmd.getOptionValue("option3"));
            Assert.assertNull(cmd.getOptionValue("option4"));
        } catch (ParseException e) {
            formatter.printHelp("GroupImplTest", "", true);
            System.out.println("Error during parsing: " + e.getMessage());
            System.exit(1);
        }

        try {
            group.parse(cmd, new String[]{"--option1", "value1", "--option2"});
            Assert.assertEquals("value1", cmd.getOptionValue("option1"));
            Assert.assertNull(cmd.getOptionValue("option2"));
            Assert.assertNull(cmd.getOptionValue("option3"));
            Assert.assertNull(cmd.getOptionValue("option4"));
        } catch (ParseException e) {
            System.out.println("Missing required option: option3");
            System.exit(1);
        }

        try {
            group.parse(cmd, new String[]{"--option1", "value1", "--option3"});
            Assert.assertEquals("value1", cmd.getOptionValue("option1"));
            Assert.assertNull(cmd.getOptionValue("option2"));
            Assert.assertNull(cmd.getOptionValue("option3"));
            Assert.assertNull(cmd.getOptionValue("option4"));
        } catch (ParseException e) {
            System.out.println("Missing required option: option3");
            System.out.println("Or the specified value for option1 is invalid");
            System.exit(1);
        }

        try {
            group.parse(cmd, new String[]{"--option1", "invalid-value1", "--option3"});
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertTrue("Invalid value for option1: invalid-value1", e.getMessage().contains("invalid-value1"));
        }

        try {
            group.parse(cmd, new String[]{"--option1", "value1", "--option3=invalid-value3"});
            Assert.assertEquals("value1", cmd.getOptionValue("option1"));
            Assert.assertNull(cmd.getOptionValue("option2"));
            Assert.assertEquals("invalid-value3", cmd.getOptionValue("option3"));
            Assert.assertNull(cmd.getOptionValue("option4"));
        } catch (ParseException e) {
            System.out.println("Invalid value for option3: invalid-value3");
            System.exit(1);
        }

        try {
            group.parse(cmd, new String[]{"--option1=value1", "--option3=invalid-value3"});
            Assert.assertEquals("value1", cmd.getOptionValue("option1"));
            Assert.assertNull(cmd.getOptionValue("option2"));
            Assert.assertNull(cmd.getOptionValue("option3"));
            Assert.assertNull(cmd.getOptionValue("option4"));
        } catch (ParseException e) {
            System.out.println("Missing required option: option3");
            System.exit(1);
        }

        try {
            group.parse(cmd, new String[]{"--option1=value1", "--option3"});
            Assert.assertEquals("value1", cmd.getOptionValue("option1"));
            Assert.assertNull(cmd.getOptionValue("option2"));
            Assert.assertNull(cmd.getOptionValue("option3"));
            Assert.assertNull(cmd.getOptionValue("option4"));
        } catch (ParseException e) {
            System.out.println("Missing required option: option3");
            System.exit(1);
        }

        try {
            group.parse(cmd, new String[]{"--option1=value1"});
            Assert.assertEquals("value1", cmd.getOptionValue("option1"));
            Assert.assertNull(cmd.getOptionValue("option2"));
            Assert.assertNull(cmd.getOptionValue("option3"));
            Assert.assertNull(cmd.getOptionValue("option4"));
        } catch (ParseException e) {
            System.out.println("Missing required option: option3 or option4");
            System.exit(1);
        }

        try {
            group.parse(cmd, new String[]{"--option1=value1", "--option4"});
            Assert.assertEquals("value1", cmd.getOptionValue("option1"));
            Assert.assertNull(cmd.getOptionValue("option2"));
            Assert.assertNull(cmd.getOptionValue("option3"));
            Assert.assertNotNull(cmd.getOptionValue("option4"));
        } catch (ParseException e) {
            System.out.println("Missing required option: option3");
            System.exit(1);
        }

        try {
            group.parse(cmd, new String[]{"--option1=value1"});
            Assert.assertEquals("value1", cmd.getOptionValue("option1"));
            Assert.assertNull(cmd.getOptionValue("option2"));
            Assert.assertNull(cmd.getOptionValue("option3"));
            Assert.assertNull(cmd.getOptionValue("option4"));
        } catch (ParseException e) {
            System.out.println("Missing required option: option3 or option4");
            System.exit(1);
        }

        try {
            group.parse(cmd, new String[0]);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        try {
            group.parse(cmd, new String[]{"--option1=value1", "--option3=value3"});
            Assert.assertEquals("value1", cmd.getOptionValue("option1"));
            Assert.assertEquals("value3", cmd.getOptionValue("option3"));
            Assert.assertNull(cmd.getOptionValue("option2"));
            Assert.assertNull(cmd.getOptionValue("option4"));
        } catch (ParseException e) {
            System.out.println("Error during parsing: " + e.getMessage());
            System.exit(1);
        }

        final List options = group.getOptions();
        final List anonymous = group.getAnonymous();

        // test toString
        Assert.assertEquals("Group:MyGroup (This is a group)", group.toString());
        Assert.assertEquals("Option:option1 (Option 1 description)", ((Option) options.get(0)).toString());
        Assert.assertEquals("Option:option2 (Option 2 description)", ((Option) options.get(1)).toString());
        Assert.assertEquals("Option:option3 (Option 3 description)", ((Option) options.get(2)).toString());
        Assert.assertEquals("Option:option4 (Option 4 description)", ((Option) options.get(3)).toString());
        for (final Object option : anonymous) {
            Assert.assertEquals("Argument:<none>", option.toString());
        }

        group.validate(cmd);

        // test help usage
        final String helpText = new StringBuffer().append("\n").append(group.helpLines(0, null, null)).toString();
        Assert.assertTrue(helpText.contains("MyGroup"));
        Assert.assertTrue(helpText.contains("This is a group"));
        Assert.assertTrue(helpText.contains("Option 1 description"));
        Assert.assertTrue(helpText.contains("Option 2 description"));
        Assert.assertTrue(helpText.contains("Option 3 description"));
        Assert.assertTrue(helpText.contains("Option 4 description"));

        // test usage
        final String[] args = {"--option1=value1", "--option3=value3"};
        group.process(cmd, Arrays.asList(args).listIterator());

        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value3", cmd.getOptionValue("option3"));
        Assert.assertNull(cmd.getOptionValue("option2"));
        Assert.assertNull(cmd.getOptionValue("option4"));
    }
}