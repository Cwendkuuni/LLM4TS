
package org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubsTest;

import org.junit.jupiter.api.*;
import org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @BeforeAll
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswerMethod() {
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = returnsDeepStubs.answer(invocation);

        assertEquals(mock, result);
    }

    @Test
    public void testGetMockMethod() {
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = returnsDeepStubs.getMock(invocation);

        assertEquals(mock, result);
    }

    @Test
    public void testGetMockMethodWithNullInvocation() {
        Assertions.assertThrows(NullPointerException.class, () -> returnsDeepStubs.getMock(null));
    }

    @Test
    public void testGetMockMethodWithNullMock() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Assertions.assertThrows(NullPointerException.class, () -> returnsDeepStubs.getMock(null));
    }

    @Test
    public void testGetMockMethodWithUnmockableType() {
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Class<?> unmockableType = int[].class;

        Assertions.assertThrows(IllegalArgumentException.class, () -> returnsDeepStubs.getMock(invocation), "Type " + unmockableType + " is not mockable.");
    }

    @Test
    public void testRecordDeepStubMockMethod() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        assertEquals(mock, result);
    }

    @Test
    public void testRecordDeepStubMockMethodWithNullInvocation() {
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Assertions.assertThrows(NullPointerException.class, () -> returnsDeepStubs.recordDeepStubMock(null, container));
    }

    @Test
    public void testRecordDeepStubMockMethodWithNullContainer() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Assertions.assertThrows(NullPointerException.class, () -> returnsDeepStubs.recordDeepStubMock(invocation, null));
    }
}