import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new BasicWriteableCommandLine();
        ListIterator args = new ArrayList<>(Arrays.asList("--option", "value1", "arg1", "arg2")).listIterator();

        Option option = new MockOption(); // replace with actual Option implementation
        option.process(commandLine, args);

        assertEquals("value1", commandLine.getOptionValue("option"));
        assertEquals("arg1", commandLine.getArg(0));
        assertEquals("arg2", commandLine.getArg(1));
        assertEquals(2, commandLine.getArgList().size());
        assertTrue(option.isProcessed());
        assertFalse(args.hasNext());
    }
}