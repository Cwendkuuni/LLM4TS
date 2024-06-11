package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOptOption");
        Option option = optionBuilder.create();
        Assert.assertEquals("longOptOption", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        option = optionBuilder.create('a');
        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertArrayEquals(new int[Option.UNLIMITED_VALUES], option.getArgs());
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        option = optionBuilder.create('o');
        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create();
        Assert.assertArrayEquals(new int[Option.UNLIMITED_VALUES], option.getArgs());
    }

    @Test
    public void testOptionBuilderHasArgsWithSpecifiedNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        Option option = optionBuilder.create();
        Assert.assertArrayEquals(new int[]{Option.UNINITIALIZED, Option.UNINITIALIZED, Option.UNINITIALIZED}, option.getArgs());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test option");
        Option option = optionBuilder.create();
        Assert.assertEquals("test option", option.getDescription());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create('v');
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertEquals(false, option.isRequired());
        optionBuilder = OptionBuilder.isRequired(true);
        option = optionBuilder.create();
        Assert.assertEquals(true, option.isRequired());
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create();
        Assert.assertNull(option.getType());
        option = optionBuilder.create();
        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderCreateWithDefaults() {
        OptionBuilder optionBuilder = OptionBuilder.create();
        Option option = optionBuilder.create();
        Assert.assertNull(option.getLongOpt());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create();
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test
    public void testOptionBuilderCreateWithInvalidLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("")
                .create();
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test
    public void testOptionBuilderCreateWithEmptyDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("");
        Option option = optionBuilder.create();
        Assert.assertNull(option.getDescription());
    }

    @Test
    public void testOptionBuilderCreateWithNullDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription(null);
        Option option = optionBuilder.create();
        Assert.assertNull(option.getDescription());
    }

    @Test
    public void testOptionBuilderCreateWithInvalidArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("");
        Option option = optionBuilder.create();
        Assert.fail("Expected IllegalArgumentException");
    }
}