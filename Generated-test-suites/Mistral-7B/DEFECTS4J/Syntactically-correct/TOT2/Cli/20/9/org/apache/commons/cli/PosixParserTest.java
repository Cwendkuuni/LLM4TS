import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        String[] args = {};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] expected = {};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SimpleArgs() {
        String[] args = {"--option1", "-o", "value1", "--option2", "-v"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "v");

        String[] expected = {"--option1", "-o", "value1", "--option2", "-v"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"--option1", "-o", "value1", "--", "nonOption"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");

        String[] expected = {"--option1", "-o", "value1", "--", "nonOption"};
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionValue() {
        String[] args = {"--option1=longOptionValue"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option1", "longOptionValue");

        String[] expected = {"--option1=longOptionValue"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithValue() {
        String[] args = {"-o", "value1", "--option2", "-v"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "v");

        String[] expected = {"-o", "value1", "--option2", "-v"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithoutValue() {
        String[] args = {"-o"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");

        String[] expected = {"-o"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_ShortOptionWithArg() {
        String[] args = {"-o", "value1", "--option2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "arg");

        String[] expected = {"-o", "value1", "--option2"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = {"--option1", "-o", "value1", "--option2", "value2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "arg");

        String[] expected = {"--option1", "-o", "value1", "--option2", "value2"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MixedOptionsAndArgs() {
        String[] args = {"--option1", "-o", "value1", "--option2", "value2", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "arg");

        String[] expected = {"--option1", "-o", "value1", "--option2", "value2", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_WithArgs() {
        String[] args = {"--option1", "-o", "value1", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o");

        String[] expected = {"--option1", "-o", "value1", "--", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expected, result);
    }
}