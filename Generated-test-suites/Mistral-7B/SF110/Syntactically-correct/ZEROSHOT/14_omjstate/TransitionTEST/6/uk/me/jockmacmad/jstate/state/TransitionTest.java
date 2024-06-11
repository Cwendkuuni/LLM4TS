package uk.me.jockmacmad.jstate.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgsMatch;
import org.mockito.Mockito;
import uk.me.jockmacmad.jstate.predicate.FixedPredicate;
import uk.me.jockmacmad.jstate.predicate.GuardCondition;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private IState currentState;

 @Before
 public void setUp() {
 initialState = Mockito.mock(IState.class);
 outcomeState = Mockito.mock(IState.class);
 triggerEvent = Mockito.mock(Event.class);
 currentState = Mockito.mock(IState.class);

 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 }

 @After
 public void tearDown() {
 Mockito.verifyNoMoreInteractions(initialState, outcomeState, triggerEvent, currentState);
 }

 @Test
 public void testEquals() {
 Transition identical = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 Transition different = new Transition("TestTransitionDifferent", triggerEvent, initialState, outcomeState);

 assertEquals(transition, identical);
 assertFalse(transition.equals(different));
 }

 @Test
 public void testHashCode() {
 Transition identical = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 Transition different = new Transition("TestTransitionDifferent", triggerEvent, initialState, outcomeState);

 assertEquals(transition.hashCode(), identical.hashCode());
 assertFalse(transition.hashCode() == different.hashCode());
 }

 @Test
 public void testToString() {
 String expectedToString = "Transition{name='TestTransition', triggerEvent=MockitoGeneratedClass$MockEventStub$_0$, initialState=MockitoGeneratedClass$MockStateStub$_1$(), outcomeState=MockitoGeneratedClass$MockStateStub$_2$()}";
 String actualToString = transition.toString();

 assertTrue(actualToString.contains(expectedToString));
 }

 @Test
 public void testEmptyGuardConditions() {
 transition.trigger(initialState, triggerEvent);
 assertEquals(outcomeState, transition.getOutcomeState());
 }

 @Test
 public void testSingleGuardCondition() {
 GuardCondition guardCondition = new FixedPredicate(true);
 transition.addGuardCondition(guardCondition);

 Mockito.when(initialState.equals(currentState)).thenReturn(true);
 Mockito.when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 assertFalse(transition.willTrigger(currentState, triggerEvent));
 Mockito.verify(currentState, Mockito.never()).equals(outcomeState);

 transition.trigger(currentState, triggerEvent);
 assertEquals(outcomeState, transition.getOutcomeState());
 Mockito.verify(currentState, Mockito.times(1)).equals(outcomeState);
 }

 @Test
 public void testMultipleGuardConditions() {
 List<GuardCondition> guardConditions = Arrays.asList(
 new FixedPredicate(true),
 new FixedPredicate(false),
 new FixedPredicate(true)
 );

 Mockito.when(initialState.equals(currentState)).thenReturn(true);
 Mockito.when(triggerEvent.equals(triggerEvent)).thenReturn(true);

 for (GuardCondition guardCondition : guardConditions) {
 transition.addGuardCondition(guardCondition);
 }

 assertFalse(transition.willTrigger(currentState, triggerEvent));

 Mockito.verify(currentState, Mockito.never()).equals(outcomeState);

 transition.addGuardCondition(new ArgsMatch<IGuardCondition>(guard -> guard.isSatisfiedBy(triggerEvent)));
 assertTrue(transition.willTrigger(currentState, triggerEvent));

 Mockito.verify(currentState, Mockito.times(1)).equals(outcomeState);
 }
}