import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod() throws Throwable {
        // Given
        Object mock = Mockito.mock(String.class, new ReturnsDeepStubs());

        // When
        Object result = ((ReturnsDeepStubs) Mockito.answer(mock)).answer(ArgumentMatchers.any(InvocationOnMock.class));

        // Then
        Mockito.verify(mock).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.assertEquals(mock, result);
    }

    @Test
    public void testGetMockMethod() throws Throwable {
        // Given
        Object mock = Mockito.mock(String.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new InvocationContainerImpl();
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        handler.setInvocationContainer(container);

        // When
        Object result = ((ReturnsDeepStubs) handler.getAnswer()).getMock(ArgumentMatchers.any(InvocationOnMock.class), ArgumentMatchers.any(GenericMetadataSupport.class));

        // Then
        Mockito.verify(mock).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.assertEquals(mock, result);
    }

    @Test(expected = Throwable.class)
    public void testAnswerMethodWithNonMockableType() throws Throwable {
        // Given
        Object mock = Mockito.mock(String[].class, new ReturnsDeepStubs());

        // When
        Object result = ((ReturnsDeepStubs) Mockito.answer(mock)).answer(ArgumentMatchers.any(InvocationOnMock.class));

        // Then - exception should be thrown
        Mockito.verify(mock).answer(ArgumentMatchers.any(InvocationOnMock.class));
        throw new AssertionError("Answer method should not return a deep stub for non-mockable types.");
    }

    @Test
    public void testGetMockMethodWithNonMatchingInvocation() throws Throwable {
        // Given
        Object mock = Mockito.mock(String.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new InvocationContainerImpl();
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        handler.setInvocationContainer(container);

        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
        container.addStubbedInvocation(stubbedInvocationMatcher);

        // When
        Object result = ((ReturnsDeepStubs) handler.getAnswer()).getMock(ArgumentMatchers.any(InvocationOnMock.class), ArgumentMatchers.any(GenericMetadataSupport.class));

        // Then
        Mockito.verify(mock, never()).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.assertEquals(null, result);
    }

    @Test
    public void testGetMockMethodWithMatchingInvocation() throws Throwable {
        // Given
        Object mock = Mockito.mock(String.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new InvocationContainerImpl();
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        handler.setInvocationContainer(container);

        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
        container.addStubbedInvocation(stubbedInvocationMatcher);

        // When
        Object result = ((ReturnsDeepStubs) handler.getAnswer()).getMock(ArgumentMatchers.any(InvocationOnMock.class), ArgumentMatchers.any(GenericMetadataSupport.class));

        // Then
        Mockito.verify(mock).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.assertEquals(mock, result);
    }
}