package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the PatternBuilder class
 */
public class PatternBuilderTest {

    @Test
    public void testCreateOptionWithClassValidator() {
        final PatternBuilder pb = new PatternBuilder();
        pb.withPattern("@:MyClass");
        final Option option = pb.create();

        assertEquals("MyClass", option.getLongOpt());
        assertEquals("Specifies the class to validate", option.getDescription());
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof ClassValidator);
    }

    @Test
    public void testCreateOptionWithNumberValidator() {
        final PatternBuilder pb = new PatternBuilder();
        pb.withPattern("%:int,--num");
        final Option option = pb.create();

        assertEquals("-num", option.getShortName());
        assertEquals("num", option.getLongOpt());
        assertEquals("Accepts an integer value", option.getDescription());
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof NumberValidator);
    }

    @Test
    public void testCreateOptionWithNoValidator() {
        final PatternBuilder pb = new PatternBuilder();
        pb.withPattern("/:url,--link");
        final Option option = pb.create();

        assertEquals("-link", option.getShortName());
        assertEquals("url", option.getLongOpt());
        assertEquals("Accepts a URL value", option.getDescription());
        assertNull(option.getValidator());
    }

    @Test
    public void testCreateMultipleOptions() {
        final PatternBuilder pb = new PatternBuilder();
        pb.withPattern("%:int,--num1 %:int,--num2");
        final Option option1 = pb.create();
        final Option option2 = pb.create();

        assertEquals("num1", option1.getLongOpt());
        assertEquals("Accepts an integer value", option1.getDescription());
        assertNotNull(option1.getValidator());
        assertTrue(option1.getValidator() instanceof NumberValidator);

        assertEquals("num2", option2.getLongOpt());
        assertEquals("Accepts an integer value", option2.getDescription());
        assertNotNull(option2.getValidator());
        assertTrue(option2.getValidator() instanceof NumberValidator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        final PatternBuilder pb = new PatternBuilder();
        pb.withPattern("@:MyClass%:int");
        pb.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingType() {
        final PatternBuilder pb = new PatternBuilder();
        pb.withPattern(":MyClass");
        pb.create();
    }
}