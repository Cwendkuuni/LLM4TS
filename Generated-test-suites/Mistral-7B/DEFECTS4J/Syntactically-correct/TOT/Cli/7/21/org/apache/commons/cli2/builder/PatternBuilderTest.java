package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the PatternBuilder class
 */
public class PatternBuilderTest {

    @Test
    public void testCreateOption() {
        PatternBuilder pb = new PatternBuilder();
        Option option = pb.create();
        assertNotNull(option);
    }

    @Test
    public void testCreateGroup() {
        PatternBuilder pb = new PatternBuilder();
        Option group = pb.create();
        assertNotNull(group);
        assertTrue(group instanceof GroupBuilder);
    }

    @Test
    public void testCreateOptionWithNoArg() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        assertNull(option.getArgument());
    }

    @Test
    public void testCreateOptionWithArgument() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:s");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        Argument arg = option.getArgument();
        assertNotNull(arg);
        assertNull(arg.getValidator());
    }

    @Test
    public void testCreateOptionWithValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("%d");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        Argument arg = option.getArgument();
        assertNotNull(arg);
        assertNotNull(arg.getValidator());
        assertTrue(arg.getValidator() instanceof NumberValidator);
    }

    @Test
    public void testCreateOptionWithClassValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("@com.example.MyClass");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        Argument arg = option.getArgument();
        assertNull(arg);
        assertTrue(option.getValidator() instanceof ClassValidator);
        ClassValidator cv = (ClassValidator) option.getValidator();
        cv.validate(new Class[] { new MyClass() });
        assertTrue(cv.isValid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:b:");
        Option option = pb.create();
    }

    @Test
    public void testCreateOptionWithFileValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("<file.txt>");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        Argument arg = option.getArgument();
        assertNotNull(arg);
        assertTrue(arg.getValidator() instanceof FileValidator);
        FileValidator fv = (FileValidator) arg.getValidator();
        fv.validate(new File("file.txt"));
        assertTrue(fv.isvalid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidFileValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("<file:");
        Option option = pb.create();
    }

    @Test
    public void testCreateOptionWithDateValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("#yyyy-MM-dd");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        Argument arg = option.getArgument();
        assertNull(arg);
        assertTrue(option.getValidator() instanceof DateValidator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidDateValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("#yyyy-MM-dd/");
        Option option = pb.create();
    }

    @Test
    public void testCreateOptionWithNumberValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("%d");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        Argument arg = option.getArgument();
        assertNotNull(arg);
        assertTrue(arg.getValidator() instanceof NumberValidator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidNumberValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("%string");
        Option option = pb.create();
    }

    @Test
    public void testCreateOptionWithUrlValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("/url");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        Argument arg = option.getArgument();
        assertNull(arg);
        assertTrue(option.getValidator() instanceof UrlValidator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidUrlValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("/url/");
        Option option = pb.create();
    }
}