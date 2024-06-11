import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        MockHandler mockHandler = mock(MockHandler.class);
        T mock = new ByteBuddyMockMaker().createMock(MockCreationSettings.forClass(String.class), mockHandler);
        verify(mockHandler).handle(ArgumentMatchers.any());
    }
}