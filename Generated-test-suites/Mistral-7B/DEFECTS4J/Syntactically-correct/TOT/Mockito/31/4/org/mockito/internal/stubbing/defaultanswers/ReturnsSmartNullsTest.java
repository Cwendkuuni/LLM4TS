import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object mockObject;

 @Before
 public void setup() {
 mockObject = mock(Object.class, returnsSmartNulls);
 returnsSmartNulls = new ReturnsSmartNulls();
 }

 @Test
 public void testReturnsSmartNullForToStringMethod() {
 when(mockObject.toString()).thenAnswer(returnsSmartNulls);
 String result = (String) mockObject.toString();
 verify(mockObject).toString();
 assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
 }

 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testThrowsSmartNullPointerExceptionForUnstubbedMethods() {
 when(mockObject.unsupportedMethod()).thenAnswer(returnsSmartNulls);
 mockObject.unsupportedMethod();
 }

 @Test(expected = NullPointerException.class)
 public void testNullReturnValueFromDelegateIsPassedThrough() {
 when(mockObject.getNullReturnValueFromDelegate()).thenAnswer(Answer.returning(null));
 assertNull(mockObject.getNullReturnValueFromDelegate());
 }

 @Test
 public void testStubbedMethodsReturnNormalValues() {
 when(mockObject.stubbedMethod()).thenReturn("Test Value");
 assertEquals("Test Value", mockObject.stubbedMethod());
 }

 @Test
 public void testCanCreateMockOfFinalClasses() {
 final String FINAL_CLASS = "FinalClass";
 when(mockObject.finalMethodInFinalClass()).thenReturn(FINAL_CLASS);
 assertEquals(FINAL_CLASS, mockObject.finalMethodInFinalClass());
 }
}