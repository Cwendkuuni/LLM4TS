import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseResult;
import org.apache.commons.cli2.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class GroupImplTest {

    private CommandLine commandLine;
    private HelpFormatter helpFormatter;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
        helpFormatter = new HelpFormatter();
    }

    // Test case for the constructor
    @Test
    public void testGroupImplConstructor() {
        List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("option1", "desc1", false));
        options.add(TestUtils.createOption("option2", "desc2", true));

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 2);

        Assert.assertEquals("groupName", group.getPreferredName());
        Assert.assertEquals(2, group.getOptions().size());
        Assert.assertEquals(1, group.getMinimum());
        Assert.assertEquals(2, group.getMaximum());
    }

    // Test case for canProcess method with valid options
    @Test
    public void testCanProcessWithValidOptions() {
        List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("option1", "desc1", false));
        options.add(TestUtils.createOption("option2", "desc2", true));

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 2);

        String[] args = {"--option1", "--option2"};

        Assert.assertTrue(group.canProcess(commandLine, args));
    }

    // Test case for canProcess method with invalid options
    @Test
    public void testCanProcessWithInvalidOptions() {
        List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("option1", "desc1", false));
        options.add(TestUtils.createOption("option2", "desc2", true));

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 2);

        String[] args = {"--option1", "--option1", "--option3"};

        Assert.assertFalse(group.canProcess(commandLine, args));
    }

    // Test case for process method with valid options
    @Test
    public void testProcessWithValidOptions() {
        List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("option1", "desc1", false));
        options.add(TestUtils.createOption("option2", "desc2", true));

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 2);

        String[] args = {"--option1", "--option2"};

        group.process(commandLine, commandLine.parser().parse(args));

        Assert.assertEquals("option1=value1, option2=true", commandLine.toString());
    }

    // Test case for process method with invalid options
    @Test(expected = OptionException.class)
    public void testProcessWithInvalidOptions() {
        List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("option1", "desc1", false));
        options.add(TestUtils.createOption("option2", "desc2", true));

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 2);

        String[] args = {"--option1", "--option1", "--option3"};

        group.process(commandLine, commandLine.parser().parse(args));
    }

    // Test case for validate method with valid options
    @Test
    public void testValidateWithValidOptions() {
        List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("option1", "desc1", false));
        options.add(TestUtils.createOption("option2", "desc2", true));

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 2);

        String[] args = {"--option1", "--option2"};

        group.validate(commandLine);
    }

    // Test case for validate method with invalid options
    @Test(expected = OptionException.class)
    public void testValidateWithInvalidOptions() {
        List<Option> options = new ArrayList<>();
        options.append(TestUtils.createOption("option1", "desc1", false));
        options.append(TestUtils.createOption("option2", "desc2", true));

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 2);

        String[] args = {"--option1", "--option1", "--option3"};

        group.validate(commandLine);
    }

    // Test case for appendUsage method
    @Test
    public void testAppendUsage() {
        List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("option1", "desc1", false));
        options.add(TestUtils.createOption("option2", "desc2", true));

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 2);

        StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, null, null);

        String expected = "groupName:\n" +
                "  option1\n" +
                "  option2\n";

        Assert.assertEquals(expected, buffer.toString());
    }

    // Test case for helpLines method
    @Test
    public void testHelpLines() {
        List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("option1", "desc1", false));
        options.add(TestUtils.createOption("option2", "desc2", true));

        GroupImpl group = new GroupImpl(options, "groupName", null, 1, 2);

        String[] args = {"--help"};

        List<HelpLine> helpLines = group.helpLines(0, null, null);

        Assert.assertEquals(4, helpLines.size());

        String expected = "groupName:\n" +
                "Options:\n" +
                "========\n" +
                "option1\n" +
                "  desc1\n" +
                "option2\n" +
                "  desc2\n" +
                "  (required)\n";

        Assert.assertEquals(expected, helpLines.get(0).getShortOpt());
        Assert.assertEquals(expected, helpLines.get(0).getLongOpt());
        Assert.assertEquals(expected, helpLines.get(0).getDescription());
        Assert.assertEquals(false, helpLines.get(0).hasArg());

        expected = "option1:\n" +
                "  desc1\n";

        Assert.assertEquals(expected, helpLines.get(1).getShortOpt());
        Assert.assertEquals(expected, helpLines.get(1).getLongOpt());
        Assert.assertEquals(expected, helpLines.get(1).getDescription());
        Assert.assertEquals(false, helpLines.get(1).hasArg());

        expected = "option2:\n" +
                "  desc2\n" +
                "  (required)\n";

        Assert.assertEquals(expected, helpLines.get(2).getShortOpt());
        Assert.assertEquals(expected, helpLines.get(2).getLongOpt());
        Assert.assertEquals(expected, helpLines.get(2).getDescription());
        Assert.assertEquals(false, helpLines.get(2).hasArg());

        expected = "groupName:\n" +
                "Options:\n" +
                "========\n" +
                "  option1 [value]:\n" +
                "  option2\n";

        Assert.assertEquals(expected, helpLines.get(3).getShortOpt());
        Assert.assertEquals(expected, helpLines.get(3).getLongOpt());
        Assert.assertEquals(expected, helpLines.get(3).getDescription());
        Assert.assertEquals(true, helpLines.get(3).hasArg());
    }
}