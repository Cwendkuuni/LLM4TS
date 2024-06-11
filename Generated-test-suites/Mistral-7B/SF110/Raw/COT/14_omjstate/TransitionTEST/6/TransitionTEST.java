package uk.me.jockmacmad.jstate.state;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class TransitionTest {

 @Mock
 private IState initialState;

 @Mock
 private IState outcomeState;

 @Mock
 private Event triggerEvent;

 @Mock
 private IPredicate predicate1;

 @Mock
 private IGuardCondition guardCondition1;

 @InjectMocks
 private Transition transition;

 @Test
 public void testTransitionConstructor() {
 // Testing the constructor
 Transition transition = new Transition("testName", triggerEvent, initialState, outcomeState);
 assertNotNull(transition.getName());
 assertSame(initialState, transition.getInitialState());
 assertSame(outcomeState, transition.getOutcomeState());
 assertSame(triggerEvent, transition.getTriggerEvent());
 }

 @Test
 public void testAddGuardCondition() {
 // Testing the addGuardCondition method
 transition.addGuardCondition(predicate1);
 List<IGuardCondition> guardConditions = transition.getGuardConditions();
 assertTrue(guardConditions.contains(predicate1));
 }

 @Test
 public void testAddGuardConditionWithGuardCondition() {
 // Testing the addGuardCondition method with IGuardCondition
 transition.addGuardCondition(guardCondition1);
 List<IGuardCondition> guardConditions = transition.getGuardConditions();
 assertTrue(guardConditions.contains(guardCondition1));
 }

 @Test
 public void testEquals() {
 // Testing the equals method
 Transition transition1 = new Transition("testName1", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("testName2", triggerEvent, initialState, outcomeState);
 assertFalse(transition1.equals(transition2));

 Transition transition3 = new Transition("testName1", triggerEvent, initialState, outcomeState);
 assertTrue(transition1.equals(transition3));
 }

 @Test
 public void testHashCode() {
 // Testing the hashCode method
 Transition transition1 = new Transition("testName1", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("testName2", triggerEvent, initialState, outcomeState);
 assertNotSame(transition1.hashCode(), transition2.hashCode());

 Transition transition3 = new Transition("testName1", triggerEvent, initialState, outcomeState);
 assertEquals(transition1.hashCode(), transition3.hashCode());
 }

 @Test
 public void testToString() {
 // Testing the toString method
 assertEquals("Transition{name='testName', triggerEvent=testEvent, initialState=testState, outcomeState=testState}", transition.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTriggerWithNullInitialState() {
 // Testing trigger method with null initial state
 transition.trigger(null, triggerEvent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTriggerWithNullOutcomeState() {
 // Testing trigger method with null outcome state
 transition.trigger(initialState, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTriggerWithNullTriggerEvent() {
 // Testing trigger method with null trigger event
 transition.trigger(initialState, null);
 }

 @Test
 public void testTriggerHappyPath() {
 // Testing trigger method with valid parameters and no guards
 when(initialState.equals(initialState)).thenReturn(true);
 when(initialState.equals(outcomeState)).thenReturn(false);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);
 when(guardCondition1.evaluate(triggerEvent)).thenReturn(true);

 IState result = transition.trigger(initialState, triggerEvent);
 assertSame(outcomeState, result);
 }

 @Test
 public void testTriggerWithInvalidInitialState() {
 // Testing trigger method with invalid initial state
 when(initialState.equals(initialState)).thenReturn(true);
 when(initialState.equals(outcomeState)).thenReturn(false);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);
 when(guardCondition1.evaluate(triggerEvent)).thenReturn(false);

 IState result = transition.trigger(initialState, triggerEvent);
 assertSame(initialState, result);
 }

 @Test
 public void testTriggerWithInvalidTriggerEvent() {
 // Testing trigger method with invalid trigger event
 when(initialState.equals(initialState)).thenReturn(true);
 when(initialState.equals(outcomeState)).thenReturn(false);
 when(triggerEvent.equals(triggerEvent)).thenReturn(false);
 when(guardCondition1.evaluate(triggerEvent)).thenReturn(true);

 IState result = transition.trigger(initialState, triggerEvent);
 assertSame(initialState, result);
 }

 @Test
 public void testTriggerWithGuards() {
 // Testing trigger method with valid parameters and guards
 when(initialState.equals(initialState)).thenReturn(true);
 when(initialState.equals(outcomeState)).thenReturn(false);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);
 when(guardCondition1.evaluate(triggerEvent)).thenReturn(false);
 when(guardCondition2.evaluate(triggerEvent)).thenReturn(true);

 IState result = transition.trigger(initialState, triggerEvent);
 assertSame(outcomeState, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testTriggerWithFailedGuards() {
 // Testing trigger method with valid parameters and failed guards
 when(initialState.equals(initialState)).thenReturn(true);
 when(initialState.equals(outcomeState)).thenReturn(false);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);
 when(guardCondition1.evaluate(triggerEvent)).thenReturn(false);
 when(guardCondition2.evaluate(triggerEvent)).thenReturn(false);

 IState result = transition.trigger(initialState, triggerEvent);
 }

 @Test
 public void testWillTriggerHappyPath() {
 // Testing willTrigger method with valid parameters and no guards
 when(initialState.equals(initialState)).thenReturn(true);
 when(initialState.equals(outcomeState)).thenReturn(false);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);
 when(guardCondition1.evaluate(triggerEvent)).thenReturn(true);

 boolean result = transition.willTrigger(initialState, triggerEvent);
 assertTrue(result);
 }

 @Test
 public void testWillTriggerInvalidInitialState() {
 // Testing willTrigger method with invalid initial state
 when(initialState.equals(initialState)).thenReturn(true);
 when(initialState.equals(outcomeState)).thenReturn(false);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);
 when(guardCondition1.evaluate(triggerEvent)).thenReturn(true);

 boolean result = transition.willTrigger(null, triggerEvent);
 assertFalse(result);
 }

 @Test
 public void testWillTriggerInvalidTriggerEvent() {
 // Testing willTrigger method with invalid trigger event
 when(initialState.equals(initialState)).thenReturn(true);
 when(initialState.equals(outcomeState)).thenReturn(false);
 when(triggerEvent.equals(triggerEvent)).thenReturn(false);
 when(guardCondition1.evaluate(triggerEvent)).thenReturn(true);

 boolean result = transition.willTrigger(initialState, null);
 assertFalse(result);
 }

 @Test
 public void testWillTriggerWithGuards() {
 // Testing willTrigger method with valid parameters and guards
 when(initialState.equals(initialState)).thenReturn(true);
 when(initialState.equals(outcomeState)).thenReturn(false);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);
 when(guardCondition1.evaluate(triggerEvent)).thenReturn(false);
 when(guardCondition2.evaluate(triggerEvent)).thenReturn(true);

 boolean result = transition.willTrigger(initialState, triggerEvent);
 assertTrue(result);
 }

 @Test
 public void testWillTriggerWithFailedGuards() {
 // Testing willTrigger method with valid parameters and failed guards
 when(initialState.equals(initialState)).thenReturn(true);
 when(initialState.equals(outcomeState)).thenReturn(false);
 when(triggerEvent.equals(triggerEvent)).thenReturn(true);
 when(guardCondition1.evaluate(triggerEvent)).thenReturn(false);
 when(guardCondition2.evaluate(triggerEvent)).thenReturn(false);

 boolean result = transition.willTrigger(initialState, triggerEvent);
 assertFalse(result);
 }
}