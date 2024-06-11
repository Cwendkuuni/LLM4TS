package lotus.core.phases;

import lotus.core.*;
import org.junit.*;
import org.junit.runner.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.mockito.invocation.*;

@RunWith(MockitoJUnitRunner.class)
public class PhaseTest {

    @BeforeClass
    public static void setUpBeforeClass() {
        // Initialize any required mocks or variables
    }

    @AfterClass
    public static void tearDownAfterClass() {
        // Clean up any mocks or variables
    }

    @Test
    public void testDoPhase_UntapPhase() {
        UntapPhase phase = new UntapPhase();
        phase.doPhase();
        // Check the expected outcome of doPhase()
    }

    @Test
    public void testNextPhase_ValidTransition() {
        Game mockGame = mock(Game.class);
        Phase currentPhase = mock(UntapPhase.class);

        when(mockGame.currentPhase).thenReturn(currentPhase);
        when(currentPhase.getClass()).thenReturn(UntapPhase.class);

        Phase nextPhase = Phase.nextPhase(mockGame);
        assertEquals(UpkeepPhase.class, nextPhase.getClass());

        verify(mockGame).currentPhase = nextPhase;
        verify(currentPhase).doPhase();
    }

    @Test(expected = IllegalStateException.class)
    public void testNextPhase_InvalidTransition() {
        Game mockGame = mock(Game.class);
        Phase currentPhase = mock(Object.class);

        when(mockGame.currentPhase).thenReturn(currentPhase);
        when(currentPhase.getClass()).thenThrow(new IllegalStateException());

        Phase nextPhase = Phase.nextPhase(mockGame);

        // Exception should be thrown
    }

    @Test
    public void testNextPhase_LastPhaseTransition() {
        Game mockGame = mock(Game.class);
        Phase currentPhase = mock(CleanupPhase.class);

        when(mockGame.currentPhase).thenReturn(currentPhase);
        when(currentPhase.getClass()).thenReturn(CleanupPhase.class);

        Phase nextPhase = Phase.nextPhase(mockGame);
        assertEquals(PlayerChangePhase.class, nextPhase.getClass());

        verify(mockGame).currentPhase = nextPhase;
        verify(currentPhase).doPhase();
    }

    @After
    public void tearDown() {
        // Clean up any mocks or variables
    }
}