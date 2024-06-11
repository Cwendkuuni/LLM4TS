import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockitoCore;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock
 private Object mock1;
 @Mock
 private Object mock2;
 @InjectMocks
 private Object systemUnderTest;
 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.<Object>mock(Object.class);
 verify(mockitoCore).mockingStarted(mock, Object.class, (MockSettingsImpl) MockSettings.none());
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNull() {
 mockitoCore.<Object>mock(null);
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithInvalidClass() {
 mockitoCore.<String>mock(Object.class);
 }

 @Test
 public void testStubbing() {
 when(mock1).thenReturn("test");
 verify(mockitoCore).stubbingStarted();
 verify(mock1).toString();
 }

 @Test(expected = NotAMockException.class)
 public void testStubbingWithNull() {
 mockitoCore.stubbingStarted();
 }

 @Test
 public void testStubbingWithVoidReturnType() {
 when(systemUnderTest.doSomething()).thenReturn(1);
 verify(systemUnderTest).doSomething();
 verify(mockitoCore).stubbingStarted();
 }

 @Test
 public void testAnswer() {
 ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
 when(mock1.doSomething(argumentCaptor.capture())).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 });
 verify(mock1).doSomething("test");
 verify(mockitoCore).stubbingStarted();
 verify(mockitoCore).resetOngoingStubbing();
 int result = argumentCaptor.getValue();
 assertEquals(1, result);
 }

 @Test
 public void testVerifyNoInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock1);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoInteractionsWithNull() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoInteractionsWithNonMock() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testVerifyOrder() {
 ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
 Object[] mocks = {mock1, mock2};
 InOrder inOrder = mockitoCore.inOrder(mocks);
 when(mock1.doSomething(argumentCaptor1.capture())).thenReturn("test1");
 when(mock2.doSomething(argumentCaptor2.capture())).thenReturn("test2");
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 mockitoCore.verifyNoMoreInteractionsInOrder(asList(mock1, mock2), inOrder);
 verify(mock1).doSomething("test1");
 verify(mock2).doSomething("test2");
 }

 @Test
 public void testVerifyOrderWithException() {
 ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
 Object[] mocks = {mock1, mock2};
 InOrder inOrder = mockitoCore.inOrder(mocks);
 when(mock1.doSomething(argumentCaptor1.capture())).thenThrow(new RuntimeException());
 when(mock2.doSomething(argumentCaptor2.capture())).thenReturn("test2");
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 mockitoCore.verifyNoMoreInteractionsInOrder(asList(mock1, mock2), inOrder);
 verify(mock1).doSomething("test1");
 verify(mock2).doSomething("test2");
 }

 @Test
 public void testVerifyOrderWithNull() {
 ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> argumentCaptor2 = ArgumentCaptor.forClass(Integer.class);
 Object[] mocks = {mock1, null};
 InOrder inOrder = mockitoCore.inOrder(mocks);
 when(mock1.doSomething(argumentCaptor1.capture())).thenReturn("test1");
 when(mock2.doSomething(argumentCaptor2.capture())).thenReturn("test2");
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 mockitoCore.verifyNoMoreInteractionsInOrder(asList(mock1, mock2), inOrder);
 verify(mock1).doSomething("test1");
 verify(mock2).doSomething("test2");
 }

 @Test
 public void testVerifyOrderWithEmptyList() {
 mockitoCore.verifyNoMoreInteractionsInOrder(asList(), mockitoCore.inOrder());
 }

 @Test
 public void testVerifyOrderWithNullInOrder() {
 mockitoCore.verifyNoMoreInteractionsInOrder(asList(mock1), null);
 }

 @Test
 public void testVerifyOrderWithNullMock() {
 mockitoCore.verifyNoMoreInteractionsInOrder(asList(null), mockitoCore.inOrder());
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock1, mock2);
 verify(mock1).reset();
 verify(mock2).reset();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyInteractionsWithNull() {
 mockitoCore.verify(null, Mockito.any());
 }
}