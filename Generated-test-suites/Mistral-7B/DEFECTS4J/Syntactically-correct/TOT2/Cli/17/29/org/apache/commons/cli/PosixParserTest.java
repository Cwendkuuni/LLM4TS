import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class PosixParserTest {

    @Test
    public void testFlattenWithSimpleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--b", "1"};
        String[] expected = {"-a", "--b", "1"};

        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        PosixParser parser = new PosixParser();
        String[] args = {"--longOption"};

        String[] result = parser.flatten(options, args, false);

        assertEquals("--longOption", result[0]);
    }

    @Test
    public void testFlattenWithNoOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--notOption"};

        String[] result = parser.flatten(options, args, true);

        assertEquals("--notOption", result[0]);
    }

    @Test
    public void testFlattenWithValue() {
        Options options = new Options();
        options.addOption("valueOption", true, "Value option");

        PosixParser parser = new PosixParser();
        String[] args = {"--valueOption=test"};

        String[] result = parser.flatten(options, args, false);

        assertEquals("--valueOption=test", result[0]);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] args = {"--invalidOption"};

        parser.flatten(options, args, false);
    }
}