import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parser.BasicParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private Options options;
    private Group group;

    @Before
    public void setUp() {
        List<Option> list = new ArrayList<>();
        list.add(new Option("g", "group", true, "group name")
                .required(false)
                .args(1)
        );
        list.add(new Option("a", "arg", true, "arg name")
                .required(false)
                .args(1)
        );
        list.add(new Option("o", "opt", true, "opt name")
                .required(false)
                .args(1)
        );
        list.add(new Option("h", "help", false, "print this help message")
        );

        options = new Options();
        options.addGroup(new Group("group", "Group test", list) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String arg) {
                return false;
            }
        });

        group = options.getOptions().get(0);
    }

    @Test
    public void testGetName() {
        assertEquals("group", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertNotNull(group.getDescription());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(0, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testCanProcess() {
        assertFalse(group.canProcess(new CommandLine(""), ""));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = new HashSet<>();
        triggers.add("g");
        assertEquals(triggers, group.getTriggers());
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = new HashSet<>();
        assertEquals(prefixes, group.getPrefixes());
    }

    @Test
    public void testGetOptions() {
        List<Option> optionsList = group.getOptions();
        assertEquals(1, optionsList.size());
        assertEquals(group, optionsList.get(0));
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymousList = group.getAnonymous();
        assertEquals(0, anonymousList.size());
    }

    @Test
    public void testFindOption() {
        Option option = new Option("o", "opt", true, "opt name")
                .required(false)
                .args(1);
        options.addOption(option);
        Option foundOption = group.findOption("opt");
        assertEquals(option, foundOption);
    }

    @Test
    public void testProcess() throws OptionException {
        CommandLine commandLine = new CommandLine("");
        String[] args = {"g", "groupName", "a", "argName", "o", "optName"};
        group.process(commandLine, Arrays.asList(args).listIterator());
        assertEquals("groupName", commandLine.getOptionValue("group"));
        assertEquals("argName", commandLine.getOptionValue("arg"));
        assertEquals("optName", commandLine.getOptionValue("opt"));
    }

    @Test
    public void testValidate() throws OptionException {
        CommandLine commandLine = new CommandLine("");
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingOption() throws OptionException {
        CommandLine commandLine = new CommandLine("");
        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, Collections.emptySet(), null);
        String expected = "group:Group test\n" +
                "  group\n" +
                "  arg NAME\n" +
                "  opt NAME\n";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLinesList = group.helpLines(0, Collections.emptySet(), null);
        int expectedSize = 5;
        assertEquals(expectedSize, helpLinesList.size());
        for (HelpLine helpLine : helpLinesList) {
            assertEquals("group", helpLine.getOption().getPreferredName());
            assertEquals("Group test", helpLine.getArgs()[0]);
        }
    }
}