package org.mockito.internal.util.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.MockCreationSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

 private MockUtil mockUtil;
 private Class<TestClass> testClass;
 private MockSettingsImpl settings;

 @Before
 public void setUp() {
 mockUtil = new MockUtil(new MockCreationValidator());
 testClass = Mockito.mock(TestClass.class);
 settings = new MockSettingsImpl();
 settings.setSpiedInstance(testClass);
 }

 @Test
 public void testCreateMock() {
 // Typical use case
 Object mock = mockUtil.createMock(TestClass.class, settings);
 assertNotNull(mock);
 assertInstanceOf(TestClass.class, mock);
 // Edge case: validating extra interfaces
 settings.setExtraInterfaces(new Class[] {AnotherTestClass.class});
 mock = mockUtil.createMock(TestClass.class, settings);
 assertNotNull(mock);
 assertInstanceOf(TestClass.class, mock);
 assertInstanceOf(AnotherTestClass.class, mock);
 // Error scenario: null class
 assertThrows(NullPointerException.class, () -> mockUtil.createMock(null, settings));
 // Error scenario: null spied instance
 assertThrows(NullPointerException.class, () -> mockUtil.createMock(TestClass.class, null));
 // Error scenario: invalid class
 assertThrows(NotAMockException.class, () -> mockUtil.createMock(Integer.class, settings));
 }

 @Test
 public void testResetMock() {
 // Typical use case
 MockHandlerInterface<TestClass> mockHandler = mockUtil.getMockHandler(testClass);
 mockUtil.resetMock(testClass);
 assertNotNull(mockHandler.getMockSettings().getInvocationNotifier());
 // Edge case: null mock
 assertThrows(NullPointerException.class, () -> mockUtil.resetMock(null));
 }

 @Test
 public void testIsMockitoMock() {
 // Typical use case: mockito mock
 assertInstanceOf(TestClass.class, mockUtil.createMock(TestClass.class, settings));
 assertTrue(mockUtil.isMockitoMock(testClass));
 // Typical use case: not mockito mock
 assertInstanceOf(NonMockitoTestClass.class, new NonMockitoTestClass());
 assertFalse(mockUtil.isMockitoMock(new NonMockitoTestClass()));
 }

 @Test
 public void testIsMock() {
 // Typical use case: mockito mock
 assertInstanceOf(TestClass.class, mockUtil.createMock(TestClass.class, settings));
 assertTrue(mockUtil.isMock(testClass));
 // Typical use case: not a mockito mock
 assertInstanceOf(NonMockitoTestClass.class, new NonMockitoTestClass());
 assertFalse(mockUtil.isMock(new NonMockitoTestClass()));
 }
}

class TestClass {
 // Test class under mock
}
class AnotherTestClass {
 // Extra interface to validate in testCreateMock() method
}
class NonMockitoTestClass {
 // Class that is not a mockito mock
}