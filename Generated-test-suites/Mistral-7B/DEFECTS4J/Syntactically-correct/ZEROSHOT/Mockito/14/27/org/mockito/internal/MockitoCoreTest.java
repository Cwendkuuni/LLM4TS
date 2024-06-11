import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockSettings mockSettings;
 private Object mock;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mockSettings = new MockSettingsImpl();
 mock = mockitoCore.mock(String.class, mockSettings);
 }

 @Test
 public void testMockCreation() {
 // Test mock creation
 Object mock1 = mockitoCore.mock(String.class, mockSettings);
 Object mock2 = mockitoCore.mock(String.class, mockSettings);

 // Verify mocks are not equal
 assert(mock1 != mock2);

 // Test stubbing
 when(mock1).thenReturn("test");
 String result = (String) mock1.toString();
 assert(result.equals("test"));
 }

 @Test
 public void testStubbing() {
 // Test stubbing with answer
 when(mock).thenAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "test";
 }
 });

 String result = (String) mock.toString();
 assert(result.equals("test"));
 }

 @Test
 public void testMultipleStubbing() {
 when(mock).thenReturn("test1").thenReturn("test2");

 String result1 = (String) mock.toString();
 assert(result1.equals("test1"));
 String result2 = (String) mock.toString();
 assert(result2.equals("test2"));
 }

 @Test
 public void testOngoingStubbing() {
 // Test ongoing stubbing
 OngoingStubbing ongoingStubbing = mockitoCore.stub();
 when(mock).then(ongoingStubbing.answers(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "test";
 }
 }));

 String result = (String) mock.toString();
 assert(result.equals("test"));
 }

 @Test(expected = IllegalStateException.class)
 public void testStubbingWithoutPreviouslyMockedObject() {
 // Test stubbing without previously mocked object
 OngoingStubbing ongoingStubbing = mockitoCore.stub();
 ongoingStubbing.answers(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "test";
 }
 });
 }

 @Test
 public void testVerification() {
 // Test verification of mocks
 verify(mock);

 // Test verification with argument
 verify(mock).toString();

 // Test verification with multiple mocks
 Object mock1 = mockitoCore.mock(String.class, mockSettings);
 Object mock2 = mockitoCore.mock(String.class, mockSettings);
 verify(mock1, ArgumentMatchers.eq(mock2));

 // Test verification with multiple mocks and different order
 verify(mock2, ArgumentMatchers.eq(mock1));
 }

 @Test
 public void testVerificationNoInteractions() {
 // Test verification with no interactions
 Object mock1 = mockitoCore.mock(String.class, mockSettings);
 Object mock2 = mockitoCore.mock(String.class, mockSettings);
 verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testVerificationInOrder() {
 // Test verification in order
 Object mock1 = mockitoCore.mock(String.class, mockSettings);
 Object mock2 = mockitoCore.mock(String.class, mockSettings);
 InOrder inOrder = inOrder(mock1, mock2);

 when(mock1.toString()).thenReturn("test1");
 when(mock2.toString()).thenReturn("test2");

 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 }

 @Test
 public void testReset() {
 // Test reset
 reset(mock);
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerificationMode() {
 // Test verification mode
 verify(mock, VerificationMode.times(1)).toString();
 }

 @Test
 public void testVerifyNoMoreInteractionsWithNull() {
 // Test verification with null
 verifyNoMoreInteractions((Object) null);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithNotAMock() {
 // Test verification with not a mock
 Object notAMock = new Object();
 verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testAnswer() {
 // Test answer
 Answer answer = Mockito.answer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "test";
 }
 });
 OngoingStubbing ongoingStubbing = mockitoCore.stub();
 ongoingStubbing.answers(answer);
 String result = (String) mock.toString();
 assert(result.equals("test"));
 }

 @Test
 public void testDoAnswer() {
 // Test do answer
 Answer answer = Mockito.answer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "test";
 }
 });
 Stubber stubber = mockitoCore.doAnswer(answer);
 stubber.doAnswer(answer).when(mock).toString();
 String result = (String) mock.toString();
 assert(result.equals("test"));
 }

 @Test
 public void testVoidMethodStubbable() {
 // Test void method stubbable
 Object voidMock = mockitoCore.mock(VoidMethodStubbable.class, mockSettings);
 mockitoCore.stubVoid(voidMock).andThrow(new RuntimeException("test"));
 voidMock.run();
 }

 @Test
 public void testValidateMockitoUsage() {
 // Test validate mockito usage
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 // Test get last invocation
 OngoingStubbing ongoingStubbing = mockitoCore.stub();
 when(mock.toString()).then(ongoingStubbing.answers(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "test";
 }
 }));
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assert(lastInvocation.getMethod().getName().equals("toString"));
 }
}