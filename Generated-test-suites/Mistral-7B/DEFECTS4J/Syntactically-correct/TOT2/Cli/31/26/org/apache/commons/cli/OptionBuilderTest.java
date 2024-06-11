package org.junit.tests;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        Option option = optionBuilder.create();
        Assert.assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test
    public void testWithLongOptNull() {
        Assert.assertThrows(IllegalArgumentException.class, () -> OptionBuilder.withLongOpt(null));
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        Assert.assertTrue(option.getOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option option = optionBuilder.create();
        Assert.assertEquals(3, option.getArgs().length);
    }

    @Test
    public void testCreateOptionWithAllPropertiesSet() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt")
                .hasArg()
                .isRequired()
                .withValueSeparator(':')
                .hasOptionalArg()
                .withType(String.class)
                .withDescription("Test option description");
        Option option = optionBuilder.create();
        Assert.assertEquals("testLongOpt", option.getLongOpt());
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertTrue(option.getOptionalArg());
        Assert.assertEquals(String.class, option.getType());
        Assert.assertEquals("Test option description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithoutAnyBuilderSet() {
        Option option = OptionBuilder.create();
        // This assertion will throw an exception if no builder has been set
        Assert.fail("An exception should have been thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateRequiredOptionWithoutArgName() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        // This assertion will throw an exception because argName is null
        Assert.assertNotNull(option.getArgName());
    }

    @Test
    public void testCreateOptionWithSameLongOptMultipleTimes() {
        OptionBuilder optionBuilder1 = OptionBuilder.withLongOpt("testLongOpt");
        Option option1 = optionBuilder1.create();
        OptionBuilder optionBuilder2 = OptionBuilder.withLongOpt("testLongOpt");
        Option option2 = optionBuilder2.create();
        Assert.assertEquals(option1, option2);
    }
}