import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.apache.commons.cli2.resource.ResourceConstants.OPTION_CLASS_NAME;
import static org.apache.commons.cli2.resource.ResourceConstants.OPTION_PREFIX_SHORT;
import static org.apache.commons.cli2.resource.ResourceConstants.OPTION_PREFIX_LONG;

public class OptionImplTest {

    @Test
    public void testOptionImpl() {
        final OptionImpl option = new OptionImpl(1, true, "o", "opt", "Option test", "This is an option test") {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
                return true;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setOptionValue(this.getName(), "defaultValue");
            }

            @Override
            public void validate(WriteableCommandLine commandLine) {
                // no validation logic here
            }
        };

        final CommandLineParser parser = new DefaultParser();
        final WriteableCommandLine cmdLine = new WriteableCommandLine();
        final HelpFormatter formatter = new HelpFormatter();

        // Test with valid arguments
        final String[] argsValid = {"-o", "validValue"};
        final List<Option> options = Arrays.asList(option);

        parser.parse(options.toArray(new Option[0]), cmdLine, argsValid, true);
        Assert.assertEquals("validValue", cmdLine.getOptionValue(option.getName()));

        // Test with no arguments
        final String[] argsNone = {};
        parser.parse(options.toArray(new Option[0]), cmdLine, argsNone, true);
        Assert.assertEquals("defaultValue", cmdLine.getOptionValue(option.getName()));

        // Test with invalid arguments
        final String[] argsInvalid = {"--invalid"};
        try {
            parser.parse(options.toArray(new Option[0]), cmdLine, argsInvalid, true);
            Assert.fail("An IllegalArgumentException should have been thrown");
        } catch (final IllegalArgumentException e) {
            // expected
        }

        // Test help
        final String[] argsHelp = {"--help"};
        final String help = formatter.printHelp(OPTION_CLASS_NAME, Arrays.asList(option.toString().split("\n")), true);
        Assert.assertEquals(help, formatter.printHelp(OPTION_CLASS_NAME, Arrays.asList(option.toString().split("\n")), true));
    }

    // Prefixes tests
    @Test(expected = IllegalArgumentException.class)
    public void testOptionImplPrefixes() {
        final OptionImpl option = new OptionImpl(1, true, null, null, "Option test", "This is an option test") {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
                return true;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setOptionValue(this.getName(), "defaultValue");
            }

            @Override
            public void validate(WriteableCommandLine commandLine) {
                // no validation logic here
            }
        };

        final Set<String> prefixes = Set.of("prefix1", "prefix2");
        option.checkPrefixes(prefixes);

        final String[] argsInvalidPrefix = {"prefix1", "option"};
        final String message = "Option 'option' does not start with a valid prefix. Supported prefixes: [prefix1, prefix2]";
        try {
            final CommandLineParser parser = new DefaultParser();
            final WriteableCommandLine cmdLine = new WriteableCommandLine();
            parser.parse(new Option[]{option}, cmdLine, Arrays.asList(argsInvalidPrefix.clone()), true);
            Assert.fail(message);
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }

    // Triggers tests
    @Test
    public void testOptionImplTriggers() {
        final OptionImpl option = new OptionImpl(1, true, "o", "opt", "Option test", "This is an option test") {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator<String> arguments) {
                return true;
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setOptionValue(this.getName(), "defaultValue");
            }

            @Override
            public void validate(WriteableCommandLine commandLine) {
                // no validation logic here
            }
        };

        final List<String> triggers = Arrays.asList("t1", "t2", "o");
        option.setTriggers(triggers);

        final String[] argsTrigger1 = {"--t1", "value1"};
        final WriteableCommandLine cmdLineTrigger1 = new WriteableCommandLine();
        final CommandLineParser parserTrigger1 = new DefaultParser();
        parserTrigger1.parse(new Option[]{option}, cmdLineTrigger1, argsTrigger1, true);
        Assert.assertEquals("value1", cmdLineTrigger1.getOptionValue(option.getName()));

        final String[] argsTrigger2 = {"--o", "value2"};
        final WriteableCommandLine cmdLineTrigger2 = new WriteableCommandLine();
        final CommandLineParser parserTrigger2 = new DefaultParser();
        parserTrigger2.parse(new Option[]{option}, cmdLineTrigger2, argsTrigger2, true);
        Assert.assertEquals("value2", cmdLineTrigger2.getOptionValue(option.getName()));
    }
}