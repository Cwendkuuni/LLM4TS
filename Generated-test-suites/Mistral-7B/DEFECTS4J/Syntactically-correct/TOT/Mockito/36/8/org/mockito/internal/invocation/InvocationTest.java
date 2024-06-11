import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.invocation.Invocation;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationTest {

 @Test
 public void testConstructorWithMockMethodAndArgs() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });

 // Verify the constructor has been called with the correct arguments
 verify(invocation.getMock()).methodName(argThat(isA(Integer.class)), argThat(isA(Integer.class)));
 }

 @Test
 public void testGetMock() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });

 // Test getting the mock object
 Object mockObject = invocation.getMock();
 assertNotNull(mockObject);
 assertEquals(mockObject, invocation.getMock());
 }

 @Test
 public void testGetMethod() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });

 // Test getting the method
 MockitoMethod method = invocation.getMethod();
 assertEquals("methodName", method.getName());
 assertNotNull(method.getReturnType());
 }

 @Test
 public void testGetArguments() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });

 // Test getting the arguments
 Object[] arguments = invocation.getArguments();
 assertArrayEquals(new Object[] { 1, 2 }, arguments);
 }

 @Test
 public void testIsVerified() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });

 // Test isVerified returns false initially
 assertFalse(invocation.isVerified());
 }

 @Test
 public void testMarkVerified() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });

 // Test marking the invocation as verified
 invocation.markVerified();
 // Test isVerified returns true after marking it as verified
 assertTrue(invocation.isVerified());
 }

 @Test(expected = RuntimeException.class)
 public void testHashCode() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 invocation.hashCode();
 }

 @Test(expected = RuntimeException.class)
 public void testToString() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 invocation.toString();
 }

 @Test
 public void testToStringWithArgs() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 String toString = invocation.toString();
 // Test the toString method returns the expected string representation
 String expected = "org.mockito.internal.invocation.Invocation@";
 int index = toString.lastIndexOf("@");
 assertNotNull(index);
 assertEquals(expected.substring(0, index), toString.substring(0, index));
 }

 @Test
 public void testToStringWithPrintSettings() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 PrintSettings printSettings = new PrintSettings();
 String toString = invocation.toString(printSettings);
 // Test the toString method returns the expected string representation with print settings
 String expected = "Invocation:";
 assertEquals(expected, toString.substring(0, expected.length()));
 }

 @Test
 public void testIsVerifiedInOrder() {
 Invocation invocation1 = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName1", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 Invocation invocation2 = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName1", int.class, int.class), new Object[] { 1, 2 }, 2, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 // Test isVerifiedInOrder returns false initially
 assertFalse(invocation1.isVerifiedInOrder());
 // Test marking the first invocation as verified in order
 invocation1.markVerifiedInOrder();
 // Test isVerifiedInOrder returns true for the first invocation after marking it as verified in order
 assertTrue(invocation1.isVerifiedInOrder());
 // Test isVerifiedInOrder returns false for the second invocation
 assertFalse(invocation2.isVerifiedInOrder());
 }

 @Test
 public void testEquals() {
 Invocation invocation1 = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 Invocation invocation2 = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 // Test equals returns true for invocations with the same arguments
 assertTrue(invocation1.equals(invocation2));
 Invocation invocation3 = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 3 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 // Test equals returns false for invocations with different arguments
 assertFalse(invocation1.equals(invocation3));
 }

 @Test(expected = RuntimeException.class)
 public void testIsToString() {
 Invocation invocation = new Invocation(mock(Object.class), new MockitoMethod() {}, new Object[] {}, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 assertFalse(Invocation.isToString(invocation));
 }

 @Test
 public void testIsValidException() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) throws Exception {
 // real method implementation
 throw new IOException("Test exception");
 }
 });
assertTrue(invocation.isValidException(new IOException("Test exception")));
 }

 @Test
 public void testIsValidReturnType() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return new ArrayList<>();
 }
 });
 assertTrue(invocation.isValidReturnType(ArrayList.class));
 }

 @Test
 public void testIsVoid() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", Void.TYPE), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 assertTrue(invocation.isVoid());
 }

 @Test
 public void testPrintMethodReturnType() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 assertEquals("Integer", invocation.printMethodReturnType());
 }

 @Test
 public void testGetMethodName() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 assertEquals("methodName", invocation.getMethodName());
 }

 @Test
 public void testReturnsPrimitive() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", boolean.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return true;
 }
 });
 assertTrue(invocation.returnsPrimitive());
 }

 @Test
 public void testGetLocation() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 assertNotNull(invocation.getLocation());
 }

 @Test
 public void testGetArgumentsCount() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 assertEquals(2, invocation.getArgumentsCount());
 }

 @Test
 public void testCallRealMethod() throws Exception {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) throws Exception {
 // real method implementation
 return 42;
 }
 });
 assertEquals(42, invocation.callRealMethod());
 }

 @Test
 public void testToStringWithPrintSettingsAndMultiline() {
 Invocation invocation = new Invocation(mock(Object.class), mockMethod(Object.class, "methodName", int.class, int.class), new Object[] { 1, 2 }, 1, new RealMethod() {
 @Override
 public Object invoke(Object mock, Object[] args) {
 // real method implementation
 return null;
 }
 });
 PrintSettings printSettings = new PrintSettings();
 printSettings.setMultiline(true);
 String toString = invocation.toString(printSettings);
 String expected = "Invocation:";
 int index = toString.indexOf(expected);
 assertTrue(index >= 0);
 }
}