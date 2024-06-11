import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E");
        options.addOption("f", false, "Option F");

        String[] args = {"--a", "-b", "-c", "1", "2", "--d", "3", "4", "5", "-e", "6", "--f", "7"};

        Parser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        String[] expectedArgs = {"--a", "-b", "--c", "1", "--d", "3", "4", "5", "-e", "6", "--f", "7"};
        Assert.assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E");
        options.addOption("f", false, "Option F");

        String[] args = {"--a", "-b", "-c", "1", "2", "--d", "3", "4", "5", "-e", "6", "nonOption"};

        Parser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        String[] expectedArgs = {"--a", "-b", "1", "2", "nonOption"};
        Assert.assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test
    public void testBursting() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");
        options.addOption("cd", true, "Option CD");

        String[] args = {"--abcd", "-a-b--cd"};

        Parser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        String[] expectedArgs = {"--ab", "--cd"};
        Assert.assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"--x", "-a"};

        Parser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}