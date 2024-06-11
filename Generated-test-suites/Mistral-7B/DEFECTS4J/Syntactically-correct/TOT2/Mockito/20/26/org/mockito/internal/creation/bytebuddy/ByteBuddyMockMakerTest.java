package org.mockito.internal.creation.bytebuddy;

import org.junit.Test;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;

public class ByteBuddyMockMakerTest {
    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().build();
        Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
        assertNotNull(mock);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializationAcrossClassLoaders() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMock_ClassCastException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<String> settings = new MockCreationSettings.Builder()
                .typeToMock(String.class)
                .build();
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMock_InstantiationException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .typeToMock(Object.class)
                .build();
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test
    public void testGetHandler_NullMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        assertNull(byteBuddyMockMaker.getHandler(null));
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().build();
        Object mock = MockitoAnnotations.mock(Object.class);
        byteBuddyMockMaker.createMock(settings, mockHandler);
        byteBuddyMockMaker.resetMock(mock, mockHandler, settings);
        MockHandler newHandler = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);
        assertEquals(newHandler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test(expected = NullPointerException.class)
    public void testResetMock_NullHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = MockitoAnnotations.mock(Object.class);
        byteBuddyMockMaker.createMock(new MockCreationSettings.Builder().build(), Mockito.mock(MockHandler.class));
        byteBuddyMockMaker.resetMock(mock, null, new MockCreationSettings.Builder().build());
    }
}