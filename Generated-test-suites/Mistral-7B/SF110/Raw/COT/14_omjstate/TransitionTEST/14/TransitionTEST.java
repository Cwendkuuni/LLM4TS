package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private IState testState;

 @Before
 public void setUp() {
 initialState = new ConcreteState();
 outcomeState = new ConcreteState();
 triggerEvent = new ConcreteEvent();
 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testConstructor() {
 Transition t = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 assertEquals(initialState, t.getInitialState());
 assertEquals(outcomeState, t.getOutcomeState());
 assertEquals(triggerEvent, t.getTriggerEvent());
 }

 @Test
 public void testAddGuardCondition() {
 IPredicate predicate = new ConcretePredicate();
 transition.addGuardCondition(predicate);
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test
 public void testGetGuardConditions() {
 IPredicate predicate = new ConcretePredicate();
 transition.addGuardCondition(predicate);
 List<IGuardCondition> guardConditions = transition.getGuardConditions();
 assertEquals(predicate, guardConditions.get(0));
 }

 @Test
 public void testEquality_SameObject() {
 Transition transition1 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 assertSame(transition, transition1);
 }

 @Test
 public void testEquality_DifferentObjects() {
 Transition transition1 = new Transition("TestTransition1", triggerEvent, initialState, outcomeState);
 assertNotSame(transition, transition1);
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TestTransition1", triggerEvent, initialState, outcomeState);
 assertEquals(HashCodeBuilder.reflectionHashCode(transition), HashCodeBuilder.reflectionHashCode(transition1));
 }

 @Test
 public void testToString() {
 String expectedToString = "Transition[name=TestTransition, triggerEvent=ConcreteEvent{name='TestEvent'}, initialState=ConcreteState{name='ConcreteState'}, outcomeState=ConcreteState{name='ConcreteState'}]";
 String transitionToString = ToStringBuilder.reflectionToString(transition);
 assertEquals(expectedToString, transitionToString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 transition.addGuardCondition(null);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullPredicateToGuardCondition() {
 IPredicate nullPredicate = null;
 transition.addGuardCondition(new ConcreteGuardCondition(nullPredicate));
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testTrigger_StateIsInitialStateAndEventIsTriggerEvent() {
 testState = initialState;
 assertTrue(transition.willTrigger(testState, triggerEvent));
 testState = outcomeState;
 assertEquals(outcomeState, transition.trigger(testState, triggerEvent));
 }

 @Test
 public void testTrigger_StateIsNotInitialStateAndEventIsTriggerEvent() {
 testState = new ConcreteState();
 assertFalse(transition.willTrigger(testState, triggerEvent));
 }

 @Test
 public void testTrigger_StateIsInitialStateAndEventIsNotTriggerEvent() {
 testState = initialState;
 assertFalse(transition.willTrigger(testState, new ConcreteEvent()));
 }

 @Test
 public void testTrigger_StateIsInitialStateAndGuardConditionDoesNotPass() {
 IPredicate predicate = new ConcretePredicate();
 transition.addGuardCondition(predicate);
 testState = initialState;
 assertFalse(transition.willTrigger(testState, triggerEvent));
 }

 @Test
 public void testTrigger_StateIsInitialStateAndGuardConditionPasses() {
 IPredicate predicate = new ConcretePredicate() {
 @Override public boolean evaluate(Event pEvent) { return true; }
 };
 transition.addGuardCondition(predicate);
 testState = initialState;
 assertTrue(transition.willTrigger(testState, triggerEvent));
 }

 @Test
 public void testTrigger_StateIsOutcomeStateAndEventIsTriggerEvent() {
 testState = outcomeState;
 assertFalse(transition.willTrigger(testState, triggerEvent));
 }

 @Test
 public void testTrigger_StateIsOutcomeStateAndEventIsNotTriggerEvent() {
 testState = outcomeState;
 assertFalse(transition.willTrigger(testState, new ConcreteEvent()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTrigger_NullState() {
 transition.trigger(null, triggerEvent);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTrigger_NullEvent() {
 transition.trigger(testState, null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testWillTrigger_StateIsInitialStateAndEventIsTriggerEventAndGuardsPass() {
 IPredicate predicate = new ConcretePredicate();
 transition.addGuardCondition(predicate);
 testState = initialState;
 assertTrue(transition.willTrigger(testState, triggerEvent));
 }

 @Test
 public void testWillTrigger_StateIsInitialStateAndEventIsTriggerEventAndGuardsDoNotPass() {
 IPredicate predicate = new ConcretePredicate();
 transition.addGuardCondition(predicate);
 testState = initialState;
 assertFalse(transition.willTrigger(testState, triggerEvent));
 }

 @Test
 public void testWillTrigger_StateIsInitialStateAndEventIsDifferent() {
 IPredicate predicate = new ConcretePredicate();
 transition.addGuardCondition(predicate);
 testState = initialState;
 assertFalse(transition.willTrigger(testState, new ConcreteEvent()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWillTrigger_NullState() {
 transition.willTrigger(null, triggerEvent);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWillTrigger_NullEvent() {
 transition.willTrigger(testState, null);
 fail("Should throw IllegalArgumentException");
 }
}