import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.TestUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArgumentImplTest {

    @Test
    public void testArgumentWithNoValues() {
        Argument arg = new ArgumentImpl("arg", "argument description", 0, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        CommandLine cmd = new CommandLine();
        arg.process(cmd, TestUtils.args("--arg"));

        assertNull(cmd.getValues("arg"));
    }

    @Test
    public void testArgumentWithOneValue() {
        Argument arg = new ArgumentImpl("arg", "argument description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Arrays.asList("value"), 0);

        CommandLine cmd = new CommandLine();
        arg.process(cmd, TestUtils.args("--arg", "value"));

        assertEquals(Arrays.asList("value"), cmd.getValues("arg"));
    }

    @Test
    public void testArgumentWithMinimumValues() {
        Argument arg = new ArgumentImpl("arg", "argument description", 2, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Arrays.asList("value1", "value2"), 0);

        CommandLine cmd = new CommandLine();
        arg.process(cmd, TestUtils.args("--arg", "value1", "value2"));

        assertEquals(Arrays.asList("value1", "value2"), cmd.getValues("arg"));
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithMoreValuesThanSpecified() {
        Argument arg = new ArgumentImpl("arg", "argument description", 2, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, Arrays.asList("value1", "value2", "value3"), 0);

        CommandLine cmd = new CommandLine();
        arg.process(cmd, TestUtils.args("--arg", "value1", "value2", "value3"));
    }

    @Test
    public void testArgumentWithConsumingOption() {
        Argument arg = new ArgumentImpl("arg", "argument description", 0, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, DEFAULT_CONSUME_REMAINING, null, 0);

        CommandLine cmd = new CommandLine();
        arg.processValues(cmd, TestUtils.list("--arg", "--consume").iterator(), arg);

        assertEquals(Arrays.asList(), cmd.getValues("arg"));
        assertEquals(Arrays.asList("--consume"), cmd.getValues("consume"));
    }

    @Test
    public void testArgumentWithSingleValueAndValidator() {
        Argument arg = new ArgumentImpl("arg", "argument description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MyValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        CommandLine cmd = new CommandLine();
        arg.process(cmd, TestUtils.args("--arg", "value"));

        assertEquals(Arrays.asList("value"), cmd.getValues("arg"));
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithInvalidValueAndValidator() {
        Argument arg = new ArgumentImpl("arg", "argument description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, new MyValidator(), DEFAULT_CONSUME_REMAINING, null, 0);

        CommandLine cmd = new CommandLine();
        arg.process(cmd, TestUtils.args("--arg", "invalid_value"));
    }
}

class MyValidator implements Validator {
    @Override
    public void validate(List values) throws InvalidArgumentException {
        for (Object value : values) {
            if (!value.toString().matches("\\d+")) {
                throw new InvalidArgumentException("Invalid value: " + value);
            }
        }
    }
}