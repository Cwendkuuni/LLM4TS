package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert.*;
import org.junit.Test;

/**
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest
{
    @Test
    public void testSimpleOption()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("optionA")
                .isRequired()
                .hasArg();
        Option option = ob.create('A');

        CommandLine cmdLine = new DefaultParser().parse(new String[]{"-A", "valueA"});
        Assert.assertEquals("valueA", option.getValue(0));

        try
        {
            cmdLine = new DefaultParser().parse(new String[]{"-A"});
            fail("Option 'A' is required");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            cmdLine = new DefaultParser().parse(new String[]{"-B", "valueA"});
            fail("Option 'B' does not exist");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testOptionalArgOption()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("optionB")
                .isRequired()
                .hasOptionalArg();
        Option option = ob.create('B');

        CommandLine cmdLine1 = new DefaultParser().parse(new String[]{"-B", "valueA"});
        Assert.assertEquals("valueA", option.getValue(0));

        CommandLine cmdLine2 = new DefaultParser().parse(new String[]{"-B"});
        Assert.assertNull(option.getValue(0));
    }

    @Test
    public void testMultipleArgsOption()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("optionC")
                .isRequired()
                .hasArgs();
        Option option = ob.create('C');

        CommandLine cmdLine = new DefaultParser().parse(new String[]{"-C", "valueA", "valueB"});
        Assert.assertEquals("valueA", option.getValue(0));
        Assert.assertEquals("valueB", option.getValue(1));

        try
        {
            cmdLine = new DefaultParser().parse(new String[]{"-C", "valueA"});
            fail("Option 'C' requires two arguments");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testValueSeparatorOption()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("optionD")
                .isRequired()
                .hasArgs()
                .withValueSeparator(':');
        Option option = ob.create('D');

        CommandLine cmdLine = new DefaultParser().parse(new String[]{"-D", "valueA:valueB"});
        Assert.assertEquals("valueA:valueB", option.getValue(0));

        try
        {
            cmdLine = new DefaultParser().parse(new String[]{"-D", "valueA"});
            fail("Option 'D' requires a value with a separator");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testTypeSpecifiedOption()
    {
        OptionBuilder ob = OptionBuilder.withLongOpt("optionE")
                .isRequired()
                .hasArg()
                .withType(int.class);
        Option option = ob.create('E');

        CommandLine cmdLine = new DefaultParser().parse(new String[]{"-E", "123"});
        int value = (int) option.getValue(0);
        Assert.assertEquals(123, value);

        try
        {
            cmdLine = new DefaultParser().parse(new String[]{"-E", "strValue"});
            fail("Option 'E' expects an integer value");
        }
        catch (ParseException e)
        {
            // expected
        }
    }
}