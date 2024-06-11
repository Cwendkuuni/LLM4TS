import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {
    @Test
    public void testMock() {
        MockSettings mockSettings = new MockSettings();
        Class<MockitoCore> clazzToMock = MockitoCore.class;
        MockitoCore mock = new MockitoCore().mock(clazzToMock, mockSettings);
        assertNotNull(mock);
    }

    // Tests for edge cases and exceptions

    // Test for edge case: Null mock settings
    @Test(expected = IllegalArgumentException.class)
    public void testMock_NullMockSettings() {
        Class<MockitoCore> clazzToMock = MockitoCore.class;
        new MockitoCore().mock(clazzToMock, null);
    }

    // Test for edge case: Null ClassToMock
    @Test(expected = IllegalArgumentException.class)
    public void testMock_NullClassToMock() {
        new MockitoCore().mock(null, new MockSettings());
    }

    // Test for exception: NotAMockException
    @Test(expected = NotAMockException.class)
    public void testVerify_NonMockObject() {
        Object nonMockObject = new Object();
        new MockitoCore().verify(nonMockObject, eq(isNull()));
    }

    // Test for exception: NotAMockException
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_NullMocks() {
        new MockitoCore().verifyNoMoreInteractions(null);
    }

    // Test for exception: NotAMockException
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_NullMocksInOrder() {
        List<Object> mocks = null;
        InOrder inOrder = mock(MockitoCore.class);
        new MockitoCore().verifyNoMoreInteractionsInOrder(mocks, inOrder);
    }

    // Test for exception: NotAMockException
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsInOrder_NullMocksInOrderAndNonMockObject() {
        List<Object> mocks = null;
        Object nonMockObject = new Object();
        InOrder inOrder = mock(MockitoCore.class);
        new MockitoCore().verifyNoMoreInteractionsInOrder(mocks, inOrder);
        inOrder.verify(nonMockObject).someMethod();
    }

    // Test for edge case: Null VerificationMode in verify
    @Test(expected = IllegalArgumentException.class)
    public void testVerify_NullVerificationMode() {
        Object mock = mock(MockitoCore.class);
        new MockitoCore().verify(mock, null);
    }

    // Test for edge case: Null VerificationMode in verifyNoMoreInteractions
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNoMoreInteractions_NullVerificationMode() {
        Object mock = mock(MockitoCore.class);
        new MockitoCore().verifyNoMoreInteractions(mock, null);
    }

    // Test for exception: NotAMockException
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_NonMockObject() {
        Object nonMockObject = new Object();
        new MockitoCore().verifyNoMoreInteractions(nonMockObject);
    }

    // Test for exception: NotAMockException
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsInOrder_NonMockObject() {
        Object nonMockObject = new Object();
        InOrder inOrder = mock(MockitoCore.class);
        new MockitoCore().verifyNoMoreInteractionsInOrder(Arrays.asList(nonMockObject), inOrder);
    }

    // Test for edge case: Empty list in verifyNoMoreInteractionsInOrder
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNoMoreInteractionsInOrder_EmptyList() {
        InOrder inOrder = mock(MockitoCore.class);
        new MockitoCore().verifyNoMoreInteractionsInOrder(Arrays.asList(), inOrder);
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(MockitoCore.class);
        Object mock2 = mock(MockitoCore.class);
        InOrder inOrder = inOrder(mock1, mock2);
        inOrder.verify(mock1).mockMethod();
        inOrder.verify(mock1).mockMethod();
        inOrder.verify(mock2).mockMethod();
        inOrder.verify(mock1).mockMethod();
    }

    @Test
    public void testStubbing() {
        Object mock = mock(MockitoCore.class);
        Stubber stubber = new MockitoCore().stub(mock);
        stubber.thenReturn("return value");
        assertEquals("return value", mock.someMethod());
    }

    @Test
    public void testWhen() {
        Object mock = mock(MockitoCore.class);
        when(mock.someMethod()).thenReturn("return value");
        assertEquals("return value", mock.someMethod());
    }

    @Test
    public void testVerify() {
        Object mock = mock(MockitoCore.class);
        verify(mock).someMethod();
    }

    @Test
    public void testReset() {
        Object mock = mock(MockitoCore.class);
        new MockitoCore().reset(mock);
        verify(mock).someMethod();
        reset(mock);
        verify(mock, never()).someMethod();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(MockitoCore.class);
        new MockitoCore().verifyNoMoreInteractions(mock);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        Object mock1 = mock(MockitoCore.class);
        Object mock2 = mock(MockitoCore.class);
        InOrder inOrder = inOrder(mock1, mock2);
        new MockitoCore().verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
    }

    @Test
    public void testDoAnswer() {
        Object mock = mock(MockitoCore.class);
        Answer answer = invocation -> { return 1; };
        Stubber stubber = new MockitoCore().doAnswer(answer);
        assertEquals(1, mock.someMethod());
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(MockitoCore.class);
        Stubber stubber = new MockitoCore().stubVoid(mock);
        stubber.then();
        mock.someVoidMethod();
        verify(mock).someVoidMethod();
    }
}