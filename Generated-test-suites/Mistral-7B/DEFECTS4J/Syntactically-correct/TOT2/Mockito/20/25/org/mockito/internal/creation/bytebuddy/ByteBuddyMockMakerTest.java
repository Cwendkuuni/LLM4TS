package org.junit.test;

import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.plugins.MockMaker;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testByteBuddyMockMakerConstructor() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        // Check if the constructor is executed successfully
        assertNotNull(byteBuddyMockMaker);
    }

    @Test
    public void testCreateMock() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>()
                .typeToMock(String.class)
                .serializableMode(SerializableMode.SERIAL)
                .build();
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        String mock = byteBuddyMockMaker.createMock(settings, handler);
        assertNotNull(mock);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializableModeAcrossClassloaders() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>()
                .typeToMock(String.class)
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testGetHandler() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings.Builder().build(), handler);
        MockHandler returnedHandler = byteBuddyMockMaker.getHandler(mock);
        assertEquals(handler, returnedHandler);
    }

    @Test
    public void testResetMock() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings.Builder().build(), handler);
        byteBuddyMockMaker.resetMock(mock, handler, new MockCreationSettings.Builder().build());
        MockHandler resetHandler = byteBuddyMockMaker.getHandler(mock);
        assertEquals(handler, resetHandler);
    }

    // Edge cases and exception handling test cases
    @Test(expected = NullPointerException.class)
    public void testByteBuddyMockMakerConstructorNullMockHandler() {
        new ByteBuddyMockMaker(null);
    }

    @Test(expected = NullPointerException.class)
    public void testByteBuddyMockMakerConstructorNullMockCreationSettings() {
        new ByteBuddyMockMaker(new MockCreationSettings.Builder().build(), null);
    }

    @Test(expected = InstantiationException.class)
    public void testByteBuddyMockMakerInstantiationExceptionForString() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>()
                .typeToMock(String.class)
                .build();
        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("InstantiationException expected");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test(expected = InstantiationException.class)
    public void testByteBuddyMockMakerInstantiationExceptionForComplexClass() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<ComplexClass> settings = new MockCreationSettings.Builder<ComplexClass>()
                .typeToMock(ComplexClass.class)
                .build();
        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("InstantiationException expected");
        } catch (InstantiationException e) {
            // Expected
        }
    }
}