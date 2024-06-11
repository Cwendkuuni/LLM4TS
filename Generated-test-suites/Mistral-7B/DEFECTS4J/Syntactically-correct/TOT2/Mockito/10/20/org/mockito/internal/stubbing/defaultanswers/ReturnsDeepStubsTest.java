import org.mockito.Answers;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.internal.util.reflection.MockitoCore;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.AnswerWithMockityAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;
import java.lang.reflect.GenericDeclaration;
import java.util.Arrays;

public class ReturnsDeepStubsTest {

    private MockitoCore mockitoCore;
    private ReturnsDeepStubs returnsDeepStubs;
    private InvocationOnMock invocation;
    private GenericMetadataSupport returnTypeGenericMetadata;

    @BeforeClass
    public void setup() {
        mockitoCore = new MockitoCore();
        returnsDeepStubs = new ReturnsDeepStubs();
        invocation = Mockito.mock(InvocationOnMock.class);
        returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
    }

    @Test
    public void testAnswer() throws Throwable {
        // Given
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);

        // When
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object answer = returnsDeepStubs.answer(invocation);

        // Then
        Mockito.verify(mockitoCore()).isTypeMockable(String.class);
        assertEquals(mock, answer);
    }

    @Test
    public void testDeepStub() throws Throwable {
        // Given
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(mockitoCore().isTypeMockable(String.class)).thenReturn(true);

        // When
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object deepStub = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        // Then
        Mockito.verify(mockitoCore()).isTypeMockable(String.class);
        assertEquals(mock, deepStub);
    }

    @Test
    public void testAnswerUnmockableType() throws Throwable {
        // Given
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(int[].class);

        // When
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object answer = returnsDeepStubs.answer(invocation);

        // Then
        Mockito.verify(mockitoCore()).isTypeMockable(int[].class);
        assertNull(answer);
    }

    @Test
    public void testDeepStubMultipleMocks() throws Throwable {
        // Given
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(mockitoCore().isTypeMockable(String.class)).thenReturn(true);

        // When
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object deepStub1 = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
        Object deepStub2 = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        // Then
        Mockito.verify(mockitoCore(), Mockito.atLeast(2)).isTypeMockable(String.class);
        assertEquals(mock1, deepStub1);
        assertEquals(mock2, deepStub2);
    }

    @Test(expectedExceptions = IOException.class)
    public void testDeepStubIOException() throws Throwable {
        // Given
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(mockitoCore().isTypeMockable(String.class)).thenThrow(new IOException());

        // When
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object deepStub = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        // Then
        Mockito.verify(mockitoCore()).isTypeMockable(String.class);
        fail("IOException should have been thrown");
    }

    @Test
    public void testDeepStubCreationFailure() throws Throwable {
        // Given
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(mockitoCore().isTypeMockable(String.class)).thenReturn(false);

        // When
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object deepStub = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        // Then
        Mockito.verify(mockitoCore()).isTypeMockable(String.class);
        assertNull(deepStub);
    }

    @Test
    public void testDeepStubException() throws Throwable {
        // Given
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(mockitoCore().isTypeMockable(String.class)).thenThrow(new RuntimeException());

        // When
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object deepStub = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        // Then
        Mockito.verify(mockitoCore()).isTypeMockable(String.class);
        assertNull(deepStub);
    }
}