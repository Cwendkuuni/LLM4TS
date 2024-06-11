package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseOptions;

/**
 * Test for {@link Options} class.
 */
public class OptionsTest {

    /**
     * Test for addOption(String, String) method.
     */
    @org.junit.Test
    public void testAddOptionStringString() {
        Options options = new Options();

        // Add option with short name and no argument
        Option optionShortNoArg = OptionBuilder.hasOptionalValue(false, "shortOption")
                .build();
        options.addOption(optionShortNoArg.getKey(), optionShortNoArg.getLongOpt(), false,
                optionShortNoArg.getDescription());

        // Add option with short name and required argument
        Option optionShortArgReq = OptionBuilder.hasOptionalValue(true, "shortOptionArgReq")
                .isRequired(true)
                .build();
        options.addOption(optionShortArgReq.getKey(), optionShortArgReq.getLongOpt(), false,
                optionShortArgReq.getDescription());

        // Add option with long name and no argument
        Option optionLongNoArg = OptionBuilder.hasOptionalValue(false, "longOption")
                .build();
        options.addOption(optionLongNoArg.getLongOpt(), optionLongNoArg);

        // Add option with long name and required argument
        Option optionLongArgReq = OptionBuilder.hasOptionalValue(true, "longOptionArgReq")
                .isRequired(true)
                .build();
        options.addOption(optionLongArgReq.getLongOpt(), optionLongArgReq);

        // Verify options have been added
        List<Option> optionsList = options.helpOptions();
        assertEquals(4, optionsList.size());
        Option optionShortNoArgExpected = new Option("shortOption", "shortOption", false,
                "No argument option");
        Option optionShortArgReqExpected = new Option("shortOptionArgReq", "shortOptionArgReq", false,
                "Required argument option");
        Option optionLongNoArgExpected = new Option("longOption", "longOption", false,
                "No argument option");
        Option optionLongArgReqExpected = new Option("longOptionArgReq", "longOptionArgReq", false,
                "Required argument option");
        for (Option option : optionsList) {
            if (option.getKey().equals(optionShortNoArgExpected.getKey())) {
                assertSame(optionShortNoArgExpected, option);
            } else if (option.getKey().equals(optionShortArgReqExpected.getKey())) {
                assertSame(optionShortArgReqExpected, option);
            } else if (option.getKey().equals(optionLongNoArgExpected.getKey())) {
                assertSame(optionLongNoArgExpected, option);
            } else if (option.getKey().equals(optionLongArgReqExpected.getKey())) {
                assertSame(optionLongArgReqExpected, option);
            }
        }

        // Test parsing with options
        String[] args = new String[] {"--longOptionArgReq", "value"};
        ParseOptions parseOptions = new ParseOptions(options);
        CommandLine commandLine = null;
        try {
            commandLine = new DefaultParser().parse(parseOptions, args);
        } catch (ParseException e) {
            e.printStackTrace();
            fail("ParseException thrown. " + e.getMessage());
        }

        // Test options have been set
        Map<String, Object> parsedOptions = commandLine.getOptions();
        assertEquals(2, parsedOptions.size());
        Object optionShortArgReqValue = parsedOptions.get("longOptionArgReq");
        assertSame(optionLongArgReqExpected, optionShortArgReqValue);
        String optionLongArgReqValue = (String) parsedOptions.get("longOptionArgReq");
        assertEquals("value", optionLongArgReqValue);

        // Test parsing with no options
        args = new String[0];
        commandLine = null;
        try {
            commandLine = new DefaultParser().parse(parseOptions, args);
        } catch (ParseException e) {
            e.printStackTrace();
            fail("ParseException thrown. " + e.getMessage());
        }
        assertNull(commandLine.getOptionValues("shortOption"));
        assertNull(commandLine.getOptionValues("longOption"));

        // Test parsing with short options
        args = new String[] {"-s", "value"};
        commandLine = null;
        try {
            commandLine = new DefaultParser().parse(parseOptions, args);
        } catch (ParseException e) {
            e.printStackTrace();
            fail("ParseException thrown. " + e.getMessage());
        }
        String[] optionShortArgReqValue = commandLine.getOptionValues("shortOptionArgReq");
        assertNull(optionShortArgReqValue);
        String[] optionShortNoArgValue = commandLine.getOptionValues("shortOption");
        assertEquals(new String[] {"value"}, optionShortNoArgValue);

        // Test parsing with long options
        args = new String[] {"--longOption", "value"};
        commandLine = null;
        try {
            commandLine = new DefaultParser().parse(parseOptions, args);
        } catch (ParseException e) {
            e.printStackTrace();
            fail("ParseException thrown. " + e.getMessage());
        }
        optionShortArgReqValue = commandLine.getOptionValues("shortOptionArgReq");
        assertNull(optionShortArgReqValue);
        optionShortNoArgValue = commandLine.getOptionValues("shortOption");
        assertEquals(new String[] {"value"}, optionShortNoArgValue);
    }

