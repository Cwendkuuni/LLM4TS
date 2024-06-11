import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import static org.mockito.Mockito.*;

public class ConstructorInstantiatorTest {

 private Object outerClassInstance;

 @Test
 public void testNewInstanceWithNullOuterClass() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 try {
 InstantiationException exception = assertThrows(InstantiationException.class, () -> instantiator.newInstance(String.class));
 assertEquals("Unable to create mock instance of 'String'.\nPlease ensure it has parameter-less constructor.", exception.getMessage());
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testNewInstanceWithParameterlessConstructor() {
 outerClassInstance = mock(SomeClass.class);
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
 String result = instantiator.newInstance(String.class);
 assertNotNull(result);
 }

 @Test
 public void testNewInstanceWithOuterClass() {
 SomeClass outerClass = mock(SomeClass.class);
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClass);
 String result = instantiator.newInstance(String.class);
 assertNotNull(result);
 verify(outerClass).someMethod(); //verify interaction with outer class
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstanceWithNoArgConstructorFailure() {
 ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
 instantiator.newInstance(SomeClassWithParams.class);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstanceWithOuterClassConstructorFailure() {
 SomeClass outerClass = mock(SomeClass.class);
 Constructor<SomeClassWithParams> constructor = SomeClassWithParams.class.getDeclaredConstructor(outerClass.getClass());
 ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClass);
 instantiator.newInstance(SomeClassWithParams.class);
 }
}