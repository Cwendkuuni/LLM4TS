package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Create a new WriteableCommandLineImpl
        Option rootOption = new Option("t", "test", false, "Test flag");
        List arguments = new ArrayList();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Add some options
        Option option1 = new Option("i", "input", true, "Input file");
        commandLine.addOption(option1);
        commandLine.addValue(option1, "input.txt");

        Option option2 = new Option("o", "output", true, "Output file");
        commandLine.addOption(option2);
        commandLine.addValue(option2, "output.txt");

        Option option3 = new Option("d", "debug", false, "Debug mode");
        commandLine.addOption(option3);

        // Add some values
        commandLine.addValue(rootOption, "test");

        // Add a switch
        commandLine.addSwitch(option1, true);

        // Test if the command line has the options
        Option testOption = commandLine.getOption("test");
        Assert.assertNotNull(testOption);
        Assert.assertEquals("test", testOption.getLongOpt());
        Assert.assertEquals(false, testOption.isValueSeparator());
        Assert.assertFalse(testOption.isRequired());
        Assert.assertNull(testOption.getValue());
        Assert.assertEquals(1, commandLine.getOptions().size());

        Option inputOption = commandLine.getOption("input");
        Assert.assertNotNull(inputOption);
        Assert.assertEquals("input", inputOption.getLongOpt());
        Assert.assertTrue(inputOption.isValueSeparator());
        Assert.assertEquals("Input file", inputOption.getDescription());
        Assert.assertEquals("input.txt", inputOption.getValue());
        Assert.assertFalse(inputOption.isRequired());
        Assert.assertEquals(1, commandLine.getValues(inputOption).size());

        Option outputOption = commandLine.getOption("output");
        Assert.assertNotNull(outputOption);
        Assert.assertEquals("output", outputOption.getLongOpt());
        Assert.assertTrue(outputOption.isValueSeparator());
        Assert.assertEquals("Output file", outputOption.getDescription());
        Assert.assertEquals("output.txt", outputOption.getValue());
        Assert.assertFalse(outputOption.isRequired());
        Assert.assertEquals(1, commandLine.getValues(outputOption).size());

        Option debugOption = commandLine.getOption("debug");
        Assert.assertNotNull(debugOption);
        Assert.assertEquals("debug", debugOption.getLongOpt());
        Assert.assertFalse(debugOption.isValueSeparator());
        Assert.assertEquals("Debug mode", debugOption.getDescription());
        Assert.assertFalse(debugOption.getValue() == null);
        Assert.assertFalse(debugOption.isRequired());

        // Test if the command line can write properties
        commandLine.addProperty(option1, "inputProperty", "inputPropValue");
        Assert.assertEquals("inputPropValue", commandLine.getProperty("input", "inputProperty", null));
        Assert.assertEquals(1, commandLine.getProperties(option1).size());

        // Test if the command line can write a map with properties
        Map<String, String> properties = new HashMap<>();
        properties.put("prop1", "propValue1");
        properties.put("prop2", "propValue2");
        commandLine.addProperties(option1, properties);
        Assert.assertEquals(2, commandLine.getProperties(option1).size());
    }

    @Test
    public void testWriteableCommandLineImplWithError() {
        // Create a new WriteableCommandLineImpl
        Option rootOption = new Option("t", "test", false, "Test flag");
        List arguments = new ArrayList();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Add some options
        Option option1 = new Option("i", "input", true, "Input file");
        commandLine.addOption(option1);

        // Add some errors
        try {
            commandLine.addValue(option1, null);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            commandLine.addSwitch(option1, null);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            commandLine.addProperty(option1, null, null);
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test if the command line has errors
        Assert.assertEquals(3, commandLine.getErrors().size());
    }

    @Test
    public void testWriteableCommandLineImplWithoutRootOption() {
        // Create a new WriteableCommandLineImpl without a root option
        List arguments = new ArrayList();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, arguments);

        // Add some options and values
        Option option1 = new Option("i", "input", true, "Input file");
        commandLine.addOption(option1);
        commandLine.addValue(option1, "input.txt");

        Option option2 = new Option("o", "output", true, "Output file");
        commandLine.addOption(option2);
        commandLine.addValue(option2, "output.txt");

        // Test if the command line has errors
        Assert.assertEquals(2, commandLine.getErrors().size());
    }

    @Test
    public void testWriteableCommandLineImplWithNoValues() {
        // Create a new WriteableCommandLineImpl
        Option rootOption = new Option("t", "test", false, "Test flag");
        List arguments = new ArrayList();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Add some options
        Option option1 = new Option("i", "input", true, "Input file");
        commandLine.addOption(option1);

        Option option2 = new Option("o", "output", true, "Output file");
        commandLine.addOption(option2);

        // Test if the command line has the options without values
        Option inputOption = commandLine.getOption("input");
        Assert.assertNotNull(inputOption);
        Assert.assertEquals("input", inputOption.getLongOpt());
        Assert.assertTrue(inputOption.isValueSeparator());
        Assert.assertEquals("Input file", inputOption.getDescription());
        Assert.assertNull(inputOption.getValue());
        Assert.assertFalse(inputOption.isRequired());

        Option outputOption = commandLine.getOption("output");
        Assert.assertNotNull(outputOption);
        Assert.assertEquals("output", outputOption.getLongOpt());
        Assert.assertTrue(outputOption.isValueSeparator());
        Assert.assertEquals("Output file", outputOption.getDescription());
        Assert.assertNull(outputOption.getValue());
        Assert.assertFalse(outputOption.isRequired());
    }

    @Test
    public void testWriteableCommandLineImplWithNoSwitches() {
        // Create a new WriteableCommandLineImpl
        Option rootOption = new Option("t", "test", false, "Test flag");
        List arguments = new ArrayList();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Add some options
        Option option1 = new Option("i", "input", true, "Input file");
        commandLine.addOption(option1);

        Option option2 = new Option("o", "output", true, "Output file");
        commandLine.addOption(option2);

        // Test if the command line has no switches
        Assert.assertEquals(0, commandLine.getSwitches().size());
    }

    @Test
    public void testWriteableCommandLineImplWithNoProperties() {
        // Create a new WriteableCommandLineImpl
        Option rootOption = new Option("t", "test", false, "Test flag");
        List arguments = new ArrayList();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Add some options
        Option option1 = new Option("i", "input", true, "Input file");
        commandLine.addOption(option1);

        Option option2 = new Option("o", "output", true, "Output file");
        commandLine.addOption(option2);

        // Test if the command line has no properties
        Assert.assertEquals(0, commandLine.getProperties().size());
    }
}