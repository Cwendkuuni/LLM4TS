import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testEmptyArgumentArray() {
        Options options = new Options();
        String[] arguments = {};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], flattenedArguments);
    }

    @Test
    public void testSingleArgument() {
        Options options = new Options();
        String[] arguments = {"arg1"};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, flattenedArguments);
    }

    @Test
    public void testSingleShortOption() {
        Options options = new Options();
        options.addOptional("o", "option", false, "Short option test");
        String[] arguments = {"-o"};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-o"}, flattenedArguments);
    }

    @Test
    public void testSingleLongOption() {
        Options options = new Options();
        options.addOptional("longOption", false, "Long option test");
        String[] arguments = {"--longOption"};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption"}, flattenedArguments);
    }

    @Test
    public void testSingleShortOptionWithValue() {
        Options options = new Options();
        options.addOptional("o", "option", true, "Short option test with value");
        String[] arguments = {"-o", "value1"};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-o", "value1"}, flattenedArguments);
    }

    @Test
    public void testSingleLongOptionWithValue() {
        Options options = new Options();
        options.addOptional("longOption", true, "Long option test with value");
        String[] arguments = {"--longOption=value1"};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption", "value1"}, flattenedArguments);
    }

    @Test(expected = ParseException.class)
    public void testInvalidShortOption() {
        Options options = new Options();
        String[] arguments = {"-z"}; // Invalid option 'z'

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testMissingOptionValue() {
        Options options = new Options();
        options.addOptional("o", "option", true, "Short option test with value");
        String[] arguments = {"-o"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testMultipleOptions() {
        Options options = new Options();
        options.addOptional("o", "option1", false, "Option 1");
        options.addOptional("o", "option2", false, "Option 2");
        String[] arguments = {"-o", "--option2"};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-o", "--option2"}, flattenedArguments);
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOptional("o", "option", false, "Option");
        String[] arguments = {"-a", "arg1", "-o", "arg2"};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"-a", "arg1"}, flattenedArguments);
    }

    @Test
    public void testBurstTokenSingleOption() {
        Options options = new Options();
        options.addOptional("o", "option", false, "Option");
        String[] arguments = {"-oo"};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-o", "-o"}, flattenedArguments);
    }

    @Test
    public void testBurstTokenLongOption() {
        Options options = new Options();
        options.addOptional("longOption", false, "Long option");
        String[] arguments = {"--longOption--"};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption--"}, flattenedArguments);
    }

    @Test
    public void testBurstTokenShortOptionWithValue() {
        Options options = new Options();
        options.addOptional("o", "option", true, "Option");
        String[] arguments = {"-o--value"};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--", "option", "value"}, flattenedArguments);
    }

    @Test
    public void testBurstTokenLongOptionWithValue() {
        Options options = new Options();
        options.addOptional("longOption", true, "Option");
        String[] arguments = {"--longOption=--value"};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption", "--value"}, flattenedArguments);
    }

    @Test
    public void testBurstTokenInvalidOption() {
        Options options = new Options();
        String[] arguments = {"--invalid"};

        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--invalid"}, flattenedArguments);
    }
}