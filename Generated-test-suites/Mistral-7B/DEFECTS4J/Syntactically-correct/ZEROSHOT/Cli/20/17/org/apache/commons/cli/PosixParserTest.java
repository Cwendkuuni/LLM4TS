import org.apache.commons.cli.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        String[] args = {"--foo", "--bar=baz", "-a", "-b", "-c", "-d=e", "-e", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("d", true, "d");
        options.addOption("e", true, "e");

        List<String> expected = new ArrayList<>();
        expected.add("foo");
        expected.add("bar");
        expected.add("b");
        expected.add("a");
        expected.add("b");
        expected.add("c");
        expected.add("d");
        expected.add("e");
        expected.add("e");
        expected.add("--");
        expected.add("arg1");
        expected.add("arg2");

        List<String> tokens = new PosixParser().flatten(options, args, false);

        Assert.assertArrayEquals(expected.toArray(new String[0]), tokens.toArray(new String[0]));
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"--foo", "--bar=baz", "-a", "-b", "-c", "-d=e", "-e", "--", "arg1", "arg2", "arg3"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("d", true, "d");
        options.addOption("e", true, "e");

        List<String> tokens = new PosixParser().flatten(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("foo");
        expected.add("bar");
        expected.add("b");
        expected.add("a");
        expected.add("b");
        expected.add("c");
        expected.add("--");
        expected.add("arg1");
        expected.add("arg2");
        expected.add("arg3");

        Assert.assertArrayEquals(expected.toArray(new String[0]), tokens.toArray(new String[0]));

        // Test with invalid argument
        String[] args2 = {"--foo", "--bar=baz", "-a", "-b", "-c", "-d=e", "--invalidOption", "--", "arg1", "arg2"};
        List<String> tokens2 = new PosixParser().flatten(options, args2, false);
        List<String> expected2 = new ArrayList<>();
        expected2.add("foo");
        expected2.add("bar");
        expected2.add("b");
        expected2.add("a");
        expected2.add("b");
        expected2.add("c");
        expected2.add("--");
        expected2.add("invalidOption");
        expected2.add("arg1");
        expected2.add("arg2");

        Assert.assertArrayEquals(expected2.toArray(new String[0]), tokens2.toArray(new String[0]));
    }
}