import org.apache.commons.cli.*;
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
        options.addOption("c", 3, "Option C with argument");

        String[] arguments = {"-a", "--b", "-c", "value"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        List<String> expectedTokens = Arrays.asList(
                "-a",
                "--b",
                "-c",
                "value"
        );

        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), parsedArguments);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C with argument");

        String[] arguments = {"-a", "--b", "-c", "--", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, true);

        List<String> expectedTokens = Arrays.asList(
                "-a",
                "--b",
                "--",
                "nonOption"
        );

        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), parsedArguments);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("abc", false, "Option ABC");
        options.addOption("def", true, "Option DEF");
        options.addOption("ghi", 1, "Option GHI with argument");

        String[] arguments = {"-abcdefg", "--ghi", "1"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        List<String> expectedTokens = Arrays.asList(
                "-abc",
                "-d",
                "-e",
                "-f",
                "--g",
                "hi",
                "1"
        );

        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), parsedArguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] arguments = {"-x", "value"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(new Options(), new String[]{"-"}, false);

        Assert.assertEquals("-", parsedArguments[0]);
    }
}