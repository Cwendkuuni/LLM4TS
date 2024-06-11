import org.junit.Test;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import static org.junit.Assert.assertNotNull;

public class ByteBuddyMockMakerTest {

    @Test
    public void testByteBuddyMockMakerConstructor() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        assertNotNull(byteBuddyMockMaker);
    }
}