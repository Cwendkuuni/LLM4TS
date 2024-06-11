import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", 2, "Option 2 with argument");
        options.addOption("option3", 3, "Option 3 with argument");

        String[] args = {"-option1", "--option2=value1", "--option3=value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"option1", "option2", "value1", "option3", "value2"}, result);
    }

    @Test
    public void testFlattenNoOptions() throws ParseException {
        Options options = new Options();

        String[] args = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenWithLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("longOption1", true, "Long Option 1");
        options.addOption("longOption2", 2, "Long Option 2 with argument");

        String[] args = {"--longOption1", "--longOption2=value1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"longOption1", "longOption2", "value1"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        Options options = new Options();

        String[] args = {"-invalidOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        // Should throw ParseException
        fail("ParseException not thrown");
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "Option 1");

        String[] args = {"-option1", "--invalidOption", "arg1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"option1"}, result);
    }
}