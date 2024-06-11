package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PatternBuilderTest {

    @org.junit.Test
    public void testPatternBuilder() {
        PatternBuilder pb = new PatternBuilder();

        // Test empty pattern
        pb.reset();
        Option option = pb.create();
        assertNull(option);

        // Test simple option without argument
        pb.withPattern("a:");
        option = pb.create();
        assertNotNull(option);
        assertEquals("a", option.getLongOpt());
        assertFalse(option.hasArg());

        // Test simple option with argument
        pb.reset();
        pb.withPattern("a:s");
        option = pb.create();
        assertNotNull(option);
        assertEquals("a", option.getLongOpt());
        assertTrue(option.hasArg());

        // Test required option
        pb.reset();
        pb.withPattern("a:s");
        pb.withPattern("a:!");
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.isRequired());

        // Test option with validator
        pb.reset();
        pb.withPattern("%d");
        option = pb.create();
        assertNotNull(option);
        assertNotNull(option.getValidator());

        // Test option with multiple validators
        pb.reset();
        pb.withPattern("%d:@java.lang.Long");
        option = pb.create();
        assertNotNull(option);
        assertNotNull(option.getValidator());
        assertEquals(LongValidator.class, option.getValidator().getClass());

        // Test option with min and max number of arguments
        pb.reset();
        pb.withPattern("a:*:1");
        option = pb.create();
        assertNotNull(option);
        assertEquals("a", option.getLongOpt());
        assertTrue(option.isAllowedMultipleValues());
        assertTrue(option.getMinimumNumberOfValues() == 1);

        // Test option with no argument
        pb.reset();
        pb.withPattern("a:");
        pb.withPattern("a:s:0");
        option = pb.create();
        assertNotNull(option);
        assertEquals("a", option.getLongOpt());
        assertFalse(option.hasArg());

        // Test option with multiple arguments
        pb.reset();
        pb.withPattern("a:*");
        pb.withPattern("a:s:1");
        option = pb.create();
        assertNotNull(option);
        assertEquals("a", option.getLongOpt());
        assertTrue(option.isAllowedMultipleValues());

        // Test option with pattern having invalid character
        pb.reset();
        assertThrows(IllegalArgumentException.class, () -> pb.withPattern("a:!:s"));

        // Test parse option with valid arguments
        CommandLine cmdLine = new CommandLine();
        pb.reset();
        pb.withPattern("a:s");
        option = pb.create();
        cmdLine.parseArgument(0, option);
        assertEquals("s", cmdLine.getOptionValue("a"));

        // Test parse option with no argument
        pb.reset();
        pb.withPattern("a:");
        option = pb.create();
        cmdLine.parseArgument(0, option);
        assertNull(cmdLine.getOptionValue("a"));

        // Test parse option with required argument
        pb.reset();
        pb.withPattern("a:s:1");
        option = pb.create();
        assertThrows(ParseException.class, () -> cmdLine.parseArgument(0, option));

        // Test parse option with incorrect data type
        pb.reset();
        pb.withPattern("%d");
        option = pb.create();
        assertThrows(ValidationException.class, () -> cmdLine.parseArgument(0, option, "abc"));

        // Test parse option with correct data type
        pb.reset();
        pb.withPattern("%d");
        option = pb.create();
        LocalDate date = LocalDate.parse("2022-03-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        cmdLine.parseArgument(0, option, date.toString());
        assertEquals(date, option.getValue());

        // Test parse option with class validator
        pb.reset();
        pb.withPattern("@java.lang.Integer:s:1");
        option = pb.create();
        assertThrows(ValidationException.class, () -> cmdLine.parseArgument(0, option, "abc"));
        Integer value = 123;
        cmdLine.parseArgument(0, option, value.toString());
        assertEquals(value, option.getValue());
    }

    private static class LongValidator extends NumberValidator {
    }
}