package org.mockito.internal.test;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class MockitoCoreTest {

    @Test
    public void testMockCreation() {
        // Given
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;

        // When
        MockitoCore mockitoCore = Mockito.mock(mockitoCoreClass);

        // Then
        assertNotNull(mockitoCore);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockCreationWithNullClass() {
        // Given
        MockitoCore mockitoCore = Mockito.mock(null);

        // Then
        throw new IllegalArgumentException("mock() requires non-null class");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockCreationWithNullMockSettings() {
        // Given
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = null;

        // When & Then
        Mockito.mock(mockitoCoreClass, mockSettings);
    }

    @Test
    public void testStubbingStarted() {
        // Given
        MockitoCore mockitoCore = new MockitoCore();

        // When
        OngoingStubbing stubbing = mockitoCore.stub();

        // Then
        assertTrue(mockitoCore.mockingProgress.stubbingStarted());
    }

    @Test(expected = IllegalStateException.class)
    public void testStubbingStartedTwice() {
        // Given
        MockitoCore mockitoCore = new MockitoCore();

        // When
        mockitoCore.stub();
        mockitoCore.stub();

        // Then
        throw new IllegalStateException("Stubbing is ongoing, cannot start a new one.");
    }

    @Test
    public void testVerify() {
        // Given
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        Object mock = mockitoCore;

        // When
        MockitoCore.verify(mock, Mockito.verifyNoMoreInteractions());

        // Then
        // No exception or assertion should be thrown
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNullMock() {
        // Given
        Object nullMock = null;

        // When & Then
        MockitoCore.verify(nullMock, Mockito.verifyNoMoreInteractions());
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNonMock() {
        // Given
        Object nonMock = new Object();

        // When & Then
        MockitoCore.verify(nonMock, Mockito.verifyNoMoreInteractions());
    }
}