package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OptionBuilderTest {

    @Test
    public void testOptionWithLongOptAndDescription() {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                .withDescription("This is a test option")
                .create();

        Assert.assertEquals("mylongopt", option.getLongOpt());
        Assert.assertEquals("This is a test option", option.getDescription());
    }

    @Test
    public void testOptionWithArgName() {
        Option option = OptionBuilder.withArgName("argName")
                .create();

        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionIsRequired() {
        Option option = OptionBuilder.isRequired()
                .create();

        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionHasArg() {
        Option option = OptionBuilder.hasArg()
                .create();

        Assert.assertEquals(Option.VALUE_REQUIRED, option.getArgs());
    }

    @Test
    public void testOptionHasArgs() {
        Option option = OptionBuilder.hasArgs()
                .create();

        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg()
                .create();

        Assert.assertTrue(option.isOptional());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':')
                .create();

        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithoutLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test
    public void testOptionCreationWithAllPropertiesSet() {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                .withDescription("This is a test option")
                .isRequired()
                .hasArg()
                .withValueSeparator(':')
                .create();

        Assert.assertEquals("mylongopt", option.getLongOpt());
        Assert.assertEquals("This is a test option", option.getDescription());
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(Option.VALUE_REQUIRED, option.getArgs());
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionCreationWithValues() {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                .withDescription("This is a test option")
                .isRequired()
                .hasArg("value")
                .create();

        Option parsedOption = Option.builder("mylongopt")
                .longOpt("mylongopt")
                .required(true)
                .argName("value")
                .build();

        Assert.assertEquals(option, parsedOption);
    }

    @Test
    public void testOptionCreationWithValuesAndValuesSeparator() {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                .withDescription("This is a test option")
                .isRequired()
                .hasArg("value")
                .withValueSeparator(':')
                .create();

        Option parsedOption = Option.builder("mylongopt")
                .longOpt("mylongopt")
                .required(true)
                .argName("value")
                .valueSeparator(':')
                .build();

        Assert.assertEquals(option, parsedOption);
    }

    @Test
    public void testOptionCreationWithValuesAndValuesSeparator_multipleValues() {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                .withDescription("This is a test option")
                .isRequired()
                .hasArgs(2)
                .withValueSeparator(':')
                .create();

        Option parsedOption = Option.builder("mylongopt")
                .longOpt("mylongopt")
                .required(true)
                .argName("value")
                .args(Option.UNLIMITED_VALUES)
                .valueSeparator(':')
                .build();

        Assert.assertEquals(option, parsedOption);
    }

    @Test
    public void testOptionCreationWithType() {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                .withDescription("This is a test option")
                .isRequired()
                .hasArg()
                .withType(String.class)
                .create();

        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testOptionCreationWithType_multipleValues() {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                .withDescription("This is a test option")
                .isRequired()
                .hasArgs(2)
                .withType(List.class)
                .create();

        Assert.assertEquals(List.class, option.getType());
    }

    @Test
    public void testOptionCreationWithHasOptionalArgs() {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                .withDescription("This is a test option")
                .isRequired()
                .hasOptionalArgs(2)
                .create();

        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testOptionCreationWithHasOptionalArgs_empty() {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                .withDescription("This is a test option")
                .isRequired()
                .hasOptionalArgs()
                .create();

        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertTrue(option.isOptional());
    }
}