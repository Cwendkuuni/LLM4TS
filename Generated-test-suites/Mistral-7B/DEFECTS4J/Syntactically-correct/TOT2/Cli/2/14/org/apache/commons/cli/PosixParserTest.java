import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    @Test
    public void testFlattenBasic() {
        Options options = new Options();
        String[] arguments = {"--foo", "-o", "option", "--", "-a", "argA", "-b", "argB", "--quit"};

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--");
        expected.add("--option");
        expected.add("--");
        expected.add("-a");
        expected.add("argA");
        expected.add("-b");
        expected.add("argB");
        expected.add("--quit");

        Assert.assertArrayEquals(expected.toArray(new String[0]), flattened);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] arguments = {"--invalid"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"--foo", "-o", "option", "--", "-a", "argA", "-b", "argB", "--quit", "anythingElse"};

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, arguments, true);

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--");
        expected.add("--option");
        expected.add("--");
        expected.add("-a");
        expected.add("argA");
        expected.add("-b");
        expected.add("argB");
        expected.add("--quit");

        Assert.assertArrayEquals(expected.toArray(new String[0]), flattened, "Test flatten with stopAtNonOption = true");
    }
}