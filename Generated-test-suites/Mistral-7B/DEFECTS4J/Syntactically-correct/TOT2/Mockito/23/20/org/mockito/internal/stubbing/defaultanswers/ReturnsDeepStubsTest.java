package org.mockito.internal.stubbing.defaultanswers.test;

import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.*;
import org.mockito.invocation.*;
import org.mockito.stubbing.*;
import org.mockito.internal.stubbing.defaultanswers.*;

public class ReturnsDeepStubsTest {
    private Object deepStub;
    private Answer<Object> answer = new ReturnsDeepStubs();

    @Before
    public void setup() {
        deepStub = Mockito.mock(String.class);
    }

    // Test Method 1: answer(InvocationOnMock invocation)
    @Test
    public void testAnswerMethod() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(deepStub);

        Object result = answer.answer(invocation);

        assertEquals(deepStub, result);
    }

    // Test Method 2: getMock(InvocationOnMock invocation, GenericMetadataSupport returnTypeGenericMetadata)
    @Test
    public void testGetMockMethod() {
        Object deepStub = Mockito.mock(String.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        when(container.getInvocationForStubbing()).thenReturn(Mockito.mock(InvocationOnMock.class));

        Object result = answer.getMock(Mockito.mock(InvocationOnMock.class), Mockito.mock(GenericMetadataSupport.class));

        assertEquals(deepStub, result);
    }

    // Test Method 3: createNewDeepStubMock(GenericMetadataSupport returnTypeGenericMetadata)
    @Test
    public void testCreateNewDeepStubMockMethod() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Object mock = Mockito.mock(String.class);

        when(answer.createNewDeepStubMock(returnTypeGenericMetadata)).thenReturn(mock);

        Object result = answer.answer(Mockito.mock(InvocationOnMock.class));

        assertEquals(mock, result);
    }

    // Test Method 4: recordDeepStubMock(Object mock, InvocationContainerImpl container)
    @Test(expected = Throwable.class)
    public void testRecordDeepStubMockMethod_throwsException() {
        Object deepStub = Mockito.mock(String.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        when(answer.recordDeepStubMock(deepStub, container)).thenThrow(new RuntimeException());

        answer.answer(Mockito.mock(InvocationOnMock.class));
    }

    @Test
    public void testRecordDeepStubMockMethod() {
        Object deepStub = Mockito.mock(String.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        answer.recordDeepStubMock(deepStub, container);

        verify(container).addAnswer(Mockito.any(Answer.class), Mockito.eq(false));
    }

    // Test Method 5: actualParameterizedType(Object mock)
    @Test
    public void testActualParameterizedTypeMethod() {
        Object mock = Mockito.mock(String.class);

        GenericMetadataSupport result = answer.actualParameterizedType(mock);

        assertEquals(Mockito.mock(GenericMetadataSupport.class), result);
    }

}