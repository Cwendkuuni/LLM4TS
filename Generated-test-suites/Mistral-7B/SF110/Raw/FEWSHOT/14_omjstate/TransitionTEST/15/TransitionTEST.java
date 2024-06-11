package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import org.junit.Test;

public class TransitionTest {

    @Test
    public void testToString() {
        IState initialState = new DummyState("Initial");
        IState outcomeState = new DummyState("Outcome");
        Event triggerEvent = new DummyEvent("Trigger");
        String name = "TransitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        String expectedToString = "TransitionName{initialState=Initial, outcomeState=Outcome, triggerEvent=Trigger}";
        String actualToString = transition.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testEquals() {
        IState initialState1 = new DummyState("Initial1");
        IState initialState2 = new DummyState("Initial2");
        IState outcomeState = new DummyState("Outcome");
        Event triggerEvent1 = new DummyEvent("Trigger1");
        Event triggerEvent2 = new DummyEvent("Trigger2");
        String name1 = "TransitionName1";
        String name2 = "TransitionName2";

        Transition transition1 = new Transition(name1, triggerEvent1, initialState1, outcomeState);
        Transition transition2 = new Transition(name2, triggerEvent2, initialState2, outcomeState);

        assertFalse(transition1.equals(transition2));

        Transition transition3 = new Transition(name1, triggerEvent1, initialState1, outcomeState);
        assertTrue(transition1.equals(transition3));
    }

    @Test
    public void testHashCode() {
        IState initialState = new DummyState("Initial");
        IState outcomeState = new DummyState("Outcome");
        Event triggerEvent = new DummyEvent("Trigger");
        String name = "TransitionName";

        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);

        int hashCode1 = transition1.hashCode();
        int hashCode2 = transition2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testAddGuardCondition() {
        IState initialState = new DummyState("Initial");
        IState outcomeState = new DummyState("Outcome");
        Event triggerEvent = new DummyEvent("Trigger");
        GuardCondition guardCondition = new DummyGuardCondition();
        String name = "TransitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        transition.addGuardCondition(guardCondition);
        List<IGuardCondition> guardConditions = transition.getGuardConditions();

        assertTrue(guardConditions.contains(guardCondition));
    }

    @Test
    public void testEvaluateGuardConditionsEmpty() {
        IState initialState = new DummyState("Initial");
        IState outcomeState = new DummyState("Outcome");
        Event triggerEvent = new DummyEvent("Trigger");
        String name = "TransitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        transition.evaluateGuardConditions();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEvaluateGuardConditionsNotEmpty() {
        IState initialState = new DummyState("Initial");
        IState outcomeState = new DummyState("Outcome");
        Event triggerEvent = new DummyEvent("Trigger");
        GuardCondition guardCondition = new DummyGuardCondition();
        String name = "TransitionName";

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition);
        transition.setGuardConditions(guardConditions);

        transition.evaluateGuardConditions();
    }

    @Test
    public void testTrigger() {
        IState initialState = new DummyState("Initial");
        IState outcomeState = new DummyState("Outcome");
        Event triggerEvent = new DummyEvent("Trigger");
        String name = "TransitionName";
        GuardCondition guardCondition = new DummyGuardCondition();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition);
        transition.setGuardConditions(guardConditions);

        IState result = transition.trigger(initialState, triggerEvent);

        assertSame(outcomeState, result);
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new DummyState("Initial");
        IState outcomeState = new DummyState("Outcome");
        Event triggerEvent = new DummyEvent("Trigger");
        String name = "TransitionName";
        GuardCondition guardCondition = new DummyGuardCondition();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition);
        transition.setGuardConditions(guardConditions);

        boolean result = transition.willTrigger(initialState, triggerEvent);

        assertTrue(result);
    }

    @Test
    public void testWillTriggerDifferentInitialState() {
        IState initialState1 = new DummyState("Initial1");
        IState initialState2 = new DummyState("Initial2");
        IState outcomeState = new DummyState("Outcome");
        Event triggerEvent = new DummyEvent("Trigger");
        String name = "TransitionName";
        GuardCondition guardCondition = new DummyGuardCondition();

        Transition transition = new Transition(name, triggerEvent, initialState1, outcomeState);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition);
        transition.setGuardConditions(guardConditions);

        boolean result = transition.willTrigger(initialState2, triggerEvent);

        assertFalse(result);
    }

    @Test
    public void testWillTriggerDifferentTriggerEvent() {
        IState initialState = new DummyState("Initial");
        IState outcomeState = new DummyState("Outcome");
        Event triggerEvent1 = new DummyEvent("Trigger1");
        Event triggerEvent2 = new DummyEvent("Trigger2");
        String name = "TransitionName";
        GuardCondition guardCondition = new DummyGuardCondition();

        Transition transition = new Transition(name, triggerEvent1, initialState, outcomeState);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition);
        transition.setGuardConditions(guardConditions);

        boolean result = transition.willTrigger(initialState, triggerEvent2);

        assertFalse(result);
    }

    @Test
    public void testWillTriggerWithGuards() {
        IState initialState = new DummyState("Initial");
        IState outcomeState = new DummyState("Outcome");
        Event triggerEvent = new DummyEvent("Trigger");
        String name = "TransitionName";
        GuardCondition guardCondition1 = new DummyGuardCondition();
        GuardCondition guardCondition2 = new DummyGuardCondition();

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition1);
        guardConditions.add(guardCondition2);
        transition.setGuardConditions(guardConditions);

        boolean result = transition.willTrigger(initialState, triggerEvent);

        assertFalse(result);
    }

    @Test
    public void testWillTriggerWithGuardsPassing() {
        IState initialState = new DummyState("Initial");
        IState outcomeState = new DummyState("Outcome");
        Event triggerEvent = new DummyEvent("Trigger");
        String name = "TransitionName";
        GuardCondition guardCondition1 = new DummyGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return true;
            }
        };
        GuardCondition guardCondition2 = new DummyGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return true;
            }
        };

        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition1);
        guardConditions.add(guardCondition2);
        transition.setGuardConditions(guardConditions);

        boolean result = transition.willTrigger(initialState, triggerEvent);

        assertTrue(result);
    }
}