package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.cli.Option.*;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option1");
        Option option = optionBuilder.create();
        Assert.assertEquals("option1", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isOptionalArg());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create();
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderHasArgsWithValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create();
        Assert.assertEquals(2, option.getArgs().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithMissingLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.create();
    }
}