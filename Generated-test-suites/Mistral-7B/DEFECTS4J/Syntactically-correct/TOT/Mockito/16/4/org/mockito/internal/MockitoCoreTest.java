import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*; // For using "given" and "then" syntax

public class MockitoCoreTest {

 private Reporter reporter;
 private MockUtil mockUtil;
 private MockingProgress mockingProgress;
 private IOngoingStubbing stubbing;
 private InOrder inOrder;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 reporter = MockitoCore.newInstance().reporter;
 mockUtil = MockitoCore.newInstance().mockUtil;
 mockingProgress = MockitoCore.newInstance().mockingProgress;
 }

 @Test
 public void testMockCreation() {
 Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
 MockSettings mockSettings = new MockSettingsImpl();
 MockSettings mockSettingsWithName = new MockSettingsImpl().name("TestMock");

 MockitoCore mockitoCore = new MockitoCore();

 Mockito.verifyNoMoreInteractions(mockitoCore); // Verifying no interactions before creating mocks

 // Testing default mock creation
 T mock1 = mockitoCore.mock(mockitoCoreClass, mockSettings);
 Mockito.verify(mock1).toString(); // Verifying that mock was created

 // Testing mock creation with custom settings
 T mock2 = mockitoCore.mock(mockitoCoreClass, mockSettingsWithName);
 Mockito.verify(mock2).toString(); // Verifying that mock was created with custom name
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNullClass() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.mock(null, new MockSettingsImpl());
 }

 @Test
 public void testStubbing() {
 IOngoingStubbing stub = mockitoCore.stub();

 given(mockitoCore.getLastInvocation()).willReturn(new InvocationOnMock(ArgumentMatchers.any(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyArray(), "methodCall"));

 OngoingStubbing<Object> stubbingWithSameReturnValue = mockitoCore.when(mockitoCore).methodCall();
 Object returnValue = "returnValue";
 stubbingWithSameReturnValue.thenReturn(returnValue);

 Mockito.verify(mockitoCore).methodCall(); // Verifying that method call was stubbed
 Mockito.verify(mockitoCore).methodCall().thenReturn(returnValue); // Verifying that stubbing was applied

 Object result = stubbingWithSameReturnValue.getMock().methodCall(); // Getting result of stubbed method call
 Mockito.assertEquals(returnValue, result); // Verifying that result matches expected return value
 }

 @Test
 public void testInOrderVerification() {
 Object mock1 = mockitoCore.mock(Object.class);
 Object mock2 = mockitoCore.mock(Object.class);

 inOrder(mock1, mock2).verify(mock1).method1();
 inOrder(mock1, mock2).verify(mock1).method2();
 inOrder(mock1, mock2).verify(mock2).method1();
 inOrder(mock1, mock2).verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = mockitoCore.mock(Object.class);
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsWithNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerify() {
 Object mock = mockitoCore.mock(Object.class);
 given(mockitoCore.verify(mock, ArgumentMatchers.isA(Object.class))).willAnswer(new Answer<Object>() {
 @Override public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Verified value";
 }
 });
 Mockito.verify(mockitoCore.verify(mock, ArgumentMatchers.isA(Object.class))).methodCall();
 Mockito.assertEquals("Verified value", mockitoCore.getLastInvocation().getArguments()[0]);
 }

 @Test
 public void testReset() {
 Object mock = mockitoCore.mock(Object.class);
 mockitoCore.reset(mock);
 Mockito.verify(mock).methodCall(); // Should not verify any interactions after reset
 }

 @Test
 public void testVerifyNoMoreInteractionsWithMultipleMocks() {
 Object mock1 = mockitoCore.mock(Object.class);
 Object mock2 = mockitoCore.mock(Object.class);

 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testAnswer() {
 Object mock = mockitoCore.mock(Object.class);
 Answer<Integer> answer = new Answer<Integer>() {
 @Override public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 };

 Mockito.verify(mockitoCore.doAnswer(answer)).methodCall();
 Mockito.verify(mockitoCore.getLastInvocation().getArguments()[0]).equals(42);
 }

 @Test
 public void testVoidMethodStubbing() {
 Object mock = mockitoCore.mock(Object.class);
 mockitoCore.stubVoid(mock).methodCall();
 Mockito.doThrow(new RuntimeException("Test exception")).when(mock).methodCall();

 try {
 mockitoCore.getMock(mock).methodCall();
 failure("Expected method call to throw an exception");
 } catch (RuntimeException e) {
 // Expected exception
 }
 }

 @Test
 public void testValidation() {
 mockitoCore.validateMockitoUsage();
 }
}