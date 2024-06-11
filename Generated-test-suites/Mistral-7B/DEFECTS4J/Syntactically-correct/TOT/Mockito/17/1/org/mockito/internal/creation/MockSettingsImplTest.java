
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.io.Serializable;
import java.lang.Class;
import java.lang.reflect.Array;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MockSettingsImplTest {

    private MockSettings mockSettings;
    private Class<?> extraInterface1;
    private Class<?> extraInterface2;
    private Class<?> nullClass;
    private Class<?> nonInterfaceClass;

    @Before
    public void setUp() {
        extraInterface1 = Mockito.mock(Serializable.class);
        extraInterface2 = Mockito.mock(java.lang.Runnable.class);
        nullClass = null;
        nonInterfaceClass = new Object() {};
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializableMockSettings() {
        MockSettings serializableSettings = mockSettings.serializable();
        verify(mockSettings).extraInterfaces(java.io.Serializable.class);
        verify(serializableSettings).isSerializable();
        verify(serializableSettings).getExtraInterfaces()[0].equals(java.io.Serializable.class);
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testEmptyExtraInterfaces() {
        mockSettings.extraInterfaces();
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testNullExtraInterface() {
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testNonInterfaceExtraInterface() {
        mockSettings.extraInterfaces(nonInterfaceClass);
    }

    @Test
    public void testGetExtraInterfaces() {
        mockSettings.extraInterfaces(extraInterface1, extraInterface2);
        Class<?>[] extraInterfaces = mockSettings.getExtraInterfaces();
        assert (extraInterfaces.length == 2);
        assert (extraInterfaces[0].equals(extraInterface1));
        assert (extraInterfaces[1].equals(extraInterface2));
    }

    @Test
    public void testName() {
        mockSettings.name("testName");
        assertEquals("testName", mockSettings.getMockName().getName());
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> answer = mock(Answer.class);
        mockSettings.defaultAnswer(answer);
        assertEquals(answer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        mockSettings.serializable();
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class<?> classToMock = Mockito.mock(Object.class);
        mockSettings.initiateMockName(classToMock);
        assertEquals("MockSettingsImpl_" + classToMock.getSimpleName(), mockSettings.getMockName().getName());
    }
}