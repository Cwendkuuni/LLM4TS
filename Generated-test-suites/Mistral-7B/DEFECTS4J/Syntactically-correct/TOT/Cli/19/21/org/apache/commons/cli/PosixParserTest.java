import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] args = {"-a", "--optionB=valueB", "-c", "--optionD=valueD"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "--optionB", "valueB", "-c", "--optionD", "valueD"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--nonOption", "-a", "--optionB=valueB", "-c", "--optionD=valueD"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--nonOption", "-a", "--optionB", "valueB", "--optionD", "valueD"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] args = {"--invalidOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
        fail("Expected ParseException but no exception was thrown.");
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        String[] args = {"--longOptionWithValue=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--longOptionWithValue", "value"}, result);
    }

    @Test
    public void testFlattenWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "help");
        String[] args = {"-a", "arg1", "--optionB=valueB", "-c", "--optionD=valueD"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "arg1", "--optionB", "valueB", "-c", "--optionD", "valueD"}, result);
    }

    @Test
    public void testFlattenWithMultipleArguments() {
        Options options = new Options();
        options.addOption("a", true, "help");
        String[] args = {"-a", "arg1", "arg2", "--optionB=valueB", "-c", "--optionD=valueD"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "arg1", "arg2", "--optionB", "valueB", "-c", "--optionD", "valueD"}, result);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "help");
        options.addOption("b", false, "brief");
        String[] args = {"-a", "arg1", "--optionB", "-b", "--optionD=valueD"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "arg1", "--optionB", "--optionD", "valueD"}, result);
    }
}