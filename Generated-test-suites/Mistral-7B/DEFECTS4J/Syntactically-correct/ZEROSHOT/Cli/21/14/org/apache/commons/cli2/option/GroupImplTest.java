import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private Options options;
    private GroupImpl group;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption(new Option("a", "alpha", true, "alpha option")).required();
        options.addOption(new Option("b", "beta", false, "beta option")).required();
        options.addOption(new Option("c", "char", false, "character option"));
        options.addOption(new Option("d", "delta", false, "delta option"));
        options.addOption(new OptionBuilder("e")
                .longOpt("echo")
                .hasArg(true)
                .argName("string")
                .desc("print the given string")
                .build());
        options.addOptionGroup(new OptionGroup("g")
                .required(false)
                .title("group")
                .addOption(new Option("g1", "group-one", false, "group one option"))
                .addOption(new Option("g2", "group-two", false, "group two option"))
                .addOption(new Option("g3", "group-three", false, "group three option")))
                .addOption(new Option("h", "help", false, "print this help message"));

        group = new GroupImpl(options.getOptions(), "group", "Group of options", 1, Integer.MAX_VALUE, false);
    }

    @Test
    public void testCanProcess() {
        final CommandLine cmd = TestHelper.createCommandLine("-a --g g1", options);

        // test options
        assertTrue(group.canProcess(cmd, "a"));
        assertFalse(group.canProcess(cmd, "b"));
        assertFalse(group.canProcess(cmd, "c"));
        assertFalse(group.canProcess(cmd, "d"));

        // test long option
        assertTrue(group.canProcess(cmd, "--echo"));

        // test group
        assertTrue(group.canProcess(cmd, "g"));
        assertTrue(group.canProcess(cmd, "g1"));
    }

    @Test
    public void testCanProcessWithArg() {
        final CommandLine cmd = TestHelper.createCommandLine("-e foo", options);

        assertTrue(group.canProcess(cmd, "e"));
    }

    @Test
    public void testCanProcessWithNoMatch() {
        final CommandLine cmd = TestHelper.createCommandLine("-a", options);

        assertFalse(group.canProcess(cmd, "z"));
    }

    @Test
    public void testGetDescription() {
        assertEquals("Group of options", group.getDescription());
    }

    @Test
    public void testGetName() {
        assertEquals("group", group.getName());
    }

    @Test
    public void testGetOptions() {
        final List options = group.getOptions();

        assertEquals(4, options.size());
        assertTrue(options.contains(new Option("a", "alpha", true, "alpha option")));
        assertTrue(options.contains(new Option("b", "beta", false, "beta option")));
        assertTrue(options.contains(new Option("c", "char", false, "character option")));
        assertTrue(options.contains(new Option("d", "delta", false, "delta option")));
    }

    @Test
    public void testGetAnonymous() {
        final List anonymous = group.getAnonymous();

        assertTrue(anonymous.isEmpty());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testProcess() {
        final CommandLine cmd = TestHelper.createCommandLine("-a --g g1", options);

        group.process(cmd, options.commandLine().listIterator());

        assertSame(cmd, options.commandLine());
        assertNotNull(cmd.getOptionValues("a"));
        assertNull(cmd.getOptionValues("b"));
        assertNull(cmd.getOptionValues("c"));
        assertNull(cmd.getOptionValues("d"));
        assertNull(cmd.getOptionValues("e"));
        assertTrue(cmd.hasOption("g"));
        assertTrue(cmd.hasOption("g1"));
    }

    @Test
    public void testProcessWithArg() {
        final CommandLine cmd = TestHelper.createCommandLine("-e foo", options);

        group.process(cmd, options.commandLine().listIterator());

        assertSame(cmd, options.commandLine());
        assertNotNull(cmd.getOptionValues("e"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooFewOptions() {
        final CommandLine cmd = TestHelper.createCommandLine("-a", options);

        group.process(cmd, options.commandLine().listIterator());
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooManyOptions() {
        final CommandLine cmd = TestHelper.createCommandLine("-a --g g1 --g g2", options);

        group.process(cmd, options.commandLine().listIterator());
    }

    @Test
    public void testValidate() {
        final CommandLine cmd = TestHelper.createCommandLine("-a --g g1 --g g2", options);

        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewOptions() {
        final CommandLine cmd = TestHelper.createCommandLine("-a", options);

        group.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() {
        final CommandLine cmd = TestHelper.createCommandLine("-a --g g1 --g g2 --g g3", options);

        group.validate(cmd);
    }

    @Test
    public void testFindOption() {
        final CommandLine cmd = TestHelper.createCommandLine("-a --g g1", options);

        assertSame(new Option("g", "group", false, "group"), group.findOption(cmd, "g"));
    }

    @Test
    public void testFindOptionNotFound() {
        final CommandLine cmd = TestHelper.createCommandLine("-a --g g1", options);

        assertNull(group.findOption(cmd, "z"));
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder sb = new StringBuilder();

        group.appendUsage(sb, new HashSet(), null);

        final String expected = "group:\n" +
                "group:\n" +
                "  group\n" +
                "  group-one\n" +
                "  group-two\n" +
                "  group-three\n" +
                "\n" +
                "--alpha\n" +
                "--beta\n" +
                "--char\n" +
                "--delta\n" +
                "--echo=<string>\n";

        assertEquals(expected, sb.toString());
    }

    @Test
    public void testHelpLines() {
        final List helpLines = group.helpLines(0, new HashSet(), null);

        final String expected = "-g, --group\n" +
                "group\n" +
                "  group\n" +
                "  group-one\n" +
                "  group-two\n" +
                "  group-three\n" +
                "\n" +
                "--alpha\n" +
                "--beta\n" +
                "--char\n" +
                "--delta\n" +
                "--echo=<string>\n";

        assertEquals(expected, TestHelper.formatHelpLines(helpLines));
    }
}