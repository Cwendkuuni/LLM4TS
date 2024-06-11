package org.mockito.internal.util.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockUtil.CreationValidator;
import org.mockito.internal.util.MockitoInvocation;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.LenientReflector;
import org.mockito.internal.util.reflection.Reflector;
import org.mockito.probes.Probes;
import org.powermock.api.mockito.internal.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class MockUtilTest {

 private MockUtil mockUtil;
 private Reflector reflector;
 private FieldSetter fieldSetter;
 private Probes probes;
 private ClassImposterizer classImposterizer;
 private CreationValidator creationValidator;
 private Object spiedInstance;

 @Rule
 public MockitoRule mockitoRule = new MockitoRule();

 @Before
 public void setup() {
 reflector = new LenientReflector();
 fieldSetter = new FieldSetter();
 probes = new Probes();
 classImposterizer = Mockito.spy(ClassImposterizer.INSTANCE);
 creationValidator = mock(CreationValidator.class);
 mockUtil = new MockUtil(creationValidator);
 spiedInstance = new Object();
 }

 @Test
 public void testCreateMockWithValidArguments() {
 Class<MockUtil> classToMock = MockUtil.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setMockName("MockUtilTestMock");
 Object mock = mockUtil.createMock(classToMock, settings);

 assertNotNull(mock);
 verify(classImposterizer).imposterise(any(MethodInterceptorFilter.class), eq(classToMock), anyArray());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClassToMock() {
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSettings() {
 mockUtil.createMock(MockUtil.class, null);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSpiedInstance() {
 mockUtil.createMock(MockUtil.class, new MockSettingsImpl(), null);
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<MockUtil> classToMock = MockUtil.class;
 Class<?> extraInterface = Mockito.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class<?>[]{extraInterface});
 settings.setMockName("MockUtilTestMock");
 Object mock = mockUtil.createMock(classToMock, settings);

 assertNotNull(mock);
 verify(classImposterizer, times(1)).imposterise(any(MethodInterceptorFilter.class), eq(classToMock), anyArray());
 verify(classImposterizer, times(1)).imposterise(any(MethodInterceptorFilter.class), eq(extraInterface), anyArray());
 }

 @Test
 public void testResetMock() {
 Object mock = mock(MockHandler.class);
 mockUtil.resetMock(mock);
 verify(mock, times(1)).setCallback(anyInt(), any(MethodInterceptorFilter.class));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test
 public void testIsMockitoMockWithMockObject() {
 MockHandler handler = mock(MockHandler.class);
 Factory factory = mockitoRule.createMock(Factory.class);
 factory.setCallback(0, handler);
 Object mock = factory.getMock();

 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockWithNonMockObject() {
 Object nonMockObject = new Object();
 assertFalse(mockUtil.isMockitoMock(nonMockObject));
 }

 @Test(expected = NotAMockException.class)
 public void testIsMockWithNullMock() {
 mockUtil.isMock(null);
 }

 @Test
 public void testGetInterceptorWithMockObject() {
 Object mock = mock(Factory.class);
 Factory factory = (Factory) mock;
 Callback callback = factory.getCallback(0);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertEquals(callback, interceptor.getHandler());
 }

 @Test
 public void testGetMockNameWithMockObject() {
 Object mock = mock(MockHandler.class);
 MockSettingsImpl settings = withSettings().defaultAnswer(RETURNS_DEFAULTS);
 MockHandler mockHandler = new MockHandler(settings);
 MockHandlerInterface mockHandlerInterface = mockHandler;
 MockName mockName = mockUtil.getMockName(mockHandlerInterface);
 assertNotNull(mockName);
 assertEquals("MockHandlerTestMock", mockName.getMockName());
 }
}