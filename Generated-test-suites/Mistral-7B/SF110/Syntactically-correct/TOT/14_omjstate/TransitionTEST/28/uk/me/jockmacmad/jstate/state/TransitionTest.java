package uk.me.jockmacmad.jstate.state.test;

import org.junit.*;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

private Transition transition;
private IState initialState;
private IState outcomeState;

@Before
public void setup() {
initialState = new StateA();
outcomeState = new StateB();
transition = new Transition("Transition1", new Event("EventA"), initialState, outcomeState);
}

@Test
public void testTrigger_ValidStateAndEvent_ReturnsOutcomeState() {
assertEquals(outcomeState, transition.trigger(initialState, new Event("EventA")));
}

@Test
public void testTrigger_InvalidState_ReturnsInitialState() {
IState invalidState = new StateC();
assertNotEquals(outcomeState, transition.trigger(invalidState, new Event("EventA")));
assertEquals(initialState, transition.trigger(invalidState, new Event("EventA")));
}

@Test
public void testTrigger_InvalidEvent_ReturnsInitialState() {
assertNotEquals(outcomeState, transition.trigger(initialState, new Event("EventB")));
assertEquals(initialState, transition.trigger(initialState, new Event("EventB")));
}

@Test
public void testTrigger_GuardsPass_ReturnsOutcomeState() {
GuardCondition guardCondition = new AndGuardCondition(new Predicate() {
@Override
public boolean evaluate(Event event) {
return event.getName().equals("EventA");
}
}, new Predicate() {
@Override
public boolean evaluate(Event event) {
return event.getPriority() > 1;
}
});
transition.addGuardCondition(guardCondition);
Event validEvent = new Event("EventA", 2);
assertEquals(outcomeState, transition.trigger(initialState, validEvent));
}

@Test
public void testTrigger_GuardsFail_ReturnsInitialState() {
GuardCondition guardCondition = new AndGuardCondition(new Predicate() {
@Override
public boolean evaluate(Event event) {
return event.getName().equals("EventA");
}
}, new Predicate() {
@Override
public boolean evaluate(Event event) {
return event.getPriority() <= 1;
}
});
transition.addGuardCondition(guardCondition);
Event invalidEvent = new Event("EventA", 1);
assertNotEquals(outcomeState, transition.trigger(initialState, invalidEvent));
assertEquals(initialState, transition.trigger(initialState, invalidEvent));
}

@Test(expected = IllegalArgumentException.class)
public void testTrigger_NullInitialState_ThrowsIllegalArgumentException() {
transition = new Transition("Transition1", new Event("EventA"), null, outcomeState);
transition.trigger(initialState, new Event("EventA"));
}

@Test(expected = IllegalArgumentException.class)
public void testTrigger_NullOutcomeState_ThrowsIllegalArgumentException() {
transition = new Transition("Transition1", new Event("EventA"), initialState, null);
transition.trigger(initialState, new Event("EventA"));
}

@Test(expected = IllegalArgumentException.class)
public void testTrigger_NullEvent_ThrowsIllegalArgumentException() {
transition = new Transition("Transition1", null, initialState, outcomeState);
transition.trigger(initialState, new Event("EventA"));
}

@Test
public void testWillTrigger_ValidStateAndEvent_ReturnsTrue() {
assertTrue(transition.willTrigger(initialState, new Event("EventA")));
}

@Test
public void testWillTrigger_InvalidState_ReturnsFalse() {
IState invalidState = new StateC();
assertFalse(transition.willTrigger(invalidState, new Event("EventA")));
}

@Test
public void testWillTrigger_InvalidEvent_ReturnsFalse() {
assertFalse(transition.willTrigger(initialState, new Event("EventB")));
}

@Test
public void testWillTrigger_GuardsPass_ReturnsTrue() {
GuardCondition guardCondition = new AndGuardCondition(new Predicate() {
@Override
public boolean evaluate(Event event) {
return event.getName().equals("EventA");
}
}, new Predicate() {
@Override
public boolean evaluate(Event event) {
return event.getPriority() > 1;
}
});
transition.addGuardCondition(guardCondition);
Event validEvent = new Event("EventA", 2);
assertTrue(transition.willTrigger(initialState, validEvent));
}

@Test
public void testWillTrigger_GuardsFail_ReturnsFalse() {
GuardCondition guardCondition = new AndGuardCondition(new Predicate() {
@Override
public boolean evaluate(Event event) {
return event.getName().equals("EventA");
}
}, new Predicate() {
@Override
public boolean evaluate(Event event) {
return event.getPriority() <= 1;
}
});
transition.addGuardCondition(guardCondition);
Event invalidEvent = new Event("EventA", 1);
assertFalse(transition.willTrigger(initialState, invalidEvent));
}

}