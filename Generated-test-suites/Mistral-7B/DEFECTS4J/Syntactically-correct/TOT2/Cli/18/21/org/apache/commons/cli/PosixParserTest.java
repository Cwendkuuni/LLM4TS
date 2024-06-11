import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();

        String[] arguments = {"--option1", "-o", "value1", "--option2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--option1", "-o", "value1", "--option2"}, flattenedArguments);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();

        String[] arguments = {"--option1", "-o", "value1", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"--option1", "-o", "value1", "nonOption"}, flattenedArguments);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "l");

        String[] arguments = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--longOption"}, flattenedArguments);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        Options options = new Options();

        String[] arguments = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }
}