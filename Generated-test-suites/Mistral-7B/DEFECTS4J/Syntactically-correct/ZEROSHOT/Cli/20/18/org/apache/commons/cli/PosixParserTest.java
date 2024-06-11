import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B");
        options.addOption("c", 3, "option C with argument");
        options.addOption("d", 3, "option D with argument");
        options.addOption("e", 2, "option E with two arguments");
        options.addOption("f", false, "option F without argument");

        String[] arguments = {"--a", "-b", "1", "--c", "arg1", "--d", "arg2", "arg3", "--e", "arg4", "arg5", "-f"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        List<String> expected = Arrays.asList(
                "--a",
                "-b",
                "--c",
                "arg1",
                "--d",
                "arg2",
                "arg3",
                "--e",
                "arg4",
                "arg5",
                "-f"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArguments);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B");
        options.addOption("c", 3, "option C with argument");
        options.addOption("d", 3, "option D with argument");
        options.addOption("e", 2, "option E with two arguments");
        options.addOption("f", false, "option F without argument");

        String[] arguments = {"--a", "-b", "1", "--c", "arg1", "--d", "arg2", "arg3", "--e", "arg4", "arg5", "arg6", "-f", "arg7"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, true);

        List<String> expected = Arrays.asList(
                "--a",
                "-b",
                "1",
                "--c",
                "arg1",
                "--d",
                "arg2",
                "arg3",
                "--e",
                "arg4",
                "arg5",
                "-f",
                "--",
                "arg6",
                "arg7"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), parsedArguments);
    }
}