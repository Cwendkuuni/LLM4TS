import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenPosixParser() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with two arguments");
        options.addOption("d", 3, "Option D with three arguments");

        String[] args = {"-a", "--b", "1", "--c", "arg1", "arg2", "-d", "arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);

        List<String> expectedTokens = Arrays.asList(
                "-a",
                "--b",
                "1",
                "-c",
                "arg1",
                "arg2",
                "-d",
                "arg1",
                "arg2",
                "arg3"
        );

        Assert.assertArrayEquals(expectedTokens.toArray(new String[expectedTokens.size()]), parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenPosixParserWithInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with two arguments");
        options.addOption("d", 3, "Option D with three arguments");

        String[] args = {"-a", "--x", "1", "--c", "arg1", "arg2", "-d", "arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);

        // The test should fail with a ParseException
    }

    @Test
    public void testFlattenPosixParserWithLongArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");

        String[] args = {"-a", "--b=long argument"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);

        List<String> expectedTokens = Arrays.asList(
                "-a",
                "--b",
                "long argument"
        );

        Assert.assertArrayEquals(expectedTokens.toArray(new String[expectedTokens.size()]), parsedArgs);
    }

    @Test
    public void testFlattenPosixParserWithNoOptions() throws ParseException {
        Options options = new Options();

        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        List<String> expectedTokens = Arrays.asList();

        Assert.assertArrayEquals(expectedTokens.toArray(new String[expectedTokens.size()]), parsedArgs);
    }
}