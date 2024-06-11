package org.mockito.internal.creation;

 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.mockito.runners.JUnit4;
 import org.mockito.internal.creation.DelegatingMethod;
 import java.lang.reflect.Method;
 import java.lang.reflect.Modifier;
 import static org.hamcrest.CoreMatchers.is;
 import static org.hamcrest.CoreMatchers.nullValue;
 import static org.hamcrest.MatcherAssert.assertThat;
 import static org.mockito.internal.utils.reflection.Whitebox.*;
 import static org.mockito.internal.utils.reflection.Whitebox.getInternalValue;

 @RunWith(JUnit4.class)
 public class DelegatingMethodTest {

 private static final Class<?> TESTED_CLASS = DelegatingMethod.class;

 @Test
 public void testGetExceptionTypes() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(createTestMethod());
 Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
 Class<?>[] expectedExceptionTypes = createTestMethod().getExceptionTypes();

 assertThat(exceptionTypes, is(expectedExceptionTypes));
 }

 @Test
 public void testGetJavaMethod() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(createTestMethod());
 Method javaMethod = delegatingMethod.getJavaMethod();
 Method expectedJavaMethod = createTestMethod();

 assertThat(javaMethod, is(expectedJavaMethod));
 }

 @Test
 public void testGetName() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(createTestMethod());
 String name = delegatingMethod.getName();
 String expectedName = createTestMethod().getName();

 assertThat(name, is(expectedName));
 }

 @Test
 public void testGetParameterTypes() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(createTestMethod());
 Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
 Class<?>[] expectedParameterTypes = createTestMethod().getParameterTypes();

 assertThat(parameterTypes, is(expectedParameterTypes));
 }

 @Test
 public void testGetReturnType() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(createTestMethod());
 Class<?> returnType = delegatingMethod.getReturnType();
 Class<?> expectedReturnType = createTestMethod().getReturnType();

 assertThat(returnType, is(expectedReturnType));
 }

 @Test
 public void testIsVarArgs() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(createTestMethod());
 boolean isVarArgs = delegatingMethod.isVarArgs();
 boolean expectedIsVarArgs = createTestMethod().isVarArgs();

 assertThat(isVarArgs, is(expectedIsVarArgs));
 }

 @Test
 public void testIsAbstract() throws Exception {
 DelegatingMethod delegatingMethod = new DelegatingMethod(createTestMethod());
 boolean isAbstract = delegatingMethod.isAbstract();
 boolean expectedIsAbstract = createTestMethod().isAbstract();

 assertThat(isAbstract, is(expectedIsAbstract));
 }

 @Test
 public void testEquals() throws Exception {
 DelegatingMethod delegatingMethod1 = new DelegatingMethod(createTestMethod1());
 DelegatingMethod delegatingMethod2 = new DelegatingMethod(createTestMethod2());
 DelegatingMethod delegatingMethod3 = new DelegatingMethod(createTestMethod1());
 DelegatingMethod delegatingMethod4 = new DelegatingMethod(createTestMethod());
 Method method1 = createTestMethod1();
 Method method2 = createTestMethod2();
 Method method3 = createTestMethod1();
 Method method4 = createTestMethod();

 assertThat(delegatingMethod1, is(delegatingMethod3));
 assertThat(delegatingMethod1, is(not(delegatingMethod2)));
 assertThat(delegatingMethod1, is(not(delegatingMethod4)));
 assertThat(delegatingMethod1, is(not(method1)));
 assertThat(delegatingMethod1, is(not(method2)));
 assertThat(delegatingMethod1, is(not(method3)));
 assertThat(delegatingMethod1, is(not(method4)));
 }

 @Test
 public void testHashCode() throws Exception {
 DelegatingMethod delegatingMethod1 = new DelegatingMethod(createTestMethod1());
 DelegatingMethod delegatingMethod2 = new DelegatingMethod(createTestMethod2());
 DelegatingMethod delegatingMethod3 = new DelegatingMethod(createTestMethod1());
 DelegatingMethod delegatingMethod4 = new DelegatingMethod(createTestMethod());
 Method method1 = createTestMethod1();
 Method method2 = createTestMethod2();
 Method method3 = createTestMethod1();
 Method method4 = createTestMethod();

 int hashCode1 = delegatingMethod1.hashCode();
 int hashCode2 = delegatingMethod2.hashCode();
 int hashCode3 = delegatingMethod3.hashCode();
 int hashCode4 = delegatingMethod4.hashCode();
 int hashCodeMethod1 = method1.hashCode();
 int hashCodeMethod2 = method2.hashCode();
 int hashCodeMethod3 = method3.hashCode();
 int hashCodeMethod4 = method4.hashCode();

 assertThat(hashCode1, is(hashCode3));
 assertThat(hashCode1, is(not(hashCode2)));
 assertThat(hashCode1, is(not(hashCode4)));
 assertThat(hashCode1, is(not(hashCodeMethod1)));
 assertThat(hashCode1, is(not(hashCodeMethod2)));
 assertThat(hashCode1, is(not(hashCodeMethod3)));
 assertThat(hashCode1, is(not(hashCodeMethod4)));
 }

 private Method createTestMethod() throws Exception {
 return getMethod(TESTED_CLASS, "test", new Class[0]);
 }

 private Method createTestMethod1() throws Exception {
 return getMethod(TESTED_CLASS, "test1", new Class[0]);
 }

 private Method createTestMethod2() throws Exception {
 return getMethod(TESTED_CLASS, "test2", new Class[0]);
 }

 private Object getMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) throws Exception {
 return clazz.getMethod(methodName, parameterTypes);
 }
 }