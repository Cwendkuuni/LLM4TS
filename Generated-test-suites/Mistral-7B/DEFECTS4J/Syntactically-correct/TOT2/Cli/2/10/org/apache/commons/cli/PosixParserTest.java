import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] arguments = new String[]{"--option1", "--option2=value", "-o", "option3", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "h", "help");
        options.addOption("option2", true, "v", "value");
        options.addOption("option3", true, null, "option3");

        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--option1", "--option2=value", "-o", "option3", "--", "arg1", "arg2"}, parsedArguments);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        String[] arguments = new String[]{"--invalidOption", "--option1", "arg1"};
        Options options = new Options();
        options.addOption("option1", true, "h", "help");

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
        // ParseException should be thrown here
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] arguments = new String[]{"--option1", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "h", "help");

        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"--option1", "arg1", "arg2"}, parsedArguments);
    }

}