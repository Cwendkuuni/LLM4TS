package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilder() {
        OptionOptionBuilder = OptionBuilder.withLongOpt("longOption")
                .withDescription("A long option test")
                .isRequired()
                .hasArg()
                .withArgName("argName")
                .hasOptionalArg()
                .withValueSeparator('-')
                .withType(String.class);
        Option option = OptionOptionBuilder.create('l');

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals("A long option test", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(1, option.getArgPosition().length);
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals('-', option.getValueSeparator());
        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionOptionBuilder = OptionBuilder.withLongOpt("longOption")
                .withDescription("A long option test")
                .hasArg();
        Option option = OptionOptionBuilder.create('l');

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals("A long option test", option.getDescription());
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasArgRequired() {
        OptionOptionBuilder = OptionBuilder.withLongOpt("longOption")
                .withDescription("A long option test")
                .isRequired()
                .hasArg();
        Option option = OptionOptionBuilder.create('l');

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals("A long option test", option.getDescription());
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionOptionBuilder = OptionBuilder.withLongOpt("longOption")
                .withDescription("A long option test")
                .hasArgs();
        Option option = OptionOptionBuilder.create('l');

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals("A long option test", option.getDescription());
        Assert.assertArrayEquals(new int[Option.UNLIMITED_VALUES], option.getArgs());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionOptionBuilder = OptionBuilder.withLongOpt("longOption")
                .withDescription("A long option test")
                .hasOptionalArg();
        Option option = OptionOptionBuilder.create('l');

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals("A long option test", option.getDescription());
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionBuilderHasOptionalArgs() {
        OptionOptionBuilder = OptionBuilder.withLongOpt("longOption")
                .withDescription("A long option test")
                .hasOptionalArgs();
        Option option = OptionOptionBuilder.create('l');

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals("A long option test", option.getDescription());
        Assert.assertArrayEquals(new int[Option.UNLIMITED_VALUES], option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionBuilderHasOptionalArgsWithValues() {
        OptionOptionBuilder = OptionBuilder.withLongOpt("longOption")
                .withDescription("A long option test")
                .hasOptionalArgs(2);
        Option option = OptionOptionBuilder.create('l');

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals("A long option test", option.getDescription());
        Assert.assertArrayEquals(new int[]{2}, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionOptionBuilder = OptionBuilder.withLongOpt("longOption")
                .withDescription("A long option test")
                .withType(Integer.class);
        Option option = OptionOptionBuilder.create('l');

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals("A long option test", option.getDescription());
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionOptionBuilder = OptionBuilder.withLongOpt("longOption")
                .withDescription("A long option test")
                .withValueSeparator(':');
        Option option = OptionOptionBuilder.create('l');

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals("A long option test", option.getDescription());
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionOptionBuilder = OptionBuilder.withLongOpt("longOption")
                .withDescription("A long option test")
                .withArgName("argName");
        Option option = OptionOptionBuilder.create('l');

        Assert.assertEquals("longOption", option.getLongOpt());
        Assert.assertEquals("A long option test", option.getDescription());
        Assert.assertEquals("argName", option.getArgName());
    }
}