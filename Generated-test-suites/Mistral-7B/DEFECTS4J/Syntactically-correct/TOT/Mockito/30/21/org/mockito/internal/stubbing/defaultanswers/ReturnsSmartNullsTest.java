import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.JUnitRunner;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnitRunner.class)
public class ReturnsSmartNullsTest {

 private static final Class<?> CLASS_UNDER_TEST = ReturnsSmartNulls.class;
 private static final Class<?> MOCKED_CLASS = String.class;

 @Test
 public void testUnstubbedMethodReturnsSmartNull() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     String mockedString = mock(MOCKED_CLASS);

     when(mockedString.toString()).thenReturn("Mockito");

     String result = returnsSmartNulls.answer(new InvocationOnMock(returnsSmartNulls, "toString", new Object[0], MOCKED_CLASS));

     verify(returnsSmartNulls).answer(ArgumentMatchers.any(InvocationOnMock.class));
     verify(mockedString).toString();

     assertNotNull(result);
     assertEquals("SmartNull returned by unstubbed toString method on mock", result.toString());
 }

 @Test
 public void testStubbedMethodReturnsDefaultValue() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     String mockedString = mock(MOCKED_CLASS);
     when(mockedString.toString()).thenReturn("Mockito");

     when(returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn("Test");

     String result = returnsSmartNulls.answer(new InvocationOnMock(returnsSmartNulls, "answer", new Object[0], CLASS_UNDER_TEST));

     verify(returnsSmartNulls).answer(ArgumentMatchers.any(InvocationOnMock.class));
     verifyZeroInteractions(mockedString);

     assertNotNull(result);
     assertEquals("Test", result);
 }

 @Test(expected = NullPointerException.class)
 public void testUnstubbedMethodReturnsNullAndThrowsNPE() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);

     String result = returnsSmartNulls.answer(new InvocationOnMock(returnsSmartNulls, "answer", new Object[0], Object.class));

     fail("Unstubbed method should return SmartNull instead of null");
 }

 @Test
 public void testUnstubbedMethodReturnsNullAndReturnsSmartNull() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     String mockedString = mock(MOCKED_CLASS);

     when(mockedString.toString()).thenReturn(null);

     String result = returnsSmartNulls.answer(new InvocationOnMock(returnsSmartNulls, "toString", new Object[0], MOCKED_CLASS));

     verify(mockedString).toString();

     assertNotNull(result);
     assertEquals("SmartNull returned by unstubbed toString method on mock", result.toString());
 }

 @Test
 public void testDelegateAnswerReturnsDefaultValue() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     String mockedString = mock(MOCKED_CLASS);
     when(mockedString.toString()).thenReturn("Mockito");

     ReturnsMoreEmptyValues returnsMoreEmptyValues = mock(ReturnsMoreEmptyValues.class);
     when(returnsSmartNulls.delegate).thenReturn(returnsMoreEmptyValues);

     String result = returnsSmartNulls.answer(new InvocationOnMock(returnsSmartNulls, "answer", new Object[0], CLASS_UNDER_TEST));

     verify(mockedString).toString();
     verify(returnsMoreEmptyValues).answer(ArgumentMatchers.any(InvocationOnMock.class));

     assertNotNull(result);
     assertEquals("", result);
 }

 @Test
 public void testDelegateAnswerReturnsNull() {
     ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
     String mockedString = mock(MOCKED_CLASS);
     when(mockedString.toString()).thenReturn(null);

     ReturnsMoreEmptyValues returnsMoreEmptyValues = mock(ReturnsMoreEmptyValues.class);
     when(returnsSmartNulls.delegate).thenReturn(returnsMoreEmptyValues);

     String result = returnsSmartNulls.answer(new InvocationOnMock(returnsSmartNulls, "answer", new Object[0], CLASS_UNDER_TEST));

     verify(mockedString).toString();
     verify(returnsMoreEmptyValues).answer(ArgumentMatchers.any(InvocationOnMock.class));

     assertNull(result);
 }
}