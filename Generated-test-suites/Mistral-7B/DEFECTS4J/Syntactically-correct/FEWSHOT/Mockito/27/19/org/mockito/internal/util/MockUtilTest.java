package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.Mockito;

import java.lang.reflect.Method;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<MyClass> classToMock = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[]{MyInterface.class});

        MockUtil util = new MockUtil();
        MyClass mock = util.createMock(classToMock, settings);
        assertNotNull(mock);

        verify(ClassImposterizer.class).imposterise(Mockito.any(MethodInterceptorFilter.class), classToMock, Mockito.anyArray(Class.class));

        Method method = MyClass.class.getMethod("myMethod", String.class);
        verify(mock).myMethod(Mockito.eq("test"));
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockNullClass() {
        MockUtil util = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();
        util.createMock(null, settings);
    }

    @Test
    public void testCreateMockEmptyExtraInterfaces() {
        Class<MyClass> classToMock = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[0]);

        MockUtil util = new MockUtil();
        MyClass mock = util.createMock(classToMock, settings);
        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockNotAMock() {
        Object notAMock = new Object();
        MockUtil util = new MockUtil();
        util.createMock(notAMock.getClass(), new MockSettingsImpl());
    }

    @Test
    public void testResetMock() {
        MyClass mock = mock(MyClass.class);
        MockUtil util = new MockUtil();
        util.resetMock(mock);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testGetMockHandler() {
        MyClass mock = mock(MyClass.class);
        MockUtil util = new MockUtil();
        MockHandlerInterface<MyClass> handler = util.getMockHandler(mock);
        assertNotNull(handler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerNull() {
        MockUtil util = new MockUtil();
        MockHandlerInterface<MyClass> handler = util.getMockHandler(null);
    }

    @Test
    public void testIsMockitoMock() {
        MyClass mock = mock(MyClass.class);
        MockUtil util = new MockUtil();
        assertNotNull(util.isMockitoMock(mock));
    }

    @Test
    public void testIsMockitoMockNull() {
        MockUtil util = new MockUtil();
        assertNull(util.isMockitoMock(null));
    }

    @Test
    public void testGetInterceptor() {
        MyClass mock = spy(MyClass.class);
        Factory factory = (Factory) mock;
        MockUtil util = new MockUtil();
        MethodInterceptorFilter interceptorFilter = util.getInterceptor(mock);
        assertNotNull(interceptorFilter);
    }

    @Test
    public void testGetInterceptorNull() {
        MyClass mock = mock(MyClass.class);
        MockUtil util = new MockUtil();
        assertNull(util.getInterceptor(null));
    }

    @Test
    public void testGetMockName() {
        MyClass mock = mock(MyClass.class);
        MockUtil util = new MockUtil();
        MockName mockName = util.getMockName(mock);
        assertNotNull(mockName);
    }

    @Test
    public void testGetMockNameNull() {
        MyClass mock = mock(MyClass.class);
        MockUtil util = new MockUtil();
        assertNull(util.getMockName(null));
    }
}