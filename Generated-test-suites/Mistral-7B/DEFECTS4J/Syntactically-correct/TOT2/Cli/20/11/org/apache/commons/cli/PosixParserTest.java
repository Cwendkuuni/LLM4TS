import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_SimpleOption() {
        String[] args = {"--option"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(Arrays.asList("--option"), Arrays.asList(result));
    }

    @Test
    public void testFlatten_OptionWithValue() {
        String[] args = {"--option", "value"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(Arrays.asList("--option", "value"), Arrays.asList(result));
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] args = {"--longOption", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "l");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(Arrays.asList("--longOption", "value"), Arrays.asList(result));
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = {"--option1", "--option2", "--option3"};
        Options options = new Options();
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "i");
        options.addOption("option3", true, "t");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(Arrays.asList("--option1", "--option2", "--option3"), Arrays.asList(result));
    }

    @Test
    public void testFlatten_SimpleArgument() {
        String[] args = {"arg"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(Arrays.asList("arg"), Arrays.asList(result));
    }

    @Test
    public void testFlatten_LongArgument() {
        String[] args = {"--arg", "value"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(Arrays.asList("--arg", "value"), Arrays.asList(result));
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"--option", "arg", "--no-option", "arg2"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(Arrays.asList("--option", "--no-option"), Arrays.asList(result));
    }

    @Test
    public void testFlatten_BurstedOption() {
        String[] args = {"--long-option-with-value"};
        Options options = new Options();
        options.addOption("longOptionWithValue", true);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(Arrays.asList("--long-option-with-value"), Arrays.asList(result));
    }

    @Test
    public void testFlatten_BurstedOptionWithValue() {
        String[] args = {"--long-option-with-value", "value"};
        Options options = new Options();
        options.addOption("longOptionWithValue", true);
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(Arrays.asList("--long-option-with-value", "value"), Arrays.asList(result));
    }
}