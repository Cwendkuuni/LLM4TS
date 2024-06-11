import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
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
        options.addOption("c", 2, "Option C with argument");
        options.addOption("d", 3, "Option D with multiple arguments");

        String[] args = new String[]{"-a", "--b", "-c", "value1", "--d", "value2", "value3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "-a",
                "--b",
                "-c",
                "value1"
        );

        List<String> actual = Arrays.asList(result);
        Assert.assertEquals(expected, actual);

        // test with stopAtNonOption = true
        result = parser.flatten(options, args, true);
        expected = Arrays.asList(
                "-a",
                "--b",
                "-c",
                "--",
                "value1",
                "--d",
                "value2",
                "value3"
        );

        actual = Arrays.asList(result);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");
        options.addOption("ac", 1, "Option AC");
        options.addOption("ad", 3, "Option AD with multiple arguments");

        String token = "abcd";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);

        List<String> expected = Arrays.asList(
                "-a",
                "-b",
                "-c",
                "--d"
        );

        List<String> actual = Arrays.asList(parser.tokens);
        parser.init(); // reset the parser for next test
        Assert.assertEquals(expected, actual);

        // test with stopAtNonOption = true
        List<String> expectedStopAtNonOption = Arrays.asList(
                "--",
                "abcd"
        );

        parser.burstToken(token, true);
        List<String> actualStopAtNonOption = Arrays.asList(parser.tokens);
        parser.init(); // reset the parser for next test
        Assert.assertEquals(expectedStopAtNonOption, actualStopAtNonOption);
    }
}