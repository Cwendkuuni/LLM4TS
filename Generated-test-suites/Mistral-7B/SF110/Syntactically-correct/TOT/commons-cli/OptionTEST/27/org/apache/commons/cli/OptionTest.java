import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionWithNoArg() {
        Option option = new Option("a", "option-a", false, "This is an option without argument");
        Assert.assertEquals("option-a", option.getOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getValue());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertEquals("This is an option without argument", option.getDescription());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionWithOneArg() {
        Option option = new Option("b", "option-b", true, "This is an option with one argument");
        Assert.assertEquals("b", option.getOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getValue());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertEquals("This is an option with one argument", option.getDescription());
        Assert.assertEquals(1, option.getArgs());

        option.setValue("value");
        Assert.assertEquals("value", option.getValue());
        List<String> values = option.getValuesList();
        Assert.assertEquals("value", values.get(0));
        Assert.assertEquals(1, values.size());
    }

    @Test
    public void testOptionWithMultipleArgs() {
        Option option = new Option("c", "option-c", true, "This is an option with multiple arguments");
        Assert.assertEquals("c", option.getOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getValue());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertEquals("This is an option with multiple arguments", option.getDescription());
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());

        option.setValue("value1,value2");
        List<String> values = option.getValuesList();
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals(2, values.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOption() {
        try {
            new Option("", "invalid", false, "");
            Assert.fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Option definition is invalid: opt is empty", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionLongName() {
        try {
            new Option("a", "invalid-long-name", false, "");
            Assert.fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid long option name: invalid-long-name", e.getMessage());
        }
    }

    @Test
    public void testParseOption() {
        String[] args = {"--option-a"};
        Options options = new Options();
        Option option = new Option("a", "option-a", false, "This is an option without argument");
        options.addOption(option);

        ParseContext parseContext = new ParseContext();
        try {
            CommandLine cmd = options.parse(args, parseContext);
            Assert.assertEquals(true, cmd.hasOption("option-a"));
        } catch (ParseException e) {
            Assert.fail("Expected no ParseException, but got: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testParseOptionWithMissingArg() {
        String[] args = {"--option-b"};
        Options options = new Options();
        Option option = new Option("b", "option-b", true, "This is an option with one argument");
        options.addOption(option);

        ParseContext parseContext = new ParseContext();
        CommandLine cmd = null;
        try {
            cmd = options.parse(args, parseContext);
        } catch (ParseException e) {
            // Expected
        }
        Assert.assertNull(cmd);
        Assert.assertEquals("Missing required option 'option-b'", e.getMessage());
    }

    @Test(expected = ParseException.class)
    public void testParseOptionWithInvalidArg() {
        String[] args = {"--option-b=invalid"};
        Options options = new Options();
        Option option = new Option("b", "option-b", true, "This is an option with one argument");
        options.addOption(option);

        ParseContext parseContext = new ParseContext();
        CommandLine cmd = null;
        try {
            cmd = options.parse(args, parseContext);
        } catch (ParseException e) {
            // Expected
        }
        Assert.assertNull(cmd);
        Assert.assertEquals("Invalid value for option 'option-b': invalid", e.getMessage());
    }
}