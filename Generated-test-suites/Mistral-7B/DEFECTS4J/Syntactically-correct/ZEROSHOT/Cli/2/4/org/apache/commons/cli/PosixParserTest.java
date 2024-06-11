import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");

        String[] args = {"--a", "--b", "-c", "-d", "value1", "-c", "value2", "value3"};
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b", "-c", "-c", "-d", "value1", "-c", "value2", "value3"}, tokens);

        args = new String[]{"--a", "--b=value1", "-c", "-d", "value2", "--c=value3", "-d", "value4"};
        tokens = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b=value1", "--c=value3", "-d", "value2", "value4"}, tokens);

        args = new String[]{"--a", "-b", "-c", "value1", "value2"};
        tokens = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--a", "-b", "--", "value1", "value2"}, tokens);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"--z", "--a", "--b=value1", "-c", "value2"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenBurstLongOption() throws ParseException {
        Options options = new Options();
        options.addOption("l", true, "Long Option");

        String[] args = {"--l--long-option-value"};
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--l--long-option-value"}, tokens);
    }

    @Test
    public void testFlattenBurstShortOptionWithValue() throws ParseException {
        Options options = new Options();
        options.addOption("x", true, "Option X");

        String[] args = {"--x-x", "value"};
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--x-x", "--x", "value"}, tokens);
    }

    @Test
    public void testFlattenBurstShortOptionNoValue() throws ParseException {
        Options options = new Options();
        options.addOption("x", true, "Option X");

        String[] args = {"--x"};
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--x"}, tokens);
    }

    @Test
    public void testFlattenBurstShortOptionWithValueAndAnotherOption() throws ParseException {
        Options options = new Options();
        options.addOption("x", true, "Option X");
        options.addOption("y", false, "Option Y");

        String[] args = {"--x-x", "value", "--y"};
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--x-x", "--x", "value"}, tokens);
    }

    @Test
    public void testFlattenStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");

        String[] args = {"--a", "--b=value1", "value2", "value3", "-c", "value4"};
        PosixParser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--a", "--b=value1", "-c", "value2", "value3"}, tokens);
    }
}