package org.mockito.internal.creation.instance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;
import org.powermock.api.support.annotation.MockStatic;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.isNull;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConstructorInstantiator.class)
@MockStatic(Constructor.class)
@PowerMockIgnore("jdk.internal.reflect.Declass")
public class ConstructorInstantiatorTest {

    @Test
    public void testNullOuterClassInstance() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
        Class<String> stringClass = String.class;
        try {
            constructorInstantiator.newInstance(stringClass);
            fail("Expected InstantiationException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNoArgConstructor() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
        Class<String> stringClass = String.class;

        String instance = constructorInstantiator.newInstance(stringClass);
        assertThat(instance, isInstanceOf(String.class));
        assertThat(instance, notNullValue());
    }

    @Test(expected = InstantiationException.class)
    public void testNoArgConstructorFailure() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;

        constructorInstantiator.newInstance(stringBuilderClass);
        fail("Expected InstantiationException");
    }

    @Test
    public void testConstructorWithOuterClass() throws Exception {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
        Class<OuterClass> outerClass = OuterClass.class;
        Class<InnerClass> innerClass = InnerClass.class;

        Stubber stubber = mock(Stubber.class);
        when(constructorInstantiator.newInstance(outerClass)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArgumentValues();
                Object outerInstance = args[0];
                constructorInstantiator = (ConstructorInstantiator) outerInstance;
                Class<?> innerClass = (Class<?>) invocation.getMethod().getParameterTypes()[0];
                constructorInstantiator = new ConstructorInstantiator(new Object());
                Constructor<InnerClass> constructor = innerClass.getDeclaredConstructor(outerClass);
                return constructor.newInstance(constructorInstantiator.outerClassInstance);
            }
        });

        OuterClass outerClassInstance = mock(OuterClass.class);
        InnerClass instance = constructorInstantiator.newInstance(innerClass);
        assertThat(instance, isInstanceOf(InnerClass.class));
        assertThat(instance, notNullValue());
        verify(constructorInstantiator, times(2)).newInstance(outerClass);
    }

    @Test(expected = InstantiationException.class)
    public void testConstructorWithOuterClassFailure() throws Exception {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
        Class<OuterClass> outerClass = OuterClass.class;
        Class<InnerClass> innerClass = InnerClass.class;

        when(ConstructorInstantiator.class.getDeclaredConstructor(outerClass.getClass()))
                .thenThrow(new InstantiationException("Mock exception"));

        OuterClass outerClassInstance = mock(OuterClass.class);

        try {
            constructorInstantiator.newInstance(innerClass);
            fail("Expected InstantiationException");
        } catch (Exception e) {
            verify(constructorInstantiator).newInstance(outerClass);
            throw e;
        }
    }

    @Test
    public void testParamsException() {
        ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(new Object());
        Class<Throwable> throwableClass = Throwable.class;

        InstantiationException exception = ConstructorInstantiator.paramsException(Throwable.class, new Throwable());

        assertThat(exception, isA(InstantiationException.class));
        assertThat(exception.getMessage(), is("Unable to create mock instance of 'Throwable'.\nPlease ensure that the outer instance has correct type and that the target class has parameter-less constructor."));
    }

}

class OuterClass {
}

class InnerClass {
    InnerClass(Object outer) {
    }
}