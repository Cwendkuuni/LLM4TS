import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        String[] args = new String[0];
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] expected = new String[0];

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SimpleOptions() {
        String[] args = {"-a", "--optionB", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "--optionB", "-c"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptions() {
        String[] args = {"--longOptionA", "--longOptionB=value", "--longOptionC=value2"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");
        options.addOption("longOptionC", true, "Long Option C");

        PosixParser parser = new PosixParser();
        String[] expected = {"--longOptionA", "--longOptionB=value", "--longOptionC=value2"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = {"--invalidOption", "-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidLongOption() {
        String[] args = {"--invalidLongOptionA", "--longOptionB=value"};
        Options options = new Options();
        options.addOption("longOptionB", true, "Long Option B");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-a", "--nonOption", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "--nonOption"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_MixedOptionsAndArguments() {
        String[] args = {"-a", "--optionB", "-c", "argument"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_SpecialTokens() {
        String[] args = {"--", "--optionA=value"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] expected = {"--", "--optionA=value"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        String[] args = {"-"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] expected = {"-"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_Bursting() {
        String[] args = {"-ab", "--longOptionC=value3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("longOptionC", true, "Long Option C");

        PosixParser parser = new PosixParser();
        String[] expected = {"-a", "-b", "--longOptionC=value3"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }
}