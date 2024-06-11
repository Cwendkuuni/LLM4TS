import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    @Test
    public void testGroupWithValidOptions() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Description for option A"));
        options.add(new Option("b", "optionB", false, "Description for option B"));

        Group group = new GroupImpl(options, "MyGroup", "This is my group", 1, 10, false);

        WriteableCommandLine commandLine = new BasicParseResult();
        group.process(commandLine, commandLine.getArgs().listIterator());

        // Check that the correct options have been processed
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
        assertFalse(commandLine.hasOption("c"));
    }

    @Test(expected = OptionException.class)
    public void testGroupWithInvalidOption() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Description for option A"));
        options.add(new Option("b", "optionB", false, "Description for option B"));

        Group group = new GroupImpl(options, "MyGroup", "This is my group", 1, 10, false);

        WriteableCommandLine commandLine = new BasicParseResult();
        commandLine.getArgs().add("invalidOption");
        group.process(commandLine, commandLine.getArgs().listIterator());

        fail("An OptionException should have been thrown");
    }

    @Test(expected = OptionException.class)
    public void testGroupWithInvalidArgument() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Description for option A"));
        options.add(new Option("b", "optionB", false, "Description for option B"));
        options.add(new Option("c", "optionC", true, "Description for option C") {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator<String> args)
                    throws OptionException {
                super.process(commandLine, args);
                assertFalse(commandLine.hasOption("a"));
                assertFalse(commandLine.hasOption("b"));
                assertTrue(commandLine.hasOption("c"));
                assertEquals("invalid", commandLine.getOptionValue("c"));
            }
        });

        Group group = new GroupImpl(options, "MyGroup", "This is my group", 1, 10, false);

        WriteableCommandLine commandLine = new BasicParseResult();
        commandLine.getArgs().add("--c=invalid");
        group.process(commandLine, commandLine.getArgs().listIterator());

        fail("An OptionException should have been thrown");
    }

    @Test
    public void testGroupWithMinimumOptions() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Description for option A"));
        options.add(new Option("b", "optionB", false, "Description for option B"));

        Group group = new GroupImpl(options, "MyGroup", "This is my group", 1, 1, false);

        WriteableCommandLine commandLine = new BasicParseResult();
        commandLine.getArgs().add("--a");
        group.process(commandLine, commandLine.getArgs().listIterator());

        // Check that the correct option has been processed
        assertTrue(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
    }

    @Test(expected = OptionException.class)
    public void testGroupWithLessThanMinimumOptions() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Description for option A"));
        options.add(new Option("b", "optionB", false, "Description for option B"));

        Group group = new GroupImpl(options, "MyGroup", "This is my group", 1, 1, false);

        WriteableCommandLine commandLine = new BasicParseResult();
        group.process(commandLine, commandLine.getArgs().listIterator());

        fail("An OptionException should have been thrown");
    }

    @Test
    public void testGroupWithMaximumOptions() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Description for option A"));
        options.add(new Option("b", "optionB", false, "Description for option B"));

        Group group = new GroupImpl(options, "MyGroup", "This is my group", 1, 10, false);

        WriteableCommandLine commandLine = new BasicParseResult();
        for (int i = 0; i < 11; i++) {
            commandLine.getArgs().add("--a");
        }
        group.process(commandLine, commandLine.getArgs().listIterator());

        // Check that all options have been processed
        for (Option option : options) {
            assertTrue(commandLine.hasOption(option.getLongOpt()));
        }
    }

    @Test(expected = OptionException.class)
    public void testGroupWithMoreThanMaximumOptions() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "optionA", true, "Description for option A"));
        options.add(new Option("b", "optionB", false, "Description for option B"));

        Group group = new GroupImpl(options, "MyGroup", "This is my group", 1, 10, false);

        WriteableCommandLine commandLine = new BasicParseResult();
        for (int i = 0; i < 12; i++) {
            commandLine.getArgs().add("--a");
        }
        group.process(commandLine, commandLine.getArgs().listIterator());

        fail("An OptionException should have been thrown");
    }
}