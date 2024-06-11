import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultParser;
import org.apache.commons.cli2.exceptions.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GroupImplTest {

    @Test
    public void testValidGroup() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                List.of(
                        new Option("g", "group", true, "group description")
                                .required(false)
                                .optionalArg(true)
                                .desc("a group option"),
                        new Option("a", "anonymous", true, "anonymous description")
                                .required(false)
                                .optionalArg(true)
                                .desc("an anonymous argument"),
                        new Option("i", "intOption", true, "an integer option")
                                .longOpt("integer")
                                .required(true)
                                .desc("an integer option"),
                        new Option("s", "stringOption", true, "a string option")
                                .longOpt("string")
                                .required(true)
                                .desc("a string option")),
                "group",
                "group description",
                List.of(),
                1,
                2);

        final DefaultParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options, new String[]{"g", "1", "s", "test", "a", "test"});

        Assert.assertEquals("1", cmd.getOptionValue("intOption"));
        Assert.assertEquals("test", cmd.getOptionValue("stringOption"));
        Assert.assertEquals("group", cmd.getOptionValue("group"));
        Assert.assertNull(cmd.getOptionValue("anonymous"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("TestGroup", options);
    }

    @Test(expected = ParseException.class)
    public void testInvalidGroup() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                List.of(
                        new Option("g", "group", true, "group description")
                                .required(false)
                                .optionalArg(true)
                                .desc("a group option"),
                        new Option("a", "anonymous", true, "anonymous description")
                                .required(false)
                                .optionalArg(true)
                                .desc("an anonymous argument"),
                        new Option("i", "intOption", true, "an integer option")
                                .longOpt("integer")
                                .required(true)
                                .desc("an integer option"),
                        new Option("s", "stringOption", true, "a string option")
                                .longOpt("string")
                                .required(true)
                                .desc("a string option")),
                "group",
                "group description",
                List.of(),
                1,
                2);

        final DefaultParser parser = new DefaultParser();
        parser.parse(options, new String[]{"g", "1", "s", "test", "g"});
    }

    @Test(expected = OptionException.class)
    public void testInvalidOptionInGroup() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                List.of(
                        new Option("g", "group", true, "group description")
                                .required(false)
                                .optionalArg(true)
                                .desc("a group option"),
                        new Option("a", "anonymous", true, "anonymous description")
                                .required(false)
                                .optionalArg(true)
                                .desc("an anonymous argument"),
                        new Option("i", "intOption", true, "an integer option")
                                .longOpt("integer")
                                .required(true)
                                .desc("an integer option"),
                        new Option("s", "stringOption", true, "a string option")
                                .longOpt("string")
                                .required(true)
                                .desc("a string option"),
                        new Option("x", "invalidOption", true, "an invalid option")
                                .longOpt("invalid")
                                .desc("an invalid option")),
                "group",
                "group description",
                List.of(),
                1,
                2);

        final DefaultParser parser = new DefaultParser();
        parser.parse(options, new String[]{"g", "1"});
    }

    @Test
    public void testEmptyGroup() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                List.of(),
                null,
                null,
                0,
                0);

        final DefaultParser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }

    @Test
    public void testNoOptionsInGroup() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                List.of(),
                null,
                null,
                0,
                0);

        final DefaultParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options, new String[0]);

        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testAnonymousArgumentInGroup() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                List.of(
                        new Option("g", "group", true, "group description")
                                .required(false)
                                .optionalArg(true)
                                .desc("a group option"),
                        new Option("a", "anonymous", true, "anonymous description")
                                .required(false)
                                .optionalArg(true)
                                .desc("an anonymous argument")),
                "group",
                "group description",
                List.of(),
                0,
                0);

        final DefaultParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options, new String[]{"-a", "test"});

        Assert.assertEquals("test", cmd.getOptionValue("anonymous"));
    }

    @Test
    public void testGroupWithEmptyList() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                Collections.emptyList(),
                null,
                null,
                0,
                0);

        final DefaultParser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }

    @Test
    public void testGroupWithNullName() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                List.of(
                        new Option("i", "intOption", true, "an integer option")
                                .longOpt("integer")
                                .required(true)
                                .desc("an integer option")),
                null,
                null,
                0,
                0);

        final DefaultParser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }

    @Test
    public void testGroupWithEmptyDescription() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                List.of(
                        new Option("i", "intOption", true, "an integer option")
                                .longOpt("integer")
                                .required(true)
                                .desc("")),
                "group",
                "",
                List.of(),
                0,
                0);

        final DefaultParser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }

    @Test
    public void testGroupWithNullMinimum() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                List.of(
                        new Option("i", "intOption", true, "an integer option")
                                .longOpt("integer")
                                .required(true)
                                .desc("an integer option")),
                "group",
                "group description",
                List.of(),
                0,
                null);

        final DefaultParser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }

    @Test
    public void testGroupWithNullMaximum() throws ParseException {
        final Options options = new Options();
        final Group group = new GroupImpl(
                List.of(
                        new Option("i", "intOption", true, "an integer option")
                                .longOpt("integer")
                                .required(true)
                                .desc("an integer option")),
                "group",
                "group description",
                List.of(),
                null,
                0);

        final DefaultParser parser = new DefaultParser();
        parser.parse(options, new String[0]);
    }
}