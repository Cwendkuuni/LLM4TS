import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testOptionWithNoPrefixes() {
        final Options options = new Options();
        final Option option = OptionBuilder.withLongOpt("noPrefixOption")
                .withDescription("An option with no prefixes")
                .create(Option.class);
        options.addOption(option);

        final CommandLineParser parser = new DefaultParser();
        WriteableCommandLine commandLine = new DefaultCommandLine();

        try {
            parser.parse(options, null, null, new String[]{"--noPrefixOption"});
            Assert.assertNull(commandLine.getUnknownOptions().get(0));
            Assert.assertTrue(commandLine.hasOption("noPrefixOption"));
            Assert.assertFalse(commandLine.hasOption("unknownOption"));
        } catch (ParseException e) {
            Assert.fail("Parsing should not have thrown an exception.");
        }
    }

    @Test
    public void testOptionWithPrefixes() {
        final Options options = new Options();
        final Option option = OptionBuilder.withLongOpt("prefixOption")
                .withDescription("An option with prefixes")
                .create(Option.class);
        final String prefix = "my";
        option.setPrefix(prefix);

        options.addOption(option);

        final CommandLineParser parser = new DefaultParser();
        WriteableCommandLine commandLine = new DefaultCommandLine();

        try {
            parser.parse(options, null, null, new String[]{"--myOption"});
            Assert.assertNull(commandLine.getUnknownOptions().get(0));
            Assert.assertTrue(commandLine.hasOption("prefixOption"));
            Assert.assertFalse(commandLine.hasOption("unknownOption"));
        } catch (ParseException e) {
            Assert.fail("Parsing should not have thrown an exception.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithoutPrefix() {
        final Options options = new Options();
        final Option option = OptionBuilder.withLongOpt("noPrefixOption")
                .withDescription("An option without prefix")
                .create(Option.class);
        final Set<String> prefixes = options.getOption("noPrefixOption").getPrefixes();
        prefixes.add("prefix");

        try {
            option.checkPrefixes(prefixes);
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }
}