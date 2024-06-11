import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for GnuParser
 */
public class GnuParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] arguments = new String[]{"-Dproperty=value", "--foo", "-bar", "arg1", "arg2", "--"};
        Options options = new Options();
        options.addPropertyOption("property", "Value for property");
        options.addOption("foo", "Option foo");

        GnuParser parser = new GnuParser();
        String[] flattened = parser.parse(options, arguments, true);

        String[] expected = new String[]{"-D", "property", "value", "--", "foo", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, flattened);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        String[] arguments = new String[]{"--invalid", "arg1", "arg2"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.parse(options, arguments, false);

        // This test should throw ParseException as the option --invalid does not exist
    }

    @Test
    public void testFlattenWithNoOption() throws ParseException {
        String[] arguments = new String[]{"arg1", "arg2"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] flattened = parser.parse(options, arguments, false);

        String[] expected = new String[]{"arg1", "arg2"};
        Assert.assertArrayEquals(expected, flattened);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] arguments = new String[]{"-Dproperty=value", "--foo", "-bar", "arg1", "arg2", "--", "nonOption"};
        Options options = new Options();
        options.addPropertyOption("property", "Value for property");
        options.addOption("foo", "Option foo");

        GnuParser parser = new GnuParser();
        String[] flattened = parser.parse(options, arguments, true);

        String[] expected = new String[]{"-D", "property", "value", "--", "foo", "arg1", "arg2"};
        Assert.assertArrayEquals(expected, flattened);

        // The following arguments should not be processed
        Assert.assertEquals("nonOption", flattened[flattened.length - 1]);
    }

    @Test
    public void testFlattenWithDoubleDash() throws ParseException {
        String[] arguments = new String[]{"--", "arg1", "arg2"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] flattened = parser.parse(options, arguments, false);

        String[] expected = new String[]{"--"};
        Assert.assertArrayEquals(expected, flattened);
    }
}