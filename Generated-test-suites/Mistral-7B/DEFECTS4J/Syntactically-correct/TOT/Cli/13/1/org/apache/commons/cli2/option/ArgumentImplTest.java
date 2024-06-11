package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {

    @Test
    public void testArgumentWithDefaultValues() {
        String[] args = {"-a", "value1", "value2"};
        Argument arg = new ArgumentImpl("a", "A test argument", 1, Integer.MAX_VALUE, '-', ' ', null, "--", Arrays.asList("default1", "default2"), 0);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        arg.process(commandLine, Arrays.asList(args).listIterator());

        List<String> values = commandLine.getValues(arg);

        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("default1", values.get(2));

        commandLine.setValues(arg, null);

        arg.defaults(commandLine);

        values = commandLine.getValues(arg);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test
    public void testArgumentWithMinimumValue() {
        String[] args = {"-a", "value1", "value2", "value3"};
        Argument arg = new ArgumentImpl("a", "A test argument", 2, Integer.MAX_VALUE, '-', ' ', null, "--", Arrays.asList("default1", "default2"), 0);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        arg.process(commandLine, Arrays.asList(args).listIterator());

        List<String> values = commandLine.getValues(arg);

        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithMaximumValue() {
        String[] args = {"-a", "value1", "value2", "value3", "value4"};
        Argument arg = new ArgumentImpl("a", "A test argument", 2, 3, '-', ' ', null, "--", Arrays.asList("default1", "default2"), 0);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        arg.process(commandLine, Arrays.asList(args).listIterator());

        List<String> values = commandLine.getValues(arg);

        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));

        commandLine.setValues(arg, null);

        arg.defaults(commandLine);

        values = commandLine.getValues(arg);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));

        arg.process(commandLine, Arrays.asList(args).listIterator());

        values = commandLine.getValues(arg);

        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));

        commandLine.setValues(arg, null);

        arg.defaults(commandLine);

        values = commandLine.getValues(arg);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));

        commandLine.setValues(arg, Arrays.asList("value4").iterator());

        arg.process(commandLine, Arrays.asList("--").listIterator());

        values = commandLine.getValues(arg);

        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));

        commandLine.setValues(arg, null);

        arg.defaults(commandLine);

        values = commandLine.getValues(arg);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));

        commandLine.setValues(arg, Arrays.asList("value4").iterator());

        arg.process(commandLine, Arrays.asList("--").listIterator());

        values = commandLine.getValues(arg);

        Assert.assertEquals(4, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));
        Assert.assertEquals("value4", values.get(3));

        commandLine.setValues(arg, null);

        arg.defaults(commandLine);

        values = commandLine.getValues(arg);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithInvalidValues() {
        String[] args = {"-a", "value1", "invalidValue"};
        Argument arg = new ArgumentImpl("a", "A test argument", 1, 1, '-', ' ', null, "--", Arrays.asList("default1"), 0);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        arg.process(commandLine, Arrays.asList(args).listIterator());

        List<String> values = commandLine.getValues(arg);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("invalidValue", values.get(1));

        commandLine.setValues(arg, null);

        arg.defaults(commandLine);

        values = commandLine.getValues(arg);

        Assert.assertEquals(1, values.size());
        Assert.assertEquals("default1", values.get(0));

        commandLine.setValues(arg, Arrays.asList("invalidValue").iterator());

        arg.process(commandLine, Arrays.asList("--").listIterator());

        values = commandLine.getValues(arg);

        Assert.assertEquals(1, values.size());
        Assert.assertEquals("invalidValue", values.get(0));

        commandLine.setValues(arg, null);

        arg.defaults(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithConsumeRemaining() {
        String[] args = {"-x", "value1", "-a", "value2", "-a", "value3"};
        Argument arg = new ArgumentImpl("a", "A test argument", 1, Integer.MAX_VALUE, '-', ' ', "--", "--", null, 0);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        arg.process(commandLine, Arrays.asList(args).listIterator());

        List<String> values = commandLine.getValues(arg);

        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value2", values.get(0));
        Assert.assertEquals("value3", values.get(1));

        commandLine.setValues(arg, null);

        arg.defaults(commandLine);

        values = commandLine.getValues(arg);

        Assert.assertEquals(0, values.size());

        arg.process(commandLine, Arrays.asList("--consumeRemaining").listIterator());

        values = commandLine.getValues(arg);

        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value2", values.get(0));
        Assert.assertEquals("value3", values.get(1));

        commandLine.setValues(arg, null);

        arg.defaults(commandLine);

        values = commandLine.getValues(arg);

        Assert.assertEquals(0, values.size());

        arg.process(commandLine, Arrays.asList("--consumeRemaining").listIterator());

        values = commandLine.getValues(arg);

        Assert.assertEquals(args.length - 2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithInvalidConsumeRemaining() {
        String[] args = {"-x", "value1", "-a", "value2", "consumeRemaining"};
        Argument arg = new ArgumentImpl("a", "A test argument", 1, Integer.MAX_VALUE, '-', ' ', "--", "--", null, 0);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        arg.process(commandLine, Arrays.asList(args).listIterator());

        List<String> values = commandLine.getValues(arg);

        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value2", values.get(0));

        commandLine.setValues(arg, null);

        arg.defaults(commandLine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentWithInvalidSeparator() {
        Argument arg = new ArgumentImpl("a", "A test argument", 1, 1, '|', ' ', "--", "--", null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentWithInvalidMinimum() {
        Argument arg = new ArgumentImpl("a", "A test argument", -1, 1, '-', ' ', "--", "--", null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentWithInvalidMaximum() {
        Argument arg = new ArgumentImpl("a", "A test argument", 1, -1, '-', ' ', "--", "--", null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentWithInvalidDefaultValues() {
        List<String> defaultValues = Arrays.asList("value1", "value2", "value3");

        Argument arg = new ArgumentImpl("a", "A test argument", 1, 1, '-', ' ', "--", "--", defaultValues, 0);
    }

    @Test
    public void testArgumentWithEmptyDefaultValues() {
        Argument arg = new ArgumentImpl("a", "A test argument", 1, 1, '-', ' ', "--", "--", Collections.emptyList(), 0);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        arg.process(commandLine, Arrays.asList("--consumeRemaining").listIterator());

        List<String> values = commandLine.getValues(arg);

        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testArgumentWithNoDefaultValues() {
        Argument arg = new ArgumentImpl("a", "A test argument", 1, 1, '-', ' ', "--", "--", null, 0);

        WriteableCommandLine commandLine = new DefaultCommandLine();

        arg.process(commandLine, Arrays.asList("--consumeRemaining").listIterator());

        List<String> values = commandLine.getValues(arg);

        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testArgumentWithValidator() {
        Argument arg = new ArgumentImpl("a", "A test argument", 1, 1, '-', ' ', "--", "--", null, 0) {
            @Override
            public Validator getValidator() {
                return new IntValidator();
            }
        };

        String[] args = {"-a", "1", "2", "3"};
        WriteableCommandLine commandLine = new DefaultCommandLine();

        arg.process(commandLine, Arrays.asList(args).listIterator());

        List<String> values = commandLine.getValues(arg);

        Assert.assertEquals(3, values.size());
        Assert.assertEquals("1", values.get(0));
        Assert.assertEquals("2", values.get(1));
        Assert.assertEquals("3", values.get(2));

        commandLine.setValues(arg, null);

        arg.defaults(commandLine);

        values = commandLine.getValues(arg);

        Assert.assertEquals(0, values.size());

        commandLine.setValues(arg, Arrays.asList("1", "2", "3").iterator());

        arg.process(commandLine, Arrays.asList("--consumeRemaining").listIterator());

        values = commandLine.getValues(arg);

        Assert.assertEquals(3, values.size());
        Assert.assertEquals("1", values.get(0));
        Assert.assertEquals("2", values.get(1));
        Assert.assertEquals("3", values.get(2));

        commandLine.setValues(arg, null);

        arg.defaults(commandLine);

        values = commandLine.getValues(arg);

        Assert.assertEquals(0, values.size());

        commandLine.setValues(arg, Arrays.asList("1", "2", "3").iterator());

        arg.process(commandLine, Arrays.asList("--consumeRemaining").listIterator());

        values = commandLine.getValues(arg);

        Assert.assertEquals(3, values.size());
        Assert.assertEquals("1", values.get(0));
        Assert.assertEquals("2", values.get(1));
        Assert.assertEquals("3", values.get(2));

        commandLine.setValues(arg, null);

        arg.defaults(commandLine);

        values = commandLine.getValues(arg);

        Assert.assertEquals(0, values.size());

        commandLine.setValues(arg, Arrays.asList("invalidValue").iterator());

        try {
            arg.process(commandLine, Arrays.asList("--consumeRemaining").listIterator());
        } catch (OptionException e) {
            Assert.assertEquals(ResourceConstants.ARGUMENT_UNEXPECTED_VALUE, e.getMessage());
        }

        values = commandLine.getValues(arg);

        Assert.assertEquals(0, values.size());
    }

    private static class IntValidator implements Validator {
        @Override
        public void validate(List<String> values) throws InvalidArgumentException {
            for (String value : values) {
                try {
                    int i = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new InvalidArgumentException("Expected an integer value");
                }
            }
        }
    }
}