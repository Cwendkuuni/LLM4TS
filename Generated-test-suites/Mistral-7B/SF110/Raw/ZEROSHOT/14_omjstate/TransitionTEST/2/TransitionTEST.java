package uk.me.jockmacmad.jstate.state;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import uk.me.jockmacmad.jstate.IState;
import uk.me.jockmacmad.jstate.event.Event;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {

 private Transition transition;
 private IState initialStateMock;
 private IState outcomeStateMock;
 private IState currentStateMock;
 private Event triggerEvent;
 private List<IPredicate> guardConditionsMock;

 @Before
 public void setup() {
 initialStateMock = Mockito.mock(IState.class);
 outcomeStateMock = Mockito.mock(IState.class);
 currentStateMock = Mockito.mock(IState.class);
 triggerEvent = Mockito.mock(Event.class);
 guardConditionsMock = new ArrayList<>();

 AndPredicate andPredicate = new AndPredicate();
 orPredicate = new OrPredicate();

 guardConditionsMock.add(andPredicate);
 guardConditionsMock.add(orPredicate);

 transition = new Transition("Transition Name", triggerEvent, initialStateMock, outcomeStateMock);
 transition.addGuardCondition(andPredicate);
 transition.addGuardCondition(orPredicate);
 }

 @Test
 public void testEquals() {
 Transition transition2 = new Transition("Transition Name", triggerEvent, initialStateMock, outcomeStateMock);
 transition2.addGuardCondition(andPredicate);
 transition2.addGuardCondition(orPredicate);

 assertEquals(transition, transition);
 assertNotEquals(transition, new Object());
 assertEquals(transition, transition2);
 }

 @Test
 public void testHashCode() {
 Transition transition2 = new Transition("Transition Name", triggerEvent, initialStateMock, outcomeStateMock);
 transition2.addGuardCondition(andPredicate);
 transition2.addGuardCondition(orPredicate);

 int hashcode1 = transition.hashCode();
 int hashcode2 = transition2.hashCode();

 assertNotSame(hashcode1, hashcode2);
 }

 @Test
 public void testToString() {
 String expectedTransitionToString = "Transition{name='Transition Name', initialState=MockState, outcomeState=MockState, triggerEvent=MockEvent, guardConditions=[And{conjunctions=[Predicate{class=class uk.me.jockmacmad.jstate.predicate.AndPredicate}, Predicate{class=class uk.me.jockmacmad.jstate.predicate.OrPredicate}]}, Or{conjunctions=[Predicate{class=class uk.me.jockmacmad.jstate.predicate.AndPredicate}, Predicate{class=class uk.me.jockmacmad.jstate.predicate.OrPredicate}]}}";
 String transitionToString = transition.toString();

 assertEquals(expectedTransitionToString, transitionToString);
 }

 @Test
 public void testTrigger_InitialStateAndEventMatches_NoGuards_TransitionToOutcomeState() {
 Mockito.when(initialStateMock.equals(currentStateMock)).thenReturn(true);
 Mockito.when(initialStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(true);
 Mockito.when(outcomeStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(true);

 IState resultState = transition.trigger(currentStateMock, triggerEvent);

 assertEquals(outcomeStateMock, resultState);
 }

 @Test
 public void testTrigger_InitialStateMatchesButEventDoesntMatch_NoGuards_DoesntTransition() {
 Mockito.when(initialStateMock.equals(currentStateMock)).thenReturn(true);
 Mockito.when(initialStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(true);
 Mockito.when(outcomeStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(false);

 IState resultState = transition.trigger(currentStateMock, triggerEvent);

 assertNotSame(outcomeStateMock, resultState);
 }

 @Test
 public void testTrigger_InitialStateAndEventMatches_GuardsPass_TransitionToOutcomeState() {
 Mockito.when(initialStateMock.equals(currentStateMock)).thenReturn(true);
 Mockito.when(initialStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(true);
 Mockito.when(outcomeStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(true);
 AndPredicate andPredicate = (AndPredicate)guardConditionsMock.get(0);
 OrPredicate orPredicate = (OrPredicate)guardConditionsMock.get(1);

 when(andPredicate.evaluate(triggerEvent)).thenReturn(true);
 when(orPredicate.evaluate(triggerEvent)).thenReturn(true);

 IState resultState = transition.trigger(currentStateMock, triggerEvent);

 assertEquals(outcomeStateMock, resultState);
 }

 @Test
 public void testTrigger_InitialStateAndEventMatches_GuardsDoNotPass_DoesntTransition() {
 Mockito.when(initialStateMock.equals(currentStateMock)).thenReturn(true);
 Mockito.when(initialStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(true);
 Mockito.when(outcomeStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(true);
 AndPredicate andPredicate = (AndPredicate)guardConditionsMock.get(0);
 OrPredicate orPredicate = (OrPredicate)guardConditionsMock.get(1);

 when(andPredicate.evaluate(triggerEvent)).thenReturn(false);
 when(orPredicate.evaluate(triggerEvent)).thenReturn(false);

 IState resultState = transition.trigger(currentStateMock, triggerEvent);

 assertNotSame(outcomeStateMock, resultState);
 }

 @Test
 public void testWillTrigger_InitialStateAndEventMatches_GuardsPass_Transitions() {
 Mockito.when(initialStateMock.equals(currentStateMock)).thenReturn(true);
 Mockito.when(initialStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(true);
 Mockito.when(outcomeStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(true);
 AndPredicate andPredicate = (AndPredicate)guardConditionsMock.get(0);
 OrPredicate orPredicate = (OrPredicate)guardConditionsMock.get(1);

 when(andPredicate.evaluate(triggerEvent)).thenReturn(true);
 when(orPredicate.evaluate(triggerEvent)).thenReturn(true);

 boolean willTransition = transition.willTrigger(currentStateMock, triggerEvent);

 assertTrue(willTransition);
 }

 @Test
 public void testWillTrigger_InitialStateAndEventMatches_GuardsDoNotPass_DoesntTransition() {
 Mockito.when(initialStateMock.equals(currentStateMock)).thenReturn(true);
 Mockito.when(initialStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(true);
 Mockito.when(outcomeStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(true);
 AndPredicate andPredicate = (AndPredicate)guardConditionsMock.get(0);
 OrPredicate orPredicate = (OrPredicate)guardConditionsMock.get(1);

 when(andPredicate.evaluate(triggerEvent)).thenReturn(false);
 when(orPredicate.evaluate(triggerEvent)).thenReturn(false);

 boolean willTransition = transition.willTrigger(currentStateMock, triggerEvent);

 assertFalse(willTransition);
 }

 @Test
 public void testWillTrigger_InitialStateDoesntMatch_EventDoesntMatter_DoesntTransition() {
 Mockito.when(initialStateMock.equals(currentStateMock)).thenReturn(false);

 boolean willTransition = transition.willTrigger(currentStateMock, triggerEvent);

 assertFalse(willTransition);
 }

 @Test
 public void testWillTrigger_InitialStateMatchesButEventDoesntMatch_DoesntTransition() {
 Mockito.when(initialStateMock.equals(currentStateMock)).thenReturn(true);
 Mockito.when(initialStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(true);
 Mockito.when(outcomeStateMock.equals(ArgumentMatchers.any(IState.class))).thenReturn(true);

 boolean willTransition = transition.willTrigger(currentStateMock, new Event("DifferentEvent"));

 assertFalse(willTransition);
 }

}