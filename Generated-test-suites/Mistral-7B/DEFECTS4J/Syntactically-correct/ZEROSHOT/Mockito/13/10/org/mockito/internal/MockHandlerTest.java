import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.verification.VerificationMode;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoTestObject> mockHandler;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;
 private Invocation invocation;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 invocation = Mockito.mock(Invocation.class);
 }

 @Test
 public void testHandle_InvocationWithAnswer_ReturnsNull() {
 // Given
 when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "Answer from default answer";
 }
 });
 when(mockSettings.getDefaultAnswer()).thenReturn(mockHandler.getMockSettings().getDefaultAnswer());

 stubInvocationWithAnswer(invocation, "Return value from answer");

 // When
 Object result = mockHandler.handle(invocation);

 // Then
 assertNull(result);
 }

 @Test
 public void testHandle_InvocationWithoutAnswer_ReturnsDefaultAnswer() {
 // Given
 when(mockSettings.getDefaultAnswer()).thenReturn(new Answer() {
 @Override
 public Object answer(Invocation invocation) throws Throwable {
 return "Answer from default answer";
 }
 });

 // When
 Object result = mockHandler.handle(invocation);

 // Then
 assertEquals("Answer from default answer", result);
 }

 @Test
 public void testHandle_InvocationWithStubbing_ReturnsNull() {
 // Given
 StubbedInvocationMatcher invocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
 when(invocationMatcher.answer(invocation)).thenReturn("Answer from stubbing");
 invocationContainer.setAnswersForStubbing(Mockito.singletonList(invocationMatcher));

 // When
 Object result = mockHandler.handle(invocation);

 // Then
 assertNull(result);
 }

 @Test
 public void testHandle_InvocationWithVerificationMode_DoesNotCallHandleMethod() {
 // Given
 VerificationMode verificationMode = Mockito.mock(VerificationMode.class);
 when(verificationMode.verify(ArgumentMatchers.any(VerificationDataImpl.class))).thenReturn(null);
 mockingProgress.pushVerificationMode(verificationMode);

 // When
 Object result = mockHandler.handle(invocation);

 // Then
 verifyZeroInteractions(invocationHandler);
 }

 @Test
 public void testHandle_InvocationWithVerificationModeOnDifferentMock_CallsHandleMethod() {
 // Given
 MockitoTestObject mockObject = Mockito.mock(MockitoTestObject.class);
 MockHandler<MockitoTestObject> mockHandlerForMockObject = new MockHandler<>(mockSettings);
 VerificationMode verificationMode = Mockito.mock(VerificationMode.class);
 when(verificationMode.verify(ArgumentMatchers.any(VerificationDataImpl.class))).thenReturn(null);
 mockingProgress.pushVerificationMode(verificationMode);

 Invocation invocationForMockObject = Mockito.mock(Invocation.class);
 when(verificationMode.getMock()).thenReturn(mockObject);

 // When
 Object result = mockHandlerForMockObject.handle(invocationForMockObject);

 // Then
 verifyZeroInteractions(mockHandler);
 }

 private void stubInvocationWithAnswer(Invocation invocation, String answer) {
 when(invocationContainer.findAnswerFor(ArgumentMatchers.eq(invocation))).thenReturn(new StubbedInvocationMatcher(answer));
}
}

class MockitoTestObject {}