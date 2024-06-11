import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_basic() {
        // given
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option option2 = new Option("s", "option2", false, "Option 2");
        Option rootOption = new Option("root", "root", true, "Root");
        rootOption.addOption(option1);
        rootOption.addOption(option2);
        List<String> arguments = new ArrayList<>();
        arguments.add("root");
        arguments.add("o");
        arguments.add("value1");
        arguments.add("s");
        arguments.add("true");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, arguments);

        // when

        // test getters
        assertEquals("root", cmd.getName());
        assertEquals(2, cmd.getOptions().size());
        assertEquals(2, cmd.getOptionTriggers().size());
        assertTrue(cmd.hasOption(option1));
        assertEquals(option1, cmd.getOption(option1.getPreferredName()));
        assertEquals(Arrays.asList("value1"), cmd.getValues(option1, null));
        assertTrue(cmd.hasOption(option2));
        assertEquals(option2, cmd.getOption(option2.getPreferredName()));
        assertEquals(Boolean.TRUE, cmd.getSwitch(option2, Boolean.FALSE));

        // test toString()
        assertEquals("root o value1 s true", cmd.toString());

        // when setting defaults
        WriteableCommandLineImpl cmdWithDefaults = new WriteableCommandLineImpl(rootOption, Collections.emptyList());
        cmdWithDefaults.setDefaultValues(option1, Arrays.asList("defaultValue1"));
        cmdWithDefaults.setDefaultSwitch(option2, Boolean.TRUE);

        // then
        assertEquals("root", cmdWithDefaults.getName());
        assertEquals(2, cmdWithDefaults.getOptions().size());
        assertEquals(2, cmdWithDefaults.getOptionTriggers().size());
        assertTrue(cmdWithDefaults.hasOption(option1));
        assertEquals(option1, cmdWithDefaults.getOption(option1.getPreferredName()));
        assertEquals(Arrays.asList("defaultValue1"), cmdWithDefaults.getValues(option1, null));
        assertTrue(cmdWithDefaults.hasOption(option2));
        assertEquals(option2, cmdWithDefaults.getOption(option2.getPreferredName()));
        assertEquals(Boolean.TRUE, cmdWithDefaults.getSwitch(option2, Boolean.FALSE));

        // test toString()
        assertEquals("root", cmdWithDefaults.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_noOption() {
        // given
        Option rootOption = new Option("root", "root", true, "Root");
        List<String> arguments = new ArrayList<>();
        arguments.add("root");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, arguments);

        // when

        // test getters
        assertEquals("root", cmd.getName());
        assertEquals(0, cmd.getOptions().size());
        assertEquals(0, cmd.getOptionTriggers().size());
        assertFalse(cmd.hasOption(null));

        // test toString()
        assertEquals("root", cmd.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_optionWithoutArgument() {
        // given
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option rootOption = new Option("root", "root", true, "Root");
        rootOption.addOption(option1);
        List<String> arguments = new ArrayList<>();
        arguments.add("root");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, arguments);

        // when

        // test getters
        assertEquals("root", cmd.getName());
        assertEquals(1, cmd.getOptions().size());
        assertEquals(1, cmd.getOptionTriggers().size());
        assertTrue(cmd.hasOption(option1));
        assertEquals(option1, cmd.getOption(option1.getPreferredName()));
        assertFalse(cmd.getValues(option1, null).isPresent());

        // test toString()
        assertEquals("root", cmd.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_optionWithArgument() {
        // given
        Option option1 = new Option("o", "option1", true, "Option 1");
        Option rootOption = new Option("root", "root", true, "Root");
        rootOption.addOption(option1);
        List<String> arguments = new ArrayList<>();
        arguments.add("root");
        arguments.add("o");
        arguments.add("value1");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, arguments);

        // when

        // test getters
        assertEquals("root", cmd.getName());
        assertEquals(1, cmd.getOptions().size());
        assertEquals(1, cmd.getOptionTriggers().size());
        assertTrue(cmd.hasOption(option1));
        assertEquals(option1, cmd.getOption(option1.getPreferredName()));
        assertEquals(Arrays.asList("value1"), cmd.getValues(option1, null));

        // test toString()
        assertEquals("root o value1", cmd.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_switch() {
        // given
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option rootOption = new Option("root", "root", true, "Root");
        rootOption.addOption(option1);
        List<String> arguments = new ArrayList<>();
        arguments.add("root");
        arguments.add("o");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, arguments);

        // when setting switch
        cmd.addSwitch(option1, true);

        // then

        // test getters
        assertEquals("root", cmd.getName());
        assertEquals(1, cmd.getOptions().size());
        assertEquals(1, cmd.getOptionTriggers().size());
        assertTrue(cmd.hasOption(option1));
        assertEquals(option1, cmd.getOption(option1.getPreferredName()));
        assertTrue(cmd.getSwitch(option1, Boolean.FALSE));

        // test toString()
        assertEquals("root", cmd.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_property() {
        // given
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option rootOption = new Option("root", "root", true, "Root");
        rootOption.addOption(option1);
        List<String> arguments = new ArrayList<>();
        arguments.add("root");
        arguments.add("o");
        arguments.add("--property");
        arguments.add("property1");
        arguments.add("value1");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, arguments);

        // when setting property
        cmd.addProperty(option1, "property1", "value1");

        // then

        // test getters
        assertEquals("root", cmd.getName());
        assertEquals(1, cmd.getOptions().size());
        assertEquals(1, cmd.getOptionTriggers().size());
        assertTrue(cmd.hasOption(option1));
        assertEquals(option1, cmd.getOption(option1.getPreferredName()));
        assertEquals(Arrays.asList("value1"), cmd.getValues(option1, null));
        assertEquals("value1", cmd.getProperty("property1"));

        // test toString()
        assertEquals("root", cmd.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_switch_alreadySet() {
        // given
        Option option1 = new Option("o", "option1", false, "Option 1");
        Option rootOption = new Option("root", "root", true, "Root");
        rootOption.addOption(option1);
        List<String> arguments = new ArrayList<>();
        arguments.add("root");
        arguments.add("o");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(rootOption, arguments);
        cmd.addSwitch(option1, true);

        // when setting switch again
        cmd.addSwitch(option1, false);

        // then an exception is thrown
    }
}