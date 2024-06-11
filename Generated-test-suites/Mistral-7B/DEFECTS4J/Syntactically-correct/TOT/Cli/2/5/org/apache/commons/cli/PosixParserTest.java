import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten_NoOptions() {
        String[] args = new String[]{"myProgram", "--no-option", "value1", "value2"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"myProgram", "--no-option", "value1", "value2"}, parsedArgs);
    }

    @Test
    public void testFlatten_OneOption() {
        String[] args = new String[]{"myProgram", "-o", "output.txt", "--no-option", "value1", "value2"};
        Options options = new Options();
        options.addRequiredOption("o", "output", true, "File to write output to");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"myProgram", "-o", "output.txt"}, parsedArgs);
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = new String[]{"myProgram", "--option1", "value1", "--option2", "value2", "--option3", "value3"};
        Options options = new Options();
        options.addOption("o", "option1", false, "Option 1");
        options.addOption("o", "option2", false, "Option 2");
        options.addOption("o", "option3", false, "Option 3");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"myProgram", "--option1", "--option2", "--option3"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = new String[]{"myProgram", "--invalidOption", "--no-option", "value1", "value2"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = new String[]{"myProgram", "-o", "output.txt", "value1", "--no-option", "value2"};
        Options options = new Options();
        options.addRequiredOption("o", "output", true, "File to write output to");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"myProgram", "-o", "output.txt", "value1", "--no-option", "value2"}, parsedArgs);
    }

    @Test
    public void testFlatten_Bursting() {
        String[] args = new String[]{"myProgram", "-abcd", "--option", "value"};
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", false, "Option C");
        options.addOption("d", "optionD", false, "Option D");
        options.addOption("o", "option", true, "Option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"myProgram", "-a", "-b", "-c", "-d", "--option", "value"}, parsedArgs);
    }
}