import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionCreationWithNoArguments() {
        Option option = new Option("opt", "Option without arguments", false);
        Assert.assertEquals("-opt", option.getKey());
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertNull(option.getValue());
        Assert.assertFalse(option.hasValues());
        Assert.assertEquals("Option without arguments", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
    }

    @Test
    public void testOptionCreationWithOneArgument() {
        Option option = new Option("opt", "Option with one argument", true);
        Assert.assertEquals("-opt", option.getKey());
        Assert.assertNull(option.getLongOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertNull(option.getValue());
        Assert.assertFalse(option.hasValues());
        Assert.assertEquals("Option with one argument", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());

        List<String> arguments = new ArrayList<>();
        arguments.add("arg");
        try {
            option.parse(new String[]{"-opt", "arg"});
        } catch (ParseException e) {
            Assert.fail();
        }
        Assert.assertEquals("arg", option.getValue());
        Assert.assertEquals(1, option.getValues().size());
        Assert.assertEquals("arg", option.getValues().get(0));
    }

    @Test
    public void testOptionCreationWithLongOptionNameAndOneArgument() {
        Option option = new Option("long-opt", "Option with long option name and one argument", true);
        Assert.assertNull(option.getKey());
        Assert.assertEquals("long-opt", option.getLongOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertNull(option.getValue());
        Assert.assertFalse(option.hasValues());
        Assert.assertEquals("Option with long option name and one argument", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());

        List<String> arguments = new ArrayList<>();
        arguments.add("arg");
        try {
            option.parse(new String[]{"--long-opt", "arg"});
        } catch (ParseException e) {
            Assert.fail();
        }
        Assert.assertEquals("arg", option.getValue());
        Assert.assertEquals(1, option.getValues().size());
        Assert.assertEquals("arg", option.getValues().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithInvalidOptionName() {
        new Option("", "Invalid option name", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithInvalidDescription() {
        new Option("opt", "", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithInvalidHasArgValue() {
        new Option("opt", "Option description", 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithInvalidNumberOfArgsValue() {
        new Option("opt", "Option description", -3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithInvalidType() {
        new Option("opt", "Option description", new String[0]);
    }

    @Test
    public void testOptionCreationWithOptionalArgument() {
        Option option = new Option("opt", "Option with optional argument", false, "default");
        Assert.assertEquals("-opt", option.getKey());
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals("default", option.getArgName());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArgs());

        List<String> arguments = new ArrayList<>();
        arguments.add("arg");
        try {
            option.parse(new String[]{"-opt", "arg"});
        } catch (ParseException e) {
            Assert.assertEquals("The option 'opt' requires an argument - arg is missing", e.getMessage());
        }

        Assert.assertNull(option.getValue());
        Assert.assertFalse(option.hasValues());

        arguments.clear();
        arguments.add("default");
        try {
            option.parse(new String[]{"-opt", "default"});
        } catch (ParseException e) {
            Assert.fail();
        }
        Assert.assertEquals("default", option.getValue());
        Assert.assertEquals(1, option.getValues().size());
        Assert.assertEquals("default", option.getValues().get(0));
    }

    @Test
    public void testOptionCreationWithLongOptionNameAndOptionalArgument() {
        Option option = new Option("long-opt", "Option with long option name and optional argument", false, "default");
        Assert.assertNull(option.getKey());
        Assert.assertEquals("long-opt", option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals("default", option.getArgName());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArgs());

        List<String> arguments = new ArrayList<>();
        arguments.add("arg");
        try {
            option.parse(new String[]{"--long-opt", "arg"});
        } catch (ParseException e) {
            Assert.assertEquals("The option 'long-opt' requires an argument - arg is missing", e.getMessage());
        }

        Assert.assertNull(option.getValue());
        Assert.assertFalse(option.hasValues());

        arguments.clear();
        arguments.add("default");
        try {
            option.parse(new String[]{"--long-opt", "default"});
        } catch (ParseException e) {
            Assert.fail();
        }
        Assert.assertEquals("default", option.getValue());
        Assert.assertEquals(1, option.getValues().size());
        Assert.assertEquals("default", option.getValues().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithInvalidArgName() {
        new Option("opt", "Option description", false, "");
    }

    @Test
    public void testOptionCreationWithMultipleArguments() {
        Option option = new Option("opt", "Option with multiple arguments", false);
        Assert.assertEquals("-opt", option.getKey());
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertFalse(option.hasValueSeparator());
        Assert.assertFalse(option.hasValues());
        Assert.assertEquals("Option with multiple arguments", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());

        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        try {
            option.parse(arguments.toArray(new String[0]));
        } catch (ParseException e) {
            Assert.fail();
        }
        Assert.assertEquals(2, option.getValues().size());
        Assert.assertEquals("arg1", option.getValues().get(0));
        Assert.assertEquals("arg2", option.getValues().get(1));
    }

    @Test
    public void testOptionCreationWithLongOptionNameAndMultipleArguments() {
        Option option = new Option("long-opt", "Option with long option name and multiple arguments", false);
        Assert.assertNull(option.getKey());
        Assert.assertEquals("long-opt", option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertFalse(option.hasValueSeparator());
        Assert.assertFalse(option.hasValues());
        Assert.assertEquals("Option with long option name and multiple arguments", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());

        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        try {
            option.parse(arguments.toArray(new String[0]));
        } catch (ParseException e) {
            Assert.fail();
        }
        Assert.assertEquals(2, option.getValues().size());
        Assert.assertEquals("arg1", option.getValues().get(0));
        Assert.assertEquals("arg2", option.getValues().get(1));
    }

    @Test
    public void testOptionCreationWithValueSeparator() {
        Option option = new Option("opt", "Option with value separator", false, ",");
        Assert.assertEquals("-opt", option.getKey());
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertTrue(option.hasValueSeparator());
        Assert.assertFalse(option.hasValues());
        Assert.assertEquals("Option with value separator", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());

        List<String> arguments = new ArrayList<>();
        arguments.add("arg1,arg2");
        try {
            option.parse(arguments.toArray(new String[0]));
        } catch (ParseException e) {
            Assert.fail();
        }
        Assert.assertEquals(2, option.getValues().size());
        Assert.assertEquals("arg1", option.getValues().get(0));
        Assert.assertEquals("arg2", option.getValues().get(1));
    }

    @Test
    public void testOptionCreationWithLongOptionNameAndValueSeparator() {
        Option option = new Option("long-opt", "Option with long option name and value separator", false, ",");
        Assert.assertNull(option.getKey());
        Assert.assertEquals("long-opt", option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertTrue(option.hasValueSeparator());
        Assert.assertFalse(option.hasValues());
        Assert.assertEquals("Option with long option name and value separator", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());

        List<String> arguments = new ArrayList<>();
        arguments.add("arg1,arg2");
        try {
            option.parse(arguments.toArray(new String[0]));
        } catch (ParseException e) {
            Assert.fail();
        }
        Assert.assertEquals(2, option.getValues().size());
        Assert.assertEquals("arg1", option.getValues().get(0));
        Assert.assertEquals("arg2", option.getValues().get(1));
    }

    @Test
    public void testOptionCreationWithRequiredArgument() {
        Option option = new Option("opt", "Option with required argument", true);
        Assert.assertEquals("-opt", option.getKey());
        Assert.assertNull(option.getLongOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertFalse(option.hasValueSeparator());
        Assert.assertFalse(option.hasValues());
        Assert.assertEquals("Option with required argument", option.getDescription());
        Assert.assertTrue(option.isRequired());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());

        List<String> arguments = new ArrayList<>();
        arguments.add("arg");
        try {
            option.parse(arguments.toArray(new String[0]));
        } catch (ParseException e) {
            Assert.fail();
        }
        Assert.assertEquals("arg", option.getValue());
        Assert.assertEquals(1, option.getValues().size());
        Assert.assertEquals("arg", option.getValues().get(0));
    }

    @Test(expected = ParseException.class)
    public void testOptionCreationWithMissingArgument() {
        Option option = new Option("opt", "Option with required argument", true);
        List<String> arguments = new ArrayList<>();
        try {
            option.parse(arguments.toArray(new String[0]));
        } catch (ParseException e) {
            Assert.assertEquals("The option 'opt' requires an argument", e.getMessage());
        }
    }
}