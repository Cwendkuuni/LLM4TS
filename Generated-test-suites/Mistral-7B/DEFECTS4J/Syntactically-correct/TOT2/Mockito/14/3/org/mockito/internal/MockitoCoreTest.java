package org.mockito.test;

import org.junit.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockitoCoreTest {

 @Before
 MockitoCore mockitoCore = new MockitoCore();

 @Test
 public void testMockMethod() {
 String mock = mock(String.class);
 assertNotNull(mock);
 }

 @Test
 public void testMockNullClass() {
 try {
 mockitoCore.mock(null, new MockSettingsImpl());
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testMockNullSettings() {
 try {
 mockitoCore.mock(String.class, null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testMockFinalClass() {
 try {
 mockitoCore.mock(Object.class, new MockSettingsImpl());
 fail();
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testMockPrimitiveType() {
 try {
 mockitoCore.mock(int.class, new MockSettingsImpl());
 fail();
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testMockExceptionInMockedClass() {
 Exception mockedException = new Exception("Test exception");
 Class<Exception> exceptionClass = Exception.class;
 try {
 MockitoCore mockitoCoreWithException = new MockitoCore() {
 @Override
 public Exception createMock(Class<?> clazz, MockSettings mockSettings) {
 throw mockedException;
 }
 };
 mockitoCoreWithException.mock(exceptionClass, new MockSettingsImpl());
 fail();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testMockClassWithoutConstructor() {
 try {
 mockitoCore = new MockitoCore() {
 @Override
 public <T> T mock(Class<T> clazz, MockSettings mockSettings) {
 throw new RuntimeException("Test exception - class without constructor");
 }
 };
 mockitoCore.mock(String.class, new MockSettingsImpl());
 fail();
 } catch (RuntimeException e) {
 // expected
 }
 }

 @Test
 public void testMockMethodWithSettings() {
 String mock = mock(String.class, withSettings().lenient());
 assertNotNull(mock);
 }

 @Test
 public void testMockMethodWithCustomAnswer() {
 MockitoCore mockitoCore = mock(MockitoCore.class);
 String answer = "Test answer";
 when(mockitoCore.mock(String.class)).thenReturn(answer);
 assertEquals(answer, mockitoCore.mock(String.class));
 }

 @Test
 public void testMockMethodWithCapture() {
 MockitoCore mockitoCore = mock(MockitoCore.class);
 when(mockitoCore.mock(String.class)).thenReturn(mock(String.class));
 assertEquals(mockitoCore.mock(String.class), mockitoCore.mock(String.class));
 }

 @Test
 public void testMockMethodWithMultipleMocks() {
 MockitoCore mockitoCore = mock(MockitoCore.class);
 String mock1 = mockitoCore.mock(String.class);
 String mock2 = mockitoCore.mock(String.class);
 assertNotSame(mock1, mock2);
 }

 @Test
 public void testMockMethodWithVerify() {
 MockitoCore mockitoCore = mock(MockitoCore.class);
 String mock = mockitoCore.mock(String.class);
 verify(mock).toString();
 }

 @Test
 public void testMockMethodWithVerifyNoInteractions() {
 MockitoCore mockitoCore = mock(MockitoCore.class);
 String mock = mockitoCore.mock(String.class);
 verify(mock, never()).toString();
 }

 @Test
 public void testMockMethodWithVerifyOrder() {
 MockitoCore mockitoCore = mock(MockitoCore.class);
 String mock1 = mockitoCore.mock(String.class);
 String mock2 = mockitoCore.mock(String.class);
 inOrder(mock1, mock2).verify(mock1).toString();
 inOrder(mock1, mock2).verify(mock2).toString();
 }

 @Test
 public void testMockMethodWithDoAnswer() {
 MockitoCore mockitoCore = mock(MockitoCore.class);
 Answer<String> answer = new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Test answer";
 }
 };
 when(mockitoCore.mock(String.class)).thenAnswer(answer);
 assertEquals("Test answer", mockitoCore.mock(String.class));
 }

 @Test
 public void testMockMethodWithVerifyException() {
 try {
 MockitoCore mockitoCore = mock(MockitoCore.class);
 String mock = mockitoCore.mock(String.class);
 verify(mock).toString();
 verifyNoMoreInteractions(mockitoCore);
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testMockMethodWithReset() {
 MockitoCore mockitoCore = mock(MockitoCore.class);
 String mock = mockitoCore.mock(String.class);
 verify(mock).toString();
 reset(mockitoCore);
 verify(mock, never()).toString();
 }
}