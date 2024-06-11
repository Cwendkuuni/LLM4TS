package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.ExactInvocationMatcher;
import org.mockito.InvocationOnMockException;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandlerInvocation;
import org.mockito.plugins.MockMaker;
import org.mockito.MockitoException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private MockMaker mockMaker;
    private GlobalConfiguration globalConfiguration;

    @Captor
    ArgumentCaptor<MockCreationSettings> mockCreationSettingsArgumentCaptor;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        mockMaker = mock(MockMaker.class);
        globalConfiguration = new GlobalConfiguration();
        MockitoAnnotations.initMocks(this);
        when(byteBuddyMockMaker.getMockMaker()).thenReturn(mockMaker);
    }

    @Test
    public void testCreateMockSuccess() {
        Class<?> mockableClass = Mockito.mock(String.class);
        MockHandler handler = mock(MockHandler.class);

        ByteBuddyMock creation = byteBuddyMockMaker.createMock(mockCreationSettings(MockCreationSettings.builder().withTypeToMock(mockableClass).build()), handler);

        assertNotNull(creation);
        verify(mockMaker).createMock(mockCreationSettingsArgumentCaptor.capture());
        MockCreationSettings settings = mockCreationSettingsArgumentCaptor.getValue();
        verify(handler).handle(argThat(new ExactInvocationMatcher(settings.getMockCreationCall())));
        assertEquals(mockableClass, settings.getTypeToMock());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializationAcrossClassloaders() {
        ByteBuddyMockMaker byteBuddyMockMaker = spy(ByteBuddyMockMaker.class);
        doReturn(byteBuddyMockMaker).when(byteBuddyMockMaker).newInstance();

        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMakerInstance = byteBuddyMockMaker.newInstance();
        byteBuddyMockMakerInstance.createMock(mockCreationSettings(MockCreationSettings.builder()
                .withTypeToMock(Object.class)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build()), handler);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockClassCastException() {
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = spy(ByteBuddyMockMaker.class);
        doReturn(byteBuddyMockMaker).when(byteBuddyMockMaker).newInstance();

        Class<?> wrongType = Object.class;
        ByteBuddyMockMaker byteBuddyMockMakerInstance = byteBuddyMockMaker.newInstance();
        byteBuddyMockMakerInstance.createMock(mockCreationSettings(MockCreationSettings.builder()
                .withTypeToMock(int.class)
                .build()), handler);
        byteBuddyMockMakerInstance.getHandler((Object) wrongType);
    }

    @Test
    public void testGetHandler() {
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        ByteBuddyMock mock = byteBuddyMockMaker.createMock(mockCreationSettings(MockCreationSettings.builder().build()), handler);
        MockHandler returnedHandler = byteBuddyMockMaker.getHandler(mock);
        assertEquals(handler, returnedHandler);
    }

    @Test
    public void testResetMock() {
        MockHandler handler = mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        ByteBuddyMock mock = byteBuddyMockMaker.createMock(mockCreationSettings(MockCreationSettings.builder().build()), handler);
        byteBuddyMockMaker.resetMock(mock, mockHandler(handler), mockCreationSettings(MockCreationSettings.builder().build()));
        assertNotNull(mock);
        verify(mock).setMockitoInterceptor(argThat(new ExactInvocationMatcher(any(MockMethodInterceptor.class))));
    }

    private MockHandler mockHandler(MockHandler handler) {
        return new MockHandler() {
            @Override
            public void handle(MockHandlerInvocation invocation) {
                handler.handle(invocation);
            }
        };
    }

    private MockCreationSettings mockCreationSettings(MockCreationSettings.Builder builder) {
        return builder.build();
    }
}