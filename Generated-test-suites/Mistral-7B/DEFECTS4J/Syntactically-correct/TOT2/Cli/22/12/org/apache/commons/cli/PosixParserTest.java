import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = {"-a", "--b", "-c"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "--b", "-c"}, result);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");
        String[] arguments = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption"}, result);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");
        String[] arguments = {"--longOption", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-a", "-b", "--nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List expectedResult = new ArrayList();
        expectedResult.add("-a");
        expectedResult.add("--nonOption");
        Assert.assertArrayEquals(expectedResult.toArray(new String[0]), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] arguments = {"--invalid"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenInvalidLongOption() {
        Options options = new Options();
        String[] arguments = {"--invalid-long"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenMissingOption() {
        Options options = new Options();
        String[] arguments = {"-x"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenMissingLongOption() {
        Options options = new Options();
        String[] arguments = {"--x"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }
}