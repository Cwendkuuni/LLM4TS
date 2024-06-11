package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    @Test
    public void testNextPhase_InitializesGameWithUntapPhase() {
        Game game = new Game();
        assertNotNull(game.getCurrentPhase());
        assertEquals(Phase.class, game.getCurrentPhase().getClass());
        assertEquals(UntapPhase.class, game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_UntapPhaseToUpkeepPhase() {
        Game game = new Game();
        Phase currentPhase = game.getCurrentPhase();
        assertNotNull(currentPhase);
        assertEquals(UntapPhase.class, currentPhase.getClass());

        Phase nextPhase = Phase.nextPhase();
        currentPhase = game.getCurrentPhase();
        assertNotNull(currentPhase);
        assertEquals(UpkeepPhase.class, currentPhase.getClass());
    }

    @Test
    public void testNextPhase_UpkeepPhaseToDrawPhase() {
        Game game = new Game();
        Phase currentPhase = game.getCurrentPhase();
        assertNotNull(currentPhase);
        assertEquals(UntapPhase.class, currentPhase.getClass());

        for (int i = 0; i < 3; i++) {
            Phase.nextPhase();
            currentPhase = game.getCurrentPhase();
            assertNotNull(currentPhase);
        }

        assertEquals(DrawPhase.class, currentPhase.getClass());
    }

    @Test
    public void testNextPhase_DrawPhaseToMain1Phase() {
        Game game = new Game();
        Phase currentPhase = game.getCurrentPhase();
        assertNotNull(currentPhase);
        assertEquals(UntapPhase.class, currentPhase.getClass());

        for (int i = 0; i < 4; i++) {
            Phase.nextPhase();
            currentPhase = game.getCurrentPhase();
            assertNotNull(currentPhase);
        }

        assertEquals(Main1Phase.class, currentPhase.getClass());
    }

    // Test other phases in a similar manner, following the same pattern as shown above.

    // ...

    @Test
    public void testChangePhase_ValidPhases() {
        Game game = new Game();
        Phase currentPhase = game.getCurrentPhase();
        assertNotNull(currentPhase);
        assertEquals(UntapPhase.class, currentPhase.getClass());

        Phase newPhase = null;
        try {
            newPhase = Phase.getClass().newInstance();
            assertEquals(Phase.class, newPhase.getClass());

            switch (game.getCurrentPhase().getClass().getSimpleName()) {
                case "UntapPhase":
                    newPhase = new UpkeepPhase();
                    break;
                case "UpkeepPhase":
                    newPhase = new DrawPhase();
                    break;
                // Add cases for other phases
                // ...
            }

            Phase.changePhase(newPhase);
            currentPhase = game.getCurrentPhase();
            assertNotNull(currentPhase);
            assertEquals(newPhase.getClass(), currentPhase.getClass());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testChangePhase_InvalidPhase() {
        Game game = new Game();
        Phase currentPhase = game.getCurrentPhase();
        assertNotNull(currentPhase);

        Phase newPhase = new CleanupPhase(); // Change this to an invalid phase
        Phase.changePhase(newPhase);
    }

    @Test
    public void testChangePhase_PlayerChangePhase() {
        Game game = new Game();
        Phase currentPhase = game.getCurrentPhase();
        assertNotNull(currentPhase);

        Phase newPhase = null;
        try {
            newPhase = Phase.getClass().newInstance();
            assertEquals(Phase.class, newPhase.getClass());
            assertEquals(PlayerChangePhase.class, newPhase.getClass());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Phase.changePhase(newPhase);
        currentPhase = game.getCurrentPhase();
        assertNotNull(currentPhase);
        assertEquals(UntapPhase.class, currentPhase.getClass());

        Game.playingPlayer = Game.playingPlayer % 2 + 1;
        currentPhase = game.getCurrentPhase();
        assertNotNull(currentPhase);
        assertEquals(UntapPhase.class, currentPhase.getClass());
    }
}