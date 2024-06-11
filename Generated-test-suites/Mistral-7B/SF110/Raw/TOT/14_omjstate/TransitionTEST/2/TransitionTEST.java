import static org.junit.Assert.assertEquals;
import uk.me.jockmacmad.jstate.state.Event;
import uk.me.jockmacmad.jstate.state.IState;
import uk.me.jockmacmad.jstate.state.Transition;
import uk.me.jockmacmad.jstate.state.impl.InitialState;
import uk.me.jockmacmad.jstate.state.impl.OutcomeState;

public class TransitionTest {
    

    @Test
    public void testTrigger_WhenInitialStateIsSameAndEventIsSame_ThenOutcomeStateIsReturned() {
        // Given
        Transition transition = new Transition("TestTransition", new Event("TestEvent"), new InitialState("TestInitialState"), new OutcomeState("TestOutcomeState"));
        IState initialState = new InitialState("TestInitialState");
        IState expectedOutcomeState = new OutcomeState("TestOutcomeState");
        IState currentState = initialState;

        // When
        IState result = transition.trigger(currentState, new Event("TestEvent"));

        // Then
        assertEquals(expectedOutcomeState, result);
    }
    
    
}