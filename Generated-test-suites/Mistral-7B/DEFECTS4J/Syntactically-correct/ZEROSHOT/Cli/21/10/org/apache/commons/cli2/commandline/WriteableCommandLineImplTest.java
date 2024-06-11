import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Test default constructor
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertNull(writeableCommandLine.getOptions());
        assertNull(writeableCommandLine.getOptionTriggers());
        assertNull(writeableCommandLine.getNormalised());

        // Test constructor with rootOption and arguments
        Option rootOption = new Option("root", "rootOption", false, "rootDescription");
        WriteableCommandLine writeableCommandLineWithOption = new WriteableCommandLineImpl(rootOption, Collections.singletonList("arg1"));
        assertSame(rootOption, writeableCommandLineWithOption.getOption(rootOption.getPreferredName()));
        assertEquals(1, writeableCommandLineWithOption.getOptions().size());
        assertTrue(writeableCommandLineWithOption.getOptions().contains(rootOption));
        assertEquals(1, writeableCommandLineWithOption.getOptionTriggers().size());
        assertTrue(writeableCommandLineWithOption.getOptionTriggers().contains(rootOption.getPreferredName()));
        assertEquals(Collections.singletonList("arg1"), writeableCommandLineWithOption.getNormalised());

        // Test addOption
        Option option1 = new Option("o1", "option1", true, "option1Description");
        Option option2 = new Option("o2", "option2", false, "option2Description");
        writeableCommandLineWithOption.addOption(option1);
        writeableCommandLineWithOption.addOption(option2);
        assertEquals(3, writeableCommandLineWithOption.getOptions().size());
        assertTrue(writeableCommandLineWithOption.getOptions().containsAll(Arrays.asList(rootOption, option1, option2)));
        assertEquals(3, writeableCommandLineWithOption.getOptionTriggers().size());
        assertTrue(writeableCommandLineWithOption.getOptionTriggers().containsAll(Arrays.asList(rootOption.getPreferredName(), option1.getPreferredName(), option2.getPreferredName())));

        // Test addValue
        writeableCommandLineWithOption.addValue(option1, "value1");
        writeableCommandLineWithOption.addValue(option1, "value2");
        List values = writeableCommandLineWithOption.getValues(option1, null);
        assertEquals(2, values.size());
        assertTrue(values.containsAll(Arrays.asList("value1", "value2")));

        // Test addSwitch
        writeableCommandLineWithOption.addSwitch(option2, true);
        Boolean switchValue = writeableCommandLineWithOption.getSwitch(option2, false);
        assertTrue(switchValue);

        // Test hasOption
        assertEquals(true, writeableCommandLineWithOption.hasOption(option1));
        assertEquals(false, writeableCommandLineWithOption.hasOption(new Option("unknown", "unknownOption", false, "unknownDescription")));

        // Test getOption
        Option optionFromGetOption = writeableCommandLineWithOption.getOption(option1.getPreferredName());
        assertSame(option1, optionFromGetOption);

        // Test getValues
        List valuesFromGetValues = writeableCommandLineWithOption.getValues(option1, null);
        assertEquals(values, valuesFromGetValues);

        // Test getUndefaultedValues
        List valuesFromGetUndefaultedValues = writeableCommandLineWithOption.getUndefaultedValues(option1);
        assertEquals(Collections.emptyList(), valuesFromGetUndefaultedValues);

        // Test getSwitch
        Boolean switchValueFromGetSwitch = writeableCommandLineWithOption.getSwitch(option2, false);
        assertTrue(switchValueFromGetSwitch);

        // Test getProperty
        Option propertyOption = new Option("p", "propertyOption", false, "propertyDescription");
        writeableCommandLineWithOption.addProperty(propertyOption, "property", "propertyValue");
        String property = writeableCommandLineWithOption.getProperty(propertyOption, null);
        assertEquals("propertyValue", property);

        // Test addProperty
        Option propertyOption2 = new Option("p2", "propertyOption2", false, "propertyDescription2");
        writeableCommandLineWithOption.addProperty(propertyOption2, "property2", "propertyValue2");
        String property2 = writeableCommandLineWithOption.getProperty(propertyOption2, null);
        assertEquals("propertyValue2", property2);

        // Test getProperties
        Set properties = writeableCommandLineWithOption.getProperties(propertyOption);
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property"));

        // Test toString
        String toString = writeableCommandLineWithOption.toString();
        String expectedToString = "arg1 o1=value1,value2 o2";
        assertEquals(expectedToString, toString);

        // Test getListOptions
        List options = writeableCommandLineWithOption.getOptions();
        List expectedOptions = Arrays.asList(rootOption, option1, option2);
        assertEquals(expectedOptions, options);

        // Test setDefaultValues
        writeableCommandLineWithOption.setDefaultValues(option1, Arrays.asList("defaultValue1", "defaultValue2"));
        List defaultValues = writeableCommandLineWithOption.getValues(option1, null);
        assertEquals(4, defaultValues.size());
        assertTrue(defaultValues.containsAll(Arrays.asList("value1", "value2", "defaultValue1", "defaultValue2")));

        // Test setDefaultSwitch
        writeableCommandLineWithOption.setDefaultSwitch(option2, false);
        Boolean defaultSwitch = writeableCommandLineWithOption.getSwitch(option2, true);
        assertFalse(defaultSwitch);

        // Test getNormalised
        List normalised = writeableCommandLineWithOption.getNormalised();
        List expectedNormalised = Arrays.asList("arg1", "o1=value1,value2", "o2");
        assertEquals(expectedNormalised, normalised);
    }

    @Test
    public void testIllegalArgumentException() {
        Option option = new Option("o", "option", false, "optionDescription");
        List arguments = Arrays.asList("arg1", "arg2");

        try {
            new WriteableCommandLineImpl(option, arguments);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("The rootOption parameter must not be null", e.getMessage());
        }

        try {
            WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
            writeableCommandLine.addOption(option);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
        }

        try {
            WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, null);
            writeableCommandLine.addOption(option);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("The arguments parameter must not be null", e.getMessage());
        }

        try {
            Option rootOption2 = new Option("root2", "root2Option", false, "root2Description");
            WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.singletonList("arg1"));
            writeableCommandLine.addOption(rootOption2);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("An Option cannot be added as a child of itself", e.getMessage());
        }

        try {
            Option option2 = new Option("o2", "option2", false, "option2Description");
            Option option1 = new Option("o1", "option1", false, "option1Description");
            Option rootOption = new Option("root", "rootOption", false, "rootDescription");
            WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1"));
            writeableCommandLine.addOption(option1);
            writeableCommandLine.addOption(option2);
            writeableCommandLine.addOption(option1);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
        }

        try {
            Option option3 = new Option("o3", "option3", false, "option3Description");
            Option option2 = new Option("o2", "option2", false, "option2Description");
            Option option1 = new Option("o1", "option1", false, "option1Description");
            Option rootOption = new Option("root", "rootOption", false, "rootDescription");
            WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("arg1"));
            writeableCommandLine.addOption(option1);
            writeableCommandLine.addOption(option2);
            writeableCommandLine.addOption(option3);
            writeableCommandLine.addOption(option1);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
        }
    }
}