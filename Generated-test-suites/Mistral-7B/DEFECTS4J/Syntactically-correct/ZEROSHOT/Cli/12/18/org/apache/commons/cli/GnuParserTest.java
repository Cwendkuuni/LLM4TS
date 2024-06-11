import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GnuParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value", "--foo", "-abc", "-def=value", "arg1", "arg2", "--"};

        GnuParser parser = new GnuParser();
        String[] flattenedArguments = parser.parse(options, arguments);

        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("-D");
        expectedTokens.add("property");
        expectedTokens.add("value");
        expectedTokens.add("--");
        expectedTokens.add("foo");
        expectedTokens.add("abc");
        expectedTokens.add("def");
        expectedTokens.add("value");
        expectedTokens.add("arg1");
        expectedTokens.add("arg2");

        Assert.assertArrayEquals(expectedTokens.toArray(), flattenedArguments);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-Dproperty=value", "--", "arg"};

        GnuParser parser = new GnuParser();
        parser.parse(options, arguments);

        // This test should throw a ParseException as stopAtNonOption is set to true by default
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.acceptOptionsAfterOpt();
        String[] arguments = {"-Dproperty=value", "--", "arg"};

        GnuParser parser = new GnuParser();
        String[] flattenedArguments = parser.parse(options, arguments, true);

        List<String> expectedTokens = new ArrayList<>();
        expectedTokens.add("-D");
        expectedTokens.add("property");
        expectedTokens.add("value");

        Assert.assertArrayEquals(expectedTokens.toArray(), flattenedArguments);
    }
}