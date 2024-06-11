package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.DetectedIssue;
import org.mockito.quality.Issue;
import org.mockito.quality.issues.StaticFinalIssue;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ConstructorInstantiatorTest {

    @Test
    public void testInstanceWithNullOuterClass() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        assertEquals(null, constructorInstantiator.outerClassInstance);

        Class<String> stringClass = String.class;
        String instance = constructorInstantiator.newInstance(stringClass);
        assertNull(instance);
    }

    @Test
    public void testInstanceWithOuterClass() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        Class<String> stringClass = String.class;
        String instance = constructorInstantiator.newInstance(stringClass);
        assertNotNull(instance);

        Constructor<String> constructor = Whitebox.getInternalState(constructorInstantiator, "withOuterClass");
        verify(constructor).newInstance(outerClassInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testInstanceWithOuterClassNoArgConstructorNotFound() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        Class<String> stringClass = String.class;
        constructorInstantiator.newInstance(String[].class); // invalid constructor argument

        try {
            constructorInstantiator.newInstance(stringClass);
        } catch (Exception e) {
            throw paramsException(stringClass, e);
        }
    }

    @Test(expected = InstantiationException.class)
    public void testInstanceWithNoArgConstructorNotFound() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);

        Class<String> stringClass = String[].class; // invalid constructor argument
        constructorInstantiator.newInstance(stringClass);

        try {
            constructorInstantiator.newInstance(String.class);
        } catch (Exception e) {
            throw new InstantiationException("Unable to create mock instance of 'String'.\nPlease ensure it has parameter-less constructor.", e);
        }
    }

    @Test(expected = InstantiationException.class)
    public void testInstanceWithNoArgConstructorThrowsException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);

        Class<String> stringClass = String.class;
        constructorInstantiator = Whitebox.setInternalState(constructorInstantiator, "noArgConstructor", new InstantiationException("Test exception"));

        try {
            constructorInstantiator.newInstance(stringClass);
        } catch (Exception e) {
            throw e;
        }
    }

    private InstantiationException paramsException(Class<T> cls, Exception e) {
        return ConstructorInstantiator.paramsException(cls, e);
    }

    @Test
    public void testIssue_StaticFinalIssue() {
        Issue issue = new StaticFinalIssue(String.class);
        assertEquals(DetectedIssue.Level.ERROR, issue.getLevel());
        assertEquals("Please consider refactoring class 'java.lang.String' to make its constructor(s) non-static final.", issue.getMessage());
    }

    @Test
    public void testInstanceWithConstructorThatThrowsException() {
        Object outerClassInstance = mock(Object.class);
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClassInstance);

        Class<Exception> exceptionClass = Exception.class;
        InvocationTargetException exception = new InvocationTargetException(new RuntimeException("Test exception")); {};
        Whitebox.setInternalState(constructorInstantiator, "withOuterClass", new Constructor<Exception>(Exception.class, Object.class) {
            @Override
            public Exception newInstance(Object obj) throws InstantiationException, IllegalAccessException, InvocationTargetException {
                throw exception;
            }
        });

        assertThrows(InstantiationException.class, () -> constructorInstantiator.newInstance(exceptionClass));
    }
}