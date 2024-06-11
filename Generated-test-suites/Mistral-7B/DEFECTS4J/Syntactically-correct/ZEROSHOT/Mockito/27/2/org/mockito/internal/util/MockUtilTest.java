package org.mockito.internal.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Answer2;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockUtilTest {

 private MockCreationValidator creationValidator;
 private MockUtil mockUtil;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 creationValidator = Mockito.spy(new MockCreationValidator());
 mockUtil = new MockUtil(creationValidator);
 }

 @Test
 public void testCreateMock() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String spiedInstance = "test instance";

 mockUtil.validateType(classToMock);
 mockUtil.validateExtraInterfaces(classToMock, new Class[0]);
 mockUtil.validateMockedType(classToMock, spiedInstance);

 String mockedMock = (String) mockUtil.createMock(classToMock, settings);

 Assert.assertNotNull(mockedMock);
 Assert.assertNotSame(spiedInstance, mockedMock);

 Class<?>[] ancillaryTypes = settings.getExtraInterfaces();
 if (ancillaryTypes != null && ancillaryTypes.length > 0) {
 Assert.assertTrue(Mockito.isAssignableFrom(Serializable.class, ancillaryTypes[0]));
 }

 when(spiedInstance.toString()).thenReturn("mocked string");
 verify(spiedInstance).toString();
 verify(mockedMock).toString();
 Assert.assertEquals("mocked string", mockedMock.toString());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockNullMockHandler() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String spiedInstance = null;
 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockInvalidType() {
 Class<String> classToMock = int.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String spiedInstance = "test instance";
 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockInvalidSpiedInstance() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = null;
 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<String> classToMock = String.class;
 Class<Serializable> serializableInterface = Serializable.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String spiedInstance = "test instance";
 settings.setExtraInterfaces(new Class[] { serializableInterface });

 String mockedMock = (String) mockUtil.createMock(classToMock, settings);
 Assert.assertNotNull(mockedMock);
 Assert.assertNotSame(spiedInstance, mockedMock);

 Class<?>[] ancillaryTypes = settings.getExtraInterfaces();
 Assert.assertEquals(1, ancillaryTypes.length);
 Assert.assertTrue(Mockito.isAssignableFrom(Serializable.class, ancillaryTypes[0]));

 when(spiedInstance.toString()).thenReturn("mocked string");
 verify(spiedInstance).toString();
 verify(mockedMock).toString();
 Assert.assertEquals("mocked string", mockedMock.toString());
 }

 @Test
 public void testCreateMockWithSpiedInstance() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String spiedInstance = "test instance";

 String mockedMock = (String) mockUtil.createMock(classToMock, settings);
 Assert.assertNotNull(mockedMock);
 Assert.assertNotSame(spiedInstance, mockedMock);

 when(spiedInstance.toString()).thenReturn("mocked string");
 verify(spiedInstance).toString();
 verify(mockedMock).toString();
 Assert.assertEquals("mocked string", mockedMock.toString());

 LenientCopyTool.copyToMock(spiedInstance, mockedMock);
 verify(mockedMock).equals(spiedInstance);
 }

 @Test
 public void testResetMock() {
 String mockedMock = mock(String.class);
 mockUtil.resetMock(mockedMock);

 verifyZeroInteractions(mockedMock);
 }

 @Test
 public void testResetMockWithCustomMockHandler() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String spiedInstance = "test instance";
 settings.setMockHandler(new MockHandler<String>() {
 @Override
 public void handleMockReset() {
 // Custom implementation
 }
 });

 String mockedMock = (String) mockUtil.createMock(classToMock, settings);
 verifyZeroInteractions(mockedMock);

 mockUtil.resetMock(mockedMock);
 verify(mockedMock).handleMockReset();
 }

 @Test
 public void testIsMock() {
 Object mock = mock(Object.class);
 Object nonMock = new Object();

 Assert.assertTrue(mockUtil.isMock(mock));
 Assert.assertFalse(mockUtil.isMock(nonMock));
 }

 @Test
 public void testGetMockHandler() {
 String mockedMock = mock(String.class);
 Object handler = mockUtil.getMockHandler(mockedMock);

 Assert.assertNotNull(handler);
 Assert.assertTrue(handler instanceof MockHandler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerNullMock() {
 Object mock = null;
 mockUtil.getMockHandler(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerNonMock() {
 Object nonMock = new Object();
 mockUtil.getMockHandler(nonMock);
 }

 @Test
 public void testIsMockitoMock() {
 String mockedMock = mock(String.class);
 Object handler = Proxy.getInvocationHandler(mockedMock);

 Assert.assertTrue(mockUtil.isMockitoMock(mockedMock));
 Assert.assertTrue(handler instanceof InvocationHandler);
 Assert.assertTrue(handler instanceof MockHandler);
 }

 @Test(expected = NotAMockException.class)
 public void testIsMockitoMockNullMock() {
 Object mock = null;
 mockUtil.isMockitoMock(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testIsMockitoMockNonMock() {
 Object nonMock = new Object();
 mockUtil.isMockitoMock(nonMock);
 }

 @Test
 public void testGetInterceptor() {
 String mockedMock = mock(String.class);
 Object handler = Proxy.getInvocationHandler(mockedMock);

 MockHandler mockHandler = (MockHandler) handler;
 MethodInterceptorFilter interceptorFilter = mockUtil.getInterceptor(mockedMock);

 Assert.assertNotNull(interceptorFilter);
 Assert.assertSame(mockHandler, interceptorFilter.getMockHandler());
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorNullMock() {
 Object mock = null;
 mockUtil.getInterceptor(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorNonMock() {
 Object nonMock = new Object();
 mockUtil.getInterceptor(nonMock);
 }

 @Test
 public void testGetMockName() {
 String mockedMock = mock(String.class);
 String expectedMockName = "String mock";
 MockHandler<String> mockHandler = mockUtil.getMockHandler(mockedMock);

 String actualMockName = mockUtil.getMockName(mockedMock);

 Assert.assertEquals(expectedMockName, actualMockName);
 }

 @Test
 public void testGetMockNameWithCustomMockName() {
 String mockedMock = mock(String.class);
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setMockName("CustomMockName");

 String actualMockName = mockUtil.getMockName(mockedMock);

 Assert.assertEquals("CustomMockName", actualMockName);
 }

 @Test
 public void testResetMockWithCustomAnswer() {
 String mockedMock = mock(String.class);
 Answer<String> answer = mock(Answer.class);
 MockHandler<String> mockHandler = new MockHandler<String>(answer);
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setMockHandler(mockHandler);

 String expectedAnswer = "reset answer";
 when(answer.answer(Mockito.eq(0), Mockito.eq(null), Mockito.anyObject())).thenReturn(expectedAnswer);

 String result = mockUtil.resetMock(mockedMock);
 Assert.assertEquals(expectedAnswer, result);

 verify(answer).answer(Mockito.eq(0), Mockito.eq(null), Mockito.anyObject());
 }

 @Test
 public void testResetMockWithCustomAnswer2() {
 String mockedMock = mock(String.class);
 Answer2<String, String> answer = mock(Answer2.class);
 MockHandler<String> mockHandler = new MockHandler<String>(answer);
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setMockHandler(mockHandler);

 String expectedAnswer = "reset answer";
 when(answer.answer(Mockito.eq(0), Mockito.eq("arg1"), Mockito.anyObject())).thenReturn(expectedAnswer);

 String result = mockUtil.resetMock(mockedMock);
 Assert.assertEquals(expectedAnswer, result);

 verify(answer).answer(Mockito.eq(0), Mockito.eq("arg1"), Mockito.anyObject());
 }

 @Test
 public void testMockitoMethodsAreProtected() {
 try {
 mockUtil.ClassImposterizer.INSTANCE.imposterise(null, String.class, new Class[0]);
 fail("Mockito.ClassImposterizer is not accessible");
 } catch (NoSuchFieldException e) {
 // Expected
 }

 try {
 mockUtil.LenientCopyTool.copyToMock(null, null);
 fail("LenientCopyTool.copyToMock is not accessible");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testMockCreationValidatorIsUsed() {
 MockCreationValidator creationValidatorSpy = mockitoSpy(creationValidator);
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String spiedInstance = "test instance";

 mockUtil.validateType(classToMock);
 verify(creationValidatorSpy).validateType(classToMock);

 mockUtil.validateExtraInterfaces(classToMock, new Class[0]);
 verify(creationValidatorSpy).validateExtraInterfaces(classToMock, new Class[0]);

 mockUtil.validateMockedType(classToMock, spiedInstance);
 verify(creationValidatorSpy).validateMockedType(classToMock, spiedInstance);
 }
}