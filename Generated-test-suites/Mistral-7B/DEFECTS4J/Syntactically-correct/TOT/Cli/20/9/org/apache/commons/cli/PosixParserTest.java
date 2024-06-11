import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testSimpleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        String[] arguments = {"-a", "--b"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a"}, parsedArgs);
        Assert.assertTrue(options.isSetOption("a"));
        Assert.assertFalse(options.isSetOption("b"));
    }

    @Test
    public void testLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", false, "Long Option B");

        String[] arguments = {"--longOptionA", "--longOptionB"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--longOptionA"}, parsedArgs);
        Assert.assertTrue(options.isSetOption("longOptionA"));
        Assert.assertFalse(options.isSetOption("longOptionB"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        Options options = new Options();

        String[] arguments = {"--missingOption"};
        PosixParser parser = new PosixParser();
        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testInvalidLongOption() throws ParseException {
        Options options = new Options();

        String[] arguments = {"--invalidLongOption"};
        PosixParser parser = new PosixParser();
        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testMixedShortAndLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("longOptionC", true, "Long Option C");

        String[] arguments = {"-a", "--longOptionC", "-b"};
        PosixParser parser = new PosixParser();
        parser.parse(options, arguments, false);
    }

    @Test
    public void testOptionsWithValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-a", "valueA", "-b", "valueB"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "valueA", "-b", "valueB"}, parsedArgs);
        Assert.assertEquals("valueA", options.getOptionValue("a"));
        Assert.assertEquals("valueB", options.getOptionValue("b"));
    }

    @Test
    public void testBurstedOptions() throws ParseException {
        Options options = new Options();
        options.addOption("ab", false, "Option AB");

        String[] arguments = {"-ab", "value"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-ab"}, parsedArgs);
        Assert.assertTrue(options.isSetOption("ab"));
    }

    @Test
    public void testBurstedOptionsWithValue() throws ParseException {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");

        String[] arguments = {"-ab", "value"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-ab"}, parsedArgs);
        Assert.assertEquals("value", options.getOptionValue("ab"));
    }
}