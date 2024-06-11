package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithBasicUsage()
    {
        Option option = OptionBuilder.withLongOpt("optionA")
                .isRequired()
                .create();

        Assert.assertEquals("optionA", option.getLongOpt());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithOptionalArg()
    {
        Option option = OptionBuilder.withLongOpt("optionB")
                .hasArg()
                .create();

        Assert.assertEquals("optionB", option.getLongOpt());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("optionC")
                .hasArgs(3)
                .withValueSeparator(',')
                .create();

        Assert.assertEquals("optionC", option.getLongOpt());
        Assert.assertEquals(3, option.getArgs().length);
        Assert.assertEquals(',', option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidUsage()
    {
        OptionBuilder.withLongOpt("optionD")
                .withArgName("name")
                .hasArg()
                .isRequired()
                .create();

        // The test should fail with an IllegalArgumentException because
        // OptionBuilder instance is reset in create() method and we are
        // trying to create two different options using the same instance.
    }

    @Test
    public void testOptionBuilderWithCustomArgName()
    {
        Option option = OptionBuilder.withLongOpt("optionE")
                .hasArg()
                .withArgName("path")
                .create();

        Assert.assertEquals("optionE", option.getLongOpt());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals("path", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithCustomType()
    {
        Option option = OptionBuilder.withLongOpt("optionF")
                .hasArg()
                .withType(String.class)
                .create();

        Assert.assertEquals("optionF", option.getLongOpt());
        Assert.assertEquals(Option.VALUE_TYPE_STRING, option.getType());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithUnlimitedArgs()
    {
        Option option = OptionBuilder.withLongOpt("optionG")
                .hasOptionalArgs()
                .create();

        String[] args = {"arg1", "arg2", "arg3"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(Arrays.asList(new String[]{"MyProgram", "-" + option.getLongOpt(), "arg1", "arg2", "arg3"}));

        Option parsedOption = cmd.getOption(option.getLongOpt());
        Assert.assertEquals(args.length, parsedOption.getArgs().length);
        Assert.assertArrayEquals(args, parsedOption.getArgs());
    }
}