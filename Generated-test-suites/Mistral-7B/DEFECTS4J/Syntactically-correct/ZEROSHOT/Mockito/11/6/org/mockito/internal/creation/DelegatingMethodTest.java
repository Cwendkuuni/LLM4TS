import org.junit.Test;
import org.junit. Assert;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

 private static final String CLASS_NAME = DelegatingMethod.class.getName();

 @Test
 public void testGetExceptionTypes() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(Object.class, "methodWithException", new Class[] { Integer.TYPE, String.class }, new Class[] { Exception.class }));
 Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
 Assert.assertArrayEquals(new Class<?>[] { Exception.class }, exceptionTypes);
 }

 @Test
 public void testGetJavaMethod() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(Object.class, "methodWithoutException", new Class[] {}));
 Method javaMethod = delegatingMethod.getJavaMethod();
 Assert.assertNotNull(javaMethod);
 Assert.assertEquals("methodWithoutException", javaMethod.getName());
 }

 @Test
 public void testGetName() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(Object.class, "methodWithoutException", new Class[] {}));
 String name = delegatingMethod.getName();
 Assert.assertEquals("methodWithoutException", name);
 }

 @Test
 public void testGetParameterTypes() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(Object.class, "methodWithTwoParameters", new Class[] { Integer.TYPE, String.class }));
 Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
 Assert.assertArrayEquals(new Class<?>[] { Integer.TYPE, String.class }, parameterTypes);
 }

 @Test
 public void testGetReturnType() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(Object.class, "methodWithReturnType", new Class[] {}));
 Class<?> returnType = delegatingMethod.getReturnType();
 Assert.assertNotNull(returnType);
 Assert.assertEquals(void.class, returnType);
 }

 @Test
 public void testIsVarArgs() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(Object.class, "methodWithVarArgs", new Class[] { Integer.TYPE, String[].class }));
 boolean isVarArgs = delegatingMethod.isVarArgs();
 Assert.assertTrue(isVarArgs);
 }

 @Test
 public void testIsAbstract() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(Object.class, "methodWithoutException", new Class[] {}));
 boolean isAbstract = delegatingMethod.isAbstract();
 Assert.assertFalse(isAbstract);
 }

 @Test
 public void testEquals() throws Exception {
 DelegatingMethod delegatingMethod1 = new DelegatingMethod(new Method(Object.class, "methodWithoutException", new Class[] {}));
 DelegatingMethod delegatingMethod2 = new DelegatingMethod(new Method(Object.class, "methodWithoutException", new Class[] {}));
 Assert.assertTrue(delegatingMethod1.equals(delegatingMethod2));

 DelegatingMethod delegatingMethod3 = new DelegatingMethod(new Method(Object.class, "methodWithException", new Class[] { Integer.TYPE, String.class }, new Class[] { Exception.class }));
 Assert.assertFalse(delegatingMethod1.equals(delegatingMethod3));
 }

 @Test
 public void testHashCode() throws Exception {
 DelegatingMethod delegatingMethod1 = new DelegatingMethod(new Method(Object.class, "methodWithoutException", new Class[] {}));
 DelegatingMethod delegatingMethod2 = new DelegatingMethod(new Method(Object.class, "methodWithoutException", new Class[] {}));
 Assert.assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
 }
}