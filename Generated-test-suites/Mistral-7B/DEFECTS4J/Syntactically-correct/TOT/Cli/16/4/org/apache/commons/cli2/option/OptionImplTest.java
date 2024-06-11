import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testOptionImpl() {
        final Options options = new Options();
        options.addOption(new OptionImpl(1, "input", true, "Input file")
                .required(true)
                .desc("specify input file")
                .setValueSeparator(':')
                .valuesSeparator(';')
                .prefix("i")
                .triggers("input")
                .validate(() -> {
                    String value = (String) Option.getValue();
                    Assert.assertNotNull(value);
                    Assert.assertTrue(value.length() > 0);
                })
                .help("specify input file"));

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine commandLine = new DefaultParseResult();

        try {
            parser.parse(options, new String[]{"-i", "/path/to/input.txt"});
            Assert.assertEquals("/path/to/input.txt", Option.getValue());
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        // test canProcess method
        Assert.assertTrue(options.getOption(1).canProcess(commandLine, options.iterator()));

        // test equals method
        Option option1 = new OptionImpl(1, "input", true, "Input file");
        Option option2 = new OptionImpl(1, "input", true, "Input file");
        Assert.assertEquals(option1, option2);

        // test hashCode method
        Option option3 = new OptionImpl(2, "input", true, "Input file");
        Assert.assertNotEquals(option1.hashCode(), option3.hashCode());

        // test findOption method with correct trigger
        Option foundOption = options.findOption("input");
        Assert.assertEquals(option1, foundOption);

        // test findOption method with incorrect trigger
        Option notFoundOption = options.findOption("unknown");
        Assert.assertNull(notFoundOption);

        // test isRequired method
        Assert.assertTrue(options.getOption(1).isRequired());
    }

    @Test
    public void testOptionImplWithInvalidPrefixes() {
        final Options options = new Options();
        options.addOption(new OptionImpl(1, "input", true, "Input file")
                .required(true)
                .desc("specify input file")
                .prefix("invalid")
                .triggers("input")
                .help("specify input file"));

        try {
            new DefaultParser().parse(options, new String[0]);
            Assert.fail("ParseException expected");
        } catch (ParseException e) {
            String expectedMessage =
                    "Error parsing command line: The prefix 'invalid' does not fit any of the triggers: [input]";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testOptionImplWithEmptyPrefixes() {
        final Options options = new Options();
        options.addOption(new OptionImpl(1, "input", true, "Input file")
                .required(true)
                .desc("specify input file")
                .prefixes()
                .triggers("input")
                .help("specify input file"));

        try {
            new DefaultParser().parse(options, new String[0]);
            Assert.fail("ParseException expected");
        } catch (ParseException e) {
            String expectedMessage =
                    "Error parsing command line: At least one prefix is required for the option: [input]";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testOptionImplWithEmptyTrigger() {
        final Options options = new Options();
        options.addOption(new OptionImpl(1, null, true, "Input file")
                .required(true)
                .desc("specify input file")
                .prefix("i")
                .help("specify input file"));

        try {
            new DefaultParser().parse(options, new String[0]);
            Assert.fail("ParseException expected");
        } catch (ParseException e) {
            String expectedMessage =
                    "Error parsing command line: Each option must have at least one trigger";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }
}