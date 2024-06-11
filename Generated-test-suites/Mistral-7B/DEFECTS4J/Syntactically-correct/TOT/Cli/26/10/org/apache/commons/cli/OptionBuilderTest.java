package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilder()
    {
        Option option = OptionBuilder.withLongOpt("longOption")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .create('L');

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals(true, option.isRequired());
        Assert.assertEquals(1, option.getArgs().length);

        try
        {
            Option invalidOption = OptionBuilder.create();
            Assert.fail();
        }
        catch(IllegalArgumentException e)
        {
            // expected
        }

        Option withValueSeparatorOption = OptionBuilder.withLongOpt("longOption")
                .hasArg()
                .isRequired()
                .withValueSeparator(':')
                .create('L');

        Assert.assertEquals("longOption", withValueSeparatorOption.getLongOpt());
        Assert.assertEquals("argName", withValueSeparatorOption.getArgName());
        Assert.assertEquals(true, withValueSeparatorOption.isRequired());
        Assert.assertEquals(1, withValueSeparatorOption.getArgs().length);
        Assert.assertEquals(":", withValueSeparatorOption.getValueSeparator());

        try
        {
            Option invalidValueSeparatorOption = OptionBuilder.withLongOpt("longOption")
                    .hasArg()
                    .isRequired()
                    .withValueSeparator('a')
                    .create('L');
            Assert.fail();
        }
        catch(IllegalArgumentException e)
        {
            // expected
        }

        Option hasArgsOption = OptionBuilder.withLongOpt("longOption")
                .hasArgs()
                .create('L');

        Assert.assertEquals("longOption", hasArgsOption.getLongOpt());
        Assert.assertEquals(Option.UNLIMITED_VALUES, hasArgsOption.getArgs().length);
    }
}