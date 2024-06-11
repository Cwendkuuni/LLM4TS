import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OptionTest {

    private Option option;
    private WriteableCommandLine commandLine;
    private ListIterator<String> args;

    @Before
    public void setup() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        args = Mockito.mock(ListIterator.class);
        option = Mockito.spy(new MockOption());
    }

    @Test
    public void testProcess() throws OptionException {
        Mockito.when(args.hasNext()).thenReturn(true).thenReturn(false);
        String argument = "test";
        option.process(commandLine, args);
        Mockito.verify(commandLine, Mockito.times(1)).setValue(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(option));
    }

    @Test(expected = OptionException.class)
    public void testProcessThrowsException() throws OptionException {
        Mockito.when(args.hasNext()).thenReturn(true).thenReturn(true);
        String argument = "test";
        option.process(commandLine, args);
        Mockito.verify(commandLine, Mockito.times(1)).setValue(ArgumentMatchers.eq("test"), ArgumentMatchers.eq(option));
        Mockito.verify(commandLine, Mockito.times(1)).addOption(ArgumentMatchers.eq(option));
        Mockito.verify(commandLine, Mockito.times(1)).error(ArgumentMatchers.eq("test is an unrecognized option"));
    }

    @Test
    public void testCanProcess() throws OptionException {
        Mockito.when(args.next()).thenReturn("--test").thenReturn("--other");
        assertTrue(option.canProcess(commandLine, args));
        assertFalse(option.canProcess(commandLine, args));
    }

    @Test
    public void testCanProcessArgument() {
        assertTrue(option.canProcess(commandLine, "test"));
        assertFalse(option.canProcess(commandLine, "other"));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();
        Set<String> expectedTriggers = new HashSet<>(Arrays.asList("--test", "--other"));
        assertEquals(expectedTriggers, triggers);
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();
        Set<String> expectedPrefixes = new HashSet<>(Arrays.asList("--"));
        assertEquals(expectedPrefixes, prefixes);
    }

    @Test
    public void testValidate() {
        Mockito.when(commandLine.getValue((String.class), ArgumentMatchers.eq("test"))).thenReturn("value");
        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateThrowsException() {
        Mockito.when(commandLine.getValue((String.class), ArgumentMatchers.eq("test"))).thenReturn(null);
        option.validate(commandLine);
        Mockito.verify(commandLine, Mockito.times(1)).error(ArgumentMatchers.eq("test must have a value"));
    }

    @Test
    public void testHelpLines() {
        List<Object> expectedHelpLines = new ArrayList<>();
        expectedHelpLines.add(Mockito.mock(HelpLine.class));
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<Option> comp = Mockito.mock(Comparator.class);
        List<Object> helpLines = option.helpLines(1, helpSettings, comp);
        assertEquals(expectedHelpLines, helpLines);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer expectedBuffer = new StringBuffer();
        setStringBufferAppend(expectedBuffer, "Option Test:");
        option.appendUsage(expectedBuffer, helpSettings, comp);
        assertEquals("Option Test:", expectedBuffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        String preferredName = option.getPreferredName();
        assertEquals("test", preferredName);
    }

    @Test
    public void testGetDescription() {
        String description = option.getDescription();
        assertEquals("This is a test option.", description);
    }

    @Test
    public void testgetId() {
        int id = option.getId();
        assertEquals(1, id);
    }

    @Test
    public void testFindOption() {
        Option option2 = Mockito.mock(Option.class);
        Mockito.when(option2.getTriggers()).thenReturn(new HashSet<>(Arrays.asList("--other")));
        assertEquals(option2, option.findOption("--other"));
        assertNull(option.findOption("--nonexistent"));
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parentOption = Mockito.mock(Option.class);
        option.setParent(parentOption);
        assertEquals(parentOption, option.getParent());
    }

    private void setStringBufferAppend(StringBuffer buffer, String value) {
        buffer.append(value);
    }
}

private class MockOption implements Option {
    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
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
        return new HashSet<>(Arrays.asList("--test", "--other"));
    }

    @Override
    public Set<String> getPrefixes() {
        return new HashSet<>(Arrays.asList("--"));
    }

    @Override
    public void validate(WriteableCommandLine commandLine) throws OptionException {
    }

    @Override
    public List<Object> helpLines(int depth, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
        return null;
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set<DisplaySetting> helpSettings, Comparator<Option> comp) {
    }

    @Override
    public String getPreferredName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "This is a test option.";
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
    }
}