    /**
     * Test for addOptionGroup method.
     */
    @org.junit.Test
    public void testAddOptionGroup() {
        Options options = new Options();

        // Create OptionGroup
        OptionGroup optionGroup = new OptionGroup();
        Option optionGroupOption = OptionBuilder.hasOptionalValue(false, "optionGroupOption")
                .build();
        optionGroup.addOption(optionGroupOption);

        // Add OptionGroup using addOptionGroup method
        options.addOptionGroup(optionGroup);

        // Verify OptionGroup has been added
        List<OptionGroup> optionGroups = options.getOptionGroups();
        assertEquals(1, optionGroups.size());
        OptionGroup optionGroupAdded = optionGroups.get(0);
        assertSame(optionGroup, optionGroupAdded);

        // Test parsing with OptionGroup and its options
        String[] args = {"--optionGroupOption"};
        ParseOptions parseOptions = new ParseOptions(options);
        CommandLine commandLine = null;
        try {
            commandLine = new DefaultParser().parse(parseOptions, args);
        } catch (ParseException e) {
            e.printStackTrace();
            fail("ParseException thrown. " + e.getMessage());
        }
        assertSame(optionGroupAdded, commandLine.getOptions().getOptionGroups().get(0));
        assertNull(commandLine.getOptionValues("shortOption"));
        assertNull(commandLine.getOptionValues("longOption"));
        assertEquals(new String[] {"value"}, commandLine.getOptionValues("optionGroupOption"));
    }

    /**
     * Test for hasOption method.
     */
    @org.junit.Test
    public void testHasOption() {
        Options options = new Options();

        // Add options
        Option optionShortNoArg = OptionBuilder.hasOptionalValue(false, "shortOption")
                .build();
        Option optionLongNoArg = OptionBuilder.hasOptionalValue(false, "longOption")
                .build();
        options.addOption(optionShortNoArg);
        options.addOption(optionLongNoArg);

        // Test hasShortOption method
        assertTrue(options.hasShortOption("shortOption"));
        assertTrue(options.hasShortOption("longOption"));
        assertFalse(options.hasShortOption("unknownOption"));

        // Test hasLongOption method
        assertTrue(options.hasLongOption("longOption"));
        assertFalse(options.hasLongOption("unknownOption"));

        // Test hasOption method when option has no argument
        assertTrue(options.hasOption("shortOption"));
        assertTrue(options.hasOption("longOption"));
        assertFalse(options.hasOption("unknownOption"));

        // Test hasOption method when option has required argument
        Option optionShortArgReq = OptionBuilder.hasOptionalValue(true, "shortOptionArgReq")
                .isRequired(true)
                .build();
        options.addOption(optionShortArgReq);
        assertTrue(options.hasOption("shortOption"));
        assertTrue(options.hasOption("longOption"));
        assertFalse(options.hasOption("shortOptionArgReq"));
        assertFalse(options.hasOption("longOptionArgReq"));
    }

    /**
     * Test for getOption method.
     */
    @org.junit.Test
    public void testGetOption() {
        Options options = new Options();

        // Add options
        Option optionShortNoArg = OptionBuilder.hasOptionalValue(false, "shortOption")
                .build();
        Option optionLongNoArg = OptionBuilder.hasOptionalValue(false, "longOption")
                .build();
        Option optionShortArgReq = OptionBuilder.hasOptionalValue(true, "shortOptionArgReq")
                .isRequired(true)
                .build();
        Option optionLongArgReq = OptionBuilder.hasOptionalValue(true, "longOptionArgReq")
                .isRequired(true)
                .build();
        Option optionGroupOption = OptionBuilder.hasOptionalValue(false, "optionGroupOption")
                .build();
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(optionGroupOption);
        options.addOptionGroup(optionGroup);
        options.addOption(optionShortNoArg);
        options.addOption(optionLongNoArg);
        options.addOption(optionShortArgReq);
        options.addOption(optionLongArgReq);

        // Test getOption method when option has no argument
        Option optionShortNoArgExpected = options.getOption("shortOption");
        Option optionLongNoArgExpected = options.getOption("longOption");
        assertSame(optionShortNoArgExpected, optionShortNoArg);
        assertSame(optionLongNoArgExpected, optionLongNoArg);

        // Test getOption method when option has required argument
        Option optionShortArgReqExpected = options.getOption("shortOptionArgReq");
        Option optionLongArgReqExpected = options.getOption("longOptionArgReq");
        assertNull(optionShortArgReqExpected);
        assertNull(optionLongArgReqExpected);

        // Test getOption method with a long option name that matches a short option name
        Option optionShortArgReqMatchLong = OptionBuilder.hasOptionalValue(true, "shortOptionArgReqMatchLong")
                .isRequired(true)
                .build();
        options.addOption(optionShortArgReqMatchLong);
        Option optionLongArgReqMatchShort = OptionBuilder.hasOptionalValue(true, "longOptionArgReqMatchShort")
                .isRequired(true)
                .build();
        options.addOption(optionLongArgReqMatchShort);
        Option optionLongOption = OptionBuilder.hasOptionalValue(false, "longOptionArgReqMatchShort")
                .build();
        options.addOption(optionLongOption);
        Option optionExpected = options.getOption("shortOptionArgReqMatchLong");
        assertSame(optionShortArgReqMatchLong, optionExpected);
        assertSame(optionShortArgReqMatchLong, options.getOption("longOptionArgReqMatchShort"));
        assertSame(optionLongArgReqMatchShort, options.getOption("longOptionArgReqMatchShort"));
        assertSame(optionLongOption, options.getOption("longOptionArgReqMatchShort"));

        // Test getOption method when option is part of an OptionGroup
        Option optionGroupOptionExpected = options.getOption("optionGroupOption");
        OptionGroup optionGroupExpected = options.getOptionGroup("optionGroup");
        assertSame(optionGroupOptionExpected, optionGroupOption);
        assertSame(optionGroupExpected, optionGroup);
    }
}