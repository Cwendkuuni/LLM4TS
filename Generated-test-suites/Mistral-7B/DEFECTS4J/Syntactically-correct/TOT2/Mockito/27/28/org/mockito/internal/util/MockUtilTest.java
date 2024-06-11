import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.exceptions.misusing.NotAMockException;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<MockUtil> mockUtilClass = MockUtil.class;
        MockCreationValidator creationValidator = Mockito.mock(MockCreationValidator.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[]{Serializable.class});
        settings.setSpiedInstance(new Object());

        MockUtil mockUtil = new MockUtil(creationValidator);
        T mock = mockUtil.createMock(Mockito.mock(ClassWithMethods.class).getClass(), settings);

        // Basic functionality test
        assertNotNull(mock);

        // Set up the mock and test the mocked class
        Mockito.when(mock.getMockName()).thenReturn("test_mock");
        String name = mock.getMockName();
        assertEquals("test_mock", name);

        // Test resetting the mock
        mockUtil.resetMock(mock);
        Mockito.verifyZeroInteractions(mock);

        // Test creating a new mock with the same settings
        T mock2 = mockUtil.createMock(Mockito.mock(ClassWithMethods.class).getClass(), settings);
        assertNotSame(mock, mock2);

        // Test creating a mock without any extra interfaces
        MockSettingsImpl settingsNoInterface = new MockSettingsImpl();
        T mock3 = mockUtil.createMock(Mockito.mock(ClassWithMethods.class).getClass(), settingsNoInterface);
        assertNotNull(mock3);

        // Test creating a mock with a null spied instance
        settingsNoInterface.setSpiedInstance(null);
        T mock4 = mockUtil.createMock(Mockito.mock(ClassWithMethods.class).getClass(), settingsNoInterface);
        Mockito.verifyNoInteractions(mock4);

        // Test creating a mock with an invalid type
        try {
            mockUtil.createMock(int.class, settingsNoInterface);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test creating a mock with both valid and invalid extra interfaces
        Class<?>[] extraInterfaces = new Class[]{Serializable.class, Integer.class};
        MockSettingsImpl settingsWithInvalidInterface = new MockSettingsImpl();
        settingsWithInvalidInterface.setExtraInterfaces(extraInterfaces);
        T mock5 = mockUtil.createMock(Mockito.mock(ClassWithMethods.class).getClass(), settingsWithInvalidInterface);
        assertNotNull(mock5);

        try {
            mockUtil.createMock(Mockito.mock(ClassWithMethods.class).getClass(), settingsWithInvalidInterface);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test creating a mock using the default constructor
        MockUtil mockUtilDefault = new MockUtil();
        T mock6 = mockUtilDefault.createMock(Mockito.mock(ClassWithMethods.class).getClass(), settings);
        assertNotNull(mock6);

        // Test creating a mock with a null creationValidator
        MockCreationValidator nullCreationValidator = null;
        try {
            MockUtil mockUtilNullCreationValidator = new MockUtil(nullCreationValidator);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test creating a mock using the default constructor with a null creationValidator
        MockUtil mockUtilDefaultNullCreationValidator = new MockUtil();
        try {
            mockUtilDefaultNullCreationValidator.createMock(Mockito.mock(ClassWithMethods.class).getClass(), null);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // ... Test cases for other methods in MockUtil class
}

class ClassWithMethods {
    public String getMockName() { return ""; }
}