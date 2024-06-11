import org.apache.commons.cli2.BaseOption;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.ParseResults;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        commandLine = new CliCommandLineParser().parse(options, new String[]{"-a", "--optionA", "valueA", "-b", "valueB"});
    }

    @Test
    public void testAddOption() {
        Option option = new BaseOption("optionC", "c", false, "Option C");
        commandLine.addOption(option);
        assertEquals(3, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        Option option = new BaseOption("optionA", "a", true, "Option A");
        commandLine.addOption(option);
        commandLine.addValue(option, "valueC");
        List<Object> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("valueA", values.get(0));
        assertEquals("valueC", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new BaseOption("optionB", "b", false, "Option B");
        commandLine.addOption(option);
        List<Object> values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new BaseOption("optionA", "a", true, "Option A");
        commandLine.addOption(option);
        List<Object> defaults = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaults);
        List<Object> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new BaseOption("optionD", "d", false, "Option D");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(option.getValue().isPresent());
        assertTrue(option.getValue().get());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new BaseOption("optionE", "e", false, "Option E");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new BaseOption("optionF", "f", false, "Option F");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertFalse(option.getValue().isPresent());
        assertFalse(option.getValue().get());
    }

    @Test
    public void testAddProperty() {
        Option option = new BaseOption("optionG", "g", false, "Option G");
        commandLine.addOption(option);
        commandLine.addProperty(option, "propertyA", "propertyValueA");
        assertEquals("propertyValueA", commandLine.getOptionValue(option, "propertyA"));
    }

    @Test
    public void testAddPropertyDefault() {
        commandLine.addProperty("propertyB", "propertyValueB");
        assertEquals("propertyValueB", commandLine.getOptionValue(null, "propertyB"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-a"));
        assertTrue(commandLine.looksLikeOption("--optionA"));
        assertFalse(commandLine.looksLikeOption("valueA"));
        assertFalse(commandLine.looksLikeOption("valueB"));
    }

    @Test
    public void testGetCurrentOption() {
        Option option = new BaseOption("optionH", "h", false, "Option H");
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        Option optionA = new BaseOption("optionA", "a", true, "Option A");
        Option optionB = new BaseOption("optionB", "b", true, "Option B");
        commandLine.addOption(optionA);
        commandLine.addOption(optionB);
        commandLine.setCurrentOption(optionB);
        assertEquals(optionB, commandLine.getCurrentOption());
    }
}