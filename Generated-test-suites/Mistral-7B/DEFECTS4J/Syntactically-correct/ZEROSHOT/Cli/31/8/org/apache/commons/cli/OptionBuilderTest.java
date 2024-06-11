package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilder() {
        Option option = OptionBuilderTest.buildOption();
        Assert.assertNotNull(option);
        Assert.assertEquals("optionDescription", option.getLongOpt());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(' ', option.getValueSeparator());

        Option optionalOption = OptionBuilderTest.buildOptionalOption();
        Assert.assertNotNull(optionalOption);
        Assert.assertEquals("optionalOptionDescription", optionalOption.getLongOpt());
        Assert.assertEquals("optionalArgName", optionalOption.getArgName());
        Assert.assertFalse(optionalOption.isRequired());
        Assert.assertEquals(Option.UNLIMITED_VALUES, optionalOption.getArgs().length);
        Assert.assertNull(optionalOption.getType());
        Assert.assertTrue(optionalOption.hasOptionalArg());
        Assert.assertEquals(' ', optionalOption.getValueSeparator());
    }

    private static Option buildOption() {
        return OptionBuilder.withLongOpt("optionDescription")
                .withArgName("argName")
                .create();
    }

    private static Option buildOptionalOption() {
        return OptionBuilder.withLongOpt("optionalOptionDescription")
                .withArgName("optionalArgName")
                .hasArgs()
                .create();
    }
}