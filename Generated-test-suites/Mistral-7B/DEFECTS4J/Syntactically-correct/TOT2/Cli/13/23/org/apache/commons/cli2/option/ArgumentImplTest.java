import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    private CommandLine commandLine;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        Argument arg = ArgumentBuilder.create("arg")
                .requiredOption()
                .withDescription("An argument test")
                .build();
        options.addOption(arg);
    }

    @Test
    public void testArgumentWithMinimumAndMaximum() throws Exception {
        DefaultParser parser = new DefaultParser();
        commandLine = parser.parse(options, new String[]{"--arg", "value1", "value2"});
        assertEquals("value1", commandLine.getOptionValue("arg", 0));
        assertEquals("value2", commandLine.getOptionValue("arg", 1));
        assertEquals(2, commandLine.getValues("arg").size());
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithMinimumButNoValues() throws Exception {
        DefaultParser parser = new DefaultParser();
        commandLine = parser.parse(options, new String[]{"--arg"});
        fail("Should have thrown an OptionException");
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithMaximumButTooManyValues() throws Exception {
        DefaultParser parser = new DefaultParser();
        commandLine = parser.parse(options, new String[]{"--arg", "value1", "value2", "value3"});
        fail("Should have thrown an OptionException");
    }

    @Test
    public void testArgumentWithDefaultValues() throws Exception {
        DefaultParser parser = new DefaultParser();
        options = new Options();
        Argument arg = ArgumentBuilder.create("arg")
                .requiredOption()
                .withDescription("An argument test")
                .withDefaultValues("defaultValue1", "defaultValue2")
                .build();
        options.addOption(arg);
        commandLine = parser.parse(options, new String[0]);
        assertEquals("defaultValue1", commandLine.getOptionValue("arg", 0));
        assertEquals("defaultValue2", commandLine.getOptionValue("arg", 1));
        assertEquals(2, commandLine.getValues("arg").size());
    }
}