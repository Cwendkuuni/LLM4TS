import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() throws ParseException {
        String[] args = {"-a", "--optionB=valueB", "-c", "--", "--optionD=valueD"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("d", true, "Option D");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "-a",
                "--optionB",
                "valueB",
                "-c",
                "--",
                "--optionD",
                "valueD"
        );

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] args = {"-a", "--optionB=valueB", "-c", "notAnOption", "--optionD=valueD"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("d", true, "Option D");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(
                "-a",
                "--optionB",
                "valueB",
                "notAnOption",
                "--optionD",
                "valueD"
        );

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testBurstingOption() throws ParseException {
        String[] args = {"-abcdef"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");
        options.addOption("e", true, "Option E");
        options.addOption("f", true, "Option F");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "-a",
                "-b",
                "-c",
                "-d",
                "-e",
                "-f"
        );

        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        String[] args = {"--invalidOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        // This test should throw a ParseException
    }
}