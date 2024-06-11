import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object mock;

 @Before
 public void setUp() {
 mock = mock(String.class);
 returnsSmartNulls = new ReturnsSmartNulls();
 }

 @Test
 public void testSmartNullForToStringMethod() {
 String expected = "SmartNull returned by unstubbed getClass() method on mock";
 when(mock.getClass()).thenAnswer(returnsSmartNulls);
 assertEquals(expected, mock.getClass());
 }

 @Test
 public void testSmartNullWhenMethodIsNotToString() {
 when(mock.hashCode()).thenAnswer(returnsSmartNulls);
 verify(mock).hashCode();
 }

 @Test
 public void testNullReturnType() {
 Object mockWithNullReturnType = mock(String[].class);
 when(mockWithNullReturnType.getClass()).thenAnswer(returnsSmartNulls);
 assertEquals(null, mockWithNullReturnType.getClass());
 }

 @Test
 public void testDelegatedAnswerIsUsedWhenNotMockable() {
 when(mock.length()).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 5;
 }
 });
 assertEquals(5, mock.length());
 }

 @Test
 public void testThrowsExceptionForToStringMethod() {
 String mockWithException = spy(String.class);
 doAnswer(invocation -> { throw new RuntimeException("Exception in spy"); }).when(mockWithException).toString();
 String smartNull = returnsSmartNulls.answer(new InvocationOnMock(mockWithException, new Method(String.class.getDeclaredMethod("toString")), new Object[0], new Object[0], new ThrowingInterceptor(new InvocationOnMock(mockWithException, new Method(String.class.getDeclaredMethod("toString")), new Object[0], new Object[0], null))));
 assertEquals("Exception in spy", smartNull.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testSmartNullPointerExceptionWhenNotNullValueIsExpected() {
 when(mock.hashCode()).thenAnswer(returnsSmartNulls);
 assertNotNull(mock.hashCode()); // will throw NPE
 }
}