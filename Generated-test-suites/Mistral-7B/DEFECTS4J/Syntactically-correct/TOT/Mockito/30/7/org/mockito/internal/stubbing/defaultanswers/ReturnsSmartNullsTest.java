import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object mockObject;

 @Before
 public void setUp() {
 mockObject = mock(Object.class, returnsSmartNulls);
 returnsSmartNulls = new ReturnsSmartNulls();
 }

 @Test
 public void testReturnsSmartNullForToStringMethod() {
 when(mockObject.toString()).thenAnswer(returnsSmartNulls);
 String result = (String) mockObject.toString();
 assertNotNull(result);
 verify(mockObject).toString();
 }

 @Test
 public void testReturnsSmartNullForNonToStringMethod() {
 when(mockObject.someMethod(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())).thenAnswer(returnsSmartNulls);
 try {
 mockObject.someMethod(1, "arg");
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 String message = e.getMessage();
 assertNotNull(message);
 String methodName = Arrays.asList(message.split(" "))
 .subList(2, message.split(" ").size())
 .get(0);
 assertEquals("someMethod", methodName);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testReturnsNullForNonMockableType() {
 when(mockObject.someFinalMethod()).thenAnswer(returnsSmartNulls);
 mockObject.someFinalMethod();
 }

 @Test
 public void testDelegateAnswerReturnValue() {
 when(mockObject.someMethod(ArgumentMatchers.anyInt())).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) {
 return "expectedValue";
 }
 });
 Object result = mockObject.someMethod(1);
 assertEquals("expectedValue", result);
 }
}