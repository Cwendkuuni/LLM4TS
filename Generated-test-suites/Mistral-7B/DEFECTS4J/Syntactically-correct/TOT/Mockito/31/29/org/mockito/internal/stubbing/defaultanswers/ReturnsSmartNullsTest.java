import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsSmartNullsTest {

 private static class TestClass {

 public Object unstubbedMethod(ReturnsSmartNulls mock) {
 return mock.answer(new InvocationOnMock(TestClass.class, "unstubbedMethod", new Object[0]));
 }
 }

 @Test
 public void testAnswerWithDefaultValue() {
 ReturnsSmartNulls mock = mock(ReturnsSmartNulls.class, Answers.RETURNS_DEEP_STUBS);
 TestClass testClass = mock(TestClass.class, Answers.RETURNS_DEEP_STUBS);

 ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

 verify(mock).answer(Mockito.eq(new InvocationOnMock(TestClass.class, "unstubbedMethod", new Object[0]))).returns(stringCaptor.capture());

 String smartNull = stringCaptor.getValue();
 System.out.println(smartNull); // prints "SmartNull returned by unstubbed unstubbedMethod() method on mock"

 assertNotNull(testClass.unstubbedMethod(mock)); // this call should not throw NPE
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerWithNull() {
 ReturnsSmartNulls mock = mock(ReturnsSmartNulls.class, Answers.RETURNS_DEEP_STUBS);
 TestClass testClass = mock(TestClass.class, Answers.RETURNS_DEEP_STUBS);

 testClass.unstubbedMethod(mock);
 }

 @Test
 public void testAnswerWithNonMockableType() {
 ReturnsSmartNulls mock = mock(ReturnsSmartNulls.class, Answers.RETURNS_DEEP_STUBS);
 TestClass testClass = mock(TestClass.class, Answers.RETURNS_DEEP_STUBS);

 assertNull(testClass.unstubbedMethod(mock));
 }
}