import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {
    @Test
    public void testGroupWithValidOptions() {
        List<Option> options = new ArrayList<>();
        Option option1 = new OptionBuilder("option1", "o1")
                .longOpt("long-option1")
                .required(false)
                .build();
        options.add(option1);
        Option option2 = new OptionBuilder("option2", "o2")
                .longOpt("long-option2")
                .required(false)
                .build();
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "MyGroup", null, 1, 2, false);

        CommandLine cmd = new CommandLine();
        cmd.parse(group.parse());

        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyGroup", group.parse(), true);
    }

    @Test
    public void testGroupWithMinimumOptions() {
        List<Option> options = new ArrayList<>();
        Option option1 = new OptionBuilder("option1", "o1")
                .longOpt("long-option1")
                .required(false)
                .build();
        options.add(option1);

        GroupImpl group = new GroupImpl(options, "MyGroup", null, 1, 1, false);

        CommandLine cmd = new CommandLine();
        cmd.parse(group.parse());

        assertTrue(cmd.hasOption("option1"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyGroup", group.parse(), true);
    }

    @Test(expected = OptionException.class)
    public void testGroupWithMaximumOptions() {
        List<Option> options = new ArrayList<>();
        Option option1 = new OptionBuilder("option1", "o1")
                .longOpt("long-option1")
                .required(false)
                .build();
        options.add(option1);
        Option option2 = new OptionBuilder("option2", "o2")
                .longOpt("long-option2")
                .required(false)
                .build();
        options.add(option2);
        Option option3 = new OptionBuilder("option3", "o3")
                .longOpt("long-option3")
                .required(false)
                .build();
        options.add(option3);

        GroupImpl group = new GroupImpl(options, "MyGroup", null, 1, 2, false);

        CommandLine cmd = new CommandLine();
        cmd.parse(group.parse());

        cmd.addOption("option1");
        cmd.addOption("option2");
        cmd.addOption("option3");
    }

    @Test(expected = OptionException.class)
    public void testGroupWithMissingOption() {
        List<Option> options = new ArrayList<>();
        Option option1 = new OptionBuilder("option1", "o1")
                .longOpt("long-option1")
                .required(false)
                .build();
        options.add(option1);

        GroupImpl group = new GroupImpl(options, "MyGroup", null, 1, 1, false);

        CommandLine cmd = new CommandLine();
        cmd.parse(group.parse());

        cmd.removeOption("option1");
    }

    @Test
    public void testGroupWithAnonymousArguments() {
        List<Option> options = new ArrayList<>();
        Option argumentOption = new OptionBuilder("arg", "a")
                .argName("value")
                .required(false)
                .build();
        options.add(argumentOption);

        GroupImpl group = new GroupImpl(options, "MyGroup", null, 1, Integer.MAX_VALUE, false);

        CommandLine cmd = new CommandLine();
        cmd.parse(group.parse());

        cmd.addOption("arg", "value1");
        cmd.addOption("arg", "value2");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyGroup", group.parse(), true);
    }
}