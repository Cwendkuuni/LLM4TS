package uk.me.jockmacmad.jstate.state;

import org.junit.Test;
import org.junit.Assert.*;
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
 initialState = new FakeInitialState();
 outcomeState = new FakeOutcomeState();
 triggerEvent = new FakeEvent();
 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testConstructor() {
 Transition transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 assertEquals("TestTransition", transition.getName());
 assertEquals(initialState, transition.getInitialState());
 assertEquals(outcomeState, transition.getOutcomeState());
 assertEquals(triggerEvent, transition.getTriggerEvent());
 }

 @Test
 public void testAddGuardCondition() {
 IPredicate predicate = new FakePredicate();
 transition.addGuardCondition(predicate);
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 transition.addGuardCondition(null);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TestTransition1", triggerEvent, initialState, outcomeState);
 assertFalse(transition.equals(transition1));
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 assertTrue(transition.equals(transition2));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TestTransition1", triggerEvent, initialState, outcomeState);
 int hashCode1 = transition.hashCode();
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 int hashCode2 = transition.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 assertEquals("Transition{name='TestTransition', triggerEvent=TestEvent, initialState=FakeInitialState, outcomeState=FakeOutcomeState}", transition.toString());
 }

 @Test
 public void testTrigger_ValidTransition() {
 currentState = initialState;
 assertTrue(transition.willTrigger(currentState, triggerEvent));
 currentState = initialState;
 transition.trigger(currentState, triggerEvent);
 assertEquals(outcomeState, currentState);
 }

 @Test
 public void testTrigger_InvalidInitialState() {
 currentState = new FakeInvalidState();
 assertFalse(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testTrigger_InvalidTriggerEvent() {
 currentState = initialState;
 Event invalidEvent = new FakeInvalidEvent();
 assertFalse(transition.willTrigger(currentState, invalidEvent));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTrigger_NullCurrentState() {
 transition.trigger(null, triggerEvent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTrigger_NullEvent() {
 transition.trigger(currentState, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testTrigger_NotCurrentState() {
 currentState = new FakeInvalidState();
 transition.trigger(currentState, triggerEvent);
 }

 @Test
 public void testWillTrigger_ValidTransition() {
 currentState = initialState;
 assertTrue(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTrigger_InvalidInitialState() {
 currentState = new FakeInvalidState();
 assertFalse(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTrigger_InvalidTriggerEvent() {
 currentState = initialState;
 Event invalidEvent = new FakeInvalidEvent();
 assertFalse(transition.willTrigger(currentState, invalidEvent));
 }

 @Test
 public void testWillTrigger_NoGuards() {
 currentState = initialState;
 assertTrue(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTrigger_GuardsFail() {
 currentState = initialState;
 IPredicate failingPredicate = new FakeFailPredicate();
 transition.addGuardCondition(failingPredicate);
 assertFalse(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTrigger_GuardsPass() {
 currentState = initialState;
 IPredicate passingPredicate = new FakePassPredicate();
 transition.addGuardCondition(passingPredicate);
 assertTrue(transition.willTrigger(currentState, triggerEvent));
 }
}