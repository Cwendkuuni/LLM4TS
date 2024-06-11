import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testSimpleOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expected = {"Option A"};

        String[] result = parser.parse(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testOptionWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with argument");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "--b", "arg2"};
        String[] expected = {"Option A", "arg1", "Option B with argument", "arg2"};

        String[] result = parser.parse(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        PosixParser parser = new PosixParser();
        String[] args = {"--longOption"};
        String[] expected = {"Long option"};

        String[] result = parser.parse(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        options.addOption("anotherLongOption", 1, "Another long option with argument");

        PosixParser parser = new PosixParser();
        String[] args = {"--longOption", "arg1", "--anotherLongOption", "arg2"};
        String[] expected = {"Long option", "arg1", "Another long option with argument", "arg2"};

        String[] result = parser.parse(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testUnknownOption() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {"--unknownOption"};

        parser.parse(options, args, true);
    }

    @Test
    public void testBursting() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with argument");

        PosixParser parser = new PosixParser();
        String[] args = {"-ab", "arg"};

        String[] expected = {"Option A", "Option B with argument", "arg"};

        String[] result = parser.parse(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with argument");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "nonOption", "arg2"};

        String[] expected = {"Option A", "arg1"};

        String[] result = parser.parse(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with argument");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "arg1", "-b", "arg2"};

        String[] expected = {"Option A", "arg1", "Option B with argument", "arg2"};

        String[] result = parser.parse(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testEmptyArgs() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] args = {};

        String[] result = parser.parse(options, args, true);
        Assert.assertArrayEquals(new String[0], result);
    }
}