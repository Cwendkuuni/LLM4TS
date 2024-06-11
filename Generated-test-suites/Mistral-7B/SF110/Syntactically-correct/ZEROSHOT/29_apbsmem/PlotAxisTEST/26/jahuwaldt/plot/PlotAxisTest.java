package jahuwaldt.plot.test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;
import jahuwaldt.plot.LinearAxisScale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

 private PlotAxis axis;
 private PlotAxisScale scale;

 @Before
 public void setUp() {
 axis = new PlotAxis() {
 @Override
 protected void resizeAxis(Rectangle p0) {
 // Empty method as it's not tested here
 }

 @Override
 public void draw(Graphics p0, Component p1, Rectangle p2) {
 // Empty method as it's not tested here
 }

 @Override
 public void setAxisBounds(PlotRunList p0) {
 // Empty method as it's not tested here
 }
 };
 scale = new LinearAxisScale();
 }

 @After
 public void tearDown() {
 axis = null;
 scale = null;
 }

 @Test
 public void testInitialization() {
 assertEquals("Initialization should set default values", 1.0, axis.xA, 0.01);
 assertEquals("Initialization should set default values", 1.0, axis.xB, 0.01);
 assertEquals("Initialization should set default values", Double.MAX_VALUE, axis.aLB, 0.01);
 assertEquals("Initialization should set default values", -1.7976931348623157E308, axis.aUB, 0.01);
 assertEquals("Initialization should set default values", 1, axis.gridStyle);
 assertNotNull("Initialization should initialize scale", axis.scale);
 assertNotNull("Initialization should initialize number format", axis.nf);
 assertNull("Initialization should not have a label", axis.label);
 }

 @Test
 public void testInitializationWithLabelScaleAndNumberFormat() {
 axis = new PlotAxis("TestLabel", scale, null, 1);
 assertEquals("Initialization with label should set label", "TestLabel", axis.label);
 }

 @Test
 public void testSetLabel() {
 axis.setLabel("NewLabel");
 assertEquals("Setting label should set the label", "NewLabel", axis.label);
 }

 @Test
 public void testSetScale() {
 axis.setScale(scale);
 assertEquals("Setting scale should set the scale", scale, axis.scale);
 }

 @Test
 public void testSetNumberFormat() {
 axis.setNumberFormat(NumberFormat.getInstance());
 assertNotNull("Setting number format should initialize it", axis.nf);
 }

 @Test
 public void testSetGridStyle() {
 axis.setGridStyle(2);
 assertEquals("Setting grid style should set the grid style", 2, axis.gridStyle);
 }

 @Test
 public void testManualLowerBounds() {
 axis.manualLowerBounds(true);
 assertTrue("Manual lower bounds should be enabled", axis.manualLB);
 }

 @Test
 public void testSetLowerBounds() {
 axis.setLowerBounds(5.0);
 assertEquals("Setting lower bounds should set the value", 5.0, axis.aLB, 0.01);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis.setLowerBounds(5.0);
 assertEquals("Lower bounds as string should format the number", "5.0", axis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 axis.manualUpperBounds(true);
 assertTrue("Manual upper bounds should be enabled", axis.manualUB);
 }

 @Test
 public void testSetUpperBounds() {
 axis.setUpperBounds(10.0);
 assertEquals("Setting upper bounds should set the value", 10.0, axis.aUB, 0.01);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis.setUpperBounds(10.0);
 assertEquals("Upper bounds as string should format the number", "10.0", axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 axis.manualTickStepSize(true);
 assertTrue("Manual tick step size should be enabled", axis.manualQuant);
 }

 @Test
 public void testSetTickStepSize() {
 axis.setTickStepSize(2.5);
 assertEquals("Setting tick step size should set the value", 2.5, axis.quantum, 0.01);
 }

 @Test
 public void testTickStepSizeAsString() {
 axis.setTickStepSize(2.5);
 assertEquals("Tick step size as string should format the number", "2.5", axis.nf.format(axis.quantum));
 }
}