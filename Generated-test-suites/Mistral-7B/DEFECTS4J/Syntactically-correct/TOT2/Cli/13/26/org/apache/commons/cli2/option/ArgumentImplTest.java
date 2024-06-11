import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.resource.ResourceConstants;

public class ArgumentImplTest {
    @Test
    public void testArgumentImplWithMinimumValue() throws OptionException {
        String[] args = new String[]{"--arg", "value"};

        Options options = new Options();
        Argument argument = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, "--", null);
        options.addArgument(argument);

        CommandLine commandLine = new CommandLineParser(options).parse(args);
        Argument option = commandLine.getOption("arg");

        assertEquals("value", option.getValue());
        assertEquals(1, option.getValue().size());
    }

    @Test
    public void testArgumentImplWithMaximumValue() throws OptionException {
        String[] args = new String[]{"--arg", "value1", "value2"};

        Options options = new Options();
        Argument argument = new ArgumentImpl("arg", "description", 1, 2, ' ', ' ', null, "--", null);
        options.addArgument(argument);

        CommandLine commandLine = new CommandLineParser(options).parse(args);
        Argument option = commandLine.getOption("arg");

        assertEquals(2, option.getValue().size());
        assertEquals("value1", option.getValue().get(0));
        assertEquals("value2", option.getValue().get(1));
    }

    @Test(expected = OptionException.class)
    public void testArgumentImplWithLessValuesThanMinimum() throws OptionException {
        String[] args = new String[]{"--arg"};

        Options options = new Options();
        Argument argument = new ArgumentImpl("arg", "description", 2, 2, ' ', ' ', null, "--", null);
        options.addArgument(argument);

        CommandLine commandLine = new CommandLineParser(options).parse(args);

        // This test should fail with OptionException
        argument.processValues(commandLine, commandLine.args(), argument);
    }

    @Test(expected = OptionException.class)
    public void testArgumentImplWithMoreValuesThanMaximum() throws OptionException {
        String[] args = new String[]{"--arg", "value1", "value2", "value3"};

        Options options = new Options();
        Argument argument = new ArgumentImpl("arg", "description", 2, 2, ' ', ' ', null, "--", null);
        options.addArgument(argument);

        CommandLine commandLine = new CommandLineParser(options).parse(args);

        // This test should fail with OptionException
        argument.processValues(commandLine, commandLine.args(), argument);
    }

    @Test
    public void testArgumentImplWithConsumingOption() throws OptionException {
        String[] args = new String[]{"--arg", "--consume"};

        Options options = new Options();
        Argument argument = new ArgumentImpl("arg", "description", 1, 1, ' ', ' ', null, "--consume", null);
        options.addArgument(argument);

        CommandLine commandLine = new CommandLineParser(options).parse(args);
        Argument option = commandLine.getOption("arg");

        assertEquals(0, option.getValue().size());
    }

    @Test
    public void testArgumentImplWithSeparator() throws OptionException {
        String[] args = new String[]{"--arg", "value1", "value2"};

        Options options = new Options();
        Argument argument = new ArgumentImpl("arg", "description", 1, 2, ' ', ',', null, "--", null);
        options.addArgument(argument);

        CommandLine commandLine = new CommandLineParser(options).parse(args);
        Argument option = commandLine.getOption("arg");

        assertEquals(2, option.getValue().size());
        assertEquals("value1,value2", option.getValue().toString());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testArgumentImplWithInvalidValues() throws OptionException, InvalidArgumentException {
        String[] args = new String[]{"--arg", "value1", "invalidValue"};

        Options options = new Options();
        Argument argument = new ArgumentImpl("arg", "description", 1, 2, ' ', ' ', new MyValidator(), "--", null);
        options.addArgument(argument);

        CommandLine commandLine = new CommandLineParser(options).parse(args);

        // This test should fail with InvalidArgumentException
        argument.processValues(commandLine, commandLine.args(), argument);
    }

    @Test
    public void testArgumentImplWithDefaultValues() throws OptionException {
        String[] args = new String[]{"--arg"};

        Options options = new Options();
        Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", Arrays.asList("defaultValue1", "defaultValue2"));
        options.addArgument(argument);

        CommandLine commandLine = new CommandLineParser(options).parse(args);
        Argument option = commandLine.getOption("arg");

        assertEquals(2, option.getValue().size());
        assertEquals("defaultValue1", option.getValue().get(0));
        assertEquals("defaultValue2", option.getValue().get(1));
    }

    @Test
    public void testArgumentImplWithNoValues() throws OptionException {
        String[] args = new String[]{"--arg"};

        Options options = new Options();
        Argument argument = new ArgumentImpl("arg", "description", 0, 1, ' ', ' ', null, "--", null);
        options.addArgument(argument);

        CommandLine commandLine = new CommandLineParser(options).parse(args);
        Argument option = commandLine.getOption("arg");

        assertEquals(0, option.getValue().size());
    }

    // helper class for testing with custom validator
    static class MyValidator implements Validator {
        @Override
        public void validate(List values) throws InvalidArgumentException {
            if (values.contains("invalidValue")) {
                throw new InvalidArgumentException("Invalid value: invalidValue");
            }
        }
    }
}