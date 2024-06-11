package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilder() {
        Option option = OptionBuilderTest.createOption();

        Assert.assertEquals("optionDescription", option.getDescription());
        Assert.assertEquals('C', option.getOpt());
        Assert.assertEquals(null, option.getLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        Assert.assertNull(option.getType());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getArgName().length);
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_SEPARATOR, option.getValueSeparator());

        Option requiredOption = OptionBuilderTest.createRequiredOption();

        Assert.assertEquals("requiredOptionDescription", requiredOption.getDescription());
        Assert.assertEquals('D', requiredOption.getOpt());
        Assert.assertEquals("requiredLongOpt", requiredOption.getLongOpt());
        Assert.assertTrue(requiredOption.isRequired());
        Assert.assertEquals(Option.NO_ARGUMENTS, requiredOption.getArgs().length);
        Assert.assertNull(requiredOption.getType());
        Assert.assertEquals(Option.NO_ARGUMENTS, requiredOption.getArgName().length);
        Assert.assertFalse(requiredOption.hasOptionalArg());
        Assert.assertEquals(Option.NO_SEPARATOR, requiredOption.getValueSeparator());

        Option argOption = OptionBuilderTest.createArgOption();

        Assert.assertEquals("argOptionDescription", argOption.getDescription());
        Assert.assertEquals('E', argOption.getOpt());
        Assert.assertEquals("argOptionLongOpt", argOption.getLongOpt());
        Assert.assertTrue(argOption.isRequired());
        Assert.assertEquals(new String[]{"arg"}, argOption.getArgs());
        Assert.assertNull(argOption.getType());
        Assert.assertEquals("argName", argOption.getArgName());
        Assert.assertFalse(argOption.hasOptionalArg());
        Assert.assertEquals(Option.NO_SEPARATOR, argOption.getValueSeparator());

        Option argWithValueSeparatorOption = OptionBuilderTest.createArgWithValueSeparatorOption();

        Assert.assertEquals("argWithValueSeparatorOptionDescription", argWithValueSeparatorOption.getDescription());
        Assert.assertEquals('F', argWithValueSeparatorOption.getOpt());
        Assert.assertEquals("argWithValueSeparatorOptionLongOpt", argWithValueSeparatorOption.getLongOpt());
        Assert.assertTrue(argWithValueSeparatorOption.isRequired());
        Assert.assertEquals(new String[]{"arg"}, argWithValueSeparatorOption.getArgs());
        Assert.assertNull(argWithValueSeparatorOption.getType());
        Assert.assertEquals("argName", argWithValueSeparatorOption.getArgName());
        Assert.assertTrue(argWithValueSeparatorOption.hasOptionalArg());
        Assert.assertEquals(':', argWithValueSeparatorOption.getValueSeparator());

        Option hasOptionalArgOption = OptionBuilderTest.createHasOptionalArgOption();

        Assert.assertEquals("hasOptionalArgOptionDescription", hasOptionalArgOption.getDescription());
        Assert.assertEquals('G', hasOptionalArgOption.getOpt());
        Assert.assertEquals("hasOptionalArgOptionLongOpt", hasOptionalArgOption.getLongOpt());
        Assert.assertFalse(hasOptionalArgOption.isRequired());
        Assert.assertEquals(Option.UNLIMITED_VALUES, hasOptionalArgOption.getArgs().length);
        Assert.assertNull(hasOptionalArgOption.getType());
        Assert.assertEquals("argName", hasOptionalArgOption.getArgName());
        Assert.assertTrue(hasOptionalArgOption.hasOptionalArg());
        Assert.assertEquals(Option.NO_SEPARATOR, hasOptionalArgOption.getValueSeparator());

        Option hasTypeOption = OptionBuilderTest.createHasTypeOption();

        Assert.assertEquals("hasTypeOptionDescription", hasTypeOption.getDescription());
        Assert.assertEquals('H', hasTypeOption.getOpt());
        Assert.assertEquals("hasTypeOptionLongOpt", hasTypeOption.getLongOpt());
        Assert.assertFalse(hasTypeOption.isRequired());
        Assert.assertEquals(Option.NO_ARGUMENTS, hasTypeOption.getArgs().length);
        Assert.assertEquals(String.class, hasTypeOption.getType().getClass());
        Assert.assertEquals("argName", hasTypeOption.getArgName());
        Assert.assertFalse(hasTypeOption.hasOptionalArg());
        Assert.assertEquals(Option.NO_SEPARATOR, hasTypeOption.getValueSeparator());

        Option hasArgOption = OptionBuilderTest.createHasArgOption();

        Assert.assertEquals("hasArgOptionDescription", hasArgOption.getDescription());
        Assert.assertEquals('I', hasArgOption.getOpt());
        Assert.assertEquals("hasArgOptionLongOpt", hasArgOption.getLongOpt());
        Assert.assertTrue(hasArgOption.isRequired());
        Assert.assertEquals(new String[]{"arg"}, hasArgOption.getArgs());
        Assert.assertNull(hasArgOption.getType());
        Assert.assertEquals("argName", hasArgOption.getArgName());
        Assert.assertFalse(hasArgOption.hasOptionalArg());
        Assert.assertEquals(Option.NO_SEPARATOR, hasArgOption.getValueSeparator());

        Option hasArgAndValueSeparatorOption = OptionBuilderTest.createHasArgAndValueSeparatorOption();

        Assert.assertEquals("hasArgAndValueSeparatorOptionDescription", hasArgAndValueSeparatorOption.getDescription());
        Assert.assertEquals('J', hasArgAndValueSeparatorOption.getOpt());
        Assert.assertEquals("hasArgAndValueSeparatorOptionLongOpt", hasArgAndValueSeparatorOption.getLongOpt());
        Assert.assertTrue(hasArgAndValueSeparatorOption.isRequired());
        Assert.assertEquals(new String[]{"arg"}, hasArgAndValueSeparatorOption.getArgs());
        Assert.assertNull(hasArgAndValueSeparatorOption.getType());
        Assert.assertEquals("argName", hasArgAndValueSeparatorOption.getArgName());
        Assert.assertTrue(hasArgAndValueSeparatorOption.hasOptionalArg());
        Assert.assertEquals(';', hasArgAndValueSeparatorOption.getValueSeparator());

        OptionBuilderTest.reset();
    }

    private static Option createOption() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("optionDescription");
        return optionBuilder.create('C');
    }

    private static Option createRequiredOption() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("requiredOptionDescription").isRequired();
        return optionBuilder.create('D');
    }

    private static Option createArgOption() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("argOptionDescription").isRequired().hasArg();
        return optionBuilder.create('E');
    }

    private static Option createArgWithValueSeparatorOption() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("argWithValueSeparatorOptionDescription").isRequired().hasArg().withValueSeparator(':');
        return optionBuilder.create('F');
    }

    private static Option createHasOptionalArgOption() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("hasOptionalArgOptionDescription").hasOptionalArg();
        return optionBuilder.create('G');
    }

    private static Option createHasTypeOption() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("hasTypeOptionDescription").withType(String.class);
        return optionBuilder.create('H');
    }

    private static Option createHasArgOption() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("hasArgOptionDescription").isRequired().hasArg();
        return optionBuilder.create('I');
    }

    private static Option createHasArgAndValueSeparatorOption() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("hasArgAndValueSeparatorOptionDescription").isRequired().hasArg().withValueSeparator(';');
        return optionBuilder.create('J');
    }

    public static void reset() {
        OptionBuilder.reset();
    }
}