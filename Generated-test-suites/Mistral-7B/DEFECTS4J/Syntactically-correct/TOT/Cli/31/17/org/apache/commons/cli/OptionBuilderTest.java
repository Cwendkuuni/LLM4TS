package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder withLongOpt = optionBuilder.withLongOpt("longOption");

        Option option = withLongOpt.create();
        assertEquals("longOption", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasArg = optionBuilder.hasArg();

        Option option = hasArg.create();
        assertEquals(1, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArg_boolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasArg = optionBuilder.hasArg(true);

        Option option = hasArg.create();
        assertEquals(1, option.getArgs().length);
        assertTrue(option.isRequired());

        OptionBuilder noArg = optionBuilder.hasArg(false);
        Option noArgOption = noArg.create();
        assertFalse(noArgOption.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder withArgName = optionBuilder.hasArg().withArgName("argName");

        Option option = withArgName.create();
        assertEquals("argName", option.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder isRequired = optionBuilder.isRequired();

        Option option = isRequired.create();
        assertTrue(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder withValueSeparator = optionBuilder.hasArg().withValueSeparator(';');

        Option option = withValueSeparator.create();
        assertEquals(';', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()}.
     */
    @Test
    public void testWithValueSeparator_default()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder withValueSeparator = optionBuilder.hasArg();

        Option option = withValueSeparator.create();
        assertEquals('=', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}.
     */
    @Test
    public void testIsRequired_boolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder isRequired = optionBuilder.isRequired(true);

        Option option = isRequired.create();
        assertTrue(option.isRequired());

        OptionBuilder notRequired = optionBuilder.isRequired(false);
        Option notRequiredOption = notRequired.create();
        assertFalse(notRequiredOption.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasArgs = optionBuilder.hasArgs();

        Option option = hasArgs.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgs_int()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasArgs = optionBuilder.hasArgs(2);

        Option option = hasArgs.create();
        assertEquals(2, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasOptionalArg = optionBuilder.hasOptionalArg();

        Option option = hasOptionalArg.create();
        assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasOptionalArgs = optionBuilder.hasOptionalArgs();

        Option option = hasOptionalArgs.create();
        assertTrue(option.isOptional());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgs_int()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasOptionalArgs = optionBuilder.hasOptionalArgs(2);

        Option option = hasOptionalArgs.create();
        assertTrue(option.isOptional());
        assertEquals(2, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}.
     */
    @Test
    public void testWithType_Class()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder withType = optionBuilder.hasArg().withType(String.class);

        Option option = withType.create();
        assertSame(String.class, option.getType());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder withDescription = optionBuilder.withDescription("description");

        Option option = withDescription.create();
        assertEquals("description", option.getLongOpt());
    }

    /**
     * Test method for {@link Option#getValue(int)}.
     */
    @Test
    public void testGetValue()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder withValue = optionBuilder.hasArg().withValueSeparator(':').isRequired();
        Option option = withValue.create('opt');

        option.setValue("value1:value2");

        String value1 = option.getValue(0);
        assertEquals("value1", value1);
        String value2 = option.getValue(1);
        assertEquals("value2", value2);
    }

    /**
     * Test method for {@link Option#getValue(int)}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValue_NegativeIndex()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder withValue = optionBuilder.hasArg().withValueSeparator(':').isRequired();
        Option option = withValue.create('opt');

        option.setValue("value1:value2");

        option.getValue(-1);
    }

    /**
     * Test method for {@link Option#getValue(int)}.
     */
    @Test
    public void testGetValue_NoValue()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder withValue = optionBuilder.hasArg().isRequired();
        Option option = withValue.create('opt');

        String value = option.getValue(0);
        assertNull(value);
    }

    /**
     * Test method for {@link Option#getValue(int)}.
     */
    @Test
    public void testGetValue_OptionalValue()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder withValue = optionBuilder.hasArg().withValueSeparator(':').isOptional();
        Option option = withValue.create('opt');

        String value = option.getValue(0);
        assertNull(value);
    }
}