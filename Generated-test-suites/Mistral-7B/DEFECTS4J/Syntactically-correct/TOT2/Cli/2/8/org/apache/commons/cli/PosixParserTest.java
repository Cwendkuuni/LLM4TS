import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArguments() {
        String[] arguments = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenSimpleOptions() {
        String[] arguments = {"-a", "--b", "-c"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-a", "--b", "-c"}, result);
    }

    @Test
    public void testFlattenLongOptions() {
        String[] arguments = {"--longOption1", "--longOption2=value", "--longOption3"};
        Options options = new Options();
        options.addOption("longOption1", true, "l1");
        options.addOption("longOption2", true, "l2").setArgName("value");
        options.addOption("longOption3", true, "l3");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption1", "--longOption2=value", "--longOption3"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = {"--", "-a", "--", "-b", "-c", "--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"--", "-a", "--", "-b", "-c", "--"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] arguments = {"-invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenMisplacedEqualsSign() {
        String[] arguments = {"--option=", "-a"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenDoubleHyphen() {
        String[] arguments = {"--", "--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }
}