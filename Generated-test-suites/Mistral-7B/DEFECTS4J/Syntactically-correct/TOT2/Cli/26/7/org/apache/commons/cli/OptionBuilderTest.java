package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)} method.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myLongOpt");
        Option option = optionBuilder.create('m');
        Assert.assertEquals("myLongOpt", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()} method.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('a');
        Assert.assertEquals(1, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)} method.
     */
    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        Option option = optionBuilder.create('b');
        Assert.assertEquals(1, option.getArgs().length);
        OptionBuilder.reset();

        optionBuilder = OptionBuilder.hasArg(false);
        option = optionBuilder.create('c');
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)} method.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option option = optionBuilder.create('x');
        Assert.assertEquals("argName", option.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()} method.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('r');
        Assert.assertTrue(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)} method.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create('v');
        Assert.assertEquals(':', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()} method.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create('s');
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)} method.
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create('t');
        Assert.assertEquals(2, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()} method.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('o');
        Assert.assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()} method.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create('p');
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        Assert.assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)} method.
     */
    @Test
    public void testHasOptionalArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option option = optionBuilder.create('q');
        Assert.assertEquals(3, option.getArgs().length);
        Assert.assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)} method.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create('u');
        Assert.assertEquals(String.class, option.getType());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)} method.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("My Option Description");
        Option option = optionBuilder.create('d');
        Assert.assertEquals("My Option Description", option.getDescription());
    }

    /**
     * Test method for {@link Option#getValue(int)} method.
     */
    @Test
    public void testGetValue()
    {
        String[] args = {"--myLongOpt", "value1", "value2"};
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .withArgName("arg")
                .hasArgs()
                .create('m');
        CommandLineParser parser = new BasicParser();
        CommandLine commandLine = parser.parse(args);
        String[] values = option.getValue(0);
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    /**
     * Test method for {@link Option#hasArg()} property.
     */
    @Test
    public void testHasArgProperty()
    {
        Option option = OptionBuilder.hasArg().create('e');
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertTrue(option.hasArg());
    }

    /**
     * Test method for {@link Option#isRequired()} property.
     */
    @Test
    public void testIsRequiredProperty()
    {
        Option option = OptionBuilder.isRequired().create('r');
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals("Required Option", option.getDescription());
    }

    /**
     * Test method for {@link Option#isOptional()} property.
     */
    @Test
    public void testIsOptionalProperty()
    {
        Option option = OptionBuilder.create('o');
        Assert.assertFalse(option.isOptional());
        Assert.assertEquals("", option.getDescription());

        option = OptionBuilder.hasOptionalArg().create('p');
        Assert.assertTrue(option.isOptional());
        Assert.assertEquals("", option.getDescription());
    }

    /**
     * Test method for {@link Option#getArgs()} property.
     */
    @Test
    public void testGetArgsProperty()
    {
        Option option = OptionBuilder.create('g');
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);

        option = OptionBuilder.hasArgs(2).create('h');
        Assert.assertEquals(2, option.getArgs().length);
    }

    /**
     * Test method for {@link Option#getType()} property.
     */
    @Test
    public void testgetTypeProperty()
    {
        Option option = OptionBuilder.create('i');
        Assert.assertNull(option.getType());

        option = OptionBuilder.withType(Integer.class).create('j');
        Assert.assertEquals(Integer.class, option.getType());
    }

    /**
     * Test method for {@link Option#getValueSeparator()} property.
     */
    @Test
    public void testgetValueSeparatorProperty()
    {
        Option option = OptionBuilder.create('k');
        Assert.assertEquals(Option.UNDEFINED_VALUE_SEPARATOR, option.getValueSeparator());

        option = OptionBuilder.withValueSeparator(',').create('l');
        Assert.assertEquals(',', option.getValueSeparator());
    }

    /**
     * Test method for {@link Option#getArgName()} property.
     */
    @Test
    public void testGetArgNameProperty()
    {
        Option option = OptionBuilder.create('m');
        Assert.assertEquals("arg", option.getArgName());

        option = OptionBuilder.withArgName("argName").create('n');
        Assert.assertEquals("argName", option.getArgName());
    }
}