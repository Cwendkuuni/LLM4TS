import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {
    //...

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator arguments = commandLine.getArguments().listIterator();

        OptionImpl option = new OptionImpl(1, false);

        assertTrue(option.canProcess(commandLine, arguments));
    }
}