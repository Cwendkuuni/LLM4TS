import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Option.class})
public class OptionTest {

    private Option option;
    private WriteableCommandLine commandLine;
    private ListIterator<String> argsIterator;

    @Before
    public void setup() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        argsIterator = Mockito.mock(ListIterator.class);
        option = PowerMockito.spy(new TestOption());
    }

    @Test
    public void testProcess() {
        PowerMockito.when(argsIterator.next()).thenReturn("--testOption");
        PowerMockito.when(argsIterator.hasNext()).thenReturn(true);

        option.process(commandLine, argsIterator);

        PowerMockito.verify(commandLine).setValue("testOption", "testValue");
        PowerMockito.verify(commandLine, ArgumentMatchers.never()).setValue("unknownOption", ArgumentMatchers.any());
    }

    @Test
    public void testProcess_whenArgumentIsNotProcessed_thenNextIsCalled() {
        PowerMockito.when(argsIterator.next()).thenReturn("--testOption");
        PowerMockito.when(argsIterator.next()).thenReturn("--unknownOption");
        PowerMockito.when(argsIterator.hasNext()).thenReturn(true);

        option.process(commandLine, argsIterator);

        PowerMockito.verify(argsIterator, ArgumentMatchers.atLeastOnce()).next();
    }

    @Test(expected = OptionException.class)
    public void testProcess_whenArgumentIsNotProcessed_thenExceptionIsThrown() {
        PowerMockito.when(argsIterator.next()).thenReturn("--testOption");
        PowerMockito.when(argsIterator.next()).thenReturn("--unknownOption");
        PowerMockito.when(argsIterator.hasNext()).thenReturn(false);

        option.process(commandLine, argsIterator);
    }

    @Test
    public void testProcess_whenArgumentIsProcessed_thenNextIsCalled() {
        PowerMockito.when(argsIterator.next()).thenReturn("--testOption=testValue");
        PowerMockito.when(argsIterator.hasNext()).thenReturn(true);

        option.process(commandLine, argsIterator);

        PowerMockito.verify(argsIterator, ArgumentMatchers.atLeastOnce()).next();
    }

    @Test
    public void testDefaults() {
        Set<String> helpSettings = new LinkedHashSet<>();
        option.defaults(commandLine);

        assertEquals("testValue1", commandLine.getValue("testOption1"));
        assertEquals("testValue2", commandLine.getValue("testOption2"));
    }

    @Test
    public void testCanProcess_whenArgumentMatchesTrigger_thenTrueIsReturned() {
        Set<String> triggers = new LinkedHashSet<>(Arrays.asList("--testOption", "--test"));
        Set<String> prefixes = new LinkedHashSet<>(Arrays.asList("--"));

        assertTrue(option.canProcess(commandLine, argsIterator, triggers, prefixes));
    }

    @Test
    public void testCanProcess_whenArgumentDoesNotMatchTrigger_thenFalseIsReturned() {
        Set<String> triggers = new LinkedHashSet<>(Arrays.asList("--testOption", "--test"));
        Set<String> prefixes = new LinkedHashSet<>(Arrays.asList("--"));

        assertFalse(option.canProcess(commandLine, argsIterator, triggers, prefixes));
    }

    @Test
    public void testCanProcess_whenArgumentMatchesTriggerWithArguments_thenFalseIsReturned() {
        Set<String> triggers = new LinkedHashSet<>(Arrays.asList("--testOption", "--test"));
        Set<String> prefixes = new LinkedHashSet<>(Arrays.asList("--"));

        PowerMockito.when(argsIterator.next()).thenReturn("--testOptionValue");

        assertFalse(option.canProcess(commandLine, argsIterator, triggers, prefixes));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_whenArgumentMatchesTriggerWithArguments_thenExceptionIsThrown() {
        Set<String> triggers = new LinkedHashSet<>(Arrays.asList("--testOption", "--test"));
        Set<String> prefixes = new LinkedHashSet<>(Arrays.asList("--"));

        PowerMockito.when(argsIterator.next()).thenReturn("--testOptionValue");
        PowerMockito.when(argsIterator.next()).thenReturn("value1");

        option.canProcess(commandLine, argsIterator, triggers, prefixes);
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();
        assertNotNull(triggers);
        assertTrue(triggers.contains("--testOption1"));
        assertTrue(triggers.contains("--testOption2"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();
        assertNotNull(prefixes);
        assertTrue(prefixes.contains("--"));
    }

    @Test
    public void testValidate() {
        Set<String> helpSettings = new LinkedHashSet<>();
        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_whenOptionValueIsInvalid_thenExceptionIsThrown() {
        Set<String> helpSettings = new LinkedHashSet<>();
        PowerMockito.when(commandLine.getValue("testOption1")).thenThrow(new OptionException("Invalid value"));

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Set<String> helpSettings = new LinkedHashSet<>();
        Comparator<Option> comp = (o1, o2) -> o1.getPreferredName().compareTo(o2.getPreferredName());

        List<Option> options = Arrays.asList(new TestOption1(), new TestOption2());
        List<Option> expected = options.stream().map(o -> o.helpLines(1, helpSettings, comp)).collect(Collectors.toList());

        List<HelpLine> result = option.helpLines(1, helpSettings, comp);

        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            HelpLine expectedHelpLine = expected.get(i);
            HelpLine resultHelpLine = result.get(i);

            assertEquals(expectedHelpLine.getShortOption(), resultHelpLine.getShortOption());
            assertEquals(expectedHelpLine.getLongOption(), resultHelpLine.getLongOption());
            assertEquals(expectedHelpLine.getOptString(), resultHelpLine.getOptString());
            assertEquals(expectedHelpLine.getDescription(), resultHelpLine.getDescription());
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<String> helpSettings = new LinkedHashSet<>();

        option.appendUsage(buffer, helpSettings, null);

        assertEquals("testOption\n" +
                "        testDescription1\n" +
                "\n" +
                "testOption2\n" +
                "        testDescription2\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("testOption1", option.getPreferredName());
        assertEquals("testOption2", new TestOption2().getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("testDescription1", option.getDescription());
        assertEquals("testDescription2", new TestOption2().getDescription());
    }

    @Test
    public void testGetId() {
        assertEquals(1, option.getId());
        assertEquals(2, new TestOption2().getId());
    }

    @Test
    public void testFindOption() {
        Option testOption1 = new TestOption1();
        Option testOption2 = new TestOption2();

        Option foundOption = option.findOption("--testOption1");
        assertNotNull(foundOption);
        assertEquals(testOption1, foundOption);

        foundOption = option.findOption("--testOption3");
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
        assertTrue(new TestOptionRequired().isRequired());
    }

    @Test(expected = OptionException.class)
    public void testFindParent_whenOptionHasNoParent_thenNullIsReturned() {
        Option parent = option.findParent();
        assertNull(parent);
    }

    @Test
    public void testFindParent_whenOptionHasAParent_thenParentIsReturned() {
        Option parent = new TestOptionGroup();
        option.setParent(parent);

        Option foundParent = option.findParent();
        assertEquals(parent, foundParent);
    }

    private static class TestOption extends Option {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args)
                throws OptionException {
            String value;
            if (args.hasNext()) {
                value = args.next();
                commandLine.setValue("testOption" + (commandLine.getValue("testOption") == null ? "" : "_") + value, value);
            } else {
                value = "testValue1";
                commandLine.setValue("testOption1", value);
            }
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            commandLine.setValue("testOption1", "testValue1");
            commandLine.setValue("testOption2", "testValue2");
        }

        @Override
        public Set<String> getTriggers() {
            return new LinkedHashSet<>(Arrays.asList("--testOption", "--test"));
        }

        @Override
        public Set<String> getPrefixes() {
            return Collections.singletonList("--");
        }

        @Override
        public void validate(WriteableCommandLine commandLine)
                throws OptionException {
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return new ArrayList<>();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
        }

        @Override
        public String getPreferredName() {
            return "testOption";
        }

        @Override
        public String getDescription() {
            return "testDescription";
        }

        @Override
        public int getId() {
            return 1;
        }

        @Override
        public Option findParent() {
            return null;
        }

        @Override
        public void setParent(Option parent) {
        }
    }

    private static class TestOption1 extends TestOption {
        @Override
        public String getDescription() {
            return "testDescription1";
        }

        @Override
        public String getPreferredName() {
            return "testOption1";
        }
    }

    private static class TestOption2 extends TestOption {
        @Override
        public String getDescription() {
            return "testDescription2";
        }

        @Override
        public String getPreferredName() {
            return "testOption2";
        }
    }

    private static class TestOptionGroup extends Option {
        @Override
        public void process(WriteableCommandLine commandLine, ListIterator<String> args)
                throws OptionException {
            // not implemented
        }

        @Override
        public void defaults(WriteableCommandLine commandLine) {
            // not implemented
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return false;
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
            return false;
        }

        @Override
        public Set<String> getTriggers() {
            return new LinkedHashSet<>();
        }

        @Override
        public Set<String> getPrefixes() {
            return new LinkedHashSet<>();
        }

        @Override
        public void validate(WriteableCommandLine commandLine)
                throws OptionException {
        }

        @Override
        public List helpLines(int depth, Set helpSettings, Comparator comp) {
            return new ArrayList<>();
        }

        @Override
        public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
        }

        @Override
        public String getPreferredName() {
            return "testOptionGroup";
        }

        @Override
        public String getDescription() {
            return "testDescriptionGroup";
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public Option findParent() {
            return null;
        }

        @Override
        public void setParent(Option parent) {
        }
    }
}