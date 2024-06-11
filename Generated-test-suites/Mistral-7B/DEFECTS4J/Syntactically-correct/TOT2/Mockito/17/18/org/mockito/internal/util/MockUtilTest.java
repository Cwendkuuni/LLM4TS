
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.MockSettingsImpl;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.MockitoException;
import org.mockito.internal.util.MockitoMessageBuilder;
import static org.junit.Assert.*;

public class MockUtilTest {

    @Test
    public void testCreateMockWithNullClassToMock() {
        MockUtil mockUtil = new MockUtil();
        try {
            mockUtil.createMock(null, new MockSettingsImpl());
            fail();
        } catch (NullPointerException e) {
            assertEquals("classToMock is marked non-null but is null", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithNullSettings() {
        MockUtil mockUtil = new MockUtil();
        try {
            mockUtil.createMock(MockitoMock.class, null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("settings is marked non-null but is null", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithUnsupportedClass() {
        MockUtil mockUtil = new MockUtil();
        Class<String> stringClass = String.class;
        try {
            mockUtil.createMock(stringClass, new MockSettingsImpl());
            fail();
        } catch (UnsupportedOperationException e) {
            assertEquals("MockUtil can only create mocks for interfaces and abstract classes", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithIncorrectExtraInterfaces() {
        MockUtil mockUtil = new MockUtil();
        Class<MockitoMock> mockitoMockClass = MockitoMock.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[]{Double.class});
        try {
            mockUtil.createMock(mockitoMockClass, settings);
            fail();
        } catch (MockitoException e) {
            assertEquals("Extra interfaces must be valid interfaces or abstract classes", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithNullSpiedInstance() {
        MockUtil mockUtil = new MockUtil();
        Class<MockitoMock> mockitoMockClass = MockitoMock.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        try {
            mockUtil.createMock(mockitoMockClass, settings, null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("spiedInstance is marked non-null but is null", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithMultipleCallbacks() {
        MockUtil mockUtil = new MockUtil();
        Class<MockitoMock> mockitoMockClass = MockitoMock.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Factory mockFactory = Mockito.mock(Factory.class);
        Callback mockCallback = Mockito.mock(Callback.class);
        Callback mockCallback2 = Mockito.mock(Callback.class);
        MethodInterceptorFilter mockFilter = new MethodInterceptorFilter(mockCallback, settings);
        MethodInterceptorFilter mockFilter2 = new MethodInterceptorFilter(mockCallback2, settings);

        settings.setCallback(0, mockFilter);
        settings.setCallback(1, mockFilter2);

        try {
            mockUtil.createMock(mockitoMockClass, settings);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Mockito does not allow multiple callbacks on the same mock", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithIncorrectHandlerType() {
        MockUtil mockUtil = new MockUtil();
        Class<MockitoMock> mockitoMockClass = MockitoMock.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        MockHandler<MockitoMock> mockHandler = Mockito.mock(MockHandler.class);

        try {
            mockUtil.createMock(mockitoMockClass, settings, mockHandler);
            fail();
        } catch (ClassCastException e) {
            assertEquals("The returned mock should be an instance of: " + MockHandler.class.getName(), e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        try {
            mockUtil.isMockitoMock(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("mock is marked non-null but is null", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithNotAMockitoMock() {
        MockUtil mockUtil = new MockUtil();
        String mockString = new String();
        try {
            mockUtil.isMockitoMock(mockString);
            fail();
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: " + mockString.getClass(), e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithNullMockName() {
        MockUtil mockUtil = new MockUtil();
        Object mock = Mockito.mock(Object.class);
        try {
            mockUtil.getMockName(mock);
            fail();
        } catch (NullPointerException e) {
            assertEquals("mockName is marked non-null but is null", e.getMessage());
        }
    }
}
