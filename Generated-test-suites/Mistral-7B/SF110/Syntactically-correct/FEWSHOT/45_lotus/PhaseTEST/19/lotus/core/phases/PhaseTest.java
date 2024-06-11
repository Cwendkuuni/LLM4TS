package lotus.core.phases;

import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    private Game game;

    @Before
    public void setUp() {
        game = new Game();
    }

    @Test
    public void testNextPhase_InitialState() {
        assertEquals(UntapPhase.class, game.getPhase().getClass());
        Phase.nextPhase();
        assertEquals(UpkeepPhase.class, game.getPhase().getClass());
    }

    @Test
    public void testNextPhase_UntapPhase() {
        game.setPhase(new UntapPhase());
        Phase.nextPhase();
        assertEquals(UpkeepPhase.class, game.getPhase().getClass());
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        game.setPhase(new UpkeepPhase());
        Phase.nextPhase();
        assertEquals(DrawPhase.class, game.getPhase().getClass());
    }

    @Test
    public void testNextPhase_DrawPhase() {
        game.setPhase(new DrawPhase());
        Phase.nextPhase();
        assertEquals(Main1Phase.class, game.getPhase().getClass());
    }

    @Test
    public void testNextPhase_Main1Phase() {
        game.setPhase(new Main1Phase());
        Phase.nextPhase();
        assertEquals(CombatBeginningPhase.class, game.getPhase().getClass());
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        game.setPhase(new CombatBeginningPhase());
        Phase.nextPhase();
        assertEquals(DeclareAttackersPhase.class, game.getPhase().getClass());
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        game.setPhase(new DeclareAttackersPhase());
        Phase.nextPhase();
        assertEquals(DeclareBlockersPhase.class, game.getPhase().getClass());
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        game.setPhase(new DeclareBlockersPhase());
        Phase.nextPhase();
        assertEquals(CombatDamagePhase.class, game.getPhase().getClass());
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        game.setPhase(new CombatDamagePhase());
        Phase.nextPhase();
        assertEquals(CombatEndPhase.class, game.getPhase().getClass());
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        game.setPhase(new CombatEndPhase());
        Phase.nextPhase();
        assertEquals(Main2Phase.class, game.getPhase().getClass());
    }

    @Test
    public void testNextPhase_Main2Phase() {
        game.setPhase(new Main2Phase());
        Phase.nextPhase();
        assertEquals(EndOfTurnPhase.class, game.getPhase().getClass());
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        game.setPhase(new EndOfTurnPhase());
        Phase.nextPhase();
        assertEquals(CleanupPhase.class, game.getPhase().getClass());
    }

    @Test
    public void testNextPhase_CleanupPhase() {
        game.setPhase(new CleanupPhase());
        Phase.nextPhase();
        assertEquals(PlayerChangePhase.class, game.getPhase().getClass());
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        game.setPhase(new PlayerChangePhase());
        Phase.nextPhase();
        assertEquals(UntapPhase.class, game.getPhase().getClass());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNextPhase_InvalidPhase() {
        game.setPhase(new Object());
        Phase.nextPhase();
    }
}