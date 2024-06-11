import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenSimpleOptions() throws ParseException {
        String[] args = {"-a", "--optionB", "-c", "--optionD=value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--optionB", "-c", "--optionD=value"}, result);
    }

    @Test
    public void testFlattenLongOptions() throws ParseException {
        String[] args = {"--longOptionA", "--long-optionB=value", "--long-optionC"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");
        options.addOption("longOptionC", true, "Long Option C");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOptionA", "--long-optionB=value", "--long-optionC"}, result);
    }

    @Test
    public void testFlattenWithMultiValueOption() throws ParseException {
        String[] args = {"--optionA=value1", "--optionA=value2"};
        Options options = new Options();
        options.addOption("a", true, "Option A", true);

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--optionA", "value1", "--optionA", "value2"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] args = {"-a", "--nonOption", "-b", "--nonOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"-a", "--nonOption", "-b", "--nonOption"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        String[] args = {"--invalidOption", "-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithMissingOption() throws ParseException {
        String[] args = {"--missingOption", "-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}