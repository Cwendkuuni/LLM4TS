import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.test.TestHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArgumentImplTest {

    private Argument argument;
    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
        argument = new ArgumentImpl("myArg", "A test argument", 1, Integer.MAX_VALUE, 'a', 'b', null, null, null, 0);
    }

    @Test
    public void testGetName() {
        assertEquals("myArg", argument.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("A test argument", argument.getDescription());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(Integer.MAX_VALUE, argument.getMaximum());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals('a', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals('b', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetSubsequentSplit() {
        assertTrue(argument.getSubsequentSplit());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertNull(argument.getConsumeRemaining());
    }

    @Test
    public void testgetDefaultValues() {
        assertNotNull(argument.getDefaultValues());
    }

    @Test
    public void testProcessValues() {
        String[] args = {"--myArg", "arg1", "--myArg", "arg2", "arg3"};
        argument.process(commandLine, TestHelper.getListIterator(args));

        assertEquals(3, commandLine.getValues(argument).size());
        assertEquals("arg1", commandLine.getValues(argument).get(0));
        assertEquals("arg2", commandLine.getValues(argument).get(1));
        assertEquals("arg3", commandLine.getValues(argument).get(2));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithTooManyArguments() {
        String[] args = {"--myArg", "arg1", "arg2", "arg3", "arg4"};
        argument.process(commandLine, TestHelper.getListIterator(args));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithInvalidArgument() {
        String[] args = {"--myArg", "arg1", "--myArg", "arg2", "invalid"};
        argument.process(commandLine, TestHelper.getListIterator(args));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithConsumingOption() {
        argument = new ArgumentImpl("consumingOption", "A test consuming option", 0, Integer.MAX_VALUE, 'c', 'c', null, null, null, 0);
        String[] args = {"--consumingOption", "--myArg", "arg1"};
        argument.process(commandLine, TestHelper.getListIterator(args));

        assertEquals(1, commandLine.getValues(argument).size());
        assertEquals("arg1", commandLine.getValues(argument).get(0));

        String[] args2 = {"--myArg", "--consumingOption", "arg1"};
        argument.process(commandLine, TestHelper.getListIterator(args2));

        assertEquals(1, commandLine.getValues(argument).size());
        assertEquals("arg1", commandLine.getValues(argument).get(0));
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "arg1"));
    }

    @Test
    public void testAppendUsage() {
        HelpFormatter formatter = new HelpFormatter();
        String usage = formatter.toLongHelpText(new OptionBuilder()
                .withLongOpt("myArg")
                .withValueSeparator('a')
                .withArgName("arg")
                .withDescription("A test argument")
                .create(), null);

        String expectedUsage = "  --myArg Arg_arg\n" +
                "                 [arg]\n" +
                "                 [arg ...]\n";

        assertEquals(expectedUsage, usage);
    }

    @Test
    public void testValidate() {
        List values = Arrays.asList("arg1", "arg2", "arg3");
        argument.validate(commandLine);
        argument.validate(commandLine, argument);

        List expectedValues = Arrays.asList("arg1", "arg2", "arg3");
        assertEquals(expectedValues, commandLine.getValues(argument));
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidArgument() {
        String[] args = {"--myArg", "arg1", "invalid"};
        argument.validate(commandLine, argument);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyArguments() {
        String[] args = {"--myArg", "arg1", "arg2", "arg3"};
        argument.validate(commandLine, argument);
    }

    @Test
    public void testValidateWithEmptyValues() {
        List values = Collections.emptyList();
        argument.validate(commandLine, argument);
        assertEquals(Collections.emptyList(), commandLine.getValues(argument));
    }

    @Test
    public void testValidateWithNullValues() {
        List values = null;
        argument.validate(commandLine, argument);
        assertEquals(Collections.emptyList(), commandLine.getValues(argument));
    }

    @Test
    public void testValidateWithDefaultValues() {
        argument = new ArgumentImpl("myArg", "A test argument", 1, Integer.MAX_VALUE, 'a', 'b', null, null, Arrays.asList("default1", "default2"), 0);
        List values = Arrays.asList("arg1", "arg2");
        commandLine.setValues(argument, values);
        argument.validate(commandLine, argument);

        List expectedValues = Arrays.asList("default1", "default2", "arg1", "arg2");
        assertEquals(expectedValues, commandLine.getValues(argument));
    }

    @Test
    public void testValidateWithDefaultValuesAndConsumingOption() {
        argument = new ArgumentImpl("myArg", "A test argument", 1, Integer.MAX_VALUE, 'a', 'b', null, null, null, 0);
        String[] args = {"--myArg", "--consumingOption", "arg1"};
        argument.process(commandLine, TestHelper.getListIterator(args));

        assertEquals(1, commandLine.getValues(argument).size());
        assertEquals("arg1", commandLine.getValues(argument).get(0));

        List expectedValues = Arrays.asList("arg1");
        argument.validate(commandLine, argument);
        assertEquals(expectedValues, commandLine.getValues(argument));
    }
}