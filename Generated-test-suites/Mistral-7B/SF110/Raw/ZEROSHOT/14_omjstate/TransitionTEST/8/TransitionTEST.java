package uk.me.jockmacmad.jstate.state;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private IState currentState;

 @Before
 public void setup() {
 initialState = new State1();
 outcomeState = new State2();
 triggerEvent = new Event("testEvent");
 transition = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 assertTrue(transition.equals(transition1));
 assertTrue(transition1.equals(transition));
 Transition transition3 = new Transition("testTransition2", triggerEvent, initialState, outcomeState);
 assertFalse(transition.equals(transition3));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 assertEquals(transition.hashCode(), transition1.hashCode());
 Transition transition3 = new Transition("testTransition2", triggerEvent, initialState, outcomeState);
 assertNotSame(transition.hashCode(), transition3.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("Transition{name='testTransition', triggerEvent=Event{name='testEvent'}, initialState=State1{}, outcomeState=State2{}, guardConditions=[]}", transition.toString());
 }

 @Test
 public void testTrigger() {
 IState expectedOutcomeState = outcomeState;
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 assertEquals(expectedOutcomeState, actualOutcomeState);
 }

 @Test
 public void testTrigger_NoGuards() {
 transition.addGuardCondition(new AlwaysTruePredicate());
 IState expectedOutcomeState = outcomeState;
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 assertEquals(expectedOutcomeState, actualOutcomeState);
 }

 @Test
 public void testTrigger_FailsGuards() {
 transition.addGuardCondition(new AlwaysFalsePredicate());
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 assertNotSame(outcomeState, actualOutcomeState);
 }

 @Test
 public void testWillTrigger() {
 boolean expectedResult = false;
 boolean actualResult = transition.willTrigger(currentState, triggerEvent);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testWillTrigger_GuardsPass() {
 transition.addGuardCondition(new AlwaysTruePredicate());
 boolean expectedResult = true;
 boolean actualResult = transition.willTrigger(currentState, triggerEvent);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testWillTrigger_GuardsFail() {
 transition.addGuardCondition(new AlwaysFalsePredicate());
 boolean expectedResult = false;
 boolean actualResult = transition.willTrigger(currentState, triggerEvent);
 assertEquals(expectedResult, actualResult);
 }
}