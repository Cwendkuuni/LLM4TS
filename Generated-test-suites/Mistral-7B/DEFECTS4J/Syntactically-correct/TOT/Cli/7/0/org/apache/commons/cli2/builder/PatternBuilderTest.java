package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testCreateOptionWithClassValidator() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("@[java.lang.String]");
        final Option option = builder.create();

        Assert.assertEquals("option does not have expected validator", ClassValidator.class, option.getValidator());
    }

    @Test
    public void testCreateOptionWithNumberValidator() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("%d");
        final Option option = builder.create();

        Assert.assertEquals("option does not have expected validator", NumberValidator.class, option.getValidator());
    }

    @Test
    public void testCreateOptionWithDateValidator() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("#{yyyy-MM-dd}");
        final Option option = builder.create();

        Assert.assertEquals("option does not have expected validator", DateValidator.class, option.getValidator());
    }

    @Test
    public void testCreateOptionWithFileValidatorAndExistingFile() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("<file>");
        final Option option = builder.create();

        Assert.assertEquals("option does not have expected validator", FileValidator.class, option.getValidator());
        ((FileValidator)option.getValidator()).assertValid("path/to/existing/file.txt");
    }

    @Test
    public void testCreateOptionWithFileValidatorAndNonExistingFile() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("<file>");
        final Option option = builder.create();

        Assert.assertEquals("option does not have expected validator", FileValidator.class, option.getValidator());
        try {
            ((FileValidator)option.getValidator()).assertValid("path/to/non-existing/file.txt");
            Assert.fail("expected an IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateOptionWithUrlValidator() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("/URL");
        final Option option = builder.create();

        Assert.assertEquals("option does not have expected validator", UrlValidator.class, option.getValidator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidPattern() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("!");
        builder.create();
    }

    @Test
    public void testCreateOptionWithRequiredAndNoArgument() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("o");
        final Option option = builder.create();

        Assert.assertTrue("option should be required", option.isRequired());
        Assert.assertNull("option should have no argument", option.getArgument());
    }

    @Test
    public void testCreateOptionWithRequiredAndArgument() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("o:s");
        final Option option = builder.create();

        Assert.assertTrue("option should be required", option.isRequired());
        Assert.assertNotNull("option should have an argument", option.getArgument());
    }

    @Test
    public void testCreateOptionWithOptionalAndArgument() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("o:s");
        builder.reset();
        builder.withPattern("o:s{1,}");
        final Option option = builder.create();

        Assert.assertFalse("option should not be required", option.isRequired());
        Assert.assertNotNull("option should have an argument", option.getArgument());
    }

    @Test
    public void testCreateOptionWithOptionalAndNoArgument() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("o:s{0,}");
        final Option option = builder.create();

        Assert.assertFalse("option should not be required", option.isRequired());
        Assert.assertNull("option should have no argument", option.getArgument());
    }

    @Test
    public void testCreateOptionWithMandatoryArgumentAndOneInstance() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("o:s{1}");
        final Option option = builder.create();

        Assert.assertFalse("option should not be required", option.isRequired());
        Assert.assertNotNull("option should have an argument", option.getArgument());
        Assert.assertEquals("argument should have minimum one instance", 1, option.getArgument().getMinimum());
        Assert.assertEquals("argument should have maximum one instance", 1, option.getArgument().getMaximum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithMandatoryArgumentAndMoreThanOneInstance() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("o:s{2,}");
        builder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithOptionalArgumentAndNoInstance() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("o:s{0,}");
        builder.create();
    }

    @Test
    public void testCreateOptionWithOptionalArgumentAndZeroOrMoreInstances() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("o:s{0,}");
        final Option option = builder.create();

        Assert.assertFalse("option should not be required", option.isRequired());
        Assert.assertNotNull("option should have an argument", option.getArgument());
        Assert.assertEquals("argument should have minimum zero instances", 0, option.getArgument().getMinimum());
        Assert.assertEquals("argument should have maximum unlimited instances", Integer.MAX_VALUE, option.getArgument().getMaximum());
    }

    @Test
    public void testCreateOptionWithOptionalArgumentAndOneOrMoreInstances() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("o:s{1,}");
        final Option option = builder.create();

        Assert.assertFalse("option should not be required", option.isRequired());
        Assert.assertNotNull("option should have an argument", option.getArgument());
        Assert.assertEquals("argument should have minimum one instance", 1, option.getArgument().getMinimum());
        Assert.assertEquals("argument should have maximum unlimited instances", Integer.MAX_VALUE, option.getArgument().getMaximum());
    }
}