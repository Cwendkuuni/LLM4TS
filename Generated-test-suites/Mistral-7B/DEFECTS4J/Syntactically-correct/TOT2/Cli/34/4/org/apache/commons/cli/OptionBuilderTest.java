package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create();

        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();

        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testHasArgWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Option option = optionBuilder.create();

        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();

        Assert.assertFalse(option.isOptional());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();

        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testHasArgsWithNumArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create();

        Assert.assertEquals(2, option.getArgs().length);
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();

        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create();

        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option option = optionBuilder.create();

        Assert.assertEquals(3, option.getArgs().length);
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testWithType()
    {
        Class<String> stringClass = String.class;
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create();

        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testWithDescription()
    {
        String description = "Description";
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Description");
        Option option = optionBuilder.create();

        Assert.assertEquals("Description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withLongOpt(" ");
        optionBuilder.create();
    }

    @Test
    public void testCreateWithArgs()
    {
        String[] args = {"arg1", "arg2"};
        OptionBuilder optionBuilder = OptionBuilder.hasArgs().withArgName("argName");
        Option option = optionBuilder.create();

        Assert.assertEquals(2, option.getArgs().length);
        Assert.assertArrayEquals(args, option.getArgs());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testCreateWithOptionalArgs()
    {
        String[] args = {"arg1", "arg2", "arg3"};
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs().withArgName("argName");
        Option option = optionBuilder.create();

        Assert.assertEquals(Arrays.asList("arg1", "arg2", "arg3"), option.getArgsList());
        Assert.assertFalse(option.isRequired());
    }
}