import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B with argument");
        options.addOption("c", 3, "Option C with integer argument");
    }

    @After
    public void tearDown() {
        parser = null;
    }

    @Test
    public void testFlattenEmpty() throws ParseException {
        String[] args = new String[0];
        String[] expected = new String[0];

        parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOption() throws ParseException {
        String[] args = {"-a"};
        String[] expected = {"-a"};

        parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOptionWithArgument() throws ParseException {
        String[] args = {"-a", "arg"};
        String[] expected = {"-a", "arg"};

        parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() throws ParseException {
        String[] args = {"-a", "-b", "--"};
        String[] expected = {"-a", "-b"};

        parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithArguments() throws ParseException {
        String[] args = {"-a", "argA", "-b", "argB", "--"};
        String[] expected = {"-a", "argA", "-b", "argB"};

        parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithIntegerArgument() throws ParseException {
        String[] args = {"-c", "3", "--"};
        String[] expected = {"-c", "3"};

        parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        String[] args = {"-x"};

        parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenTwoInvalidOptions() throws ParseException {
        String[] args = {"-x", "-y"};

        parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] args = {"-a", "-b", "-", "--", "nonOption"};
        String[] expected = {"-a", "-b", "--", "nonOption"};

        parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOption() throws ParseException {
        String[] args = {"--longOption"};
        String[] expected = {"--longOption"};

        parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionArgument() throws ParseException {
        String[] args = {"--longOption", "arg"};
        String[] expected = {"--longOption", "arg"};

        parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }
}