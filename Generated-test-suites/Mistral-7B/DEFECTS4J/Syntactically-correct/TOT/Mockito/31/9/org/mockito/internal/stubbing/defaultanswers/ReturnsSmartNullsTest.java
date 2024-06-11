import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object mockObject;

 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 mockObject = mock(Object.class, returnsSmartNulls);
 }

 @Test
 public void testReturnsSmartNullForUnstubbedMethod() {
 ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);

 assertNull(returnsSmartNulls.answer(Mockito.argThat(i -> i.getMethod().getName().equals("toString"))));

 verify(System.err).println(errorMessageCaptor.capture());
 String errorMessage = errorMessageCaptor.getValue();

 assertNotNull(errorMessage);
 String expectedErrorMessage = "SmartNull returned by unstubbed toString() method on mock";
 assertNotNull(errorMessage.contains(expectedErrorMessage));
 }

 @Test
 public void testNullReturnedForNonMockableType() {
 assertNull(returnsSmartNulls.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(byte[].class))));
 }

 @Test
 public void testDefaultReturnValueIsReturnedForStubbedMethod() {
 ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
 returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
 Mockito.doReturn(new Integer(42)).when(returnsSmartNulls).answer(Mockito.any(InvocationOnMock.class));

 Integer result = (Integer) returnsSmartNulls.answer(Mockito.argThat(i -> i.getMethod().getName().equals("answer"))).callRealMethod();
 assertNotNull(result);
 assertEquals(42, result.intValue());
 }
}