import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.builder.ValidateOptions;
import org.apache.commons.cli2.exceptions.OptionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ArgumentImpl.class })
public class ArgumentImplTest {

    private ArgumentImpl argument;
    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp() {
        this.parser = PowerMockito.mock(CommandLineParser.class);
        this.options = new Options();
        ArgumentBuilder argBuilder = new ArgumentBuilder()
                .withLongOpt("arg")
                .withDescription("A test Argument")
                .withMinValues(1)
                .withMaxValues(3)
                .withInitialValue("default")
                .isRequired();
        this.argument = (ArgumentImpl) argBuilder.build();
        ValidateOptions.validateOptions(options, null);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() throws OptionException {
        List<String> arguments = new ArrayList<>();
        arguments.add("--arg");
        arguments.add("value1");
        arguments.add("value2");

        argument.process(this.parser.parse(options, arguments.toArray(new String[0])), arguments.listIterator());

        assertEquals(2, this.parser.getValues(argument).size());
        assertEquals("value1", this.parser.getValues(argument).get(0));
        assertEquals("value2", this.parser.getValues(argument).get(1));
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(this.parser, "value"));
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertEquals(Collections.singletonList("default"), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testValidate() throws OptionException {
        List<String> arguments = new ArrayList<>();
        arguments.add("--arg");
        arguments.add("value1");
        arguments.add("value2");
        arguments.add("value3");

        argument.validate(this.parser, argument);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValue() throws OptionException {
        List<String> arguments = new ArrayList<>();
        arguments.add("--arg");
        arguments.add("value1");
        arguments.add("value2");
        arguments.add("invalidValue");

        argument.validate(this.parser, argument);
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithConsumingOption() throws OptionException {
        List<String> arguments = new ArrayList<>();
        arguments.add("--arg");
        arguments.add("--consumingOption");
        arguments.add("value1");
        arguments.add("value2");

        argument.process(this.parser.parse(options, arguments.toArray(new String[0])), arguments.listIterator());

        assertEquals(2, this.parser.getValues(argument).size());
        assertEquals("value1", this.parser.getValues(argument).get(0));
        assertEquals("value2", this.parser.getValues(argument).get(1));

        assertEquals(0, this.parser.getValues("--consumingOption").size());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        assertEquals("  [--arg]                          A test Argument\n", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        assertEquals("A test Argument", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        HelpFormatter formatter = new HelpFormatter();

        String helpText = formatter.printHelp("test", options);

        assertTrue(helpText.contains("--arg"));
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }
}