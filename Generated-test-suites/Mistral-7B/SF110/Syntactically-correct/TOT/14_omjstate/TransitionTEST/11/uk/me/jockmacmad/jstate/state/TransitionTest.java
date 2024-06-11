package uk.me.jockmacmad.jstate.state;
 import org.junit.Test;
 import org.junit.Assert.*;
 import uk.me.jockmacmad.jstate.event.Event;
 import uk.me.jockmacmad.jstate.state.IGuardCondition;
 import uk.me.jockmacmad.jstate.state.IState;
 import uk.me.jockmacmad.jstate.state.Transition;
 import uk.me.jockmacmad.jstate.state.DummyState;
 import uk.me.jockmacmad.jstate.state.DummyEvent;

 public class TransitionTest {
     @Test
     public void testTransitionConstructor() {
         String name = "TestTransition";
         Event triggerEvent = new DummyEvent();
         IState initialState = new DummyState();
         IState outcomeState = new DummyState();

         Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

         assertEquals(name, transition.getName());
         assertEquals(triggerEvent, transition.getTriggerEvent());
         assertEquals(initialState, transition.getInitialState());
         assertEquals(outcomeState, transition.getOutcomeState());
         assertEquals(0, transition.getGuardConditions().size());
     }

     @Test
     public void testTriggerMethod() {
         String name = "TestTransition";
         Event triggerEvent = new DummyEvent();
         IState initialState = new DummyState();
         IState outcomeState = new DummyState();
         Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

         IState currentState = initialState;
         IState expectedOutcome = outcomeState;

         IState result = transition.trigger(currentState, triggerEvent);

         assertEquals(expectedOutcome, result);
     }

     @Test
     public void testWillTriggerMethod() {
         String name = "TestTransition";
         Event triggerEvent = new DummyEvent();
         IState initialState = new DummyState();
         IState outcomeState = new DummyState();
         Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);

         boolean expectedResult = false;
         boolean result = transition.willTrigger(initialState, triggerEvent);

         assertEquals(expectedResult, result);

         IState currentState = new DummyState(); // Different state than initial state
         expectedResult = false;
         result = transition.willTrigger(currentState, triggerEvent);

         assertEquals(expectedResult, result);

         IState currentStateWithSameName = new DummyState();
         currentStateWithSameName.setName(initialState.getName());
         expectedResult = true;
         result = transition.willTrigger(currentStateWithSameName, triggerEvent);

         assertEquals(expectedResult, result);
     }
 }