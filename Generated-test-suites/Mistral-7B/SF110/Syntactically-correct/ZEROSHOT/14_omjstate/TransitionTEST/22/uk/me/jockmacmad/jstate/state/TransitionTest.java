package uk.me.jockmacmad.jstate.state;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.DummyPredicate;
import uk.me.jockmacmad.jstate.predicate.DummyGuardCondition;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;

 @Before
 public void setup() {
 initialState = new DummyState("Initial State");
 outcomeState = new DummyState("Outcome State");
 triggerEvent = new DummyEvent("Dummy Event");

 transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testTransitionName() {
 assertEquals("Transition Name", transition.getName());
 }

 @Test
 public void testInitialState() {
 assertEquals(initialState, transition.getInitialState());
 }

 @Test
 public void testOutcomeState() {
 assertEquals(outcomeState, transition.getOutcomeState());
 }

 @Test
 public void testTriggerEvent() {
 assertEquals(triggerEvent, transition.getTriggerEvent());
 }

 @Test
 public void testAddGuardConditionWithIPredicate() {
 List<IGuardCondition> expected = new ArrayList<IGuardCondition>() {{
 add(new DummyGuardCondition("Guard Condition 1"));
 add(new DummyGuardCondition("Guard Condition 2"));
 }};

 transition.addGuardCondition(new DummyPredicate("Predicate 1"));
 transition.addGuardCondition(new DummyGuardCondition("Guard Condition 3"));

 List<IGuardCondition> actual = transition.getGuardConditions();

 assertEquals(expected.size(), actual.size());

 for (int i = 0; i < expected.size(); i++) {
 IGuardCondition expectedGuard = expected.get(i);
 IGuardCondition actualGuard = actual.get(i);

 assertEquals(expectedGuard.getClass(), actualGuard.getClass());
 assertEquals(expectedGuard.toString(), actualGuard.toString());
 }
}

 @Test
 public void testAddGuardConditionWithIGuardCondition() {
 List<IGuardCondition> expected = new ArrayList<IGuardCondition>() {{
 add(new DummyGuardCondition("Guard Condition 1"));
 add(new DummyGuardCondition("Guard Condition 2"));
 add(new DummyGuardCondition("Guard Condition 3"));
 }};

 transition.addGuardCondition(new DummyGuardCondition("Guard Condition 4"));
 transition.addGuardCondition(new DummyGuardCondition("Guard Condition 5"));

 List<IGuardCondition> actual = transition.getGuardConditions();

 assertEquals(expected.size(), actual.size());

 for (int i = 0; i < expected.size(); i++) {
 IGuardCondition expectedGuard = expected.get(i);
 IGuardCondition actualGuard = actual.get(i);

 assertEquals(expectedGuard.getClass(), actualGuard.getClass());
 assertEquals(expectedGuard.toString(), actualGuard.toString());
 }
}

 @Test
 public void testEquals() {
 Transition transition2 = new Transition("Transition Name", triggerEvent, initialState, outcomeState);

 assertFalse(transition.equals(null));
 assertFalse(transition.equals(new Object()));
 assertTrue(transition.equals(transition));
 assertFalse(transition.equals(transition2));
 }

 @Test
 public void testHashCode() {
 int expectedHashCode = HashCodeBuilder.reflectionHashCode(transition);
 int actualHashCode = transition.hashCode();

 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testToString() {
 String expectedToString = new ToStringBuilder(transition, ToStringStyle.SHORT_PREFIX_STYLE)
 .append("Transition Name")
 .append(" (triggerEvent=")
 .append(triggerEvent)
 .append(", initialState=")
 .append(initialState)
 .append(", outcomeState=")
 .append(outcomeState)
 .append(", guardConditions=")
 .append(ArrayUtils.toString(transition.getGuardConditions().toArray()))
 .toString();

 String actualToString = transition.toString();

 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testEvaluateGuardConditions() {
 // This method is empty, so no test is needed
 }

 @Test(expected = IllegalStateException.class)
 public void testTriggerWithInvalidState() {
 IState invalidState = new DummyState("Invalid State");
 transition.trigger(invalidState, triggerEvent);
 }

 @Test(expected = IllegalStateException.class)
 public void testTriggerWithInvalidEvent() {
 Event invalidEvent = new DummyEvent("Invalid Event");
 transition.trigger(initialState, invalidEvent);
 }

 @Test
 public void testTrigger() {
 IState currentState = initialState;
 transition.trigger(currentState, triggerEvent);
 assertEquals(outcomeState, currentState);
 }

 @Test
 public void testWillTrigger() {
 IState currentState = initialState;
 assertTrue(transition.willTrigger(currentState, triggerEvent));
 }

 @Test
 public void testWillTriggerWithInvalidState() {
 IState invalidState = new DummyState("Invalid State");
 assertFalse(transition.willTrigger(invalidState, triggerEvent));
 }

 @Test
 public void testWillTriggerWithInvalidEvent() {
 Event invalidEvent = new DummyEvent("Invalid Event");
 assertFalse(transition.willTrigger(initialState, invalidEvent));
 }
}