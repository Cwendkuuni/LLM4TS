import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file path");
        List arguments = new ArrayList();
        arguments.add("test.txt");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        // Test addOption method
        Option option1 = new Option("i", "input", true, "Input file path");
        writeableCommandLine.addOption(option1);
        Option option2 = new Option("h", "help", false, "Print help message");
        writeableCommandLine.addOption(option2);

        // Test hasOption method
        assertTrue(writeableCommandLine.hasOption(option));
        assertTrue(writeableCommandLine.hasOption(option1));
        assertFalse(writeableCommandLine.hasOption(new Option("nonexistent", "nonexistent", false, "Nonexistent option")));

        // Test getOption method
        Option retrievedOption = writeableCommandLine.getOption("output");
        assertEquals(option, retrievedOption);

        // Test getValues method
        List<String> values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));

        // Test getUndefaultedValues method
        List<String> undefaultedValues = writeableCommandLine.getUndefaultedValues(option);
        assertNull(undefaultedValues);

        // Test getSwitch method
        assertFalse(writeableCommandLine.getSwitch(option, false));

        // Test addProperty method
        String property = "property1";
        String propertyValue = "value1";
        writeableCommandLine.addProperty(option, property, propertyValue);
        Properties properties = (Properties) writeableCommandLine.getOptionToProperties().get(option);
        assertEquals(1, properties.size());
        assertEquals(propertyValue, properties.getProperty(property));

        // Test getProperty method
        String retrievedPropertyValue = writeableCommandLine.getProperty(option, property, null);
        assertEquals(propertyValue, retrievedPropertyValue);

        // Test getProperties method
        Set<String> propertiesSet = writeableCommandLine.getProperties(option);
        assertEquals(1, propertiesSet.size());
        assertTrue(propertiesSet.contains(property));

        // Test toString method
        String toString = writeableCommandLine.toString();
        String expectedToString = "output test.txt";
        assertEquals(expectedToString, toString);

        // Test getOptions method
        List<Option> options = writeableCommandLine.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(option));
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }
}