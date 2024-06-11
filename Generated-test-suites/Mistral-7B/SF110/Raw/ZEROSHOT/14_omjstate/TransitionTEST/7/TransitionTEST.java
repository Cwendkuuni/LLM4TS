import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.me.jockmacmad.jstate.state.Event;
import uk.me.jockmacmad.jstate.state.IGuardCondition;
import uk.me.jockmacmad.jstate.state.IState;
import uk.me.jockmacmad.jstate.state.StateFactory;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TransitionTest {

 private Transition transition;
 private IState initialState;
 private IState outcomeState;
 private Event triggerEvent;

 @Before
 public void setUp() {
 initialState = StateFactory.createState("InitialState");
 outcomeState = StateFactory.createState("OutcomeState");
 triggerEvent = new Event("TriggerEvent");
 transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
 }

 @Test
 public void testTransitionName() {
 assertEquals("TransitionName", transition.getName());
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
 public void testAddGuardCondition() {
 IGuardCondition guardCondition = Mockito.mock(IGuardCondition.class);
 transition.addGuardCondition(guardCondition);
 assertTrue(transition.getGuardConditions().contains(guardCondition));
 }

 @Test(expected = ClassCastException.class)
 public void testAddIncompatibleGuardCondition() {
 IPredicate incompatibleGuard = Mockito.mock(IPredicate.class);
 transition.addGuardCondition(incompatibleGuard);
 }

 @Test
 public void testEvaluateGuardConditionsEmpty() {
 transition.evaluateGuardConditions();
 // No assertions here, as this method should not throw an exception if the guard list is empty
 }

 @Test
 public void testEvaluateGuardConditionsPass() {
 List<IGuardCondition> guardConditions = new ArrayList<>();
 IGuardCondition guardCondition1 = Mockito.mock(IGuardCondition.class);
 IGuardCondition guardCondition2 = Mockito.mock(IGuardCondition.class);
 guardConditions.add(guardCondition1);
 guardConditions.add(guardCondition2);
 transition.setGuardConditions(guardConditions);

 Mockito.when(guardCondition1.evaluate(triggerEvent)).thenReturn(true);
 Mockito.when(guardCondition2.evaluate(triggerEvent)).thenReturn(true);

 transition.evaluateGuardConditions();
 // No assertions here, as this method should not throw an exception when all guards pass
 }

 @Test
 public void testEvaluateGuardConditionsFail() {
 List<IGuardCondition> guardConditions = new ArrayList<>();
 IGuardCondition guardCondition1 = Mockito.mock(IGuardCondition.class);
 IGuardCondition guardCondition2 = Mockito.mock(IGuardCondition.class);
 guardConditions.add(guardCondition1);
 guardConditions.add(guardCondition2);
 transition.setGuardConditions(guardConditions);

 Mockito.when(guardCondition1.evaluate(triggerEvent)).thenReturn(false);
 Mockito.when(guardCondition2.evaluate(triggerEvent)).thenReturn(false);

 try {
 transition.evaluateGuardConditions();
 fail("Expected GuardEvaluationException");
 } catch (Exception e) {
 // Expected exception
 assertTrue(e instanceof GuardEvaluationException);
 }
 }

 @Test
 public void testTrigger() {
 assertEquals(initialState, transition.trigger(initialState, triggerEvent));
 assertFalse(transition.willTrigger(outcomeState, triggerEvent));

 transition.addGuardCondition(Mockito.mock(IGuardCondition.class));
 assertEquals(initialState, transition.trigger(initialState, triggerEvent));
 assertFalse(transition.willTrigger(outcomeState, triggerEvent));

 Mockito.when(transition.getGuardConditions().get(0).evaluate(triggerEvent)).thenReturn(true);
 assertEquals(outcomeState, transition.trigger(initialState, triggerEvent));
 assertTrue(transition.willTrigger(initialState, triggerEvent));
 }

 @Test
 public void testToString() {
 String expectedToString = "TransitionName [InitialState=>OutcomeState, TriggerEvent]";
 assertEquals(expectedToString, transition.toString());
 }
}