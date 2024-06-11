import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OptionTest {

    @Mock
    private WriteableCommandLine commandLine;
    @Mock
    private ListIterator<String> args;

    private Option option;

    @Before
    public void setup() {
        option = new SomeOption(); // replace SomeOption with the actual class name
    }

    @Test
    public void testProcess() throws OptionException {
        // Basic test case
        option.process(commandLine, args);
        // Edge case: empty arguments list
        args = new ArrayList<String>().listIterator();
        option.process(commandLine, args);
        // Error case: invalid argument
        args.next();
        args.set("invalid_argument");
        option.process(commandLine, args);
    }
}