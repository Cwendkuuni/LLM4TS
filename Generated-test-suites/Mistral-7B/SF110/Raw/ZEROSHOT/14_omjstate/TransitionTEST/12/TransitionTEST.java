package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.ArrayList;
import java.util.List;

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
 private IPredicate predicate2;

 @InjectMocks
 private Transition transition;

 @Test
 public void testTransitionConstruction() {
 //Given
 String name = "TestTransition";
 //When
 transition = new Transition(name, triggerEvent, initialState, outcomeState);
 //Then
 Mockito.verify(initialState, Mockito.times(1)).equals(initialState);
 Mockito.verify(outcomeState, Mockito.times(1)).equals(outcomeState);
 Mockito.verify(initialState, Mockito.times(1)).equals(transition.getInitialState());
 Mockito.verify(outcomeState, Mockito.times(1)).equals(transition.getOutcomeState());
 Mockito.verify(triggerEvent, Mockito.times(1)).equals(transition.getTriggerEvent());
 }

 @Test
 public void testAddGuardConditionIPredicate() {
 //Given
 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 //When
 transition.addGuardCondition(predicate1);
 transition.addGuardCondition(predicate2);
 //Then
 Mockito.verify(guardConditions, Mockito.times(1)).add(predicate1);
 Mockito.verify(guardConditions, Mockito.times(1)).add(predicate2);
 }

 @Test
 public void testAddGuardConditionIGuardCondition() {
 //Given
 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 IGuardCondition guardCondition = Mockito.mock(IGuardCondition.class);
 //When
 transition.addGuardCondition(guardCondition);
 //Then
 Mockito.verify(guardConditions, Mockito.times(1)).add(guardCondition);
 }

 @Test
 public void testEquals() {
 //Given
 Transition transition1 = new Transition("TestTransition1", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition2", triggerEvent, initialState, outcomeState);
 //When
 //Then
 Mockito.verifyNoMoreInteractions(initialState, outcomeState, triggerEvent);
 Mockito.verify(EqualsBuilder.class, Mockito.times(1)).reflectionEquals(transition1, transition2);
 }

 @Test
 public void testHashCode() {
 //Given
 Transition transition1 = new Transition("TestTransition1", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition2", triggerEvent, initialState, outcomeState);
 //When
 //Then
 Mockito.verifyNoMoreInteractions(initialState, outcomeState, triggerEvent);
 Mockito.verify(HashCodeBuilder.class, Mockito.times(1)).reflectionHashCode(transition1);
 Mockito.verify(HashCodeBuilder.class, Mockito.times(1)).reflectionHashCode(transition2);
 }

 @Test
 public void testToString() {
 //Given
 //When
 //Then
 Mockito.verifyNoMoreInteractions(initialState, outcomeState, triggerEvent);
 Mockito.verify(ToStringBuilder.class, Mockito.times(1)).reflectionToString(transition);
 }

 @Test
 public void testTrigger_StateIsInitialStateAndEventIsTriggerEvent() {
 //Given
 IState currentState = Mockito.mock(IState.class);
 Mockito.when(currentState.equals(initialState)).thenReturn(true);
 Mockito.when(currentState.equals(transition.getInitialState())).thenReturn(true);
Mockito.when(triggerEvent.equals(transition.getTriggerEvent())).thenReturn(true);
 IState expectedOutcomeState = Mockito.mock(IState.class);
 //When
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 //Then
 Mockito.verify(initialState, Mockito.times(1)).equals(currentState);
 Mockito.verify(triggerEvent, Mockito.times(1)).equals(triggerEvent);
 Mockito.verify(currentState, Mockito.times(1)).equals(transition.getInitialState());
 Mockito.verify(outcomeState, Mockito.never()).equals(actualOutcomeState);
 Mockito.verify(initialState, Mockito.times(1)).equals(transition.getInitialState());
 Mockito.verify(outcomeState, Mockito.times(1)).equals(expectedOutcomeState);
 Mockito.verify(actualOutcomeState, Mockito.times(1)).equals(expectedOutcomeState);
 }

 @Test
 public void testTrigger_StateIsInitialStateAndEventIsTriggerEvent_NoGuards() {
 //Given
 IState currentState = Mockito.mock(IState.class);
 Mockito.when(currentState.equals(initialState)).thenReturn(true);
 Mockito.when(currentState.equals(transition.getInitialState())).thenReturn(true);
Mockito.when(triggerEvent.equals(transition.getTriggerEvent())).thenReturn(true);
 IState expectedOutcomeState = Mockito.mock(IState.class);
 Mockito.when(transition.guardConditions.isEmpty()).thenReturn(true);
 //When
 IState actualOutcomeState = transition.trigger(currentState, triggerEvent);
 //Then
 Mockito.verify(initialState, Mockito.times(1)).equals(currentState);
 Mockito.verify(triggerEvent, Mockito.times(1)).equals(triggerEvent);
 Mockito.verify(currentState, Mockito.times(1)).equals(transition.getInitialState());
 Mockito.verify(outcomeState, Mockito.never()).equals(actualOutcomeState);
 Mockito.verify(initialState, Mockito.times(1)).equals(transition.getInitialState());
 Mockito.verify(outcomeState, Mockito.times(1)).equals(expectedOutcomeState);
 Mockito.verify(actualOutcomeState, Mockito.times(1)).equals(expectedOutcomeState);
 }

 @Test
 public void testTrigger_StateIsInitialStateAndEventIsTriggerEvent_GuardsPass() {
 //Given
 IState currentState = Mockito.mock(IState.class);
 Mockito.when(currentState.equals(initialState)).thenReturn(true);
 Mockito.when(currentState.equals(transition.getInitialState())).thenReturn(true);
Mockito.when(triggerEvent.equals(transition.getTriggerEvent())).thenReturn(true);
 IState expectedOutcomeState = Mockito.mock(IState.class);
 IPredicate predicate = Mockito.mock(IPredicate.class);
 Mockito.when(predicate.evaluate(triggerEvent)).thenReturn(true);
 Mockito.when(transition.guardConditions.size()).thenReturn(1);
 Mockito.when(transition.guardConditions.get(0)).thenReturn(predicate);
 IState actualOutcomeState = null;
 //When
 actualOutcomeState = transition.trigger(currentState, triggerEvent);
 //Then
 Mockito.verify(initialState, Mockito.times(1)).equals(currentState);
 Mockito.verify(triggerEvent, Mockito.times(1)).equals(triggerEvent);
 Mockito.verify(currentState, Mockito.times(1)).equals(transition.getInitialState());
 Mockito.verify(outcomeState, Mockito.times(1)).equals(expectedOutcomeState);
 Mockito.verify(initialState, Mockito.times(1)).equals(transition.getInitialState());
 Mockito.verify(outcomeState, Mockito.times(1)).equals(expectedOutcomeState);
 Mockito.verify(actualOutcomeState, Mockito.times(1)).equals(expectedOutcomeState);
 Mockito.verify(predicate, Mockito.times(1)).evaluate(triggerEvent);
 }

 @Test
 public void testTrigger_StateIsInitialStateAndEventIsTriggerEvent_GuardsFail() {
 //Given
 IState currentState = Mockito.mock(IState.class);
 Mockito.when(currentState.equals(initialState)).thenReturn(true);
 Mockito.when(currentState.equals(transition.getInitialState())).thenReturn(true);
Mockito.when(triggerEvent.equals(transition.getTriggerEvent())).thenReturn(true);
 IState expectedOutcomeState = Mockito.mock(IState.class);
 IPredicate predicate = Mockito.mock(IPredicate.class);
 Mockito.when(predicate.evaluate(triggerEvent)).thenReturn(false);
 Mockito.when(transition.guardConditions.size()).thenReturn(1);
 Mockito.when(transition.guardConditions.get(0)).thenReturn(predicate);
 //When
 //Then
 Mockito.verify(initialState, Mockito.times(1)).equals(currentState);
 Mockito.verify(triggerEvent, Mockito.times(1)).equals(triggerEvent);
 Mockito.verify(currentState, Mockito.times(1)).equals(transition.getInitialState());
 Mockito.verify(outcomeState, Mockito.never()).equals(expectedOutcomeState);
 Mockito.verify(initialState, Mockito.times(1)).equals(transition.getInitialState());
 Mockito.verify(outcomeState, Mockito.never()).equals(expectedOutcomeState);
 Mockito.verifyNoMoreInteractions(predicate);
 }

 @Test
 public void testWillTrigger_StateIsInitialStateAndEventIsTriggerEvent() {
 //Given
 IState currentState = Mockito.mock(IState.class);
 Mockito.when(currentState.equals(initialState)).thenReturn(true);
 Mockito.when(currentState.equals(transition.getInitialState())).thenReturn(true);
Mockito.when(triggerEvent.equals(transition.getTriggerEvent())).thenReturn(true);
 //When
 boolean result = transition.willTrigger(currentState, triggerEvent);
 //Then
 Mockito.verify(initialState, Mockito.times(1)).equals(currentState);
 Mockito.verify(triggerEvent, Mockito.times(1)).equals(triggerEvent);
 Mockito.verify(currentState, Mockito.times(1)).equals(transition.getInitialState());
 Mockito.verify(guardConditions, Mockito.never()).size();
 Mockito.verify(guardConditions, Mockito.never()).get(0);
 Mockito.verify(result, Mockito.times(1)).equals(true);
 }

 @Test
 public void testWillTrigger_StateIsInitialStateAndEventIsTriggerEvent_NoGuards() {
 //Given
 IState currentState = Mockito.mock(IState.class);
 Mockito.when(currentState.equals(initialState)).thenReturn(true);
 Mockito.when(currentState.equals(transition.getInitialState())).thenReturn(true);
Mockito.when(triggerEvent.equals(transition.getTriggerEvent())).thenReturn(true);
 //When
 boolean result = transition.willTrigger(currentState, triggerEvent);
 //Then
 Mockito.verify(initialState, Mockito.times(1)).equals(currentState);
 Mockito.verify(triggerEvent, Mockito.times(1)).equals(triggerEvent);
 Mockito.verify(currentState, Mockito.times(1)).equals(transition.getInitialState());
 Mockito.verify(guardConditions, Mockito.never()).size();
 Mockito.verify(guardConditions, Mockito.never()).get(0);
 Mockito.verify(result, Mockito.times(1)).equals(false);
 }

 @Test
 public void testWillTrigger_StateIsInitialStateAndEventIsTriggerEvent_GuardsPass() {
 //Given
 IState currentState = Mockito.mock(IState.class);
 Mockito.when(currentState.equals(initialState)).thenReturn(true);
 Mockito.when(currentState.equals(transition.getInitialState())).thenReturn(true);
Mockito.when(triggerEvent.equals(transition.getTriggerEvent())).thenReturn(true);
 IPredicate predicate = Mockito.mock(IPredicate.class);
 Mockito.when(predicate.evaluate(triggerEvent)).thenReturn(true);
 Mockito.when(transition.guardConditions.size()).thenReturn(1);
 Mockito.when(transition.guardConditions.get(0)).thenReturn(predicate);
 //When
 boolean result = transition.willTrigger(currentState, triggerEvent);
 //Then
 Mockito.verify(initialState, Mockito.times(1)).equals(currentState);
 Mockito.verify(triggerEvent, Mockito.times(1)).equals(triggerEvent);
 Mockito.verify(currentState, Mockito.times(1)).equals(transition.getInitialState());
 Mockito.verify(predicate, Mockito.times(1)).evaluate(triggerEvent);
 Mockito.verify(result, Mockito.times(1)).equals(true);
 }

 @Test
 public void testWillTrigger_StateIsInitialStateAndEventIsTriggerEvent_GuardsFail() {
 //Given
 IState currentState = Mockito.mock(IState.class);
 Mockito.when(currentState.equals(initialState)).thenReturn(true);
 Mockito.when(currentState.equals(transition.getInitialState())).thenReturn(true);
Mockito.when(triggerEvent.equals(transition.getTriggerEvent())).thenReturn(true);
 IPredicate predicate = Mockito.mock(IPredicate.class);
 Mockito.when(predicate.evaluate(triggerEvent)).thenReturn(false);
 Mockito.when(transition.guardConditions.size()).thenReturn(1);
 Mockito.when(transition.guardConditions.get(0)).thenReturn(predicate);
 //When
 //Then
 Mockito.verify(initialState, Mockito.times(1)).equals(currentState);
 Mockito.verify(triggerEvent, Mockito.times(1)).equals(triggerEvent);
 Mockito.verify(currentState, Mockito.times(1)).equals(transition.getInitialState());
 Mockito.verify(predicate, Mockito.never()).evaluate(triggerEvent);
 Mockito.verify(result, Mockito.never()).equals(true);
 }
}