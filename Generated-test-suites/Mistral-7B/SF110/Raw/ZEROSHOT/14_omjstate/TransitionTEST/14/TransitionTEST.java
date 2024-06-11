package uk.me.jockmacmad.jstate.state;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.me.jockmacmad.jstate.predicate.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private List<IPredicate> guardConditions;

 @Before
 public void setUp() {
 initialState = Mockito.mock(IState.class);
 outcomeState = Mockito.mock(IState.class);
 triggerEvent = Mockito.mock(Event.class);
 guardConditions = new ArrayList<>();

 transition = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("testTransition2", triggerEvent, initialState, outcomeState);

 assertTrue(transition.equals(transition1));
 assertFalse(transition.equals(transition2));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("testTransition", triggerEvent, initialState, outcomeState);

 assertEquals(transition.hashCode(), transition1.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("Transition{name='testTransition', triggerEvent=", transition.toString());
 }

 @Test
 public void testAddGuardCondition() {
 IPredicate predicate = Mockito.mock(IPredicate.class);

 transition.addGuardCondition(predicate);
 transition.addGuardCondition(new AlwaysTruePredicate());

 assertEquals(3, transition.guardConditions.size());
 }

 @Test
 public void testEvaluateGuardConditions() {
 IPredicate predicate = Mockito.mock(IPredicate.class);
 Mockito.when(predicate.evaluate(triggerEvent)).thenReturn(true);

 transition.addGuardCondition(predicate);
 transition.addGuardCondition(new AlwaysTruePredicate());

 transition.evaluateGuardConditions();

 verify(predicate, times(1)).evaluate(triggerEvent);
 }

 @Test
 public void testGetInitialState() {
 assertEquals(initialState, transition.getInitialState());
 }

 @Test
 public void testGetOutcomeState() {
 assertEquals(outcomeState, transition.getOutcomeState());
 }

 @Test
 public void testGetTriggerEvent() {
 assertEquals(triggerEvent, transition.getTriggerEvent());
 }

 @Test
 public void testTrigger() {
 IState currentState = Mockito.mock(IState.class);

 Mockito.when(initialState.equals(currentState)).thenReturn(true);
 Mockito.when(outcomeState.equals(Mockito.any())).thenReturn(outcomeState);

 transition.trigger(currentState, triggerEvent);

 assertEquals(outcomeState, transition.getOutcomeState());
 }

 @Test
 public void testWillTrigger() {
 IState currentState = Mockito.mock(IState.class);

 Mockito.when(initialState.equals(currentState)).thenReturn(true);
 Mockito.when(outcomeState.equals(Mockito.any())).thenReturn(outcomeState);

 assertFalse(transition.willTrigger(currentState, triggerEvent));

 transition.addGuardCondition(Mockito.mock(IPredicate.class));

 Mockito.when(guardConditions.get(0).evaluate(triggerEvent)).thenReturn(true);

 assertTrue(transition.willTrigger(currentState, triggerEvent));
 }
}