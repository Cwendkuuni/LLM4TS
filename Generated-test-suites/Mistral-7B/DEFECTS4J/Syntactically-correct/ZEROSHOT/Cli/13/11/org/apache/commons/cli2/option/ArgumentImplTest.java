import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ArgumentImplTest {

    private Argument argument;
    private Parser parser;

    @Before
    public void setUp() {
        List<String> defaultValues = Arrays.asList("default1", "default2");
        argument = new ArgumentImpl("arg", "Description", 1, 5, ' ', ' ', null, "--", defaultValues, 0);
        parser = new Parser();
    }

    @After
    public void tearDown() {
        argument = null;
        parser = null;
    }

    @Test
    public void testGetName() {
        assertEquals("arg", argument.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Description", argument.getDescription());
    }

    @Test
    public void testCanProcess() {
        CommandLine commandLine = new CommandLine();
        assertFalse(argument.canProcess(commandLine, "option"));
        assertTrue(argument.canProcess(commandLine, "arg"));
    }

    @Test
    public void testProcess() throws Exception {
        CommandLine commandLine = new CommandLine();

        List<Object> args = Arrays.asList("arg1", "arg2", "arg3");
        commandLine.parse(parser.parse(parser.parseArguments(Arrays.asList("appName", "--arg", "arg1", "arg2", "arg3"))));

        argument.process(commandLine, commandLine.iterator());

        List<String> values = commandLine.getValues(argument);
        assertEquals(3, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));
        assertEquals("arg3", values.get(2));
    }

    @Test
    public void testProcessValues() throws Exception {
        CommandLine commandLine = new CommandLine();

        List<Object> args = Arrays.asList("--arg", "arg1", "--", "arg3", "--arg", "arg4");
        commandLine.parse(parser.parse(parser.parseArguments(Arrays.asList("appName", "--arg", "--", "arg3"))));

        argument.processValues(commandLine, commandLine.iterator(), argument);

        List<String> values = commandLine.getValues(argument);
        assertEquals(4, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg3", values.get(1));
        assertEquals("arg4", values.get(2));
        assertNull(values.get(3));
    }

    @Test
    public void testProcessValuesConsumeRemaining() throws Exception {
        CommandLine commandLine = new CommandLine();

        List<Object> args = Arrays.asList("--arg", "--consume-remaining", "arg1", "arg2", "arg3");
        commandLine.parse(parser.parse(parser.parseArguments(Arrays.asList("appName", "--arg"))));

        argument.processValues(commandLine, commandLine.iterator(), argument);

        List<String> values = commandLine.getValues(argument);
        assertEquals(3, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));
        assertEquals("arg3", values.get(2));

        List<Object> remainingArgs = commandLine.getValues(OptionBuilder.noArg().isRequired(false).create("no-arg"));
        assertEquals(2, remainingArgs.size());
        assertEquals("arg1", remainingArgs.get(0));
        assertEquals("arg2", remainingArgs.get(1));
    }

    @Test
    public void testProcessValuesWithSeparator() throws Exception {
        CommandLine commandLine = new CommandLine();

        List<Object> args = Arrays.asList("--arg", "arg1", "--sep", "arg2", "--arg", "arg3,arg4");
        commandLine.parse(parser.parse(parser.parseArguments(Arrays.asList("appName", "--arg"))));

        argument.processValues(commandLine, commandLine.iterator(), argument);

        List<String> values = commandLine.getValues(argument);
        assertEquals(4, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));
        assertEquals("arg3,arg4", values.get(2));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithTooManyValues() throws Exception {
        CommandLine commandLine = new CommandLine();

        List<Object> args = Arrays.asList("--arg", "arg1", "--sep", "arg2", "--arg", "arg3", "arg4");
        commandLine.parse(parser.parse(parser.parseArguments(Arrays.asList("appName", "--arg"))));

        argument.processValues(commandLine, commandLine.iterator(), argument);

        List<String> values = commandLine.getValues(argument);
        assertEquals(4, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));
        assertEquals("arg3,arg4", values.get(2));

        // Should throw an exception
        values.get(3);
    }

    @Test
    public void testProcessValuesWithTooFewValues() throws Exception {
        CommandLine commandLine = new CommandLine();

        List<Object> args = Arrays.asList("--arg", "arg1");
        commandLine.parse(parser.parse(parser.parseArguments(Arrays.asList("appName", "--arg"))));

        argument.processValues(commandLine, commandLine.iterator(), argument);

        List<String> values = commandLine.getValues(argument);
        assertEquals(1, values.size());
        assertEquals("arg1", values.get(0));

        List<String> remainingArgs = commandLine.getValues(OptionBuilder.noArg().isRequired(false).create("no-arg"));
        assertEquals(0, remainingArgs.size());
    }

    @Test
    public void testValidate() throws Exception {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(parser.parse(parser.parseArguments(Arrays.asList("appName", "--arg"))));

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewValues() throws Exception {
        CommandLine commandLine = new CommandLine();
        commandLine.parse(parser.parse(parser.parseArguments(Arrays.asList("appName"))));

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyValues() throws Exception {
        CommandLine commandLine = new CommandLine();
        List<Object> args = Arrays.asList("--arg", "arg1", "--sep", "arg2", "--arg", "arg3", "arg4");
        commandLine.parse(parser.parse(parser.parseArguments(Arrays.asList("appName", "--arg"))));

        argument.validate(commandLine);
    }

    @Test
    public void testValidateWithInvalidValue() throws Exception {
        CommandLine commandLine = new CommandLine();
        List<Object> args = Arrays.asList("--arg", "arg1", "--sep", "arg2", "--arg", "invalid");
        commandLine.parse(parser.parse(parser.parseArguments(Arrays.asList("appName", "--arg"))));

        argument.validate(commandLine);
    }

    @Test
    public void testValidateWithInvalidValues() throws Exception {
        CommandLine commandLine = new CommandLine();
        List<Object> args = Arrays.asList("--arg", "arg1", "--sep", "arg2", "--arg", "invalid1", "invalid2");
        commandLine.parse(parser.parse(parser.parseArguments(Arrays.asList("appName", "--arg"))));

        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        String expected = "  arg:";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testAppendUsageWithOptions() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);
        buffer.append(" [--consume-remaining]");

        String expected = "  arg:";
        assertEquals(expected, buffer.substring(0, buffer.length() - 15));
    }

    @Test
    public void testAppendUsageWithValues() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);
        buffer.append(" [--consume-remaining]");
        buffer.append(" <arg1> [<arg2> ...]");

        String expected = "  arg: <arg1> [<arg2> ...]";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testAppendUsageWithValuesAndOptions() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);
        buffer.append(" [--consume-remaining]");
        buffer.append(" <arg1> [<arg2> ...] [--opt] [--opt-arg]");

        String expected = "  arg: <arg1> [<arg2> ...] [--opt] [--opt-arg]";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() throws Exception {
        HelpFormatter formatter = new HelpFormatter();

        List<HelpLine> helpLines = argument.helpLines(0, null, null);
        formatter.printHelp("appName", helpLines.toArray(new HelpLine[0]));

        String expected = "  arg: <arg1> [<arg2> ...]";
        assertTrue(formatter.getOutput().contains(expected));
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(5, argument.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testGetDefaultValues() {
        assertEquals(2, argument.getDefaultValues().size());
        assertEquals("default1", argument.getDefaultValues().get(0));
        assertEquals("default2", argument.getDefaultValues().get(1));
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }
}