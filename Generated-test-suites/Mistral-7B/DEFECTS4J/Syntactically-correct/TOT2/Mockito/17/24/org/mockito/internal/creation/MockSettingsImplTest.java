
package org.mockito.internal.creation;

import org.junit.*;
import static org.mockito.*;
import static org.junit.Assert.*;

public class MockSettingsImplTest {
    private MockSettings settings;

    @Before
    public void setUp() {
        settings = Mockito.mock(MockSettings.class);
    }

    @Test
    public void testSerializable() {
        MockSettings serializableSettings = settings.serializable();
        assertEquals(settings, serializableSettings);
    }

    @Test(expected = org.mockito.exceptions.Reporter.ExtraInterfacesRequiresAtLeastOneInterfaceException.class)
    public void testExtraInterfacesNullArray() {
        settings.extraInterfaces((Class<?>[]) null);
    }

    @Test(expected = org.mockito.exceptions.Reporter.ExtraInterfacesRequiresAtLeastOneInterfaceException.class)
    public void testExtraInterfacesEmptyArray() {
        settings.extraInterfaces(new Class[0]);
    }

    @Test(expected = org.mockito.exceptions.Reporter.ExtraInterfacesAcceptsOnlyInterfacesException.class)
    public void testExtraInterfacesNonInterfaceClass() {
        settings.extraInterfaces(String.class);
    }

    @Test
    public void testGetMockName() {
        MockName mockName = mock(MockName.class);
        when(settings.getMockName()).thenReturn(mockName);
        assertEquals(mockName, settings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = new Class[] { String.class, Serializable.class };
        when(settings.getExtraInterfaces()).thenReturn(extraInterfaces);
        assertArrayEquals(new Class[] { String.class, Serializable.class }, settings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        when(settings.getSpiedInstance()).thenReturn(spiedInstance);
        assertEquals(spiedInstance, settings.getSpiedInstance());
    }

    @Test
    public void testName() {
        settings.name("MockSettings test");
        assertEquals("MockSettings test", settings.getName());
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        settings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, settings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = mock(Answer.class);
        settings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, settings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class classToMock = String.class;
        settings.initiateMockName(classToMock);
        MockName mockName = settings.getMockName();
        assertEquals(settings.getClass(), mockName.getClass());
        assertEquals("MockSettings", mockName.getName());
        assertEquals(classToMock, mockName.getMockType());
    }
}
