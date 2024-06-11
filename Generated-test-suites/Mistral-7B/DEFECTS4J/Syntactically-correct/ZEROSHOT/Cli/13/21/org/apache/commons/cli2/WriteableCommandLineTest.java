import org.apache.commons.cli2.BaseParser;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WriteableCommandLine.class)
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = PowerMockito.mock(WriteableCommandLine.class);
        option = new Option("option", true, false, "option description");
    }

    @Test
    public void testAddOption() {
        WriteableCommandLine expectedCommandLine = new BaseParser().parse(new String[]{"--option"});
        PowerMockito.whenNew(BaseParser.class).withArgs().thenReturn(PowerMockito.spy(new BaseParser()));
        PowerMockito.doReturn(expectedCommandLine).when(PowerMockito.spy(new BaseParser())).parse(PowerMockito.anyStrings());

        commandLine.addOption(option);

        PowerMockito.verifyNew(Option.class).withArgs("option", true, false, "option description");
        PowerMockito.verifyZeroInteractions(commandLine);
    }

    @Test
    public void testAddValue() {
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        PowerMockito.when(commandLine.getOptions()).thenReturn(new Option[] {option});
        PowerMockito.when(option.getValue()).thenReturn(null);

        commandLine.addValue(option, "value1");

        PowerMockito.verify(option).setValue(PowerMockito.eq("value1"));
        PowerMockito.verifyZeroInteractions(commandLine);
    }

    @Test
    public void testGetValues() {
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        PowerMockito.when(commandLine.getOptions()).thenReturn(new Option[] {option});
        PowerMockito.when(option.getValue()).thenReturn(expectedValues.toArray(new String[0]));

        List<Object> actualValues = commandLine.getValues(option);

        assertEquals(expectedValues, actualValues);
        PowerMockito.verifyZeroInteractions(commandLine);
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        PowerMockito.verify(option).setDefaultValues(defaultValues.toArray(new String[0]));
        PowerMockito.verifyZeroInteractions(commandLine);
    }

    @Test
    public void testAddSwitch() {
        PowerMockito.when(commandLine.getOptions()).thenReturn(new Option[0]);

        commandLine.addSwitch(option, true);

        PowerMockito.verifyNew(Option.class).withArgs("option", false, true, null);
        PowerMockito.verify(option).setSwitch(true);
        PowerMockito.verifyZeroInteractions(commandLine);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        PowerMockito.when(commandLine.getOptions()).thenReturn(new Option[] {option});

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);

        PowerMockito.verifyNew(Option.class).withArgs("option", false, true, null).times(1);
        PowerMockito.verify(option).setSwitch(true);
        PowerMockito.verify(option).setSwitch(true);
        PowerMockito.verifyThrows(IllegalStateException.class);
    }

    @Test
    public void testSetDefaultSwitch() {
        PowerMockito.when(commandLine.getOptions()).thenReturn(new Option[0]);

        commandLine.setDefaultSwitch(option, true);

        PowerMockito.verifyNew(Option.class).withArgs("option", false, false, null);
        PowerMockito.verify(option).setDefaultValue(true);
        PowerMockito.verifyZeroInteractions(commandLine);
    }

    @Test
    public void testAddProperty() {
        PowerMockito.when(commandLine.getProperties()).thenReturn(new String[0]);

        commandLine.addProperty("property", "property value");

        PowerMockito.verify(commandLine).addProperty("property", "property value");
        PowerMockito.verifyZeroInteractions(commandLine.getOptions());
    }

    @Test
    public void testLooksLikeOption() {
        PowerMockito.when(commandLine.getOptions()).thenReturn(new Option[0]);

        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
        assertFalse(commandLine.looksLikeOption(""));

        PowerMockito.verifyZeroInteractions(commandLine.getOptions());
    }
}