import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class OptionImplTest {

    @Test
    public void testOptionById() {
        OptionImpl option = new MyOptionImpl(1, true);
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Iterator<String> arguments = Mockito.mock(Iterator.class);

        PowerMockito.when(arguments.hasNext()).thenReturn(true);
        PowerMockito.when(arguments.next()).thenReturn("arg");

        Assert.assertTrue(option.canProcess(commandLine, arguments));
        PowerMockito.verify(commandLine).addOption(option);

        PowerMockito.when(arguments.hasNext()).thenReturn(false);

        PowerMockito.verify(commandLine, Mockito.never()).addOption(Mockito.any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionByIdWithInvalidTrigger() {
        OptionImpl option = new MyOptionImpl(1, true);
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Iterator<String> arguments = Mockito.mock(Iterator.class);

        PowerMockito.when(arguments.hasNext()).thenReturn(false);

        option.findOption("invalidTrigger");
    }

    @Test
    public void testOptionEquals() {
        OptionImpl option1 = new MyOptionImpl(1, true);
        OptionImpl option2 = new MyOptionImpl(1, true);

        Assert.assertEquals(option1, option2);
    }

    @Test
    public void testOptionHashCode() {
        OptionImpl option1 = new MyOptionImpl(1, true);
        OptionImpl option2 = new MyOptionImpl(1, true);

        Assert.assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testOptionToString() {
        OptionImpl option = new MyOptionImpl(1, true);

        Assert.assertNotNull(option.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithEmptyPrefixes() {
        OptionImpl option = new MyOptionImpl(1, true);

        Set<String> prefixes = Mockito.mock(Set.class);
        option.checkPrefixes(prefixes);
    }
}

class MyOptionImpl extends OptionImpl {
    public MyOptionImpl(int id, boolean required) {
        super(id, required);
    }
}