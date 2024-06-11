import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    // Test for withLongOpt method
    @Test
    public void testWithLongOpt() throws Exception {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionName");
        Option option = optionBuilder.create();
        assertEquals("optionName", option.getLongOpt());
    }

    // Test for hasArg method with one argument
    @Test
    public void testHasArgOneArg() throws Exception {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);

        String[] args = new String[]{"arg1"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(optionBuilder.parse(args));
        assertEquals("arg1", cmd.getOptionValue("optionName"));
    }

    // Test for hasArg method with no argument
    @Test
    public void testHasArgNoArg() throws Exception {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(optionBuilder.parse(new String[0]));
        assertNull(cmd.getOptionValue("optionName"));
    }

    // Test for hasOptionalArg method
    @Test
    public void testHasOptionalArg() throws Exception {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);

        String[] args = new String[]{"arg1"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(optionBuilder.parse(args));
        assertEquals("arg1", cmd.getOptionValue("optionName"));

        args = new String[0];
        cmd = parser.parse(optionBuilder.parse(args));
        assertNull(cmd.getOptionValue("optionName"));
    }

    // Test for isRequired method
    @Test(expected = IllegalArgumentException.class)
    public void testIsRequired() throws Exception {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertFalse(option.isRequired());

        String[] args = new String[0];
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(optionBuilder.parse(args));
        assertNull(cmd.getOptionValue("optionName"));

        throw new IllegalArgumentException("Test for required option with no argument");
    }

    // Test for withValueSeparator method
    @Test
    public void testWithValueSeparator() throws Exception {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        assertEquals(':', option.getValueSeparator());

        String[] args = new String[]{"optionName", "value1:value2"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(optionBuilder.parse(args));
        assertEquals("value1:value2", cmd.getOptionValue("optionName"));
    }
}