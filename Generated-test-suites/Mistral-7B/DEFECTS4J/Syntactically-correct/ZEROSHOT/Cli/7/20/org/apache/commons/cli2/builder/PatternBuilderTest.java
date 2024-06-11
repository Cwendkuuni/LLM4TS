package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreateOptionWithNoPattern() {
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testCreateOptionWithSingleCharacterPattern() {
        patternBuilder.withPattern("a");
        Option option = patternBuilder.create();
        assertEquals("option with single character pattern 'a' created", "a", option.getShortName());
        assertEquals("option with single character pattern 'a' created", OptionBuilder.NO_ARGUMENT, option.getOpt());
        assertFalse("option with single character pattern 'a' created, should not have an argument", option.hasArg());
        assertNull("option with single character pattern 'a' created, should not have an argument builder", option.getArgument());
    }

    @Test
    public void testCreateOptionWithMultipleCharacterPattern() {
        patternBuilder.withPattern("bo");
        Option option = patternBuilder.create();
        assertEquals("option with multiple character pattern 'bo' created", "bo", option.getShortName());
        assertEquals("option with multiple character pattern 'bo' created", OptionBuilder.NO_ARGUMENT, option.getOpt());
        assertFalse("option with multiple character pattern 'bo' created, should not have an argument", option.hasArg());
        assertNull("option with multiple character pattern 'bo' created, should not have an argument builder", option.getArgument());
    }

    @Test
    public void testCreateOptionWithOptionalSingleCharacterPattern() {
        patternBuilder.withPattern("b:");
        Option option = patternBuilder.create();
        assertEquals("option with optional single character pattern 'b:' created", "b", option.getShortName());
        assertEquals("option with optional single character pattern 'b:' created", OptionBuilder.HAS_ARGUMENT, option.getOpt());
        assertTrue("option with optional single character pattern 'b:' created, should have an argument", option.hasArg());
        assertNull("option with optional single character pattern 'b:' created, should not have an argument builder", option.getArgument());
    }

    @Test
    public void testCreateOptionWithOptionalMultiCharacterPattern() {
        patternBuilder.withPattern("bc:");
        Option option = patternBuilder.create();
        assertEquals("option with optional multiple character pattern 'bc:' created", "bc", option.getShortName());
        assertEquals("option with optional multiple character pattern 'bc:' created", OptionBuilder.HAS_ARGUMENT, option.getOpt());
        assertTrue("option with optional multiple character pattern 'bc:' created, should have an argument", option.hasArg());
        assertNull("option with optional multiple character pattern 'bc:' created, should not have an argument builder", option.getArgument());
    }

    @Test
    public void testCreateOptionWithRequiredSingleCharacterPattern() {
        patternBuilder.withPattern("b:");
        patternBuilder.reset();
        patternBuilder.withPattern("b:");
        Option option = patternBuilder.create();
        assertEquals("option with required single character pattern 'b:' created", "b", option.getShortName());
        assertEquals("option with required single character pattern 'b:' created", OptionBuilder.REQUIRED_ARGUMENT, option.getOpt());
        assertTrue("option with required single character pattern 'b:' created, should have an argument", option.hasArg());
        assertNull("option with required single character pattern 'b:' created, should not have an argument builder", option.getArgument());
    }

    @Test
    public void testCreateOptionWithRequiredMultiCharacterPattern() {
        patternBuilder.withPattern("bc:");
        patternBuilder.reset();
        patternBuilder.withPattern("bc:");
        Option option = patternBuilder.create();
        assertEquals("option with required multiple character pattern 'bc:' created", "bc", option.getShortName());
        assertEquals("option with required multiple character pattern 'bc:' created", OptionBuilder.REQUIRED_ARGUMENT, option.getOpt());
        assertTrue("option with required multiple character pattern 'bc:' created, should have an argument", option.hasArg());
        assertNull("option with required multiple character pattern 'bc:' created, should not have an argument builder", option.getArgument());
    }

    @Test
    public void testCreateOptionWithMandatorySingleCharacterPattern() {
        patternBuilder.withPattern("b:");
        patternBuilder.reset();
        patternBuilder.withPattern("b:");
        Option option = patternBuilder.create();
        assertEquals("option with mandatory single character pattern 'b:' created", "b", option.getShortName());
        assertEquals("option with mandatory single character pattern 'b:' created", OptionBuilder.HAS_ARGUMENT, option.getOpt());
        assertTrue("option with mandatory single character pattern 'b:' created, should have an argument", option.hasArg());
        assertNull("option with mandatory single character pattern 'b:' created, should not have an argument builder", option.getArgument());
    }

    @Test
    public void testCreateOptionWithMandatoryMultiCharacterPattern() {
        patternBuilder.withPattern("bc:");
        patternBuilder.reset();
        patternBuilder.withPattern("bc:");
        Option option = patternBuilder.create();
        assertEquals("option with mandatory multiple character pattern 'bc:' created", "bc", option.getShortName());
        assertEquals("option with mandatory multiple character pattern 'bc:' created", OptionBuilder.HAS_ARGUMENT_WITH_DEFAULT_VALUE, option.getOpt());
        assertTrue("option with mandatory multiple character pattern 'bc:' created, should have an argument", option.hasArg());
        assertNull("option with mandatory multiple character pattern 'bc:' created, should not have an argument builder", option.getArgument());
    }

    @Test
    public void testCreateOptionWithClassValidator() {
        patternBuilder.withPattern("@c:");
        Option option = patternBuilder.create();
        assertEquals("option with class validator created", OptionBuilder.HAS_ARGUMENT, option.getOpt());
        assertTrue("option with class validator created, should have an argument", option.hasArg());
        Argument argument = option.getArgument();
        assertNotNull("option with class validator created, argument should not be null", argument);
        Validator validator = argument.getValidator();
        assertNotNull("option with class validator created, validator should not be null", validator);
        assertTrue("option with class validator created, validator instance of ClassValidator", validator instanceof ClassValidator);
    }

    @Test
    public void testCreateOptionWithNumberValidator() {
        patternBuilder.withPattern("%d:");
        Option option = patternBuilder.create();
        assertEquals("option with number validator created", OptionBuilder.HAS_ARGUMENT, option.getOpt());
        assertTrue("option with number validator created, should have an argument", option.hasArg());
        Argument argument = option.getArgument();
        assertNotNull("option with number validator created, argument should not be null", argument);
        Validator validator = argument.getValidator();
        assertNotNull("option with number validator created, validator should not be null", validator);
        assertTrue("option with number validator created, validator instance of NumberValidator", validator instanceof NumberValidator);
    }

    @Test
    public void testCreateOptionWithDateValidator() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        patternBuilder.withPattern("#d:");
        Option option = patternBuilder.create();
        assertEquals("option with date validator created", OptionBuilder.HAS_ARGUMENT, option.getOpt());
        assertTrue("option with date validator created, should have an argument", option.hasArg());
        Argument argument = option.getArgument();
        assertNotNull("option with date validator created, argument should not be null", argument);
        Validator validator = argument.getValidator();
        assertNotNull("option with date validator created, validator should not be null", validator);
        assertTrue("option with date validator created, validator instance of DateValidator", validator instanceof DateValidator);
        LocalDate date = LocalDate.parse("2022-12-31", formatter);
        validator.validate(date.toString());
    }

    @Test
    public void testCreateOptionWithFileValidator() {
        patternBuilder.withPattern("<f:>");
        Option option = patternBuilder.create();
        assertEquals("option with file validator created", OptionBuilder.HAS_ARGUMENT_WITH_DEFAULT_VALUE, option.getOpt());
        assertTrue("option with file validator created, should have an argument", option.hasArg());
        Argument argument = option.getArgument();
        assertNotNull("option with file validator created, argument should not be null", argument);
        Validator validator = argument.getValidator();
        assertNotNull("option with file validator created, validator should not be null", validator);
        assertTrue("option with file validator created, validator instance of FileValidator", validator instanceof FileValidator);
    }

    @Test
    public void testCreateOptionWithUrlValidator() {
        patternBuilder.withPattern("/u:");
        Option option = patternBuilder.create();
        assertEquals("option with url validator created", OptionBuilder.HAS_ARGUMENT, option.getOpt());
        assertTrue("option with url validator created, should have an argument", option.hasArg());
        Argument argument = option.getArgument();
        assertNotNull("option with url validator created, argument should not be null", argument);
        Validator validator = argument.getValidator();
        assertNotNull("option with url validator created, validator should not be null", validator);
        assertTrue("option with url validator created, validator instance of UrlValidator", validator instanceof UrlValidator);
    }

    private void assertEquals(String message, char expected, char actual) {
        assertEquals(message, Character.toString(expected), Character.toString(actual));
    }

    private void assertEquals(String message, String expected, String actual) {
        assertEquals(message, expected, actual);
    }

}