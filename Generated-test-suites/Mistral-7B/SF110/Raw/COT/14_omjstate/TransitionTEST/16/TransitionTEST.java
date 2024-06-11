package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;

 @Before
 public void setup() {
 initialState = new FooState();
 outcomeState = new BarState();
 triggerEvent = new TriggerEvent();
 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testConstructor() {
 Transition transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 assertEquals("TransitionName", transition.getName());
 assertEquals(initialState, transition.getInitialState());
 assertEquals(outcomeState, transition.getOutcomeState());
 assertEquals(triggerEvent, transition.getTriggerEvent());
 }

 @Test
 public void testAddGuardCondition() {
 transition.addGuardCondition(new AlwaysTruePredicate());
 assertEquals(1, transition.getGuardConditions().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 transition.addGuardCondition(null);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TransitionName1", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TransitionName2", triggerEvent, initialState, outcomeState);
 Transition transition3 = new Transition("TransitionName1", triggerEvent, initialState, outcomeState);
 Transition transition4 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);

 assertEquals(transition, transition);
 assertNotEquals(transition, transition1);
 assertNotEquals(transition, transition2);
 assertNotEquals(transition, transition3);
 assertNotEquals(transition, transition4);
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TransitionName1", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TransitionName2", triggerEvent, initialState, outcomeState);
 Transition transition3 = new Transition("TransitionName1", triggerEvent, initialState, outcomeState);
 Transition transition4 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);

 int hashCode1 = transition.hashCode();
 int hashCode2 = transition1.hashCode();
 int hashCode3 = transition2.hashCode();
 int hashCode4 = transition3.hashCode();
 int hashCode5 = transition4.hashCode();

 assertNotEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode1, hashCode4);
 assertNotEquals(hashCode1, hashCode5);
 }

 @Test
 public void testToString() {
 assertEquals("TransitionName{initialState=FooState, outcomeState=BarState, triggerEvent=TriggerEvent}", transition.toString());
 }

 @Test
 public void testTrigger() {
 IState currentState = new FooState();
 IState expectedState = outcomeState;
 Transition transition = this.transition;

 IState actualState = transition.trigger(currentState, triggerEvent);
 assertEquals(expectedState, actualState);
 }

 @Test
 public void testTrigger_initialStateNotMatching() {
 IState currentState = new BarState();
 IState expectedState = initialState;
 Transition transition = this.transition;

 IState actualState = transition.trigger(currentState, triggerEvent);
 assertEquals(expectedState, actualState);
 }

 @Test
 public void testTrigger_triggerEventNotMatching() {
 IState currentState = new FooState();
 Event event = new TriggerEvent2();
 Transition transition = this.transition;

 IState actualState = transition.trigger(currentState, event);
 assertEquals(currentState, actualState);
 }

 @Test
 public void testTrigger_guardsNotPassing() {
 IState currentState = new FooState();
 List<IGuardCondition> guardConditions = new ArrayList<>();
 guardConditions.add(new AlwaysFalsePredicate());
 transition.setGuardConditions(guardConditions);

 IState actualState = transition.trigger(currentState, triggerEvent);
 assertEquals(currentState, actualState);
 }

 @Test
 public void testWillTrigger() {
 IState currentState = new FooState();
 IState expectedState = outcomeState;
 Transition transition = this.transition;

 boolean actualState = transition.willTrigger(currentState, triggerEvent);
 assertEquals(expectedState != null, actualState);
 }

 @Test
 public void testWillTrigger_initialStateNotMatching() {
 IState currentState = new BarState();
 Transition transition = this.transition;

 boolean actualState = transition.willTrigger(currentState, triggerEvent);
 assertEquals(false, actualState);
 }

 @Test
 public void testWillTrigger_triggerEventNotMatching() {
 IState currentState = new FooState();
 Event event = new TriggerEvent2();
 Transition transition = this.transition;

 boolean actualState = transition.willTrigger(currentState, event);
 assertEquals(false, actualState);
 }

 @Test
 public void testWillTrigger_guardsNotPassing() {
 IState currentState = new FooState();
 List<IGuardCondition> guardConditions = new ArrayList<>();
 guardConditions.add(new AlwaysFalsePredicate());
 transition.setGuardConditions(guardConditions);

 boolean actualState = transition.willTrigger(currentState, triggerEvent);
 assertEquals(false, actualState);
 }

 @Test
 public void testEvaluateGuardConditions() {
 List<IGuardCondition> guardConditions = Arrays.asList(new AlwaysTruePredicate(), new AlwaysFalsePredicate());
 transition.setGuardConditions(guardConditions);

 boolean passesGuards = transition.evaluateGuardConditions();
 assertEquals(false, passesGuards);
 }

 @Test
 public void testEvaluateGuardConditions_onePassingGuard() {
 List<IGuardCondition> guardConditions = Arrays.asList(new AlwaysTruePredicate(), new AlwaysFalsePredicate(), new AlwaysTruePredicate());
 transition.setGuardConditions(guardConditions);

 boolean passesGuards = transition.evaluateGuardConditions();
 assertEquals(true, passesGuards);
 }

 @Test
 public void testEvaluateGuardConditions_allGuardsPassing() {
 List<IGuardCondition> guardConditions = Collections.singletonList(new AlwaysTruePredicate());
 transition.setGuardConditions(guardConditions);

 boolean passesGuards = transition.evaluateGuardConditions();
 assertEquals(true, passesGuards);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetGuardConditions_guardConditionsCannotBeSetAfterConstruction() {
 List<IGuardCondition> guardConditions = new ArrayList<>();
 transition.setGuardConditions(guardConditions);
 }

 @Test
 public void testGetGuardConditions() {
 List<IGuardCondition> expectedGuardConditions = Arrays.asList(new AlwaysTruePredicate(), new AlwaysFalsePredicate());
 List<IGuardCondition> actualGuardConditions = transition.getGuardConditions();

 assertEquals(expectedGuardConditions, actualGuardConditions);
 }
}