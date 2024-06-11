import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testSimpleOption() throws ParseException {
        String[] args = {"-a", "--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(new String[]{"--optionA"}, parsedArgs);
    }

    @Test
    public void testOptionWithArgument() throws ParseException {
        String[] args = {"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(new String[]{"--optionA", "value"}, parsedArgs);
    }

    @Test
    public void testLongOption() throws ParseException {
        String[] args = {"--longOption"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption"}, parsedArgs);
    }

    @Test
    public void testLongOptionWithArgument() throws ParseException {
        String[] args = {"--longOptionValue"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOptionValue"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        String[] args = {"-x", "--invalidOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test
    public void testDoubleDash() throws ParseException {
        String[] args = {"--", "--optionA", "value"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(new String[]{"--optionA", "value"}, parsedArgs);
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        String[] args = {"-a", "--optionB", "valueB", "-x", "xValue"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        Assert.assertArrayEquals(new String[]{"-a", "--optionB", "valueB"}, parsedArgs);
    }

    @Test
    public void testBursting() throws ParseException {
        String[] args = {"-foo", "--bar", "--baz=value", "valueB"};
        Options options = new Options();
        options.addOption("foo", false, "Foo");
        options.addOption("bar", true, "Bar");
        options.addOption("baz", true, "Baz");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "--bar", "--baz", "value"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testInvalidBursting() throws ParseException {
        String[] args = {"-foo", "--ba", "--baz=value"};
        Options options = new Options();
        options.addOption("foo", false, "Foo");
        options.addOption("ba", true, "Ba");

        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);
    }
}