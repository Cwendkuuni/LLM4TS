import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E");
        parser = new PosixParser();
    }

    @After
    public void tearDown() {
        parser = null;
        options = null;
    }

    @Test
    public void testEmptyArgs() {
        String[] args = {};
        String[] expected = {};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSingleArg() {
        String[] args = {"arg1"};
        String[] expected = {"arg1"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testSingleOption() {
        String[] args = {"-a"};
        String[] expected = {"-a"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
        Assert.assertEquals(options.getOption("a"), parser.getCurrentOption());
    }

    @Test
    public void testSingleOptionWithArg() {
        String[] args = {"-a", "arg2"};
        String[] expected = {"-a", "arg2"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
        Assert.assertEquals(options.getOption("a"), parser.getCurrentOption());
    }

    @Test
    public void testDoubleOption() {
        String[] args = {"--option", "arg1", "--option", "arg2"};
        String[] expected = {"--option", "arg1", "--option", "arg2"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testDoubleOptionWithArgs() {
        String[] args = {"--option", "arg1", "--option", "arg1", "arg2"};
        String[] expected = {"--option", "arg1", "--option", "arg2"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testDoubleOptionWithDifferentNames() {
        String[] args = {"--optionA", "arg1", "--optionB", "arg2"};
        String[] expected = {"--optionA", "arg1", "--optionB", "arg2"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalOption() {
        String[] args = {"-x"};
        parser.flatten(options, args, false);
    }

    @Test
    public void testStopAtNonOption() {
        String[] args = {"arg1", "-a", "-b", "arg3"};
        String[] expected = {"arg1", "-a", "-b", "arg3"};
        String[] actual = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBurstToken() {
        String[] args = {"--option-ab"};
        String[] expected = {"--option", "-a", "b"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBurstTokenWithArg() {
        String[] args = {"--option-abc"};
        String[] expected = {"--option", "-a", "-c"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBurstTokenWithMultipleArgs() {
        String[] args = {"--option-abcd"};
        String[] expected = {"--option", "-a", "-b", "-c", "d"};
        String[] actual = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        String[] args = {"--option-abcd", "arg1"};
        String[] expected = {"--option", "-a", "-b", "-c", "d", "arg1"};
        String[] actual = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, actual);
    }
}