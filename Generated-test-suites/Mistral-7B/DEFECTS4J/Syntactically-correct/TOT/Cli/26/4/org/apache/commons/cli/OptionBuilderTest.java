package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for OptionBuilder.
 */
public class OptionBuilderTest
{
    @Test
    public void testOptionWithLongOptArgNameHasArgRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .isRequired()
                .withArgName("argName");
        Option option = optionBuilder.create('l');

        // test with valid argument
        CommandLine cmdLine = new DefaultParser().parse(new String[]{"--longOpt", "argValue"});
        Assert.assertEquals("argValue", option.getValue());

        // test with no argument
        try
        {
            cmdLine = new DefaultParser().parse(new String[]{"--longOpt"});
            Assert.fail();
        }
        catch (ParseException e)
        {
            // expected
        }

        // test with incorrect argument number
        try
        {
            cmdLine = new DefaultParser().parse(new String[]{"--longOpt", "argValue1", "argValue2"});
            Assert.fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testOptionWithLongOptArgNameHasArgOptional()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArg(true)
                .withArgName("argName");
        Option option = optionBuilder.create('l');

        // test with no argument
        CommandLine cmdLine = new DefaultParser().parse(new String[]{"--longOpt"});
        Assert.assertNull(option.getValue());

        // test with valid argument
        cmdLine = new DefaultParser().parse(new String[]{"--longOpt", "argValue"});
        Assert.assertEquals("argValue", option.getValue());

        // test with multiple arguments
        cmdLine = new DefaultParser().parse(new String[]{"--longOpt", "argValue1", "argValue2"});
        Assert.assertEquals("argValue1 argValue2", option.getValue());
    }

    @Test
    public void testOptionWithLongOptArgNameNoArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withArgName("argName");
        Option option = optionBuilder.create('l');

        // test with no argument
        CommandLine cmdLine = new DefaultParser().parse(new String[]{"--longOpt"});
        Assert.assertNull(option.getValue());

        // test with argument
        try
        {
            cmdLine = new DefaultParser().parse(new String[]{"--longOpt", "argValue"});
            Assert.fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testOptionWithLongOptNoArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option option = optionBuilder.create('l');

        // test with no argument
        CommandLine cmdLine = new DefaultParser().parse(new String[]{"--longOpt"});
        Assert.assertNull(option.getValue());

        // test with argument
        try
        {
            cmdLine = new DefaultParser().parse(new String[]{"--longOpt", "argValue"});
            Assert.fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testOptionWithLongOptHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArgs();
        Option option = optionBuilder.create('l');

        // test with multiple arguments
        CommandLine cmdLine = new DefaultParser().parse(new String[]{"--longOpt", "argValue1", "argValue2"});
        Assert.assertEquals(new String[]{"argValue1", "argValue2"}, option.getArgs());
    }

    @Test
    public void testOptionWithLongOptHasArgType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .hasArg(true)
                .withType(Integer.TYPE);
        Option option = optionBuilder.create('l');

        // test with integer value
        CommandLine cmdLine = new DefaultParser().parse(new String[]{"--longOpt", "123"});
        int value = (int) option.getValue();
        Assert.assertEquals(123, value);

        // test with non-integer value
        try
        {
            cmdLine = new DefaultParser().parse(new String[]{"--longOpt", "abc"});
            Assert.fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }
}