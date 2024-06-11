import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file path");
        List arguments = new ArrayList();
        arguments.add("--output=test.txt");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        // test addOption
        Option option1 = new Option("i", "input", true, "Input file path");
        writeableCommandLine.addOption(option1);
        Option option2 = new Option("h", "help", false, "Prints help message");
        writeableCommandLine.addOption(option2);
        List<Option> options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        Option actualOption1 = options.get(0);
        Option expectedOption1 = new Option("i", "input", true, "Input file path");
        assertEquals(expectedOption1.getLongOpt(), actualOption1.getLongOpt());
        assertEquals(expectedOption1.getShortOpt(), actualOption1.getShortOpt());
        assertEquals(expectedOption1.isRequired(), actualOption1.isRequired());
        assertEquals(expectedOption1.getDescription(), actualOption1.getDescription());

        Option actualOption2 = options.get(1);
        Option expectedOption2 = new Option("h", "help", false, "Prints help message");
        assertEquals(expectedOption2.getLongOpt(), actualOption2.getLongOpt());
        assertEquals(expectedOption2.getShortOpt(), actualOption2.getShortOpt());
        assertEquals(expectedOption2.isRequired(), actualOption2.isRequired());
        assertEquals(expectedOption2.getDescription(), actualOption2.getDescription());

        // test hasOption
        assertTrue(writeableCommandLine.hasOption(option));
        assertFalse(writeableCommandLine.hasOption(option1));

        // test getOption
        Option actualOption = writeableCommandLine.getOption("output");
        Option expectedOption = option;
        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
        assertEquals(expectedOption.getShortOpt(), actualOption.getShortOpt());
        assertEquals(expectedOption.isRequired(), actualOption.isRequired());
        assertEquals(expectedOption.getDescription(), actualOption.getDescription());

        // test getValues
        List<Object> actualValues = writeableCommandLine.getValues(option, null);
        List<Object> expectedValues = new ArrayList<>();
        expectedValues.add("test.txt");
        assertEquals(expectedValues, actualValues);

        // test getUndefaultedValues
        List<Object> actualUndefaultedValues = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(Collections.EMPTY_LIST, actualUndefaultedValues);

        // test getSwitch
        Boolean actualSwitch = writeableCommandLine.getSwitch(option, false);
        Boolean expectedSwitch = null;
        assertEquals(expectedSwitch, actualSwitch);

        // test getProperty
        String actualProperty = writeableCommandLine.getProperty("output");
        String expectedProperty = "test.txt";
        assertEquals(expectedProperty, actualProperty);

        // test addProperty
        writeableCommandLine.addProperty(option, "fileType", "txt");
        Map<String, String> actualProperties = writeableCommandLine.getProperties(option);
        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("fileType", "txt");
        assertEquals(expectedProperties, actualProperties);

        // test getProperties
        Map<String, String> actualAllProperties = writeableCommandLine.getProperties();
        Map<String, String> expectedAllProperties = new HashMap<>();
        expectedAllProperties.put("fileType", "txt");
        assertEquals(expectedAllProperties, actualAllProperties);

        // test looksLikeOption
        assertTrue(writeableCommandLine.looksLikeOption("--output"));
        assertFalse(writeableCommandLine.looksLikeOption("--input"));

        // test toString
        String actualToString = writeableCommandLine.toString();
        String expectedToString = "--output=test.txt --input";
        assertEquals(expectedToString, actualToString);

        // test getOptions
        List<Option> actualOptions = writeableCommandLine.getOptions();
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        assertEquals(expectedOptions, actualOptions);

        // test getOptionTriggers
        Set<String> actualOptionTriggers = writeableCommandLine.getOptionTriggers();
        Set<String> expectedOptionTriggers = new HashSet<>();
        expectedOptionTriggers.add("output");
        expectedOptionTriggers.add("input");
        expectedOptionTriggers.add("help");
        assertEquals(expectedOptionTriggers, actualOptionTriggers);

        // test setDefaultValues
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default.txt");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List<Object> actualDefaultValues = writeableCommandLine.getValues(option, null);
        List<Object> expectedDefaultValues = new ArrayList<>();
        expectedDefaultValues.add("default.txt");
        expectedDefaultValues.add("test.txt");
        assertEquals(expectedDefaultValues, actualDefaultValues);

        // test setDefaultSwitch
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean actualDefaultSwitch = writeableCommandLine.getSwitch(option, false);
        assertEquals(defaultSwitch, actualDefaultSwitch);

        // test getNormalised
        List<String> actualNormalised = writeableCommandLine.getNormalised();
        List<String> expectedNormalised = new ArrayList<>();
        expectedNormalised.add("--output=test.txt");
        assertEquals(expectedNormalised, actualNormalised);
    }
}