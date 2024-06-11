import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with two arguments");
    }

    @Test
    public void testFlattenEmptyArgs() {
        String[] args = {};
        String[] expectedArgs = {};

        PosixParser parser = new PosixParser();
        String[] resultArgs = parser.flatten(options, args, false);

        assertEquals(expectedArgs.length, resultArgs.length);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenSingleArg() {
        String[] args = {"arg"};
        String[] expectedArgs = {"arg"};

        PosixParser parser = new PosixParser();
        String[] resultArgs = parser.flatten(options, args, false);

        assertEquals(expectedArgs.length, resultArgs.length);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenSingleHyphen() {
        String[] args = {"-"};
        String[] expectedArgs = {"-"};

        PosixParser parser = new PosixParser();
        String[] resultArgs = parser.flatten(options, args, false);

        assertEquals(expectedArgs.length, resultArgs.length);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenDoubleHyphen() {
        String[] args = {"--"};
        String[] expectedArgs = {"--"};

        PosixParser parser = new PosixParser();
        String[] resultArgs = parser.flatten(options, args, false);

        assertEquals(expectedArgs.length, resultArgs.length);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenSingleOption() {
        String[] args = {"-a"};
        String[] expectedArgs = {"-a"};

        PosixParser parser = new PosixParser();
        String[] resultArgs = parser.flatten(options, args, false);

        assertEquals(expectedArgs.length, resultArgs.length);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenSingleOptionWithArgument() {
        String[] args = {"-a", "arg"};
        String[] expectedArgs = {"-a", "arg"};

        PosixParser parser = new PosixParser();
        String[] resultArgs = parser.flatten(options, args, false);

        assertEquals(expectedArgs.length, resultArgs.length);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenOptionWithMultipleArguments() {
        String[] args = {"-c", "arg1", "arg2"};
        String[] expectedArgs = {"-c", "arg1", "arg2"};

        PosixParser parser = new PosixParser();
        String[] resultArgs = parser.flatten(options, args, false);

        assertEquals(expectedArgs.length, resultArgs.length);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"-z"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"-a", "arg", "-", "nonOption"};
        String[] expectedArgs = {"-a", "arg"};

        PosixParser parser = new PosixParser();
        String[] resultArgs = parser.flatten(options, args, true);

        assertEquals(expectedArgs.length, resultArgs.length);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenStopAtNonOptionWithInvalidOption() {
        String[] args = {"-z", "arg", "-"};

        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, args, true);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testFlattenBurstToken() {
        String[] args = {"-abc"};
        String[] expectedArgs = {"-a", "-c"};

        PosixParser parser = new PosixParser();
        String[] resultArgs = parser.flatten(options, args, false);

        assertEquals(expectedArgs.length, resultArgs.length);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenBurstTokenWithStopAtNonOption() {
        String[] args = {"-abc", "nonOption"};
        String[] expectedArgs = {"-a"};

        PosixParser parser = new PosixParser();
        String[] resultArgs = parser.flatten(options, args, true);

        assertEquals(expectedArgs.length, resultArgs.length);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenBurstTokenWithArgument() {
        String[] args = {"-ac", "arg"};
        String[] expectedArgs = {"-a", "-c", "arg"};

        PosixParser parser = new PosixParser();
        String[] resultArgs = parser.flatten(options, args, false);

        assertEquals(expectedArgs.length, resultArgs.length);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }
}