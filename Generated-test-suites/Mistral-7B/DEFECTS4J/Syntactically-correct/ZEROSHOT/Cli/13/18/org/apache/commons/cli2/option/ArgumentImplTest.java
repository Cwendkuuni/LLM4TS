import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {

    private CommandLineParser parser;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        formatter = new HelpFormatter();
    }

    @Test
    public void testArgumentWithDefaultValuesAndNoValidator() {
        Option option = new OptionBuilder()
                .withLongOpt("myOption")
                .withDescription("This is my option")
                .isRequired(false)
                .withArgName("arg")
                .hasArg()
                .withDefaultValue("default1, default2")
                .create(null);

        Argument argument = new ArgumentBuilder()
                .withName("arg")
                .withDescription("Argument description")
                .withMinValues(1)
                .withMaxValues(2)
                .create(option);

        Options options = new Options();
        options.addOption(option);

        try {
            CommandLine commandLine = parser.parse(options, new String[]{"myOption", "arg1", "arg2"});
            List<String> values = commandLine.getValues("myOption");
            List<String> defaults = argument.getDefaultValues();

           Assert.assertEquals(Arrays.asList("arg1", "arg2"), values);
            Assert.assertEquals(Arrays.asList("default1", "default2"), defaults);

        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testArgumentWithNoDefaultValuesAndNoValidator() {
        Option option = new OptionBuilder()
                .withLongOpt("myOption")
                .withDescription("This is my option")
                .isRequired(false)
                .withArgName("arg")
                .hasArg()
                .create(null);

        Argument argument = new ArgumentBuilder()
                .withName("arg")
                .withDescription("Argument description")
                .withMinValues(1)
                .withMaxValues(2)
                .create(option);

        Options options = new Options();
        options.addOption(option);

        try {
            CommandLine commandLine = parser.parse(options, new String[]{"myOption", "arg1", "arg2"});
            List<String> values = commandLine.getValues("myOption");
            List<String> defaults = argument.getDefaultValues();

            Assert.assertEquals(Arrays.asList("arg1", "arg2"), values);
            Assert.assertNull(defaults);

        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testArgumentWithDefaultValuesAndValidator() {
        Option option = new OptionBuilder()
                .withLongOpt("myOption")
                .withDescription("This is my option")
                .isRequired(false)
                .withArgName("arg")
                .hasArg()
                .create(null);

        Argument argument = new ArgumentBuilder()
                .withName("arg")
                .withDescription("Argument description")
                .withMinValues(1)
                .withMaxValues(2)
                .withDefaultValue("default1, default2")
                .withValidator(new MyValidator())
                .create(option);

        Options options = new Options();
        options.addOption(option);

        try {
            CommandLine commandLine = parser.parse(options, new String[]{"myOption", "arg1", "arg2"});
            List<String> values = commandLine.getValues("myOption");
            List<String> defaults = argument.getDefaultValues();

            Assert.assertEquals(Arrays.asList("arg1", "arg2"), values);
            Assert.assertEquals(Arrays.asList("default1", "default2"), defaults);

        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testArgumentWithIncorrectNumberOfArguments() {
        Option option = new OptionBuilder()
                .withLongOpt("myOption")
                .withDescription("This is my option")
                .isRequired(false)
                .withArgName("arg")
                .hasArg()
                .create(null);

        Argument argument = new ArgumentBuilder()
                .withName("arg")
                .withDescription("Argument description")
                .withMinValues(1)
                .withMaxValues(2)
                .create(option);

        Options options = new Options();
        options.addOption(option);

        try {
            CommandLine commandLine = parser.parse(options, new String[]{"myOption", "arg1"});
        } catch (ParseException e) {
            Assert.assertEquals(e.getMessage(), "Argument arg has wrong number of values: 1, but should be between 1 and 2.");
            throw e;
        }
    }

    @Test(expected = ParseException.class)
    public void testArgumentWithInvalidValues() {
        Option option = new OptionBuilder()
                .withLongOpt("myOption")
                .withDescription("This is my option")
                .isRequired(false)
                .withArgName("arg")
                .hasArg()
                .create(null);

        Argument argument = new ArgumentBuilder()
                .withName("arg")
                .withDescription("Argument description")
                .withMinValues(1)
                .withMaxValues(2)
                .withValidator(new MyValidator())
                .create(option);

        Options options = new Options();
        options.addOption(option);

        try {
            CommandLine commandLine = parser.parse(options, new String[]{"myOption", "invalidValue"});
        } catch (ParseException e) {
            Assert.assertEquals(e.getMessage(), "Argument arg: invalid value(s): invalidValue.");
            throw e;
        }
    }

    private static class MyValidator implements Validator {
        @Override
        public void validate(List<String> values) throws InvalidArgumentException {
            for (String value : values) {
                if (!isValid(value)) {
                    throw new InvalidArgumentException("Argument arg: invalid value(s): " + value);
                }
            }
        }

        private boolean isValid(String value) {
            // validation logic here
            return true;
        }
    }

    @Test
    public void testArgumentUsage() {
        Option option = new OptionBuilder()
                .withLongOpt("myOption")
                .withDescription("This is my option")
                .isRequired(false)
                .withArgName("arg")
                .hasArg()
                .create(null);

        Argument argument = new ArgumentBuilder()
                .withName("arg")
                .withDescription("Argument description")
                .withMinValues(1)
                .withMaxValues(2)
                .create(option);

        Options options = new Options();
        options.addOption(option);

        String usage = TestUtils.printUsage(formatter, options, true);
        System.out.println(usage);
    }
}