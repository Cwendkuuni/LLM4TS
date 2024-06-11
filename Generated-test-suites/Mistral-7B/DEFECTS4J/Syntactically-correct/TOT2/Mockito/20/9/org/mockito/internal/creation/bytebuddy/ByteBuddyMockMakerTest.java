import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;

@RunWith(JUnit4.class)
public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler handler = Mockito.mock(MockHandler.class);
        Class<?> mockType = Mockito.mock(Class.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withTypeToMock(mockType).build();

        Object mock = byteBuddyMockMaker.createMock(settings, handler);

        assertNotNull(mock);
        assertEquals(Mockito.class, mock.getClass().getSuperclass());
    }
}