import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenValidArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {"--optionA", "--optionB=valueB", "-c", "--optionC"};
        Options options = new Options();
        options.addOption("optionA", true);
        options.addOption("optionB", true, "valueB");
        options.addOption("optionC", false);

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--optionA", "--optionB", "valueB", "-c", "--optionC"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {"invalidArg"};
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenStopAtNonOptionArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {"--optionA", "-c", "--invalid", "arg"};
        Options options = new Options();
        options.addOption("optionA", true);

        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--optionA", "-c", "--invalid"}, result);
    }

    @Test
    public void testBurstingSingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "option"};
        Options options = new Options();
        options.addOption("option", true);

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-o", "--option"}, result);
    }

    @Test
    public void testBurstingOptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "option=value"};
        Options options = new Options();
        options.addOption("option", true, "value");

        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-o", "--option", "value"}, result);
    }

    @Test
    public void testBurstingInvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "invalidOption"};
        Options options = new Options();

        try {
            parser.flatten(options, args, false);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"-o", "--", "invalidOption"}, result);
    }

    @Test
    public void testBurstingStopAtNonOptionInvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "invalidOption"};
        Options options = new Options();

        try {
            parser.flatten(options, args, true);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }
    }
}