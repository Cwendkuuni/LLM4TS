import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B with argument");
        options.addOption("c", 3, "Option C with argument");

        String[] args = {"-a", "--b", "1", "2", "--c", "3", "4"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--b", "1", "--c", "3"}, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("long-option-a", true, "Long Option A");
        options.addOption("long-option-b", 2, "Long Option B with argument");

        String[] args = {"--long-option-a", "--long-option-b=1", "--long-option-b=2", "invalid"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--long-option-a", "--long-option-b=1", "--long-option-b=2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"-z", "--a", "1"}; // invalid option 'z' is present
        PosixParser parser = new PosixParser();

        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenMissingOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--b", "1"}; // missing option 'a'
        PosixParser parser = new PosixParser();

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--", "non-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"--", "non-option"}, result);
    }

    @Test
    public void testFlattenBursting() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");
        options.addOption("ac", 2, "Option AC with argument");

        String[] args = {"-ab", "1", "-ac", "2", "3", "4"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-ab", "--ac=2", "3", "4"}, result);
    }
}