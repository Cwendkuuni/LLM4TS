import org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

   @Test
   public void testGetName() {
      DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod", new Class[]{}, int.class));
      String name = delegatingMethod.getName();
      assertEquals("testMethod", name);
   }

   @Test(expected = AssertionError.class)
   public void testGetNameWithNullMethod() {
      DelegatingMethod delegatingMethod = new DelegatingMethod(null);
      // This test will fail with an AssertionError as the method cannot be null
   }

   @Test
   public void testGetExceptionTypes() {
      DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod", new Class[]{}, int.class, new Class[]{}));
      Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
      assertArrayEquals(new Class[]{}, exceptionTypes);

      DelegatingMethod delegatingMethodWithExceptions = new DelegatingMethod(new Method("testMethod", new Class[]{}, int.class, new Class[]{IOException.class, SQLException.class}));
      Class<?>[] exceptionTypesWithExceptions = delegatingMethod.getExceptionTypes();
      assertArrayEquals(new Class[]{IOException.class, SQLException.class}, exceptionTypesWithExceptions);
   }

   @Test
   public void testGetJavaMethod() {
      DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod", new Class[]{}, int.class));
      Method javaMethod = delegatingMethod.getJavaMethod();
      assertEquals("testMethod", javaMethod.getName());
      assertEquals(int.class, javaMethod.getReturnType());
      assertArrayEquals(new Class[]{}, javaMethod.getParameterTypes());
   }

   @Test(expected = AssertionError.class)
   public void testGetJavaMethodWithNullMethod() {
      DelegatingMethod delegatingMethod = new DelegatingMethod(null);
      // This test will fail with an AssertionError as the method cannot be null
   }

   @Test
   public void testIsVarArgs() {
      DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod", new Class[]{}, int.class));
      assertFalse(delegatingMethod.isVarArgs());

      DelegatingMethod delegatingMethodWithVarArgs = new DelegatingMethod(new Method("testMethod", new Class[]{}, int.class, new Class[]{}));
      assertFalse(delegatingMethodWithVarArgs.isVarArgs());

      DelegatingMethod delegatingMethodWithVarArgs2 = new DelegatingMethod(new Method("testMethod", new Class[]{String.class}, int.class));
      assertTrue(delegatingMethodWithVarArgs2.isVarArgs());
   }

   @Test
   public void testIsAbstract() {
      DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod", new Class[]{}, int.class));
      assertFalse(delegatingMethod.isAbstract());

      DelegatingMethod delegatingMethodAbstract = new DelegatingMethod(new Method("testMethod", new Class[]{}, void.class));
      assertTrue(delegatingMethodAbstract.isAbstract());
   }

   @Test
   public void testEquals() {
      Method method1 = new Method("testMethod1", new Class[]{}, int.class);
      Method method2 = new Method("testMethod2", new Class[]{}, int.class);
      DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
      DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
      assertFalse(delegatingMethod1.equals(delegatingMethod2));

      DelegatingMethod delegatingMethod3 = new DelegatingMethod(method1);
      assertTrue(delegatingMethod1.equals(delegatingMethod3));

      DelegatingMethod delegatingMethod4 = new DelegatingMethod(method1);
      assertTrue(delegatingMethod1.hashCode() == delegatingMethod4.hashCode());
   }
}