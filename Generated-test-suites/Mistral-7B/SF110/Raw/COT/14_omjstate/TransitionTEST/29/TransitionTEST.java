package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EqualsBuilder;
import org.apache.commons.lang3.HashCodeBuilder;
import org.apache.commons.lang3.ToStringBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.DummyPredicate;
import uk.me.jockmacmad.jstate.predicate.DummyGuardCondition;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;

 @Before
 public void setup() {
 initialState = new State("InitialState");
 outcomeState = new State("OutcomeState");
 triggerEvent = new Event("TriggerEvent");
 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testConstructor() {
 Assert.assertNotNull(transition);
 }

 @Test
 public void testAddGuardConditionIPredicate() {
 List<IGuardCondition> guardConditions = new ArrayList<>();
 transition.addGuardCondition(new DummyPredicate());
 transition.addGuardCondition(guardConditions.toArray(new IGuardCondition[0]));
 Assert.assertEquals(2, transition.getGuardConditions().size());
 }

 @Test
 public void testAddGuardConditionIGuardCondition() {
 List<IGuardCondition> guardConditions = new ArrayList<>();
 guardConditions.add(new DummyGuardCondition());
 transition.addGuardCondition(guardConditions.get(0));
 Assert.assertEquals(1, transition.getGuardConditions().size());
 }

 @Test
 public void testEqualsObject() {
 Transition transition1 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 Assert.assertTrue(transition.equals(transition1));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 Assert.assertEquals(transition.hashCode(), transition1.hashCode());
 }

 @Test
 public void testToString() {
 String expectedToString = "TransitionName{initialState=InitialState, outcomeState=OutcomeState, triggerEvent=TriggerEvent}";
 String actualToString = transition.toString();
 Assert.assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testTriggerNullInitialState() {
 IState nullInitialState = null;
 Assert.assertNotSame(outcomeState, transition.trigger(nullInitialState, triggerEvent));
 }

 @Test
 public void testTriggerNullOutcomeState() {
 IState nullOutcomeState = null;
 transition.setOutcomeState(nullOutcomeState);
 Assert.assertNotSame(initialState, transition.trigger(initialState, triggerEvent));
 }

 @Test
 public void testTriggerNullTriggerEvent() {
 Event nullTriggerEvent = null;
 Assert.assertSame(initialState, transition.trigger(initialState, nullTriggerEvent));
 }

 @Test
 public void testTriggerSameInitialStateAndEvent() {
 IState sameInitialState = initialState;
 Assert.assertSame(outcomeState, transition.trigger(sameInitialState, triggerEvent));
 }

 @Test
 public void testTriggerDifferentInitialStateAndEvent() {
 IState differentInitialState = new State("DifferentInitialState");
 Assert.assertNotSame(outcomeState, transition.trigger(differentInitialState, triggerEvent));
 }

 @Test
 public void testTriggerSameInitialStateAndDifferentEvent() {
 IState sameInitialState = initialState;
 Event differentEvent = new Event("DifferentEvent");
 Assert.assertSame(initialState, transition.trigger(sameInitialState, differentEvent));
 }

 @Test
 public void testTriggerInitialStateAndDifferentEventWithGuards() {
 IState sameInitialState = initialState;
 IState differentOutcomeState = new State("DifferentOutcomeState");
 transition.addGuardCondition(new DummyGuardCondition() {
 @Override public boolean evaluate(Event event) {
 return event.getName().equals("TriggerEvent");
 }});
 Assert.assertSame(differentOutcomeState, transition.trigger(sameInitialState, new Event("DifferentEvent")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 transition.addGuardCondition(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEmptyGuardConditionArray() {
 transition.addGuardCondition((IGuardCondition[]) ArrayUtils.EMPTY_ARRAY);
 }
}