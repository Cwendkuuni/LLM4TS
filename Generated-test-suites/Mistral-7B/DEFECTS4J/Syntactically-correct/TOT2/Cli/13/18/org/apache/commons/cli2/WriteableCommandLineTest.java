package org.apache.commons.cli2.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine() { // Dummy implementation
        };
        Option option = new Option("input", "inputFile", false, "Set input file");
        commandLine.addOption(option);
        assertNotNull(commandLine.getOptions().stream().filter(opt -> opt.getValue().equals(option.getValue())).findFirst().orElse(null));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLine() { // Dummy implementation
        };
        Option option = new Option("input", "inputFile", true, "Set input file");
        commandLine.addOption(option);
        commandLine.addValue(option, new Value("test.txt"));
        assertEquals("test.txt", commandLine.getOptionValues("inputFile").get(0).getValue());
    }

    @Test
    public void testGetOptionValues() {
        WriteableCommandLine commandLine = new WriteableCommandLine() { // Dummy implementation
            @Override
            public List<Value> getOptionValues(Option option) {
                return new Values();
            }
        };
        Option inputOption = new Option("input", "inputFile", true, "Set input file");
        commandLine.addOption(inputOption);
        commandLine.addValue(inputOption, new Value("test.txt"));
        assertEquals("test.txt", commandLine.getOptionValues(inputOption).get(0).getValue());
    }

    @Test(expected = DuplicateOptionException.class)
    public void testAddDuplicateOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine() { // Dummy implementation
        };
        Option inputOption = new Option("input", "inputFile", true, "Set input file");
        commandLine.addOption(inputOption);
        commandLine.addOption(inputOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueToNonOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine() { // Dummy implementation
        };
        commandLine.addValue(new Object(), "test.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueToMultipleOptions() {
        WriteableCommandLine commandLine = new WriteableCommandLine() { // Dummy implementation
        };
        Option inputOption1 = new Option("input1", "inputFile1", true, "Set input file 1");
        Option inputOption2 = new Option("input2", "inputFile2", true, "Set input file 2");
        commandLine.addOption(inputOption1);
        commandLine.addOption(inputOption2);
        commandLine.addValue(inputOption1, new Value("test.txt"));
        commandLine.addValue(inputOption2, new Value("test.txt"));
    }
}