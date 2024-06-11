package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import uk.me.jockmacmad.jstate.state.predicate.MockPredicate;
import uk.me.jockmacmad.jstate.state.predicate.MockGuardCondition;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransitionTest {

    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;
    private List<IGuardCondition> guardConditions;

    @Before
    public void setup() {
        initialState = Mockito.mock(IState.class);
        outcomeState = Mockito.mock(IState.class);
        triggerEvent = Mockito.mock(Event.class);
        guardConditions = new ArrayList<>();

        transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
    }

    @Test
    public void testConstructor() {
        assertNotNull(transition);
    }

    @Test
    public void testGetInitialState() {
        assertEquals(initialState, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testAddGuardCondition() {
        MockGuardCondition mockGuardCondition = Mockito.mock(MockGuardCondition.class);
        transition.addGuardCondition(mockGuardCondition);
        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullGuardCondition() {
        transition.addGuardCondition(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullTransition() {
        Transition nullTransition = null;
        transition.addGuardCondition(nullTransition);
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("TransitionName1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("TransitionName2", triggerEvent, initialState, outcomeState);

        assertFalse(EqualsBuilder.reflectionEquals(transition1, transition2));

        Transition transition3 = new Transition("TransitionName1", triggerEvent, initialState, outcomeState);

        assertTrue(EqualsBuilder.reflectionEquals(transition1, transition3));
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("TransitionName1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("TransitionName2", triggerEvent, initialState, outcomeState);

        int hashCode1 = HashCodeBuilder.reflectionHashCode(transition1);
        int hashCode2 = HashCodeBuilder.reflectionHashCode(transition2);

        assertNotEquals(hashCode1, hashCode2);

        Transition transition3 = new Transition("TransitionName1", triggerEvent, initialState, outcomeState);

        assertEquals(hashCode1, HashCodeBuilder.reflectionHashCode(transition3));
    }

    @Test
    public void testToString() {
        String expectedToString = "TransitionName{initialState=MockState, outcomeState=MockState, triggerEvent=MockEvent}";

        String toString = ToStringBuilder.reflectionToString(transition);

        assertEquals(expectedToString, toString);
    }

    @Test
    public void testEvaluateGuardConditions() {
        MockGuardCondition mockGuardCondition1 = Mockito.mock(MockGuardCondition.class);
        MockGuardCondition mockGuardCondition2 = Mockito.mock(MockGuardCondition.class);

        transition.addGuardCondition(mockGuardCondition1);
        transition.addGuardCondition(mockGuardCondition2);

        Mockito.when(mockGuardCondition1.evaluate(triggerEvent)).thenReturn(true);
        Mockito.when(mockGuardCondition2.evaluate(triggerEvent)).thenReturn(true);

        transition.evaluateGuardConditions();

        // No exception thrown, so guards passed
    }

    @Test(expected = RuntimeException.class)
    public void testEvaluateGuardConditionsWithFailingCondition() {
        MockGuardCondition mockGuardCondition1 = Mockito.mock(MockGuardCondition.class);
        MockGuardCondition mockGuardCondition2 = Mockito.mock(MockGuardCondition.class);

        transition.addGuardCondition(mockGuardCondition1);
        transition.addGuardCondition(mockGuardCondition2);

        Mockito.when(mockGuardCondition1.evaluate(triggerEvent)).thenThrow(new RuntimeException("Exception message"));

        try {
            transition.evaluateGuardConditions();
            fail("Expected exception to be thrown");
        } catch (RuntimeException e) {
            // Expected exception thrown
        }
    }

    @Test
    public void testTrigger() {
        MockState mockInitialState = Mockito.mock(IState.class);
        MockState mockOutcomeState = Mockito.mock(IState.class);

        Mockito.when(mockInitialState.equals(initialState)).thenReturn(true);
        Mockito.when(mockInitialState.equals(mockOutcomeState)).thenReturn(false);

        Mockito.when(mockOutcomeState.equals(outcomeState)).thenReturn(true);

        Mockito.when(mockInitialState.equals(mockOutcomeState)).thenReturn(false);

        Mockito.when(mockGuardConditions.isEmpty()).thenReturn(true);

        assertTrue(transition.trigger(mockInitialState, triggerEvent).equals(outcomeState));

        MockGuardCondition mockGuardCondition = Mockito.mock(MockGuardCondition.class);
        transition.addGuardCondition(mockGuardCondition);

        Mockito.when(mockGuardCondition.evaluate(triggerEvent)).thenReturn(false);

        assertFalse(transition.trigger(mockInitialState, triggerEvent).equals(outcomeState));
    }

    @Test
    public void testWillTrigger() {
        MockState mockInitialState = Mockito.mock(IState.class);
        MockState mockOutcomeState = Mockito.mock(IState.class);

        Mockito.when(mockInitialState.equals(initialState)).thenReturn(true);
        Mockito.when(mockInitialState.equals(mockOutcomeState)).thenReturn(false);

        Mockito.when(mockOutcomeState.equals(outcomeState)).thenReturn(true);

        Mockito.when(mockInitialState.equals(mockOutcomeState)).thenReturn(false);

        Mockito.when(mockGuardConditions.isEmpty()).thenReturn(true);

        assertTrue(transition.willTrigger(mockInitialState, triggerEvent));

        MockGuardCondition mockGuardCondition = Mockito.mock(MockGuardCondition.class);
        transition.addGuardCondition(mockGuardCondition);

        Mockito.when(mockGuardCondition.evaluate(triggerEvent)).thenReturn(false);

        assertFalse(transition.willTrigger(mockInitialState, triggerEvent));
    }

    @Test
    public void testWillTriggerWithNullInitialState() {
        assertFalse(transition.willTrigger(null, triggerEvent));
    }

    @Test
    public void testWillTriggerWithNullEvent() {
        assertFalse(transition.willTrigger(initialState, null));
    }
}