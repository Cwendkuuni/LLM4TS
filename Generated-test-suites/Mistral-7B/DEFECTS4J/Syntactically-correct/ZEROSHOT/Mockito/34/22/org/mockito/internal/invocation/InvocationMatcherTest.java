import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.CapturesArguments;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.Mockito;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

 private InvocationMatcher invocationMatcher;
 private Invocation mockInvocation;
 private Method mockMethod;
 private List<Matcher> mockMatchers;
 private ArgumentsComparator argumentsComparator;
 private PrintSettings printSettings;

 @Before
 public void setup() {
 mockInvocation = Mockito.mock(Invocation.class);
 mockMethod = Mockito.mock(Method.class);
 mockMatchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(CapturesArguments.class));
 argumentsComparator = Mockito.spy(new ArgumentsComparator());
 printSettings = new PrintSettings();
 invocationMatcher = new InvocationMatcher(mockInvocation, mockMatchers);
 }

 @Test
 public void testInvocationMatcherConstructorWithInvocationAndMatchers() {
 // given
 // when
 InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, mockMatchers);
 // then
 assertThat(invocationMatcher.getInvocation(), is(mockInvocation));
 assertThat(invocationMatcher.getMatchers(), is(mockMatchers));
 }

 @Test
 public void testInvocationMatcherConstructorWithInvocationOnly() {
 // given
 // when
 InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
 // then
 assertThat(invocationMatcher.getInvocation(), is(mockInvocation));
 assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
 }

 @Test
 public void testGetMethod() {
 // given
 // when
 Method method = invocationMatcher.getMethod();
 // then
 assertThat(method, is(mockMethod));
 }

 @Test
 public void testGetInvocation() {
 // given
 // when
 Invocation invocation = invocationMatcher.getInvocation();
 // then
 assertThat(invocation, is(mockInvocation));
 }

 @Test
 public void testGetMatchers() {
 // given
 // when
 List<Matcher> matchers = invocationMatcher.getMatchers();
 // then
 assertThat(matchers, is(mockMatchers));
 }

 @Test
 public void testToString() {
 // given
 // when
 String toString = invocationMatcher.toString();
 // then
 assertThat(toString, notNullValue());
 }

 @Test
 public void testMatches() throws Exception {
 // given
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation, mockMatchers);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation, Arrays.asList(Mockito.mock(Matcher.class)));
 Invocation invocationToCompare = Mockito.mock(Invocation.class);
 doThrow(new RuntimeException()).when(invocationToCompare).getArguments();
 argumentsComparator.argumentsMatch(invocationMatcher1, invocationMatcher2);
 // when
 boolean matches = invocationMatcher1.matches(invocationToCompare);
 // then
 verifyZeroInteractions(invocationToCompare);
 assertThat(matches, is(false));
 }

 @Test
 public void testHasSimilarMethod() throws Exception {
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation, mockMatchers);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation, Arrays.asList(Mockito.mock(Matcher.class)));
 Invocation invocationToCompare = Mockito.mock(Invocation.class);
 doThrow(new RuntimeException()).when(invocationToCompare).getArguments();
 Invocation invocationWithSameMethod = Mockito.mock(Invocation.class);
 when(invocationWithSameMethod.getMethod()).thenReturn(mockMethod);
 // when
 boolean hasSimilarMethod = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
 boolean hasSimilarMethod2 = invocationMatcher1.hasSimilarMethod(invocationWithSameMethod);
 // then
 verifyZeroInteractions(invocationMatcher2, invocationWithSameMethod);
 assertThat(hasSimilarMethod, is(false));
 assertThat(hasSimilarMethod2, is(true));
 }

 @Test
 public void testHasSameMethod() throws Exception {
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation, mockMatchers);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation, mockMatchers);
 Invocation invocationToCompare = Mockito.mock(Invocation.class);
 doThrow(new RuntimeException()).when(invocationToCompare).getArguments();
 when(invocationToCompare.getMethod()).thenReturn(mockMethod);
 // when
 boolean hasSameMethod = invocationMatcher1.hasSameMethod(invocationMatcher2);
 boolean hasSameMethod2 = invocationMatcher1.hasSameMethod(invocationToCompare);
 // then
 verifyZeroInteractions(invocationMatcher2, invocationToCompare);
 assertThat(hasSameMethod, is(true));
 assertThat(hasSameMethod2, is(false));
 }

 @Test
 public void testToStringWithPrintSettings() throws Exception {
 InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, mockMatchers);
 // when
 String toString = invocationMatcher.toString(printSettings);
 // then
 assertThat(toString, notNullValue());
 }

 @Test
 public void testCaptureArgumentsFrom() throws Exception {
 InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, mockMatchers);
 Invocation invocation = Mockito.mock(Invocation.class);
 Object[] arguments = new Object[]{1, "test"};
 // when
 invocationMatcher.captureArgumentsFrom(invocation);
 // then
 verify(mockMatchers.get(0), org.hamcrest.MatcherAssert.notNullValue());
 verify(mockMatchers.get(0), org.mockito.Mockito.argThat(isA(CapturesArguments.class)));
 verify(mockMatchers.get(0), org.mockito.Mockito.argThat(arg -> arg.captureFrom(arguments[0])));
 verify(mockMatchers.get(1), org.hamcrest.MatcherAssert.notNullValue());
 }
}