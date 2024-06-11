import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        String[] args = {"-o", "output.txt", "--foo"};
        String[] expected = {"output", "o", "output.txt", "--foo"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        options.addOption("f", "force", false, "force option");
        options.addOption("h", "help", false, "help option");
        String[] args = {"--help", "-f", "-o", "output.txt"};
        String[] expected = {"help", "--help", "f", "o", "output.txt"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenNoOptions() throws ParseException {
        Options options = new Options();
        String[] args = {"--", "arg1", "arg2", "arg3"};
        String[] expected = {"arg1", "arg2", "arg3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        String[] args = {"--", "-o", "output.txt", "arg1"};
        String[] expected = {"-o", "output.txt"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        String[] args = {"--unknown"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testReset() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertNull(parser.getCurrentOption());
        assertFalse(parser.isEatTheRest());
        assertNull(parser.getTokens());
    }

    @Test
    public void testBurstTokenSimple() throws ParseException {
        Options options = new Options();
        String token = "--foo";
        String[] expected = {"foo"};

        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenMultiple() throws ParseException {
        Options options = new Options();
        String token = "--foo-bar";
        String[] expected = {"foo", "bar"};

        PosixParser parser = new PosixParser();
        parser.burstToken(token, false);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenStopAtNonOption() throws ParseException {
        Options options = new Options();
        String token = "--foo-bar";
        String[] expected = {"--", "foo-bar"};

        PosixParser parser = new PosixParser();
        parser.burstToken(token, true);
        String[] result = (String[]) parser.getTokens().toArray(new String[parser.getTokens().size()]);
        assertArrayEquals(expected, result);
    }

}