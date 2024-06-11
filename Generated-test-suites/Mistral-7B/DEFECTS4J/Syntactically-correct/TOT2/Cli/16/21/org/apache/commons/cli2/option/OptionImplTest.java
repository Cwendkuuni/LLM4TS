import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.command.Arg;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OptionImplTest {

    @Mock
    private WriteableCommandLine mockCommandLine;

    @Mock
    private ListIterator mockArguments;

    private OptionImpl optionImpl;

    @Before
    public void setUp() {
        optionImpl = new OptionImpl(1, true);
    }

    // test for canProcess with null WriteableCommandLine
    @Test(expected = NullPointerException.class)
    public void testCanProcess_nullWriteableCommandLine() {
        OptionImpl optionImpl = new OptionImpl(1, true);
        optionImpl.canProcess(null, mockArguments);
    }

    // test for canProcess with null ListIterator
    @Test(expected = NullPointerException.class)
    public void testCanProcess_nullListIterator() {
        OptionImpl optionImpl = new OptionImpl(1, true);
        optionImpl.canProcess(mockCommandLine, null);
    }

    // test for canProcess with empty argument list
    @Test
    public void testCanProcess_emptyArgumentList() {
        OptionImpl optionImpl = new OptionImpl(1, true);
        when(mockArguments.hasNext()).thenReturn(false);

        boolean result = optionImpl.canProcess(mockCommandLine, mockArguments);

        verifyNoInteractions(mockCommandLine);
        assertFalse(result);
    }

    // test for canProcess with an empty string argument
    @Test
    public void testCanProcess_emptyStringArgument() {
        OptionImpl optionImpl = new OptionImpl(1, true);
        when(mockArguments.next()).thenReturn("");

        boolean result = optionImpl.canProcess(mockCommandLine, mockArguments);

        verifyNoInteractions(mockCommandLine);
        assertFalse(result);
    }

    // test for canProcess with an argument that matches the trigger but not the preferred name
    @Test
    public void testCanProcess_argumentDoesntMatchPreferredName() {
        OptionImpl optionImpl = new OptionImpl(1, true);
        optionImpl.setPrefixes(Arrays.asList("--prefix"));

        when(mockArguments.next()).thenReturn("--prefix-other");

        boolean result = optionImpl.canProcess(mockCommandLine, mockArguments);

        verifyNoInteractions(mockCommandLine);
        assertFalse(result);
    }

    // test for canProcess when an option with the same trigger already exists in the WriteableCommandLine
    @Test
    public void testCanProcess_optionWithSameTriggerExists() {
        OptionImpl optionImpl = new OptionImpl(1, true);
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        when(commandLine.getOptions()).thenReturn(Arrays.asList(new OptionImpl(1, false)));

        boolean result = optionImpl.canProcess(commandLine, mockArguments);

        verifyNoInteractions(mockCommandLine);
        assertFalse(result);
    }

    // test for isRequired when the option is not required
    @Test
    public void testIsRequired_isNotRequiredOption() {
        OptionImpl optionImpl = new OptionImpl(1, false);

        boolean result = optionImpl.isRequired();

        assertFalse(result);
    }

    // test for toString method when the option has no preferred name, description, prefixes, or triggers
    @Test
    public void testToString_noPreferredNameDescriptionPrefixesOrTriggers() {
        OptionImpl optionImpl = new OptionImpl(1, true);

        String result = optionImpl.toString();

        assertEquals("", result);
    }

    // test for equals method
    @Test
    public void testEquals_equalOptions() {
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(1, true);

        assertTrue(option1.equals(option2));
    }

    // test for equals method
    @Test
    public void testEquals_notEqualOptions() {
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(2, true);

        assertFalse(option1.equals(option2));
    }

    // test for isRequired method
    @Test
    public void testIsRequired_requiredOption() {
        OptionImpl option = new OptionImpl(1, true);

        assertTrue(option.isRequired());
    }

    // test for hashCode method
    @Test
    public void testHashCode_equalOptions() {
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(1, true);

        assertEquals(option1.hashCode(), option2.hashCode());
    }

    // test for hashCode method
    @Test
    public void testHashCode_notEqualOptions() {
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(2, true);

        assertNotEquals(option1.hashCode(), option2.hashCode());
    }
}