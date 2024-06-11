import org.apache.commons.cli2.CommandLine;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class OptionTest {

    private Option option;
    private WriteableCommandLine commandLine;
    private ListIterator<String> args;

    @Before
    public void setUp() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        args = Mockito.mock(ListIterator.class);
        option = Mockito.spy(new TestOption());
    }

    @Test
    public void testProcess() {
        Mockito.when(args.next()).thenReturn("--test").thenReturn("value");

        option.process(commandLine, args);

        Mockito.verify(commandLine).setValue(ArgumentMatchers.eq("test"), ArgumentMatchers.eq("value"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithException() {
        Mockito.when(args.next()).thenThrow(new OptionException("Test exception"));

        option.process(commandLine, args);
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);

        Mockito.verify(commandLine).setValue(ArgumentMatchers.eq("test"), ArgumentMatchers.eq("defaultValue"));
    }

    @Test
    public void testCanProcessString() {
        Mockito.when(args.next()).thenReturn("value");

        assertTrue(option.canProcess(commandLine, "value"));
    }

    @Test
    public void testCanProcessListIterator() {
        Mockito.when(args.next()).thenReturn("--test").thenReturn("value");

        assertTrue(option.canProcess(commandLine, args));
        Mockito.verify(args).previous();
    }

    @Test
    public void testCanProcessStringWithException() {
        Mockito.when(args.next()).thenThrow(new IllegalArgumentException("Test exception"));

        assertFalse(option.canProcess(commandLine, "value"));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();

        assertNotNull(triggers);
        assertEquals( Arrays.asList("--test", "-t"), new ArrayList<>(triggers));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();

        assertNotNull(prefixes);
        assertEquals( Arrays.asList("--", "-"), new ArrayList<>(prefixes));
    }

    @Test
    public void testValidate() {
        Mockito.when(commandLine.hasOption("test")).thenReturn(false);

        option.validate(commandLine);

        Mockito.verifyNoMoreInteractions(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithException() {
        Mockito.when(commandLine.hasOption("test")).thenThrow(new OptionException("Test exception"));

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Set<String> helpSettings = new LinkedHashSet<>();
        Comparator<Option> comp = (o1, o2) -> o1.getId() - o2.getId();

        List<Option> options = Arrays.asList(option, new TestOption2());
        List<Option> expected = Arrays.asList(option, new TestOption2());

        List<Option> actual = option.helpLines(1, helpSettings, comp);

        assertEquals(expected, actual);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();

        option.appendUsage(buffer, helpSettings, comp);

        assertEquals("Test option\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("test", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("This is a test option\n", option.getDescription());
    }

    @Test
    public void testGetId() {
        assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option testOption2 = new TestOption2();

        Option found = option.findOption("--test2");

        assertSame(testOption2, found);
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parent = new TestOption();
        option.setParent(parent);

        assertSame(parent, option.getParent());
    }
}

class TestOption extends AbstractOption {
    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
        super.process(commandLine, args);
        commandLine.setValue("test", "defaultValue");
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
        super.defaults(commandLine);
        commandLine.setValue("test", "defaultValue");
    }
}

class TestOption2 extends AbstractOption {
    @Override
    public void process(WriteableCommandLine commandLine, ListIterator<String> args) throws OptionException {
        super.process(commandLine, args);
        commandLine.setValue("test2", "defaultValue2");
    }

    @Override
    public void defaults(WriteableCommandLine commandLine) {
        super.defaults(commandLine);
        commandLine.setValue("test2", "defaultValue2");
    }

    @Override
    public Set<String> getTriggers() {
        return new HashSet<>(Arrays.asList("--test2", "-t2"));
    }

    @Override
    public Set<String> getPrefixes() {
        return new HashSet<>(Arrays.asList("--", "-"));
    }

    @Override
    public void validate(WriteableCommandLine commandLine) throws OptionException {
        if(!commandLine.hasOption("test2")) {
            throw new OptionException("Test option2 is required");
        }
    }

    @Override
    public List helpLines(int depth, Set helpSettings, Comparator<?> comp) {
        return Arrays.asList(new HelpLineImpl("Test option2", "This is a test option2", null, null, null));
    }

    @Override
    public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator<?> comp) {
        buffer.append("-t2, --test2\n");
        buffer.append("Test option2\n");
        buffer.append("\tThis is a test option2\n");
    }
}