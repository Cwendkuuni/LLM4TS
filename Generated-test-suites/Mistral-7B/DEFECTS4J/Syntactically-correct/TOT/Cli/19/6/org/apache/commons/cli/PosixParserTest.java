import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.cli.ParsePosition.ParsingFailed;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], flattenedArgs);
    }

    @Test
    public void testFlattenSimpleArgs() {
        Options options = new Options();
        String[] args = {"--option1", "--option2=value"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--option1", "--option2=value"}, flattenedArgs);
    }

    @Test
    public void testFlattenWithBurstedArgs() {
        Options options = new Options();
        options.addOption("o", "option", true, "an option with value");
        String[] args = {"-o", "value1", "--option2=value2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-o", "option", "value1", "--option2=value2"}, flattenedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidArg() {
        Options options = new Options();
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
        fail("Expected ParseException but it didn't occur.");
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "an option with value");
        String[] args = {"--option1", "-o", "value1", "--option2=value2", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--option1", "-o", "option", "value1", "--option2=value2"}, flattenedArgs);
    }

    @Test
    public void testBurstToken() {
        String token = "-foo-bar";
        PosixParser parser = new PosixParser();
        List<String> burstedTokens = parser.burstToken(token, false);
        Assert.assertEquals(Arrays.asList("--foo", "--bar"), burstedTokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenWithEmptyToken() {
        String token = "";
        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        fail("Expected IllegalArgumentException but it didn't occur.");
    }
}