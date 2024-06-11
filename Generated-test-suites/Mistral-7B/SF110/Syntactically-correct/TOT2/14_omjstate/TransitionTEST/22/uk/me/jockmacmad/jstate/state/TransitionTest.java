package uk.me.jockmacmad.jstate.state;

import org.junit.*;
import org.junit.Assert.*;
import static org.junit.Assert.*;

public class TransitionTest {
   private IState initialState;
   private IState outcomeState;
   private Event triggerEvent;
   private Transition transition;

   @Before
   public void setUp() {
      initialState = new State("InitialState");
      outcomeState = new State("OutcomeState");
      triggerEvent = new Event("TriggerEvent");
      transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
   }

   @Test
   public void testAddGuardCondition() {
      IPredicate predicate = new Predicate() {};
      transition.addGuardCondition(predicate);
      List<IGuardCondition> guardConditions = transition.getGuardConditions();
      assertTrue(guardConditions.contains(predicate));
   }

   @Test
   public void testTrigger() {
      IState currentState = initialState;
      IState expectedOutcome = outcomeState;
      IState actualOutcome = transition.trigger(currentState, triggerEvent);

      assertEquals(expectedOutcome, actualOutcome);
   }

   @Test(expected = AssertionError.class)
   public void testEdgeCasesAndExceptions() {
      IState initialState = null;
      IState outcomeState = new State("OutcomeState");
      Event triggerEvent = new Event("TriggerEvent");
      Transition transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);

      IState currentState = new State("CurrentState");
      IState expectedOutcome = outcomeState;

      transition.trigger(currentState, triggerEvent);
   }

   @Test
   public void testWillTrigger() {
      IState currentState = initialState;
      boolean expectedResult = true;
      boolean actualResult = transition.willTrigger(currentState, triggerEvent);

      assertEquals(expectedResult, actualResult);
   }

   @After
   public void tearDown() {
      initialState = null;
      outcomeState = null;
      triggerEvent = null;
      transition = null;
   }
}