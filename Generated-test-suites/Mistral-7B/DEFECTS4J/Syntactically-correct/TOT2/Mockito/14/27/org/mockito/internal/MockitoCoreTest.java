import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockSettings;

public class MockitoCoreTest {
    @Test
    public void testMock() {
        Class<String> stringClass = String.class;
        MockSettings mockSettings = mockSettings().lenient();
        String mock = mock(stringClass, mockSettings);
        // verify basic functionality
        assertNotNull(mock);
        // edge case: mock an interface
        Class<MyMockInterface> interfaceClass = MyMockInterface.class;
        MockSettings lenientInterfaceSettings = mockSettings().lenient();
        MyMockInterface mockInterface = mock(interfaceClass, lenientInterfaceSettings);
        // exception handling: NullPointerException when classToMock is null
        try {
            mock(null, mockSettings());
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // expected result
        }
        // exception handling: IllegalArgumentException when classToMock is an abstract class
        try {
            mock(AbstractMockClass.class, mockSettings());
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected result
        }
    }
}