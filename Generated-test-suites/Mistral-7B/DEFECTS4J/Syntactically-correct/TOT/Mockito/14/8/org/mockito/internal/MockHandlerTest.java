import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerConstants;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoTestMock> mockHandler;
 private MockitoTestMock mockitoTestMock;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 mockitoTestMock = mock(MockitoTestMock.class);
 }

 @Test
 public void testHandle_NoAnswersForStubbing_CallRealMethod() {
 // given
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 .thenCallRealMethod();

 // when
 Object result = mockHandler.handle(new InvocationOnMock(Mockito.mock(ClassWithMethodToTest.class), Arrays.asList("testMethod"), new Object[0]));

 // then
 // The real method is called and the result is returned
 verify(mockitoTestMock).testMethod("testArgument");
 assertEquals("expectedResult", result);
 }

 @Test
 public void testHandle_AnswersForStubbing_VoidMethod() {
 // given
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 .thenAnswer(AnswerConstants.VOID);

 // when
 mockHandler.handle(new InvocationOnMock(mockitoTestMock, Arrays.asList("voidMethod"), new Object[0]));

 // then
 // The void method is stubbed and no result is returned
 verify(mockitoTestMock).voidMethod();
 }

 @Test
 public void testHandle_AnswersForStubbing_Answer() {
 // given
 Answer<Object> answer = mock(Answer.class);
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 .thenAnswer(answer);

 // when
 Object result = mockHandler.handle(new InvocationOnMock(mockitoTestMock, Arrays.asList("answerMethod"), new Object[0]));

 // then
 // The answer is returned
 verifyZeroInteractions(mockitoTestMock);
 assertEquals("expectedResult", result);
 verify(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
 }

 @Test(expected = RuntimeException.class)
 public void testHandle_AnswersForStubbing_Error() {
 // given
 Answer<Object> answer = mock(Answer.class);
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 .thenAnswer(answer);
 when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenThrow(new RuntimeException());

 // when
 // The error is thrown
 mockHandler.handle(new InvocationOnMock(mockitoTestMock, Arrays.asList("answerMethodWithError"), new Object[0]));

 // then
 // The error is propagated
 }

 @Test
 public void testHandle_VerificationModeIsNotNull() {
 // given
 Answer<Object> answer = mock(Answer.class);
 when(mockHandler.handle(ArgumentMatchers.any(InvocationOnMock.class)))
 .thenAnswer(answer);

 VerificationMode verificationMode = Mockito.verify(mockitoTestMock).methodToVerify(eq("testArgument"));

 // when
 mockHandler.handle(new InvocationOnMock(mockitoTestMock, Arrays.asList("methodToVerify"), new Object[0]));

 // then
 // The verification is done and the result is null
 verify(mockitoTestMock).methodToVerify(eq("testArgument"));
 }
}

class MockitoTestMock {
 // mock class under test
 // replace with the real class under test
 interface MethodsToMock {
 void voidMethod();
 String answerMethod(String argument);
 String methodToVerify(String argument);
 }

 String testMethod(String testArgument) {
 return "expectedResult";
 }
}

class ClassWithMethodToTest {
 // replace with the real class under test
 interface MethodsToMock {
 void voidMethod();
 String methodToVerify(String argument);
 }

 String testMethod(String testArgument) {
 return "expectedResult";
 }
}