import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoTestSubject> mockHandler;
 @Mock
 private MockSettingsImpl mockSettings;
 @Mock
 private InvocationContainer invocationContainer;
 @Mock
 private InvocationMatcher invocationMatcher;
 @Mock
 private StubbedInvocationMatcher stubbedInvocation;
 @Mock
 private OngoingStubbingImpl<MockitoTestSubject> ongoingStubbing;
 @InjectMocks
 private MockHandlerInterface<MockitoTestSubject> mockHandlerInterface;

 @Before
 public void setUp() {
 mockHandler = new MockHandler<>(mockHandlerInterface);
 }

 @Test
 public void testHandlePositiveVoidStubbing() {
 given(mockSettings.getDefaultAnswer()).willReturn(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "default answer";
 }
 });
 doAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "custom answer";
 }
 }).when(mockHandler).voidMethodStubbable(Mockito.mock(MockitoTestSubject.class));
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(Mockito.mock(MockitoTestSubject.class));
 when(invocation.getMethod()).thenReturn(Mockito.mock(VoidMethod.class));
 doThrow(new RuntimeException("Test exception")).when(invocation).proceed();

 mockHandler.handle(invocation);

 verifyNoInteractions(invocationContainer);
 }

 @Test
 public void testHandlePositiveAnswerStubbing() {
 given(mockSettings.getDefaultAnswer()).willReturn(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "default answer";
 }
 });
 doAnswer(Answer.returnValues(1)).when(mockHandler).handle(ArgumentMatchers.any(InvocationOnMock.class));
 ArgumentCaptor<InvocationMatcher> invocationMatcherArgumentCaptor = ArgumentCaptor.forClass(InvocationMatcher.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(Mockito.mock(MockitoTestSubject.class));
 when(invocation.getMethod()).thenReturn(Mockito.mock(VoidMethod.class));

 mockHandler.handle(invocation);

 verify(invocationContainer).setAnswersForStubbing(ArgumentMatchers.isA(List.class));
 verify(invocationContainer).setAnswersForStubbing(ArgumentMatchers.argThat(argumentList -> argumentList.size() == 1));
 verify(invocationContainer).setAnswersForStubbing(ArgumentMatchers.argThat(answerList -> answerList.get(0).getAnswer() instanceof Answer && ((Answer) answerList.get(0)).getAnswer().equals(1)));
 }

 @Test(expected = RuntimeException.class)
 public void testHandleNegativeVerificationMode() {
 given(mockSettings.getDefaultAnswer()).willReturn(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "default answer";
 }
 });
 doAnswer(Answer.returnValues("custom answer")).when(mockHandler).handle(ArgumentMatchers.any(InvocationOnMock.class));
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(Mockito.mock(MockitoTestSubject.class));
 when(invocation.getMethod()).thenReturn(Mockito.mock(VoidMethod.class));
 doThrow(new RuntimeException("Test exception")).when(invocation).proceed();

 VerificationMode verificationMode = Mockito.mock(VerificationMode.class);
 mockingProgress.pushVerificationMode(verificationMode);

 mockHandler.handle(invocation);

 verify(invocationContainerImpl).setInvocationForPotentialStubbing(ArgumentMatchers.isA(InvocationMatcher.class));
 verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
 verify(verificationMode).verify(ArgumentMatchers.any());
 }

 @Test
 public void testHandleErrorDuringStubbing() {
 given(mockSettings.getDefaultAnswer()).willReturn(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException("Test exception");
 }
 });
 doAnswer(Answer.returnValues("custom answer")).when(mockHandler).handle(ArgumentMatchers.any(InvocationOnMock.class));
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(Mockito.mock(MockitoTestSubject.class));
 when(invocation.getMethod()).thenReturn(Mockito.mock(VoidMethod.class));
 doThrow(new RuntimeException("Test exception")).when(invocation).proceed();

 try {
 mockHandler.handle(invocation);
 } catch (RuntimeException e) {
 // expected
 }

 verifyNoInteractions(invocationContainer);
 }
}

 class MockitoTestSubject {
 }

 class VoidMethod {
 }