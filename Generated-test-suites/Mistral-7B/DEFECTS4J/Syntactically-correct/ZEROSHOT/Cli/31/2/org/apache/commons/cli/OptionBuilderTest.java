package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilder() {
        Option option = OptionBuilderTest.createOption();
        Assert.assertNotNull(option);

        // test long option
        Option longOption = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .create('L');
        Assert.assertEquals("longOpt", longOption.getLongOpt());
        Assert.assertEquals("Option with long option", longOption.getDescription());
        Assert.assertEquals(true, longOption.isRequired());
        Assert.assertEquals(1, longOption.getArgPosition());
        Assert.assertNull(longOption.getValue());

        // test short option
        Option shortOption = OptionBuilder.create();
        Assert.assertEquals("", shortOption.getLongOpt());
        Assert.assertEquals("Option without long option", shortOption.getDescription());
        Assert.assertFalse(shortOption.isRequired());
        Assert.assertEquals(-1, shortOption.getArgPosition());
        Assert.assertNull(shortOption.getValue());

        // test optional argument
        Option optionalArgOption = OptionBuilder.hasOptionalArg()
                .withValueSeparator('=')
                .create('O');
        Assert.assertEquals("Option with optional argument", optionalArgOption.getDescription());
        Assert.assertEquals(true, optionalArgOption.isOptional());
        Assert.assertEquals(1, optionalArgOption.getArgs().length);
        Assert.assertEquals('=', optionalArgOption.getValueSeparator());

        // test multiple arguments
        Option multipleArgsOption = OptionBuilder.hasArgs(2)
                .create('M');
        Assert.assertEquals("Option with multiple arguments", multipleArgsOption.getDescription());
        Assert.assertEquals(2, multipleArgsOption.getArgs().length);
        Assert.assertNull(multipleArgsOption.getValue());

        // test type
        Option intTypeOption = OptionBuilder.withType(Integer.class)
                .create('I');
        Assert.assertEquals(Integer.class, intTypeOption.getType());

        // test description
        Option descriptionOption = OptionBuilder.withDescription("test description")
                .create('D');
        Assert.assertEquals("test description", descriptionOption.getDescription());
    }

    private static Option createOption() {
        Option option = OptionBuilder.create();
        return option;
    }
}