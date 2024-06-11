package uk.me.jockmacmad.jstate.state.test;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;
 private List<IPredicate> guardConditions;

 @Before
 public void setup() {
 initialState = new State("InitialState");
 outcomeState = new State("OutcomeState");
 triggerEvent = new Event("TriggerEvent");
 guardConditions = new ArrayList<>();

 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testEquals() {
 Transition transition1 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);

 assertEquals(transition, transition1);
 assertEquals(transition1, transition);
 assertEquals(transition, transition2);
 assertEquals(transition2, transition1);

 Transition transition3 = new Transition("TestTransition2", triggerEvent, initialState, outcomeState);
 assertNotEquals(transition, transition3);
 }

 @Test
 public void testHashCode() {
 Transition transition1 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 Transition transition2 = new Transition("TestTransition", triggerEvent, initialState, outcomeState);

 assertEquals(transition.hashCode(), transition1.hashCode());
 }

 @Test
 public void testToString() {
 transition.addGuardCondition(new AndPredicate(new EqualsPredicate("eventAttribute", triggerEvent), new EqualsPredicate("anotherAttribute", new Integer(1))));

 String expectedToString = "Transition{name='TestTransition', triggerEvent=TriggerEvent, initialState=InitialState, outcomeState=OutcomeState, guardConditions=[And{Predicates=[Equals{AttributeName='eventAttribute', AttributeValue=TriggerEvent}, Equals{AttributeName='anotherAttribute', AttributeValue=1}]}}]}";
 String resultToString = transition.toString();

 assertEquals(expectedToString, resultToString);
 }

 @Test
 public void testTrigger() {
 transition.addGuardCondition(new EqualsPredicate("eventAttribute", triggerEvent));

 IState initialState = this.initialState;
 IState outcomeState = this.transition.getOutcomeState();

 assertEquals(initialState, transition.trigger(initialState, triggerEvent));
 assertEquals(outcomeState, transition.trigger(outcomeState, triggerEvent));
 }

 @Test
 public void testWillTrigger() {
 transition.addGuardCondition(new AndPredicate(new EqualsPredicate("eventAttribute", triggerEvent), new EqualsPredicate("anotherAttribute", new Integer(1))));

 IState initialState = this.initialState;
 IState outcomeState = this.transition.getOutcomeState();
 Event event = new Event("TriggerEvent");

 assertFalse(transition.willTrigger(initialState, null));
 assertFalse(transition.willTrigger(initialState, new Event("DifferentEvent")));
 assertTrue(transition.willTrigger(initialState, event));
 assertFalse(transition.willTrigger(outcomeState, event));
 }

 @Test
 public void testAddGuardCondition() {
 IPredicate guardCondition1 = new EqualsPredicate("eventAttribute", triggerEvent);
 IPredicate guardCondition2 = new EqualsPredicate("anotherAttribute", new Integer(1));
 transition.addGuardCondition(guardCondition1);
 transition.addGuardCondition(guardCondition2);

 List<IPredicate> expectedGuardConditions = Arrays.asList(guardCondition1, guardCondition2);
 List<IPredicate> actualGuardConditions = transition.getGuardConditions();

 assertEquals(expectedGuardConditions.size(), actualGuardConditions.size());
 assertArrayEquals(expectedGuardConditions.toArray(new IPredicate[0]), actualGuardConditions.toArray(new IPredicate[0]));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition() {
 transition.addGuardCondition(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddGuardConditionAfterTransitionCreated() {
 transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
 transition.addGuardCondition(new EqualsPredicate("eventAttribute", triggerEvent));

 assertThrows(IllegalStateException.class, () -> transition.addGuardCondition(new EqualsPredicate("eventAttribute", triggerEvent)));
 }
}