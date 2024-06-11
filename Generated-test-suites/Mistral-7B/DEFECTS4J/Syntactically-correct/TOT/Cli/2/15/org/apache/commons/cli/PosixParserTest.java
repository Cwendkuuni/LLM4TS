import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = {"--foo", "--bar=test", "-abc", "-d", "-e=value", "--", "-f", "-g"};
        Options options = new Options();
        options.addOption("foo", true, "A boolean option");
        options.addOption("bar", true, "An option with an argument");
        options.addOption("d", false, "An option without an argument");
        options.addOption("e", true, "An option with an optional argument");
        options.addOption("f", false, "Another option without an argument");
        options.addOption("g", false, "Yet another option without an argument");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--foo", "--bar", "test", "-a", "-b", "-c", "-d", "--", "-f", "-g"}, parsedArgs);

        // Test with stopAtNonOption = true
        parsedArgs = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--foo", "--bar=test", "-abc", "-d", "-e=value", "--", "-f", "-g"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        String[] args = {"--foo", "--bar=test", "-x", "-d", "-e=value", "--", "-f", "-g"};
        Options options = new Options();
        options.addOption("foo", true, "A boolean option");
        options.addOption("bar", true, "An option with an argument");
        options.addOption("d", false, "An option without an argument");
        options.addOption("e", true, "An option with an optional argument");
        options.addOption("f", false, "Another option without an argument");
        options.addOption("g", false, "Yet another option without an argument");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        // The following line should throw an ParseException
        parser.flatten(options, args, false);
    }
}