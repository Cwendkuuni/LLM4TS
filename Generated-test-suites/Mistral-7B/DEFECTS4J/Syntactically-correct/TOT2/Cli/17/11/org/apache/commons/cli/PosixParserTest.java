import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class PosixParserTest {

    @Test
    public void testFlattenBasic() throws ParseException {
        String[] args = {"--foo", "-o", "opt", "--bar=value", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("o", "opt");
        options.addOption("bar", true, "b").hasArg();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--foo", "-o", "opt", "--bar", "value", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenEmptyArgs() throws ParseException {
        String[] args = {};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[0], result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        String[] args = {"--invalid"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}