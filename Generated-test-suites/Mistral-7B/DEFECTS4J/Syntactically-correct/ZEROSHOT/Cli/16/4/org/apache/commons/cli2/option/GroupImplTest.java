import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.ArgBuilder;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.exceptions.MissingArgumentException;
import org.apache.commons.cli2.exceptions.OptionException;
import org.apache.commons.cli2.util.HelpFormatterHelper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class GroupImplTest {
    private CommandLine commandLine;
    private Options options;
    private GroupImpl group;

    @Before
    public void setUp() {
        commandLine = new CommandLine();

        // create options for the test group
        List<Option> testOptions = new ArrayList<>();
        testOptions.add(OptionBuilder.withName("testOptionA")
                                     .withLongOpt("test-option-a")
                                     .withDescription("Test option A")
                                     .withType(String.class)
                                     .isRequired()
                                     .create());
        testOptions.add(OptionBuilder.withName("testOptionB")
                                     .withLongOpt("test-option-b")
                                     .withDescription("Test option B")
                                     .withType(String.class)
                                     .create());
        testOptions.add(ArgBuilder.withName("testArg")
                                  .isOptional()
                                  .create());

        options = new Options();
        options.addOptionGroup(new GroupImpl(testOptions, "testGroup", "Test group", 1, 2));

        group = (GroupImpl) options.getOptionGroup("testGroup");
    }

    @Test
    public void testCanProcessTrue() throws OptionException {
        // testOptionA can be processed by the group
        String[] arguments = {"test-option-a", "testValueA"};
        assertTrue(group.canProcess(commandLine, arguments));

        // testOptionA can be processed by testOptionB
        String[] argumentsB = {"test-option-b", "testValueB", "test-option-a", "testValueA"};
        assertTrue(group.canProcess(commandLine, argumentsB));

        // testArg can be processed by the group
        String[] argumentsArg = {"testArg", "testValueArg"};
        assertTrue(group.canProcess(commandLine, argumentsArg));

        // testOptionA can be processed if testOptionB was already processed by another option
        String[] argumentsB2 = {"test-option-b", "testValueB", "testOptionA", "testValueA"};
        assertTrue(group.canProcess(commandLine, argumentsB2));
    }

    @Test
    public void testCanProcessFalse() throws OptionException {
        // testOptionA can't be processed if testOptionB already was processed
        String[] argumentsBA = {"test-option-b", "testValueB", "test-option-a", "testValueA"};
        assertFalse(group.canProcess(commandLine, argumentsBA));

        // testOptionA can't be processed if an anonymous argument already was processed
        String[] argumentsAA = {"testArg", "testValueArg", "test-option-a", "testValueA"};
        assertFalse(group.canProcess(commandLine, argumentsAA));

        // testOptionA can't be processed if a non-anonymous argument is missing
        String[] argumentsM = {"test-option-b", "testValueB"};
        assertFalse(group.canProcess(commandLine, argumentsM));

        // testOptionA can't be processed if there are too many non-anonymous arguments
        String[] argumentsT = {"test-option-a", "testValueA", "test-option-a", "testValueA", "testValueOther"};
        assertFalse(group.canProcess(commandLine, argumentsT));
    }

    @Test
    public void testProcess() throws OptionException {
        // testOptionA is processed correctly
        String[] argumentsA = {"test-option-a", "testValueA"};
        group.process(commandLine, commandLine.parser().parse(argumentsA));
        assertEquals("testValueA", commandLine.getOptionValue("testOptionA"));
        assertEquals("testValueA", commandLine.getOptionValue("test-option-a"));

        // testOptionB is processed correctly
        String[] argumentsB = {"test-option-b", "testValueB", "test-option-a", "testValueA"};
        group.process(commandLine, commandLine.parser().parse(argumentsB));
        assertEquals("testValueB", commandLine.getOptionValue("testOptionB"));
        assertEquals("testValueB", commandLine.getOptionValue("test-option-b"));
        assertEquals("testValueA", commandLine.getOptionValue("testOptionA"));
        assertEquals("testValueA", commandLine.getOptionValue("test-option-a"));

        // testArg is processed correctly
        String[] argumentsArg = {"testArg", "testValueArg"};
        group.process(commandLine, commandLine.parser().parse(argumentsArg));
        assertEquals("testValueArg", commandLine.getOptionValue("testArg"));
    }

    @Test
    public void testValidate() throws OptionException {
        // validate the group with correct number of options and no anonymous arguments
        group.validate(commandLine);

        // validate the group with correct number of options and an anonymous argument
        String[] argumentsC = {"test-option-a", "testValueA", "testArg", "testValueArg"};
        group.process(commandLine, commandLine.parser().parse(argumentsC));
        group.validate(commandLine);

        // validate the group with too few options
        String[] argumentsD = {"test-option-a", "testValueA"};
        group.process(commandLine, commandLine.parser().parse(argumentsD));
        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            assertEquals("Minimum number of options not met", e.getMessage());
        }

        // validate the group with too many options
        String[] argumentsE = {"test-option-a", "testValueA", "test-option-a", "testValueA"};
        group.process(commandLine, commandLine.parser().parse(argumentsE));
        try {
            group.validate(commandLine);
            fail("Expected OptionException");
        } catch (OptionException e) {
            assertEquals("Maximum number of options exceeded", e.getMessage());
        }
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("testGroup", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test group", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        group.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "-testGroup TEST_GROUP_NAME\n";
        expectedUsage += "  Test group\n";
        expectedUsage += "\n";
        expectedUsage += "  --test-option-a <TEST_OPTION_A>\n";
        expectedUsage += "  Test option A\n";
        expectedUsage += "\n";
        expectedUsage += "  --test-option-b <TEST_OPTION_B>\n";
        expectedUsage += "  Test option B\n";
        expectedUsage += "\n";
        expectedUsage += "  --testArg <TEST_ARG>\n";
        expectedUsage += "  Test argument\n";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_EXPANDED);
        helpSettings.add(DisplaySetting.DISPLAY_GROUP_ARGUMENT);

        List<HelpLine> helpLines = group.helpLines(0, helpSettings, null);

        HelpFormatter formatter = new HelpFormatterHelper();
        formatter.printHelp(buffer, "test", helpLines.toArray(new HelpLine[0]));

        String expectedHelp = "NAME\n";
        expectedHelp += "test - Test\n\n";
        expectedHelp += "GROUPS:\n";
        expectedHelp += "  testGroup\n";
        expectedHelp += "\n";
        expectedHelp += "OPTIONS:\n";
        expectedHelp += "  -test-option-a <TEST_OPTION_A>\n";
        expectedHelp += "    Test option A\n";
        expectedHelp += "\n";
        expectedHelp += "  -test-option-b <TEST_OPTION_B>\n";
        expectedHelp += "    Test option B\n";
        expectedHelp += "\n";
        expectedHelp += "  --testArg <TEST_ARG>\n";
        expectedHelp += "    Test argument\n";
        expectedHelp += "\n";

        assertEquals(expectedHelp, buffer.toString());
    }

    @Test
    public void testFindOption() {
        assertNull(group.findOption("nonexistent"));
        Option testOptionA = group.findOption("testOptionA");
        assertSame(testOptionA, testOptions.get(0));
        Option testOptionB = group.findOption("testOptionB");
        assertSame(testOptionB, testOptions.get(1));
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(2, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);
        assertNull(commandLine.getOptionValue("testOptionA"));
        assertNull(commandLine.getOptionValue("testOptionB"));
        assertNull(commandLine.getOptionValue("testArg"));
    }
}