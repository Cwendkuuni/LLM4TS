import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest
{
    @Test
    public void testSimpleOption() throws ParseException
    {
        String[] args = new String[]{"-a", "--optionA", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(args, options, true);

        Assert.assertArrayEquals(new String[]{"-a", "--optionA", "--optionB=valueB"}, parsedArgs);
        Assert.assertTrue(options.isSetOption("a"));
        Assert.assertTrue(options.isSetOption("b"));
        Assert.assertEquals("valueB", options.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException
    {
        String[] args = new String[]{"--missingOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.parse(args, options, false);

        // ParseException should be thrown
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException
    {
        String[] args = new String[]{"--invalid"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.parse(args, options, false);

        // ParseException should be thrown
    }

    @Test
    public void testLongOptionWithValue() throws ParseException
    {
        String[] args = new String[]{"--optionC=valueC"};
        Options options = new Options();
        options.addOption("c", true, "Option C");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(args, options, true);

        Assert.assertArrayEquals(new String[]{"--optionC=valueC"}, parsedArgs);
        Assert.assertTrue(options.isSetOption("c"));
        Assert.assertEquals("valueC", options.getOptionValue("c"));
    }

    @Test
    public void testMultipleOptionsWithValues() throws ParseException
    {
        String[] args = new String[]{"-a", "--optionB=valueB", "--optionC=valueC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(args, options, true);

        Assert.assertArrayEquals(new String[]{"-a", "--optionB=valueB", "--optionC=valueC"}, parsedArgs);
        Assert.assertTrue(options.isSetOption("a"));
        Assert.assertTrue(options.isSetOption("b"));
        Assert.assertTrue(options.isSetOption("c"));
        Assert.assertEquals("valueB", options.getOptionValue("b"));
        Assert.assertEquals("valueC", options.getOptionValue("c"));
    }

    @Test
    public void testLongOptionWithoutValue() throws ParseException
    {
        String[] args = new String[]{"--optionD"};
        Options options = new Options();
        options.addOption("d", false, "Option D");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(args, options, true);

        Assert.assertArrayEquals(new String[]{"--optionD"}, parsedArgs);
        Assert.assertTrue(options.isSetOption("d"));
    }

    @Test
    public void testMultipleLongOptionsWithoutValues() throws ParseException
    {
        String[] args = new String[]{"--optionE", "--optionF"};
        Options options = new Options();
        options.addOption("e", false, "Option E");
        options.addOption("f", false, "Option F");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(args, options, true);

        Assert.assertArrayEquals(new String[]{"--optionE", "--optionF"}, parsedArgs);
        Assert.assertTrue(options.isSetOption("e"));
        Assert.assertTrue(options.isSetOption("f"));
    }

    @Test
    public void testShortOption() throws ParseException
    {
        String[] args = new String[]{"-a", "--optionB=valueB", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(args, options, true);

        Assert.assertArrayEquals(new String[]{"-a", "--optionB=valueB", "-b"}, parsedArgs);
        Assert.assertTrue(options.isSetOption("a"));
        Assert.assertTrue(options.isSetOption("b"));
    }

    @Test
    public void testShortOptionWithArg() throws ParseException
    {
        String[] args = new String[]{"-c=valueC", "--optionD"};
        Options options = new Options();
        options.addOption("c", true, "Option C");
        options.addOption("d", false, "Option D");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(args, options, true);

        Assert.assertArrayEquals(new String[]{"-c=valueC", "--optionD"}, parsedArgs);
        Assert.assertTrue(options.isSetOption("c"));
        Assert.assertEquals("valueC", options.getOptionValue("c"));
        Assert.assertTrue(options.isSetOption("d"));
    }
}