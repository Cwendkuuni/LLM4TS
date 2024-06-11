package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class OptionImplTest {

    private OptionImpl option;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        option = new OptionImpl(1, true);
    }

    @Test
    public void testCanProcess_whenArgumentIsPresent_shouldReturnTrue() {
        Mockito.when(commandLine.getArguments()).thenReturn(Arrays.asList("arg1", "arg2"));

        assertTrue(option.canProcess(commandLine, Arrays.asList("arg1", "arg2").listIterator()));
    }

    @Test
    public void testCanProcess_whenArgumentIsNotPresent_shouldReturnFalse() {
        Mockito.when(commandLine.getArguments()).thenReturn(Arrays.asList());

        assertFalse(option.canProcess(commandLine, Arrays.asList().listIterator()));
    }

    @Test
    public void testToString() {
        final String expected = "OptionImpl[id=1, required=true]";

        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals_whenOtherIsSameInstance_shouldReturnTrue() {
        final OptionImpl otherOption = new OptionImpl(1, true);

        assertTrue(option.equals(otherOption));
    }

    @Test
    public void testEquals_whenOtherIsDifferentClass_shouldReturnFalse() {
        final String otherObject = "otherObject";

        assertFalse(option.equals(otherObject));
    }

    @Test
    public void testEquals_whenOtherHasSameProperties_shouldReturnTrue() {
        final OptionImpl otherOption = new OptionImpl(1, true);

        assertTrue(option.equals(otherOption));
    }

    @Test
    public void testHashCode_shouldReturnUniqueCode() {
        final OptionImpl otherOption = new OptionImpl(1, true);

        assertEquals(option.hashCode(), otherOption.hashCode());

        final OptionImpl otherOption2 = new OptionImpl(2, false);

        assertNotEquals(option.hashCode(), otherOption2.hashCode());
        assertNotEquals(otherOption.hashCode(), otherOption2.hashCode());
        assertNotEquals(option.hashCode(), otherOption2.hashCode());
    }

    @Test
    public void testFindOption_whenTriggerIsMatched_shouldReturnSelf() {
        final OptionImpl otherOption = new OptionImpl(2, true);

        assertTrue(option.findOption("trigger1") == option);
        assertTrue(otherOption.findOption("trigger2") == otherOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindOption_whenTriggerIsNotMatched_shouldThrowIllegalArgumentException() {
        final OptionImpl otherOption = new OptionImpl(1, true);

        otherOption.findOption("trigger2");
    }

    @Test
    public void testIsRequired_shouldReturnCorrectValue() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults_shouldDoNothing() {
        option.defaults(commandLine);

        // no assertions needed, as this method doesn't throw exceptions
    }

    @Test
    public void testCheckPrefixes_whenPrefixesContainsPreferredName_shouldDoNothing() {
        final Set prefixes = new HashSet(Arrays.asList("--prefix1", "--prefix2"));

        option.checkPrefixes(prefixes);

        // no assertions needed, as this method doesn't throw exceptions
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_whenPrefixesDoesNotContainTrigger_shouldThrowIllegalArgumentException() {
        final Set prefixes = new HashSet(Arrays.asList("--prefix1", "--prefix2"));

        option.checkPrefixes(prefixes);

        // no assertions needed, as this method throws an exception
    }

    private void assertEquals(final Object expected,
                              final Object actual) {
        if (expected != null && actual != null) {
            assertEquals(expected.getClass(), actual.getClass());
            assertEquals(expected.toString(), actual.toString());
        } else {
            assertEquals(expected, actual);
        }
    }

    private void assertNotEquals(final Object expected,
                                final Object actual) {
        assertFalse("Expected " + expected + " to not equal " + actual,
                   expected.equals(actual));
    }
}