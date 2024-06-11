import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockSettingsImpl;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.reflection.LenientCopyTool;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertThat;
import static org.mockito.Mockito.eq;
import static org.junit.Assert.fail;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<String> stringClass = String.class;
        String mockString = mock(String.class);

        T mockedObject = mockUtil.createMock(stringClass, settings);

        verify(mockString).equals(eq("mockString")); // LenientCopyTool copies the name to the mock
        assertThat(mockedObject).isInstanceOf(String.class);

        LenientCopyTool.copyToMock(new Object() {
            String name = "mockName";
        }, mockedObject);

        MockName mockName = mockUtil.getMockName(mockedObject);
        assertThat(mockName.getMockName()).isEqualTo("mockName");
    }

    @Test
    public void testEdgeCasesAndExceptions() {
        MockUtil mockUtil = new MockUtil();

        // Test createMock with null
        try {
            mockUtil.createMock(null, null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }

        // Test getMockHandler with null
        Object nonMockObject = new Object();
        try {
            mockUtil.getMockHandler(nonMockObject);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }

        // Test getInterceptor with null
        MockHandler mockHandler = mock(MockHandler.class);
        mockUtil.getInterceptor(mock(Object.class, mockHandler));
        try {
            mockUtil.getInterceptor(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test isMock with non-Mockito mock
        Object nonMockitoMock = mock(Object.class, new MockitoJdk3MockCreator());
        assertFalse(mockUtil.isMock(nonMockitoMock));
    }
}