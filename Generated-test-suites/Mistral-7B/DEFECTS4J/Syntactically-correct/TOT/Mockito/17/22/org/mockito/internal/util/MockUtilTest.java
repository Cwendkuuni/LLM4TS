package org.mockito.internal.util.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.creation.CreationSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

 private MockUtil mockUtil;
 private MockSettingsImpl settings;
 private Foo foo;
 private Bar bar;

 @Before
 public void setUp() {
 mockUtil = new MockUtil();
 settings = new MockSettingsImpl();
 foo = mock(Foo.class);
 bar = spy(Foo.class);
 }

 @Test
 public void testCreateMock() throws Exception {
 Object mock = mockUtil.createMock(Foo.class, settings);
 assertNotNull(mock);
 assertEquals(Foo.class, mock.getClass());
 verify(settings).initiateMockName(Foo.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, settings);
 }

 @Test
 public void testCreateMockWithExtraInterfaces() throws Exception {
 settings.setExtraInterfaces(new Class[]{Bar.class});
 Object mock = mockUtil.createMock(Foo.class, settings);
 assertNotNull(mock);
 assertEquals(Foo.class, mock.getClass());
 assertInstanceOf(Bar.class, mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithDuplicateExtraInterfaces() throws Exception {
 settings.setExtraInterfaces(new Class[]{Foo.class, Foo.class});
 mockUtil.createMock(Foo.class, settings);
 }

 @Test
 public void testCreateMockWithSpiedInstance() throws Exception {
 settings.setSpiedInstance(bar);
 Object mock = mockUtil.createMock(Foo.class, settings);
 assertNotNull(mock);
 assertEquals(Foo.class, mock.getClass());
 assertSame(bar, mockUtil.getMockHandler(mock).getMockSettings().getSpiedInstance());
 }

 @Test
 public void testCreateMockWithoutSpiedInstance() throws Exception {
 Object mock = mockUtil.createMock(Foo.class, settings);
 assertNotNull(mock);
 assertNull(mockUtil.getMockHandler(mock).getMockSettings().getSpiedInstance());
 }

 @Test
 public void testResetMock() throws Exception {
 Object mock = mockUtil.createMock(Foo.class, settings);
 mockUtil.resetMock(mock);
 MethodInterceptorFilter filter = (MethodInterceptorFilter) mockUtil.getInterceptor(mock);
 assertNotNull(filter);
 Whitebox.setInternalState(filter, "mockHandler", null);
 assertNull(mockUtil.getMockHandler(mock));
 }

 @Test
 public void testGetMockHandler() throws Exception {
 Object mock = mockUtil.createMock(Foo.class, settings);
 assertNotNull(mockUtil.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() throws Exception {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() throws Exception {
 Object nonMockitoMock = new Foo();
 mockUtil.getMockHandler(nonMockitoMock);
 }

 @Test
 public void testIsMock() throws Exception {
 assertFalse(mockUtil.isMock(null));
 assertFalse(mockUtil.isMock(new Foo()));
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptor() throws Exception {
 Object mock = mockUtil.createMock(Foo.class, settings);
 assertNotNull(mockUtil.getInterceptor(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() throws Exception {
 mockUtil.getInterceptor(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMockitoMock() throws Exception {
 Object nonMockitoMock = new Foo();
 mockUtil.getInterceptor(nonMockitoMock);
 }

 @Test
 public void testGetMockName() throws Exception {
 Object mock = mockUtil.createMock(Foo.class, settings);
 assertNotNull(mockUtil.getMockName(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNullMock() throws Exception {
 mockUtil.getMockName(null);
 }

 @Test
 public void testValidateType() throws Exception {
 try {
 mockUtil.validateType(Object.class);
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }

 try {
 mockUtil.validateType(String.class);
 assertDoesNotThrow(() -> mockUtil.validateType(String.class));
 } catch (Exception e) {
 fail();
 }
}

 @Test
 public void testValidateExtraInterfaces() throws Exception {
 try {
 mockUtil.validateExtraInterfaces(String.class, new Class[0]);
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }

 try {
 mockUtil.validateExtraInterfaces(Foo.class, new Class[]{Bar.class, Bar.class});
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }

 try {
 mockUtil.validateExtraInterfaces(Foo.class, new Class[]{Foo.class, Foo.class});
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }

 try {
 mockUtil.validateExtraInterfaces(Foo.class, new Class[]{Foo.class, Bar.class});
 assertDoesNotThrow(() -> mockUtil.validateExtraInterfaces(Foo.class, new Class[]{Foo.class, Bar.class}));
 } catch (Exception e) {
 fail();
 }
}

 @Test
 public void testValidateMockedType() throws Exception {
 try {
 mockUtil.validateMockedType(Object.class, "mockName");
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }

 try {
 mockUtil.validateMockedType(Foo.class, null);
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }

 try {
 mockUtil.validateMockedType(Foo.class, "mockName");
 assertDoesNotThrow(() -> mockUtil.validateMockedType(Foo.class, "mockName"));
 } catch (Exception e) {
 fail();
 }
}

 @Test
 public void testInitiateMockName() throws Exception {
 try {
 mockUtil.initiateMockName(Object.class);
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }

 try {
 mockUtil.initiateMockName("mockName");
 fail();
 } catch (IllegalArgumentException e) {
 //expected
 }

 try {
 mockUtil.initiateMockName(Foo.class);
 assertDoesNotThrow(() -> mockUtil.initiateMockName(Foo.class));
 } catch (Exception e) {
 fail();
 }
}

 @Test
 public void testImposterise() throws Exception {
 Class<?> filterClass = Mockito.mock(MethodInterceptorFilter.class);
 Object mock = mockUtil.ClassImposterizer.INSTANCE.imposterise(filterClass, Foo.class, new Class[0]);
 assertNotNull(mock);
 assertEquals(Foo.class, mock.getClass());
 }

 @Test
 public void testCopyToMock() throws Exception {
 Foo original = new Foo();
 LenientCopyTool lenientCopyTool = new LenientCopyTool();
 Object mock = mockUtil.createMock(Foo.class, settings);
 lenientCopyTool.copyToMock(original, mock);
 assertSame(original, mockUtil.getMockHandler(mock).getMockSettings().getSpiedInstance());
 }

 @Test
 public void testCopyToMockWithNullSpiedInstance() throws Exception {
 Object mock = mockUtil.createMock(Foo.class, settings);
 mockUtil.getMockHandler(mock).getMockSettings().setSpiedInstance(null);
 assertNull(mockUtil.getMockHandler(mock).getMockSettings().getSpiedInstance());
 }
}