import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.ListIterator;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OptionImplTest {
    OptionImpl option;
    WriteableCommandLine commandLine;
    ListIterator arguments;

    @Before
    public void setUp() {
        option = new OptionImpl(1, true);
        commandLine = mock(WriteableCommandLine.class);
        arguments = mock(ListIterator.class);
    }

    @Test
    public void testCanProcess() {
        when(arguments.hasNext()).thenReturn(true);
        when(arguments.next()).thenReturn("arg1");
        when(arguments.previous()).thenReturn(null);

        Assert.assertTrue(option.canProcess(commandLine, arguments));
    }
}