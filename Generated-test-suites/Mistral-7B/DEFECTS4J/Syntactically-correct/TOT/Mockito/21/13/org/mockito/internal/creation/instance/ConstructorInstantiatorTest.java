import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

@Category(IntegrationTests.class)
public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @SuppressWarnings("unchecked")
 @Test
 public <T> void testNewInstanceWithParameterlessConstructor() {
 Class<T> cls = (Class<T>) Mockito.spy(SomeClassWithParameterlessConstructor.class);
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
 T instance = constructorInstantiator.newInstance(cls);
 assertNotNull(instance);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public <T> void testNewInstanceWithMissingParameterlessConstructor() {
 Class<T> cls = (Class<T>) Mockito.spy(SomeClassWithMissingConstructor.class);
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
 constructorInstantiator.newInstance(cls);
 fail("Exception expected but not thrown");
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public <T> void testNewInstanceWithNonAccessibleParameterlessConstructor() {
 Class<T> cls = (Class<T>) Mockito.spy(SomeClassWithPrivateParameterlessConstructor.class);
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
 try {
 constructorInstantiator.newInstance(cls);
 } catch (InstantiationException e) {
 //expected
 } catch (IllegalAccessException e) {
 throw new RuntimeException("Should not reach here", e);
 }
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public <T> void testNewInstanceWithWrongOuterClassType() {
 Object wrongOuterClassInstance = new Object();
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(wrongOuterClassInstance);
 Class<T> cls = (Class<T>) Mockito.spy(SomeClassWithConstructor.class);
 try {
 constructorInstantiator.newInstance(cls);
 } catch (InstantiationException e) {
 String expectedMessage = "Unable to create mock instance of 'SomeClassWithConstructor'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
 String actualMessage = e.getMessage();
 assertTrue(expectedMessage.contains(actualMessage));
 }
 }

 @SuppressWarnings("unchecked")
 @Test(expected = InstantiationException.class)
 public <T> void testNewInstanceWithExceptionInConstructor() {
 Class<T> cls = (Class<T>) Mockito.spy(SomeClassWithExceptionInConstructor.class);
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
 try {
 constructorInstantiator.newInstance(cls);
 } catch (InstantiationException e) {
 String expectedMessage = "Unable to create mock instance of 'SomeClassWithExceptionInConstructor'.\nPlease ensure it has parameter-less constructor.";
 String actualMessage = e.getMessage();
 assertTrue(expectedMessage.contains(actualMessage));
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public <T> void testNewInstanceWithConstructor() {
 Class<T> cls = (Class<T>) Mockito.spy(SomeClassWithConstructor.class);
 Constructor<T> constructor = cls.getDeclaredConstructor(String.class, int.class);
 constructor.setAccessible(true);
 Object arg1 = "arg1";
 int arg2 = 1;
 ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
 T instance = constructorInstantiator.newInstance(cls, arg1, arg2);
 assertNotNull(instance);
 }

 @Test
 public void testParamsExceptionIsThrownWhenConstructorHasParams() {
 try {
 new ConstructorInstantiator(null).newInstance(SomeClassWithConstructor.class);
 } catch (InstantiationException e) {
 String expectedMessage = "Unable to create mock instance of 'SomeClassWithConstructor'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.";
 String actualMessage = e.getMessage();
 assertTrue(expectedMessage.contains(actualMessage));
 }
 }
}

 class SomeClassWithParameterlessConstructor {
 //empty class
 }

 class SomeClassWithMissingConstructor {
 //no constructor defined
 }

 class SomeClassWithPrivateParameterlessConstructor {
 private SomeClassWithPrivateParameterlessConstructor() {
 }
 }

 class SomeClassWithConstructor {
 private String someString;
 private int someInt;

 SomeClassWithConstructor(String someString, int someInt) {
 this.someString = someString;
 this.someInt = someInt;
 }
 }

 class SomeClassWithExceptionInConstructor {
 private String someString;

 SomeClassWithExceptionInConstructor() throws Exception {
 throw new Exception("Exception in constructor");
 }
 }