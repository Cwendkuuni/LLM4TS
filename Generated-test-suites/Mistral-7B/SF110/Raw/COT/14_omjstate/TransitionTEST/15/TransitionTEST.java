package uk.me.jockmacmad.jstate.state.test;

import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private IState currentState;
 private IPredicate predicate1;
 private IPredicate predicate2;
 private IGuardCondition guardCondition1;
 private IGuardCondition guardCondition2;

 @Test
 public void testTransitionConstructor() {
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 assertEquals("testTransition", transition.getName());
 assertNull(transition.getTriggerEvent());
 assertNotNull(transition.getInitialState());
 assertNotNull(transition.getOutcomeState());
 assertEmpty(transition.getGuardConditions());
 }

 @Test
 public void testAddGuardCondition() {
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 guardCondition1 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent");
 }
 };
 transition.addGuardCondition(guardCondition1);
 assertTrue(transition.getGuardConditions().size() == 1);
 }

 @Test
 public void testAddGuardConditionWithPredicate() {
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 predicate1 = new AlwaysTruePredicate();
 transition.addGuardCondition(predicate1);
 assertTrue(transition.getGuardConditions().get(0) instanceof AlwaysTruePredicate);
 }

 @Test
 public void testAddGuardConditionWithGuardCondition() {
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 guardCondition1 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent");
 }
 };
 transition.addGuardCondition(guardCondition1);
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 guardCondition2 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent2");
 }
 };
 transition.addGuardCondition(guardCondition2);
 assertEquals(2, transition.getGuardConditions().size());
 }

 @Test
 public void testEvaluateGuardConditions() {
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 guardCondition1 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent");
 }
 };
 guardCondition1.setNegated(true);
 transition.addGuardCondition(guardCondition1);
 transition.evaluateGuardConditions();
 assertFalse(transition.willTrigger(initialState, new Event("testEvent")));
 }

 @Test
 public void testWillTriggerWithEmptyGuardConditions() {
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 assertTrue(transition.willTrigger(initialState, new Event("testEvent")));
 }

 @Test
 public void testWillTriggerWithOneGuardCondition() {
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 guardCondition1 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent");
 }
 };
 transition.addGuardCondition(guardCondition1);
 assertTrue(transition.willTrigger(initialState, new Event("testEvent")));
 assertFalse(transition.willTrigger(initialState, new Event("testEvent2")));
 }

 @Test
 public void testWillTriggerWithMultipleGuardConditions() {
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 guardCondition1 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent");
 }
 };
 guardCondition2 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent2");
 }
 };
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);
 assertTrue(transition.willTrigger(initialState, new Event("testEvent")));
 assertFalse(transition.willTrigger(initialState, new Event("testEvent2")));
 }

 @Test(expected = IllegalStateException.class)
 public void testWillTriggerWithNullInitialState() {
 transition = new Transition("testTransition", new Event("testEvent"), null, outcomeState);
 transition.willTrigger(currentState, triggerEvent);
 fail("IllegalStateException expected but not thrown");
 }

 @Test(expected = IllegalStateException.class)
 public void testWillTriggerWithNullEvent() {
 transition = new Transition("testTransition", null, initialState, outcomeState);
 transition.willTrigger(currentState, triggerEvent);
 fail("IllegalStateException expected but not thrown");
 }

 @Test
 public void testTriggerWithEmptyGuardConditions() {
 currentState = initialState;
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 assertSame(initialState, transition.trigger(currentState, triggerEvent));
 }

 @Test
 public void testTriggerWithOneGuardConditionPassed() {
 currentState = initialState;
 guardCondition1 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent");
 }
 };
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 transition.addGuardCondition(guardCondition1);
 assertTrue(transition.willTrigger(currentState, triggerEvent));
 assertSame(outcomeState, transition.trigger(currentState, triggerEvent));
 }

 @Test
 public void testTriggerWithOneGuardConditionFailed() {
 currentState = initialState;
 guardCondition1 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return !event.getName().equals("testEvent");
 }
 };
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 transition.addGuardCondition(guardCondition1);
 assertSame(initialState, transition.trigger(currentState, triggerEvent));
 }

 @Test
 public void testTriggerWithMultipleGuardConditionsPassed() {
 currentState = initialState;
 guardCondition1 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent");
 }
 };
 guardCondition2 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent2");
 }
 };
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);
 event = new Event("testEvent1");
 assertFalse(transition.willTrigger(currentState, event));
 event = new Event("testEvent");
 assertTrue(transition.willTrigger(currentState, event));
 assertSame(outcomeState, transition.trigger(currentState, event));
 }

 @Test
 public void testTriggerWithMultipleGuardConditionsFailed() {
 currentState = initialState;
 guardCondition1 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent");
 }
 };
 guardCondition2 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return !event.getName().equals("testEvent");
 }
 };
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);
 assertSame(initialState, transition.trigger(currentState, new Event("testEvent")));
 }

 @Test
 public void testTriggerWithNullInitialState() {
 transition = new Transition("testTransition", new Event("testEvent"), null, outcomeState);
 transition.trigger(currentState, triggerEvent);
 fail("NullPointerException expected but not thrown");
 }

 @Test
 public void testTriggerWithNullEvent() {
 transition = new Transition("testTransition", null, initialState, outcomeState);
 transition.trigger(currentState, triggerEvent);
 fail("NullPointerException expected but not thrown");
 }

 @Test
 public void testTriggerWithEmptyGuardConditionsAndInitialStateIsOutcomeState() {
 initialState = outcomeState;
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 assertSame(initialState, transition.trigger(initialState, triggerEvent));
 }

 @Test
 public void testTriggerWithOneGuardConditionAndInitialStateIsOutcomeState() {
 initialState = outcomeState;
 guardCondition1 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent");
 }
 };
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 transition.addGuardCondition(guardCondition1);
 assertSame(initialState, transition.trigger(initialState, triggerEvent));
 }

 @Test
 public void testTriggerWithMultipleGuardConditionsAndInitialStateIsOutcomeState() {
 initialState = outcomeState;
 guardCondition1 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent");
 }
 };
 guardCondition2 = new GuardCondition() {
 @Override
 public boolean evaluate(Event event) {
 return event.getName().equals("testEvent2");
 }
 };
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);
 assertSame(initialState, transition.trigger(initialState, triggerEvent));
 }

 @Test
 public void testToString() {
 transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
 assertEquals("Transition{name='testTransition', initialState=" + initialState.toString() + ", outcomeState=" + outcomeState.toString() + ", triggerEvent=" + triggerEvent.toString() + ", guardConditions=[]}", transition.toString());
 }

 @Test
 public void testHashCode() {
 List<Object> objects = new ArrayList<Object>(Arrays.asList(new Object[]{transition, initialState, outcomeState, triggerEvent}));
 int hashCode = HashCodeBuilder.reflectionHashCode(objects.toArray());
 assertEquals(hashCode, transition.hashCode());
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("testTransition1", new Event("testEvent1"), initialState, outcomeState);
 Transition transition2 = new Transition("testTransition2", new Event("testEvent2"), initialState, outcomeState);
 Transition transition3 = new Transition("testTransition1", new Event("testEvent1"), initialState, outcomeState);
 assertFalse(transition1.equals(transition2));
 assertTrue(transition1.equals(transition3));
 }
}