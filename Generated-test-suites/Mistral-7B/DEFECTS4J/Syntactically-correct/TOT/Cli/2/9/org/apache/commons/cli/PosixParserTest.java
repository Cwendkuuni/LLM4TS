import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", 3, "Option C with argument");

        String[] arguments = {"-a", "--b", "1", "-c", "3", "4"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "--b", "1", "-c", "3"}, result);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("long-option", true, "Long option with argument");

        String[] arguments = {"--long-option", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();

        String[] arguments = {"--", "--double-dash"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--", "--double-dash"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() {
        Options options = new Options();

        String[] arguments = {"-z", "value"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenMissingArgument() {
        Options options = new Options();
        options.addOption("c", 1, "Option C with required argument");

        String[] arguments = {"-c"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenExtraArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a", "--", "extra"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }
}