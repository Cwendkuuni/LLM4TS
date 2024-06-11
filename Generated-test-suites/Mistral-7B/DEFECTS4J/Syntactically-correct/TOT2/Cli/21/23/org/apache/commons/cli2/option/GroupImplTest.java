
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroupWithBasicOptions() {
        Options options = new Options();

        Option groupOption = new OptionBuilder("g")
                .withDescription("This is a group option")
                .hasArg(true)
                .isRequired(false)
                .create(false);

        Option option1 = new OptionBuilder("o1")
                .withDescription("This is an option")
                .isRequired(false)
                .create(false);

        Option option2 = new OptionBuilder("o2")
                .withDescription("This is another option")
                .isRequired(false)
                .create(false);

        options.addOption(groupOption);
        options.addOption(option1);
        options.addOption(option2);

        CommandLine cmd = new CommandLine();
        cmd.parse(options, new String[]{"--group", "groupValue1", "--option1", "optionValue1"});

        assertEquals("groupValue1", cmd.getOptionValue("group"));
        assertEquals("optionValue1", cmd.getOptionValue("option1"));
        assertNull(cmd.getOptionValue("option2"));
    }

    @Test
    public void testGroupWithMinimumAndMaximum() {
        Options options = new Options();

        Option groupOption = new OptionBuilder("g")
                .withDescription("This is a group option")
                .hasArg(true)
                .isRequired(true)
                .create(false);

        Option option1 = new OptionBuilder("o1")
                .withDescription("This is an option")
                .isRequired(false)
                .create(false);

        Option option2 = new OptionBuilder("o2")
                .withDescription("This is another option")
                .isRequired(false)
                .create(false);

        options.addOption(groupOption);
        options.addOption(option1);
        options.addOption(option2);

        CommandLine cmd = new CommandLine();
        cmd.parse(options, new String[]{"--group", "groupValue1", "--option1", "optionValue1"});

        assertEquals("groupValue1", cmd.getOptionValue("group"));
        assertEquals("optionValue1", cmd.getOptionValue("option1"));
        assertEquals("optionValue2", cmd.getOptionValue("option2"));

        cmd = new CommandLine();
        try {
            cmd.parse(options, new String[]{"--group", "groupValue1"});
            fail("At least 2 options are required for this group");
        } catch (ParseException e) {
            // expected
        }

        cmd = new CommandLine();
        try {
            cmd.parse(options, new String[]{"--group", "groupValue1", "--option1", "optionValue1", "--option1", "optionValue2"});
            fail("At most 2 options are allowed for this group");
        } catch (ParseException e) {
            // expected
        }
    }
}
