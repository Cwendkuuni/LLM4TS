import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenBasicUsage() {
        String[] arguments = {"--foo", "-o", "optA", "-a", "argA", "--bar=valueB", "argC"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("o", "opt");
        options.addOption("a", true, "a");
        options.addOption("bar", true, "b");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--foo", "-o", "opt", "-a", "argA", "--bar=valueB", "argC"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] arguments = {"--invalid", "--foo", "-o", "optA", "-a", "argA", "--bar=valueB", "argC"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("o", "opt");
        options.addOption("a", true, "a");
        options.addOption("bar", true, "b");

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);

        // The test should fail with a ParseException
    }

    @Test
    public void testFlattenMultipleValidOptions() {
        String[] arguments = {"--foo", "--bar", "-o", "optA", "-a", "argA", "--bar=valueB", "argC"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("o", "opt");
        options.addOption("a", true, "a");
        options.addOption("bar", true, "b");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar", "-o", "opt", "-a", "argA", "--bar=valueB", "argC"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"--foo", "-o", "optA", "-a", "argA", "--bar=valueB", "argC", "nonOption"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("o", "opt");
        options.addOption("a", true, "a");
        options.addOption("bar", true, "b");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"--foo", "-o", "optA", "-a", "argA", "--bar=valueB"}, result);
    }

    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = {};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenSingleArgument() {
        String[] arguments = {"arg"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"arg"}, result);
    }
}