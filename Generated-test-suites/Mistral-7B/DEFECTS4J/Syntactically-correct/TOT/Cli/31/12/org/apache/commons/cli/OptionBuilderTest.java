package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest
{
    @Test
    public void testSimpleOption()
    {
        Option option = OptionBuilder.withLongOpt("option1")
                .isRequired()
                .create();

        assertEquals("option1", option.getLongOpt());
        assertEquals("", option.getArgName());
        assertEquals(null, option.getValue());
        assertEquals(Option.NO_ARGUMENTS, option.getArgs());
        assertNull(option.getType());
        assertFalse(option.isOptionalArg());
        assertFalse(option.isRequired());

        CommandLine cmd = new DefaultParser().parse(new String[]{"-option1"});
        assertEquals(1, cmd.getOptions().size());
        assertEquals(option, cmd.getOptions().get(0));
    }

    @Test
    public void testOptionalArgumentOption()
    {
        Option option = OptionBuilder.withLongOpt("option2")
                .hasArg()
                .isRequired()
                .create();

        assertEquals("option2", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
        assertEquals(1, option.getArgs());
        assertNull(option.getType());
        assertTrue(option.isOptionalArg());
        assertTrue(option.isRequired());

        CommandLine cmd = new DefaultParser().parse(new String[]{"-option2", "arg1"});
        assertEquals(1, cmd.getOptions().size());
        assertEquals(option, cmd.getOptions().get(0));
        assertEquals("arg1", option.getValue());

        try
        {
            new DefaultParser().parse(new String[]{"-option2"});
        }
        catch (ParseException e)
        {
            assertEquals("The option -option2 requires an argument.", e.getMessage());
        }
    }

    @Test
    public void testValueSeparatorOption()
    {
        Option option = OptionBuilder.withLongOpt("option3")
                .withValueSeparator(':')
                .hasArgs()
                .isRequired()
                .create();

        assertEquals("option3", option.getLongOpt());
        assertEquals("", option.getArgName());
        assertNull(option.getValue());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertNull(option.getType());
        assertTrue(option.isOptionalArg());
        assertTrue(option.isRequired());

        Option parsedOption = new DefaultParser().parse(new String[]{"-option3", "arg1:value1", "arg2:value2"}).getOptions().get(0);
        assertEquals(option, parsedOption);
        assertEquals(new String[]{"arg1=value1", "arg2=value2"}, parsedOption.getValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLongOpt()
    {
        OptionBuilder.create();
    }
}