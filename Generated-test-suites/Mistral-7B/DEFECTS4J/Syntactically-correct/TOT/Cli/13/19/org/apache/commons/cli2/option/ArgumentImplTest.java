package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.ValidateResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ArgumentImplTest {

    private Argument arg;
    private CommandLine cmd;
    private HelpFormatter formatter;

    @Before
    public void setup() {
        arg = new ArgumentImpl("arg", "an argument", 1, 5, 'a', 'b', null, "--", Arrays.asList("val1", "val2"), 0);
        cmd = new CommandLine();
        formatter = new HelpFormatter();
    }

    @Test
    public void testValidValues() {
        arg.process(cmd, Arrays.asList("--arg", "val1", "val2").listIterator());

        assertEquals(2, cmd.getValues(arg).size());
        assertEquals("val1", cmd.getValues(arg).get(0));
        assertEquals("val2", cmd.getValues(arg).get(1));
    }

    @Test
    public void testConsumeRemaining() {
        arg = new ArgumentImpl("arg", "an argument", 1, Integer.MAX_VALUE, 'a', 'b', null, "--", null, 0);

        arg.process(cmd, Arrays.asList("--arg", "--consume-remaining").listIterator());
        arg.process(cmd, Arrays.asList("val1", "val2", "val3").listIterator());

        assertEquals(3, cmd.getValues(arg).size());
        assertEquals("val1", cmd.getValues(arg).get(0));
        assertEquals("val2", cmd.getValues(arg).get(1));
        assertEquals("val3", cmd.getValues(arg).get(2));
    }

    @Test
    public void testInvalidValues() {
        arg = new ArgumentImpl("arg", "an argument", 1, 1, 'a', 'b', null, "--", Arrays.asList("val1"), 0);

        assertThrows(OptionException.class, () -> arg.process(cmd, Arrays.asList("--arg").listIterator()));

        arg = new ArgumentImpl("arg", "an argument", 1, 1, 'a', 'b', null, "--", Arrays.asList("val1", "val2", "val3"), 0);

        assertThrows(OptionException.class, () -> arg.process(cmd, Arrays.asList("--arg", "val1", "val2", "val3", "val4").listIterator()));
    }

    @Test
    public void testValidator() {
        arg = new ArgumentImpl("arg", "an argument", 1, 1, 'a', 'b', new ArgumentValidator(), "--", null, 0);

        List<String> values = Arrays.asList("val1", "val2");

        ValidateResult result = arg.validator.validate(values);

        assertEquals(ValidateResult.VALID, result);

        values = Arrays.asList("val1", "val2", "invalid");

        result = arg.validator.validate(values);

        assertEquals(ValidateResult.INVALID, result);
        assertEquals("Invalid argument: invalid", result.getMessage());
    }

    @Test
    public void testUsage() {
        String usage = formatter.printHelp("Test", arg, true);

        assertEquals("arg [VALUES]...\n" +
                "an argument\n", usage);
    }
}