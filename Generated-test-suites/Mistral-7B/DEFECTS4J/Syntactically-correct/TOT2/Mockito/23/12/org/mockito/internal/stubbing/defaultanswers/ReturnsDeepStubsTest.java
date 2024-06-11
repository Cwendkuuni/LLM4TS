import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mock.answer(invocation)).thenReturn("test");

        String returnedValue = (String) mock.answer(invocation);
        verify(mock).answer(invocation);

        assertEquals("test", returnedValue);
    }

    @Test
    public void testAnswerMethodWithStubbedInvocation() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mock.answer(invocation)).thenReturn("test");
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        Object deepStubMock = mock.getMock(invocation, Mockito.type(InvocationOnMock.class));
        verify(deepStubMock).answer(invocation);
        assertEquals("test", deepStubMock.answer(invocation));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAnswerMethodWithUnsupportedType() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object[].class.getMethod("toString", (Class[]) null));

        mock.answer(invocation);
    }

    @Test
    public void testGetMockMethodWithStubbedInvocation() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mock.getMock(invocation, Mockito.type(InvocationOnMock.class))).thenReturn("test");
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        Object deepStubMock = mock.getMock(invocation, returnTypeGenericMetadata);
        verify(deepStubMock).answer(invocation);
        assertEquals("test", deepStubMock.answer(invocation));
    }

    @Test
    public void testCreateNewDeepStubMockMethod() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mock.answer(invocation)).thenReturn("test");
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        Object deepStubMock = mock.createNewDeepStubMock(returnTypeGenericMetadata);
        verify(deepStubMock).answer(invocation);
        assertEquals("test", deepStubMock.answer(invocation));
    }

    @Test
    public void testCreateDeepStubMockWithUnsupportedType() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object[].class.getMethod("toString", (Class[]) null));

        mock.createNewDeepStubMock(returnTypeGenericMetadata);
    }

    // ... add more test cases for other scenarios

    private GenericMetadataSupport returnTypeGenericMetadata;

    @Before
    public void setup() {
        returnTypeGenericMetadata = Mockito.type(InvocationOnMock.class);
    }
}