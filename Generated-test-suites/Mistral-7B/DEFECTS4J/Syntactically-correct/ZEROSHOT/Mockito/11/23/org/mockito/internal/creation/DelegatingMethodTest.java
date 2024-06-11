import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

 private DelegatingMethod delegatingMethod1;
 private DelegatingMethod delegatingMethod2;
 private DelegatingMethod delegatingMethod3;
 private Method method1;
 private Method method2;
 private Method method3;

 @Test
 public void testGetExceptionTypes() {
 method1 = new Method(TestClass.class, "method1", new Class[]{}, new Class[0]);
 delegatingMethod1 = new DelegatingMethod(method1);
 delegatingMethod2 = new DelegatingMethod(new Method(TestClass.class, "method2", new Class[]{}, new Class[0]));

 Assert.assertArrayEquals(new Class[0], delegatingMethod1.getExceptionTypes());
 Assert.assertArrayEquals(new Class[0], delegatingMethod2.getExceptionTypes());

 method3 = new Method(TestClass.class, "method3", new Class[]{Integer.TYPE}, new Class[0]);
 delegatingMethod3 = new DelegatingMethod(method3);
 Assert.assertEquals(new Class[]{Integer.TYPE.class}, delegatingMethod3.getExceptionTypes());
 }

 @Test
 public void testGetJavaMethod() {
 method1 = new Method(TestClass.class, "method1", new Class[]{}, new Class[0]);
 delegatingMethod1 = new DelegatingMethod(method1);

 Assert.assertEquals(method1, delegatingMethod1.getJavaMethod());
 }

 @Test
 public void testGetName() {
 method1 = new Method(TestClass.class, "method1", new Class[]{}, new Class[0]);
 delegatingMethod1 = new DelegatingMethod(method1);

 Assert.assertEquals("method1", delegatingMethod1.getName());
 }

 @Test
 public void testGetParameterTypes() {
 method1 = new Method(TestClass.class, "method1", new Class[]{}, new Class[0]);
 delegatingMethod1 = new DelegatingMethod(method1);

 Assert.assertArrayEquals(new Class[0], delegatingMethod1.getParameterTypes());

 method2 = new Method(TestClass.class, "method2", new Class[]{String.class, Integer.TYPE}, new Class[0]);
 delegatingMethod2 = new DelegatingMethod(method2);

 Assert.assertArrayEquals(new Class[]{String.class, Integer.TYPE.class}, delegatingMethod2.getParameterTypes());
 }

 @Test
 public void testGetReturnType() {
 method1 = new Method(TestClass.class, "method1", new Class[]{}, new Class[0]);
 delegatingMethod1 = new DelegatingMethod(method1);

 Assert.assertEquals(void.class, delegatingMethod1.getReturnType());

 method2 = new Method(TestClass.class, "method2", new Class[]{String.class, Integer.TYPE}, new Class[0]);
 delegatingMethod2 = new DelegatingMethod(method2);

 Assert.assertEquals(void.class, delegatingMethod2.getReturnType());

 method3 = new Method(TestClass.class, "method3", new Class[]{Integer.TYPE}, new Class[0]);
 delegatingMethod3 = new DelegatingMethod(method3);

 Assert.assertEquals(int.class, delegatingMethod3.getReturnType());
 }

 @Test
 public void testIsVarArgs() {
 method1 = new Method(TestClass.class, "method1", new Class[]{}, new Class[0]);
 delegatingMethod1 = new DelegatingMethod(method1);

 Assert.assertFalse(delegatingMethod1.isVarArgs());

 method2 = new Method(TestClass.class, "method2", new Class[]{String[].class}, new Class[0]);
 delegatingMethod2 = new DelegatingMethod(method2);

 Assert.assertTrue(delegatingMethod2.isVarArgs());
 }

 @Test
 public void testIsAbstract() {
 method1 = new Method(TestClass.class, "method1", new Class[]{}, new Class[0]);
 delegatingMethod1 = new DelegatingMethod(method1);

 Assert.assertFalse(delegatingMethod1.isAbstract());

 method3 = new Method(TestClass.class, "method3", new Class[]{Integer.TYPE}, Modifier.ABSTRACT);
 delegatingMethod3 = new DelegatingMethod(method3);

 Assert.assertTrue(delegatingMethod3.isAbstract());
 }

 @Test
 public void testEquals() {
 method1 = new Method(TestClass.class, "method1", new Class[]{}, new Class[0]);
 delegatingMethod1 = new DelegatingMethod(method1);
 method2 = new Method(TestClass.class, "method1", new Class[]{}, new Class[0]);
 delegatingMethod2 = new DelegatingMethod(method2);
 method3 = new Method(TestClass.class, "method2", new Class[]{}, new Class[0]);
 delegatingMethod3 = new DelegatingMethod(method3);

 Assert.assertTrue(delegatingMethod1.equals(delegatingMethod1));
 Assert.assertTrue(delegatingMethod1.equals(delegatingMethod2));
 Assert.assertFalse(delegatingMethod1.equals(delegatingMethod3));
 Assert.assertFalse(delegatingMethod1.equals(new Object()));

 Assert.assertTrue(delegatingMethod2.equals(delegatingMethod1));
 Assert.assertTrue(delegatingMethod2.equals(delegatingMethod2));
 Assert.assertFalse(delegatingMethod2.equals(delegatingMethod3));
 Assert.assertFalse(delegatingMethod2.equals(new Object()));

 Assert.assertFalse(delegatingMethod1.equals(method1));
 Assert.assertFalse(delegatingMethod1.equals(new Method(TestClass.class, "method1", new Class[]{}, new Class[0])));
 Assert.assertFalse(delegatingMethod1.equals(new Object()));
 }

 @Test
 public void testHashCode() {
 method1 = new Method(TestClass.class, "method1", new Class[]{}, new Class[0]);
 delegatingMethod1 = new DelegatingMethod(method1);
 method2 = new Method(TestClass.class, "method1", new Class[]{}, new Class[0]);
 delegatingMethod2 = new DelegatingMethod(method2);

 Assert.assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
 Assert.assertEquals(delegatingMethod1.hashCode(), delegatingMethod1.hashCode());
 Assert.assertNotEquals(delegatingMethod1.hashCode(), delegatingMethod3.hashCode());
 }
}

class TestClass {
 // methods to be tested
}