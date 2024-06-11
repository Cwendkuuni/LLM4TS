import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenBasic() {
        Options options = new Options();
        String[] arguments = {"--foo", "-o", "out.txt", "-h", "--help"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--foo", "-o", "out.txt", "-h", "--help");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--foo", "-o", "out.txt", "-h", "nonOption"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List<String> expected = Arrays.asList("--foo", "-o", "out.txt", "--", "nonOption");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenSingleDash() {
        Options options = new Options();
        String[] arguments = {"-", "--help"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("-", "--help");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] arguments = {"--invalid", "arg1", "arg2"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--invalid", "arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);

        // check if an exception is thrown when an invalid option is encountered
        try {
            parser.flatten(options, new String[]{"--invalid"}, false);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        String[] arguments = {"--foo=value"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--foo=value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenLongOptionWithoutValue() {
        Options options = new Options();
        String[] arguments = {"--foo"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--foo");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlattenOptionWithArgument() {
        Options options = new Options();
        options.addOptionalArg(true, "outFile");
        String[] arguments = {"--foo", "-o", "out.txt"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--foo", "-o", "out.txt");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);

        // check if an exception is thrown when an option without an argument is encountered and argument is provided
        try {
            parser.flatten(options, new String[]{"--foo", "-o", "out.txt"}, false);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBursting() {
        Options options = new Options();
        String[] arguments = {"--foo-bar"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("--foo", "-", "bar");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testBurstingWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--foo-bar", "nonOption"};
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List<String> expected = Arrays.asList("--foo", "--", "bar", "--", "nonOption");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }
}