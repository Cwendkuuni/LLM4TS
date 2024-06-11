import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpRequest;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    private Options options;
    private CommandLineParser parser;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", "alpha", false, "Option A");
        options.addOption("b", "beta", true, "Option B");
        options.addOption("c", "char", 'c', "Option C");
        options.addGroup(new Option.Builder("g")
                .longOpt("group")
                .description("Group of options")
                .addOption("d", "delta", false, "Option D in group")
                .addOption("e", "epsilon", true, "Option E in group")
                .build());
        parser = new DefaultParser();
    }

    @Test
    public void testBasic() throws ParseException {
        final HelpFormatter formatter = new HelpFormatter();
        final WriteableCommandLine cmd = new DefaultCommandLine();

        // parse with required options
        cmd.parse(parser, options.toArray(new Option[0]), true);
        Assert.assertEquals("Basic test", true, cmd.hasOption('a'));
        Assert.assertEquals("Basic test", true, cmd.hasOption('b'));
        Assert.assertEquals("Basic test", true, cmd.hasOption('c'));
        Assert.assertEquals("Basic test", true, cmd.hasOption('d'));
        Assert.assertEquals("Basic test", false, cmd.hasOption('e'));

        // parse with optional options
        List<Object> args = Arrays.asList("--alpha", "--beta", "--char", "--group=epsilon", "--group=delta");
        cmd.parse(parser, args.toArray(new Object[0]), true);
        Assert.assertEquals("Test with optional options", true, cmd.hasOption('a'));
        Assert.assertEquals("Test with optional options", true, cmd.hasOption('b'));
        Assert.assertEquals("Test with optional options", true, cmd.hasOption('c'));
        Assert.assertEquals("Test with optional options", true, cmd.hasOption('d'));
        Assert.assertEquals("Test with optional options", true, cmd.hasOption('e'));

        // parse with invalid option
        args = Arrays.asList("--gamma");
        try {
            cmd.parse(parser, args.toArray(new Object[0]), true);
            Assert.fail("Test with invalid option should have thrown ParseException");
        } catch (ParseException e) {
            // expected
        }

        // print help
        formatter.printHelp("Test", options);
    }

    @Test
    public void testGroup() throws ParseException {
        final HelpFormatter formatter = new HelpFormatter();
        final WriteableCommandLine cmd = new DefaultCommandLine();

        // parse with required group option
        cmd.parse(parser, Arrays.asList("--group=delta"), true);
        Assert.assertEquals("Test group required option", true, cmd.hasOption('d'));
        Assert.assertEquals("Test group required option", false, cmd.hasOption('e'));

        // parse with optional group option
        cmd.parse(parser, Arrays.asList("--group=delta", "--group=epsilon"), true);
        Assert.assertEquals("Test group optional option", true, cmd.hasOption('d'));
        Assert.assertEquals("Test group optional option", true, cmd.hasOption('e'));

        // parse with group option in wrong order
        try {
            cmd.parse(parser, Arrays.asList("--epsilon", "--group=delta"), true);
            Assert.fail("Test group option in wrong order should have thrown ParseException");
        } catch (ParseException e) {
            // expected
        }

        // parse with only group option
        try {
            cmd.parse(parser, Arrays.asList("--group"), true);
            Assert.fail("Test group only option should have thrown ParseException");
        } catch (ParseException e) {
            // expected
        }

        // print help
        formatter.printHelp("Test", options);
    }

    @Test
    public void testMissingOptions() throws ParseException {
        final HelpFormatter formatter = new HelpFormatter();
        final WriteableCommandLine cmd = new DefaultCommandLine();

        // parse with missing options
        try {
            cmd.parse(parser, Collections.emptyList().toArray(new Object[0]), true);
            Assert.fail("Test missing options should have thrown ParseException");
        } catch (ParseException e) {
            // expected
        }

        // print help
        formatter.printHelp("Test", options);
    }
}