package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.MockitoAnnotation;

import java.io.Serializable;
import java.lang.reflect.Proxy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        Class<PrintStream> classToMock = PrintStream.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setMockedType(classToMock);

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings);
        Assert.assertNotNull(mock);
        Assert.assertTrue(isMockitoMock(mock));

        when(System.out.printf("MockUtil.createMock(%s, %s) returned %s", classToMock.getName(), settings, mock)).thenReturn(mock);
        Object mockWithSpy = mockUtil.createMock(classToMock, settings);
        Assert.assertSame(mock, mockWithSpy);

        Class<MyClass> myClass = MyClass.class;
        settings.setSpiedInstance(spy(myClass));
        Object mockWithSpyAndName = mockUtil.createMock(myClass, settings);
        Assert.assertSame(myClass.getName() + "Mock", getMockName(mockWithSpyAndName).getName());

        MockitoAnnotation.mockStatic(System.out.class);
        try {
            mockUtil.createMock(new Object() {}, settings);
            Assert.fail("Should have thrown NotAMockException for non-class argument");
        } finally {
            MockitoAnnotation.unmock(System.out.class);
        }

        try {
            mockUtil.createMock(null, settings);
            Assert.fail("Should have thrown NotAMockException for null argument");
        } finally {
            settings.dispose();
        }

        try {
            mockUtil.createMock(String[].class, settings);
            Assert.fail("Should have thrown IllegalArgumentException for array argument");
        } finally {
            settings.dispose();
        }
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        Class<PrintStream> classToMock = PrintStream.class;
        Class<Serializable> serializableInterface = Serializable.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setMockedType(classToMock);
        settings.setExtraInterfaces(new Class[]{serializableInterface});

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings);
        Assert.assertNotNull(mock);
        Assert.assertTrue(isMockitoMock(mock));
        Assert.assertArrayEquals(new Class[]{serializableInterface}, mock.getClass().getInterfaces());
    }

    @Test
    public void testCreateMockWithNullExtraInterfaces() {
        Class<PrintStream> classToMock = PrintStream.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setMockedType(classToMock);

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings);
        Assert.assertNotNull(mock);
        Assert.assertTrue(isMockitoMock(mock));
        Assert.assertArrayEquals(new Class[0], mock.getClass().getInterfaces());
    }

    @Test
    public void testCreateMockWithMockedTypeEqualsSpiedInstance() {
        Class<MyClass> myClass = MyClass.class;
        Object spiedInstance = mock(myClass);

        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setMockedType(myClass);
        settings.setSpiedInstance(spiedInstance);

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(myClass, settings);
        Assert.assertSame(spiedInstance, mock);
    }

    @Test
    public void testCreateMockWithMockedTypeSubclassOfSpiedInstance() {
        Class<MySubClass> mySubClass = MySubClass.class;
        Object spiedInstance = mock(MyClass.class);

        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setMockedType(mySubClass);
        settings.setSpiedInstance(spiedInstance);

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(mySubClass, settings);
        Assert.assertNotNull(mock);
        Assert.assertTrue(isMockitoMock(mock));
        Assert.assertEquals(mySubClass, mock.getClass());

        try {
            ClassImposterizer.imposterise(new MethodInterceptorFilter(null, null), mySubClass, new Class[0]);
            Assert.fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);

        MockHandlerInterface<Object> handler = getMockHandler(mock);
        VerifyResettableMockHandler<Object> verifyHandler = (VerifyResettableMockHandler) handler;
        verify(verifyHandler).reset();
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNonMock() {
        Object nonMock = new Object();
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(nonMock);
    }

    @Test
    public void testGetMockHandler() {
        Object mock = mock(Object.class);
        MockUtil mockUtil = new MockUtil();
        MockHandlerInterface<Object> handler = mockUtil.getMockHandler(mock);

        Assert.assertNotNull(handler);
        Assert.assertEquals(mock, handler.getMock());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMock() {
        Object nonMock = new Object();
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(nonMock);
    }

    @Test
    public void testIsMockitoMock() {
        Object mock = mock(Object.class);
        MockUtil mockUtil = new MockUtil();
        Assert.assertTrue(mockUtil.isMockitoMock(mock));

        Object nonMock = new Object();
        Assert.assertFalse(mockUtil.isMockitoMock(nonMock));

        Factory factory = Mockito.mock(Factory.class);
        Assert.assertFalse(mockUtil.isMockitoMock(factory));

        Factory mockFactory = mock(Factory.class);
        Assert.assertTrue(mockUtil.isMockitoMock(mockFactory));
    }

    @Test
    public void testIsMock() {
        Object mock = mock(Object.class);
        MockUtil mockUtil = new MockUtil();
        Assert.assertTrue(mockUtil.isMock(mock));

        Object nonMock = new Object();
        Assert.assertFalse(mockUtil.isMock(nonMock));
    }

    @Test
    public void testGetInterceptor() {
        Object mock = mock(Object.class);
        MockUtil mockUtil = new MockUtil();

        Factory factory = (Factory) Proxy.getInvocationHandler(mock);
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
        Assert.assertSame(interceptor, factory.getCallback(0));
    }

    @Test
    public void testGetInterceptorWithNonMock() {
        Object nonMock = new Object();
        MockUtil mockUtil = new MockUtil();
        Assert.assertNull(mockUtil.getInterceptor(nonMock));
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);
        MockUtil mockUtil = new MockUtil();
        MockName mockName = mockUtil.getMockName(mock);
        Assert.assertNotNull(mockName);
        Assert.assertEquals(MockName.class.getName(), mockName.getClass().getName());
        Assert.assertEquals("mock", mockName.getName());
    }

    @Test
    public void testGetMockNameWithNamedMock() {
        Object mock = mock(Object.class, "namedMock");
        MockUtil mockUtil = new MockUtil();
        MockName mockName = mockUtil.getMockName(mock);
        Assert.assertNotNull(mockName);
        Assert.assertEquals("namedMock", mockName.getName());
    }

    private static class MyClass {
        // empty
    }

    private static class MySubClass extends MyClass {
        // empty
    }

    private static class VerifyResettableMockHandler<T> implements MockHandlerInterface<T> {
        private final MockHandler<T> delegate;

        public VerifyResettableMockHandler(MockHandler<T> delegate) {
            this.delegate = delegate;
        }

        public void reset() {
            // no-op
        }

        public void answer(Invocation invocation) {
            delegate.answer(invocation);
        }

        public void answerVoid(Invocation invocation) {
            delegate.answerVoid(invocation);
        }

        public void answerThrowable(Invocation invocation, Throwable exception) {
            delegate.answerThrowable(invocation, exception);
        }

        public void answerStatically(Class<?> targetClass, Method method, Object[] arguments, Object result) {
            delegate.answerStatically(targetClass, method, arguments, result);
        }
    }
}