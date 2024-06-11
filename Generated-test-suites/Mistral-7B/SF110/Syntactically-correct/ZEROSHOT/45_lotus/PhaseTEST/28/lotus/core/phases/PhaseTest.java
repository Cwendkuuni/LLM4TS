import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest
{
    private Phase untapPhase;
    private Phase upkeepPhase;
    private Phase drawPhase;
    private Phase main1Phase;
    private Phase combatBeginningPhase;
    private Phase declareAttackersPhase;
    private Phase declareBlockersPhase;
    private Phase combatDamagePhase;
    private Phase combatEndPhase;
    private Phase main2Phase;
    private Phase endOfTurnPhase;
    private Phase cleanupPhase;
    private Phase playerChangePhase;
    private Game game;

    @Before
    public void setup()
    {
        untapPhase = Mockito.spy(new UntapPhase());
        upkeepPhase = Mockito.spy(new UpkeepPhase());
        drawPhase = Mockito.spy(new DrawPhase());
        main1Phase = Mockito.spy(new Main1Phase());
        combatBeginningPhase = Mockito.spy(new CombatBeginningPhase());
        declareAttackersPhase = Mockito.spy(new DeclareAttackersPhase());
        declareBlockersPhase = Mockito.spy(new DeclareBlockersPhase());
        combatDamagePhase = Mockito.spy(new CombatDamagePhase());
        combatEndPhase = Mockito.spy(new CombatEndPhase());
        main2Phase = Mockito.spy(new Main2Phase());
        endOfTurnPhase = Mockito.spy(new EndOfTurnPhase());
        cleanupPhase = Mockito.spy(new CleanupPhase());
        playerChangePhase = Mockito.spy(new PlayerChangePhase());
        game = mock(Game.class);

        Mockito.when(game.currentPhase).thenReturn(untapPhase);
        Mockito.when(untapPhase.doPhase()).thenCallRealMethod();

        Mockito.when(game.currentPhase).thenReturn(upkeepPhase);
        Mockito.when(upkeepPhase.doPhase()).thenCallRealMethod();

        Mockito.when(game.currentPhase).thenReturn(drawPhase);
        Mockito.when(drawPhase.doPhase()).thenCallRealMethod();

        Mockito.when(game.currentPhase).thenReturn(main1Phase);
        Mockito.when(main1Phase.doPhase()).thenCallRealMethod();

        Mockito.when(game.currentPhase).thenReturn(combatBeginningPhase);
        Mockito.when(combatBeginningPhase.doPhase()).thenCallRealMethod();

        Mockito.when(game.currentPhase).thenReturn(declareAttackersPhase);
        Mockito.when(declareAttackersPhase.doPhase()).thenCallRealMethod();

        Mockito.when(game.currentPhase).thenReturn(declareBlockersPhase);
        Mockito.when(declareBlockersPhase.doPhase()).thenCallRealMethod();

        Mockito.when(game.currentPhase).thenReturn(combatDamagePhase);
        Mockito.when(combatDamagePhase.doPhase()).thenCallRealMethod();

        Mockito.when(game.currentPhase).thenReturn(combatEndPhase);
        Mockito.when(combatEndPhase.doPhase()).thenCallRealMethod();

        Mockito.when(game.currentPhase).thenReturn(main2Phase);
        Mockito.when(main2Phase.doPhase()).thenCallRealMethod();

        Mockito.when(game.currentPhase).thenReturn(endOfTurnPhase);
        Mockito.when(endOfTurnPhase.doPhase()).thenCallRealMethod();

        Mockito.when(game.currentPhase).thenReturn(cleanupPhase);
        Mockito.when(cleanupPhase.doPhase()).thenCallRealMethod();

        Mockito.when(game.currentPhase).thenReturn(playerChangePhase);
        Mockito.when(playerChangePhase.doPhase()).thenCallRealMethod();
    }

    @Test
    public void testNextPhase_UntapPhaseToUpkeepPhase()
    {
        Phase.nextPhase(game);
        verify(upkeepPhase).doPhase();
    }

    @Test
    public void testNextPhase_UpkeepPhaseToDrawPhase()
    {
        Mockito.when(game.currentPhase).thenReturn(upkeepPhase);
        Phase.nextPhase(game);
        verify(drawPhase).doPhase();
    }

    // Add more test cases here for all possible conditions and phases

    @Test
    public void testNextPhase_CleanupPhaseToPlayerChangePhase()
    {
        Mockito.when(game.currentPhase).thenReturn(cleanupPhase);
        Phase.nextPhase(game);
        verify(playerChangePhase).doPhase();
    }
}