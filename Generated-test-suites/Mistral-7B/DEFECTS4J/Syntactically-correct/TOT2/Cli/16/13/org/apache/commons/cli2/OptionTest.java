import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class OptionTest {

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        ListIterator args = mock(ListIterator.class);
        Option option = mock(Option.class);

        when(commandLine.getArgs()).thenReturn(args);

        option.process(commandLine, args);

        verify(commandLine, times(1)).processArgument(eq("argument"), anyInt());
    }
}