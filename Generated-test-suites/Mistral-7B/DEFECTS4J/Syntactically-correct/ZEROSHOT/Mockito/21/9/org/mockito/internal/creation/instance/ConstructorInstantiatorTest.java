package org.mockito.internal.creation.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ExactInvocationMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConstructorInstantiatorTest {

    private ConstructorInstantiator constructorInstantiator;
    private Object outerClassInstance;

    @Before
    public void setup() {
        outerClassInstance = mock(Object.class);
        constructorInstantiator = new ConstructorInstantiator(outerClassInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorNullOuterClassInstance() {
        constructorInstantiator = new ConstructorInstantiator(null);
        constructorInstantiator.newInstance(String.class);
    }

    @Test
    public void testConstructorNoArgs() {
        String result = constructorInstantiator.newInstance(String.class);
        verify(outerClassInstance, never()).equals(any());
        Assert.assertNotNull(result);
        Assert.assertEquals("", result.toString());
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithArgsNoConstructor() {
        when(outerClassInstance.getClass()).thenReturn(Object.class);

        constructorInstantiator.newInstance(StringWithOuterClass.class);
    }

    @Test
    public void testConstructorWithArgs() {
        StringWithOuterClass result = constructorInstantiator.newInstance(StringWithOuterClass.class);

        Constructor<StringWithOuterClass> constructor = StringWithOuterClass.class.getDeclaredConstructor(Object.class);
        constructor.setAccessible(true);

        verify(outerClassInstance, new ExactInvocationMatcher(new Object[]{outerClassInstance})).equals(result);

        InvocationOnMock invocation = Mockito.argThat(new ExactInvocationMatcher(new Object[]{outerClassInstance})).getMock().invocations().get(0);
        Answer answer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new StringWithOuterClass("test");
            }
        };
        when(constructor).answer(answer);

        StringWithOuterClass expectedResult = new StringWithOuterClass("test");
        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorException() {
        when(outerClassInstance.getClass()).thenReturn(StringWithOuterClassException.class);

        constructorInstantiator.newInstance(String.class);
    }

    @Test
    public void testConstructorExceptionThrown() {
        try {
            when(outerClassInstance.getClass()).thenThrow(new InstantiationException("Test"));

            constructorInstantiator.newInstance(String.class);
            fail("Should have thrown InstantiationException");
        } catch (InstantiationException e) {
            Assert.assertEquals("Test", e.getMessage());
        }
    }

    private static class StringWithOuterClass {
        private final Object outerClass;

        public StringWithOuterClass() {
            this.outerClass = outerClass;
        }

        public StringWithOuterClass(Object outerClass) {
            this.outerClass = outerClass;
        }

        public String toString() {
            return "";
        }
    }

    private static class StringWithOuterClassException extends Exception {
        public StringWithOuterClassException() {
            super();
        }

        public StringWithOuterClassException(String message) {
            super(message);
        }

        public StringWithOuterClassException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}