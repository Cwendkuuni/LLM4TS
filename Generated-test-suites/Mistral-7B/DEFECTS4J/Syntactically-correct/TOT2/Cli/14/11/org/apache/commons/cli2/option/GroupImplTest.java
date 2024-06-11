import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.Parser;
import org.apache.commons.cli2.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupWithOptions() throws Exception {
        final String[] args = new String[]{"--group", "--option1", "--option2"};

        final Options options = new Options();
        options.addGroup(new GroupOptions("group"));
        options.getGroup("group").addOption(Option.builder("option1").build());
        options.getGroup("group").addOption(Option.builder("option2").build());

        final Parser parser = new DefaultParser();
        final CommandLine cl = parser.parse(options, args);

        Assert.assertEquals(2, cl.getOptions().size());
        Assert.assertTrue(cl.hasOption("group"));
        Assert.assertTrue(cl.hasOption("option1"));
        Assert.assertTrue(cl.hasOption("option2"));
    }

    @Test(expected = OptionException.class)
    public void testGroupWithInvalidOption() throws Exception {
        final String[] args = new String[]{"--group", "--invalidOption"};

        final Options options = TestUtils.createTestOptions();
        options.addGroup(new GroupOptions("group"));

        final Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test(expected = OptionException.class)
    public void testGroupWithoutRequiredOption() throws Exception {
        final String[] args = new String[]{"--group"};

        final Options options = TestUtils.createTestOptions();
        options.addGroup(new GroupOptions("group")).required(true);

        final Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testGroupWithAnonymousArguments() throws Exception {
        final String[] args = new String[]{"--group", "arg1", "arg2"};

        final Options options = new Options();
        options.addGroup(new GroupOptions("group")).anonymous(true);
        options.getGroup("group").addOption(Option.builder("arg1").build());
        options.getGroup("group").addOption(Option.builder("arg2").build());

        final Parser parser = new DefaultParser();
        final CommandLine cl = parser.parse(options, args);

        Assert.assertEquals(2, cl.getOptions().size());
        Assert.assertEquals("arg1", cl.getOptionValues("arg1").get(0));
        Assert.assertEquals("arg2", cl.getOptionValues("arg2").get(0));
    }

    @Test
    public void testGroupWithMaximumOptions() throws Exception {
        final String[] args = new String[]{"--group", "--option1", "--option1", "--option2"};

        final Options options = new Options();
        options.addGroup(new GroupOptions("group"));
        options.getGroup("group").addOption(Option.builder("option1").build())
                .required(false)
                .maxOccurs(2);
        options.getGroup("group").addOption(Option.builder("option2").build());

        final Parser parser = new DefaultParser();
        final CommandLine cl = parser.parse(options, args);

        Assert.assertEquals(3, cl.getOptions().size());
        Assert.assertTrue(cl.hasOption("group"));
        Assert.assertTrue(cl.hasOption("option1"));
        Assert.assertTrue(cl.hasOption("option1"));
        Assert.assertTrue(cl.hasOption("option2"));
    }

    @Test(expected = OptionException.class)
    public void testGroupWithMinimumMaximumMismatch() throws Exception {
        final String[] args = new String[]{"--group"};

        final Options options = new Options();
        options.addGroup(new GroupOptions("group")).minOccurs(2);

        final Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testGroupWithTriggers() throws Exception {
        final String[] args = new String[]{"--group=mygroup", "--mygroup=value"};

        final Options options = new Options();
        options.addGroup(new GroupOptions("group")).trigger("mygroup");

        final Parser parser = new DefaultParser();
        final CommandLine cl = parser.parse(options, args);

        Assert.assertEquals("value", cl.getOptionValues("mygroup").get(0));
        Assert.assertTrue(cl.hasOption("group"));
    }

    @Test
    public void testGroupWithNestedGroups() throws Exception {
        final String[] args = new String[]{"--group1", "--group2.option1", "--group2.option2"};

        final Options options = new Options();
        options.addGroup(new GroupOptions("group1"));
        options.getGroup("group1").addGroup(new GroupOptions("group2"));
        options.getGroup("group2").addOption(Option.builder("option1").build());
        options.getGroup("group2").addOption(Option.builder("option2").build());

        final Parser parser = new DefaultParser();
        final CommandLine cl = parser.parse(options, args);

        Assert.assertEquals(2, cl.getOptions().size());
        Assert.assertTrue(cl.hasOption("group1"));
        Assert.assertTrue(cl.hasOption("group2"));
        Assert.assertTrue(cl.hasOption("group2.option1"));
        Assert.assertTrue(cl.hasOption("group2.option2"));
    }

    @Test
    public void testGroupWithMixedOptionsAndArguments() throws Exception {
        final String[] args = new String[]{"--group", "--option1", "arg1", "--option2=arg2"};

        final Options options = new Options();
        options.addGroup(new GroupOptions("group"));
        options.getGroup("group").addOption(Option.builder("option1").build());
        options.getGroup("group").addOption(Option.builder("option2").withArgName("arg").build());

        final Parser parser = new DefaultParser();
        final CommandLine cl = parser.parse(options, args);

        Assert.assertEquals(3, cl.getOptions().size());
        Assert.assertTrue(cl.hasOption("group"));
        Assert.assertTrue(cl.hasOption("option1"));
        Assert.assertArrayEquals(new String[]{"arg1"}, cl.getOptionValues("option2"));
    }

    @Test
    public void testGroupWithOuterGroup() throws Exception {
        final String[] args = new String[]{"--outerGroup", "--group=mygroup", "--mygroup=value"};

        final Options options = new Options();
        options.addGroup(new GroupOptions("outerGroup"));
        options.getGroup("outerGroup").addGroup(new GroupOptions("group")).trigger("mygroup");

        final Parser parser = new DefaultParser();
        final CommandLine cl = parser.parse(options, args);

        Assert.assertEquals("value", cl.getOptionValues("mygroup").get(0));
        Assert.assertTrue(cl.hasOption("outerGroup"));
        Assert.assertTrue(cl.hasOption("group"));
    }
}

class GroupOptions extends GroupImpl {
    public GroupOptions(final String name) {
        super(TestUtils.createTestOptions().getOptions(), name, null, 0, 0);
    }

    public GroupOptions(final String name, final int minOccurs) {
        super(TestUtils.createTestOptions().getOptions(), name, null, minOccurs, 0);
    }

    public GroupOptions(final String name, final int minOccurs, final int maxOccurs) {
        super(TestUtils.createTestOptions().getOptions(), name, null, minOccurs, maxOccurs);
    }

    public GroupOptions(final List options, final String name, final String description) {
        super(options, name, description, 0, 0);
    }

    public GroupOptions(final List options, final String name, final String description, final int minOccurs) {
        super(options, name, description, minOccurs, 0);
    }

    public GroupOptions(final List options, final String name, final String description, final int minOccurs, final int maxOccurs) {
        super(options, name, description, minOccurs, maxOccurs);
    }

    public GroupOptions anonymous(final boolean anonymous) {
        if (anonymous) {
            for (final Option option : options) {
                if (option instanceof Argument) {
                    throw new IllegalStateException("Anonymous arguments are not supported for GroupOptions");
                }
            }
        }
        return (GroupOptions) super.clone();
    }

    public GroupOptions required(final boolean required) {
        if (required && (minOccurs < 1)) {
            throw new IllegalStateException("GroupOptions cannot be required with a minimum number of occurrences less than 1");
        }
        return (GroupOptions) super.clone();
    }

    public GroupOptions trigger(final String trigger) {
        final Set triggers = new HashSet();
        triggers.add(trigger);
        return (GroupOptions) super.clone();
    }
}