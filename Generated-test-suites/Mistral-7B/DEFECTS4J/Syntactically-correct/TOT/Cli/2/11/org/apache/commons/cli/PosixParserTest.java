import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();

        String[] expected = {};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NoOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2"};
        Options options = new Options();

        String[] expected = {"arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionA", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", false, null, "Option A");

        String[] expected = {"-a", "--optionA", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_OptionWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "valueA", "--optionB", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, null, "Option A");
        options.addOption("b", false, null, "Option B");

        String[] expected = {"-a", "valueA", "--optionB", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--optionA", "-b", "--optionB", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", false, null, "Option A");
        options.addOption("b", false, null, "Option B");

        String[] expected = {"-a", "--optionA", "-b", "--optionB", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-a", "--optionA", "arg3", "arg4"};
        Options options = new Options();
        options.addOption("a", false, null, "Option A");

        String[] expected = {"arg1", "-a", "--optionA"};
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-z", "--optionA", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", false, null, "Option A");

        parser.flatten(options, args, false);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertNull(parser.tokens);
        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
        assertNull(parser.options);
    }
}