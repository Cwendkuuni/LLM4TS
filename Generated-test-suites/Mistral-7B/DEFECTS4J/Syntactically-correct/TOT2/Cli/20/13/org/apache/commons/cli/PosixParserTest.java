import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlattenBasic() throws ParseException {
        Options options = new Options();
        String[] args = {"--foo", "-b", "-c=value", "--", "-d", "-e", "--foo=bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "-b", "--", "-c", "value", "--foo=bar"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] args = {"--foo", "-b", "-c=value", "--", "-d", "-e", "--foo=bar", "--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--foo", "-b", "--", "arg1", "arg2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        Options options = new Options();
        String[] args = {"--invalidOption", "--foo"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenNoOption() throws ParseException {
        Options options = new Options();
        String[] args = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        String[] args = {"--foo", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Option option = options.getOption("foo");
        Assert.assertNotNull(option);
        Assert.assertEquals(option.getValue(), "value");
        Assert.assertArrayEquals(new String[]{"--foo"}, result);
    }

    @Test
    public void testProcessOptionNoValue() throws ParseException {
        Options options = new Options();
        String[] args = {"--foo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Option option = options.getOption("foo");
        Assert.assertNotNull(option);
        Assert.assertNull(option.getValue());
        Assert.assertArrayEquals(new String[]{"--foo"}, result);
    }

    @Test
    public void testBurstToken() throws ParseException {
        Options options = new Options();
        String[] args = {"--foo-bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Option option = options.getOption("foo");
        Assert.assertNotNull(option);
        Assert.assertEquals(option.getValue(), "bar");
        Assert.assertArrayEquals(new String[]{"--foo", "--"}, result);
    }

    @Test
    public void testBurstTokenWithArgs() throws ParseException {
        Options options = new Options();
        options.addOption("foo", true, "f");
        String[] args = {"--foo-bar", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Option option = options.getOption("foo");
        Assert.assertNotNull(option);
        Assert.assertArrayEquals(new String[]{"--foo", "--"}, result);
        Assert.assertTrue(option.hasArgs());
        Assert.assertEquals(option.getArgs().length, 2);
        Assert.assertEquals(option.getArgs()[0], "arg1");
        Assert.assertEquals(option.getArgs()[1], "arg2");
    }

    @Test(expected = ParseException.class)
    public void testBurstTokenInvalidOption() throws ParseException {
        Options options = new Options();
        String[] args = {"--invalidOption-bar"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testBurstTokenEmptyOption() throws ParseException {
        Options options = new Options();
        String[] args = {"--", "--"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}