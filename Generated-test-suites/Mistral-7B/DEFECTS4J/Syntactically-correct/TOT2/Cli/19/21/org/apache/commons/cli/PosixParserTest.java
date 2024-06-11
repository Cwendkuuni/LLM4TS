import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenWithSingleOption() {
        Options options = new Options();
        options.addOption("option1", true, "Option 1");

        PosixParser parser = new PosixParser();
        String[] args = {"--option1"};
        String[] expected = {"option1"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionAndArgument() {
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", 1, "Option 2");

        PosixParser parser = new PosixParser();
        String[] args = {"--option1", "value1", "--option2", "value2"};
        String[] expected = {"option1", "value1", "option2", "value2"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithMultipleOptionsAndArguments() {
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", 1, "Option 2");
        options.addOption("option3", true, "Option 3");

        PosixParser parser = new PosixParser();
        String[] args = {"--option1", "value1", "--option2", "value2", "--option3"};
        String[] expected = {"option1", "value1", "option2", "value2", "option3"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithNoOptionsAndArguments() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2", "arg3"};
        String[] expected = {"arg1", "arg2", "arg3"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"--invalidOption"};

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOptionAndArguments() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"--invalidOption", "arg1", "arg2"};

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithMixedOptionsAndArguments() {
        Options options = new Options();
        options.addOption("option1", true, "Option 1");

        PosixParser parser = new PosixParser();
        String[] args = {"--option1", "--invalidOption"};

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithMixedOptionsArgumentsAndNoStopAtNonOption() {
        Options options = new Options();
        options.addOption("option1", true, "Option 1");

        PosixParser parser = new PosixParser();
        String[] args = {"--option1", "--invalidOption", "arg1", "arg2"};

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithNoOptionsAndEmptyArgs() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {};
        String[] expected = {};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("longOption1", true, "Long Option 1");

        PosixParser parser = new PosixParser();
        String[] args = {"--longOption1"};
        String[] expected = {"longOption1"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionAndArgument() {
        Options options = new Options();
        options.addOption("longOption1", true, "Long Option 1");
        options.addOption("longOption2", 1, "Long Option 2");

        PosixParser parser = new PosixParser();
        String[] args = {"--longOption1", "value1", "--longOption2", "value2"};
        String[] expected = {"longOption1", "value1", "longOption2", "value2"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidLongOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"--invalidLongOption"};

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidLongOptionAndArguments() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"--invalidLongOption", "arg1", "arg2"};

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithLongOptionsAndNoArguments() {
        Options options = new Options();
        options.addOption("longOption1", true, "Long Option 1");

        PosixParser parser = new PosixParser();
        String[] args = {"--longOption1"};
        String[] expected = {"longOption1"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionsAndArguments() {
        Options options = new Options();
        options.addOption("longOption1", true, "Long Option 1");
        options.addOption("longOption2", 1, "Long Option 2");

        PosixParser parser = new PosixParser();
        String[] args = {"--longOption1", "value1", "--longOption2", "value2"};
        String[] expected = {"longOption1", "value1", "longOption2", "value2"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }
}