package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;

 @Parameters(name = "{0}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // Test case 1
 new Object[]{new Transition("TestTransition1", new Event("TestEvent1"), new State("InitialState1"), new State("OutcomeState1")), new State("InitialState1"), new Event("TestEvent1"), true},
 // Test case 2
 new Object[]{new Transition("TestTransition2", new Event("TestEvent2"), new State("InitialState2"), new State("OutcomeState2")), new State("InitialState2"), new Event("TestEvent2"), true},
 // Test case 3
 new Object[]{new Transition("TestTransition3", new Event("TestEvent3"), new State("InitialState3"), new State("OutcomeState3")), new State("InitialState3"), new Event("TestEvent3"), false},
 // Test case 4
 new Object[]{new Transition("TestTransition4", new Event("TestEvent4"), new State("InitialState4"), new State("OutcomeState4")), new State("InitialState4"), new Event("TestEvent4"), true},
 // Test case 5
 new Object[]{new Transition("TestTransition5", new Event("TestEvent5"), new State("InitialState5"), new State("OutcomeState5")), new State("InitialState5"), new Event("TestEvent5"), false}
 });
 }

 public TransitionTest(Transition pTransition, IState pInitialState, Event pTriggerEvent, boolean pWillTrigger) {
 this.transition = pTransition;
 this.initialState = pInitialState;
 this.triggerEvent = pTriggerEvent;
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullInitialState() {
 this.transition = new Transition(null, new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullOutcomeState() {
 this.transition = new Transition("TestTransition", new Event("TestEvent"), null, new State("OutcomeState"));
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullTriggerEvent() {
 this.transition = new Transition("TestTransition", null, new State("InitialState"), new State("OutcomeState"));
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TestTransition1", new Event("TestEvent1"), new State("InitialState1"), new State("OutcomeState1"));
 Transition transition2 = new Transition("TestTransition2", new Event("TestEvent1"), new State("InitialState1"), new State("OutcomeState1"));
 Transition transition3 = new Transition("TestTransition3", new Event("TestEvent1"), new State("InitialState1"), new State("OutcomeState2"));

 assertTrue(EqualsBuilder.reflectionEquals(transition1, transition1));
 assertTrue(EqualsBuilder.reflectionEquals(transition1, transition2));
 assertFalse(EqualsBuilder.reflectionEquals(transition1, transition3));
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TestTransition1", new Event("TestEvent1"), new State("InitialState1"), new State("OutcomeState1"));
 Transition transition2 = new Transition("TestTransition1", new Event("TestEvent1"), new State("InitialState1"), new State("OutcomeState1"));
 Transition transition3 = new Transition("TestTransition1", new Event("TestEvent1"), new State("InitialState1"), new State("OutcomeState2"));

 int hashCode1 = HashCodeBuilder.reflectionHashCode(transition1);
 int hashCode2 = HashCodeBuilder.reflectionHashCode(transition2);
 int hashCode3 = HashCodeBuilder.reflectionHashCode(transition3);

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
 String expectedString = "Transition{name='TestTransition', triggerEvent=TestEvent, initialState=InitialState, outcomeState=OutcomeState}";
 String actualString = ToStringBuilder.reflectionToString(transition);
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testGetInitialState() {
 assertEquals(this.initialState, this.transition.getInitialState());
 }

 @Test
 public void testGetOutcomeState() {
 assertEquals(this.outcomeState, this.transition.getOutcomeState());
 }

 @Test
 public void testGetTriggerEvent() {
 assertEquals(this.triggerEvent, this.transition.getTriggerEvent());
 }

 @Test
 public void testTriggerNullState() {
 IState currentState = null;
 IState retVal = this.transition.trigger(currentState, this.triggerEvent);
 assertNull(retVal);
 }

 @Test
 public void testTriggerNullEvent() {
 IState currentState = new State("InitialState");
 Event event = null;
 IState retVal = this.transition.trigger(currentState, event);
 assertSame(currentState, retVal);
 }

 @Test
 public void testTriggerDifferentInitialState() {
 IState currentState = new State("DifferentInitialState");
 IState retVal = this.transition.trigger(currentState, this.triggerEvent);
 assertSame(currentState, retVal);
 assertFalse(this.transition.willTrigger(currentState, this.triggerEvent));
 }

 @Test
 public void testTriggerSameInitialStateDifferentEvent() {
 IState currentState = new State("InitialState");
 Event event = new Event("DifferentEvent");
 IState retVal = this.transition.trigger(currentState, event);
 assertSame(currentState, retVal);
 assertFalse(this.transition.willTrigger(currentState, event));
 }

 @Test
 public void testTriggerSameInitialStateSameEvent() {
 IState currentState = new State("InitialState");
 Event event = this.triggerEvent;
 IState retVal = this.transition.trigger(currentState, event);
 assertEquals(this.outcomeState, retVal);
 assertTrue(this.transition.willTrigger(currentState, event));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddGuardConditionNullPredicate() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
 transition.addGuardCondition(null);
 }

 @Test
 public void testAddGuardCondition() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
 IPredicate guard1 = Mockito.mock(IPredicate.class);
 IPredicate guard2 = Mockito.mock(IPredicate.class);
 transition.addGuardCondition(guard1);
 transition.addGuardCondition(guard2);
 assertEquals(2, transition.guardConditions.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddGuardConditionAfterSizeLimit() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
 for (int i = 0; i < Transition.MAX_GUARD_CONDITIONS; i++) {
 transition.addGuardCondition(Mockito.mock(IPredicate.class));
 }
 transition.addGuardCondition(Mockito.mock(IPredicate.class));
 }

 @Test
 public void testEvaluateGuardsNullEvent() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
 boolean passesGuards = transition.evaluateGuardConditions(null);
 assertFalse(passesGuards);
 }

 @Test
 public void testEvaluateGuardsEmptyGuardList() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
 boolean passesGuards = transition.evaluateGuardConditions(this.triggerEvent);
 assertTrue(passesGuards);
 }

 @Test
 public void testEvaluateGuardsOneGuardPasses() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
 IPredicate guard1 = Mockito.mock(IPassesPredicate.class);
 transition.addGuardCondition(guard1);
 boolean passesGuards = transition.evaluateGuardConditions(this.triggerEvent);
 assertTrue(passesGuards);
 }

 @Test
 public void testEvaluateGuardsOneGuardFails() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
 IPredicate guard1 = Mockito.mock(IFailsPredicate.class);
 transition.addGuardCondition(guard1);
 boolean passesGuards = transition.evaluateGuardConditions(this.triggerEvent);
 assertFalse(passesGuards);
 }

 @Test
 public void testEvaluateGuardsMultipleGuardsPass() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
 IPredicate guard1 = Mockito.mock(IPassesPredicate.class);
 IPredicate guard2 = Mockito.mock(IPassesPredicate.class);
 transition.addGuardCondition(guard1);
 transition.addGuardCondition(guard2);
 boolean passesGuards = transition.evaluateGuardConditions(this.triggerEvent);
 assertTrue(passesGuards);
 }

 @Test
 public void testEvaluateGuardsMultipleGuardsFail() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
 IPredicate guard1 = Mockito.mock(IFailsPredicate.class);
 IPredicate guard2 = Mockito.mock(IPassesPredicate.class);
 transition.addGuardCondition(guard1);
 transition.addGuardCondition(guard2);
 boolean passesGuards = transition.evaluateGuardConditions(this.triggerEvent);
 assertFalse(passesGuards);
 }

 @Test
 public void testEvaluateGuardsMixedResult() {
 Transition transition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
 IPredicate guard1 = Mockito.mock(IPassesPredicate.class);
 IPredicate guard2 = Mockito.mock(IFailsPredicate.class);
 transition.addGuardCondition(guard1);
 transition.addGuardCondition(guard2);
 boolean passesGuards = false;
 for (IGuardCondition guard : transition.guardConditions) {
 passesGuards = guard.evaluate(this.triggerEvent);
 if (passesGuards) {
 break;
 }
 }
 assertFalse(passesGuards);
 }

}