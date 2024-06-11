import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("t", "test", false, "Test option");
        List arguments = new ArrayList<>();
        arguments.add("test1");
        arguments.add("test2");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Test addOption method
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("o", "option2", true, "Option 2");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandline.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));

        // Test addValue method
        writeableCommandLine.addValue(option1, "value1");
        List values = writeableCommandLine.getValues(option1, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        // Test addSwitch method
        writeableCommandLine.addSwitch(option1, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option1, false);
        assertTrue(switchValue);

        // Test hasOption method
        assertTrue(writeableCommandLine.hasOption(option1));

        // Test getOption method
        Option optionFromGetOption = writeableCommandLine.getOption("o");
        assertEquals(option1, optionFromGetOption);

        // Test getValues method
        List defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        writeableCommandLine.setDefaultValues(option1, defaultValues);
        values = writeableCommandLine.getValues(option1, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("defaultValue1", writeableCommandLine.getValues(option1, null).get(0));

        // Test getSwitch method
        Boolean defaultSwitch = false;
        writeableCommandLine.setDefaultSwitch(option1, defaultSwitch);
        switchValue = writeableCommandLine.getSwitch(option1, defaultSwitch);
        assertFalse(switchValue);
        assertEquals(defaultSwitch, writeableCommandLine.getSwitch(option1, null));

        // Test addProperty method
        writeableCommandLine.addProperty("property", "value");
        String propertyValue = writeableCommandLine.getProperty("property", "defaultValue");
        assertEquals("value", propertyValue);

        // Test getProperty method
        writeableCommandLine.addProperty("property2", "defaultValue");
        propertyValue = writeableCommandLine.getProperty("property2", "value");
        assertEquals("defaultValue", propertyValue);

        // Test getProperties method
        Set properties = writeableCommandLine.getProperties();
        assertEquals(1, properties.size());
        assertTrue(properties.contains("property"));

        // Test looksLikeOption method
        assertTrue(writeableCommandLine.looksLikeOption("o"));
        assertFalse(writeableCommandLine.looksLikeOption("n"));

        // Test toString method
        String toString = writeableCommandLine.toString();
        String expectedToString = "test1 test2";
        assertEquals(expectedToString, toString);

        // Test getOptions method
        List optionsList = writeableCommandLine.getOptions();
        assertEquals(2, optionsList.size());
        assertTrue(optionsList.contains(option1));
        assertTrue(optionsList.contains(option2));

        // Test getOptionTriggers method
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("o"));
    }
}