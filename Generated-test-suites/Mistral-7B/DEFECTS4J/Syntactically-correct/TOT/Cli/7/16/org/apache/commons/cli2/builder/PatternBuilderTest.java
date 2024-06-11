import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.junit.Assert;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testSimpleOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p:");
        Option option = builder.create();

        CommandLineParser parser = new org.apache.commons.cli2.basic.DefaultParser();
        try {
            parser.parse(option.getArgs(), new String[]{"-p", "test"});
            Assert.assertEquals("test", option.getValue());
        } catch (ParseException e) {
            Assert.fail("Error parsing arguments: " + e.getMessage());
        }
    }

    @Test
    public void testRequiredOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p:!");
        Option option = builder.create();

        CommandLineParser parser = new org.apache.commons.cli2.basic.DefaultParser();
        try {
            parser.parse(option.getArgs(), new String[]{});
            Assert.fail("Expected a ParseException when a required option is not present");
        } catch (ParseException e) {
            Assert.assertEquals("Option p is required", e.getMessage());
        }

        parser.parse(option.getArgs(), new String[]{"-p", "test"});
        Assert.assertEquals("test", option.getValue());
    }

    @Test
    public void testClassOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("c:@java.lang.String");
        Option option = builder.create();

        CommandLineParser parser = new org.apache.commons.cli2.basic.DefaultParser();
        try {
            parser.parse(option.getArgs(), new String[]{"-c", "java.util.ArrayList"});
            Assert.assertEquals("java.util.ArrayList", option.getValue());
        } catch (ParseException e) {
            Assert.fail("Error parsing arguments: " + e.getMessage());
        }

        try {
            parser.parse(option.getArgs(), new String[]{"-c", "java.util.HashMap"});
            Assert.assertEquals("java.util.HashMap", option.getValue());
        } catch (ParseException e) {
            Assert.fail("Expected a ParseException when a class option has an incorrect value");
        }

        try {
            parser.parse(option.getArgs(), new String[]{});
            Assert.fail("Expected a ParseException when a required class option is not present");
        } catch (ParseException e) {
            Assert.assertEquals("Option c is required and must be followed by a class name", e.getMessage());
        }
    }

    @Test
    public void testNumberOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("%n:");
        Option option = builder.create();

        CommandLineParser parser = new org.apache.commons.cli2.basic.DefaultParser();
        try {
            parser.parse(option.getArgs(), new String[]{"-n", "123"});
            Assert.assertEquals("123", option.getValue());
        } catch (ParseException e) {
            Assert.fail("Error parsing arguments: " + e.getMessage());
        }

        try {
            parser.parse(option.getArgs(), new String[]{"-n", "abc"});
            Assert.fail("Expected a ParseException when a number option has an incorrect value");
        } catch (ParseException e) {
            Assert.assertEquals("Invalid number: \"abc\"", e.getMessage());
        }
    }

    @Test
    public void testDateOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("#d:");
        Option option = builder.create();

        CommandLineParser parser = new org.apache.commons.cli2.basic.DefaultParser();
        try {
            parser.parse(option.getArgs(), new String[]{"-d", "2022-12-31"});
            Assert.assertEquals("2022-12-31", option.getValue());
        } catch (ParseException e) {
            Assert.fail("Error parsing arguments: " + e.getMessage());
        }

        try {
            parser.parse(option.getArgs(), new String[]{"-d", "2022-12-32"});
            Assert.fail("Expected a ParseException when a date option has an incorrect value");
        } catch (ParseException e) {
            Assert.assertEquals("Invalid date: \"2022-12-32\"", e.getMessage());
        }
    }

    @Test
    public void testFileOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("<f:>");
        Option option = builder.create();

        CommandLineParser parser = new org.apache.commons.cli2.basic.DefaultParser();
        try {
            parser.parse(option.getArgs(), new String[]{"-f", "/path/to/file.txt"});
            Assert.assertEquals("/path/to/file.txt", option.getValue());
        } catch (ParseException e) {
            Assert.fail("Error parsing arguments: " + e.getMessage());
        }

        try {
            parser.parse(option.getArgs(), new String[]{});
            Assert.fail("Expected a ParseException when a required file option is not present");
        } catch (ParseException e) {
            Assert.assertEquals("Option f is required and must be followed by a file path", e.getMessage());
        }
    }

    @Test
    public void testUrlOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("/u:");
        Option option = builder.create();

        CommandLineParser parser = new org.apache.commons.cli2.basic.DefaultParser();
        try {
            parser.parse(option.getArgs(), new String[]{"-u", "https://www.example.com"});
            Assert.assertEquals("https://www.example.com", option.getValue());
        } catch (ParseException e) {
            Assert.fail("Error parsing arguments: " + e.getMessage());
        }

        try {
            parser.parse(option.getArgs(), new String[]{});
            Assert.fail("Expected a ParseException when a required url option is not present");
        } catch (ParseException e) {
            Assert.assertEquals("Option u is required and must be followed by a URL", e.getMessage());
        }
    }

    @Test
    public void testMixedOptions() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("p:s,c:@java.lang.String,#d:,<f:>,/u:");
        Option option = builder.create();

        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("PatternBuilderTest", option.getOptParser(), true);
        System.out.println(help);

        // Test parsing simple options
        CommandLineParser parser = new org.apache.commons.cli2.basic.DefaultParser();
        try {
            parser.parse(option.getArgs(), new String[]{
                    "-p", "testString",
                    "-c", "java.util.ArrayList",
                    "-d", "2022-12-31"});

            Assert.assertEquals("testString", option.getValueForOption("p"));
            Assert.assertEquals("java.util.ArrayList", option.getValueForOption("c"));
            Assert.assertEquals("2022-12-31", option.getValueForOption("d"));
        } catch (ParseException e) {
            Assert.fail("Error parsing arguments: " + e.getMessage());
        }

        // Test parsing file and url options
        try {
            parser.parse(option.getArgs(), new String[]{
                    "-f", "/path/to/file.txt",
                    "-u", "https://www.example.com"});

            Assert.assertEquals("/path/to/file.txt", option.getValueForOption("f"));
            Assert.assertEquals("https://www.example.com", option.getValueForOption("u"));
        } catch (ParseException e) {
            Assert.fail("Error parsing arguments: " + e.getMessage());
        }
    }
}