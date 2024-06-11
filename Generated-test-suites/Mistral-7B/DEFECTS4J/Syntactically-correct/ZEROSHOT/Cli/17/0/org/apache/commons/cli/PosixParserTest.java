import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", 2, "Option C with a string argument");
        options.addOption("d", 3, "Option D with an optional integer argument");
    }

    @Test
    public void testFlatten_EmptyArgs() throws ParseException {
        String[] args = {};
        String[] expected = {};
        String[] actual = PosixParser.parse(options, args);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_SingleOption() throws ParseException {
        String[] args = {"-a"};
        String[] expected = {"-a"};
        String[] actual = PosixParser.parse(options, args);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_SingleValueOption() throws ParseException {
        String[] args = {"-a", "value"};
        String[] expected = {"-a", "value"};
        String[] actual = PosixParser.parse(options, args);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_OptionWithArgument() throws ParseException {
        String[] args = {"-b", "1", "arg"};
        String[] expected = {"-b", "1", "arg"};
        String[] actual = PosixParser.parse(options, args);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_OptionWithLongArgument() throws ParseException {
        String[] args = {"-c", "long argument"};
        String[] expected = {"-c", "long argument"};
        String[] actual = PosixParser.parse(options, args);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_MultipleOptions() throws ParseException {
        String[] args = {"-a", "-b", "1"};
        String[] expected = {"-a", "-b", "1"};
        String[] actual = PosixParser.parse(options, args);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_LongOption() throws ParseException {
        String[] args = {"--option-c", "value"};
        String[] expected = {"--option-c", "value"};
        String[] actual = PosixParser.parse(options, args);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_OptionWithOptionalArgument() throws ParseException {
        String[] args = {"-d", "3", "arg"};
        String[] expected = {"-d", "3", "arg"};
        String[] actual = PosixParser.parse(options, args);
        Assert.assertArrayEquals(expected, actual);

        String[] argsWithoutArgument = {"-d", "3"};
        String[] expectedWithoutArgument = {"-d", "3"};
        String[] actualWithoutArgument = PosixParser.parse(options, argsWithoutArgument);
        Assert.assertArrayEquals(expectedWithoutArgument, actualWithoutArgument);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() throws ParseException {
        String[] args = {"-z"};
        PosixParser.parse(options, args);
    }

    @Test
    public void testFlatten_InvalidOptionWithArguments() throws ParseException {
        String[] args = {"-z", "arg"};
        String[] expected = {"--", "z", "arg"};
        String[] actual = PosixParser.parse(options, args);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_MixedOptions() throws ParseException {
        String[] args = {"-a", "-b", "--option-c", "value", "-d", "3", "arg"};
        String[] expected = {"-a", "-b", "--option-c", "value", "-d", "3", "arg"};
        String[] actual = PosixParser.parse(options, args);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_StopAtNonOption() throws ParseException {
        String[] args = {"-a", "-b", "1", "-", "c", "d"};
        String[] expected = {"-a", "-b", "1", "-", "c", "d"};
        String[] actual = PosixParser.parse(options, args, true);
        Assert.assertArrayEquals(expected, actual);

        String[] argsWithoutStopAtNonOption = {"-a", "-b", "1", "c", "d"};
        String[] expectedWithoutStopAtNonOption = {"-a", "-b", "1", "c", "d"};
        String[] actualWithoutStopAtNonOption = PosixParser.parse(options, argsWithoutStopAtNonOption);
        Assert.assertArrayEquals(expectedWithoutStopAtNonOption, actualWithoutStopAtNonOption);
    }

    @Test
    public void testFlatten_BurstOption() throws ParseException {
        String[] args = {"--option-c--value"};
        String[] expected = {"--option-c", "--value"};
        String[] actual = PosixParser.parse(options, args, false);
        Assert.assertArrayEquals(expected, actual);

        String[] argsWithStopAtNonOption = {"--option-c--value"};
        String[] expectedWithStopAtNonOption = {"--option-c--value"};
        String[] actualWithStopAtNonOption = PosixParser.parse(options, argsWithStopAtNonOption, true);
        Assert.assertArrayEquals(expectedWithStopAtNonOption, actualWithStopAtNonOption);
    }

    @Test
    public void testFlatten_DoubleDash() throws ParseException {
        String[] args = {"--"};
        String[] expected = {"--"};
        String[] actual = PosixParser.parse(options, args);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlatten_DoubleDashWithArguments() throws ParseException {
        String[] args = {"--option", "arg1", "arg2"};
        String[] expected = {"--option", "arg1", "arg2"};
        String[] actual = PosixParser.parse(options, args);
        Assert.assertArrayEquals(expected, actual);
    }

    private static String[] parse(Options options, String[] args, boolean stopAtNonOption) {
        return PosixParser.parse(options, args, stopAtNonOption, new String[0]);
    }
}

class PosixParser {

    public static String[] parse(Options options, String[] args, boolean stopAtNonOption, String[] expected) {
        List<String> tokens = new ArrayList<>();
        PosixParser parser = new PosixParser();
        String[] flatArgs = parser.flatten(options, args, stopAtNonOption);

        Assert.assertEquals(expected.length, tokens.size());
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], tokens.get(i));
        }

        return (String[]) tokens.toArray(new String[tokens.size()]);
    }
}