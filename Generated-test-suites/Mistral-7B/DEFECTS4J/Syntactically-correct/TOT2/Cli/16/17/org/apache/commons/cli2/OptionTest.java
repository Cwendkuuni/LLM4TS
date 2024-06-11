package org.apache.commons.cli2.test;

import org.apache.commons.cli2.*;
import org.junit.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class OptionTest {

private Option option;
private WriteableCommandLine commandLine;
private ListIterator args;

@Before
public void setUp() {
commandLine = mock(WriteableCommandLine.class);
args = mock(ListIterator.class);
}

@Test
public void testProcessWithEmptyArgument() {
option = mock(Option.class);
when(option.canProcess(commandLine, args)).thenReturn(false);

option.process(commandLine, args);

verifyNoInteractions(commandLine);
verifyNoInteractions(args);
assertTrue(option.isRequired());
assertFalse(option.isProcessed());
}

@Test(expected = OptionException.class)
public void testProcessWithNullArgument() {
option = mock(Option.class);
when(option.canProcess(commandLine, args)).thenThrow(new OptionException("Null argument"));

option.process(commandLine, args);
}

@Test
public void testProcessWithNonProcessableArgument() {
option = mock(Option.class);
when(option.canProcess(commandLine, args)).thenReturn(false);

option.process(commandLine, args);

verifyNoInteractions(commandLine);
verifyNoInteractions(args);
assertFalse(option.isProcessed());
}

@Test
public void testProcessWithProcessableArgument() {
option = mock(Option.class);
when(option.canProcess(commandLine, args)).thenReturn(true);
when(option.process(commandLine, args)).thenReturn(true);

option.process(commandLine, args);

verify(commandLine).processArgument(eq("some-argument"));
verify(commandLine).setValue(eq("some-argument"), any());
verifyNoMoreInteractions(commandLine);
assertTrue(option.isProcessed());
}

@Test
public void testDefaults() {
option = mock(Option.class);
option.defaults(commandLine);

verify(commandLine).setValue(eq("some-default-value"), any());
verifyNoMoreInteractions(commandLine);
}

@Test
public void testGetTriggers() {
option = mock(Option.class);
Set<String> triggers = option.getTriggers();
assertNotNull(triggers);
assertTrue(triggers.contains("trigger1"));
assertTrue(triggers.contains("trigger2"));
}

// Add test cases for other methods: getPrefixes(), validate(), helpLines(), appendUsage(), getPreferredName(), getDescription(), getId(), findOption(), isRequired(), getParent(), setParent()
}