import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.EventState;
import org.apache.commons.math3.ode.events.DefaultEventHandler;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.events.AbstractStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private ExpandableStatefulODE ode;
 private StepHandler stepHandler;
 private EventHandler eventHandler;
 private List<EventState> eventStates;

 @Before
 public void setUp() {
 integrator = new AbstractIntegrator("Test Integrator") {
 @Override
 public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Override the integrate method for testing purposes, but leave the abstract method call to ensure correctness
 assertEquals(t, equations.getTime(), 1e-12);
 }
 };
 ode = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] ydot) {
 ydot[0] = y[0] * 2;
 }
 });
 stepHandler = new StepHandler() {
 @Override
 public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
 assertEquals(isLastStep, interpolator.isForward());
 }
 };
 eventHandler = new DefaultEventHandler() {
 @Override
 public void handleEvent(double time, double[] state) {
 fail("Event handler should not be called in this test");
 }
 };
 eventStates = new ArrayList<>();
 integrator.addEventHandler(eventHandler, 0.1, 1e-6, 10);
 }

 @Test
 public void testAddStepHandler() {
 integrator.addStepHandler(stepHandler);
 integrator.getStepHandlers().forEach(h -> assertEquals(stepHandler, h));
 }

 @Test
 public void testGetStepHandlers() {
 assertEquals(Collections.singletonList(stepHandler), integrator.getStepHandlers());
 }

 @Test
 public void testClearStepHandlers() {
 integrator.clearStepHandlers();
 assertTrue(integrator.getStepHandlers().isEmpty());
 }

 @Test
 public void testAddEventHandler() {
 integrator.addEventHandler(eventHandler, 0.1, 1e-6, 10);
 assertEquals(Arrays.asList(eventHandler), integrator.getEventHandlers());
 }

 @Test
 public void testGetEventHandlers() {
 assertEquals(Arrays.asList(eventHandler), integrator.getEventHandlers());
 }

 @Test
 public void testClearEventHandlers() {
 integrator.clearEventHandlers();
 assertTrue(integrator.getEventHandlers().isEmpty());
 }

 @Test
 public void testGetCurrentStepStart() {
 integrator.initIntegration(0.0, new double[1], 1.0);
 assertEquals(0.0, integrator.getCurrentStepStart(), 1e-12);
 }

 @Test
 public void testGetCurrentSignedStepsize() {
 integrator.initIntegration(0.0, new double[1], 1.0);
 assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 1e-12);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxEvaluationsNegative() {
 integrator.setMaxEvaluations(-1);
 }

 @Test
 public void testGetMaxEvaluations() {
 integrator.setMaxEvaluations(100);
 assertEquals(100, integrator.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 integrator.setMaxEvaluations(10);
 integrator.evaluate(ode, 0.0, new double[1], 1.0, new double[1]);
 assertEquals(1, integrator.getEvaluations());
 }

 @Test
 public void testIntegrate() throws Exception {
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(ode);
 integrator.integrate(ode, 1.0);
 double[] expectedY = new double[] { 1.0 };
 double[] actualY = new double[ode.getDimension()];
 integrator.getEvaluationsCounter().getPrimaryState(actualY);
 assertArrayEquals(expectedY, actualY);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateDimensionMismatch() throws Exception {
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 2;
 }
 }));
 integrator.integrate(ode, 1.0);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateNumberIsTooSmall() throws Exception {
 integrator.setMaxEvaluations(1);
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(ode);
 integrator.integrate(ode, 1.0);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateMaxCountExceeded() throws Exception {
 integrator.setMaxEvaluations(1);
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(ode);
 integrator.integrate(ode, 1.0);
 integrator.integrate(ode, 1.0);
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateNoBracketing() throws Exception {
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
 @Override
 public void computeDerivatives(double t, double[] y, double[] ydot) {
 ydot[0] = -y[0];
 }
 }, new double[] { -1.0, 1.0 }));
 integrator.integrate(ode, 0.0);
 }

 @Test
 public void testAcceptStep() throws Exception {
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(ode);
 integrator.addEventHandler(new DefaultEventHandler() {
 @Override
 public void handleEvent(double time, double[] state) {
 eventStates.add(new EventState(eventHandler, time));
 }
 }, 0.5, 1e-6, 10);
 integrator.acceptStep(new AbstractStepInterpolator() {
 @Override
 public double getGlobalCurrentTime() {
 return 0.2;
 }
 }, new double[] { 0.1 }, new double[] { 0.2 }, 0.5);
 assertEquals(2, integrator.getEvaluations());
 assertEquals(1, eventStates.size());
 assertEquals(0.5, eventStates.get(0).getEventTime(), 1e-12);
 }

 @Test
 public void testAcceptStepWithEvent() throws Exception {
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(ode);
 integrator.addEventHandler(new When(0.2, new DefaultEventHandler() {
 @Override
 public void handleEvent(double time, double[] state) {
 eventStates.add(new EventState(eventHandler, time));
 }
 }), 0.5, 1e-6, 10);
 integrator.acceptStep(new AbstractStepInterpolator() {
 @Override
 public double getGlobalCurrentTime() {
 return 0.2;
 }
 }, new double[] { 0.1 }, new double[] { 0.2 }, 0.5);
 assertEquals(2, integrator.getEvaluations());
 assertEquals(1, eventStates.size());
 assertEquals(0.2, eventStates.get(0).getEventTime(), 1e-12);
 }

 @Test
 public void testAcceptStepNoEvent() throws Exception {
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(ode);
 integrator.acceptStep(new AbstractStepInterpolator() {
 @Override
 public double getGlobalCurrentTime() {
 return 0.2;
 }
 }, new double[] { 0.1 }, new double[] { 0.2 }, 0.5);
 assertEquals(2, integrator.getEvaluations());
 assertEquals(0, eventStates.size());
 }

 @Test
 public void testAcceptStepReset() throws Exception {
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(ode);
 integrator.addEventHandler(new AbstractEventHandler() {
 @Override
 public void handleEvent(double time, double[] state) {
 eventStates.add(new EventState(eventHandler, time));
 }

 @Override
 public boolean reset(double time, double[] state) {
 return true;
 }
 }, 0.5, 1e-6, 10);
 integrator.acceptStep(new AbstractStepInterpolator() {
 @Override
 public double getGlobalCurrentTime() {
 return 0.2;
 }
 }, new double[] { 0.1 }, new double[] { 0.2 }, 0.5);
 assertEquals(3, integrator.getEvaluations());
 assertEquals(1, eventStates.size());
 }

 @Test
 public void testAcceptStepWithoutReset() throws Exception {
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(ode);
 integrator.addEventHandler(new AbstractEventHandler() {
 @Override
 public void handleEvent(double time, double[] state) {
 eventStates.add(new EventState(eventHandler, time));
 }

 @Override
 public boolean reset(double time, double[] state) {
 return false;
 }
 }, 0.5, 1e-6, 10);
 integrator.acceptStep(new AbstractStepInterpolator() {
 @Override
 public double getGlobalCurrentTime() {
 return 0.2;
 }
 }, new double[] { 0.1 }, new double[] { 0.2 }, 0.5);
 assertEquals(3, integrator.getEvaluations());
 assertEquals(1, eventStates.size());
 }

}