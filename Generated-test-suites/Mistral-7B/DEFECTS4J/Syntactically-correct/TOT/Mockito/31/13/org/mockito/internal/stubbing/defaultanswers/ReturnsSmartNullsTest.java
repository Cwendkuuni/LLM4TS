import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object mock;

 @Before
 public void setup() {
 mock = mock(Object.class, returnsSmartNulls);
 returnsSmartNulls = Mockito.spy(returnsSmartNulls);
 }

 @Test
 public void testSmartNullReturnedByStubbedMethod() throws Exception {
 Answer<Object> answer = Mockito.callRealMethod(returnsSmartNulls, "answer", ArgumentMatchers.capture(InvocationOnMock.class));
 Object smartNull = answer.answer(Mockito.mock(InvocationOnMock.class));

 assertNull(smartNull); // This will not cause a NPE due to ReturnsSmartNulls
 }

 @Test
 public void testSmartNullReturnedByUnstubbedMethod() throws Exception {
 Object unstubbedObject = mock.toString();
 verify(returnsSmartNulls).answer(ArgumentMatchers.any(InvocationOnMock.class));

 String exceptionMessage = "SmartNull returned by unstubbed toString() method on mock";
 String expectedStackTrace = "at org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls.answer(ReturnsSmartNulls.java:63)";

 try {
 mock.toString();
 } catch (Throwable e) {
 assertEquals(exceptionMessage, e.getMessage());
 assertEquals(expectedStackTrace, e.getStackTrace().toString());
 }
}

 @Test(expected = NullPointerException.class)
 public void testNullPointerExceptionThrownByUnstubbedMethod() throws Exception {
 Object unstubbedNull = mock.toString();
 }
}