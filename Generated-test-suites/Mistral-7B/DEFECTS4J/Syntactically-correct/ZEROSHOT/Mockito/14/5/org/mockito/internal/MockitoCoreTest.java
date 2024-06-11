import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock
 private Object mock;
 @Mock
 private Object anotherMock;
 @Mock
 private Object thirdMock;
 private List<Object> mocks;
 private InOrder order;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mocks = Arrays.asList(mock, anotherMock, thirdMock);
 order = inOrder(mock, anotherMock, thirdMock);
 }

 @Test
 public void testMockCreation() {
 Object mockedObject = mockitoCore.mock(Object.class, new MockSettings());
 verify(mockedObject).toString();
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNullClass() {
 mockitoCore.mock(null, new MockSettings());
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNullMockSettings() {
 mockitoCore.mock(Object.class, null);
 }

 @Test
 public void testStubbing() {
 when(mock.toString()).thenReturn("mockedObject");
 verify(mock).toString().equals("mockedObject");
 }

 @Test(expected = NotAMockException.class)
 public void testStubbingWithNullMock() {
 mockitoCore.when(null).toString().thenReturn("mockedObject");
 }

 @Test
 public void testStubbingWithAnswer() {
 when(mock).toString().thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "mockedObject";
 }
 });
 verify(mock).toString().equals("mockedObject");
 }

 @Test
 public void testStubbingWithVoidMethod() {
 mockitoCore.stubVoid(mock).toString();
 verify(mock).toString();
 }

 @Test
 public void testVerification() {
 verify(mock).toString();
 }

 @Test(expected = NotAMockException.class)
 public void testVerificationWithNullMock() {
 mockitoCore.verify(null, Mockito.any(VerificationMode.class));
 }

 @Test
 public void testVerificationWithVerificationMode() {
 verify(mock, Mockito.never());
 }

 @Test
 public void testVerificationWithMultipleMocks() {
 verify(mock, Mockito.never());
 verify(anotherMock, Mockito.never());
 verify(thirdMock, Mockito.never());
 }

 @Test
 public void testVerificationWithOrder() {
 order.verify(mock).methodCall1();
 order.verify(mock).methodCall2();
 order.verify(mock).methodCall3();
 }

 @Test
 public void testVerificationNoMoreInteractions() {
 verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testVerificationNoMoreInteractionsWithInOrder() {
 verifyNoMoreInteractionsInOrder(mocks, order);
 }

 @Test
 public void testReset() {
 // TODO: Implement test for reset
 }

 @Test
 public void testGetLastInvocation() {
 // TODO: Implement test for getLastInvocation
 }

 @Test
 public void testValidateMockitoUsage() {
 // TODO: Implement test for validateMockitoUsage
 }
}