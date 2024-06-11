import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PhaseTest_DoPhase {
    private Phase phase;

    @Before
    public void setup() {
        phase = Mockito.spy(new ConcretePhase());
    }

    @Test
    public void testDoPhase() {
        Phase.nextPhase();
        verify(phase).doPhase();
    }
}