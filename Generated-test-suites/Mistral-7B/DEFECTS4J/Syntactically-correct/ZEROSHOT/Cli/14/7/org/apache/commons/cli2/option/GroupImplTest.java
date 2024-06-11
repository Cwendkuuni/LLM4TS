import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;
    private GroupImpl group;

    @Before
    public void setUp() {
        List<Option> optionsList = new ArrayList<>();

        Option option1 = Option.builder("o1")
                .longOpt("option-one")
                .required(false)
                .build();
        Option option2 = Option.builder("o2")
                .longOpt("option-two")
                .required(false)
                .build();
        Option option3 = Option.builder("o3")
                .longOpt("option-three")
                .required(false)
                .build();

        optionsList.add(option1);
        optionsList.add(option2);
        optionsList.add(option3);

        group = new GroupImpl(optionsList, "MyGroup", null, 1, 3);

        options = new Options();
        options.addAll(group.getOptions());
    }

    @Test
    public void testCanProcess() {
        CommandLine cmd = TestUtils.createCommandLine("foo", new String[0]);

        for (String arg : new String[]{"o1", "o2", "o3", "o1", "o2", "o1"}) {
            assertTrue(group.canProcess(cmd, arg));
        }

        assertFalse(group.canProcess(cmd, "invalid-option"));

        cmd = TestUtils.createCommandLine("foo", new String[]{"o1", "o1", "o1"});
        assertFalse(group.canProcess(cmd, "o4"));
    }

    @Test
    public void testValidate() throws OptionException {
        CommandLine cmd = TestUtils.createCommandLine("foo", new String[0]);

        group.validate(cmd);

        cmd = TestUtils.createCommandLine("foo", new String[]{"o1", "o2"});
        group.validate(cmd);

        cmd = TestUtils.createCommandLine("foo", new String[]{"o1", "o1", "o1"});
        group.validate(cmd);

        cmd = TestUtils.createCommandLine("foo", new String[]{"o1", "o1", "o1", "o1"});
        assertThrows(OptionException.class, () -> group.validate(cmd));

        cmd = TestUtils.createCommandLine("foo", new String[]{"invalid-option"});
        assertThrows(OptionException.class, () -> group.validate(cmd));
    }

    @Test
    public void testProcess() throws OptionException {
        CommandLine cmd = TestUtils.createCommandLine("foo", new String[0]);

        group.process(cmd, cmd.listIterator());

        assertEquals(0, cmd.getNumOptions());

        cmd = TestUtils.createCommandLine("foo", new String[]{"o1"});
        group.process(cmd, cmd.listIterator());

        assertEquals(1, cmd.getNumOptions());
        assertEquals(option1, cmd.getOption("o1"));

        cmd = TestUtils.createCommandLine("foo", new String[]{"o1", "o2"});
        group.process(cmd, cmd.listIterator());

        assertEquals(2, cmd.getNumOptions());
        assertEquals(option1, cmd.getOption("o1"));
        assertEquals(option2, cmd.getOption("o2"));

        cmd = TestUtils.createCommandLine("foo", new String[]{"o1", "o1", "o3"});
        group.process(cmd, cmd.listIterator());

        assertEquals(3, cmd.getNumOptions());
        assertEquals(option1, cmd.getOption("o1"));
        assertEquals(option3, cmd.getOption("o3"));

        cmd = TestUtils.createCommandLine("foo", new String[]{"o1", "o1", "o1", "o4"});
        group.process(cmd, cmd.listIterator());

        assertEquals(4, cmd.getNumOptions());
        assertEquals(option1, cmd.getOption("o1"));
        assertEquals(option1, cmd.getOption("o1"));
        assertEquals(option1, cmd.getOption("o1"));
        assertEquals(option4, cmd.getOption("o4"));
    }

    @Test
    public void testAppendUsage() {
        StringWriter writer = new StringWriter();

        group.appendUsage(writer, new HashSet<>(), null);

        assertEquals("MyGroup:\n" +
                "  This is a group\n" +
                "\n" +
                "Options:\n" +
                "=========\n" +
                "o1\n" +
                "  This is option one\n" +
                "o2\n" +
                "  This is option two\n" +
                "o3\n" +
                "  This is option three\n", writer.toString());
    }

    @Test
    public void testHelpLines() {
        StringWriter writer = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();

        group.helpLines(1, new HashSet<>(), null, writer);

        HelpLine helpLine = formatter.parseHelpLine(writer.toString(), 1);
        assertEquals("MyGroup", helpLine.getHeadText());
        assertEquals("This is a group", helpLine.getShortText());
        assertEquals("Options:", helpLine.getOptionsText());
        assertEquals(3, helpLine.getOptions().size());

        List<HelpLine> helpLines = formatter.parseHelp(writer.toString(), 1);
        assertEquals(3, helpLines.size());
        for (HelpLine line : helpLines) {
            assertEquals("  o1", line.getLongText());
            assertEquals("This is option one", line.getLongDescription());
        }
    }
}