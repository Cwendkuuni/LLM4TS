import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimpleOptions() throws ParseException {
        String[] args = {"-a", "--optionB=valueB", "-c", "--optionD=valueD"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--optionB", "valueB", "-c", "--optionD", "valueD"}, parsedArgs);
    }

    @Test
    public void testFlattenLongOptions() throws ParseException {
        String[] args = {"--longOptionA", "--longOptionB=valueB", "--longOptionC", "--longOptionD=valueD"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");
        options.addOption("longOptionC", true, "Long Option C");
        options.addOption("longOptionD", true, "Long Option D");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOptionA", "--longOptionB", "valueB", "--longOptionC", "--longOptionD", "valueD"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        String[] args = {"--invalidOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);

        // The test should fail with a ParseException
    }

    @Test
    public void testFlattenSingleDash() throws ParseException {
        String[] args = {"-"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertEquals(new String[]{"--"}, parsedArgs);
    }

    @Test
    public void testFlattenDoubleDash() throws ParseException {
        String[] args = {"--"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--"}, parsedArgs);
    }

    @Test
    public void testFlattenWithArg() throws ParseException {
        String[] args = {"-a", "argA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "argA"}, parsedArgs);
    }

    @Test
    public void testFlattenWithMultiArg() throws ParseException {
        String[] args = {"-a", "argA1", "argA2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "argA1", "argA2"}, parsedArgs);
    }

    @Test
    public void testFlattenWithArgAndDoubleDash() throws ParseException {
        String[] args = {"-a", "--", "argB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--", "argB"}, parsedArgs);
    }

    @Test
    public void testFlattenWithMultiArgAndDoubleDash() throws ParseException {
        String[] args = {"-a", "--", "argB1", "argB2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--", "argB1", "argB2"}, parsedArgs);
    }

    @Test
    public void testFlattenWithLongOptionAndArg() throws ParseException {
        String[] args = {"--longOptionA", "argA"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOptionA", "argA"}, parsedArgs);
    }

    @Test
    public void testFlattenWithLongOptionAndMultiArg() throws ParseException {
        String[] args = {"--longOptionA", "argA1", "argA2"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOptionA", "argA1", "argA2"}, parsedArgs);
    }

    @Test
    public void testFlattenWithLongOptionAndDoubleDash() throws ParseException {
        String[] args = {"--longOptionA", "--"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOptionA", "--"}, parsedArgs);
    }

    @Test
    public void testFlattenWithLongOptionAndMultiArgAndDoubleDash() throws ParseException {
        String[] args = {"--longOptionA", "--", "argB1", "argB2"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOptionA", "--", "argB1", "argB2"}, parsedArgs);
    }

    @Test
    public void testFlattenWithShortOptionWithArg() throws ParseException {
        String[] args = {"-a", "argA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "argA"}, parsedArgs);
    }

    @Test
    public void testFlattenWithShortOptionWithMultiArg() throws ParseException {
        String[] args = {"-a", "argA1", "argA2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "argA1", "argA2"}, parsedArgs);
    }

    @Test
    public void testFlattenWithShortOptionAndDoubleDash() throws ParseException {
        String[] args = {"-a", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--"}, parsedArgs);
    }

    @Test
    public void testFlattenWithShortOptionAndMultiArgAndDoubleDash() throws ParseException {
        String[] args = {"-a", "--", "argB1", "argB2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--", "argB1", "argB2"}, parsedArgs);
    }
}