// Combined test cases
package org.mockito.internal.test;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithMockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private Object mock;
 private Invocation invocation;
 private Answer answer;

 public MockHandlerTest(Answer answer) {
 this.answer = answer;
 }

 @Parameterized.Parameters
 public static List<Answer> data() {
 return Arrays.asList(new AnswerWithMockito.Returns("test"), new AnswerWithMockito.Returns(42), new AnswerWithMockito.DoNothing(), new AnswerWithMockito.Throws(RuntimeException.class));
 }

 @Before
 public void setup() {
 mock = mock(Object.class);
 mockHandler = new MockHandler<Object>(new MockSettingsImpl());
 }

 @Test
 public void testHandleMethod() throws Throwable {
 // Test case: Basic functionality of handle method
 when(mock.methodName()).thenReturn("testReturnValue");
 invocation = new Invocation(Object.class, "methodName", new Object[]{1, 2, 3}, new Class[]{int[].class, int.class, String.class});
 Object result = mockHandler.handle(invocation);
 verify(mock).methodName();
 assertEquals("testReturnValue", result);

 // Test case: Stubbing methods with return types other than void
 doAnswer(answer).when(mock).methodWithReturnType(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyString());
 invocation = new Invocation(Object.class, "methodWithReturnType", new Object[]{1, 2, 3}, new Class[]{int[].class, int.class, String.class});
 Object result2 = mockHandler.handle(invocation);
 if (answer instanceof AnswerWithMockito.Returns) {
 assertEquals(((AnswerWithMockito.Returns) answer).getReturnValue(), result2);
 } else if (answer instanceof AnswerWithMockito.DoNothing) {
 verifyZeroInteractions(mock);
 } else if (answer instanceof AnswerWithMockito.Throws) {
 try {
 mockHandler.handle(invocation);
 fail();
 } catch (RuntimeException e) {
 // expected
 }
 }

 // Test case: Stubbing void methods
 doAnswer(answer).when(mock).voidMethod();
 mockHandler.handle(new Invocation(Object.class, "voidMethod", new Object[0], new Class[0]));
 verifyZeroInteractions(mock);

 // Test case: Verification of method calls
 verify(mock).methodName();
 verify(mock).methodWithReturnType(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyString());
 verify(mock).voidMethod();
 }

 // Test case: Handling exceptions
 @Test(expected = RuntimeException.class)
 public void testHandleMethod_Exception() throws Throwable {
 doThrow(new RuntimeException()).when(mock).methodName();
 mockHandler.handle(new Invocation(Object.class, "methodName", new Object[]{1}, new Class[]{int.class}));
 }

 // Test case: Testing edge cases with null arguments
 @Test
 public void testHandleMethod_NullArgumentsEdgeCases() {
 // Testing null method name
 Invocation invocation = new Invocation(Object.class, null, new Object[]{1}, new Class[]{int.class});
 mockHandler.handle(invocation);
 verifyZeroInteractions(mock);

 // Testing null method signature
 invocation = new Invocation(Object.class, "testMethod", null, new Class[0]);
 mockHandler.handle(invocation);
 verifyZeroInteractions(mock);

 // Testing null arguments
 invocation = new Invocation(Object.class, "testMethod", new Object[]{null}, new Class[]{int[].class});
 mockHandler.handle(invocation);
 verifyZeroInteractions(mock);
 }
}