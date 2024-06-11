package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        Option option = optionBuilder.create();
        Assert.assertEquals("longopt", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertTrue(option.hasArg());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create();
        Assert.assertEquals(2, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        String description = "This is a test option";
        OptionBuilder optionBuilder = OptionBuilder.withDescription(description);
        Option option = optionBuilder.create();
        Assert.assertEquals(description, option.getDescription());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithArgName() {
        String argName = "testArg";
        OptionBuilder optionBuilder = OptionBuilder.withArgName(argName);
        Option option = optionBuilder.create();
        Assert.assertEquals(argName, option.getArgName());
    }

    @Test
    public void testOptionBuilderHasArgsWithType() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(1).withType(String.class);
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.TYPE_STRING, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithEmptyLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");
        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithNullLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(null);
        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(-1);
        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(new Object());
        OptionBuilder.create();
    }
}