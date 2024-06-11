import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        String[] args = {"--foo", "--bar=test", "-a", "-b", "-c", "-d=value", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("c", false, "c");
        options.addOption("d", true, "d");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--foo", "--bar=test", "-a", "-b", "--", "-c", "-d=value", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"--foo", "--bar=test", "-a", "-b", "--", "-c", "-d=value", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("c", false, "c");
        options.addOption("d", true, "d");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"--foo", "--bar=test", "-c", "-d=value", "arg1", "arg2"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullOptions() {
        String[] args = {"--foo", "--bar=test", "-a", "-b", "--", "-c", "-d=value", "arg1", "arg2"};
        PosixParser parser = new PosixParser();

        parser.flatten(null, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullArguments() {
        String[] args = null;
        Options options = new Options();
        options.addOption("foo", true, "f");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}