import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.commandline.Arg;
import org.apache.commons.cli2.commandline.OptionBuilder;
import org.apache.commons.cli2.commandline.OptionsParser;
import org.apache.commons.cli2.examples.GroupOptionsExample;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupImpl() throws ParseException {
        // Given
        final String[] args = new String[]{"-g", "A", "--intValue=1", "--longValue=2"};
        final Options options = new Options();
        options.addGroup(new OptionBuilder()
                .longOpt("groupA")
                .hasArg(true)
                .isRequired(true)
                .valuesSeparator('=')
                .build()
                .addOption(new Arg(0).longOpt("intValue")
                        .required(true)
                        .intValParser(Integer::parseInt)
                        .build())
                .addOption(new Arg(1).longOpt("longValue")
                        .required(true)
                        .longValParser(Long::parseLong)
                        .build()));

        final CommandLineParser parser = new CommandLineParser();
        final WriteableCommandLine commandLine = new WriteableCommandLine();

        // When
        parser.parse(options, args, true, commandLine);

        // Then
        Assert.assertEquals("groupA", commandLine.getGroupName());
        Assert.assertEquals(1, commandLine.getValuesForOption("intValue").size());
        Assert.assertEquals(1, commandLine.getValuesForOption("intValue").get(0));
        Assert.assertEquals(2, commandLine.getValuesForOption("longValue").size());
        Assert.assertEquals(2L, commandLine.getValuesForOption("longValue").get(0).longValue());
        Assert.assertEquals(2L, commandLine.getValuesForOption("longValue").get(1).longValue());

        // Given
        final String[] argsEmpty = {};
        final String[] argsError = new String[]{"-g", "A", "--intValue=abc"};

        // When - Empty
        try {
            parser.parse(options, argsEmpty, true, commandLine);
            Assert.fail();
        } catch (ParseException e) {
            // Then
            Assert.assertEquals("Missing required option: groupA", e.getMessage());
        }

        // When - Invalid value
        try {
            parser.parse(options, argsError, true, commandLine);
            Assert.fail();
        } catch (ParseException e) {
            // Then
            Assert.assertEquals("Invalid value for option: 'intValue': value = [abc]", e.getMessage());
        }

        // Given
        final Options optionsEmpty = new Options();

        // When - Empty options
        try {
            parser.parse(optionsEmpty, args, true, commandLine);
            Assert.fail();
        } catch (ParseException e) {
            // Then
            Assert.assertEquals("No option 'groupA' found", e.getMessage());
        }

        // Help
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options, true);
        formatter.printHelp("Test", options, true, "arguments", true);
    }
}