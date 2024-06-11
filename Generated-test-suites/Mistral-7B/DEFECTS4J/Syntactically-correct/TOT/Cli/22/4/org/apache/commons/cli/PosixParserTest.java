import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"--foo", "-o", "out.txt", "-h", "--bar=baz"};
        Options options = new Options();
        options.addOption("foo", true, "Help text for --foo");
        options.addOption("o", "out", "Output file name");
        options.addOption("h", false, "Help text for -h");
        options.addOption("bar", true, "Help text for --bar");

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--bar");
        expected.add("baz");
        expected.add("-o");
        expected.add("out.txt");
        expected.add("--");
        expected.add("h");

        List<String> actual = new PosixParser().flatten(options, args, false);
        assertEquals("Unexpected result", expected.toArray(new String[expected.size()]), actual.toArray(new String[actual.size()]));
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"--invalid"};
        Options options = new Options();

        new PosixParser().flatten(options, args, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"--foo", "-o", "out.txt", "invalid"};
        Options options = new Options();
        options.addOption("foo", true, "Help text for --foo");
        options.addOption("o", "out", "Output file name");

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("--");
        expected.add("o");
        expected.add("out.txt");
        expected.add("invalid");

        List<String> actual = new PosixParser().flatten(options, args, true);
        assertEquals("Unexpected result", expected.toArray(new String[expected.size()]), actual.toArray(new String[actual.size()]));
    }

    @Test
    public void testFlattenNoOptions() {
        String[] args = {"foo.txt", "bar.txt"};
        Options options = new Options();

        List<String> expected = new ArrayList<>();
        expected.add("foo.txt");
        expected.add("bar.txt");

        List<String> actual = new PosixParser().flatten(options, args, false);
        assertEquals("Unexpected result", expected.toArray(new String[expected.size()]), actual.toArray(new String[actual.size()]));
    }

    @Test
    public void testFlattenWithLongOptions() {
        String[] args = {"--longOption1", "--longOption2=value", "--longOption3"};
        Options options = new Options();
        options.addOption("longOption1", false, "Help text for --longOption1");
        options.addOption("longOption2", true, "Help text for --longOption2");
        options.addOption("longOption3", false, "Help text for --longOption3");

        List<String> expected = new ArrayList<>();
        expected.add("--longOption1");
        expected.add("--longOption2");
        expected.add("value");
        expected.add("--longOption3");

        List<String> actual = new PosixParser().flatten(options, args, false);
        assertEquals("Unexpected result", expected.toArray(new String[expected.size()]), actual.toArray(new String[actual.size()]));
    }

    @Test
    public void testFlattenMissingOption() {
        String[] args = {"--missingOption"};
        Options options = new Options();

        try {
            new PosixParser().flatten(options, args, false);
            fail("Failed to throw ParseException");
        } catch (ParseException e) {
            // expected
        }
    }
}