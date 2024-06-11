import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private Options options;
    private String[] arguments;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        arguments = new String[]{"--a", "--b=5", "-a", "--", "-b", "-c"};
    }

    @Test
    public void testFlatten_BasicUsage() throws ParseException {
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--a", "--b", "-a", "--", "-b"}, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[]{"-"}, false);
        assertArrayEquals(new String[]{"-"}, result);
    }

    @Test
    public void testFlatten_TwoCharacterOption() {
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[]{"-a"}, false);
        assertArrayEquals(new String[]{"-a"}, result);
    }

    @Test
    public void testFlatten_TwoCharacterOptionWithArgument() {
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[]{"-ab"}, false);
        assertArrayEquals(new String[]{"-a", "-b"}, result);
    }

    @Test
    public void testFlatten_LongOption() {
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[]{"--long-option"}, false);
        assertArrayEquals(new String[]{"--long-option"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_UnknownOption() {
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[]{"-z"}, false);
        fail("Should throw ParseException for unknown option");
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[]{"--a", "-z", "--b", "--c"}, true);
        assertArrayEquals(new String[]{"--a", "-z", "--b", "--c"}, result);
    }

    @Test
    public void testFlatten_EmptyOption() {
        PosixParser parser = new PosixParser();
        Options emptyOptions = new Options();
        String[] result = parser.flatten(emptyOptions, new String[]{}, false);
        assertArrayEquals(new String[]{}, result);
    }
}