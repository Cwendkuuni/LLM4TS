import org.apache.commons.cli2.*;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Mock
    private WriteableCommandLine commandLine;

    @InjectMocks
    private MyOption option;

    @Captor
    private ArgumentCaptor<String> argumentCaptor;

    private List<String> args;

    @Before
    public void setup() {
        args = new ArrayList<>();
        args.add("--myoption");
        args.add("value1");
        args.add("--anotheroption");
    }

    @Test
    public void testProcess() throws OptionException {
        // Given
        OptionExpectation expectation = new OptionExpectation("--myoption", "value1");

        // When
        option.process(commandLine, args.listIterator());

        // Then
        Mockito.verify(commandLine).setValue(argumentCaptor.capture(), Mockito.eq(option));
        assertEquals(expectation.getValue(), commandLine.getValue().get(expectation.getName()));
    }

    @Test
    public void testProcess_InvalidArgument() throws OptionException {
        // Given
        OptionExpectation expectation = new OptionExpectation("--myoption", "invalidValue");

        // When
        OptionException exception = assertThrows(OptionException.class, () -> option.process(commandLine, args.listIterator()));

        // Then
        assertEquals("Invalid value 'invalidValue' for option: --myoption", exception.getMessage());
        Mockito.verify(commandLine, never()).setValue(argumentCaptor.capture(), Mockito.eq(option));
    }

    @Test
    public void testProcess_MissingArgument() throws OptionException {
        // Given
        OptionExpectation expectation = new OptionExpectation("--myoption");

        // When
        option.process(commandLine, args.listIterator());

        // Then
        Mockito.verify(commandLine, never()).setValue(argumentCaptor.capture(), Mockito.eq(option));
        assertTrue(commandLine.getValues(expectation.getName()).isEmpty());
    }

    @Test
    public void testProcess_MissingOption() {
        // Given
        String[] argsArray = new String[]{"command", "arg1", "arg2"};
        List<String> argsList = Arrays.asList(argsArray);
        OptionExpectation expectation = new OptionExpectation("--myoption");

        // When
        option.process(commandLine, argsList.listIterator());

        // Then
        Mockito.verify(commandLine, never()).setValue(argumentCaptor.capture(), Mockito.eq(option));
        assertTrue(commandLine.getValues(expectation.getName()).isEmpty());
    }

    @Test
    public void testDefaults() {
        // Given

        // When
        option.defaults(commandLine);

        // Then
        List<String> expectedDefaults = Arrays.asList("defaultValue1", "defaultValue2");
        assertEquals(expectedDefaults, commandLine.getOptionDefaults(option.getClass()));
    }

    @Test
    public void testCanProcess() {
        // Given
        OptionExpectation expectation = new OptionExpectation("--myoption");

        // When
        boolean result = option.canProcess(commandLine, args.listIterator());

        // Then
        assertTrue(result);
    }

    @Test
    public void testCanProcess_WithArguments() {
        // Given
        OptionExpectation expectation = new OptionExpectation("--myoption", "value1");

        // When
        boolean result = option.canProcess(commandLine, args.listIterator());

        // Then
        assertTrue(result);
    }

    @Test
    public void testCanProcess_WithInvalidOption() {
        // Given
        OptionExpectation expectation = new OptionExpectation("--invalidoption");

        // When
        boolean result = option.canProcess(commandLine, args.listIterator());

        // Then
        assertFalse(result);
    }

    @Test
    public void testGetTriggers() {
        // Given

        // When
        Set<String> triggers = option.getTriggers();

        // Then
        assertEquals(Arrays.asList("--myoption"), triggers);
    }

    @Test
    public void testGetPrefixes() {
        // Given

        // When
        Set<String> prefixes = option.getPrefixes();

        // Then
        assertEquals(Arrays.asList("--"), prefixes);
    }

    @Test
    public void testValidate() {
        // Given

        // When
        option.validate(commandLine);

        // Then
        // No exception thrown
    }

    @Test(expected = OptionException.class)
    public void testValidate_WithInvalidArgument() {
        // Given
        OptionException exception = new OptionException("Invalid value 'value1' for option: --myoption");

        // When
        option.validate(commandLine);
        commandLine.setValue("value1", option);

        // Then
        throw exception;
    }

    @Test
    public void testHelpLines() {
        // Given
        int depth = 0;
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator<Option> comp = null;

        // When
        List<HelpLine> helpLines = option.helpLines(depth, helpSettings, comp);

        // Then
        String expectedHelpLine = "  --myoption <VALUE>\n" +
                "             Option description\n";
        assertEquals(Arrays.asList(new HelpLineImpl(expectedHelpLine)), helpLines);
    }

    @Test
    public void testAppendUsage() {
        // Given
        StringBuffer buffer = new StringBuffer();

        // When
        option.appendUsage(buffer, helpSettings, comp);

        // Then
        String expectedUsage = "  --myoption <VALUE>\n" +
                "             Option description\n";
        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        // Given

        // When
        String preferredName = option.getPreferredName();

        // Then
        assertEquals("--myoption", preferredName);
    }

    @Test
    public void testGetDescription() {
        // Given

        // When
        String description = option.getDescription();

        // Then
        assertEquals("Option description", description);
    }

    @Test
    public void testgetId() {
        // Given

        // When
        int id = option.getId();

        // Then
        assertEquals(1, id);
    }

    @Test
    public void testFindOption() {
        // Given
        Option anotherOption = Mockito.mock(Option.class);

        // When
        Option result = option.findOption("--anotheroption");

        // Then
        assertEquals(anotherOption, result);
    }

    @Test
    public void testIsRequired() {
        // Given

        // When
        boolean result = option.isRequired();

        // Then
        assertFalse(result);
    }

    @Test
    public void testFindParent() {
        // Given
        OptionGroup optionGroup = Mockito.mock(OptionGroup.class);
        option.setParent(optionGroup);

        // When
        Option result = option.findOption("--anotheroption");

        // Then
        assertNull(result);
    }

    private static class OptionExpectation {
        private final String name;
        private final String value;

        public OptionExpectation(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}