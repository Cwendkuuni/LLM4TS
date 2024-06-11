
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.stub.Answer;
import org.mockito.stub.Answer2;
import org.mockito.stub.Answer3;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MockUtilTest {
    private MockUtil mockUtil;
    private MockCreationValidator mockCreationValidator;
    private MockSettingsImpl mockSettings;
    private Object mock;

    @Before
    public void setUp() {
        mockCreationValidator = Mockito.mock(MockCreationValidator.class);
        mockUtil = new MockUtil(mockCreationValidator);
        mockSettings = new MockSettingsImpl();
        mock = Mockito.mock(String.class);
    }

    @Test
    public void testCreateMock_ValidArguments() {
        // Given
        Class<String> classToMock = String.class;
        Object spiedInstance = "Hello";

        // When
        String mock = mockUtil.createMock(classToMock, mockSettings);

        // Then
        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        Assert.assertNotNull(mockHandler);
        Assert.assertEquals(classToMock, mockHandler.getMockType());
        Assert.assertNull(mockHandler.getMockName());
        Assert.assertNotNull(mock);
    }

    @Test
    public void testCreateMock_WithExtraInterfaces() {
        // Given
        Class<String> classToMock = String.class;
        Object spiedInstance = "Hello";
        Class<?>[] extraInterfaces = {List.class};

        // When
        String mock = mockUtil.createMock(classToMock, new MockSettingsImpl().extraInterfaces(extraInterfaces).spiedInstance(spiedInstance));

        // Then
        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        Assert.assertNotNull(mockHandler);
        Assert.assertEquals(classToMock, mockHandler.getMockType());
        Assert.assertNotNull(mockHandler.getMockName());
        Assert.assertArrayEquals(new Class<?>[]{List.class, String.class}, mockHandler.getMockName().getMockNameParts());
        Assert.assertNotNull(mock);
    }

    @Test
    public void testCreateMock_NullArguments() {
        // Given
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = "Hello";

        // When and Then
        Assert.assertThrows(NullPointerException.class, () -> mockUtil.createMock(null, settings));
        Assert.assertThrows(NullPointerException.class, () -> mockUtil.createMock(classToMock, null));
        Assert.assertThrows(NullPointerException.class, () -> mockUtil.createMock(classToMock, settings).spiedInstance(null));
    }

    @Test
    public void testCreateMock_NonInstantiatableClassToMock() {
        // Given
        Class<String> classToMock = String[].class;

        // When and Then
        Assert.assertThrows(IllegalArgumentException.class, () -> mockUtil.createMock(classToMock, mockSettings));
    }

    @Test
    public void testCreateMock_MultipleCallsWithSameArguments() {
        // Given
        Class<String> classToMock = String.class;
        Object spiedInstance = "Hello";
        MockSettingsImpl settings = new MockSettingsImpl();

        // When
        String mock1 = mockUtil.createMock(classToMock, settings);
        String mock2 = mockUtil.createMock(classToMock, settings);

        // Then
        Assert.assertNotSame(mock1, mock2);
    }

    @Test
    public void testCreateMock_NotAMockOrFactoryInstance() {
        // Given
        Object mock = new Object();

        // When and Then
        Assert.assertFalse(mockUtil.isMock(mock));
        Assert.assertThrows(NotAMockException.class, () -> mockUtil.getMockHandler(mock));
    }

    @Test
    public void testResetMock_ValidArguments() {
        // Given

        // When
        mockUtil.resetMock(mock);

        // Then
        MockHandlerInterface<String> oldMockHandler = mockUtil.getMockHandler(mock);
        MockHandler<String> newMockHandler = mockUtil.getMockHandler(mock);

        // Check that the handlers are different
        Assert.assertNotSame(oldMockHandler, newMockHandler);
    }

    @Test(expected = NullPointerException.class)
    public void testResetMock_NullMock() {
        // Given
        mockUtil = new MockUtil();
        mock = null;

        // When
        mockUtil.resetMock(mock);

        // Then
        // Assertion error should be thrown
    }
}