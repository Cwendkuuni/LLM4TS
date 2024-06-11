import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.creation.instance.ConstructorInstantiator;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testNullOuterClassInstance() {
        ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
        Class<Object> objectClass = Object.class;
        try {
            InstantiationException exception = assertThrows(InstantiationException.class, () -> instantiator.newInstance(objectClass));
            String expectedMessage = "Unable to create mock instance of 'Object'.\nPlease ensure it has parameter-less constructor.";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        } catch (Exception e) {
            fail("Expected InstantiationException but got " + e.getClass().getName());
        }
    }

    @Test
    public void testParamelessConstructor() {
        Object outerClassInstance = mock(Object.class, Strictness.STRICT);
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Class<Object> objectClass = Object.class;
        Object instance = instantiator.newInstance(objectClass);
        assertNotNull(instance);
        verifyNoInteractions(outerClassInstance);
    }

    @Test
    public void testConstructorWithOneParameter() {
        Object outerClassInstance = mock(Object.class, Strictness.STRICT);
        Class<WithOuterClass> withOuterClass = WithOuterClass.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        Object innerClassInstance = new Object() {};
        Whitebox.setInternalState(outerClassInstance, "inner", innerClassInstance);

        WithOuterClass instance = instantiator.newInstance(withOuterClass);
        assertNotNull(instance);
        assertEquals(innerClassInstance, Whitebox.getInternalState(instance, "inner"));
        verify(outerClassInstance).equals(innerClassInstance);
        verify(outerClassInstance).hashCode();
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithTwoParametersThrowsException() {
        Object outerClassInstance = mock(Object.class, Strictness.STRICT);
        Class<WithOuterClassAndTwoParams> withOuterClassAndTwoParams = WithOuterClassAndTwoParams.class;
        ConstructorInstantiator instantiator = new ConstructorInstantiator(outerClassInstance);
        instantiator.newInstance(withOuterClassAndTwoParams);
    }

    @Test
    public void testParamsExceptionIsThrown() {
        Class<WithOuterClassAndTwoParams> withOuterClassAndTwoParams = WithOuterClassAndTwoParams.class;
        try {
            ConstructorInstantiator instantiator = new ConstructorInstantiator(null);
            instantiator.newInstance(withOuterClassAndTwoParams);
            fail("Expected InstantiationException but no exception was thrown");
        } catch (InstantiationException e) {
            assertEquals("Unable to create mock instance of 'WithOuterClassAndTwoParams'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor.", e.getMessage());
        }
    }

    private static class WithOuterClass {
        public WithOuterClass(Object outer) {}
    }

    private static class WithOuterClassAndTwoParams {
        public WithOuterClassAndTwoParams(Object outer, int i) {}
    }
}