package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static uk.me.jockmacmad.jstate.state.GuardConditionTest.*;
import static uk.me.jockmacmad.jstate.state.StateTest.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TransitionTest {

    private List<IGuardCondition> guardConditions;
    private Transition transition;
    private IState initialState;
    private IState outcomeState;

    @Before
    public void setUp() {
        this.guardConditions = new ArrayList<>();
        this.initialState = createState("InitialState");
        this.outcomeState = createState("OutcomeState");
        this.transition = new Transition("TransitionName", new Event("EventName"), this.initialState, this.outcomeState);
    }

    @Test
    public void testEquals() {
        Transition transition2 = new Transition("TransitionName", new Event("EventName"), this.initialState, this.outcomeState);
        assertTrue(this.transition.equals(transition2));

        IState initialState2 = createState("InitialState2");
        Transition transition3 = new Transition("TransitionName", new Event("EventName"), this.initialState, this.outcomeState);
        assertFalse(this.transition.equals(transition3));
        assertFalse(transition3.equals(this.transition));

        Transition transition4 = new Transition("TransitionName2", new Event("EventName"), this.initialState, this.outcomeState);
        assertFalse(this.transition.equals(transition4));
        assertFalse(transition4.equals(this.transition));

        Transition transition5 = new Transition("TransitionName", new Event("EventName2"), this.initialState, this.outcomeState);
        assertFalse(this.transition.equals(transition5));
        assertFalse(transition5.equals(this.transition));
    }

    @Test
    public void testHashCode() {
        Transition transition2 = new Transition("TransitionName", new Event("EventName"), this.initialState, this.outcomeState);
        assertEquals(this.transition.hashCode(), transition2.hashCode());

        IState initialState2 = createState("InitialState2");
        Transition transition3 = new Transition("TransitionName", new Event("EventName"), this.initialState, this.outcomeState);
        assertNotSame(this.transition.hashCode(), transition3.hashCode());

        Transition transition4 = new Transition("TransitionName2", new Event("EventName"), this.initialState, this.outcomeState);
        assertNotSame(this.transition.hashCode(), transition4.hashCode());

        Transition transition5 = new Transition("TransitionName", new Event("EventName"), this.initialState, this.outcomeState);
        transition5.addGuardCondition(alwaysTrue());
        this.transition.addGuardCondition(alwaysTrue());
        assertEquals(this.transition.hashCode(), transition5.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("TransitionName[initialState=InitialState, outcomeState=OutcomeState, triggerEvent=EventName]", this.transition.toString());
    }

    @Test
    public void testTrigger() {
        assertEquals(this.outcomeState, this.transition.trigger(this.initialState, new Event("EventName")));

        this.transition.addGuardCondition(alwaysFalse());
        assertNull(this.transition.trigger(this.initialState, new Event("EventName")));

        this.transition.addGuardCondition(alwaysTrue());
        this.transition.addGuardCondition(new GuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("EventName2");
            }
        });
        assertNull(this.transition.trigger(this.initialState, new Event("EventName")));
        assertEquals(this.outcomeState, this.transition.trigger(this.initialState, new Event("EventName2")));
    }

    @Test
    public void testWillTrigger() {
        assertFalse(this.transition.willTrigger(this.initialState, new Event("EventName")));
        assertFalse(this.transition.willTrigger(this.initialState, new Event("EventName2")));

        this.transition.addGuardCondition(alwaysTrue());
        assertTrue(this.transition.willTrigger(this.initialState, new Event("EventName")));

        this.transition.addGuardCondition(new GuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("EventName3");
            }
        });
        assertFalse(this.transition.willTrigger(this.initialState, new Event("EventName")));
        assertFalse(this.transition.willTrigger(this.initialState, new Event("EventName3")));
    }

    @Test
    public void testGuardConditions() {
        Transition transition = new Transition("TransitionName", new Event("EventName"), this.initialState, this.outcomeState);
        transition.addGuardCondition(alwaysTrue());
        transition.addGuardCondition(new GuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return event.getName().equals("EventName2");
            }
        });

        List<String> expectedGuardConditions = Arrays.asList("alwaysTrue", "GuardCondition2");
        List<String> actualGuardConditions = new ArrayList<>();
        for (IGuardCondition condition : transition.getGuardConditions()) {
            actualGuardConditions.add(condition.getClass().getSimpleName());
        }
        assertEquals(expectedGuardConditions, actualGuardConditions);

        transition.removeGuardCondition(transition.getGuardConditions().get(1));
        actualGuardConditions.clear();
        for (IGuardCondition condition : transition.getGuardConditions()) {
            actualGuardConditions.add(condition.getClass().getSimpleName());
        }
        expectedGuardConditions = Arrays.asList("alwaysTrue");
        assertEquals(expectedGuardConditions, actualGuardConditions);
    }
}