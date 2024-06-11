package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.OptionPrinter;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link ArgumentImpl} class.
 */
public class ArgumentImplTest {

    @Test
    public void testArgument() throws OptionException {
        Argument arg = new ArgumentImpl("arg", "test argument", 1, 1, ' ', ' ', null, null, null, 0);

        // test parsing
        CommandLine cmd = new DefaultParser().parse(arg.getOptions());
        Assert.assertEquals(1, cmd.getValues(arg).size());
        Assert.assertEquals("value1", cmd.getValues(arg).get(0));

        // test usage
        arg.process(cmd, cmd.iterator());
        Assert.assertEquals(1, cmd.getValues(arg).size());
        Assert.assertEquals("value1", cmd.getValues(arg).get(0));

        // test help
        OptionPrinter op = new HelpFormatter().printHelp("test", arg.getOptions(), true);
        Assert.assertNotNull(op);
    }

    @Test(expected = OptionException.class)
    public void testArgumentInvalidMinimum() throws OptionException {
        Argument arg = new ArgumentImpl("arg", "test argument", 0, 1, ' ', ' ', null, null, null, 0);

        CommandLine cmd = new DefaultParser().parse(arg.getOptions());
    }

    @Test(expected = OptionException.class)
    public void testArgumentInvalidMaximum() throws OptionException {
        Argument arg = new ArgumentImpl("arg", "test argument", 1, 0, ' ', ' ', null, null, null, 0);

        CommandLine cmd = new DefaultParser().parse(arg.getOptions());
    }

    @Test(expected = OptionException.class)
    public void testArgumentInvalidSeparator() throws OptionException {
        Argument arg = new ArgumentImpl("arg", "test argument", 1, 1, 'a', ' ', null, null, null, 0);

        CommandLine cmd = new DefaultParser().parse(arg.getOptions());
    }

    @Test
    public void testArgumentConsumeRemaining() throws OptionException {
        Argument arg = new ArgumentImpl("arg", "test argument", 1, 1, ' ', ' ', null, "--consume", null, 0);

        CommandLine cmd = new DefaultParser().parse(arg.getOptions());
        cmd.addOption(new Option("-c", "--consume", false, null, null, 1, null, null, 1, null, null));

        arg.process(cmd, cmd.iterator());
        Assert.assertEquals(2, cmd.getValues(arg).size());
        Assert.assertEquals("value1", cmd.getValues(arg).get(0));
        Assert.assertEquals("value2", cmd.getValues(arg).get(1));

        // test help
        OptionPrinter op = new HelpFormatter().printHelp("test", arg.getOptions(), true);
        Assert.assertNotNull(op);
    }
}