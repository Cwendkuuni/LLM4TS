import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.cli2.resource.ResourceConstants.OPTION_ID_HELP;
import static org.apache.commons.cli2.resource.ResourceConstants.OPTION_ID_INVALID;

public class OptionImplTest {

    @Test
    public void testOptionWithValidArguments() {
        final OptionImpl option = new MockOptionImpl(OPTION_ID_HELP, false);
        final CommandLineParser parser = new DefaultParser();
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        List<String> arguments = new ArrayList<>();
        arguments.add("--help");

        boolean parsed = option.canProcess(commandLine, arguments.listIterator());
        Assert.assertTrue("Option with valid arguments should be processed", parsed);

        option.defaults(commandLine);
        Assert.assertEquals("Help option should not have any value by default", null, commandLine.getValueForOption(option.getId()));
    }

    @Test
    public void testOptionWithInvalidArgument() {
        final OptionImpl option = new MockOptionImpl(OPTION_ID_INVALID, true);
        final CommandLineParser parser = new DefaultParser();
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        List<String> arguments = new ArrayList<>();
        arguments.add("--invalid-option");

        boolean parsed = option.canProcess(commandLine, arguments.listIterator());
        Assert.assertFalse("Option with invalid argument should not be processed", parsed);
        Assert.assertEquals("Command line should not contain the invalid option", 0, commandLine.getNumOptions());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithMissingPrefix() {
        final OptionImpl option = new MockOptionImpl(OPTION_ID_HELP, false);
        option.checkPrefixes(Arrays.asList("invalid"));
    }

    @Test
    public void testOptionWithPrefix() {
        final OptionImpl option = new MockOptionImpl(OPTION_ID_HELP, false);
        option.checkPrefixes(Arrays.asList("--", "--help"));
    }

    private static class MockOptionImpl extends OptionImpl {
        public MockOptionImpl(final int id,
                              final boolean required) {
            super(id, required);
        }

        @Override
        public void defaults(final WriteableCommandLine commandLine) {
            commandLine.setValueForOption(getId(), "defaultValue");
        }
    }
}