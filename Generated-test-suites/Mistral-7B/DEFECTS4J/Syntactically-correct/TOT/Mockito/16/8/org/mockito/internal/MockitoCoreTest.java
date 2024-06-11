import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.reset;

public class MockitoCoreTest {

 private Reporter reporter;
 private MockUtil mockUtil;
 private MockingProgress mockingProgress;
 private IOngoingStubbing ongoingStubbing;
 private Invocation lastInvocation;

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
 MockitoCore mockitoCore = MockitoCore.newInstance();

 MockitoCore mockedMockitoCore = mockitoCore.mock(mockitoCoreClass, mockSettings, true);

 verify(mockUtil).createMock(mockitoCoreClass, mockSettings);
 }

 @Test
 public void testStubbing() {
 Object mock = mock(Object.class);

 when(mock.toString()).thenReturn("Test Mock");

 String result = (String) mock.toString();

 assertEquals("Test Mock", result);

 verify(mock).toString();
 }

 @Test
 public void testReset() {
 Object mock = mock(Object.class);

 when(mock.toString()).thenReturn("Test Mock");
 String result = (String) mock.toString();

 assertEquals("Test Mock", result);

 reset(mock);

 result = (String) mock.toString();

 assertEquals(null, result);

 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerify() {
 Object mock = Mockito.mock(Object.class);

 verify(mock).toString();
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNullMock() {
 verify(null);
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 verify(notAMock);
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testVerifyNoMoreInteractionsNullMock() {
 verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = mock(Object.class);
 mock.toString();
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 Object notAMock = new Object();
 verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InOrder inOrder = inOrder(mock1, mock2);

 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 }

 @Test
 public void testAnswer() {
 Object mock = mock(Object.class);

 Answer<String> answer = new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Answer: " + invocation.getMethod()[0].getName();
 }
 };

 when(mock.toString()).thenAnswer(answer);

 String result = (String) mock.toString();

 assertEquals("Answer: toString", result);
 }

 @Test
 public void testVoidMethodStubbable() {
 Object mock = mock(Object.class);

 doAnswer(invocation -> System.out.println("Answer: " + invocation.getMethod()[0].getName()))
 .when(mock).voidMethod();

 mock.voidMethod();

 verify(mock).voidMethod();
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = mock(Object.class);when(mock.toString()).thenReturn("MockitoCoreTest: toString()");
 mock.toString();

 ongoingStubbing = MockitoCore.newInstance().stub();
 lastInvocation = ((OngoingStubbingImpl) ongoingStubbing).getLastInvocation();

 assertEquals("MockitoCoreTest: toString()", lastInvocation.getMethod()[0].getName());
 }

 @Test
 public void testValidateMockitoUsage() {
 MockitoCore.newInstance().validateMockitoUsage();
 }
}