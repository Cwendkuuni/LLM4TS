import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.InvocationContainerImpl;
import org.mockito.internal.stubbing.AnswerAnnotations;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        // Given
        Answer<Object> answer = Mockito.mock(Answer.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        // When
        Object result = answer.answer(invocation);

        // Then
        Assert.assertEquals(answer, result);
    }

    @Test
    public void testAnswerWithNullInvocation() {
        // Given
        Answer<Object> answer = Mockito.mock(Answer.class);
        InvocationOnMock nullInvocation = null;

        // When
        // Then exception is thrown
        answer.answer(nullInvocation);
    }

    @Test
    public void testDeepStub() throws Throwable {
        // Given
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        // When
        Object result = new ReturnsDeepStubs().deepStub(invocation, returnTypeGenericMetadata);

        // Then
        Assert.assertEquals(mock, result);
    }

    @Test(expected = NullPointerException.class)
    public void testDeepStubWithNullInvocation() throws Throwable {
        // Given
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock nullInvocation = null;
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        // When
        // Then exception is thrown
        new ReturnsDeepStubs().deepStub(nullInvocation, returnTypeGenericMetadata);
    }

    @Test(expected = NullPointerException.class)
    public void testDeepStubWithNullReturnTypeGenericMetadata() throws Throwable {
        // Given
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport nullReturnTypeGenericMetadata = null;

        // When
        // Then exception is thrown
        new ReturnsDeepStubs().deepStub(invocation, nullReturnTypeGenericMetadata);
    }

    @Test
    public void testNewDeepStubMock() {
        // Given
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        // When
        Object mock = new ReturnsDeepStubs().newDeepStubMock(returnTypeGenericMetadata);

        // Then
        Assert.assertNotNull(mock);
    }

    @Test(expected = NullPointerException.class)
    public void testNewDeepStubMockWithNullReturnTypeGenericMetadata() {
        // Given
        GenericMetadataSupport nullReturnTypeGenericMetadata = null;

        // When
        // Then exception is thrown
        new ReturnsDeepStubs().newDeepStubMock(nullReturnTypeGenericMetadata);
    }

    @Test
    public void testWithSettingsUsing() {
        // Given
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        // When
        Mockito.MockSettings mockSettings = new ReturnsDeepStubs().withSettingsUsing(returnTypeGenericMetadata);

        // Then
        Assert.assertNotNull(mockSettings);
    }

    @Test(expected = NullPointerException.class)
    public void testRecordDeepStubAnswerWithNullMock() throws Throwable {
        // Given
        Object nullMock = null;
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        // When
        // Then exception is thrown
        new ReturnsDeepStubs().recordDeepStubAnswer(nullMock, container);
    }

    @Test(expected = NullPointerException.class)
    public void testRecordDeepStubAnswerWithNullContainer() throws Throwable {
        // Given
        Object mock = Mockito.mock(Object.class);
        InvocationContainerImpl nullContainer = null;

        // When
        // Then exception is thrown
        new ReturnsDeepStubs().recordDeepStubAnswer(mock, nullContainer);
    }
}