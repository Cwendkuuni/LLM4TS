import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "an option with boolean value");
        options.addOption("b", 1, "an option with an integer value");
    }

    @Test
    public void testFlattenSimple() {
        String[] args = {"-a"};
        String[] expected = {"-a"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBooleanValue() {
        String[] args = {"--booleanValue", "true"};
        String[] expected = {"--booleanValue", "true"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithIntegerValue() {
        String[] args = {"--integerValue", "42"};
        String[] expected = {"--integerValue", "42"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithNonExistentOption() {
        String[] args = {"--nonExistentOption"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, true);
    }

    @Test
    public void testFlattenWithMultipleOptions() {
        String[] args = {"-a", "--booleanValue", "true", "-b", "3"};
        String[] expected = {"-a", "--booleanValue", "true", "-b", "3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        String[] args = {"--invalidOption"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithLongOption() {
        String[] args = {"--longOption"};
        String[] expected = {"--longOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        String[] args = {"--longOptionValue", "this is a long option value"};
        String[] expected = {"--longOptionValue", "this is a long option value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithLongOptionValueMissingValue() {
        String[] args = {"--longOptionValue"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithMixedOptions() {
        String[] args = {"-a", "--booleanValue", "true", "-b", "3", "--longOptionValue", "this is a long option value"};
        String[] expected = {"-a", "--booleanValue", "true", "-b", "3", "--longOptionValue", "this is a long option value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"-a", "--booleanValue", "true", "-b", "3", "--nonExistentOption"};
        String[] expected = {"-a", "--booleanValue", "true", "--nonExistentOption"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(expected, result);
    }

    @After
    public void tearDown() {
        options = null;
    }
}