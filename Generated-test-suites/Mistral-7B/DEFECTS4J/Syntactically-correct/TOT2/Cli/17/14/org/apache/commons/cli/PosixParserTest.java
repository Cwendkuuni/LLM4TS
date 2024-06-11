import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        // given
        String[] args = {"--foo", "-o", "bar", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "A boolean");
        options.addOption("o", "opt", true, "An option with a short argument");

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, args, false);

        // then
        Assert.assertArrayEquals(new String[]{"--foo", "-o", "opt", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        // given
        String[] args = {"--foo", "-o", "bar", "--", "arg1", "arg2", "nonOption"};
        Options options = new Options();
        options.addOption("foo", true, "A boolean");
        options.addOption("o", "opt", true, "An option with a short argument");

        PosixParser parser = new PosixParser();

        // when
        String[] result = parser.flatten(options, args, true);

        // then
        Assert.assertArrayEquals(new String[]{"--foo", "-o", "bar", "nonOption"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        // given
        String[] args = {"--invalidOption", "arg1", "arg2"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        // when and then
        parser.flatten(options, args, false);

        // the test should fail with a ParseException
    }
}