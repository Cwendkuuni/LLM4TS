package org.mockito.internal.test;

import org.junit.*;
import org.mockito.*;
import org.mockito.internal.MockitoCore;
import org.mockito.invocation.*;
import java.util.*;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {
    private MockitoCore mockitoCore;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }

    // Method 1 test
    @Test
    public <T> void testMock() {
        Class<T> mockClass = Mockito.mock(String.class);
        MockSettings mockSettings = MockSettings.builder().build();

        T mockedObject = mockitoCore.mock(mockClass, mockSettings, false);

        // checking if the mocked object behaves like a mock
        verify(mockedObject).toString();

        // edge cases and exception handling
        testMockWithNullClassToMock();
        testMockWithNullMockSettings();
        testMockWithNullReporter();
        testMockWithInvalidClassToMock();
    }

    // Method 2 test
    @Test
    public void testStub() {
        Object mock = Mockito.mock(Object.class);

        IOngoingStubbing stubbing = mockitoCore.stub();
        stubbing.thenReturn(1L);

        assertEquals(1L, verify(mock).someMethod());

        // edge cases and exception handling
        testStubWithNullMock();
        testStubbingInProgress();
    }

    // edge case test methods with proper names
    @Test(expected = NullPointerException.class)
    public void testMockWithNullClassToMock() {
        Class<MockitoCore> clazz = MockitoCore.class;
        MockSettings mockSettings = MockSettings.builder().build();
        MockitoCore mockitoCore = new MockitoCore();

        mockitoCore.mock(null, mockSettings, false);
    }

    @Test(expected = NullPointerException.class)
    public void testMockWithNullMockSettings() {
        Class<MockitoCore> clazz = MockitoCore.class;
        Class<String> mockClass = Mockito.mock(String.class);
        MockitoCore mockitoCore = new MockitoCore();

        mockitoCore.mock(mockClass, null, false);
    }

    @Test(expected = NullPointerException.class)
    public void testMockWithNullReporter() {
        Class<MockitoCore> clazz = MockitoCore.class;
        Class<String> mockClass = Mockito.mock(String.class);
        MockSettings mockSettings = MockSettings.builder().build();
        MockitoCore mockitoCore = new MockitoCore();

        mockitoCore.mock(mockClass, mockSettings, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockWithInvalidClassToMock() {
        Class<MockitoCore> clazz = MockitoCore.class;
        Class<Object> invalidMockClass = null; // Non-instantiable class
        MockSettings mockSettings = MockSettings.builder().build();
        MockitoCore mockitoCore = new MockitoCore();

        mockitoCore.mock(invalidMockClass, mockSettings, false);
    }

    @Test(expected = AssertionError.class)
    public void testStubWithNullMock() {
        Object mock = null;
        MockitoCore mockitoCore = new MockitoCore();

        IOngoingStubbing stubbing = mockitoCore.stub();
    }

    @Test(expected = AssertionError.class)
    public void testStubbingInProgress() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        IOngoingStubbing stubbing = mockitoCore.stub();

        stubbing.thenReturn(1L);
        mockitoCore.stub();
    }
}