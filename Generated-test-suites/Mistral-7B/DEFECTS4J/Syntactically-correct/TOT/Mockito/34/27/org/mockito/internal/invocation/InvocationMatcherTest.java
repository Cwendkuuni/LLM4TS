import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.matchers.Arguments;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

 testMethodMatches() {
 Invocation invocation = mock(Invocation.class);
 List<Matcher> matchers = Collections.singletonList(any());
 InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

 Invocation actual = mock(Invocation.class);
 Invocation expected = mock(Invocation.class);
 when(expected.getMethod()).thenReturn(new Method("testMethod", String.class, int.class));
 when(expected.getArguments()).thenReturn(new Object[]{ "test", 1 });

 assertThat(matcher.matches(actual), is(true));
 }

 testMethodDoesNotMatchDifferentClass() {
 Invocation invocation = mock(Invocation.class);
 InvocationMatcher matcher = new InvocationMatcher(invocation);

 Invocation actual = mock(Invocation.class);
 when(actual.getMethod()).thenReturn(new Method("differentMethod", String.class));

 assertFalse(matcher.matches(actual));
 }

 testMethodDoesNotMatchDifferentMock() {
 Invocation invocation = mock(Invocation.class);
 InvocationMatcher matcher = new InvocationMatcher(invocation);
 Invocation actual = mock(Invocation.class);
 Invocation expected = mock(Invocation.class);
 when(expected.getMock()).thenReturn(new Object());

 assertFalse(matcher.matches(actual));
 }

 testMethodDoesNotMatchOverloaded() {
 Invocation invocation = mock(Invocation.class);
 InvocationMatcher matcher = new InvocationMatcher(invocation);
 Invocation actual = mock(Invocation.class);
 Invocation expected = mock(Invocation.class);
 when(expected.getMethod()).thenReturn(new Method("testMethod", int.class));
 when(expected.getArguments()).thenReturn(new Object[]{ 1 });

 Invocation actual2 = mock(Invocation.class);
 when(actual2.getMethod()).thenReturn(new Method("testMethod", String.class));
 when(actual2.getArguments()).thenReturn(new Object[]{ "test" });

 assertFalse(matcher.matches(actual2));
 }

 testMethodMatchesArguments() {
 Invocation invocation = mock(Invocation.class);
 List<Matcher> matchers = Arrays.asList(eq("test"), eq(1));
 InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

 Invocation actual = mock(Invocation.class);
 Object[] expectedArgs = new Object[]{ "test", 1 };
 when(actual.getArguments()).thenReturn(expectedArgs);
 when(actual.getMethod()).thenReturn(new Method("testMethod", String.class, int.class));

 matcher.captureArgumentsFrom(actual);
 ArgumentCaptor<String> stringArgument = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> integerArgument = ArgumentCaptor.forClass(Integer.class);

 verify(matcher).captureArgumentsFrom(actual);
 verify(matcher).getMatchers().get(0).matches(stringArgument.capture());
 verify(matcher).getMatchers().get(1).matches(integerArgument.capture());

 assertEquals("test", stringArgument.getValue());
 assertEquals(1, integerArgument.getValue().intValue());
 }

 testMethodMatchesArgumentsDifferentOrder() {
 Invocation invocation = mock(Invocation.class);
 List<Matcher> matchers = Arrays.asList(eq(1), eq("test"));
 InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

 Invocation actual = mock(Invocation.class);
 Object[] expectedArgs = new Object[]{ "test", 1 };
 when(actual.getArguments()).thenReturn(expectedArgs);
 when(actual.getMethod()).thenReturn(new Method("testMethod", String.class, int.class));

 matcher.captureArgumentsFrom(actual);
 ArgumentCaptor<String> stringArgument = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> integerArgument = ArgumentCaptor.forClass(Integer.class);

 verify(matcher).captureArgumentsFrom(actual);
 verify(matcher).getMatchers().get(1).matches(stringArgument.capture());
 verify(matcher).getMatchers().get(0).matches(integerArgument.capture());

 assertEquals("test", stringArgument.getValue());
 assertEquals(1, integerArgument.getValue().intValue());
 }
}