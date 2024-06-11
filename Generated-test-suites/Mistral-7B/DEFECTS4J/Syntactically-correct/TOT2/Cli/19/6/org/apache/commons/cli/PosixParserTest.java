import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class PosixParserTest {

    @Test
    public void testFlattenBasic() throws ParseException {
        String[] args = new String[]{"-a", "--foo=bar", "-b", "--", "-c"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "--foo", "bar", "--", "-c");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        String[] args = new String[]{"--invalid"};
        Options options = new Options();
        Parser parser = new PosixParser();
        parser.flatten(options, args, false);
        // Parser should throw ParseException for unknown option
    }

    @Test
    public void testFlattenLongOptionWithValue() throws ParseException {
        String[] args = new String[]{"--longOption=value"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--longOption", "value");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenLongOptionWithoutValue() throws ParseException {
        String[] args = new String[]{"--longOption"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--longOption");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenMultipleLongOptions() throws ParseException {
        String[] args = new String[]{"--longOption1", "--longOption2=value2", "--longOption3"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--longOption1", "--longOption2", "value2", "--longOption3");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenStopAtNonOption() throws ParseException {
        String[] args = new String[]{"--stop", "-a", "--foo=bar", "-b", "--", "c"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("--stop", "-a", "--foo", "bar", "--", "c");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenBurstOption() throws ParseException {
        String[] args = new String[]{"-abcd"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "-b", "-c", "-d");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenBurstOptionWithValue() throws ParseException {
        String[] args = new String[]{"-abc=d"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "-b", "d");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenBurstOptionWithMultipleValues() throws ParseException {
        String[] args = new String[]{"-abc=d1 d2"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-a", "-b", "d1", "d2");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenBurstOptionWithStopAtNonOption() throws ParseException {
        String[] args = new String[]{"-abcd", "--test"};
        Options options = new Options();
        Parser parser = new PosixParser();
        parser.flatten(options, args, true);
        // Parser should not throw any exception but just return flattened arguments
    }

    @Test
    public void testFlattenBurstOptionWithStopAtNonOptionAndValue() throws ParseException {
        String[] args = new String[]{"-abc=d", "--test"};
        Options options = new Options();
        Parser parser = new PosixParser();
        parser.flatten(options, args, true);
        // Parser should not throw any exception but just return flattened arguments
    }

    @Test
    public void testFlattenBurstOptionWithStopAtNonOptionAndMultipleValues() throws ParseException {
        String[] args = new String[]{"-abc=d1 d2", "--test"};
        Options options = new Options();
        Parser parser = new PosixParser();
        parser.flatten(options, args, true);
        // Parser should not throw any exception but just return flattened arguments
    }
}