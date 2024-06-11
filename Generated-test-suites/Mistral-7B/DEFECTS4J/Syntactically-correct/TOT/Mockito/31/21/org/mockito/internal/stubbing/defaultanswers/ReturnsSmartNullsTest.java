import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls smartNulls;
 private Object mock;

 @Before
 public void setup() {
 smartNulls = new ReturnsSmartNulls();
 mock = mock(String.class, smartNulls);
 }

 @Test
 public void testReturnsSmartNullForUnstubbedMethod() {
 String expected = "SmartNull returned by unstubbed method on mock";
 String result = (String) smartNulls.answer(new InvocationOnMock(mock, new MethodTestSubject()));
 verify(mock).toString();
 assertThat(result, is(expected));
 }

 @Test
 public void testDelegatesToReturnsMoreEmptyValuesWhenMethodReturnsNotNull() {
 String expected = "Expected String value";
 String result = (String) smartNulls.answer(new InvocationOnMock(mock, new MethodThatReturnsString()));
 verify(mock, never()).toString();
 assertThat(result, is(expected));
 }

 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testThrowsSmartNullPointerExceptionForUnstubbedMethod() {
 smartNulls.answer(new InvocationOnMock(mock, new MethodThatThrowsException()));
 }

 @Test
 public void testCanReturnSmartNullForMethodWithToString() {
 String expected = "SmartNull returned by unstubbed toString method on mock";
 String result = (String) smartNulls.answer(new InvocationOnMock(mock, new MethodWithToString()));
 verify(mock, times(1)).toString();
 assertThat(result, is(expected));
 }

 private static class MethodTestSubject {}

 private static class MethodThatReturnsString {
 Method method;

 MethodThatReturnsString() {
 method = new MethodTestSubject().getClass().getMethod("toString");
 }
 }

 private static class MethodThatThrowsException {
 Method method;

 MethodThatThrowsException() {
 method = new MethodTestSubject().getClass().getMethod("toString", Integer.class);
 }

 @SuppressWarnings("ResultOfMethodCallIgnored")
 private void throwException(InvocationOnMock invocationOnMock) throws Throwable {
 throw new RuntimeException();
 }

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @Mockito.Mock(answer = Answer.RETURNS_DELEGATE.INSTANCE)
 private ThrowingInterceptor throwingInterceptor;

 @Mockito.Inject
 private Reporter reporter;

 @Before
 public void setupMocks() {
 Mockito.when(throwingInterceptor.intercept(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.anyArray(), ArgumentMatchers.any()))
 .thenCallRealMethod();
 }
 }

 private static class MethodWithToString {
 Method method;

 MethodWithToString() {
 method = new MethodTestSubject().getClass().getMethod("toString");
 }
 }
}