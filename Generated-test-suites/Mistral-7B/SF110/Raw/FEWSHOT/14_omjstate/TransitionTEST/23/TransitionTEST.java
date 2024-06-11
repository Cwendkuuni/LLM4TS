package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import static uk.me.jockmacmad.jstate.state.Transition.*;

import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.DummyPredicate;
import uk.me.jockmacmad.jstate.predicate.DummyGuardCondition;
import uk.me.jockmacmad.jstate.state.IState;
import uk.me.jockmacmad.jstate.state.Transition;

public class TransitionTest {

    @Test
    public void testEquals() {
        Transition t1 = new Transition("name1", new Event("event1"), new DummyState(), new DummyState());
        Transition t2 = new Transition("name1", new Event("event1"), new DummyState(), new DummyState());
        assertTrue(t1.equals(t2));
        Transition t3 = new Transition("name2", new Event("event1"), new DummyState(), new DummyState());
        assertFalse(t1.equals(t3));
        Transition t4 = null;
        assertFalse(t1.equals(t4));
    }

    @Test
    public void testHashCode() {
        Transition t1 = new Transition("name1", new Event("event1"), new DummyState(), new DummyState());
        Transition t2 = new Transition("name1", new Event("event1"), new DummyState(), new DummyState());
        assertEquals(t1.hashCode(), t2.hashCode());
        Transition t3 = new Transition("name2", new Event("event1"), new DummyState(), new DummyState());
        assertNotEquals(t1.hashCode(), t3.hashCode());
        Transition t4 = null;
        assertNotEquals(t1.hashCode(), t4.hashCode());
    }

    @Test
    public void testToString() {
        Transition t = new Transition("name", new Event("event"), new DummyState(), new DummyState());
        String expected = "Transition[name=name, triggerEvent=Event[event], initialState=DummyState, outcomeState=DummyState]";
        assertEquals(expected, t.toString());
    }

    @Test
    public void testTrigger() {
        Transition t = new Transition("name", new Event("event"), new DummyState(), new DummyState());
        DummyState initialState = new DummyState();
        DummyState outcomeState = new DummyState();
        assertEquals(initialState, t.getInitialState());
        assertEquals(outcomeState, t.getOutcomeState());

        assertNotEquals(outcomeState, t.trigger(initialState, new Event("event"))) ;
        assertEquals(initialState, t.trigger(outcomeState, new Event("event"))) ;
        assertEquals(outcomeState, t.trigger(initialState, new Event("event2"))) ;
        assertEquals(initialState, t.trigger(outcomeState, new Event("event"))) ;
    }

    @Test
    public void testWillTrigger() {
        Transition t = new Transition("name", new Event("event"), new DummyState(), new DummyState());
        DummyState initialState = new DummyState();
        DummyState outcomeState = new DummyState();
        assertEquals(false, t.willTrigger(initialState, new Event("event2"))) ;
        assertEquals(true, t.willTrigger(initialState, new Event("event"))) ;
        assertEquals(false, t.willTrigger(outcomeState, new Event("event"))) ;
        assertEquals(true, t.willTrigger(initialState, new Event("event"))) ;
    }

    @Test
    public void testAddGuardCondition() {
        Transition t = new Transition("name", new Event("event"), new DummyState(), new DummyState());
        assertEquals(0, t.getGuardConditions().size());

        t.addGuardCondition(new DummyGuardCondition());
        assertEquals(1, t.getGuardConditions().size());

        t.addGuardCondition(new DummyGuardCondition());
        assertEquals(2, t.getGuardConditions().size());
    }

    @Test
    public void testEvaluateGuards() {
        Transition t = new Transition("name", new Event("event"), new DummyState(), new DummyState());

        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(new DummyGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("event");
            }
        });

        t.addGuardConditions(guardConditions);

        DummyState initialState = new DummyState();
        Event event = new Event("event");
        assertTrue(t.evaluateGuardConditions());
        assertEquals(outcomeState, t.trigger(initialState, event));
    }
}