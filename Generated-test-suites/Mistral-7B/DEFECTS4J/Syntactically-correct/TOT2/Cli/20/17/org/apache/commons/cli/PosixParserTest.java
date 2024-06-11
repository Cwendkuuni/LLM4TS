import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenWithEmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];
        Options options = new Options();
        String[] expected = new String[0];
        String[] actual = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithValidOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--foo", "--bar=value", "-x", "-y=value"};
        Options options = new Options();
        options.addOption("foo", true, false, "A boolean option");
        options.addOption("bar", true, true, "An option with an argument");
        options.addOption("x", false, false, "An option without an argument");
        String[] expected = {"--foo", "--bar", "value", "-x", "-y", "value"};
        String[] actual = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--foo", "--bar=value", "-x", "-y=value", "-z"};
        Options options = new Options();
        options.addOption("foo", true, false, "A boolean option");
        options.addOption("bar", true, true, "An option with an argument");
        options.addOption("x", false, false, "An option without an argument");
        String[] expected = {"--foo", "--bar=value", "-x", "-y=value"};
        String[] actual = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--invalid", "-x", "-y", "-z"};
        Options options = new Options();
        options.addOption("foo", true, false, "A boolean option");
        options.addOption("bar", true, true, "An option with an argument");
        options.addOption("x", false, false, "An option without an argument");
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidLongOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"----not-valid", "--foo", "-x", "-y"};
        Options options = new Options();
        options.addOption("foo", true, false, "A boolean option");
        options.addOption("bar", true, true, "An option with an argument");
        options.addOption("x", false, false, "An option without an argument");
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidShortOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--foo", "--bar=value", "A", "-y", "-z"};
        Options options = new Options();
        options.addOption("foo", true, false, "A boolean option");
        options.addOption("bar", true, true, "An option with an argument");
        options.addOption("x", false, false, "An option without an argument");
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testProcessLongOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--foo=value", "--bar=value2"};
        Options options = new Options();
        options.addOption("foo", true, true, "An option with an argument");
        options.addOption("bar", true, true, "An option with an argument");
        List expected = Arrays.asList("--foo", "value", "--bar", "value2");
        List actual = parser.parse(arguments, options);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testProcessShortOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "value", "-b", "value2"};
        Options options = new Options();
        options.addOption("f", true, true, "An option with an argument");
        options.addOption("b", true, true, "An option with an argument");
        List expected = Arrays.asList("-f", "value", "-b", "value2");
        List actual = parser.parse(arguments, options);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testProcessShortOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "value"};
        Options options = new Options();
        options.addOption("f", true, false, "An option without an argument");
        options.addOption("foo", true, true, "An option with an argument");
        List expected = Arrays.asList("-f", "value");
        List actual = parser.parse(arguments, options);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testProcessInvalidOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"--invalid"};
        Options options = new Options();
        options.addOption("foo", true, false, "An option without an argument");
        parser.parse(arguments, options);
    }

    @Test(expected = ParseException.class)
    public void testProcessInvalidLongOption() {
        PosixParser parser = new PosixParser();
        String[] arguments = {"----invalid"};
        Options options = new Options();
        options.addOption("foo", true, false, "An option without an argument");
        parser.parse(arguments, options);
    }
}