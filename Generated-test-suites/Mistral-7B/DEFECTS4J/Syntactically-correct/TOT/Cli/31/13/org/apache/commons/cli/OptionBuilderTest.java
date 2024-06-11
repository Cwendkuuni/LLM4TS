package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest
{
    @Test
    public void testSimpleOption()
    {
        Option option = OptionBuilder.hasArg()
                                     .withLongOpt("myOption")
                                     .withDescription("A description for myOption")
                                     .create('m');

        assertEquals("myOption", option.getLongOpt());
        assertEquals("A description for myOption", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());

        CommandLine cmd = new DefaultParser().parse(new String[]{"-m", "value"});
        assertEquals("value", option.getValue(cmd));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLongOpt()
    {
        OptionBuilder.create();
    }

    @Test
    public void testOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                                     .withLongOpt("myOption")
                                     .withDescription("A description for myOption")
                                     .create('m');

        assertEquals("myOption", option.getLongOpt());
        assertEquals("A description for myOption", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getType());
        assertTrue(option.hasOptionalArg());

        CommandLine cmd = new DefaultParser().parse(new String[]{"-m", "value1", "value2"});
        assertEquals("value1", option.getValue(cmd, 0));
        assertEquals("value2", option.getValue(cmd, 1));
    }

    @Test
    public void testRequiredArg()
    {
        Option option = OptionBuilder.hasArg()
                                     .withLongOpt("myOption")
                                     .withDescription("A description for myOption")
                                     .isRequired()
                                     .create('m');

        assertEquals("myOption", option.getLongOpt());
        assertEquals("A description for myOption", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals(1, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());

        CommandLine cmd = new DefaultParser().parse(new String[]{"-m", "value"});
        assertEquals("value", option.getValue(cmd));

        try
        {
            new DefaultParser().parse(new String[]{"-m"});
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(':')
                                     .withLongOpt("myOption")
                                     .withDescription("A description for myOption")
                                     .create('m');

        assertEquals("myOption", option.getLongOpt());
        assertEquals("A description for myOption", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals(1, option.getNumberOfArgs());
        assertNull(option.getType());
        assertTrue(option.hasOptionalArg());

        CommandLine cmd = new DefaultParser().parse(new String[]{"-m", "value1:value2"});
        assertEquals("value1:value2", option.getValue(cmd));

        try
        {
            new DefaultParser().parse(new String[]{"-m", "value1"});
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testType()
    {
        Option option = OptionBuilder.withType(Integer.TYPE)
                                     .withLongOpt("myOption")
                                     .withDescription("A description for myOption")
                                     .create('m');

        assertEquals("myOption", option.getLongOpt());
        assertEquals("A description for myOption", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(Integer.TYPE, option.getType());
        assertFalse(option.hasOptionalArg());

        CommandLine cmd = new DefaultParser().parse(new String[]{"-m", "1"});
        assertEquals(1, ((Number) option.getValue(cmd)).intValue());

        try
        {
            new DefaultParser().parse(new String[]{"-m", "abc"});
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testMultipleArgs()
    {
        Option option = OptionBuilder.hasArgs()
                                     .withLongOpt("myOption")
                                     .withDescription("A description for myOption")
                                     .create('m');

        assertEquals("myOption", option.getLongOpt());
        assertEquals("A description for myOption", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());

        CommandLine cmd = new DefaultParser().parse(new String[]{"-m", "value1", "value2"});
        assertEquals(new String[]{"value1", "value2"}, option.getValues(cmd));

        try
        {
            new DefaultParser().parse(new String[]{"-m"});
            fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testMultipleOptionalArgs()
    {
        Option option = OptionBuilder.hasOptionalArgs()
                                     .withLongOpt("myOption")
                                     .withDescription("A description for myOption")
                                     .create('m');

        assertEquals("myOption", option.getLongOpt());
        assertEquals("A description for myOption", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
        assertNull(option.getType());
        assertTrue(option.hasOptionalArg());

        CommandLine cmd = new DefaultParser().parse(new String[]{"-m", "value1", "value2"});
        assertEquals(new String[]{"value1", "value2"}, option.getValues(cmd));

        CommandLine cmd2 = new DefaultParser().parse(new String[]{"-m", "value1"});
        assertEquals(new String[]{"value1"}, option.getValues(cmd2));

        CommandLine cmd3 = new DefaultParser().parse(new String[]{"-m"});
        assertTrue(cmd3.getOptions().isEmpty());
    }
}