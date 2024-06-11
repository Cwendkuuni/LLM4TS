import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.Stub;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.AnswerWithInfo;
import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;
 private MockingProgress mockingProgress;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockingProgress = new ThreadSafeMockingProgress();
 invocationContainer = new InvocationContainerImpl(mockingProgress);
 mockHandler = new MockHandler<>(mockSettings);
 }

 @Test
 public void testHandleVoidMethodInvocation() {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = ArgumentMatchers.any();

 when(mockHandler.handle(invocation)).thenReturn(null);

 OngoingStubbing<MockitoInvocationHandler> ongoingStubbing = mockHandler.voidMethodStubbable(mock);
 ongoingStubbing.doAnswer(AnswerWithInfo.create(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "Mocked response";
 }
 })).when(mock).voidMethod();

 Object result = mockHandler.handle(invocation);

 verify(mock).voidMethod();
 assertNotNull(result);
 assertEquals("Mocked response", result);
 }

 @Test
 public void testHandleNonVoidMethodInvocation() {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = ArgumentMatchers.any();

 when(mockHandler.handle(invocation)).thenReturn(42);

 when(mock.someMethod(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())).thenReturn(10);

 int result = (int) mockHandler.handle(new InvocationOnMock(mock, "someMethod", Arrays.asList(1, "test"), new Object[0]));

 verify(mock).someMethod(ArgumentMatchers.eq(1), ArgumentMatchers.eq("test"));
 assertEquals(10, result);
 }

 @Test
 public void testHandleNonVoidMethodInvocationWithAnswer() {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = ArgumentMatchers.any();

 when(mockHandler.handle(invocation)).thenAnswer(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "Mocked response";
 }
 });

 Object result = mockHandler.handle(invocation);

 assertNotNull(result);
 assertEquals("Mocked response", result);
 }

 @Test
 public void testHandleNonVoidMethodInvocationWithMultipleAnswers() {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = ArgumentMatchers.any();

 List<Answer> answers = Arrays.asList(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "Answer 1";
 }
 }, new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "Answer 2";
 }
 });

 when(mockHandler.handle(invocation)).thenAnswer(Answers.returning(answers));

 Object result1 = mockHandler.handle(invocation);
 Object result2 = mockHandler.handle(invocation);

 assertEquals("Answer 1", result1);
 assertEquals("Answer 2", result2);
 }
}