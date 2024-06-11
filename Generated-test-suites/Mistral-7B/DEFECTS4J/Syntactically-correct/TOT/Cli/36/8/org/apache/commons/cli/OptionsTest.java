package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("o", "output", false, "Output file name");
        Option option1 = options.addOption("i", "input", true, "Input file name");
        Option option2 = options.addOption("g", "group", false, "Input group name");
        OptionGroup group = new OptionGroup("group", "Input group options");
        group.setRequired(false);
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
    }

    @Test
    public void testAddOptionWithShortName() {
        Option option = options.getOption("o");
        assertEquals("Output file name", option.getLongOpt());
        assertEquals('o', option.getKey());
        assertFalse(option.hasArg());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionWithShortNameAndValue() {
        Option option = options.getOption("i");
        assertEquals("Input file name", option.getLongOpt());
        assertEquals('i', option.getKey());
        assertTrue(option.hasArg());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionWithShortNameAndValueSet() {
        options.parse("--output test.txt");
        Option option = options.getOption("o");
        assertEquals("test.txt", option.getValue());
    }

    @Test(expected = ParseException.class)
    public void testAddOptionWithShortNameAndInvalidValue() {
        options.parse("--output test.txt file.txt");
    }

    @Test
    public void testAddOptionWithLongName() {
        Option option = options.getOption("group");
        assertEquals("Input group options", option.getDescription());
        assertEquals(Option.NO_ARGUMENT, option.getOpt());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = options.getOptionGroup("group");
        assertNotNull(group);
        List<Option> optionsInGroup = group.getOptions();
        assertEquals(2, optionsInGroup.size());
        for (Option option : optionsInGroup) {
            assertTrue(option.isVisible());
        }
    }

    @Test
    public void testParseWithNoOptions() {
        try {
            options.parse(new String[0]);
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() {
        options.parse(new String[]{"--invalid"});
    }

    @Test
    public void testParseWithValidOptions() {
        options.parse(new String[]{"--output", "test.txt", "--input", "input.txt"});
        Option optionOutput = options.getOption("o");
        Option optionInput = options.getOption("i");
        assertEquals("test.txt", optionOutput.getValue());
        assertEquals("input.txt", optionInput.getValue());
    }

    @Test
    public void testParseWithLongOptionArg() {
        options.parse(new String[]{"--output", "test--output.txt"});
        Option optionOutput = options.getOption("o");
        assertEquals("test--output.txt", optionOutput.getValue());
    }

    @Test
    public void testParseWithShortOptionArg() {
        options.parse(new String[]{"--o", "test.txt"});
        Option optionOutput = options.getOption("o");
        assertEquals("test.txt", optionOutput.getValue());
    }

    @Test
    public void testParseWithShortAndLongOption() {
        options.parse(new String[]{"--o", "test.txt", "--output", "test--output.txt"});
        Option optionOutput = options.getOption("o");
        assertEquals("test.txt", optionOutput.getValue());
        Option optionOutputLong = options.getOption("output");
        assertEquals("test--output.txt", optionOutputLong.getValue());
    }

    @Test
    public void testParseWithShortOptionArgAndLongOption() {
        options.parse(new String[]{"--o", "test.txt", "--output", "test--output.txt"});
        Option optionOutput = options.getOption("o");
        assertEquals("test.txt", optionOutput.getValue());
        Option optionOutputLong = options.getOption("output");
        assertEquals("test--output.txt", optionOutputLong.getValue());
    }

    @Test
    public void testParseWithShortOptionArgAndLongOptionWithValue() {
        options.parse(new String[]{"--o", "test.txt", "--output", "test--output.txt=output.txt"});
        Option optionOutput = options.getOption("o");
        assertEquals("test.txt", optionOutput.getValue());
        Option optionOutputLong = options.getOption("output");
        assertEquals("output.txt", optionOutputLong.getValue());
    }

    @Test
    public void testParseWithOptionGroup() {
        options.parse(new String[]{"--group"});
        OptionGroup group = options.getOptionGroup("group");
        List<Option> optionsInGroup = group.getOptions();
        assertEquals(2, optionsInGroup.size());
        for (Option option : optionsInGroup) {
            assertTrue(option.isVisible());
        }
    }

    @Test
    public void testParseWithOptionGroupAndOption() {
        options.parse(new String[]{"--group", "--i", "input.txt"});
        OptionGroup group = options.getOptionGroup("group");
        List<Option> optionsInGroup = group.getOptions();
        assertEquals(2, optionsInGroup.size());
        for (Option option : optionsInGroup) {
            assertTrue(option.isVisible());
        }
        Option optionInput = options.getOption("i");
        assertEquals("input.txt", optionInput.getValue());
    }

    @Test
    public void testParseWithOptionGroupAndOptionWithValue() {
        options.parse(new String[]{"--group", "--i", "input.txt", "--o", "output.txt"});
        OptionGroup group = options.getOptionGroup("group");
        List<Option> optionsInGroup = group.getOptions();
        assertEquals(2, optionsInGroup.size());
        for (Option option : optionsInGroup) {
            assertTrue(option.isVisible());
        }
        Option optionInput = options.getOption("i");
        assertEquals("input.txt", optionInput.getValue());
        Option optionOutput = options.getOption("o");
        assertEquals("output.txt", optionOutput.getValue());
    }

    @Test
    public void testParseWithRequiredOption() {
        options.addOption("r", "required", true, "Required option");
        try {
            options.parse(new String[0]);
            fail();
        } catch (ParseException e) {
            List<String> messages = Arrays.asList(e.getMessages());
            List<String> expected = Arrays.asList("Missing required option 'r'.");
            assertEquals(expected, messages);
        }
    }

    @Test
    public void testParseWithOptionGroupAndRequiredOption() {
        options.addOption("r", "required", true, "Required option");
        OptionGroup group = new OptionGroup("group", "Input group options");
        group.setRequired(true);
        group.addOption(options.getOption("i"));
        options.addOptionGroup(group);
        try {
            options.parse(new String[0]);
            fail();
        } catch (ParseException e) {
            List<String> messages = Arrays.asList(e.getMessages());
            List<String> expected = Arrays.asList("Missing required option 'i'.");
            assertEquals(expected, messages);
        }
    }
}