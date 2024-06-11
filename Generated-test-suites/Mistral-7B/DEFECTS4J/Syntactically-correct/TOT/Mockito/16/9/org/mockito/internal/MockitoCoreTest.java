import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoCore;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.doAnswer;
import static org.junit.Assert.assertNotNull;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock1;
 private Object mock2;
 private Object mock3;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 mock1 = mock(Object.class);
 mock2 = mock(Object.class);
 mock3 = mock(Object.class);
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class);
 assertNotNull(mock);
 }

 @Test
 public void testStubbing() {
 when(mock1).thenReturn("Some Value");
 String result = (String) mock1.someMethod();
 verify(mock1).someMethod();
 assertEquals("Some Value", result);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoInteractionWithNotAMock() {
 verify(new Object());
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock1, mock2, mock3);
 verifyNoMoreInteractions(mock1, mock2, mock3);
 }

 @Test
 public void testInOrder() {
 Object mock4 = mock(Object.class);
 List<Object> mocks = Arrays.asList(mock1, mock2, mock3, mock4);
 InOrder inOrder = mockitoCore.inOrder(mocks.toArray(new Object[0]));

 verify(mock1).someMethod();
 verify(mock2).someOtherMethod();
 inOrder.verify(mock3).someMethod();
 inOrder.verify(mock4).someOtherMethod();
 }

 @Test
 public void testAnswer() {
 Answer<String> answer = invocation -> "Answer Value";
 doAnswer(answer).when(mock1).someMethod();
 String result = (String) mock1.someMethod();
 verify(mock1).someMethod();
 assertEquals("Answer Value", result);
 }

 @Test
 public void testVoidMethodStubbable() {
 mockitoCore.stubVoid(mock1).someVoidMethod();
 verify(mock1).someVoidMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Invocation invocation = mockitoCore.getLastInvocation();
 assertNotNull(invocation);
 }
}