package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionContext;
import org.apache.commons.cli2.ParseContext;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.commons.cli2.TestUtils.assertOptionPresent;
import static org.apache.commons.cli2.TestUtils.assertParseErrors;
import static org.apache.commons.cli2.TestUtils.printHelp;

public class GroupImplTest {

    private OptionContext context;
    private HelpFormatter helpFormatter;

    @Before
    public void setUp() {
        context = new OptionContext();
        helpFormatter = new HelpFormatter();
    }

    @Test
    public void testGroupWithOptions() {
        final List groupOptions = new ArrayList();
        groupOptions.add(OptionBuilder.withName("g1").withDescription("group 1").createOption());
        groupOptions.add(OptionBuilder.withName("g2").withDescription("group 2").createOption());

        final GroupImpl group = new GroupImpl(groupOptions, "MyGroup", "This is my group.", 1, Integer.MAX_VALUE);

        final ParseContext parseContext = new ParseContext();
        parseContext.setHelpFormatter(helpFormatter);

        // Test parsing with valid input
        final CommandLine commandLine1 = new CommandLine();
        group.parse(commandLine1, parseContext.getStream(), parseContext, null, groupOptions, true);
        assertOptionPresent(commandLine1, "g1");
        assertOptionPresent(commandLine1, "g2");

        // Test parsing with invalid input - missing option
        final CommandLine commandLine2 = new CommandLine();
        assertParseErrors(new String[]{"-x", "--g1"}, commandLine2, group, parseContext);

        // Test parsing with invalid input - too many options
        final CommandLine commandLine3 = new CommandLine();
        assertParseErrors(new String[]{"--g1", "--g2", "--g3"}, commandLine3, group, parseContext);

        // Test help formatting
        printHelp("test", helpFormatter, group, true);
    }
}