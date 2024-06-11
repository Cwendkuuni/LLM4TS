import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(MockitoJUnitRunner.class)
public class UntapPhaseTest extends Phase {

    @Test
    public void testDoPhase() {
        // Initialize mocks
        Game mockGame = Mockito.mock(Game.class);

        // Set up expectations for mocks
        Mockito.when(mockGame.currentPhase).thenReturn(this);
        
        // Call method under test
        this.doPhase();

        // Verify the expected behavior
        Mockito.verify(mockGame).currentPhase = new UpkeepPhase();
    }
}