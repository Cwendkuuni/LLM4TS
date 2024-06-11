import org.apache.commons.cli2. Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
public class ArgumentImplTest {

    private Argument argument;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        commandLine = Mockito.mock(WriteableCommandLine.class);

        List defaultValues = new ArrayList<>();
        defaultValues.add("test1");
        defaultValues.add("test2");

        argument = new ArgumentImpl("testArgument",
                                    "A test argument",
                                    1,
                                    1,
                                    ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                                    ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR,
                                    Mockito.mock(Validator.class),
                                    ArgumentImpl.DEFAULT_CONSUME_REMAINING,
                                    defaultValues,
                                    0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("testArgument", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() {
        String[] args = {"--testArgument", "test1", "test2"};

        PowerMockito.when(commandLine.getParsedArgs()).thenReturn(args);

        argument.process(commandLine, Arrays.asList(commandLine.getParsedArgs().list().iterator()).listIterator());

        List<String> values = commandLine.getValues(argument);

        assertEquals(2, values.size());
        assertEquals("test1", values.get(0));
        assertEquals("test2", values.get(1));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesInvalidNumberOfArguments() {
        String[] args = {"--testArgument", "test1", "test2", "test3"};

        PowerMockito.when(commandLine.getParsedArgs()).thenReturn(Arrays.asList(args));

        argument.process(commandLine, Arrays.asList(commandLine.getParsedArgs().list().iterator()).listIterator());
    }

    @Test
    public void testCanProcess() {
        String arg = "test";

        assertTrue(argument.canProcess(commandLine, arg));
    }

    @Test
    public void testGetTriggers() {
        Set triggers = argument.getTriggers();

        assertNotNull(triggers);
        assertEquals(Collections.EMPTY_SET, triggers);
    }

    @Test
    public void testProcess() {
        String[] args = {"--testArgument", "test1", "test2"};

        PowerMockito.when(commandLine.getParsedArgs()).thenReturn(Arrays.asList(args));

        argument.process(commandLine, Arrays.asList(commandLine.getParsedArgs().list().iterator()).listIterator());

        assertNotNull(commandLine.getValues(argument));
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals(ArgumentImpl.DEFAULT_CONSUME_REMAINING, argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertNotNull(argument.getDefaultValues());
        assertEquals(2, argument.getDefaultValues().size());
        assertEquals("test1", argument.getDefaultValues().get(0));
        assertEquals("test2", argument.getDefaultValues().get(1));
    }

    @Test
    public void testGetValidator() {
        assertNotNull(argument.getValidator());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValues() {
        String[] args = {"--testArgument", "test1", "test2", "invalidValue"};

        PowerMockito.when(commandLine.getParsedArgs()).thenReturn(Arrays.asList(args));

        argument.validate(commandLine);
    }

    @Test
    public void testValidateWithValidValues() {
        String[] args = {"--testArgument", "test1", "test2"};

        PowerMockito.when(commandLine.getParsedArgs()).thenReturn(Arrays.asList(args));

        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, Collections.EMPTY_SET, null);

        assertEquals("--testArgument <arg> ", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        assertEquals("A test argument", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        HelpLine helpLine = argument.helpLines(1, Collections.EMPTY_SET, null).get(0);

        assertEquals("--testArgument <arg> ", helpLine.getOptionSpec());
        assertEquals("", helpLine.getLongOptionSpec());
        assertEquals("A test argument\n", helpLine.getDescription());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }
}