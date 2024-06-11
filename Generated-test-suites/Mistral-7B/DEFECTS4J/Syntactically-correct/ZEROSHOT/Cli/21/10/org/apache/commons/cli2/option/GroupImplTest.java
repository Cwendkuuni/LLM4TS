import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private static final String GROUP_NAME = "myGroup";
    private static final String GROUP_DESCRIPTION = "A group of options";
    private static final int GROUP_MINIMUM = 1;
    private static final int GROUP_MAXIMUM = 3;

    private WriteableCommandLine commandLine;
    private Group group;

    @Before
    public void setUp() {
        commandLine = new DefaultCommandLine();
        final List<Option> options = new ArrayList<>();
        final List<Argument> anonymous = new ArrayList<>();

        final Option option1 = new OptionBuilder()
                .withLongOpt("option1")
                .withDescription("Option 1")
                .isRequired()
                .build();
        options.add(option1);

        final Option option2 = new OptionBuilder()
                .withLongOpt("option2")
                .withDescription("Option 2")
                .build();
        options.add(option2);

        final Argument arg1 = new ArgumentBuilder()
                .withLongOpt("arg1")
                .withDescription("Argument 1")
                .build();
        anonymous.add(arg1);

        final Argument arg2 = new ArgumentBuilder()
                .withLongOpt("arg2")
                .withDescription("Argument 2")
                .build();
        anonymous.add(arg2);

        group = new GroupImpl(options, GROUP_NAME, GROUP_DESCRIPTION, GROUP_MINIMUM, GROUP_MAXIMUM, false);
        group.setTriggers(new HashSet<String>());
    }

    @Test
    public void testCanProcessWithValidOption() {
        group.process(commandLine, commandLine.parser().getCommandLineParser().parse(new String[]{"--option1"}));
        assertTrue(commandLine.hasOption("option1"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithInvalidOption() {
        group.process(commandLine, commandLine.parser().getCommandLineParser().parse(new String[]{"--invalid"}));
        assertFalse(commandLine.hasOption("invalid"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithMissingOption() {
        commandLine.parser().getCommandLineParser().parse(new String[]{});
        group.process(commandLine, commandLine.parser().getCommandLineParser().parse(new String[]{}));
        assertFalse(commandLine.hasOption("option1"));
    }

    @Test
    public void testCanProcessWithAnonymousArgument() {
        group.process(commandLine, commandLine.parser().getCommandLineParser().parse(new String[]{"--arg1", "value1", "--option1"}));
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.getOptionValue("arg1").equals("value1"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithMissingAnonymousArgument() {
        commandLine.parser().getCommandLineParser().parse(new String[]{"--option1"});
        group.process(commandLine, commandLine.parser().getCommandLineParser().parse(new String[]{}));
        assertFalse(commandLine.hasOption("option1"));
    }

    @Test
    public void testValidateWithValidOptions() {
        commandLine.parser().getCommandLineParser().parse(new String[]{"--option1", "--option1", "--option2"});
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidOptions() {
        commandLine.parser().getCommandLineParser().parse(new String[]{"--option1", "--option1", "--option2", "--option1"});
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingOptions() {
        commandLine.parser().getCommandLineParser().parse(new String[]{"--option1"});
        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, new HashSet<DisplaySetting>(), null);
        final String expected = "|" + GROUP_NAME + " (" + GROUP_DESCRIPTION + ")\n" +
                "[-]option1  Option 1\n" +
                "[-]option2  Option 2\n" +
                "[-]arg1     Argument 1\n" +
                "[-]arg2     Argument 2\n";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List<HelpLine> helpLines = group.helpLines(0, new HashSet<DisplaySetting>(), null);
        final String expected = "|" + GROUP_NAME + " (" + GROUP_DESCRIPTION + ")\n" +
                "[-]option1  Option 1\n" +
                "[-]option2  Option 2\n" +
                "[-]arg1     Argument 1\n" +
                "[-]arg2     Argument 2\n";
        assertEquals(expected, helpLines.get(0).getHelp());
    }

    @Test
    public void testGetOptions() {
        final List<Option> options = group.getOptions();
        assertEquals(2, options.size());
    }

    @Test
    public void testGetAnonymous() {
        final List<Argument> anonymous = group.getAnonymous();
        assertEquals(2, anonymous.size());
    }

    @Test
    public void testFindOption() {
        final Option option1 = group.findOption("option1");
        assertNotNull(option1);
        final Option option2 = group.findOption("option2");
        assertNotNull(option2);
        final Option invalidOption = group.findOption("invalid");
        assertNull(invalidOption);
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, group.getMaximum());
    }
}