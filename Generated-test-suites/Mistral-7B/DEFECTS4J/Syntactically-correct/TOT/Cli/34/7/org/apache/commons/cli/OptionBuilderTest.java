package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for OptionBuilder
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}
     */
    @Test
    public void testWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                     .create();

        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}
     */
    @Test
    public void testHasArg()
    {
        Option option = OptionBuilder.hasArg()
                                      .create();

        Assert.assertEquals(1, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}
     */
    @Test
    public void testHasOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                                      .create();

        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertTrue(option.isOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}
     */
    @Test
    public void testIsRequired()
    {
        Option option = OptionBuilder.isRequired()
                                      .create();

        Assert.assertTrue(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}
     */
    @Test
    public void testWithValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(',')
                                      .create();

        Assert.assertEquals(',', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}
     */
    @Test
    public void testHasArgs()
    {
        Option option = OptionBuilder.hasArgs()
                                      .create();

        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}
     */
    @Test
    public void testHasArgsInt()
    {
        Option option = OptionBuilder.hasArgs(2)
                                      .create();

        Assert.assertEquals(2, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}
     */
    @Test
    public void testWithType()
    {
        Option option = OptionBuilder.withType(String.class)
                                      .create();

        Assert.assertNotNull(option.getType());
        Assert.assertEquals(String.class, option.getType());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}
     */
    @Test
    public void testWithDescription()
    {
        Option option = OptionBuilder.withDescription("description")
                                      .create();

        Assert.assertEquals("description", option.getDescription());
    }

    /**
     * Test method for {@link Option#getValue(int)}
     */
    @Test
    public void testGetValue()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                     .hasArg()
                                     .create();

        String[] values = {"value1", "value2"};
        CommandLine cmd = new DefaultParser().parse(Arrays.asList("--longOpt", "value1", "value2").toArray(new String[0]));

        Assert.assertEquals("value1", option.getValue(cmd, 0));
        Assert.assertEquals("value2", option.getValue(cmd, 1));
    }
}