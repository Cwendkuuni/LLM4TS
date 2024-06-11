package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        String expectedLongOpt = "newLongOpt";

        OptionBuilder actualOptionBuilder = OptionBuilder.withLongOpt(expectedLongOpt);
        Assert.assertEquals(expectedLongOpt, OptionBuilder.longopt);
    }

    // Testing a null argument
    @Test(expected = IllegalArgumentException.class)
    public void testWithLongOptNullArgument() {
        OptionBuilder optionBuilder = new OptionBuilder();

        OptionBuilder.withLongOpt(null);
    }

    // Testing an empty string
    @Test
    public void testWithLongOptEmptyString() {
        OptionBuilder optionBuilder = new OptionBuilder();
        String expectedLongOpt = "";

        OptionBuilder actualOptionBuilder = OptionBuilder.withLongOpt(expectedLongOpt);
        Assert.assertEquals(expectedLongOpt, OptionBuilder.longopt);
    }

    // Testing a long option with special characters
    @Test
    public void testWithLongOptSpecialCharacters() {
        OptionBuilder optionBuilder = new OptionBuilder();
        String expectedLongOpt = "#%^&*";

        OptionBuilder actualOptionBuilder = OptionBuilder.withLongOpt(expectedLongOpt);
        Assert.assertEquals(expectedLongOpt, OptionBuilder.longopt);
    }

    // Testing create method with valid option
    @Test
    public void testCreateValidOption() {
        String expectedDescription = "Option description";
        char expectedOpt = 'A';
        String expectedLongOpt = "longOptionA";

        Option option = OptionBuilder.create(expectedOpt).withDescription(expectedDescription).create();

        Assert.assertEquals(expectedDescription, option.getDescription());
        Assert.assertEquals(expectedOpt, option.getOpt());
        Assert.assertEquals(expectedLongOpt, option.getLongOpt());
    }

    // Testing create method with default values
    @Test
    public void testCreateDefaultOption() {
        Option option = OptionBuilder.create().create();

        Assert.assertNull(option.getDescription());
        Assert.assertNull(OptionBuilder.longopt);
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertEquals(0, option.getValueSeparator());
    }

    // Testing create method with invalid arguments
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidOption() {
        OptionBuilder optionBuilder = new OptionBuilder();

        Option option = OptionBuilder.create();
        option.setRequired(true);

        OptionBuilder.withArgName("argName");

        OptionBuilder.create();
    }
}