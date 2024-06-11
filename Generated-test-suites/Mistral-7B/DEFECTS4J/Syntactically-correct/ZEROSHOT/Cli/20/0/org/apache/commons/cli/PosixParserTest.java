import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", 2, "Option C with a positional argument");
        options.addOption("d", 3, "Option D with three positional arguments");

        String[] args = {"-a", "--b", "1", "--c", "arg1", "arg2", "--d", "arg3", "arg4", "arg5"};
        String[] expected = {"-a", "--b", "1", "--c", "arg1", "arg2"};
        List<String> tokens = new PosixParser().flatten(options, args, false);
        Assert.assertArrayEquals(expected.toString().split(","), tokens.toString().split(","));

        String[] args2 = {"-a", "--b=value", "--c=arg1", "--d=arg3,arg4,arg5"};
        String[] expected2 = {"-a", "--b=value", "--c=arg1", "--d", "arg3", "arg4", "arg5"};
        tokens = new PosixParser().flatten(options, args2, false);
        Assert.assertArrayEquals(expected2.toString().split(","), tokens.toString().split(","));

        String[] args3 = {"--", "arg1", "arg2"};
        String[] expected3 = {"--", "arg1", "arg2"};
        tokens = new PosixParser().flatten(options, args3, true);
        Assert.assertArrayEquals(expected3.toString().split(","), tokens.toString().split(","));

        String[] args4 = {"-a", "--non-option"};
        String[] expected4 = {"-a", "--non-option"};
        tokens = new PosixParser().flatten(options, args4, true);
        Assert.assertArrayEquals(expected4.toString().split(","), tokens.toString().split(","));

        String[] args5 = {"--non-option", "-a"};
        Assert.assertThrows(ParseException.class, () -> new PosixParser().flatten(options, args5, false));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("ab", true, "Option AB");
        options.addOption("ac", 1, "Option AC with an argument");

        String[] args = {"-a-b", "arg1", "-ac"};
        List<String> tokens = new PosixParser().flatten(options, args, false);
        String[] expected = {"-a", "--b", "arg1", "-ac"};
        Assert.assertArrayEquals(expected.toString().split(","), tokens.toString().split(","));

        String[] args2 = {"--ab", "arg1", "-ac"};
        List<String> tokens2 = new PosixParser().flatten(options, args2, false);
        String[] expected2 = {"--ab", "arg1", "-ac"};
        Assert.assertArrayEquals(expected2.toString().split(","), tokens2.toString().split(","));

        String[] args3 = {"-a-b-c", "arg1", "arg2"};
        List<String> tokens3 = new PosixParser().flatten(options, args3, false);
        String[] expected3 = {"-a", "--b", "arg1", "arg2"};
        Assert.assertArrayEquals(expected3.toString().split(","), tokens3.toString().split(","));

        String[] args4 = {"-a--b", "arg1", "arg2"};
        List<String> tokens4 = new PosixParser().flatten(options, args4, false);
        String[] expected4 = {"-a", "--b", "arg1", "arg2"};
        Assert.assertArrayEquals(expected4.toString().split(","), tokens4.toString().split(","));

        String[] args5 = {"--ab", "arg1", "-ac"};
        List<String> tokens5 = new PosixParser().flatten(options, args5, false);
        String[] expected5 = {"--ab", "arg1", "-ac"};
        Assert.assertArrayEquals(expected5.toString().split(","), tokens5.toString().split(","));

        String[] args6 = {"--ab", "arg1", "--ac=arg2"};
        List<String> tokens6 = new PosixParser().flatten(options, args6, false);
        String[] expected6 = {"--ab", "arg1", "--ac=arg2"};
        Assert.assertArrayEquals(expected6.toString().split(","), tokens6.toString().split(","));

        String[] args7 = {"--ac=arg1", "arg2"};
        List<String> tokens7 = new PosixParser().flatten(options, args7, false);
        String[] expected7 = {"--ac=arg1", "arg2"};
        Assert.assertArrayEquals(expected7.toString().split(","), tokens7.toString().split(","));

        String[] args8 = {"--ac=arg1", "--non-option"};
        Assert.assertThrows(ParseException.class, () -> new PosixParser().flatten(options, args8, false));
    }
}