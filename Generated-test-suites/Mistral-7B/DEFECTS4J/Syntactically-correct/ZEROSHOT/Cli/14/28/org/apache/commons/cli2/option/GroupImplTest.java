import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private Options options;
    private Group group;

    @Before
    public void setUp() {
        options = new Options();

        final List<Option> optionsList = new ArrayList<>();
        optionsList.add(new Option("a", "arg1", false, "description1"));
        optionsList.add(new Option("b", "arg2", true, "description2"));
        optionsList.add(new Option("c", "arg3", false, "description3"));
        optionsList.add(new OptionGroup("group1")
            .optional(true)
            .addOption(new Option("d", "arg4", false, "description4"))
            .addOption(new Option("e", "arg5", false, "description5"))
            .addOption(new Option("f", "arg6", false, "description6")));
        optionsList.add(new Option("g", "arg7", false, "description7"));

        group = new OptionGroup("groupName", "groupDescription")
            .optional(true)
            .addOptions(optionsList);

        options.addOptionGroup(group);
    }

    @Test
    public void testCanProcess() {
        final CommandLine commandLine = new CommandLine();

        commandLine.parse(options.getOptions());

        // test option processing
        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "b"));
        assertTrue(group.canProcess(commandLine, "c"));
        assertTrue(group.canProcess(commandLine, "d"));
        assertTrue(group.canProcess(commandLine, "e"));
        assertTrue(group.canProcess(commandLine, "f"));
        assertFalse(group.canProcess(commandLine, "g"));

        // test argument processing
        commandLine.parse(new String[]{"--arg1", "--arg2", "--arg3", "--arg4", "--arg5"});
        assertFalse(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "b"));
        assertTrue(group.canProcess(commandLine, "c"));
        assertTrue(group.canProcess(commandLine, "d"));
        assertTrue(group.canProcess(commandLine, "e"));
        assertTrue(group.canProcess(commandLine, "f"));
        assertFalse(group.canProcess(commandLine, "g"));

        commandLine.parse(new String[]{"--arg1", "--arg2", "--arg3"});
        assertFalse(group.canProcess(commandLine, "a"));
        assertFalse(group.canProcess(commandLine, "b"));
        assertFalse(group.canProcess(commandLine, "c"));
        assertTrue(group.canProcess(commandLine, "d"));
        assertTrue(group.canProcess(commandLine, "e"));
        assertTrue(group.canProcess(commandLine, "f"));
        assertFalse(group.canProcess(commandLine, "g"));
    }

    @Test
    public void testProcess() {
        final CommandLine commandLine = new CommandLine();

        // test option processing
        commandLine.parse(options.getOptions());

        group.process(commandLine, commandLine.iterator());

        assertEquals(5, commandLine.getOptions().size());
        assertSame(options.findOption("a"), commandLine.getOption("a"));
        assertSame(options.findOption("b"), commandLine.getOption("b"));
        assertSame(options.findOption("c"), commandLine.getOption("c"));
        assertSame(options.findOption("d"), commandLine.getOption("d"));
        assertSame(options.findOption("e"), commandLine.getOption("e"));
        assertNull(commandLine.getOption("f"));
        assertNull(commandLine.getOption("g"));

        // test argument processing
        commandLine.parse(new String[]{"--arg1", "--arg2", "--arg3", "--arg4"});

        group.process(commandLine, commandLine.iterator());

        assertEquals(6, commandLine.getOptions().size());
        assertSame(options.findOption("a"), commandLine.getOption("a"));
        assertSame(options.findOption("b"), commandLine.getOption("b"));
        assertSame(options.findOption("c"), commandLine.getOption("c"));
        assertSame(options.findOption("d"), commandLine.getOption("d"));
        assertSame(options.findOption("e"), commandLine.getOption("e"));
        assertSame(options.findOption("f"), commandLine.getOption("f"));
        assertNull(commandLine.getOption("g"));
    }

    @Test
    public void testValidate() {
        final CommandLine commandLine = new CommandLine();

        // test option processing with minimum=1 and maximum=1
        commandLine.parse(options.getOptions());

        try {
            group.validate(commandLine);
        } catch (final OptionException e) {
            assertEquals("Option 'groupName' defined twice", e.getMessage());
        }

        // test option processing with minimum=1 and maximum=3
        commandLine.parse(new String[]{"--arg1", "--arg2", "--arg1", "--arg3"});

        try {
            group.validate(commandLine);
        } catch (final OptionException e) {
            assertEquals("Option 'arg1' can appear a maximum of 1 times", e.getMessage());
        }

        // test option processing with minimum=0 and maximum=3
        commandLine.parse(new String[]{"--arg1", "--arg2", "--arg3", "--arg4"});

        try {
            group.validate(commandLine);
        } catch (final OptionException e) {
            assertNull(e);
        }

        // test argument processing (minimum=1, maximum=1)
        commandLine.parse(new String[]{"--arg1", "--arg2", "--arg3", "--arg4"});

        try {
            group.validate(commandLine);
        } catch (final OptionException e) {
            assertEquals("Option 'arg4' can appear a maximum of 1 times", e.getMessage());
        }

        // test argument processing (minimum=1, maximum=3)
        commandLine.parse(new String[]{"--arg1", "--arg2", "--arg3", "--arg4", "--arg5"});

        try {
            group.validate(commandLine);
        } catch (final OptionException e) {
            assertNull(e);
        }

        // test argument processing (minimum=0, maximum=3)
        commandLine.parse(new String[]{"--arg1", "--arg2", "--arg3", "--arg4", "--arg5"});

        try {
            group.validate(commandLine);
        } catch (final OptionException e) {
            assertNull(e);
        }

        // test argument processing (minimum=0, maximum=0)
        commandLine.parse(new String[]{"--arg1", "--arg2", "--arg3"});

        try {
            group.validate(commandLine);
        } catch (final OptionException e) {
            assertNull(e);
        }
    }

    @Test
    public void testGetPrefixes() {
        final Set<String> prefixes = group.getPrefixes();

        assertEquals(0, prefixes.size());
    }

    @Test
    public void testGetTriggers() {
        final Set<String> triggers = group.getTriggers();

        assertEquals(7, triggers.size());
        assertTrue(triggers.contains("a"));
        assertTrue(triggers.contains("b"));
        assertTrue(triggers.contains("c"));
        assertTrue(triggers.contains("d"));
        assertTrue(triggers.contains("e"));
        assertTrue(triggers.contains("f"));
        assertTrue(triggers.contains("group1"));
    }

    @Test
    public void testGetName() {
        assertEquals("groupName", group.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("groupDescription", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder buffer = new StringBuilder();

        group.appendUsage(buffer, null, null);

        assertEquals("groupDescription\n", buffer.toString());

        group.appendUsage(buffer, null, null, "\n");

        assertEquals("groupDescription\n\n", buffer.toString());

        group.appendUsage(buffer, null, null, "|");

        assertEquals("groupDescription\n\n" +
            "|--arg1\n" +
            "|    description1\n" +
            "|--arg2\n" +
            "|    description2\n" +
            "|--arg3\n" +
            "|    description3\n" +
            "|--group1\n" +
            "|    groupDescription\n" +
            "|--arg4\n" +
            "|    description4\n" +
            "|--arg5\n" +
            "|    description5\n" +
            "|--arg6\n" +
            "|    description6\n" +
            "|--arg7\n" +
            "|    description7\n", buffer.toString());

        group.appendUsage(buffer, null, null, "| ");

        assertEquals("groupDescription\n\n" +
            "|--arg1\n" +
            "|    description1\n" +
            "|--arg2\n" +
            "|    description2\n" +
            "|--arg3\n" +
            "|    description3\n" +
            "|--group1\n" +
            "|    groupDescription\n" +
            "|--arg4\n" +
            "|    description4\n" +
            "|--arg5\n" +
            "|    description5\n" +
            "|--arg6\n" +
            "|    description6\n" +
            "|--arg7\n" +
            "|    description7\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final StringBuilder buffer = new StringBuilder();

        group.appendUsage(buffer, null, null);

        final List<HelpLine> helpLines = group.helpLines(0, null, null);

        assertEquals(9, helpLines.size());
        assertEquals("groupDescription\n", helpLines.get(0).getHeader());
        assertEquals("", helpLines.get(0).getBody());
        assertEquals("", helpLines.get(0).getFooter());

        assertEquals("option group 'groupName'\n", helpLines.get(1).getHeader());
        assertEquals("groupDescription\n", helpLines.get(1).getBody());
        assertEquals("", helpLines.get(1).getFooter());

        final HelpLine helpLine3 = helpLines.get(2);
        assertEquals("option '--arg1' [-a]\n", helpLine3.getHeader());
        assertEquals("description1\n", helpLine3.getBody());
        assertEquals("", helpLine3.getFooter());

        final HelpLine helpLine4 = helpLines.get(3);
        assertEquals("option '--arg2' [-b]\n", helpLine4.getHeader());
        assertEquals("description2 (this value can be used multiple times)\n", helpLine4.getBody());
        assertEquals("", helpLine4.getFooter());

        final HelpLine helpLine5 = helpLines.get(4);
        assertEquals("option '--arg3' [-c]\n", helpLine5.getHeader());
        assertEquals("description3\n", helpLine5.getBody());
        assertEquals("", helpLine5.getFooter());

        final HelpLine helpLine6 = helpLines.get(5);
        assertEquals("group 'group1'\n", helpLine6.getHeader());
        assertEquals("groupDescription\n", helpLine6.getBody());
        assertEquals("", helpLine6.getFooter());

        final HelpLine helpLine7 = helpLines.get(6);
        assertEquals("option '--arg4' [-d]\n", helpLine7.getHeader());
        assertEquals("description4\n", helpLine7.getBody());
        assertEquals("", helpLine7.getFooter());

        final HelpLine helpLine8 = helpLines.get(7);
        assertEquals("option '--arg5' [-e]\n", helpLine8.getHeader());
        assertEquals("description5\n", helpLine8.getBody());
        assertEquals("", helpLine8.getFooter());

        final HelpLine helpLine9 = helpLines.get(8);
        assertEquals("option '--arg6' [-f]\n", helpLine9.getHeader());
        assertEquals("description6\n", helpLine9.getBody());
        assertEquals("", helpLine9.getFooter());
    }

    @Test
    public void testGetOptions() {
        assertEquals(optionsList, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(Collections.emptyList(), group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        assertSame(options.findOption("a"), group.findOption("a"));
        assertSame(options.findOption("b"), group.findOption("b"));
        assertSame(options.findOption("c"), group.findOption("c"));
        assertSame(options.findOption("d"), group.findOption("d"));
        assertSame(options.findOption("e"), group.findOption("e"));
        assertSame(options.findOption("f"), group.findOption("f"));
        assertNull(group.findOption("g"));
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
}