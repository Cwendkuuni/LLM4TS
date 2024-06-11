package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import uk.me.jockmacmad.jstate.state.GuardCondition;
import uk.me.jockmacmad.jstate.state.IGuardCondition;
import uk.me.jockmacmad.jstate.state.IState;
import uk.me.jockmacmad.jstate.state.Predicate;
import uk.me.jockmacmad.jstate.state.State;
import uk.me.jockmacmad.jstate.state.Transition;
import uk.me.jockmacmad.jstate.state.event.Event;
import uk.me.jockmacmad.jstate.state.predicate.PredicateFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitionTest {

    @Test
    public void testTransitionConstructor() {
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Trigger Event");
        Transition transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
        assertEquals("Transition Name", transition.getName());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertEquals(0, transition.getGuardConditions().size());
    }

    @Test
    public void testAddGuardCondition() {
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Trigger Event");
        Transition transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
        IGuardCondition guardCondition1 = new GuardCondition("Predicate 1", new PredicateFactory() {
            @Override
            public Predicate createPredicate() {
                return new Predicate() {
                    @Override
                    public boolean evaluate(Event event) {
                        return event.getName().equals("Event 1");
                    }
                };
            }
        });
        transition.addGuardCondition(guardCondition1);
        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(guardCondition1, transition.getGuardConditions().get(0));
        transition.addGuardCondition(guardCondition1);
        assertEquals(1, transition.getGuardConditions().size());
        IGuardCondition guardCondition2 = new GuardCondition("Predicate 2", new PredicateFactory() {
            @Override
            public Predicate createPredicate() {
                return new Predicate() {
                    @Override
                    public boolean evaluate(Event event) {
                        return event.getName().equals("Event 2");
                    }
                };
            }
        });
        transition.addGuardCondition(guardCondition2);
        assertEquals(2, transition.getGuardConditions().size());
    }

    @Test
    public void testEquals() {
        IState initialState1 = new State("Initial State 1");
        IState initialState2 = new State("Initial State 2");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Trigger Event");
        Transition transition1 = new Transition("Transition Name 1", triggerEvent, initialState1, outcomeState);
        Transition transition2 = new Transition("Transition Name 1", triggerEvent, initialState1, outcomeState);
        Transition transition3 = new Transition("Transition Name 1", triggerEvent, initialState2, outcomeState);
        assertTrue(EqualsBuilder.reflectionEquals(transition1, transition2));
        assertFalse(EqualsBuilder.reflectionEquals(transition1, transition3));
    }

    @Test
    public void testHashCode() {
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Trigger Event");
        Transition transition1 = new Transition("Transition Name 1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("Transition Name 2", triggerEvent, initialState, outcomeState);
        int hashCode1 = HashCodeBuilder.reflectionHashCode(transition1);
        int hashCode2 = HashCodeBuilder.reflectionHashCode(transition2);
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Trigger Event");
        Transition transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
        String expectedToString = "Transition Name{initialState=Initial State, outcomeState=Outcome State, triggerEvent=Trigger Event, guardConditions=[], name='Transition Name'}";
        String toString = ToStringBuilder.reflectionToString(transition);
        assertEquals(expectedToString, toString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerNullInitialState() {
        IState initialState = null;
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Trigger Event");
        Transition transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerNullOutcomeState() {
        IState initialState = new State("Initial State");
        IState outcomeState = null;
        Event triggerEvent = new Event("Trigger Event");
        Transition transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTriggerNullEvent() {
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = null;
        Transition transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
    }

    @Test
    public void testTrigger() {
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Trigger Event");
        Transition transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
        assertTrue(transition.willTrigger(initialState, triggerEvent));
        assertEquals(initialState, transition.trigger(initialState, triggerEvent));
        assertFalse(transition.willTrigger(initialState, triggerEvent));
        assertFalse(transition.willTrigger(outcomeState, triggerEvent));
        assertFalse(transition.trigger(outcomeState, triggerEvent) instanceof State);
    }

    @Test
    public void testTriggerDifferentEvents() {
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Trigger Event 1");
        Transition transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
        Event triggerEvent2 = new Event("Trigger Event 2");
        assertTrue(transition.willTrigger(initialState, triggerEvent));
        assertFalse(transition.willTrigger(initialState, triggerEvent2));
        assertFalse(transition.trigger(initialState, triggerEvent2) instanceof State);
    }

    @Test
    public void testTriggerSameState() {
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Trigger Event");
        Transition transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
        assertTrue(transition.willTrigger(initialState, triggerEvent));
        assertTrue(transition.trigger(initialState, triggerEvent) instanceof State);
        assertEquals(outcomeState, transition.trigger(initialState, triggerEvent));
    }

    @Test
    public void testTriggerMultipleGuardConditions() {
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Trigger Event");
        Transition transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
        IGuardCondition guardCondition1 = new GuardCondition("Predicate 1", new PredicateFactory() {
            @Override
            public Predicate createPredicate() {
                return new Predicate() {
                    @Override
                    public boolean evaluate(Event event) {
                        return event.getName().equals("Event 1");
                    }
                };
            }
        });
        IGuardCondition guardCondition2 = new GuardCondition("Predicate 2", new PredicateFactory() {
            @Override
            public Predicate createPredicate() {
                return new Predicate() {
                    @Override
                    public boolean evaluate(Event event) {
                        return event.getName().equals("Event 2");
                    }
                };
            }
        });
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);
        assertTrue(transition.willTrigger(initialState, triggerEvent));
        assertFalse(transition.willTrigger(initialState, new Event("Event 1")));
        assertFalse(transition.willTrigger(initialState, new Event("Event 3")));
        assertTrue(transition.willTrigger(initialState, new Event("Event 1")));
        assertTrue(transition.willTrigger(initialState, new Event("Event 2")));
        assertFalse(transition.willTrigger(initialState, new Event("Event 3")));
        assertFalse(transition.trigger(initialState, new Event("Event 3")) instanceof State);
        assertTrue(transition.trigger(initialState, new Event("Event 1")) instanceof State);
        assertEquals(outcomeState, transition.trigger(initialState, new Event("Event 1")));
        assertTrue(transition.trigger(initialState, new Event("Event 2")) instanceof State);
        assertEquals(outcomeState, transition.trigger(initialState, new Event("Event 2")));
    }

    @Test(expected = IllegalStateException.class)
    public void testTriggerWithoutInitialState() {
        IState initialState = null;
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Trigger Event");
        Transition transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
        transition.trigger(new State("Current State"), triggerEvent);
    }

    @Test(expected = IllegalStateException.class)
    public void testTriggerWithoutGuardConditions() {
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Trigger Event");
        Transition transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
        transition.trigger(initialState, triggerEvent);
    }

    @Test
    public void testToStringWithGuardConditions() {
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Trigger Event");
        Transition transition = new Transition("Transition Name", triggerEvent, initialState, outcomeState);
        IGuardCondition guardCondition1 = new GuardCondition("Predicate 1", new PredicateFactory() {
            @Override
            public Predicate createPredicate() {
                return new Predicate() {
                    @Override
                    public boolean evaluate(Event event) {
                        return event.getName().equals("Event 1");
                    }
                };
            }
        });
        IGuardCondition guardCondition2 = new GuardCondition("Predicate 2", new PredicateFactory() {
            @Override
            public Predicate createPredicate() {
                return new Predicate() {
                    @Override
                    public boolean evaluate(Event event) {
                        return event.getName().equals("Event 2");
                    }
                };
            }
        });
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);
        String expectedToString = "Transition Name{initialState=Initial State, outcomeState=Outcome State, triggerEvent=Trigger Event, guardConditions=[Predicate 1, Predicate 2], name='Transition Name'}";
        String toString = ToStringBuilder.reflectionToString(transition);
        assertEquals(expectedToString, toString);
    }
}