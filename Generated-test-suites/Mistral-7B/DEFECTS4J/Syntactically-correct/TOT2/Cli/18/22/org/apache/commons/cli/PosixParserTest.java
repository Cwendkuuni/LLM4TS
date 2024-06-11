import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArguments() throws ParseException {
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(new Options(), arguments, false);
        List<String> expected = new ArrayList<>();
        // No tokens should be added when passing an empty array
        Assert.assertArrayEquals(expected.toArray(new String[0]), flattenedArguments);
    }

    @Test
    public void testFlattenSimpleArguments() throws ParseException {
        String[] arguments = {"--opt1", "--opt2=value", "-o", "opt3", "--"};
        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(new Options(), arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--opt1");
        expected.add("--opt2");
        expected.add("value");
        expected.add("-o");
        expected.add("opt3");
        Assert.assertArrayEquals(expected.toArray(new String[0]), flattenedArguments);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        String[] arguments = {"--invalidOption", "--opt1=value", "-o", "opt3", "--"};
        PosixParser parser = new PosixParser();
        parser.flatten(new Options(), arguments, false);
        // An exception should be thrown when an invalid option is encountered
    }

    @Test
    public void testFlattenStopAtNonOption() throws ParseException {
        String[] arguments = {"--opt1", "--opt2=value", "-o", "opt3", "--nonOption", "--"};
        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(new Options(), arguments, true);
        List<String> expected = new ArrayList<>();
        expected.add("--opt1");
        expected.add("--opt2");
        expected.add("value");
        expected.add("-o");
        expected.add("opt3");
        expected.add("--nonOption");
        Assert.assertArrayEquals(expected.toArray(new String[0]), flattenedArguments);
    }

    @Test
    public void testBurstTokenSingleCharacterOption() {
        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(new Options(), new String[]{"-o"}, false).clone();
        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("o");
        Assert.assertArrayEquals(expected.toArray(new String[0]), flattenedArguments);
    }

    @Test
    public void testBurstTokenMultiCharacterOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("abc", true, true, "abc option");
        String[] flattenedArguments = parser.flatten(options, new String[]{"-abc"}, false).clone();
        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("abc");
        Assert.assertArrayEquals(expected.toArray(new String[0]), flattenedArguments);
    }

    @Test
    public void testBurstTokenOptionWithArgument() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("opt", true, true, "opt option");
        String[] flattenedArguments = parser.flatten(options, new String[]{"--opt=value"}, false).clone();
        List<String> expected = new ArrayList<>();
        expected.add("--opt");
        expected.add("value");
        Assert.assertArrayEquals(expected.toArray(new String[0]), flattenedArguments);
    }

    @Test
    public void testBurstTokenOptionWithMultipleArguments() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("opt", true, 2, "opt option");
        String[] flattenedArguments = parser.flatten(options, new String[]{"--opt=value1 value2"}, false).clone();
        List<String> expected = new ArrayList<>();
        expected.add("--opt");
        expected.add("value1");
        expected.add("value2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), flattenedArguments);
    }
}