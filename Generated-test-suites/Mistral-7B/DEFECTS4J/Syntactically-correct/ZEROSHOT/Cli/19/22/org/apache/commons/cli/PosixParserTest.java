import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenBasic() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"--foo", "-o", "opt", "--bar=value", "arg1", "-a", "--", "arg3", "arg4"};
        String[] expected = {"--foo", "-o", "--", "--bar", "value", "arg1", "-a", "--", "arg3", "arg4"};

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"--foo", "-o", "opt", "--bar=value", "arg1", "-a", "--", "arg3", "arg4", "nonOption"};
        String[] expected = {"--foo", "-o", "--", "--bar", "value", "arg1", "-a", "--", "arg3", "arg4", "nonOption"};

        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenSingleOption() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"--foo-bar"};

        List<String> tokens = parser.flatten(options, args, false);
        Assert.assertEquals("--foo-bar", tokens.get(0));
        Assert.assertEquals("--foo", tokens.get(1));
        Assert.assertEquals("--", tokens.get(2));
        Assert.assertNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstTokenOptionWithArg() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"--foo-bar=value"};

        List<String> tokens = parser.flatten(options, args, false);
        Assert.assertEquals("--foo-bar", tokens.get(0));
        Assert.assertEquals("--foo", tokens.get(1));
        Assert.assertEquals("--", tokens.get(2));
        Assert.assertEquals("value", tokens.get(3));
        Assert.assertNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstTokenInvalidOption() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"--invalidOption"};

        List<String> tokens = parser.flatten(options, args, false);
        Assert.assertEquals("--invalidOption", tokens.get(0));
        Assert.assertEquals("--", tokens.get(1));
        Assert.assertNull(parser.getCurrentOption());
    }

    @Test(expected = IllegalStateException.class)
    public void testBurstTokenInvalidOptionWithStopAtNonOption() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"--invalidOption"};

        parser.flatten(options, args, true);
    }

    @Test
    public void testBurstTokenLongOption() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"--longOptionWithArg=value"};

        List<String> tokens = parser.flatten(options, args, false);
        Assert.assertEquals("--longOptionWithArg", tokens.get(0));
        Assert.assertEquals("--longOption", tokens.get(1));
        Assert.assertEquals("--", tokens.get(2));
        Assert.assertEquals("value", tokens.get(3));
        Assert.assertNull(parser.getCurrentOption());
    }

    @Test
    public void testBurstTokenLongOptionWithMultipleArgs() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"--longOptionWithMultipleArgs=value1 value2 value3"};

        List<String> tokens = parser.flatten(options, args, false);
        Assert.assertEquals("--longOptionWithMultipleArgs", tokens.get(0));
        Assert.assertEquals("--longOption", tokens.get(1));
        Assert.assertEquals("--", tokens.get(2));
        Assert.assertEquals("value1", tokens.get(3));
        Assert.assertEquals("value2", tokens.get(4));
        Assert.assertEquals("value3", tokens.get(5));
        Assert.assertNull(parser.getCurrentOption());
    }
}