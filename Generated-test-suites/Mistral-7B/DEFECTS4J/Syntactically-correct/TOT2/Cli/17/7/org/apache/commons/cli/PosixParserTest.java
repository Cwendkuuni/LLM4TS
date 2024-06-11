import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.ParsingArgumentRegex;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = new String[]{"--option1", "value1", "--option2", "value2"};
        Options options = new Options();
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("o", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--option1", "value1", "--option2", "value2");
        Assert.assertArrayEquals(expected.toArray(), parsedArgs);
    }

    @Test
    public void testFlattenWithLongOption() {
        String[] args = new String[]{"--long-option", "--long-option-value"};
        Options options = new Options();
        Option option = new Option("l", "long-option", true, "Long Option");
        option.setLongOpt("longOption");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("--long-option", "--long-option-value");
        Assert.assertArrayEquals(expected.toArray(), parsedArgs);
    }

    @Test
    public void testFlattenWithShortOption() {
        String[] args = new String[]{"-o", "option1", "-o", "value1", "--another-option"};
        Options options = new Options();
        Option option = new Option("o", "option1", true, "Option 1");
        options.addOption(option);
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("-o", "option1", "--", "value1", "--another-option");
        Assert.assertArrayEquals(expected.toArray(), parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        String[] args = new String[]{"--invalidOption"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        // An exception should be thrown
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = new String[]{"--option1", "value1", "-n", "nonOption", "--option2", "value2"};
        Options options = new Options();
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("o", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("--option1", "value1", "nonOption", "--option2", "value2");
        Assert.assertArrayEquals(expected.toArray(), parsedArgs);
    }
}