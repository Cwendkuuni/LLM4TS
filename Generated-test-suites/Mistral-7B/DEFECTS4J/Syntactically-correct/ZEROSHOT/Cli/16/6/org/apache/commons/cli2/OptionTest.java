import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.util.Arguments;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionTest {

    private Option option;
    private WriteableCommandLine commandLine;
    private ListIterator<String> args;

    @Before
    public void setUp() {
        option = new MockOption();
        commandLine = new MockWriteableCommandLine();
        args = Arrays.asList("test", "--option", "value").listIterator();
    }

    @Test
    public void testProcess() throws OptionException {
        option.process(commandLine, args);
        assertEquals("value", commandLine.getOptionValue("option"));
    }

    @Test
    public void testProcess_InvalidArgument() throws OptionException {
        args = Arrays.asList("test", "--option_invalid").listIterator();
        assertThrows(OptionException.class, () -> option.process(commandLine, args));
    }

    @Test
    public void testProcess_MultipleArgument() throws OptionException {
        args = Arrays.asList("test", "--option", "value1", "value2").listIterator();
        option.process(commandLine, args);
        List<String> values = commandLine.getListOptions().get("option");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);
        assertNull(commandLine.getOptionValue("option"));
    }

    @Test
    public void testCanProcess() {
        assertTrue(option.canProcess(commandLine, "option"));
        assertFalse(option.canProcess(commandLine, "test"));
    }

    @Test
    public void testCanProcess_WithArguments() {
        assertTrue(option.canProcess(commandLine, args));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();
        assertNotNull(triggers);
        assertEquals("--option", triggers.toString());
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();
        assertNotNull(prefixes);
        assertEquals("--", prefixes.toString());
    }

    @Test
    public void testValidate() throws Exception {
        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidArgument() throws Exception {
        args = Arrays.asList("test", "--invalid").listIterator();
        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = option.helpLines(1, new LinkedHashSet<>(), null);
        assertNotNull(helpLines);
        assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        assertSame(option.getClass(), helpLine.getOption().getClass());
        assertEquals("--option", helpLine.getOption().getPreferredName());
        assertEquals("an option\n", helpLine.getShortDescription());
        assertEquals("Option description\n", helpLine.getLongDescription());
        assertEquals("value\n", helpLine.getArgsUsage());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, new LinkedHashSet<>(), null);
        assertEquals("--option\nOptional: value\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("option", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Option description\n", option.getDescription());
    }

    @Test
    public void testgetId() {
        assertEquals(0, option.getId());
    }

    @Test
    public void testFindOption() {
        Option option2 = new MockOption2();
        commandLine.addOption(option);
        commandLine.addOption(option2);

        Option foundOption = option.findOption("option");
        assertSame(option, foundOption);

        foundOption = option.findOption("option2");
        assertSame(option2, foundOption);

        foundOption = option.findOption("nonexistent");
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testGetParent() {
        assertNull(option.getParent());
    }

    @Test
    public void testSetParent() {
        Option parent = new MockOptionParent();
        option.setParent(parent);
        assertSame(parent, option.getParent());
    }

    private static class MockOption extends AbstractOption {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
            // empty implementation
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            // empty implementation
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return true;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
            return true;
        }

        @Override
        public Set<String> getTriggers() {
            return new LinkedHashSet<>(Arrays.asList("--option"));
        }

        @Override
        public Set<String> getPrefixes() {
            return new LinkedHashSet<>(Arrays.asList("--"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // empty implementation
        }

        @Override
        public List<HelpLine> helpLines(int depth, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
            return new ArrayList<>();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
            // empty implementation
        }

        @Override
        public String getPreferredName() {
            return "option";
        }

        @Override
        public String getDescription() {
            return "Option description";
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public Option findOption(String trigger) {
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public Option getParent() {
            return null;
        }

        @Override
        public void setParent(Option parent) {
            // empty implementation
        }
    }

    private static class MockOptionParent extends AbstractOption {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
            // empty implementation
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            // empty implementation
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return true;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
            return true;
        }

        @Override
        public Set<String> getTriggers() {
            return new LinkedHashSet<>(Arrays.asList("--parent"));
        }

        @Override
        public Set<String> getPrefixes() {
            return new LinkedHashSet<>(Arrays.asList("--"));
        }

        @Override
        public void validate(WriteableCommandLine commandLine) throws OptionException {
            // empty implementation
        }

        @Override
        public List<HelpLine> helpLines(int depth, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
            return new ArrayList<>();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
            // empty implementation
        }

        @Override
        public String getPreferredName() {
            return "parent";
        }

        @Override
        public String getDescription() {
            return "Parent option description";
        }

        @Override
        public int getId() {
            return 1;
        }

        @Override
        public Option findOption(String trigger) {
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public Option getParent() {
            return null;
        }

        @Override
        public void setParent(Option parent) {
            // empty implementation
        }
    }

    private static class MockWriteableCommandLine implements WriteableCommandLine {
        private final CommandLine cmd;

        public MockWriteableCommandLine() {
            cmd = new CommandLine(new DefaultParser().parse("test --option value"));
        }

        @Override
        public CommandLine getCommandLine() {
            return cmd;
        }

        @Override
        public void setOption(String longName, String value) {
            cmd.setOption(longName, value);
        }

        @Override
        public void setOption(String longName, boolean value) {
            cmd.setOption(longName, value);
        }

        @Override
        public void setOption(String longName, int value) {
            cmd.setOption(longName, value);
        }

        @Override
        public void setOption(String longName, List values) {
            cmd.setOption(longName, values);
        }

        @Override
        public void setOption(String longName, Arguments values) {
            cmd.setOption(longName, values);
        }

        @Override
        public List<String> getArguments() {
            return Arrays.asList(cmd.getArgs());
        }

        @Override
        public String getOptionValue(String longName) {
            return cmd.getOptionValue(longName);
        }

        @Override
        public Set<String> getOptionNames() {
            return cmd.getOptions().stream().map(Option::getLongOpt).collect(Collectors.toSet());
        }

        @Override
        public Set<String> getOptionNames(Option option) {
            if (option == null) {
                return cmd.getOptions().stream().map(Option::getLongOpt).collect(Collectors.toSet());
            } else {
                return Arrays.asList(option.getLongOpt()).stream().collect(Collectors.toSet());
            }
        }

        @Override
        public void putAllOptions(Map<String, Object> map) {
            cmd.putAll(map);
        }

        @Override
        public Option getOption(String longName) {
            return cmd.getOption(longName);
        }

        @Override
        public boolean hasOption(String longName) {
            return cmd.hasOption(longName);
        }

        @Override
        public void addOption(Option option) {
            cmd.addOption(option);
        }

        @Override
        public void addOptions(Collection<Option> options) {
            cmd.addOptions(options);
        }

        @Override
        public void putPaths(String key, String value) {
            // empty implementation
        }
    }
}