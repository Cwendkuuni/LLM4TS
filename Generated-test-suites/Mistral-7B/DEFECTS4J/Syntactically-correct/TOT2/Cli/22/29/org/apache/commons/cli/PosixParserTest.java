import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest
{
    @Test
    public void testFlattenSimple() throws ParseException
    {
        String[] args = {"--optionA", "arg1", "--optionB", "arg2"};
        Options options = new Options();
        options.addOption("optionA", true, "descA");
        options.addOption("optionB", 2, "descB");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(new String[]{"--optionA", "true", "--optionB", "arg2"});
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithArg() throws ParseException
    {
        String[] args = {"--optionA", "arg1", "--optionB=valueB", "--optionC=valueC"};
        Options options = new Options();
        options.addOption("optionA", true, "descA");
        options.addOption("optionB", true, "descB");
        options.addOption("optionC", true, "descC");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(new String[]{"--optionA", "true", "--optionB", "valueB", "--optionC", "valueC"});
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithLongArg() throws ParseException
    {
        String[] args = {"--longOption=value1", "--longOption2=value2"};
        Options options = new Options();
        options.addOption("longOption", true, "desc");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(new String[]{"--longOption", "value1", "--longOption", "value2"});
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() throws ParseException
    {
        String[] args = {"--unknownOption", "--optionA", "--optionB"};
        Options options = new Options();
        options.addOption("optionA", true, "descA");
        options.addOption("optionB", true, "descB");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        // an exception should be thrown
    }

    @Test
    public void testFlattenStopAtNonOption() throws ParseException
    {
        String[] args = {"--optionA", "arg1", "nonOption"};
        Options options = new Options();
        options.addOption("optionA", true, "descA");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(new String[]{"--optionA", "arg1", "nonOption"});
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenEmptyArgs() throws ParseException
    {
        String[] args = {};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList();
        Assert.assertArrayEquals(expected.toArray(), result);
    }
}