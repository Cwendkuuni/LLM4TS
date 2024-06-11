import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];

        String[] expected = new String[0];

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleArgument() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"arg1"};

        String[] expected = {"arg1"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenDoubleHyphen() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"--arg"};

        String[] expected = {"--arg"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleHyphenWithArgument() {
        Options options = new Options();
        options.addOption("a", "arg", true, "an argument");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-a", "value"};

        String[] expected = {"-a", "value"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "lopt", "a long option");
        PosixParser parser = new PosixParser();
        String[] arguments = {"--longOption", "value"};

        String[] expected = {"--longOption", "value"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("longOption", true, "lopt", "a long option");
        PosixParser parser = new PosixParser();
        String[] arguments = {"--longOption", "value1", "value2"};

        String[] expected = {"--longOption", "value1", "value2"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"--invalid"};

        String[] expected = {};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenBursting() {
        Options options = new Options();
        options.addOption("l", "long", true, "a long option");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-l", "longOption", "value"};

        String[] expected = {"-l", "longOption", "value"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenBurstingInvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"-l", "invalid"};

        String[] expected = {};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] arguments = {"--", "arg1", "-a", "value", "arg2"};

        String[] expected = {"--", "arg1", "-a", "value"};

        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOptionWithArguments() {
        Options options = new Options();
        options.addOption("a", "arg", true, "an argument");
        PosixParser parser = new PosixParser();
        String[] arguments = {"--", "arg1", "-a", "value1", "value2"};

        String[] expected = {"--", "arg1", "-a", "value1", "value2"};

        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("lo", "longOption", true, "a long option");
        options.addOption("co", "anotherLongOption", true, "another long option");
        PosixParser parser = new PosixParser();
        String[] arguments = {"--lo", "value1", "--co", "value2"};

        String[] expected = {"--lo", "value1", "--co", "value2"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMixedOptions() {
        Options options = new Options();
        options.addOption("s", "shortOption", true, "a short option");
        options.addOption("lo", "longOption", true, "a long option");
        PosixParser parser = new PosixParser();
        String[] arguments = {"-s", "value1", "--lo", "value2"};

        String[] expected = {"-s", "value1", "--lo", "value2"};

        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }
}