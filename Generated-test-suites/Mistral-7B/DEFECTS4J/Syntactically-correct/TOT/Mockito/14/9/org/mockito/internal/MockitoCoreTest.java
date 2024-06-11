import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.MockitoCore;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 verify(mockitoCore).mockingStarted(eq(mock), eq(String.class), any(MockSettings.class));
 }

 @Test
 public void testStubbing() {
 ArgumentCaptor<Answer<Integer>> answerCaptor = ArgumentCaptor.forClass(Answer.class);
 mockitoCore.stubbingStarted();
 OngoingStubbing<String> stubbing = mockitoCore.when(mock);
 stubbing.thenReturn(42);
 verify(mockitoCore).pullOngoingStubbing();

 verify(mockitoCore).verify(eq(mock), eq(VerificationMode.times(1)), any(VerificationMode.class));
 verify(mockitoCore).reset();

 verify(mockitoCore, never()).verifyNoMoreInteractions(any());

 ArgumentCaller argumentCaller = new ArgumentCaller();
 stubbing.thenAnswer(answerCaptor.capture());
 argumentCaller.passArgument(mock, 1);

 assertEquals(42, argumentCaller.getReturnValue());
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNullMock() {
 mockitoCore.verify(null, VerificationMode.times(1));
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, VerificationMode.times(1));
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock);
 verify(mockitoCore).reset();
 verify(mockitoCore).resetOngoingStubbing();
 verify(mockitoCore).reset();
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.mockingProgress.mockingStarted(mock, String.class, new MockSettingsImpl());
 mockitoCore.mockingProgress.stubbingStarted();
 when(mock).thenReturn("mockito");

 mockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = mockitoCore.mock(String.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Integer.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

 verifyNoMoreInteractionsInOrder(inOrder, Arrays.asList(mock1, mock2));
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsOnNull() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsOnEmptyList() {
 mockitoCore.verifyNoMoreInteractionsInOrder(Collections.emptyList(), new InOrderImpl(Collections.emptyList()));
 }

 @Test
 public void testVerifyNoMoreInteractionsOnListWithNull() {
 Object mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 Object notAMock = new Object();
 List<Object> mocks = Arrays.asList(mock, notAMock);

 mockitoCore.verifyNoMoreInteractionsInOrder(mocks, new InOrderImpl(mocks));
 }

 @Test
 public void testVerifyNoMoreInteractionsOnListWithNotAMock() {
 Object mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 Object notAMock = new Object();
 List<Object> mocks = Arrays.asList(mock, notAMock);

 mockitoCore.verifyNoMoreInteractionsInOrder(mocks, new InOrderImpl(mocks));
 }

 @Test
 public void testVerifyNoMoreInteractionsOnListWithMocks() {
 Object mock1 = mockitoCore.mock(String.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(String.class, new MockSettingsImpl());
 List<Object> mocks = Arrays.asList(mock1, mock2);

 mockitoCore.verifyNoMoreInteractionsInOrder(mocks, new InOrderImpl(mocks));
 }

 @Test
 public void testInOrderCreationNull() {
 mockitoCore.mocksHaveToBePassedWhenCreatingInOrder();
 }

 @Test
 public void testInOrderCreationEmpty() {
 mockitoCore.mocksHaveToBePassedToCreateInOrder();
 }

 @Test
 public void testInOrderCreationWithNull() {
 Object mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 Object notAMock = new Object();
 InOrder inOrder = mockitoCore.inOrder(mock, notAMock);
 }

 @Test
 public void testInOrderCreationWithNotAMock() {
 Object mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 Object notAMock = new Object();
 InOrder inOrder = mockitoCore.inOrder(mock, notAMock);
 }

 @Test
 public void testInOrderCreationWithMocks() {
 Object mock1 = mockitoCore.mock(String.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(String.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 }

 @Test
 public void testAnswer() {
 ArgumentCaptor<Answer<Integer>> answerCaptor = ArgumentCaptor.forClass(Answer.class);
 OngoingStubbing<String> stubbing = mockitoCore.when(mock);
 Answer<Integer> answer = new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 };
 stubbing.thenAnswer(answer);
 ArgumentCaller argumentCaller = new ArgumentCaller();
 stubbing.thenAnswer(answerCaptor.capture());
 argumentCaller.passArgument(mock, 1);

 assertEquals(42, argumentCaller.getReturnValue());
 }

 @Test
 public void testStubVoid() {
 Object mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 MockHandlerInterface<String> handler = mockitoCore.getMockHandler(mock);
 handler.voidMethodStubbable(mock).voidMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 OngoingStubbing<String> stubbing = mockitoCore.when(mock);
 stubbing.thenReturn("mockito");
 ArgumentCaller argumentCaller = new ArgumentCaller();
 stubbing.thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 argumentCaller.passArgument(mock, invocation.getArgument(0, Integer.class));
 return "result";
 }
 });
 argumentCaller.passArgument(mock, 1);
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertEquals("result", lastInvocation.getMethod().invoke(mock, 1));
 }
}