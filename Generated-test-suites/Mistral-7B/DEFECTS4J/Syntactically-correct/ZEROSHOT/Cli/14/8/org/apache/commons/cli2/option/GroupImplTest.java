import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        final Options options = new Options();

        final List<Option> groupOptions = new ArrayList<>();
        groupOptions.add(new Option("g", "group", true, "group name")
                .required(false)
                .desc("group description"));
        groupOptions.add(new Option("a", "anonymous", true, "anonymous argument")
                .required(false)
                .desc("anonymous argument description"));
        groupOptions.add(new Option("o", "option", true, "option description")
                .required(false)
                .desc("option description"));

        final Group group = new GroupImpl(groupOptions, "group", "group description", 1, 1);

        // test canProcess
        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.getOptions());

        Assert.assertTrue(group.canProcess(commandLine, "g"));
        Assert.assertTrue(group.canProcess(commandLine, "o"));
        Assert.assertTrue(group.canProcess(commandLine, "a"));
        Assert.assertFalse(group.canProcess(commandLine, "unknown"));

        // test getPrefixes
        Assert.assertEquals(group.getPrefixes(), Collections.singleton("g"));

        // test getTriggers
        Assert.assertEquals(group.getTriggers(), Collections.singletonList("g"));

        // test process
        commandLine.clear();
        final List<String> arguments = new LinkedList<>(Arrays.asList("g", "value"));
        group.process(commandLine, arguments.listIterator());

        Assert.assertEquals(commandLine.getOptionValue("group"), "value");
        Assert.assertEquals(commandLine.getOptionValues("anonymous").size(), 0);

        // test validate
        commandLine.clear();
        arguments.clear();
        arguments.add("g");
        arguments.add("value1");
        arguments.add("g");
        arguments.add("value2");
        group.validate(commandLine);

        Assert.assertEquals(commandLine.getOptionValues("group").size(), 1);
        Assert.assertEquals(commandLine.getOptionValues("group").get(0), "value1");

        // test getPreferredName
        Assert.assertEquals(group.getPreferredName(), "group");

        // test getDescription
        Assert.assertEquals(group.getDescription(), "group description");

        // test appendUsage
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, null, null);

        Assert.assertEquals(sb.toString(),
                "group description:\n" +
                        "  -g GROUP    group description\n");

        // test helpLines
        final List<HelpLine> helpLines = group.helpLines(0, null, null);

        Assert.assertEquals(helpLines.size(), 1);
        Assert.assertEquals(helpLines.get(0).getOptionSpec().getLongOpt(), "group");
        Assert.assertEquals(helpLines.get(0).getOptionSpec().getOpt(), "g");
        Assert.assertEquals(helpLines.get(0).getDescription(), "group description");

        // test getOptions
        Assert.assertEquals(group.getOptions(), options.getOptions());

        // test getAnonymous
        Assert.assertEquals(group.getAnonymous(), options.getArgList());

        // test findOption
        Assert.assertNull(group.findOption("unknown"));
        Assert.assertEquals(group.findOption("g"), group.findOption("group"));

        // test isRequired
        Assert.assertFalse(group.isRequired());

        // test defaults
        commandLine.clear();
        group.defaults(commandLine);

        Assert.assertNull(commandLine.getOptionValue("group"));
        Assert.assertNull(commandLine.getOptionValues("anonymous"));
    }
}