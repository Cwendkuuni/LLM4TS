import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private WriteableCommandLine writeableCommandLine;
    private Option option;

    @Before
    public void setUp() {
        option = new Option("o", "output", true, "Output file path");
    }

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLineImpl.addOption(option);
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        assertEquals(1, writeableCommandLine.getOptions().size());
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLineImpl.addOption(option);
        writeableCommandLineImpl.addValue(option, "test.txt");
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLineImpl.addOption(option);
        writeableCommandLineImpl.addSwitch(option, true);
        writeableCommandLineImpl.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLineImpl.addOption(option);
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLineImpl.addOption(option);
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        Option result = writeableCommandLine.getOption("o");
        assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLineImpl.addOption(option);
        writeableCommandLineImpl.addValue(option, "test.txt");
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.singletonList("test.txt"));
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        List values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.emptyList());
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        Boolean result = writeableCommandLine.getSwitch(option, false);
        assertFalse(result);
    }

    @Test
    public void testGetSwitchWithValue() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLineImpl.addSwitch(option, true);
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        Boolean result = writeableCommandLine.getSwitch(option, false);
        assertTrue(result);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new PropertyOption("p", "property"), Collections.emptyList());
        writeableCommandLineImpl.addProperty("property", "value");
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        String result = writeableCommandLine.getProperty("property");
        assertEquals("value", result);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new PropertyOption("p", "property"), Collections.emptyList());
        writeableCommandLineImpl.addProperty("property", "value");
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        String result = writeableCommandLine.getProperty("property");
        assertEquals("value", result);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(new PropertyOption("p", "property"), Collections.emptyList());
        writeableCommandLineImpl.addProperty("property", "value1");
        writeableCommandLineImpl.addProperty("property", "value2");
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        Set result = writeableCommandLine.getProperties("property");
        assertEquals(2, result.size());
        assertTrue(result.contains("property"));
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertTrue(writeableCommandLineImpl.looksLikeOption("o"));
        assertFalse(writeableCommandLineImpl.looksLikeOption("x"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.singletonList("arg1"));
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        String result = writeableCommandLine.toString();
        String expected = "arg1";
        assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLineImpl.addOption(option);
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        List options = writeableCommandLine.getOptions();
        assertEquals(1, options.size());
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLineImpl.addOption(option);
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertTrue(optionTriggers.contains("o"));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.emptyList());
        List defaultValues = Arrays.asList("default1", "default2");
        writeableCommandLineImpl.setDefaultValues(option, defaultValues);
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertTrue(values.containsAll(defaultValues));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLineImpl.setDefaultSwitch(option, true);
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        Boolean result = writeableCommandLine.getSwitch(option, false);
        assertTrue(result);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl writeableCommandLineImpl = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        WriteableCommandLine writeableCommandLine = writeableCommandLineImpl;
        List normalised = writeableCommandLine.getNormalised();
        List expected = Arrays.asList("arg1", "arg2");
        assertEquals(expected, normalised);
    }
}