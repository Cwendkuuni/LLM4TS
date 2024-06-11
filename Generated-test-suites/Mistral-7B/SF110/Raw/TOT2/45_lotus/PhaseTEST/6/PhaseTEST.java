import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;

public class PhaseTest {
    private Phase phase;
    private Game gameMock;

    @Before
    public void setup() {
        gameMock = Mockito.mock(Game.class);
        phase = new ConcretePhase(); // replace ConcretePhase with the actual subclass
        phase.setGame(gameMock);
    }

    @Test
    public void testDoPhase() {
        phase.doPhase();
        verify(gameMock).currentPhase = Mockito.eq(phase.getClass());
    }
}