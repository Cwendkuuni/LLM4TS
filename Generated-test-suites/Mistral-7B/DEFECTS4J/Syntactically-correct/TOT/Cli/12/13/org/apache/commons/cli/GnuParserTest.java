import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for GnuParser's flatten method.
 */
public class GnuParserTest {

    @Test
    public void testFlattenBasic() {
        String[] args = {"-Dprop1=val1", "-Dprop2=val2", "--", "-optA", "-optB=valB"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-D", "prop1", "val1", "-D", "prop2", "val2", "--", "-optA", "-optB", "valB"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"-Dprop1=val1", "-Dprop2=val2", "--", "-nonOpt", "-optB=valB"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"-D", "prop1", "val1", "-D", "prop2", "val2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"-invalidOption", "-Dprop1=val1", "-Dprop2=val2"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenDuplicateOption() {
        String[] args = {"-Dprop=val1", "-Dprop=val2", "-Dprop=val3"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenNoOption() {
        String[] args = {"--", "arg1", "arg2"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenDoubleDash() {
        String[] args = {"--arg1", "--arg2"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);
    }
}