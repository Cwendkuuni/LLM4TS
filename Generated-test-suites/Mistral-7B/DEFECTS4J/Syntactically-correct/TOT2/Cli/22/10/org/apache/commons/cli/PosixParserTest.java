import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"--foo", "--bar=baz", "-abc", "-d", "-e", "--"};
        Options options = new Options();
        options.addOption("foo", true, false, "A foo option");
        options.addOption("bar", true, true, "A bar option with an argument");
        options.addOption("abc", false, false, "An abc option");
        options.addOption("d", false, false, "A d option");
        options.addOption("e", false, false, "An e option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar", "baz", "-abc", "--d", "--e"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenMissingOption() {
        String[] args = {"--unknown"};
        Options options = new Options();
        options.addOption("foo", true, false, "A foo option");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"--invalid"};
        Options options = new Options();
        options.addOption("foo", true, false, "A foo option");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithArg() {
        String[] args = {"--foo=value"};
        Options options = new Options();
        options.addOption("foo", true, true, "A foo option with an argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "value"}, result);
    }

    @Test
    public void testFlattenLongOptionWithArg() {
        String[] args = {"--long-option=value"};
        Options options = new Options();
        options.addOption("longOption", true, true, "A long option with an argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option", "value"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        String[] args = {"--foo", "--bar=value", "-c", "--d", "--e"};
        Options options = new Options();
        options.addOption("foo", true, false, "A foo option");
        options.addOption("bar", true, true, "A bar option with an argument");
        options.addOption("c", false, false, "A c option");
        options.addOption("d", false, false, "A d option");
        options.addOption("e", false, false, "An e option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar", "value", "-c", "--d", "--e"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"--foo", "--bar=value", "-c", "--d", "--e", "nonOption"};
        Options options = new Options();
        options.addOption("foo", true, false, "A foo option");
        options.addOption("bar", true, true, "A bar option with an argument");
        options.addOption("d", false, false, "A d option");
        options.addOption("e", false, false, "An e option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar", "value"}, result);
    }

    @Test
    public void testFlattenWithLongOptionAndArg() {
        String[] args = {"--long-option-with-arg=value"};
        Options options = new Options();
        options.addOption("longOptionWithArg", true, true, "A long option with an argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-with-arg", "value"}, result);
    }

    @Test
    public void testFlattenWithLongOptionWithoutArg() {
        String[] args = {"--long-option"};
        Options options = new Options();
        options.addOption("longOption", true, false, "A long option without an argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test
    public void testFlattenWithBurstableOption() {
        String[] args = {"-abcd"};
        Options options = new Options();
        options.addOption("a", false, false, "An a option");
        options.addOption("bcd", false, false, "A bcd option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "-c", "-d"}, result);
    }

    @Test
    public void testFlattenWithBurstableOptionWithArg() {
        String[] args = {"-abcde"};
        Options options = new Options();
        options.addOption("a", false, false, "An a option");
        options.addOption("bcd", false, true, "A bcd option with an argument");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "-c", "de"}, result);
    }
}