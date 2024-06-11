import org.apache.commons.cli2.BaseParseResult;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;
    private ParseContext parseContext;

    @Before
    public void setUp() {
        parseContext = Mockito.mock(ParseContext.class);
        commandLine = Mockito.mock(WriteableCommandLine.class);
        option = Mockito.mock(Option.class);

        Mockito.when(commandLine.getParseContext()).thenReturn(parseContext);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption(option.getLongOpt()));
    }

    @Test
    public void testAddValue() {
        String value = "testValue";
        commandLine.addValue(option, value);
        assertArrayEquals(new String[]{value}, commandLine.getValues(option.getLongOpt()));
    }

    @Test
    public void testGetValues() {
        String value1 = "testValue1";
        String value2 = "testValue2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);

        assertArrayEquals(expectedValues.toArray(new String[0]), commandLine.getValues(option.getLongOpt()));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        assertArrayEquals(defaultValues.toArray(new String[0]), commandLine.getValues(option.getLongOpt()));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getValue(option.getOpt(), Boolean.class));
    }

    @Test
    public void testSetDefaultSwitchFalse() {
        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getValue(option.getOpt(), Boolean.class));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("testProperty", "testValue");
        assertEquals("testValue", commandLine.getProperty("testProperty"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        assertFalse(commandLine.looksLikeOption("testArgument"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        String optionValue = "testOptionValue";

        Mockito.when(option.getOpt()).thenReturn(optionValue);
        Mockito.when(commandLine.getValue(optionValue, String.class)).thenReturn("existingValue");

        String testArgument = optionValue;
        assertTrue(commandLine.looksLikeOption(testArgument));

        testArgument = "testArgument";
        assertFalse(commandLine.looksLikeOption(testArgument));
    }

    @Test
    public void testParseWithValues() {
        String[] args = {"--option", "value1", "--option", "value2"};

        Mockito.when(parseContext.parse(commandLine)).thenReturn(new BaseParseResult());

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> parsedValues = commandLine.parse(args);

        assertEquals(2, parsedValues.size());
        assertArrayEquals(new String[]{"value1", "value2"}, parsedValues.toArray(new String[0]));
    }

    @Test(expected = IllegalStateException.class)
    public void testParseWithDuplicateOptions() {
        String[] args = {"--option", "value1", "--option"};

        Mockito.when(parseContext.parse(commandLine)).thenReturn(new BaseParseResult());

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List<String> parsedValues = commandLine.parse(args);
    }
}