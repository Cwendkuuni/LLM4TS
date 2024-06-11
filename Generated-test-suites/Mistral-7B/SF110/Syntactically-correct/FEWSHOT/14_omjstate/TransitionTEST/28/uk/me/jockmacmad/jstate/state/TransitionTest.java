package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;

public class TransitionTest {

    @Test
    public void testEquals() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition1 = new Transition("Transition1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("Transition2", triggerEvent, initialState, outcomeState);
        Transition transition3 = new Transition("Transition3", new Event("DifferentEvent"), initialState, outcomeState);

        assertTrue(transition1.equals(transition1));
        assertTrue(transition1.equals(transition2));
        assertFalse(transition1.equals(transition3));
    }

    @Test
    public void testHashCode() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);

        int hashCode1 = transition.hashCode();
        IState initialState2 = new State("InitialState2");
        IState outcomeState2 = new State("OutcomeState2");
        Event triggerEvent2 = new Event("TriggerEvent2");
        Transition transition2 = new Transition("Transition", triggerEvent2, initialState2, outcomeState2);

        assertNotSame(hashCode1, transition2.hashCode());
    }

    @Test
    public void testToString() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);

        String expected = "Transition(initialState=InitialState, outcomeState=OutcomeState, triggerEvent=TriggerEvent)";
        String actual = transition.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testTrigger() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);

        IState currentState = initialState;
        IState retVal = transition.trigger(currentState, triggerEvent);

        assertEquals(outcomeState, retVal);
    }

    @Test
    public void testTrigger_InitialStateNotMatch() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", triggerEvent, new State("InitialState2"), outcomeState);

        IState currentState = initialState;
        IState retVal = transition.trigger(currentState, triggerEvent);

        assertNotSame(outcomeState, retVal);
    }

    @Test
    public void testTrigger_EventNotMatch() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", new Event("DifferentEvent"), initialState, outcomeState);

        IState currentState = initialState;
        IState retVal = transition.trigger(currentState, triggerEvent);

        assertNotSame(outcomeState, retVal);
    }

    @Test
    public void testTrigger_GuardsNotPassed() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);
        And guardCondition = new And(new Equals(new StringProperty("Property1"), "Value1"), new Equals(new StringProperty("Property2"), "Value2"));
        transition.addGuardCondition(guardCondition);

        IState currentState = initialState;
        IState retVal = transition.trigger(currentState, triggerEvent);

        assertNotSame(outcomeState, retVal);
    }

    @Test
    public void testTrigger_GuardsPassed() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);
        And guardCondition = new And(new Equals(new StringProperty("Property1"), "Value1"), new Equals(new StringProperty("Property2"), "Value2"));
        transition.addGuardCondition(guardCondition);

        IState currentState = new State("InitialState_WithProperties", new Properties() {{
            put("Property1", "Value1");
            put("Property2", "Value2");
        }});
        IState retVal = transition.trigger(currentState, triggerEvent);

        assertEquals(outcomeState, retVal);
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);

        IState currentState = initialState;
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);

        assertFalse(willTrigger);
    }

    @Test
    public void testWillTrigger_InitialStateNotMatch() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", triggerEvent, new State("InitialState2"), outcomeState);

        IState currentState = initialState;
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);

        assertFalse(willTrigger);
    }

    @Test
    public void testWillTrigger_EventNotMatch() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", new Event("DifferentEvent"), initialState, outcomeState);

        IState currentState = initialState;
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);

        assertFalse(willTrigger);
    }

    @Test
    public void testWillTrigger_GuardsNotPassed() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);
        And guardCondition = new And(new Equals(new StringProperty("Property1"), "Value1"), new Equals(new StringProperty("Property2"), "Value2"));
        transition.addGuardCondition(guardCondition);

        IState currentState = initialState;
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);

        assertFalse(willTrigger);
    }

    @Test
    public void testWillTrigger_GuardsPassed() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);
        And guardCondition = new And(new Equals(new StringProperty("Property1"), "Value1"), new Equals(new StringProperty("Property2"), "Value2"));
        transition.addGuardCondition(guardCondition);

        IState currentState = new State("InitialState_WithProperties", new Properties() {{
            put("Property1", "Value1");
            put("Property2", "Value2");
        }});
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);

        assertTrue(willTrigger);
    }

    @Test
    public void testAddGuardCondition() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        And guardCondition1 = new And(new Equals(new StringProperty("Property1"), "Value1"));
        Or guardCondition2 = new Or(new Equals(new StringProperty("Property2"), "Value2"), new Equals(new IntProperty("Age"), 21));
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);

        assertEquals(2, transition.getGuardConditions().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddGuardCondition_NullGuardCondition() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);

        transition.addGuardCondition(null);
    }

    @Test(expected = ClassCastException.class)
    public void testAddGuardCondition_NonGuardCondition() {
        IState initialState = new State("InitialState");
        IState outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TriggerEvent");
        Transition transition = new Transition("Transition", triggerEvent, initialState, outcomeState);

        transition.addGuardCondition(new Object());
    }
}