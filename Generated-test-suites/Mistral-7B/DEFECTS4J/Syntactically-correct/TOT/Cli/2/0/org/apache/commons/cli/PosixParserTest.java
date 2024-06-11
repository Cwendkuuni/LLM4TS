import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenWithEmptyArguments() {
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assertions.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithValidOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with argument");
        String[] arguments = {"-a", "--b=1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assertions.assertArrayEquals(new String[]{"-a", "--b=1"}, result);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--invalid"};
        PosixParser parser = new PosixParser();
        try {
            String[] result = parser.flatten(options, arguments, false);
            Assertions.fail();
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("long-option-with-argument", true, "Long option with argument");
        String[] arguments = {"--long-option-with-argument"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assertions.assertArrayEquals(new String[]{"--long-option-with-argument"}, result);
    }

    @Test
    public void testFlattenWithMultipleLongOptions() {
        Options options = new Options();
        options.addOption("long-option1", true, "Long option 1");
        options.addOption("long-option2", true, "Long option 2");
        String[] arguments = {"--long-option1", "--long-option2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assertions.assertArrayEquals(new String[]{"--long-option1", "--long-option2"}, result);
    }

    @Test
    public void testFlattenWithShortAndLongOptions() {
        Options options = new Options();
        options.addOption("s", true, "Short option");
        options.addOption("l", "longOption", true, "Long option");
        String[] arguments = {"-s", "--longOption=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assertions.assertArrayEquals(new String[]{"-s", "--longOption=value"}, result);
    }

    @Test
    public void testFlattenWithBothShortAndLongOptions() {
        Options options = new Options();
        options.addOption("s", true, "Short option");
        options.addOption("l", "longOption", true, "Long option");
        options.addOption("a", true, "Another option");
        String[] arguments = {"-s", "-l", "--longOption=value", "--", "-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assertions.assertArrayEquals(new String[]{"-s", "--longOption=value", "--", "-a"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with argument");
        String[] arguments = {"--invalid", "-a", "--b=1", "-c"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        Assertions.assertArrayEquals(new String[]{"--invalid", "-a", "--b=1"}, result);
    }

    @Test
    public void testFlattenWithDoubleDash() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--", "-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assertions.assertArrayEquals(new String[]{"--", "-a"}, result);
    }

    @Test
    public void testFlattenWithMultipleDoubleDash() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assertions.assertArrayEquals(new String[]{"--", "--"}, result);
    }

    @Test
    public void testFlattenWithEmptyOptionValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--a=", "--a=   "};
        PosixParser parser = new PosixParser();
        try {
            String[] result = parser.flatten(options, arguments, false);
            Assertions.fail();
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testFlattenWithInvalidOptionValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--a=invalid"};
        PosixParser parser = new PosixParser();
        try {
            String[] result = parser.flatten(options, arguments, false);
            Assertions.fail();
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testFlattenWithMixedArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with argument");
        String[] arguments = {"--invalid", "-a", "--b=1", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assertions.assertArrayEquals(new String[]{"--invalid", "-a", "--b=1", "arg1", "arg2"}, result);
    }
}