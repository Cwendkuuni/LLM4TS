import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        String[] args = new String[0];
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], results);
        parser.init();
    }

    @Test
    public void testFlattenSingleOption() {
        String[] args = {"--foo"};
        Options options = new Options();
        options.addOption("foo", false, null, "Print Foo");
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo"}, results);
        parser.init();
    }

    @Test
    public void testFlattenOptionWithValue() {
        String[] args = {"--foo=bar"};
        Options options = new Options();
        options.addOption("foo", true, null, "Foo value");
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "bar"}, results);
        parser.init();
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        String[] args = {"--long-option-with-value=someValue"};
        Options options = new Options();
        options.addOption("longOptionWithValue", true, null, "Long option value");
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option-with-value", "someValue"}, results);
        parser.init();
    }

    @Test
    public void testFlattenSingleDash() {
        String[] args = {"-"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-"}, results);
        parser.init();
    }

    @Test
    public void testFlattenTwoDashes() {
        String[] args = {"--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--"}, results);
        parser.init();
    }

    @Test
    public void testFlattenTwoLetterOption() {
        String[] args = {"-oo"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        try {
            String[] results = parser.flatten(options, args, false);
            Assert.fail();
        } catch (ParseException e) {
            //Expected
        }
        parser.init();
    }

    @Test
    public void testFlattenTwoLetterOptionWithValue() {
        String[] args = {"-oo=value"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        try {
            String[] results = parser.flatten(options, args, false);
            Assert.fail();
        } catch (ParseException e) {
            //Expected
        }
        parser.init();
    }

    @Test
    public void testFlattenTwoLetterOptionWithLongName() {
        String[] args = {"-oo", "--option-o"};
        Options options = new Options();
        options.addOption("o", false, null, "Option O");
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-oo", "--option-o"}, results);
        parser.init();
    }

    @Test
    public void testFlattenTwoLetterOptionWithLongNameAndValue() {
        String[] args = {"-oo=value", "--option-o=value2"};
        Options options = new Options();
        options.addOption("o", true, null, "Option O value");
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-oo", "value", "--option-o", "value2"}, results);
        parser.init();
    }

    @Test
    public void testFlattenMultipleOptions() {
        String[] args = {"--foo", "--bar"};
        Options options = new Options();
        options.addOption("foo", false, null, "Print Foo");
        options.addOption("bar", false, null, "Print Bar");
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "--bar"}, results);
        parser.init();
    }

    @Test
    public void testFlattenOptionWithArgument() {
        String[] args = {"--foo", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", 1, null, "Foo with argument");
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "arg1", "arg2"}, results);
        parser.init();
    }

    @Test
    public void testFlattenOptionWithMultipleArguments() {
        String[] args = {"--foo", "arg1", "arg2", "arg3"};
        Options options = new Options();
        options.addOption("foo", 3, null, "Foo with multiple arguments");
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--foo", "arg1", "arg2", "arg3"}, results);
        parser.init();
    }

    @Test
    public void testFlattenOptionWithLongNameAndArgument() {
        String[] args = {"--long-option-with-argument", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("longOptionWithArgument", 2, null, "Long option with argument");
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--long-option-with-argument", "arg1", "arg2"}, results);
        parser.init();
    }

    @Test
    public void testFlattenComplexOptions() {
        String[] args = {"--foo", "--bar=value", "--baz", "--long-option=value1,value2"};
        Options options = new Options();
        options.addOption("foo", false, null, "Print Foo");
        options.addOption("bar", true, null, "Bar value");
        options.addOption("baz", false, null, "Print Baz");
        options.addOption("longOption", 2, null, "Long option value");
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, false);
        List expected = Arrays.asList(new String[]{"--foo", "--bar", "value", "--baz", "--long-option", "value1", "value2"});
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), results);
        parser.init();
    }

    @Test
    public void testFlattenComplexOptionsWithStopAtNonOption() {
        String[] args = {"--foo", "--bar=value", "--baz", "--long-option=value1,value2", "arg"};
        Options options = new Options();
        options.addOption("foo", false, null, "Print Foo");
        options.addOption("bar", true, null, "Bar value");
        options.addOption("baz", false, null, "Print Baz");
        options.addOption("longOption", 2, null, "Long option value");
        PosixParser parser = new PosixParser();
        String[] results = parser.flatten(options, args, true);
        List expected = Arrays.asList(new String[]{"--foo", "--bar", "value", "--baz", "--long-option", "value1,value2"});
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), results);
        parser.init();
    }
}