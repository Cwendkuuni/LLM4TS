import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private InvocationOnMock mock;
 private Answer answer;

 @Before
 public void setUp() {
 mock = mock(MockitoInvocationHandler.class);
 mockHandler = new MockHandler<>(mock.getMockSettings());
 answer = Mockito.answer(new Object());
 }

 @Test
 public void testHandleInvocationWithStubbing() {
 List<Answer> answers = new ArrayList<>();
 answers.add(answer);
 mockHandler.setAnswersForStubbing(answers);

 Invocation invocation = new Invocation(mock, new Object[0], null, new Class[0], null);

 mockHandler.handle(invocation);

 // Verify that the stubbing answer was returned
 verify(mock).handle(ArgumentMatchers.eq(invocation));
 verify(answer).answeredWith(ArgumentMatchers.eq(answer.getCaptor().getValue()));
 }

 @Test
 public void testHandleInvocationWithVerification() {
 VerificationMode verificationMode = Mockito.verify(mock, Mockito.times(1)).handle(ArgumentMatchers.any());

 Invocation invocation = new Invocation(mock, new Object[0], null, new Class[0], null);
 mockHandler.handle(invocation);

 verify(mock).handle(ArgumentMatchers.eq(invocation));
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleInvocationWithVerificationAndStubbing() {
 VerificationMode verificationMode = Mockito.verify(mock, Mockito.times(1)).handle(ArgumentMatchers.any());
 List<Answer> answers = new ArrayList<>();
 answers.add(answer);
 mockHandler.setAnswersForStubbing(answers);

 Invocation invocation = new Invocation(mock, new Object[0], null, new Class[0], null);
 mockHandler.handle(invocation);
 }

 @Test
 public void testHandleInvocationWithVoidMethodStubbable() {
 VoidMethodStubbable<MockitoInvocationHandler> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 voidMethodStubbable.stubVoid();

 Invocation invocation = new Invocation(mock, new Object[0], null, new Class[0], null);
 mockHandler.handle(invocation);

 //Verify that the void method stubbing was called
 verify(mock).handle(ArgumentMatchers.isNull());
 }
}