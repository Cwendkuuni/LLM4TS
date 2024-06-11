import org.apache.commons.cli2.*;
import org.apache.commons.cli2.commandline.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_BasicUsage() {
        Option optionA = new Option("a", true, "Optional long option A")
                .setDefaultValue(1)
                .setValueSeparator(':');

        Option optionB = new Option("b", 2, "Required long option B with an argument");

        Option optionC = new Option("c", null, "Optional boolean switch C");

        Option rootOption = new Option("root", 0, null, "Root option")
                .addOption(optionA)
                .addOption(optionB)
                .addOption(optionC);

        List<String> args = new ArrayList<>();
        args.add("root");
        args.add("--a:1");
        args.add("--b:test");
        args.add("--c");

        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(rootOption, args);

        // Basic checks
        assertEquals(rootOption, cmdLine.getRootOption());
        assertEquals(args.size(), cmdLine.size());
        assertEquals("root", cmdLine.getName());
        assertEquals(3, cmdLine.getNumberOfOptions());
        assertEquals(3, cmdLine.getNumberOfValues());

        // Check option A
        Option optionAInCmdLine = cmdLine.getOption("a");
        assertEquals(Option.VALUE_PRESENT, optionAInCmdLine.getState());
        assertEquals("1", optionAInCmdLine.getValue());

        // Check option B
        Option optionBInCmdLine = cmdLine.getOption("b");
        assertEquals(Option.VALUE_PRESENT, optionBInCmdLine.getState());
        assertEquals("test", optionBInCmdLine.getValue());

        // Check option C
        Option optionCInCmdLine = cmdLine.getOption("c");
        assertEquals(Option.VALUE_ABSENT, optionCInCmdLine.getState());
        assertTrue(optionCInCmdLine.isSet());

        // Check values
        List values = cmdLine.getValues(optionA);
        assertEquals(1, values.size());
        assertEquals("1", values.get(0));

        values = cmdLine.getValues(optionB);
        assertEquals(1, values.size());
        assertEquals("test", values.get(0));

        // Check properties
        Properties properties = cmdLine.getProperties(optionA);
        assertEquals(1, properties.size());
        assertEquals("Optional long option A", properties.getProperty("description"));

        // Negative checks
        Option nonExistentOption = cmdLine.getOption("nonexistent");
        assertNull(nonExistentOption);
    }

    @Test
    public void testWriteableCommandLineImpl_MissingRequiredOption() {
        Option optionA = new Option("a", 1, "Required long option A")
                .setRequired(true);

        Option rootOption = new Option("root", 0, null, "Root option")
                .addOption(optionA);

        List<String> args = new ArrayList<>();

        try {
            new WriteableCommandLineImpl(rootOption, args);
            fail();
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    @Test
    public void testWriteableCommandLineImpl_MissingRequiredOptionValue() {
        Option optionA = new Option("a", 1, "Required long option A with an argument")
                .setRequired(true);

        Option rootOption = new Option("root", 0, null, "Root option")
                .addOption(optionA);

        List<String> args = new ArrayList<>();
        args.add("root");

        try {
            new WriteableCommandLineImpl(rootOption, args);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Missing required argument 'a'"));
        }
    }

    @Test
    public void testWriteableCommandLineImpl_DuplicateOption() {
        Option optionA = new Option("a", 1, "Duplicate long option A")
                .setDuplicatesAllowed(true);

        Option rootOption = new Option("root", 0, null, "Root option")
                .addOption(optionA)
                .addOption(optionA);

        List<String> args = new ArrayList<>();
        args.add("root");
        args.add("--a");

        try {
            new WriteableCommandLineImpl(rootOption, args);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Option --a can only appear once"));
        }
    }

    @Test
    public void testWriteableCommandLineImpl_DuplicateSwitch() {
        Option optionA = new Option("a", false, "Duplicate boolean switch A");

        Option rootOption = new Option("root", 0, null, "Root option")
                .addOption(optionA)
                .addOption(optionA);

        List<String> args = new ArrayList<>();
        args.add("root");

        try {
            new WriteableCommandLineImpl(rootOption, args);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Switch --a can only appear once"));
        }
    }
}