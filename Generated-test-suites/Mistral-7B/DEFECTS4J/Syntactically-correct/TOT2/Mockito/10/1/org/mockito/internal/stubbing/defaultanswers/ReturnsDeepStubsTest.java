import org.junit.Test;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.invocation.InvocationContainerImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.spy;
import static org.junit.Assert.assertEquals;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object result = new ReturnsDeepStubs().answer(invocation);

        verify(mock).answer(invocation);
        assertEquals(mock, result);
    }

    @Test
    public void testAnswer_NonMockableReturnType() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("toString", new Class[0]));

        try {
            new ReturnsDeepStubs().answer(invocation);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        verify(mock).delegate().returnValueFor(String.class);
    }

    @Test
    public void testDeepStubAlreadyExists() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object deepStub = mock(Object.class);

        Mockito.doReturn(deepStub).when(mock).deepStub(any(InvocationOnMock.class), any(GenericMetadataSupport.class));

        Object result = new ReturnsDeepStubs().answer(invocation);

        assertEquals(deepStub, result);
    }

    @Test(expected = Throwable.class)
    public void testRecordDeepStubAnswerThrowsException() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenThrow(new RuntimeException());

        Object result = new ReturnsDeepStubs().recordDeepStubAnswer(mock, container);

        fail("Expected RuntimeException");
    }

    @Test
    public void testDeepStub() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object deepStub = new ReturnsDeepStubs().deepStub(invocation, Mockito.mock(GenericMetadataSupport.class));

        assertEquals(mock, deepStub);
    }

    @Test
    public void testNewDeepStubMock() throws Throwable {
        Class<Object> type = Object.class;
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        Object deepStubMock = new ReturnsDeepStubs().newDeepStubMock(returnTypeGenericMetadata);

        assertEquals(type, deepStubMock.getClass());
        assertEquals(returnTypeGenericMetadata, Mockito.mock(GenericMetadataSupport.class).equals(deepStubMock.getMetadata()));
    }

    @Test
    public void testWithSettingsUsing() throws Throwable {
        Class<Object> type = Object.class;
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        MockSettings mockSettings = new ReturnsDeepStubs().withSettingsUsing(returnTypeGenericMetadata);

        assertEquals(type, mockSettings.getTypeToMock());
        assertEquals(returnTypeGenericMetadata, mockSettings.getReturnTypeGenericMetadata());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() throws Throwable {
        Class<Object> type = Object.class;
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs().returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        assertEquals(type, returnsDeepStubs.actualParameterizedType(null).getTypeToMock());
        assertEquals(returnTypeGenericMetadata, returnsDeepStubs.actualParameterizedType(null).getMetadata());
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        Object deepStub = new ReturnsDeepStubs().recordDeepStubAnswer(mock, container);

        assertEquals(mock, deepStub);
    }

    @Test
    public void testActualParameterizedType() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        GenericMetadataSupport genericMetadataSupport = Mockito.mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.getTypeToMock()).thenReturn(Integer.class);

        GenericMetadataSupport actualParameterizedType = new ReturnsDeepStubs().actualParameterizedType(mock);

        assertEquals(Integer.class, actualParameterizedType.getTypeToMock());
    }
}