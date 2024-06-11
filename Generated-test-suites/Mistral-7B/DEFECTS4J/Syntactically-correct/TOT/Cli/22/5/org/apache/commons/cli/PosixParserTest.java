import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testSimpleOption() {
        String[] args = {"-a", "--optionA", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertEquals(Arrays.asList("a", "b", "valueB"), Arrays.asList(parsedArgs));
    }

    @Test
    public void testLongOption() {
        String[] args = {"--longOption", "--longOption=value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertEquals(Arrays.asList("longOption", "value"), Arrays.asList(parsedArgs));
    }

    @Test(expected = ParseException.class)
    public void testUnknownOption() {
        String[] args = {"--unknownOption", "--optionA", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        fail("Expected ParseException to be thrown");
    }

    @Test
    public void testMixedOptionsAndArguments() {
        String[] args = {"-a", "--optionB", "arg1", "--optionC=argC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertEquals(Arrays.asList("a", "b", "arg1", "c", "argC"), Arrays.asList(parsedArgs));
    }

    @Test
    public void testStopAtNonOption() {
        String[] args = {"arg1", "--optionA", "arg2", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        assertEquals(Arrays.asList("arg1", "--optionA", "arg2", "--optionB=valueB"), Arrays.asList(parsedArgs));
    }

    @Test
    public void testBursting() {
        String[] args = {"-abc", "--optionC", "--optionD=valueD"};
        Options options = new Options();
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertEquals(Arrays.asList("-c", "-c", "-", "d", "valueD"), Arrays.asList(parsedArgs));
    }
}