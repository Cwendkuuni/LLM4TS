import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.HelpFormatterFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GroupImplTest {

    private Group group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        options.add(Option.builder("o1")
                .required(true)
                .longOpt("opt1")
                .build());
        options.add(Option.builder("o2")
                .longOpt("opt2")
                .hasArg(true)
                .build());

        group = new GroupImpl(options, "MyGroup", "My Group", 1, 2, false);
        commandLine = new WriteableCommandLine("cmd");
    }

    

    @Test
    public void testCanProcess_ValidOption() {
        commandLine.parse(group.getOptions());
        assertTrue(group.canProcess(commandLine, "o1"));
    }

    @Test
    public void testCanProcess_BurstedOption() {
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        assertTrue(group.canProcess(commandLine, "o1"));
    }

    @Test
    public void testCanProcess_LooksLikeOption() {
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        assertFalse(group.canProcess(commandLine, "arg"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_InvalidOption() {
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        group.canProcess(commandLine, "invalidOption");
    }

    @Test
    public void testCanProcess_AnonymousArgument() {
        List<Argument> anonymousArguments = new ArrayList<>();
        anonymousArguments.add(Argument.builder("arg")
                .required(true)
                .build());

        group = new GroupImpl(Collections.emptyList(), null, null, 1, 1, false) {
            @Override
            public List getAnonymous() {
                return Collections.unmodifiableList(anonymousArguments);
            }
        };

        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse("arg", new String[]{"argValue"});

        assertTrue(group.canProcess(commandLine, "arg"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_AnonymousArgument_InvalidIndex() {
        List<Argument> anonymousArguments = new ArrayList<>();
        anonymousArguments.add(Argument.builder("arg1")
                .required(true)
                .build());
        anonymousArguments.add(Argument.builder("arg2")
                .required(true)
                .build());

        group = new GroupImpl(Collections.emptyList(), null, null, 1, 1, false) {
            @Override
            public List getAnonymous() {
                return Collections.unmodifiableList(anonymousArguments);
            }
        };

        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());

        assertFalse(group.canProcess(commandLine, "arg3"));
    }

    @Test
    public void testValidate_ValidOptions() {
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());

        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("validate should not throw an exception for valid options");
        }
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidOptions_Minimum() {
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());

        try {
            group.validate(commandLine);
            fail("validate should have thrown an exception for minimum not met");
        } catch (OptionException e) {
            assertEquals("Missing option 'o1'", e.getMessage());
        }
    }

    @Test(expected = OptionException.class)
    public void testValidate_InvalidOptions_Maximum() {
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());

        try {
            group.validate(commandLine);
            fail("validate should have thrown an exception for maximum exceeded");
        } catch (OptionException e) {
            assertEquals("Too many options", e.getMessage());
        }
    }

    @Test
    public void testProcess_ValidOptions() {
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());

        List<String> args = new ArrayList<>();
        args.add("argValue1");
        args.add("argValue2");

        group.process(commandLine, args.listIterator());

        assertEquals(2, commandLine.getOptions().size());
        assertEquals("opt1", commandLine.getOptionValues("o1")[0]);
        assertEquals("argValue1", commandLine.getOptionValues("o2")[0]);
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidOptions_Minimum() {
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());

        List<String> args = new ArrayList<>();
        args.add("argValue1");
        args.add("argValue2");

        try {
            group.process(commandLine, args.listIterator());
            fail("process should have thrown an exception for minimum not met");
        } catch (OptionException e) {
            assertEquals("Missing option 'o1'", e.getMessage());
        }
    }

    @Test(expected = OptionException.class)
    public void testProcess_InvalidOptions_Maximum() {
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());
        commandLine.parse(group.getOptions());

        List<String> args = new ArrayList<>();
        args.add("argValue1");
        args.add("argValue2");
        args.add("argValue3");

        try {
            group.process(commandLine, args.listIterator());
            fail("process should have thrown an exception for maximum exceeded");
        } catch (OptionException e) {
            assertEquals("Too many options", e.getMessage());
        }
    }

    @Test
    public void testHelpLines_ValidGroup() {
        StringWriter writer = new StringWriter();
        HelpFormatter formatter = HelpFormatterFactory.getHelpFormatter();

        List<HelpLine> helpLines = group.helpLines(1, Collections.emptySet(), null);

        formatter.printHelp(writer, "cmd", group.getOptions(), helpLines.toArray(new HelpLine[0]));

        String helpText = writer.toString();

        assertTrue(helpText.contains("My Group"));
        assertTrue(helpText.contains("o1"));
        assertTrue(helpText.contains("opt1"));
        assertTrue(helpText.contains("o2"));
        assertTrue(helpText.contains("opt2"));
    }

    @Test
    public void testHelpLines_EmptyGroup() {
        StringWriter writer = new StringWriter();
        HelpFormatter formatter = HelpFormatterFactory.getHelpFormatter();

        List<HelpLine> helpLines = new GroupImpl(Collections.emptyList(), null, null, 0, 0, false).helpLines(1, Collections.emptySet(), null);

        formatter.printHelp(writer, "cmd", Collections.emptyList(), helpLines.toArray(new HelpLine[0]));

        String helpText = writer.toString();

        assertTrue(helpText.isEmpty());
    }

    
}