package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Before;
import org.junit.Test;

public class TransitionTest {

    private IState initialState;
    private IState outcomeState;
    private List<IGuardCondition> guardConditions;
    private Event triggerEvent;
    private Transition transition;

    @Before
    public void setup() {
        initialState = mock(IState.class);
        outcomeState = mock(IState.class);
        guardConditions = new ArrayList<>();
        triggerEvent = mock(Event.class);
        transition = new Transition("testTransition", triggerEvent, initialState, outcomeState);
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("testTransition", triggerEvent, initialState, outcomeState);

        assertEquals(transition1, transition1);
        assertEquals(transition1, transition2);
        assertEquals(transition2, transition1);

        IState initialStateDifferent = mock(IState.class);
        Transition transitionDifferent = new Transition("testTransition", triggerEvent, initialStateDifferent, outcomeState);

        assertNotEquals(transition1, transitionDifferent);

        Event eventDifferent = mock(Event.class);
        Transition transitionEventDifferent = new Transition("testTransition", eventDifferent, initialState, outcomeState);

        assertNotEquals(transition1, transitionEventDifferent);
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("testTransition", triggerEvent, initialState, outcomeState);

        assertEquals(transition1.hashCode(), transition2.hashCode());

        IState initialStateDifferent = mock(IState.class);
        Transition transitionDifferent = new Transition("testTransition", triggerEvent, initialStateDifferent, outcomeState);

        assertNotEquals(transition1.hashCode(), transitionDifferent.hashCode());

        Event eventDifferent = mock(Event.class);
        Transition transitionEventDifferent = new Transition("testTransition", eventDifferent, initialState, outcomeState);

        assertNotEquals(transition1.hashCode(), transitionEventDifferent.hashCode());
    }

    @Test
    public void testToString() {
        String expectedToString = ReflectionToStringBuilder.reflectionToString(transition, ToStringStyle.SHORT_PREFIX_STYLE);

        String actualToString = transition.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testAddGuardCondition() {
        IPredicate predicate = mock(IPredicate.class);

        transition.addGuardCondition(predicate);

        assertEquals(1, guardConditions.size());
        assertEquals(predicate, guardConditions.get(0));

        transition.addGuardCondition(new ConstantGuardCondition(EventType.START));

        assertEquals(2, guardConditions.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullGuardCondition() {
        transition.addGuardCondition(null);
    }

    @Test
    public void testEvaluateGuardsWhenNoGuards() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        boolean passesGuards = transition.evaluateGuardConditions();

        assertTrue(passesGuards);
    }

    @Test
    public void testEvaluateGuardsWhenGuardsPresent() {
        IPredicate predicate = mock(IPredicate.class);
        when(predicate.evaluate(triggerEvent)).thenReturn(true);
        guardConditions.add(predicate);

        when(initialState.equals(initialState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        boolean passesGuards = transition.evaluateGuardConditions();

        assertTrue(passesGuards);
    }

    @Test
    public void testEvaluateGuardsWhenGuardsFail() {
        IPredicate predicate = mock(IPredicate.class);
        when(predicate.evaluate(triggerEvent)).thenReturn(false);
        guardConditions.add(predicate);

        when(initialState.equals(initialState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        boolean passesGuards = transition.evaluateGuardConditions();

        assertFalse(passesGuards);
    }

    @Test
    public void testTrigger() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        when(guardConditions.isEmpty()).thenReturn(true);

        IState expectedOutcomeState = outcomeState;

        IState actualOutcomeState = transition.trigger(initialState, triggerEvent);

        assertEquals(expectedOutcomeState, actualOutcomeState);

        when(guardConditions.isEmpty()).thenReturn(false);
        IPredicate predicate = mock(IPredicate.class);
        when(predicate.evaluate(triggerEvent)).thenReturn(true);
        guardConditions.add(predicate);

        IState actualOutcomeState2 = transition.trigger(initialState, triggerEvent);

        assertEquals(expectedOutcomeState, actualOutcomeState2);
    }

    @Test
    public void testTriggerWhenGuardsFail() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        when(guardConditions.isEmpty()).thenReturn(false);
        IPredicate predicate = mock(IPredicate.class);
        when(predicate.evaluate(triggerEvent)).thenReturn(false);
        guardConditions.add(predicate);

        IState actualOutcomeState = transition.trigger(initialState, triggerEvent);

        assertNull(actualOutcomeState);
    }

    @Test
    public void testWillTrigger() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        boolean passesGuards = true;

        for (final IGuardCondition ig : guardConditions) {
            passesGuards = ig.evaluate(triggerEvent) && passesGuards;
        }

        boolean expectedWillTrigger = passesGuards;

        boolean actualWillTrigger = transition.willTrigger(initialState, triggerEvent);

        assertEquals(expectedWillTrigger, actualWillTrigger);
    }

    @Test
    public void testWillTriggerWhenGuardsFail() {
        when(initialState.equals(initialState)).thenReturn(true);
        when(triggerEvent.equals(triggerEvent)).thenReturn(true);

        boolean passesGuards = false;

        for (final IGuardCondition ig : guardConditions) {
            passesGuards = ig.evaluate(triggerEvent) && passesGuards;
        }

        boolean expectedWillTrigger = passesGuards;

        boolean actualWillTrigger = transition.willTrigger(initialState, triggerEvent);

        assertEquals(expectedWillTrigger, actualWillTrigger);
    }
}