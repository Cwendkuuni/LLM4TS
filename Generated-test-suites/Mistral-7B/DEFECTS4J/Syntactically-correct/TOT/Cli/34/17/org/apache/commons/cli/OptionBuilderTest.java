package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("foo")
                .hasArg()
                .isRequired()
                .create('f');

        assertTrue(option.getLongOpt().equals("foo"));
        assertTrue(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getValue());

        try
        {
            option.parse("--foo");
            fail("Expected an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        try
        {
            option.parse("--foo value");
            fail("Expected an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        try
        {
            OptionBuilder.reset();
            option = OptionBuilder.withLongOpt("bar")
                    .hasArg()
                    .isRequired(false)
                    .create('b');

            Option validOption = new Option('b', "bar", false, "value", "A boolean option");
            validOption.setArgName("value");
            validOption.setOptionalArg(true);

            option.parse("--bar value");
            assertEquals(validOption, option);
        }
        finally
        {
            OptionBuilder.reset();
        }
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("foo")
                .hasArg()
                .withValueSeparator('=')
                .create('f');

        assertTrue(option.getLongOpt().equals("foo"));
        assertTrue(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getValue());

        try
        {
            option.parse("--foo");
            fail("Expected an IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        try
        {
            option.parse("--foo=value");
            Option expectedOption = new Option('f', "foo", true, "value", "An option with a value");
            expectedOption.setArgName("value");
            expectedOption.setValueSeparator('=');

            assertEquals(expectedOption, option);
        }
        finally
        {
            OptionBuilder.reset();
        }
    }

    @Test
    public void testOptionBuilderWithValueSeparatorAndArgName()
    {
        Option option = OptionBuilder.withLongOpt("foo")
                .hasArg()
                .withValueSeparator('=')
                .withArgName("bar")
                .create('f');

        assertTrue(option.getLongOpt().equals("foo"));
        assertTrue(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getValue());

        try
        {
            option.parse("--foo=bar");
            Option expectedOption = new Option('f', "foo", true, "bar", "An option with a value named bar");
            expectedOption.setArgName("bar");
            expectedOption.setValueSeparator('=');

            assertEquals(expectedOption, option);
        }
        finally
        {
            OptionBuilder.reset();
        }

        Option option2 = OptionBuilder.withLongOpt("foo")
                .hasArgs()
                .withValueSeparator(':')
                .create('f');

        assertTrue(option2.getLongOpt().equals("foo"));
        assertFalse(option2.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option2.getArgs());
        assertNull(option2.getValue());

        try
        {
            option2.parse("--foo value1 value2");
            Option expectedOption = new Option('f', "foo", false, null, "An option with multiple values");
            expectedOption.setValueSeparator(':');

            assertEquals(expectedOption, option2);
        }
        finally
        {
            OptionBuilder.reset();
        }
    }

    @Test
    public void testOptionBuilderWithType()
    {
        Option option = OptionBuilder.withLongOpt("foo")
                .hasArg()
                .withType(String.class)
                .create('f');

        assertTrue(option.getLongOpt().equals("foo"));
        assertTrue(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getValue());

        try
        {
            option.parse("--foo value");
            Option expectedOption = new Option('f', "foo", true, "value", "An option with a String value");
            expectedOption.setValueSeparator('=');
            expectedOption.setType(String.class);

            assertEquals(expectedOption, option);
        }
        finally
        {
            OptionBuilder.reset();
        }
    }
}