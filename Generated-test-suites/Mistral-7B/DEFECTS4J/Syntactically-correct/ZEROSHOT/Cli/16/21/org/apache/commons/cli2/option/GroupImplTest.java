import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.cli2.CommandLine.PARSE_FAILURE;
import static org.apache.commons.cli2.CommandLine.PARSE_SUCCESS;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        final List<Option> options = new ArrayList<>();
        options.add(createOption("option1", "option1", false));
        options.add(createOption("option2", "option2", true));
        options.add(createArgument("arg1", "arg1"));

        final Group group = new GroupImpl(options, "group", "Group test", 1, 2);

        Assert.assertEquals("group", group.getPreferredName());
        Assert.assertEquals("Group test", group.getDescription());
        Assert.assertEquals(1, group.getMinimum());
        Assert.assertEquals(2, group.getMaximum());
        Assert.assertTrue(group.canProcess(createCommandLine(), "option1"));
        Assert.assertTrue(group.canProcess(createCommandLine(), "option2"));
        Assert.assertTrue(group.canProcess(createCommandLine(), "arg1"));

        group.process(createCommandLine(Arrays.asList("option1", "option2")), createListIterator());
        Assert.assertEquals(PARSE_SUCCESS, createCommandLine().getParseResult().getStatus());

        group.validate(createCommandLine(Arrays.asList("option1", "option1")));

        try {
            group.validate(createCommandLine(Arrays.asList("option1")));
            Assert.fail();
        } catch (OptionException e) {
            // expected
        }

        group.process(createCommandLine(Arrays.asList("option1", "arg1")), createListIterator());
        Assert.assertEquals(PARSE_FAILURE, createCommandLine().getParseResult().getStatus());

        group.appendUsage(new StringBuilder(), Collections.emptySet(), null);

        final Options optionsParser = new Options();
        optionsParser.addGroup(group);
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", optionsParser, true);
    }

    private Option createOption(final String longName, final String shortName, final boolean required) {
        return Option.builder()
                .longOpt(longName)
                .optPosixStyle(shortName != null)
                .hasArg(false)
                .required(required)
                .build();
    }

    private Argument createArgument(final String name, final String argument) {
        return Argument.builder()
                .name(name)
                .usage(' ')
                .build();
    }

    private CommandLine createCommandLine() {
        return new CommandLine();
    }

    private CommandLine createCommandLine(final List<String> args) {
        final CommandLine commandLine = new CommandLine();
        commandLine.parseArgumentList(args.toArray(new String[0]), null);
        return commandLine;
    }

    private ListIterator<String> createListIterator() {
        return new ListIterator<String>() {
            private List<String> list = new ArrayList<>();

            @Override
            public boolean hasNext() {
                return list.hasNext();
            }

            @Override
            public String next() {
                return list.poll();
            }

            @Override
            public boolean hasPrevious() {
                return list.size() > 0;
            }

            @Override
            public String previous() {
                return list.remove(list.size() - 1);
            }
        };
    }

    private DefaultParser parser = new DefaultParser();
}