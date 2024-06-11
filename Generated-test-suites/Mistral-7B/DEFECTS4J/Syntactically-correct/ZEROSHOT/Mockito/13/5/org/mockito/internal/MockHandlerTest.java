import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoTestSubject> mockHandler;
 private MockitoTestSubject mockitoTestSubject;
 private Answer answer;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 mockitoTestSubject = mock(MockitoTestSubject.class);
 answer = Mockito.answer(new Object() {});
 }

 @Test
 public void testHandleVoidInvocation() {
 Invocation invocation = Mockito.mock(Invocation.class);
 when(mockHandler.handle(invocation)).thenReturn(null);

 mockHandler.voidMethodStubbable(mockitoTestSubject).stubVoid();

 InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
 invocationOnMock.getMock().equals(mockitoTestSubject);
 invocationOnMock.getMethod().getName().equals("voidMethod");
 invocationOnMock.getArguments().clear();

 mockHandler.handle(invocationOnMock);

 // Verify that the void method was stubbed
 verify(mockitoTestSubject).voidMethod();
 }

 @Test
 public void testHandleNonVoidInvocationWithAnswer() {
 Invocation invocation = Mockito.mock(Invocation.class);
 when(mockHandler.handle(invocation)).thenReturn(answer.getAnswer());

 List<Answer> answers = new ArrayList<>();
 answers.add(answer);

 mockHandler.setAnswersForStubbing(answers);

 InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
 invocationOnMock.getMock().equals(mockitoTestSubject);
 invocationOnMock.getMethod().getName().equals("nonVoidMethod");
 invocationOnMock.getArguments().add("testArg");

 Object result = mockHandler.handle(invocationOnMock);

 // Verify that the answer was returned
 verify(answer).answeredWith(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleNonVoidInvocationWithoutAnswer() {
 Invocation invocation = Mockito.mock(Invocation.class);
 when(mockHandler.handle(invocation)).thenReturn(answer.getAnswer());

 List<Answer> answers = new ArrayList<>();
 mockHandler.setAnswersForStubbing(answers);

 InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
 invocationOnMock.getMock().equals(mockitoTestSubject);
 invocationOnMock.getMethod().getName().equals("nonVoidMethod");
 invocationOnMock.getArguments().add("testArg");

 mockHandler.handle(invocationOnMock);
 }

 @Test
 public void testHandleNonVoidInvocationWithArgumentMatcher() {
 Invocation invocation = Mockito.mock(Invocation.class);
 when(mockHandler.handle(invocation)).thenReturn(answer.getAnswer());

 ArgumentMatchers.argThat(ArgumentMatchers.eq("testArg"))
 .answers(answer);

 mockHandler.setAnswersForStubbing(List.of(ArgumentMatchers.argThat(ArgumentMatchers.eq("testArg"))));

 InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
 invocationOnMock.getMock().equals(mockitoTestSubject);
 invocationOnMock.getMethod().getName().equals("nonVoidMethod");
 invocationOnMock.getArguments().add("testArg");

 Object result = mockHandler.handle(invocationOnMock);

 // Verify that the answer was returned with the argument matched
 verify(answer).answeredWith(result);
 }
}

class MockitoTestSubject {
 // A dummy class to be mocked
}