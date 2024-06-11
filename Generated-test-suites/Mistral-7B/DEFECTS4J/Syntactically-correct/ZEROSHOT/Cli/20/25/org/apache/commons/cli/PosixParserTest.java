import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenWithEmptyArgs() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {};
        String[] expectedArgs = {};

        String[] resultArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithSingleArgument() {
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] args = {"arg1"};
        String[] expectedArgs = {"arg1"};

        String[] resultArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithSingleShortOption() {
        Options options = new Options();
        options.addOption("a", true, "A short option");
        Parser parser = new PosixParser();
        String[] args = {"-a"};
        String[] expectedArgs = {"-a"};

        String[] resultArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithSingleLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "A long option");
        Parser parser = new PosixParser();
        String[] args = {"--longOption"};
        String[] expectedArgs = {"--longOption"};

        String[] resultArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithSingleLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "A long option");
        Parser parser = new PosixParser();
        String[] args = {"--longOption=value"};
        String[] expectedArgs = {"--longOption", "value"};

        String[] resultArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithMultipleShortOptions() {
        Options options = new Options();
        options.addOption("a", true, "A short option");
        options.addOption("b", true, "Another short option");
        Parser parser = new PosixParser();
        String[] args = {"-a", "-b"};
        String[] expectedArgs = {"-a", "-b"};

        String[] resultArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithMultipleLongOptions() {
        Options options = new Options();
        options.addOption("longOption1", true, "A long option");
        options.addOption("longOption2", true, "Another long option");
        Parser parser = new PosixParser();
        String[] args = {"--longOption1", "--longOption2"};
        String[] expectedArgs = {"--longOption1", "--longOption2"};

        String[] resultArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithShortOptionWithValue() {
        Options options = new Options();
        options.addOption("a", true, "A short option");
        Parser parser = new PosixParser();
        String[] args = {"-a", "value"};
        String[] expectedArgs = {"-a", "value"};

        String[] resultArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithLongOptionWithValue() {
        Options options = new Options();
        options.addOption("longOption", true, "A long option");
        Parser parser = new PosixParser();
        String[] args = {"--longOption", "value"};
        String[] expectedArgs = {"--longOption", "value"};

        String[] resultArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithShortOptionWithArgument() {
        Options options = new Options();
        options.addOption("a", 1, "A short option");
        Parser parser = new PosixParser();
        String[] args = {"-a", "arg"};
        String[] expectedArgs = {"-a", "arg"};

        String[] resultArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("longOption", 1, "A long option");
        Parser parser = new PosixParser();
        String[] args = {"--longOption", "arg"};
        String[] expectedArgs = {"--longOption", "arg"};

        String[] resultArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithMixedOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", true, "A short option");
        options.addOption("longOption", true, "A long option");
        Parser parser = new PosixParser();
        String[] args = {"-a", "--longOption", "arg"};
        String[] expectedArgs = {"-a", "--longOption", "arg"};

        String[] resultArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "A short option");
        Parser parser = new PosixParser();
        String[] args = {"-a", "arg1", "--nonOption", "arg2"};
        String[] expectedArgs = {"-a", "arg1"};

        String[] resultArgs = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithBursting() {
        Options options = new Options();
        options.addOption("longOption", true, "A long option");
        Parser parser = new PosixParser();
        String[] args = {"--longOption-arg"};
        String[] expectedArgs = {"--longOption", "arg"};

        String[] resultArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }

    @Test
    public void testFlattenWithBurstingAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("longOption", true, "A long option");
        Parser parser = new PosixParser();
        String[] args = {"--longOption-arg1", "--nonOption"};
        String[] expectedArgs = {"--longOption", "arg1"};

        String[] resultArgs = parser.flatten(options, args, true);
        Assert.assertArrayEquals(expectedArgs, resultArgs);
    }
}