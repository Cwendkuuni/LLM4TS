import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;
    private CommandLine commandLine;

    @Before
    public void setUp() {
        List<Option> list = new ArrayList<>();
        list.add(Option.builder("a").longOpt("arg1").build());
        list.add(Option.builder("b").longOpt("arg2").hasArg().build());
        list.add(Option.builder("c").longOpt("arg3").valueOptional(true).build());
        list.add(Option.builder("g").longOpt("group").value("testGroup").group(true).build());
        list.add(Option.builder("x").longOpt("argX").build());

        options = new Options();
        options.addOptions(list);

        commandLine = new CommandLine();
    }

    @Test
    public void testGroupImplCanProcess() throws ParseException {
        Group group = (Group) options.findOption("g");
        assertTrue(group.canProcess(commandLine, "testGroup"));
        assertFalse(group.canProcess(commandLine, "arg1"));
        assertFalse(group.canProcess(commandLine, "arg2"));
        assertFalse(group.canProcess(commandLine, "arg3"));
        assertFalse(group.canProcess(commandLine, "x"));
    }

    @Test
    public void testGroupImplProcess() throws ParseException {
        Group group = (Group) options.findOption("g");

        // process all options in the group
        commandLine.parse(group.getOptions().toArray(new String[0]), true);
        assertEquals("testGroup", commandLine.getOptionValue("g"));
        assertEquals(3, commandLine.getOptions().size());

        // process a specific option
        commandLine.clearParsedOptions();
        commandLine.parse(new String[]{"--arg1", "--arg2=value", "--testGroup"}, true);
        assertEquals("value", commandLine.getOptionValue("arg2"));

        // test options within the group
        assertEquals("value", commandLine.getOptionValue("arg2"));
        assertNull(commandLine.getOptionValue("argX"));
        assertNull(commandLine.getOptionValue("arg1"));
    }

    @Test
    public void testGroupImplValidate() throws ParseException {
        Group group = (Group) options.findOption("g");

        commandLine.clearParsedOptions();

        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            assertTrue(e.getMessage().startsWith("Missing required option: "));
            throw e;
        }

        commandLine.parse(new String[]{"--testGroup"}, true);
        group.validate(commandLine);

        commandLine.clearParsedOptions();
        commandLine.parse(new String[]{"--testGroup", "--arg1"}, true);
        group.validate(commandLine);

        commandLine.clearParsedOptions();
        commandLine.parse(new String[]{"--testGroup", "--arg1", "--arg2"}, true);
        group.validate(commandLine);

        commandLine.clearParsedOptions();
        commandLine.parse(new String[]{"--testGroup", "--arg1", "--arg2", "--arg3"}, true);
        group.validate(commandLine);

        commandLine.clearParsedOptions();
        commandLine.parse(new String[]{"--testGroup", "--arg1", "--arg2", "--arg3", "--argX"}, true);
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Unrecognized option: --argX", e.getMessage());
        }

        commandLine.clearParsedOptions();
        commandLine.parse(new String[]{"--testGroup", "--arg1", "--arg2", "--arg3", "--arg3"}, true);
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Duplicate option: --arg3", e.getMessage());
        }

        commandLine.clearParsedOptions();
        commandLine.parse(new String[]{"--testGroup", "--arg1", "--arg2", "--arg3", "--arg3", "--argX"}, true);
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            assertEquals("Unrecognized option: --argX", e.getMessage());
        }
    }

    @Test
    public void testGroupImplUsage() {
        Group group = (Group) options.findOption("g");

        StringWriter sw = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
        String usage = sw.toString();

        String expected = "Test:\n" +
                "Options:\n" +
                "  -a, --arg1\n" +
                "      arg1\n" +
                "  -b, --arg2=ARG\n" +
                "      arg2\n" +
                "  -c, --arg3=ARG\n" +
                "      arg3 (default: null)\n" +
                "  -g, --group=GROUP\n" +
                "      group\n" +
                "  -x, --argX\n" +
                "      argX\n";

        assertTrue(expected.equalsIgnoreCase(usage));

        sw.getBuffer().setLength(0);
        formatter.printHelp("Test", options, true);
        usage = sw.toString();

        expected = "Test:\n" +
                "Options:\n" +
                "  -a <arg1>\n" +
                "  -b ARG\n" +
                "  -c [ARG]\n" +
                "  -g GROUP\n" +
                "  -x\n";

        assertTrue(expected.equalsIgnoreCase(usage));

        sw.getBuffer().setLength(0);
        formatter.printHelp("Test", group.helpLines(0, Collections.emptySet(), null), true);
        usage = sw.toString();

        expected = "group:\n" +
                "Options:\n" +
                "  -a <arg1>\n" +
                "  -b ARG\n" +
                "  -c [ARG]\n";

        assertTrue(expected.equalsIgnoreCase(usage));
    }
}