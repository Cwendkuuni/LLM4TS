package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PatternBuilderTest {

    private PatternBuilder builder;

    @Before
    public void setUp() {
        builder = new PatternBuilder();
    }

    @Test
    public void testCreateOptionWithClassValidator() {
        builder.withPattern("@:MyClass");
        Option option = builder.create();
        assertEquals("MyClass", option.getLongOpt());
        assertEquals(ClassValidator.class, option.getValidator().getClass());
    }

    @Test
    public void testCreateOptionWithNumberValidator() {
        builder.withPattern("%:number");
        Option option = builder.create();
        assertEquals("number", option.getLongOpt());
        assertEquals(NumberValidator.class, option.getValidator().getClass());
    }

    @Test
    public void testCreateOptionWithDateValidator() {
        builder.withPattern("#:date");
        Option option = builder.create();
        assertEquals("date", option.getLongOpt());
        assertEquals(DateValidator.class, option.getValidator().getClass());
    }

    @Test
    public void testCreateOptionWithFileValidatorExisting() {
        builder.withPattern("<:file");
        Option option = builder.create();
        assertEquals("file", option.getLongOpt());
        assertTrue(option.getValidator() instanceof FileValidator);
        FileValidator fileValidator = (FileValidator) option.getValidator();
        fileValidator.setExisting(true);
    }

    @Test
    public void testCreateOptionWithFileValidatorNonExisting() {
        builder.withPattern("<:nonExistingFile");
        Option option = builder.create();
        assertEquals("nonExistingFile", option.getLongOpt());
        assertTrue(option.getValidator() instanceof FileValidator);
        FileValidator fileValidator = (FileValidator) option.getValidator();
        fileValidator.setExisting(false);
    }

    @Test
    public void testCreateOptionWithUrlValidator() {
        builder.withPattern("/:url");
        Option option = builder.create();
        assertEquals("url", option.getLongOpt());
        assertEquals(UrlValidator.class, option.getValidator().getClass());
    }

    @Test
    public void testCreateOptionWithRequiredArgument() {
        builder.withPattern(":myArg");
        Option option = builder.create();
        assertFalse(option.isRequired());

        builder.reset();
        builder.withPattern("!:myArg");
        option = builder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreateOptionWithMultipleAllowedValues() {
        builder.withPattern("*:value{1,}");
        Option option = builder.create();
        Argument argument = option.getArgument();
        assertEquals(HashSet.class, argument.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidPattern() {
        builder.withPattern("!:");
        builder.create();
    }
}