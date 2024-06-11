import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.Invocation;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationTest {

 @Test
 public void testInvocationConstruction() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[]{String.class});
 Object[] rawArguments = new Object[]{"test"};
 RealMethod realMethod = Mockito.mock(RealMethod.class);

 // when
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{"test"}, 1, realMethod);

 // then
 assertEquals(mock, invocation.getMock());
 assertEquals(mockitoMethod, invocation.getMethod());
 assertEquals(Arrays.asList(new Object[]{"test"}), Arrays.asList(invocation.getArguments()));
 assertEquals(1, invocation.getSequenceNumber());
 assertNotNull(invocation.getLocation());
 assertEquals(realMethod, invocation.realMethod);
 }

 @Test
 public void testEquals() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[]{String.class});
 Object[] rawArguments1 = new Object[]{"test1"};
 Object[] rawArguments2 = new Object[]{"test2"};
 RealMethod realMethod = Mockito.mock(RealMethod.class);
 Invocation invocation1 = new Invocation(mock, mockitoMethod, rawArguments1, 1, realMethod);
 Invocation invocation2 = new Invocation(mock, mockitoMethod, rawArguments2, 2, realMethod);

 // when
 // then
 assertFalse(invocation1.equals(invocation2));
 }

 @Test
 public void testToString() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[]{String.class});
 Object[] arguments = new Object[]{"test"};
 Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

 // when
 String result = invocation.toString();

 // then
 assertNotNull(result);
 assertEquals("test(String)", result);
 }

 @Test
 public void testToStringWithMatchers() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[]{String.class});
 Object[] arguments = new Object[]{"test"};
 Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);
 PrintSettings printSettings = new PrintSettings("test");

 // when
 String result = invocation.toString(printSettings);

 // then
 assertNotNull(result);
 assertEquals("test(String)", result);
 }

 @Test
 public void testToStringMultilineWithMatchers() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "substring", new Class[]{IntType.class, IntType.class}, new Class[]{String.class});
 Object[] arguments = new Object[]{1, 2};
 Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);
 PrintSettings printSettings = new PrintSettings("test", true);

 // when
 String result = invocation.toString(null, printSettings);

 // then
 assertNotNull(result);
 assertTrue(result.contains("substring(int, int)"));
 assertTrue(result.contains("String mock"));
 }

 @Test
 public void testIsToString() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "toString", new Class[0], new Class[0]);
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

 // when
 boolean result = Invocation.isToString(invocation);

 // then
 assertTrue(result);
 }

 @Test
 public void testIsValidException() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[]{String.class});
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);
 Throwable throwable = new RuntimeException();

 // when
 boolean result = invocation.isValidException(throwable);

 // then
 assertFalse(result);
 }

 @Test
 public void testIsValidReturnType() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[]{String.class});
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

 // when
 boolean result = invocation.isValidReturnType(int.class);

 // then
 assertTrue(result);
 }

 @Test
 public void testIsVoid() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[]{String.class});
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

 // when
 boolean result = invocation.isVoid();

 // then
 assertFalse(result);
 }

 @Test
 public void testPrintMethodReturnType() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[]{String.class});
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

 // when
 String result = invocation.printMethodReturnType();

 // then
 assertEquals("int", result);
 }

 @Test
 public void testGetMethodName() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[]{String.class});
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

 // when
 String result = invocation.getMethodName();

 // then
 assertEquals("length", result);
 }

 @Test
 public void testReturnsPrimitive() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[]{String.class});
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

 // when
 boolean result = invocation.returnsPrimitive();

 // then
 assertFalse(result);
 }

 @Test
 public void testGetLocation() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[]{String.class});
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

 // when
 Location result = invocation.getLocation();

 // then
 assertNotNull(result);
 }

 @Test
 public void testGetArgumentsCount() {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[]{String.class});
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

 // when
 int result = invocation.getArgumentsCount();

 // then
 assertEquals(0, result);
 }

 @Test
 public void testCallRealMethod() throws Throwable {
 // given
 Object mock = Mockito.mock(String.class);
 MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[]{}, new Class[]{String.class});
 String expected = "test";
 Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{expected}, 1, null);

 // when
 int result = (int) invocation.callRealMethod();

 // then
 assertEquals(expected.length(), result);
 }
}