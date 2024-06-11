package uk.me.jockmacmad.jstate.state;
 import static org.junit.Assert.*;
 import uk.me.jockmacmad.jstate.predicate.*;
 import org.apache.commons.lang.builder.EqualsBuilder;
 import org.apache.commons.lang.builder.HashCodeBuilder;
 import org.junit.Test;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.List;

 public class TransitionTest {

 private Transition validTransition;
 private Transition invalidTransition1;
 private Transition invalidTransition2;
 private IState initialState;
 private IState outcomeState;
 private Event validEvent;
 private Event invalidEvent;

 @Before
 public void setUp() {
 InitialState initialStateMock = mock(InitialState.class);
 OutcomeState outcomeStateMock = mock(OutcomeState.class);
 validEvent = new Event("ValidEvent");
GuardCondition guardConditionMock1 = mock(GuardCondition.class);
GuardCondition guardConditionMock2 = mock(GuardCondition.class);

 validTransition = new Transition("ValidTransition", validEvent, initialStateMock, outcomeStateMock);
 validTransition.addGuardCondition(guardConditionMock1);
 validTransition.addGuardCondition(guardConditionMock2);

 initialState = new InitialState();
 outcomeState = new OutcomeState();

 invalidTransition1 = new Transition("InvalidTransition1", validEvent, outcomeState, initialState);
 invalidTransition2 = new Transition("InvalidTransition2", new Event("InvalidEvent"), initialState, outcomeState);
 }

 @Test
 public void testEquals() {
 Transition equalTransition = new Transition("ValidTransition", validEvent, initialState, outcomeState);
 assertTrue(validTransition.equals(equalTransition));
 assertTrue(equalTransition.equals(validTransition));
 }

 @Test
 public void testHashCode() {
 Transition equalTransition = new Transition("ValidTransition", validEvent, initialState, outcomeState);
 assertEquals(validTransition.hashCode(), equalTransition.hashCode());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullInitialState() {
 Transition transition = new Transition("Transition", validEvent, null, outcomeState);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullOutcomeState() {
 Transition transition = new Transition("Transition", validEvent, initialState, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullTriggerEvent() {
 Transition transition = new Transition("Transition", null, initialState, outcomeState);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition1() {
 Transition transition = new Transition("Transition", validEvent, initialState, outcomeState);
 transition.addGuardCondition(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullGuardCondition2() {
 Transition transition = new Transition("Transition", validEvent, initialState, outcomeState);
 List<IGuardCondition> guardConditions = new ArrayList<>();
 transition.addGuardCondition(guardConditions);
 }

 @Test
 public void testTriggerWithValidInitialStateAndEvent() {
 assertEquals(outcomeState, validTransition.trigger(initialState, validEvent));
 }

 @Test
 public void testTriggerWithInvalidInitialStateAndEvent() {
 assertNotEquals(outcomeState, invalidTransition1.trigger(initialState, validEvent));
 }

 @Test
 public void testTriggerWithValidInitialStateAndInvalidEvent() {
 assertNotEquals(outcomeState, validTransition.trigger(initialState, invalidEvent));
 }

 @Test
 public void testTriggerWithInvalidInitialStateAndValidEvent() {
 assertNotEquals(outcomeState, invalidTransition2.trigger(initialState, validEvent));
 }

 @Test
 public void testTriggerWithValidInitialStateAndValidEventButFailedGuards() {
 List<IGuardCondition> guardConditions = Arrays.asList(guardConditionMock1, guardConditionMock2);
 when(guardConditionMock1.evaluate(validEvent)).thenReturn(false);
 when(guardConditionMock2.evaluate(validEvent)).thenReturn(false);
 assertNotEquals(outcomeState, validTransition.trigger(initialState, validEvent));
 }

 @Test
 public void testWillTriggerWithValidInitialStateAndEvent() {
 assertTrue(validTransition.willTrigger(initialState, validEvent));
 }

 @Test
 public void testWillTriggerWithInvalidInitialStateAndEvent() {
 assertFalse(invalidTransition1.willTrigger(initialState, validEvent));
 }

 @Test
 public void testWillTriggerWithValidInitialStateAndInvalidEvent() {
 assertFalse(validTransition.willTrigger(initialState, invalidEvent));
 }

 @Test
 public void testWillTriggerWithInvalidInitialStateAndValidEventButFailedGuards() {
 List<IGuardCondition> guardConditions = Arrays.asList(guardConditionMock1, guardConditionMock2);
 when(guardConditionMock1.evaluate(validEvent)).thenReturn(false);
 when(guardConditionMock2.evaluate(validEvent)).thenReturn(false);
 assertFalse(validTransition.willTrigger(initialState, validEvent));
 }

 @Test
 public void testToString() {
 String expected = "Transition[name=ValidTransition, initialState=InitialState@1b6d3582, outcomeState=OutcomeState@29b576d, triggerEvent=ValidEvent, guardConditions=[]]";
 assertEquals(expected, validTransition.toString());
 }
 }