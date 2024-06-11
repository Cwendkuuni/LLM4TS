import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C");
        options.addOption("d", 3, "Option D with argument");
        options.addOption("e", 4, "Option E with multiple arguments");
        options.addOption("f", "Option F with value");
        options.addOption("g", "Option G with value");

        String[] arguments = {"--a", "-b", "-c", "--d", "arg1", "--e", "arg1", "arg2", "--f", "value1", "--g", "value2"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(
                "--a",
                "-c",
                "--d",
                "arg1",
                "--e",
                "arg1",
                "arg2",
                "--f",
                "value1",
                "--g",
                "value2"
        );

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);

        // Test with stopAtNonOption = true
        String[] argumentsStopAtNonOption = {"--a", "-b", "-c", "--d", "arg1", "--e", "arg1", "arg2", "--f", "value1", "--g", "value2", "nonOption"};
        result = parser.flatten(options, argumentsStopAtNonOption, true);

        expected = Arrays.asList(
                "--a",
                "-b",
                "--c",
                "--d",
                "arg1",
                "--e",
                "arg1",
                "arg2",
                "--f",
                "value1",
                "--g",
                "value2",
                "nonOption"
        );

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");
        options.addOption("cd", 1, "Option CD");

        String[] arguments = {"--abcd"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(
                "--ab",
                "--cd"
        );

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);

        // Test with stopAtNonOption = true
        String[] argumentsStopAtNonOption = {"--abcd", "nonOption"};
        result = parser.flatten(options, argumentsStopAtNonOption, true);

        expected = Arrays.asList(
                "--abcd",
                "nonOption"
        );

        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }
}