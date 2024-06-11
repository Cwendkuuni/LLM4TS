import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.commandline.Parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;
    private Parser parser;

    @Before
    public void setUp() {
        List<Option> optionsList = new ArrayList<>();

        Option groupOption1 = new Option("g1", "Group 1")
                .required(false)
                .args(new Option("arg1", true, "Arg 1"))
                .args(new Option("arg2", true, "Arg 2"));
        optionsList.add(groupOption1);

        Option groupOption2 = new Option("g2", "Group 2")
                .required(true)
                .args(new Option("arg3", true, "Arg 3"));

        Option option1 = new Option("o1", "Option 1");
        Option option2 = new Option("o2", "Option 2");

        options = new Options();
        options.addOption(groupOption1);
        options.addOption(groupOption2);
        options.addOption(option1);
        options.addOption(option2);

        parser = new Parser();
        parser.parse(options);
    }

    @Test
    public void testCanProcessWithGroup() throws Exception {
        String[] args = {"--g1", "arg11", "--g1", "arg12", "--o1"};
        WriteableCommandLine cmd = new DefaultCommandLine();

        GroupImpl group1 = (GroupImpl) options.findOption("g1");
        assertEquals(true, group1.canProcess(cmd, args[0]));
        assertEquals(true, group1.canProcess(cmd, args[1]));
        assertEquals(false, group1.canProcess(cmd, args[2]));
        assertEquals(false, group1.canProcess(cmd, args[3]));
        assertEquals(true, option1.canProcess(cmd, args[4]));

        String[] argsWithBursting = {"--g1", "arg11", "--arg1", "value1", "--arg2", "value2", "--g1", "arg12", "--o1"};
        WriteableCommandLine cmdWithBursting = new DefaultCommandLine();

        for (String arg : argsWithBursting) {
            cmdWithBursting.parseArgument(arg);
        }

        GroupImpl group1WithBursting = (GroupImpl) options.findOption("g1");
        assertEquals(true, group1WithBursting.canProcess(cmdWithBursting, cmdWithBursting.getArguments().listIterator()));
    }

    @Test
    public void testProcessWithGroup() throws Exception {
        String[] args = {"--g1", "arg11", "--g1", "arg12", "--o1"};
        WriteableCommandLine cmd = new DefaultCommandLine();

        GroupImpl group1 = (GroupImpl) options.findOption("g1");
        group1.process(cmd, cmd.getArguments().listIterator());

        assertEquals(2, cmd.getOptions().size());
        assertEquals(group1.getOptions().get(0), cmd.getOptions().get(0));
        assertEquals(group1.getOptions().get(1), cmd.getOptions().get(1));
        assertEquals(group1.getAnonymous().get(0), cmd.getOptions().get(2));
        assertEquals(group1.getAnonymous().get(1), cmd.getOptions().get(3));
        assertEquals(option1, cmd.getOptions().get(4));

        assertEquals(0, cmd.getUnrecognized().size());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithGroupMinimum() throws Exception {
        String[] args = {"--g2"};
        WriteableCommandLine cmd = new DefaultCommandLine();

        GroupImpl group2 = (GroupImpl) options.findOption("g2");
        group2.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithGroupMaximum() throws Exception {
        String[] args = {"--g1", "arg11", "--g1", "arg11", "--g1", "arg11", "--o1"};
        WriteableCommandLine cmd = new DefaultCommandLine();

        GroupImpl group1 = (GroupImpl) options.findOption("g1");
        group1.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithGroupExtra() throws Exception {
        String[] args = {"--g1", "arg11", "--g1", "arg12", "--g1", "arg13", "--o1"};
        WriteableCommandLine cmd = new DefaultCommandLine();

        GroupImpl group1 = (GroupImpl) options.findOption("g1");
        group1.validate(cmd);
    }

    @Test
    public void testValidateWithGroupMinimumAndExtra() throws Exception {
        String[] args = {"--g1", "arg11", "--g1", "arg12"};
        WriteableCommandLine cmd = new DefaultCommandLine();

        GroupImpl group1 = (GroupImpl) options.findOption("g1");
        group1.validate(cmd);

        assertEquals(2, cmd.getOptions().size());
        assertEquals(group1.getOptions().get(0), cmd.getOptions().get(0));
        assertEquals(group1.getOptions().get(1), cmd.getOptions().get(1));
    }

    @Test
    public void testAppendUsage() {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);

        GroupImpl group1 = (GroupImpl) options.findOption("g1");

        group1.appendUsage(printWriter, DisplaySet.ALL, null);

        String expectedUsage = "--group1\n" +
                "  Group 1\n" +
                "  Arg 1:\n" +
                "    arg1 is required.\n" +
                "  Arg 2:\n" +
                "    arg2 is required.\n";

        assertEquals(expectedUsage, writer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = new ArrayList<>();
        GroupImpl group1 = (GroupImpl) options.findOption("g1");

        group1.helpLines(0, DisplaySet.ALL, null, "\n", helpLines);

        assertEquals(5, helpLines.size());
        assertEquals("--group1 ARG_GROUP_ARG1 [arg1]\n" +
                "  Arg 1:\n" +
                "    arg1 is required.\n", helpLines.get(0).getHelp());
        assertEquals("--group1 ARG_GROUP_ARG2 [arg2]\n" +
                "  Arg 2:\n" +
                "    arg2 is required.\n", helpLines.get(1).getHelp());
        assertEquals("", helpLines.get(2).getHelp());
        assertEquals("--group1\n", helpLines.get(3).getHelp());
        assertEquals("", helpLines.get(4).getHelp());
    }
}