package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import java.util.ArrayList;

public class TransitionTest {

    @Test
    public void testTransitionCreation() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        assertEquals(name, transition.getName());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertTrue(transition.getGuardConditions().isEmpty());
    }

    @Test
    public void testAddGuardCondition() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        IPredicate predicate = MockPredicate.createMockPredicate();
        transition.addGuardCondition(predicate);
        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(predicate, transition.getGuardConditions().get(0));

        IGuardCondition guardCondition = new AndGuard(predicate, MockPredicate.createMockPredicate());
        transition.addGuardCondition(guardCondition);
        assertEquals(2, transition.getGuardConditions().size());
        assertEquals(guardCondition, transition.getGuardConditions().get(1));
    }

    @Test(expected = java.lang.UnsupportedOperationException.class)
    public void testAddNullGuardCondition() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        transition.addGuardCondition(null);
    }

    @Test
    public void testEquals() {
        String name1 = "testTransition1";
        Event triggerEvent1 = new Event("testEvent1");
        IState initialState1 = new FakeState("initialState1");
        IState outcomeState1 = new FakeState("outcomeState1");
        Transition transition1 = new Transition(name1, triggerEvent1, initialState1, outcomeState1);

        String name2 = "testTransition2";
        Event triggerEvent2 = new Event("testEvent2");
        IState initialState2 = new FakeState("initialState2");
        IState outcomeState2 = new FakeState("outcomeState2");
        Transition transition2 = new Transition(name2, triggerEvent2, initialState2, outcomeState2);

        assertFalse(transition1.equals(transition2));

        String name3 = name1;
        Event triggerEvent3 = triggerEvent1;
        IState initialState3 = initialState1;
        IState outcomeState3 = outcomeState1;
        Transition transition3 = new Transition(name3, triggerEvent3, initialState3, outcomeState3);
        assertTrue(transition1.equals(transition3));
    }

    @Test
    public void testHashCode() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);
        assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testToString() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        String expected = String.format("Transition{name='%s', triggerEvent=Event{name='testEvent'}, initialState=FakeState{stateName='initialState'}, outcomeState=FakeState{stateName='outcomeState'}, guardConditions=[]}%n", name);
        assertEquals(expected, transition.toString());
    }

    @Test
    public void testTrigger() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertTrue(transition.trigger(initialState, triggerEvent).equals(outcomeState));
        assertFalse(transition.trigger(initialState, new Event("testEvent2")).equals(outcomeState));
        assertFalse(transition.trigger(new FakeState("anotherState"), triggerEvent).equals(outcomeState));
        assertFalse(transition.trigger(initialState, null).equals(outcomeState));
    }

    @Test
    public void testWillTrigger() {
        String name = "testTransition";
        Event triggerEvent = new Event("testEvent");
        IState initialState = new FakeState("initialState");
        IState outcomeState = new FakeState("outcomeState");
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

        assertTrue(transition.willTrigger(initialState, triggerEvent));
        assertFalse(transition.willTrigger(initialState, new Event("testEvent2")));
        assertFalse(transition.willTrigger(new FakeState("anotherState"), triggerEvent));
        assertFalse(transition.willTrigger(initialState, null));
    }
}