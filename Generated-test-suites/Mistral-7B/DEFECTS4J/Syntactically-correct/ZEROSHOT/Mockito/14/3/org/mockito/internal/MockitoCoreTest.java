package org.mockito.internal.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoCore;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock
 private MockingProgress mockingProgress;
 @Mock
 private Reporter reporter;
 @Mock
 private MockUtil mockUtil;
 @Mock
 private Object mock;
 @Mock
 private InOrderContext inOrderContext;
 @Mock
 private VerificationModeFactory verificationModeFactory;
 @Mock
 private VerificationMode verificationMode;
 @Mock
 private List<Invocation> invocations;
 @Mock
 private Object[] mocks;

 @BeforeEach
 public void setUp() {
 mockitoCore = new MockitoCore() {
 @Override
 Reporter getReporter() {
 return reporter;
 }

 @Override
 MockUtil getMockUtil() {
 return mockUtil;
 }

 @Override
 MockingProgress getMockingProgress() {
 return mockingProgress;
 }
 };
 }

 @Test
 public void testMockCreation() {
 Class<?> clazz = String.class;
 MockSettings mockSettings = new MockSettingsImpl();
 T mock = mockitoCore.mock(clazz, mockSettings);
 Assertions.assertNotNull(mock);
 }

 @Test
 public void testStubbingStarted() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 Assertions.assertNotNull(stubbing);
 }

 @Test
 public void testStubbingWithMethodCall() {
 DeprecatedOngoingStubbing<Object> stubbing = mockitoCore.stub(mock);
 Assertions.assertNotNull(stubbing);
 }

 @Test
 public void testOngoingStubbing() {
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
 Assertions.assertNotNull(ongoingStubbing);
 }

 @Test
 public void testVerificationMode() {
 VerificationData verificationData = new VerificationDataImpl(invocations, verificationMode);
 VerificationModeFactory verificationModeFactorySpy = Mockito.spy(verificationModeFactory);
 VerificationMode verificationModeSpy = verificationModeFactorySpy.verifyMode(verificationData);
 Assertions.assertEquals(verificationMode, verificationModeSpy);
 }

 @Test
 public void testVerifyNullMock() {
 Assertions.assertThrows(MockitoException.class, () -> mockitoCore.verify(null, verificationMode));
 }

 @Test
 public void testVerifyNotAMock() {
 Assertions.assertThrows(MockitoException.class, () -> mockitoCore.verify(new Object(), verificationMode));
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock);
 MockHandlerInterface<Object> handler = mockUtil.getMockHandler(mock);
 handler.reset();
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 List<Object> mocksList = Arrays.asList(mock, mock);
 VerificationDataInOrder verificationData = new VerificationDataInOrderImpl(inOrderContext, invocations, verificationMode);
 mockitoCore.verifyNoMoreInteractionsInOrder(mocksList, inOrderContext);
 }

 @Test
 public void testGetLastInvocation() {
 IOngoingStubbing ongoingStubbing = mockitoCore.stub();
 List<Invocation> allInvocations = ongoingStubbing.getRegisteredInvocations();
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 Assertions.assertEquals(lastInvocation, allInvocations.get(allInvocations.size() - 1));
 }
}