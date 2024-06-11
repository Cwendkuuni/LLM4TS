import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionImplTest {

    private Option option;
    private CommandLineParser commandLineParser;
    private Options options;
    private ResourceHelper resourceHelper;

    @Before
    public void setup() {
        option = new OptionImpl(1, "testOption", true, "t") {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
                return true;
            }

            @Override
            public String toString() {
                return "Test Option";
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setValue(getId(), false);
            }
        };

        commandLineParser = new DefaultParser();
        options = new Options();
        options.addOption(option);
        resourceHelper = ResourceHelper.getResourceHelper();
    }

    @Test
    public void testOptionWithValue() {
        final WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.parse(options, new String[]{"-t"});

        assertEquals(true, commandLine.hasOption(option.getId()));
        assertEquals("t", commandLine.getOptionValue(option.getId()));
    }

    @Test
    public void testOptionWithoutValue() {
        final WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.parse(options, new String[]{"-t"});

        assertEquals(true, commandLine.hasOption(option.getId()));
        assertNull(commandLine.getOptionValue(option.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithoutPrefix() {
        final WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.parse(options, new String[]{"testOption"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithInvalidPrefix() {
        final WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.parse(options, new String[]{"--testOption"});
    }

    @Test
    public void testGetOption() {
        final WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.parse(options, new String[]{"-t"});

        assertEquals(option, commandLine.getOption(option.getId()));
    }

    @Test
    public void testOptionEquals() {
        final Option option1 = new OptionImpl(1, "testOption1", true, "t1");
        final Option option2 = new OptionImpl(1, "testOption2", true, "t2");
        final Option option3 = new OptionImpl(1, "testOption", true, "t");

        assertEquals(option1, option1);
        assertEquals(option1, option1);
        assertEquals(option1, option3);
        assertNotEquals(option1, new Object());
        assertNotEquals(option1, option2);
    }

    @Test
    public void testOptionHashCode() {
        final Option option1 = new OptionImpl(1, "testOption1", true, "t1");
        final Option option2 = new OptionImpl(1, "testOption2", true, "t2");
        final Option option3 = new OptionImpl(1, "testOption", true, "t");

        assertEquals(option1.hashCode(), option1.hashCode());
        assertEquals(option1.hashCode(), option1.hashCode());
        assertEquals(option1.hashCode(), option3.hashCode());
        assertNotEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), new Object().hashCode());
    }
}