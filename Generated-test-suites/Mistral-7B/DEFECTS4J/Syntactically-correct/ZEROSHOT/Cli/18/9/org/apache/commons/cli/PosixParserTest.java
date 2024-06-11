import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", 1, "Option C");
        options.addOption("d", 2, "Option D");

        String[] arguments = {"--a", "--b", "-c", "1", "--d=2", "--e", "-f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(
                "--a",
                "-c",
                "1",
                "--d",
                "2",
                "--e",
                "-f"
        );

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("long-option-a", true, "Long Option A");
        options.addOption("long-option-b", false, "Long Option B");

        String[] arguments = {"--long-option-a", "--long-option-b"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(
                "--long-option-a",
                "--long-option-b"
        );

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithLongOptionsAndValues() throws ParseException {
        Options options = new Options();
        options.addOption("long-option-a", true, "Long Option A");
        options.addOption("long-option-b", true, "Long Option B");

        String[] arguments = {"--long-option-a", "--long-option-b=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(
                "--long-option-a",
                "--long-option-b=value"
        );

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidLongOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"--invalid-option"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"--a", "--b", "non-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        List<String> expected = Arrays.asList(
                "--a",
                "--b",
                "non-option"
        );

        Assert.assertArrayEquals(expected.toArray(), result);
    }
}