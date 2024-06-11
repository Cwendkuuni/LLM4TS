import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() throws ParseException {
        String[] args = {"--option1", "arg1", "--option2=arg2", "-o", "option3"};
        Options options = new Options();
        options.addOption("option1", true, "h", "help");
        options.addOption("option2", true, "i", "input");
        options.addOption("option3", null, "o", "output");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--", "option1", "true", "--", "option2", "arg2", "--", "-o", "option3"}, result);
    }

    @Test
    public void testFlattenWithNoOptions() throws ParseException {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--", "arg1", "--", "arg2"}, result);
    }

    @Test
    public void testFlattenWithLongOptions() throws ParseException {
        String[] args = {"--longOptionWithArg=arg1", "--longOption2"};
        Options options = new Options();
        options.addOption("longOption", true, null, "longOptionWithArg");
        options.addOption("longOption2", false, null, "longOption2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--", "--longOptionWithArg", "arg1", "--", "--longOption2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        String[] args = {"--invalidOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
        fail("No ParseException was thrown");
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] args = {"--option1", "arg1", "--", "arg2", "--option2=arg3", "-o"};
        Options options = new Options();
        options.addOption("option1", true, "h", "help");
        options.addOption("option2", true, "i", "input");
        options.addOption("option3", null, "o", "output");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"--", "option1", "true", "--", "arg2", "--option2", "arg3", "-o"}, result);
    }
}