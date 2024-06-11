package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.DummyPredicate;
import uk.me.jockmacmad.jstate.state.Transition;

public class TransitionTest {

 private Transition transition;

 @Before
 public void setUp() {
 Event triggerEvent = new Event("testEvent");
 IState initialState = new DummyState("initialState");
 IState outcomeState = new DummyState("outcomeState");
 this.transition = new Transition("testTransition", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testConstructor() {
 Transition transition = new Transition("testTransition", new Event("testEvent"), new DummyState("initialState"), new DummyState("outcomeState"));
 assertNotNull(transition.getInitialState());
 assertNotNull(transition.getOutcomeState());
 assertNotNull(transition.getTriggerEvent());
 assertSame(transition.getName(), "testTransition");
 }

 @Test
 public void testAddGuardCondition() {
 IPredicate predicate = new DummyPredicate();
 this.transition.addGuardCondition(predicate);
 assertFalse(this.transition.getGuardConditions().isEmpty());
 }

 @Test
 public void testAddGuardConditionType1() {
 IGuardCondition guardCondition = new DummyGuardCondition();
 this.transition.addGuardCondition(guardCondition);
 assertFalse(this.transition.getGuardConditions().isEmpty());
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("testTransition1", new Event("testEvent"), new DummyState("initialState"), new DummyState("outcomeState"));
 Transition transition2 = new Transition("testTransition2", new Event("testEvent"), new DummyState("initialState"), new DummyState("outcomeState"));
 assertTrue(transition1.equals(transition1));
 assertTrue(transition1.equals(transition2));
 Transition transition3 = new Transition("testTransition3", new Event("testEvent2"), new DummyState("initialState"), new DummyState("outcomeState"));
 assertFalse(transition1.equals(transition3));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("testTransition1", new Event("testEvent"), new DummyState("initialState"), new DummyState("outcomeState"));
 Transition transition2 = new Transition("testTransition1", new Event("testEvent"), new DummyState("initialState"), new DummyState("outcomeState"));
 assertTrue(transition1.hashCode() == transition2.hashCode());
 Transition transition3 = new Transition("testTransition3", new Event("testEvent"), new DummyState("initialState"), new DummyState("outcomeState"));
 assertFalse(transition1.hashCode() == transition3.hashCode());
 }

 @Test
 public void testToString() {
 String expected = "Transition{name='testTransition', triggerEvent=Event{name='testEvent'}, initialState=DummyState{name='initialState'}, outcomeState=DummyState{name='outcomeState'}}";
 String actual = transition.toString();
 assertTrue(ToStringBuilder.reflectionToString(transition).equals(expected));
 }

 @Test(expected = NullPointerException.class)
 public void testTriggerNullInitialState() {
 Transition transition = new Transition("testTransition", new Event("testEvent"), null, new DummyState("outcomeState"));
 IState pCurrentState = null;
 assertNull(transition.trigger(pCurrentState, new Event("testEvent")));
 }

 @Test(expected = NullPointerException.class)
 public void testTriggerNullOutcomeState() {
 Transition transition = new Transition("testTransition", new Event("testEvent"), new DummyState("initialState"), null);
 IState pCurrentState = new DummyState("initialState");
 assertNull(transition.trigger(pCurrentState, new Event("testEvent")));
 }

 @Test(expected = NullPointerException.class)
 public void testTriggerNullEvent() {
 Transition transition = new Transition("testTransition", null, new DummyState("initialState"), new DummyState("outcomeState"));
 IState pCurrentState = new DummyState("initialState");
 assertNull(transition.trigger(pCurrentState, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTriggerDifferentEvents() {
 Transition transition = new Transition("testTransition", new Event("testEvent"), new DummyState("initialState"), new DummyState("outcomeState"));
 IState pCurrentState = new DummyState("initialState");
 assertFalse(transition.willTrigger(pCurrentState, new Event("testEvent2")));
 }

 @Test
 public void testTriggerEmptyGuardConditions() {
 IState pCurrentState = new DummyState("initialState");
 assertTrue(transition.willTrigger(pCurrentState, new Event("testEvent")));
 }

 @Test
 public void testTriggerNotPassingGuards() {
 IGuardCondition guardCondition = new DummyGuardCondition();
 guardCondition.setPassesGuard(false);
 this.transition.addGuardCondition(guardCondition);
 IState pCurrentState = new DummyState("initialState");
 assertFalse(transition.willTrigger(pCurrentState, new Event("testEvent")));
 }

 @Test
 public void testTriggerPassingGuards() {
 IGuardCondition guardCondition = new DummyGuardCondition();
 guardCondition.setPassesGuard(true);
 this.transition.addGuardCondition(guardCondition);
 IState pCurrentState = new DummyState("initialState");
 assertTrue(transition.willTrigger(pCurrentState, new Event("testEvent")));
 }

 @Test
 public void testTriggerPassingInitialStateAndEvent() {
 IState pCurrentState = new DummyState("initialState");
 assertTrue(transition.willTrigger(pCurrentState, new Event("testEvent")));
 }

 @Test
 public void testTriggerPassingInitialStateDifferentEvent() {
 IState pCurrentState = new DummyState("initialState");
 assertFalse(transition.willTrigger(pCurrentState, new Event("testEvent2")));
 }

 @Test
 public void testTriggerPassingInitialStateNullEvent() {
 IState pCurrentState = new DummyState("initialState");
 assertNull(transition.trigger(pCurrentState, null));
 }

 @Test
 public void testTriggerPassingOutcomeState() {
 IState pCurrentState = this.transition.getInitialState();
 assertTrue(this.transition.willTrigger(pCurrentState, this.transition.getTriggerEvent()));
 }

 @Test
 public void testTriggerPassingOutcomeStateDifferentEvent() {
 IState pCurrentState = this.transition.getInitialState();
 assertFalse(this.transition.willTrigger(pCurrentState, new Event("testEvent2")));
 }

 @Test
 public void testTriggerPassingOutcomeStateNullEvent() {
 IState pCurrentState = this.transition.getInitialState();
 assertNull(this.transition.trigger(pCurrentState, null));
 }

 @Test
 public void testTriggerMultipleGuardConditions() {
 IGuardCondition guardCondition1 = new DummyGuardCondition();
 guardCondition1.setPassesGuard(true);
 IGuardCondition guardCondition2 = new DummyGuardCondition();
 guardCondition2.setPassesGuard(false);
 this.transition.addGuardCondition(guardCondition1);
 this.transition.addGuardCondition(guardCondition2);
 IState pCurrentState = new DummyState("initialState");
 assertFalse(transition.willTrigger(pCurrentState, new Event("testEvent")));
 }

 @Test
 public void testTriggerMultipleGuardConditionsPassing() {
 IGuardCondition guardCondition1 = new DummyGuardCondition();
 guardCondition1.setPassesGuard(true);
 IGuardCondition guardCondition2 = new DummyGuardCondition();
 guardCondition2.setPassesGuard(true);
 this.transition.addGuardCondition(guardCondition1);
 this.transition.addGuardCondition(guardCondition2);
 IState pCurrentState = new DummyState("initialState");
 assertTrue(transition.willTrigger(pCurrentState, new Event("testEvent")));
 }

 @Test
 public void testTriggerMultipleGuardConditionsPassing2() {
 IGuardCondition guardCondition1 = new DummyGuardCondition();
 guardCondition1.setPassesGuard(true);
 IGuardCondition guardCondition2 = new DummyGuardCondition();
 guardCondition2.setPassesGuard(true);
 this.transition.addGuardCondition(guardCondition1);
 this.transition.addGuardCondition(guardCondition2);
 IState pCurrentState = new DummyState("initialState");
 assertTrue(transition.willTrigger(pCurrentState, new Event("testEvent")));
 }

 @Test(expected = IllegalStateException.class)
 public void testTriggerAlreadyTriggered() {
 IState pCurrentState = this.transition.getInitialState();
 this.transition.trigger(pCurrentState, this.transition.getTriggerEvent());
 assertFalse(this.transition.willTrigger(pCurrentState, this.transition.getTriggerEvent()));
 }

 @Test
 public void testTriggerMultipleTransitionsSameStateSameEvent() {
 IState pCurrentState = this.transition.getInitialState();
 Transition transition1 = new Transition("testTransition1", new Event("testEvent"), pCurrentState, new DummyState("outcomeState1"));
 Transition transition2 = new Transition("testTransition2", new Event("testEvent"), pCurrentState, new DummyState("outcomeState2"));
 transition1.addGuardCondition(new DummyGuardCondition());
 transition2.addGuardCondition(new DummyGuardCondition());
 IState outcomeState1 = transition1.trigger(pCurrentState, new Event("testEvent"));
 IState outcomeState2 = transition2.trigger(pCurrentState, new Event("testEvent"));
 assertFalse(outcomeState1.equals(outcomeState2));
 }

 @Test
 public void testTriggerMultipleTransitionsSameStateSameEventDifferentInitialStates() {
 IState pCurrentState1 = this.transition.getInitialState();
 IState pCurrentState2 = new DummyState("initialState2");
 Transition transition1 = new Transition("testTransition1", new Event("testEvent"), pCurrentState1, new DummyState("outcomeState1"));
 Transition transition2 = new Transition("testTransition2", new Event("testEvent"), pCurrentState2, new DummyState("outcomeState2"));
 transition1.addGuardCondition(new DummyGuardCondition());
 transition2.addGuardCondition(new DummyGuardCondition());
 IState outcomeState1 = transition1.trigger(pCurrentState1, new Event("testEvent"));
 IState outcomeState2 = transition2.trigger(pCurrentState2, new Event("testEvent"));
 assertFalse(outcomeState1.equals(outcomeState2));
 }

 @Test
 public void testTriggerMultipleTransitionsSameStateDifferentEvents() {
 IState pCurrentState = this.transition.getInitialState();
 Transition transition1 = new Transition("testTransition1", new Event("testEvent1"), pCurrentState, new DummyState("outcomeState1"));
 Transition transition2 = new Transition("testTransition2", new Event("testEvent2"), pCurrentState, new DummyState("outcomeState2"));
 transition1.addGuardCondition(new DummyGuardCondition());
 transition2.addGuardCondition(new DummyGuardCondition());
 IState outcomeState1 = transition1.trigger(pCurrentState, new Event("testEvent1"));
 IState outcomeState2 = transition2.trigger(pCurrentState, new Event("testEvent2"));
 assertFalse(outcomeState1.equals(outcomeState2));
 }

 @Test
 public void testTriggerMultipleTransitionsDifferentStatesSameEvent() {
 IState pCurrentState1 = this.transition.getInitialState();
 IState pCurrentState2 = new DummyState("initialState2");
 Transition transition1 = new Transition("testTransition1", new Event("testEvent"), pCurrentState1, new DummyState("outcomeState1"));
 Transition transition2 = new Transition("testTransition2", new Event("testEvent"), pCurrentState2, new DummyState("outcomeState2"));
 transition1.addGuardCondition(new DummyGuardCondition());
 transition2.addGuardCondition(new DummyGuardCondition());
 IState outcomeState1 = transition1.trigger(pCurrentState1, new Event("testEvent"));
 IState outcomeState2 = transition2.trigger(pCurrentState2, new Event("testEvent"));
 assertFalse(outcomeState1.equals(outcomeState2));
 }

 @Test
 public void testTriggerMultipleTransitionsDifferentStatesDifferentEvents() {
 IState pCurrentState1 = this.transition.getInitialState();
 IState pCurrentState2 = new DummyState("initialState2");
 Transition transition1 = new Transition("testTransition1", new Event("testEvent1"), pCurrentState1, new DummyState("outcomeState1"));
 Transition transition2 = new Transition("testTransition2", new Event("testEvent2"), pCurrentState2, new DummyState("outcomeState2"));
 transition1.addGuardCondition(new DummyGuardCondition());
 transition2.addGuardCondition(new DummyGuardCondition());
 IState outcomeState1 = transition1.trigger(pCurrentState1, new Event("testEvent1"));
 IState outcomeState2 = transition2.trigger(pCurrentState2, new Event("testEvent2"));
 assertFalse(outcomeState1.equals(outcomeState2));
 }
}