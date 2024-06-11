package uk.me.jockmacmad.jstate.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;

 @Before
 public void setUp() {
 initialState = new State("InitialState");
 outcomeState = new State("OutcomeState");
 triggerEvent = new Event("TriggerEvent");
 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 }

 @After
 public void tearDown() {
 transition = null;
 initialState = null;
 outcomeState = null;
 triggerEvent = null;
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 Transition transition3 = new Transition("DifferentTransitionName", triggerEvent, initialState, outcomeState);

 assertEquals(transition1, transition1);
 assertEquals(transition1, transition2);
 assertNotEquals(transition1, transition3);
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 Transition transition3 = new Transition("DifferentTransitionName", triggerEvent, initialState, outcomeState);

 assertEquals(transition1.hashCode(), transition1.hashCode());
 assertEquals(transition1.hashCode(), transition2.hashCode());
 assertNotEquals(transition1.hashCode(), transition3.hashCode());
 }

 @Test
 public void testToString() {
 String expectedToString = "TransitionName{initialState=InitialState, outcomeState=OutcomeState, triggerEvent=TriggerEvent}";
 assertEquals(expectedToString, transition.toString());
 }

 @Test
 public void testAddGuardCondition() {
 IPredicate predicate = new AlwaysTruePredicate();
 transition.addGuardCondition(predicate);
 List<IGuardCondition> expectedGuardConditions = new ArrayList<>();
 expectedGuardConditions.add(predicate);
 List<IGuardCondition> actualGuardConditions = transition.getGuardConditions();
 assertArrayEquals(expectedGuardConditions.toArray(), actualGuardConditions.toArray());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 IPredicate nullPredicate = null;
 transition.addGuardCondition(nullPredicate);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testEvaluateGuardConditions() {
 IPredicate predicate = new AndPredicate(new AlwaysTruePredicate(), new AlwaysFalsePredicate());
 transition.addGuardCondition(predicate);
 Event event = new Event("Event");
 assertFalse(transition.evaluateGuardConditions(event));
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
 IState currentState = new State("CurrentState");
 transition.trigger(currentState, triggerEvent);
 assertEquals(outcomeState, currentState);
 }

 @Test
 public void testWillTrigger() {
 IState currentState = new State("CurrentState");
 assertTrue(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillNotTrigger() {
 IState currentState = new State("CurrentStateDifferentState");
 assertFalse(transition.willTrigger(currentState, triggerEvent));
 }
}