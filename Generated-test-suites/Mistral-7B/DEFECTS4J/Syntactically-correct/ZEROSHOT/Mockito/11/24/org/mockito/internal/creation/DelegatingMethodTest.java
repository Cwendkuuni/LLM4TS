import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;
import static org.mockito.internal.util.reflection.Reflection.getDeclaredMethod;

public class DelegatingMethodTest {

 @Test
 public void testGetExceptionTypes() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(getMethodWithExceptions());
 Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

 Assert.assertArrayEquals(new Class<?>[]{Exception1.class, Exception2.class}, exceptionTypes);
 }

 @Test
 public void testGetJavaMethod() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(getMethodWithoutException());
 Method javaMethod = delegatingMethod.getJavaMethod();

 Assert.assertNotNull(javaMethod);
 Assert.assertEquals("testMethod", javaMethod.getName());
 }

 @Test
 public void testGetName() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(getMethodWithoutException());
 String name = delegatingMethod.getName();

 Assert.assertEquals("testMethod", name);
 }

 @Test
 public void testGetParameterTypes() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(getMethodWithParameters());
 Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

 Assert.assertArrayEquals(new Class<?>[]{String.class, int.class}, parameterTypes);
 }

 @Test
 public void testGetReturnType() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(getMethodWithReturnTypeVoid());
 Class<?> returnType = delegatingMethod.getReturnType();

 Assert.assertEquals(Void.class, returnType);
 }

 @Test
 public void testIsVarArgs() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(getMethodWithVarArgs());
 boolean varArgs = delegatingMethod.isVarArgs();

 Assert.assertTrue(varArgs);
 }

 @Test
 public void testIsAbstract() {
 DelegatingMethod delegatingMethod = new DelegatingMethod(getAbstractMethod());
 boolean abstractMethod = delegatingMethod.isAbstract();

 Assert.assertTrue(abstractMethod);
 }

 @Test
 public void testEquals() {
 DelegatingMethod delegatingMethod1 = new DelegatingMethod(getMethodWithException1());
 DelegatingMethod delegatingMethod2 = new DelegatingMethod(getMethodWithException1());
 DelegatingMethod delegatingMethod3 = new DelegatingMethod(getMethodWithoutException());

 Assert.assertTrue(delegatingMethod1.equals(delegatingMethod2));
 Assert.assertFalse(delegatingMethod1.equals(delegatingMethod3));
 }

 @Test
 public void testHashCode() {
 DelegatingMethod delegatingMethod1 = new DelegatingMethod(getMethodWithException1());
 DelegatingMethod delegatingMethod2 = new DelegatingMethod(getMethodWithException1());
 DelegatingMethod delegatingMethod3 = new DelegatingMethod(getMethodWithoutException());

 Assert.assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
 Assert.assertNotEquals(delegatingMethod1.hashCode(), delegatingMethod3.hashCode());
 }

 private Method getMethodWithExceptions() {
 return getDeclaredMethod(Object.class, "methodWithExceptions", new Class<?>[]{String.class, int.class}, new Class<?>[]{Exception1.class, Exception2.class});
 }

 private Method getMethodWithoutException() {
 return getDeclaredMethod(Object.class, "testMethod", new Class<?>[]{});
 }

 private Method getMethodWithReturnTypeVoid() {
 return getDeclaredMethod(Object.class, "voidMethod", new Class<?>[]{});
 }

 private Method getMethodWithParameters() {
 return getDeclaredMethod(Object.class, "methodWithParameters", new Class<?>[]{String.class, int.class});
 }

 private Method getMethodWithVarArgs() {
 return getDeclaredMethod(Object.class, "methodWithVarArgs", new Class<?>[]{String[].class});
 }

 private Method getAbstractMethod() {
 return getDeclaredMethod(Object.class, "abstractMethod", new Class<?>[]{});
 }

 private Method getMethodWithException1() {
 return new DelegatingMethod(getMethodWithExceptions()).getJavaMethod();
 }
}