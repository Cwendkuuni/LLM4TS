import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.stub.Answer;
import java.util.*;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

 @Test
 public void testInvocationMatcherWithEmptyMatchers() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher matcher = new InvocationMatcher(invocation);
 Method method = Mockito.mock(Method.class);
 Mockito.when(invocation.getMethod()).thenReturn(method);

 assertThat(matcher.matches(Mockito.mock(Invocation.class)), is(true));
 }

 @Test
 public void testInvocationMatcherWithMatchers() {
 Invocation invocation = Mockito.mock(Invocation.class);
 Mockito.when(invocation.getArguments()).thenReturn(new Object[]{1, "two", new Object[0]});
 InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(
 Mockito.argThat(isA(Integer.class)),
 Mockito.argThat(is(String.class)),
 Mockito.argThat(Mockito.isNull())
 ));
 Method method = Mockito.mock(Method.class);
 Mockito.when(invocation.getMethod()).thenReturn(method);

 Mockito.verify(method).invoke(Mockito.anyObject(), Mockito.eq(1), Mockito.eq("two"), Mockito.isNull());
 }

 @Test(expected = RuntimeException.class)
 public void testInvocationMatcherWithMismatchedTypes() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher matcher = new InvocationMatcher(invocation);
 Method method = Mockito.mock(Method.class);
 Mockito.when(invocation.getMethod()).thenReturn(method);

 Mockito.when(method.getParameterTypes()).thenReturn(new Class[]{Integer.class, String.class, Float.class});

 try {
 matcher.matches(Mockito.mock(Invocation.class));
 } catch (RuntimeException re) {
 //expected
 }
 }

 @Test
 public void testInvocationMatcherWithVarArgs() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher matcher = new InvocationMatcher(invocation);
 Method method = Mockito.mock(Method.class);
 Mockito.when(invocation.getMethod()).thenReturn(method);
 Object[] args = new Object[]{1, "two", new Object[]{3, 4}};
 Mockito.when(invocation.getRawArguments()).thenReturn(args);

 Mockito.verify(method).invoke(Mockito.anyObject(), Mockito.arrayContaining(isA(Integer.class)),
 Mockito.contains(isA(String.class)), isA(Object[].class));
 }

 @Test
 public void testInvocationMatcherWithCapturedArguments() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher matcher = new InvocationMatcher(invocation);
 Method method = Mockito.mock(Method.class);
 Object[] expectedArgs = new Object[]{1, "two", new Object[]{3, 4}};
 Mockito.when(invocation.getMethod()).thenReturn(method);
 Mockito.when(invocation.getRawArguments()).thenReturn(expectedArgs);
 List<Matcher> matchers = Arrays.asList(
 Mockito.argThat(new CaptureArgumentMatcher(0)),
 Mockito.argThat(new CaptureArgumentMatcher(1)),
 Mockito.argThat(new CaptureArgumentMatcher(2))
 );
 matcher.captureArgumentsFrom(invocation);

 Mockito.verify(matchers.get(0)).captureFrom(Mockito.eq(1));
 Mockito.verify(matchers.get(1)).captureFrom(Mockito.eq("two"));
 Mockito.verify(matchers.get(2)).captureFrom(Mockito.eq(new Object[]{3, 4}));
 }

 @Test
 public void testInvocationMatcherWithNoMatch() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher matcher = new InvocationMatcher(invocation);
 Method method1 = Mockito.mock(Method.class);
 Method method2 = Mockito.mock(Method.class);
 Mockito.when(invocation.getMethod()).thenReturn(method1);
 Invocation invocation2 = Mockito.mock(Invocation.class);
 Mockito.when(invocation2.getMethod()).thenReturn(method2);
 Mockito.when(method1.getName()).thenReturn("testMethod");
 Mockito.when(method2.getName()).thenReturn("testOtherMethod");

 MatcherDecorator.verify(matcher).matches(invocation2);
 }
}

class CaptureArgumentMatcher implements Matcher<Object> {
 private int index;

 CaptureArgumentMatcher(int index) {
 this.index = index;
 }

 @Override
 public boolean matches(Object argument) {
 System.out.println("Captured argument " + index + " is " + argument);
 return true;
 }
}