import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlattenBasicUsage() {
        Options options = new Options();
        String[] arguments = {"--option1", "-o", "value1", "--option2", "-i", "inputFile.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--option1", "-o", "value1", "--option2", "-i", "inputFile.txt"}, result);
    }

    @Test
    public void testFlattenSingleHyphen() {
        Options options = new Options();
        String[] arguments = {"-", "--option1", "-i", "inputFile.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--", "--option1", "-i", "inputFile.txt"}, result);
    }

    @Test
    public void testFlattenOptionWithArg() {
        Options options = new Options();
        options.addOption("o", "output", true, "output value");
        String[] arguments = {"-o", "outputValue"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-o", "outputValue"}, result);
    }

    @Test
    public void testFlattenOptionWithMultipleArgs() {
        Options options = new Options();
        options.addOption("i", "inputFiles", true, "list of input files");
        String[] arguments = {"-i", "file1.txt", "--", "file2.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-i", "file1.txt", "--", "file2.txt"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] arguments = {"--invalidOption"};
        PosixParser parser = new PosixParser();

        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOptionWithArg() {
        Options options = new Options();
        String[] arguments = {"-u", "unknownOption", "arg"};
        PosixParser parser = new PosixParser();

        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOptionWithoutArg() {
        Options options = new Options();
        String[] arguments = {"-u", "unknownOption"};
        PosixParser parser = new PosixParser();

        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenMissingArgForOptionWithArg() {
        Options options = new Options();
        options.addOption("o", "output", true, "output value");
        String[] arguments = {"-o"};
        PosixParser parser = new PosixParser();

        parser.flatten(options, arguments, false);
    }
}