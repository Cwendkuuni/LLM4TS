package uk.me.jockmacmad.jstate.state;

import org.junit.*;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

@Before
public void setup() {
// Initialize test objects
initialState = new ConcreteState();
outcomeState = new ConcreteState();
Transition transition = new Transition("testTransition", new Event("testEvent"), initialState, outcomeState);
}

@After
public void teardown() {
// Clear test objects
initialState = null;
outcomeState = null;
transition = null;
}

private IState initialState;
private IState outcomeState;
private Transition transition;

@Test
public void testTrigger() {
// Given
IState currentState = initialState;

// When
IState result = transition.trigger(currentState, new Event("testEvent"));

// Then
assertEquals(outcomeState, result);
}

@Test
public void testWillTrigger() {
// Given
IState currentState = initialState;

// When
boolean result = transition.willTrigger(currentState, new Event("testEvent"));

// Then
assertFalse(result);

// Given the same state and event, but with guard conditions met
initialState.setGuardCondition(new ConcreteGuardCondition());
outcomeState.setGuardCondition(new ConcreteGuardCondition());
transition.addGuardCondition(new ConcreteGuardCondition());

// When
result = transition.willTrigger(currentState, new Event("testEvent"));

// Then
assertTrue(result);
}

@Test(expected = IllegalArgumentException.class)
public void testConstructorWithNullName() {
// Given
Event triggerEvent = new Event("testEvent");
IState initialState = new ConcreteState();
IState outcomeState = new ConcreteState();

// When
Transition transition = new Transition(null, triggerEvent, initialState, outcomeState);

// Then an exception is thrown
}

@Test(expected = IllegalArgumentException.class)
public void testConstructorWithNullInitialState() {
// Given
String name = "testTransition";
Event triggerEvent = new Event("testEvent");
IState initialState = null;
IState outcomeState = new ConcreteState();

// When
Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

// Then an exception is thrown
}

@Test(expected = IllegalArgumentException.class)
public void testConstructorWithNullOutcomeState() {
// Given
String name = "testTransition";
Event triggerEvent = new Event("testEvent");
IState initialState = new ConcreteState();
IState outcomeState = null;

// When
Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

// Then an exception is thrown
}
}