package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;
import uk.me.jockmacmad.jstate.predicate.PredicateStub;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private List<IPredicate> guardConditions;

 @Before
 public void setUp() {
 initialState = Mockito.mock(IState.class);
 outcomeState = Mockito.mock(IState.class);
 triggerEvent = new Event("TestEvent");
 guardConditions = new ArrayList<>();
 }

 @Test
 public void testTransition() {
 // Test constructor
 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 assertNotNull(transition);
 assertEquals("TransitionName", transition.getName());
 assertEquals(initialState, transition.getInitialState());
 assertEquals(outcomeState, transition.getOutcomeState());
 assertEquals(triggerEvent, transition.getTriggerEvent());

 // Test equals and hashCode
 Transition transition2 = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 assertTrue(transition.equals(transition2));
 assertEquals(transition.hashCode(), transition2.hashCode());

 Transition transition3 = new Transition("TransitionName2", triggerEvent, initialState, outcomeState);
 assertFalse(transition.equals(transition3));
 assertFalse(transition.hashCode() == transition3.hashCode());

 // Test addGuardCondition
 IPredicate guardCondition = new PredicateStub(true);
 transition.addGuardCondition(guardCondition);
 assertEquals(1, transition.getGuardConditions().size());
 assertEquals(guardCondition, transition.getGuardConditions().get(0));

 // Test evaluateGuardConditions
 transition.evaluateGuardConditions();
 // This method is empty by design, so no test is needed

 // Test trigger
 assertTrue(transition.willTrigger(initialState, triggerEvent));
 assertEquals(initialState, transition.trigger(initialState, triggerEvent));
 assertEquals(initialState, transition.trigger(outcomeState, triggerEvent));
 assertEquals(outcomeState, transition.trigger(initialState, new Event("NotTriggerEvent")));

 // Test toString
 assertEquals(ToStringBuilder.reflectionToString(transition), transition.toString());
 }

 @Test
 public void testTransitionWithMultipleGuards() {
 // Arrange
 IPredicate guardCondition1 = Mockito.mock(IPredicate.class);
 IPredicate guardCondition2 = Mockito.mock(IPredicate.class);
 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);

 // Act
 Mockito.when(guardCondition1.evaluate(triggerEvent)).thenReturn(true);
 Mockito.when(guardCondition2.evaluate(triggerEvent)).thenReturn(true);

 // Assert
 assertTrue(transition.willTrigger(initialState, triggerEvent));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransitionNullParameters() {
 // Arrange
 transition = new Transition(null, triggerEvent, initialState, outcomeState);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransitionNullInitialState() {
 // Arrange
 transition = new Transition("TransitionName", triggerEvent, null, outcomeState);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransitionNullOutcomeState() {
 // Arrange
 transition = new Transition("TransitionName", triggerEvent, initialState, null);
 }
}