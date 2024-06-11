import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        assertEquals(new String[0], parsedArgs);
    }

    @Test
    public void testFlattenSimpleArgs() {
        Options options = new Options();
        options.addOption("a", true, "an option with a boolean flag");
        String[] args = {"-a"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a"}, parsedArgs);
    }

    @Test
    public void testFlattenComplexArgs() {
        Options options = new Options();
        options.addOption("a", true, "an option with a boolean flag");
        options.addOption("b", 1, "an option with an integer value");
        String[] args = {"--a", "valueA", "--b", "5", "--"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--a", "valueA", "--b", "5"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() {
        Options options = new Options();
        String[] args = {"--unknown"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
        fail("Expected ParseException");
    }

    @Test
    public void testFlattenMultipleUnknownOptions() {
        Options options = new Options();
        String[] args = {"--unknown1", "--unknown2"};
        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, args, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        String[] parsedArgs = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--unknown1", "--unknown2"}, parsedArgs);
    }

    @Test
    public void testFlattenLongOptions() {
        Options options = new Options();
        options.addOption("longOption", true, "a long option with a boolean flag");
        String[] args = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        assertEquals(new String[]{"--longOption"}, parsedArgs);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "a long option with a boolean flag");
        options.addOption("longOptionValue", true, "a long option with a value");
        String[] args = {"--longOptionValue", "value"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--longOptionValue", "value"}, parsedArgs);
    }

    @Test
    public void testFlattenBurstedOption() {
        Options options = new Options();
        options.addOption("long", true, "a long option with a boolean flag");
        String[] args = {"--long-option"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        assertEquals(new String[]{"--long-option"}, parsedArgs);
    }

    @Test
    public void testFlattenBurstedOptionWithValue() {
        Options options = new Options();
        options.addOption("long", true, "a long option with a boolean flag");
        String[] args = {"--long--option-value"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--long--option-value"}, parsedArgs);
    }

    @Test
    public void testFlattenMultipleBurstedOptions() {
        Options options = new Options();
        options.addOption("long1", true, "a long option with a boolean flag");
        options.addOption("long2", true, "a long option with a boolean flag");
        String[] args = {"--long1--", "--long2--value"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--long1--", "--long2--value"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownLongOption() {
        Options options = new Options();
        String[] args = {"--unknown-long"};
        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, args, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        String[] parsedArgs = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--unknown-long"}, parsedArgs);
    }

    @Test
    public void testFlattenMultipleUnknownLongOptions() {
        Options options = new Options();
        String[] args = {"--unknown-long1", "--unknown-long2"};
        PosixParser parser = new PosixParser();
        try {
            parser.flatten(options, args, false);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        String[] parsedArgs = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--unknown-long1", "--unknown-long2"}, parsedArgs);
    }
}