import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PosixParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSimpleOption() {
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option optionA = new Option("a", true, "A help message for option a");
        options.addOption(optionA);
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlattenLongOption() {
        String[] arguments = {"--long-option"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option longOption = new Option("long-option", true, "A help message for long option");
        options.addOption(longOption);
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlattenOptionWithArgument() {
        String[] arguments = {"-a", "argA"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option optionA = new Option("a", true, "A help message for option a");
        options.addOption(optionA);
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "argA"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() {
        String[] arguments = {"-x"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"-a", "--", "optionB", "-b"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option optionA = new Option("a", true, "A help message for option a");
        Option optionB = new Option("b", true, "A help message for option b");
        options.addOption(optionA);
        options.addOption(OptionBuilder.withLongOpt("optionB").hasArg().isRequired().create("optionB"));
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlattenLongTokens() {
        String[] arguments = {"--long-option-with-argument=value"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option longOption = new Option("long-option", true, "A help message for long option")
                .longOptParsers(new InputArgParser());
        options.addOption(longOption);
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--long-option-with-argument=value"}, result);
    }
}