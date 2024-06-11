import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenSimpleOption() {
        String[] args = new String[]{"-a", "--optionA", "--optionB=valueB", "valueC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        String[] expectedArgs = new String[]{"-a", "--optionA", "--optionB", "valueB", "valueC"};
        Assert.assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        String[] args = new String[]{"--longOption=value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        String[] expectedArgs = new String[]{"--longOption", "value"};
        Assert.assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test
    public void testFlattenLongOptionWithoutValue() {
        String[] args = new String[]{"--longOption"};
        Options options = new Options();
        options.addOption("longOption", false, "Long Option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        String[] expectedArgs = new String[]{"--longOption"};
        Assert.assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test
    public void testFlattenMultipleOptions() {
        String[] args = new String[]{"-a", "-b", "--optionC", "--optionD=valueD"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        String[] expectedArgs = new String[]{"-a", "-b", "--optionC", "--optionD"};
        Assert.assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = new String[]{"-a", "nonOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        String[] expectedArgs = new String[]{"-a", "nonOption"};
        Assert.assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = new String[]{"-x"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenMissingOptionValue() {
        String[] args = new String[]{"--optionB="};
        Options options = new Options();
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }
}