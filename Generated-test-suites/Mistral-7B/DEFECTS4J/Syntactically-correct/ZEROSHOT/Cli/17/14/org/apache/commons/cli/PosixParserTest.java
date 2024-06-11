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
        // Given
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D with arg");
        options.addOption("e", 5, "Option E with multiple args");
        options.addOption("f");
        options.addOption("g");

        String[] arguments = {"--a", "--b", "--c", "--d=value", "--e=value1 value2", "-e", "--g"};

        // When
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // Then
        List<String> expected = Arrays.asList(
                "--a",
                "--b",
                "--c",
                "--d",
                "value",
                "--e",
                "value1",
                "value2"
        );
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        // When - stopAtNonOption is true
        result = parser.flatten(options, arguments, true);

        // Then
        expected = Arrays.asList(
                "--a",
                "--b",
                "--c",
                "--d=value",
                "value1",
                "value2",
                "--e",
                "--g",
                "--",
                "--",
                "-e"
        );
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }
}