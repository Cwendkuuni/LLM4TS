import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object mock;

 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 mock = mock(String.class, returnsSmartNulls);
 }

 @Test
 public void testReturnsSmartNullForToStringMethod() {
 String result = returnsSmartNulls.answer((InvocationOnMock) Mockito.method(String.class, "toString"));
 assertNull(result);

 String expectedMessage = "SmartNull returned by unstubbed toString method on mock";
 String actualMessage = (String) Mockito.getMessages().get(0);
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testReturnsNullForNonMockableType() {
 ArgumentCaptor<Answer<Integer>> answerCaptor = ArgumentCaptor.forClass(Answer.class);
 Mockito.doAnswer(answerCaptor.capture(answer)).when(mock).someMethodReturningInt();
 answer.answer(Mockito.mock(InvocationOnMock.class));

 int result = mock.someMethodReturningInt();
 assertNull(result);
 }

 @Test(expected = org.mockito.exceptions.base.NullPointerException.class)
 public void testThrowsNullPointerExceptionForUnstubbedMethod() {
 mock.someMethodReturningObject();
 }

 @Test
 public void testDelegatesToReturnsMoreEmptyValues() {
 String result = (String) returnsSmartNulls.answer((InvocationOnMock) Mockito.method(String.class, "value").invokeMock(mock));
 assertNull(result);
 }
}