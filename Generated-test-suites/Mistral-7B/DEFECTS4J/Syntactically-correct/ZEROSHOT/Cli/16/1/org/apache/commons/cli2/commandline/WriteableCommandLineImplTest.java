import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl writeableCommandLine;
    private Option option;

    @Before
    public void setUp() {
        option = new Option("o", "option", true, "An option with a value");
        writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
    }

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        writeableCommandLine.addValue(option, "value1");
        assertEquals(1, writeableCommandLine.getValues(option, null).size());
        assertEquals("value1", writeableCommandLine.getValues(option, null).get(0));

        writeableCommandLine.addValue(option, "value2");
        assertEquals(2, writeableCommandLine.getValues(option, null).size());
        assertEquals("value1", writeableCommandLine.getValues(option, null).get(0));
        assertEquals("value2", writeableCommandLine.getValues(option, null).get(1));
    }

    @Test
    public void testAddSwitch() {
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));

        try {
            writeableCommandLine.addSwitch(option, true);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testHasOption() {
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        assertEquals(option, writeableCommandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        List emptyValues = writeableCommandLine.getValues(option, Collections.emptyList());
        assertEquals(0, emptyValues.size());
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());

        List undefaultedValues = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(0, undefaultedValues.size());

        writeableCommandLine.setDefaultValues(option, Collections.singletonList("defaultValue"));
        values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());

        undefaultedValues = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(2, undefaultedValues.size());
        assertEquals("value1", undefaultedValues.get(0));
        assertEquals("value2", undefaultedValues.get(1));
    }

    @Test
    public void testGetSwitch() {
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertFalse(writeableCommandLine.getSwitch(option, true));

        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertTrue(writeableCommandLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        writeableCommandLine.addProperty(option, "property", "propertyValue");
        assertEquals("propertyValue", writeableCommandLine.getProperty(option, "property"));
        assertEquals("propertyValue", writeableCommandLine.getProperty(option, "property", "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        writeableCommandLine.addProperty(option, "property1", "propertyValue1");
        writeableCommandLine.addProperty(option, "property2", "propertyValue2");

        Set properties = writeableCommandLine.getProperties(option);
        assertEquals(2, properties.size());

        Map propertiesMap = (Map) writeableCommandLine.getOptionToProperties().get(option);
        assertEquals(2, propertiesMap.size());
    }

    @Test
    public void testGetProperties() {
        writeableCommandLine.addProperty(option, "property1", "propertyValue1");
        writeableCommandLine.addProperty(option, "property2", "propertyValue2");

        Set properties = writeableCommandLine.getProperties();
        assertEquals(0, properties.size());

        Set propertiesOption = writeableCommandLine.getProperties(option);
        assertEquals(2, propertiesOption.size());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("o"));
        assertFalse(writeableCommandLine.looksLikeOption("x"));
    }

    @Test
    public void testToString() {
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");

        writeableCommandLine = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        String expected = "-o value1 value2 arg1 arg2";
        String actual = writeableCommandLine.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOptionsList() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        List options = writeableCommandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggersSet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertEquals("o", optionTriggers.iterator().next());
    }

    @Test
    public void testSetDefaultValues() {
        writeableCommandLine.setDefaultValues(option, Arrays.asList("defaultValue1", "defaultValue2"));
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
        assertTrue(writeableCommandLine.getSwitch(option, true));
    }

    @Test
    public void testGetNormalisedList() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        List normalised = writeableCommandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}