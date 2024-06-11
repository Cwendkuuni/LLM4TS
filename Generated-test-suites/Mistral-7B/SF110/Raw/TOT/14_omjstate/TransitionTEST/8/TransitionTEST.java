package uk.me.jockmacmad.jstate.state.test;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.*;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {
    private static Transition transition;
    private static IState initialState, outcomeState, currentState;
    private static Event triggerEvent;

    @BeforeClass
    public static void beforeClass() {
        transition = new Transition("TestTransition", new Event("TestEvent"), new DummyState(), new DummyState());
    }

    @BeforeMethod
    public void beforeMethod() {
        initialState = new DummyState();
        outcomeState = new DummyState();
        currentState = new DummyState();
    }

    @AfterMethod
    public void afterMethod() {
        transition = null;
        initialState = null;
        outcomeState = null;
        currentState = null;
    }

    @Test
    public void testTransitionConstructor() {
        // Test for constructor
        assertEquals("TestTransition", transition.getName());
        assertEquals(new Event("TestEvent"), transition.getTriggerEvent());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertTrue(transition.getGuardConditions().isEmpty());
    }

    @Test
    public void testAddGuardCondition() {
        // Test for addGuardCondition methods
        IPredicate predicate = new DummyPredicate();
        transition.addGuardCondition(predicate);

        assertEquals(1, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(predicate));

        IGuardCondition guardCondition = new DummyGuardCondition();
        transition.addGuardCondition(guardCondition);

        assertEquals(2, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(guardCondition));
    }

    @Test
    public void testTransitionTriggerTypicalCase() {
        // Test for trigger method with typical use case
        transition.addGuardCondition(new DummyGuardCondition());

        assertEquals(outcomeState, transition.trigger(initialState, triggerEvent));
    }

    @Test
    public void testTransitionTriggerEdgeCase() {
        // Test for trigger method with edge case
        transition.addGuardCondition(new DummyGuardCondition());
        initialState.setSomeField("edge_case_value");

        assertEquals(outcomeState, transition.trigger(initialState, triggerEvent));
    }

    @Test(expected = AssertionError.class)
    public void testTransitionTriggerErrorScenario() {
        // Test for trigger method with error scenario
        transition.addGuardCondition(new DummyGuardCondition());

        transition.trigger(null, triggerEvent);
    }

    @Test
    public void testTransitionWillTriggerTypicalCase() {
        // Test for willTrigger method with typical use case
        transition.addGuardCondition(new DummyGuardCondition());

        assertTrue(transition.willTrigger(initialState, triggerEvent));
    }

    @Test
    public void testTransitionWillTriggerEdgeCase() {
        // Test for willTrigger method with edge case
        transition.addGuardCondition(new DummyGuardCondition());
        initialState.setSomeField("edge_case_value");

        assertTrue(transition.willTrigger(initialState, triggerEvent));
    }

    @Test
    public void testTransitionWillTriggerFalseCase() {
        // Test for willTrigger method with false case
        transition.addGuardCondition(new DummyGuardCondition());

        currentState = new DummyState();
        currentState.setSomeField("wrong_event");

        assertFalse(transition.willTrigger(currentState, triggerEvent));
    }

    @Test
    public void testToString() {
        // Test for toString method
        transition.addGuardCondition(new DummyGuardCondition());

        String expectedToString = "Transition{name='TestTransition', triggerEvent=TestEvent, initialState="
                + ArrayUtils.toString(initialState.getFieldNames())
                + ", outcomeState="
                + ArrayUtils.toString(outcomeState.getFieldNames())
                + ", guardConditions="
                + ArrayUtils.toString(transition.getGuardConditions().toArray())
                + '}';

        assertEquals(expectedToString, transition.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Test for equals and hashCode methods
        Transition transition2 = new Transition("TestTransition2", new Event("TestEvent2"), initialState, outcomeState);
        Transition transition3 = new Transition("TestTransition", new Event("TestEvent"), initialState, outcomeState);

        assertTrue(transition.equals(transition3));
        assertTrue(transition3.equals(transition));
        assertEquals(hashCode(transition), hashCode(transition3));
        assertEquals(hashCode(transition3), hashCode(transition));
    }

    private int hashCode(Object object) {
        return object != null ? object.hashCode() : 0;
    }
}