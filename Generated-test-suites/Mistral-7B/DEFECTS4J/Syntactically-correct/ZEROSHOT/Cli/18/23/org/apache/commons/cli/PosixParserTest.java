import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C");
        options.addOption("d", 3, "Option D");
        options.addOption("e", 4, "Option E");

        String[] args = {"--a", "-b", "-c=valueC", "--d=valueD", "-e=valueE", "valueF"};
        List<String> tokens = new PosixParser().flatten(options, args, false);

        List<String> expectedTokens = Arrays.asList(
                "--a",
                "-b",
                "-c",
                "valueC",
                "--d",
                "valueD",
                "-e",
                "valueE",
                "--",
                "valueF"
        );

        Assert.assertEquals(expectedTokens, tokens);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C");
        options.addOption("d", 3, "Option D");
        options.addOption("e", 4, "Option E");

        String[] args = {"--a", "-b", "-c=valueC", "--", "valueF"};
        List<String> tokens = new PosixParser().flatten(options, args, true);

        List<String> expectedTokens = Arrays.asList(
                "--a",
                "-b",
                "--",
                "valueF"
        );

        Assert.assertEquals(expectedTokens, tokens);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C");
        options.addOption("d", 3, "Option D");
        options.addOption("e", 4, "Option E");

        String[] args = {"--f", "-c=valueC", "--", "valueF"};
        new PosixParser().flatten(options, args, false);
    }
}