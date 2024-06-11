package uk.me.jockmacmad.jstate.state.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.*;
import org.apache.commons.lang3.builder.*;
import java.util.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;

 @Before
 public void setUp() {
 initialState = new InitialState();
 outcomeState = new OutcomeState();
 triggerEvent = new TriggerEvent();

 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);

 assertTrue(transition.equals(transition1));
 assertTrue(transition1.equals(transition));

 Transition transition3 = new Transition("TestTransition2", triggerEvent, initialState, outcomeState);
 assertFalse(transition.equals(transition3));
 assertFalse(transition3.equals(transition));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);

 assertEquals(transition.hashCode(), transition1.hashCode());

 Transition transition3 = new Transition("TestTransition2", triggerEvent, initialState, outcomeState);
 assertNotEquals(transition.hashCode(), transition3.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("Transition{name='TestTransition', triggerEvent=TriggerEvent, initialState=InitialState, outcomeState=OutcomeState}", transition.toString());
 }

 @Test
 public void testTrigger() {
 IState currentState = new CurrentState();

 assertNotEquals(outcomeState, transition.trigger(currentState, triggerEvent));
 assertNotEquals(outcomeState, transition.trigger(initialState, triggerEvent));
 assertSame(initialState, transition.trigger(outcomeState, triggerEvent));

 transition.addGuardCondition(new AndGuard(new IsEventTypeGuard(EventType.EVENT_TYPE_1), new IsValueGuard(Value.VALUE_1)));
 IState guardedOutcomeState = new GuardedOutcomeState();

 assertNotEquals(outcomeState, transition.trigger(currentState, triggerEvent));
 assertNotEquals(outcomeState, transition.trigger(initialState, triggerEvent));
 assertSame(initialState, transition.trigger(outcomeState, triggerEvent));

 assertSame(guardedOutcomeState, transition.trigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTrigger() {
 IState currentState = new CurrentState();

 assertFalse(transition.willTrigger(currentState, triggerEvent));
 assertFalse(transition.willTrigger(initialState, triggerEvent));

 transition.addGuardCondition(new AndGuard(new IsEventTypeGuard(EventType.EVENT_TYPE_1), new IsValueGuard(Value.VALUE_1)));
 IState guardedCurrentState = new GuardedCurrentState();

 assertFalse(transition.willTrigger(currentState, triggerEvent));
 assertFalse(transition.willTrigger(initialState, triggerEvent));

 assertFalse(transition.willTrigger(guardedCurrentState, triggerEvent));

 IState guardedOutcomeState = new GuardedOutcomeState();

 assertFalse(transition.willTrigger(currentState, triggerEvent));
 assertFalse(transition.willTrigger(initialState, triggerEvent));

 assertTrue(transition.willTrigger(guardedOutcomeState, triggerEvent));
 }
}