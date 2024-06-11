package org.apache.commons.cli.test;

import org.apache.commons.cli. Option;
import org.apache.commons.cli. OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myLongOpt");
        Option expectedOption = Option.builder()
                .longOpt("myLongOpt")
                .build();

        Option option = optionBuilder.create();
        assertEquals(expectedOption.getLongOpt(), option.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = Option.builder()
                .numberOfArgs(1)
                .build();

        Option option = optionBuilder.create();
        assertEquals(expectedOption.getNumberOfArgs(), option.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option expectedOption = Option.builder()
                .argName("argName")
                .numberOfArgs(1)
                .build();

        Option option = optionBuilder.create();
        assertEquals(expectedOption.getArgName(), option.getArgName());
        assertEquals(expectedOption.getNumberOfArgs(), option.getNumberOfArgs());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = Option.builder()
                .required(true)
                .build();

        Option option = optionBuilder.create();
        assertEquals(expectedOption.isRequired(), option.isRequired());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = Option.builder()
                .numberOfArgs(1)
                .optionalArg(true)
                .build();

        Option option = optionBuilder.create();
        assertEquals(expectedOption.isOptionalArg(), option.isOptionalArg());
        assertEquals(expectedOption.getNumberOfArgs(), option.getNumberOfArgs());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = Option.builder()
                .numberOfArgs(Option.UNLIMITED_VALUES)
                .optionalArg(true)
                .build();

        Option option = optionBuilder.create();
        assertEquals(expectedOption.getNumberOfArgs(), option.getNumberOfArgs());
        assertEquals(expectedOption.isOptionalArg(), option.isOptionalArg());
    }

    @Test
    public void testHasArgumentsWithName()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs("argName");
        Option expectedOption = Option.builder()
                .argName("argName")
                .numberOfArgs(Option.UNLIMITED_VALUES)
                .optionalArg(true)
                .build();

        Option option = optionBuilder.create();
        assertEquals(expectedOption.getArgName(), option.getArgName());
        assertEquals(expectedOption.getNumberOfArgs(), option.getNumberOfArgs());
        assertEquals(expectedOption.isOptionalArg(), option.isOptionalArg());
    }

    @Test
    public void testHasArgsWithNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option expectedOption = Option.builder()
                .numberOfArgs(2)
                .build();

        Option option = optionBuilder.create();
        assertEquals(expectedOption.getNumberOfArgs(), option.getNumberOfArgs());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = Option.builder()
                .valueSeparator(':')
                .build();

        Option option = optionBuilder.create();
        assertEquals(expectedOption.getValueSeparator(), option.getValueSeparator());
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option expectedOption = Option.builder()
                .type(String.class)
                .build();

        Option option = optionBuilder.create();
        assertEquals(expectedOption.getType(), option.getType());
    }

    @Test
    public void testCreate()
    {
        Option option = OptionBuilder.create("myOption");
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("My Option Description");
        Option expectedOption = Option.builder()
                .description("My Option Description")
                .build();

        Option option = optionBuilder.create("myOption");
        assertEquals(expectedOption.getDescription(), option.getDescription());
    }

    @Test
    public void testCreateWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myLongOpt");
        Option expectedOption = Option.builder()
                .longOpt("myLongOpt")
                .build();

        Option option = optionBuilder.create("myOption");
        assertEquals(expectedOption.getLongOpt(), option.getLongOpt());
    }

    @Test
    public void testCreateWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option expectedOption = Option.builder()
                .argName("argName")
                .build();

        Option option = optionBuilder.create("myOption");
        assertEquals(expectedOption.getArgName(), option.getArgName());
    }

    @Test
    public void testCreateWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option expectedOption = Option.builder()
                .type(Integer.class)
                .build();

        Option option = optionBuilder.create("myOption");
        assertEquals(expectedOption.getType(), option.getType());
    }

    @Test
    public void testCreateWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = Option.builder()
                .valueSeparator(':')
                .build();

        Option option = optionBuilder.create("myOption");
        assertEquals(expectedOption.getValueSeparator(), option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create();
    }
}