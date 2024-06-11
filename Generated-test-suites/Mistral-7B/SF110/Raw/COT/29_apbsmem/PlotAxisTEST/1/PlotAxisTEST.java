package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

 private PlotAxis axis;
 private PlotAxisScale scale;
 private NumberFormat numberFormat;

 @Test
 public void testPlotAxisConstructor() {
 axis = new PlotAxis();
 assertNotNull(axis);
 assertSame(1.0, axis.getxA());
 assertSame(1.0, axis.getxB());
 assertNaN(axis.getALB());
 assertNaN(axis.getAUB());
 assertSame(1, axis.getGridStyle());
 assertSame(LinearAxisScale.class, axis.getScale().getClass());
 assertSame(NumberFormat.getInstance().getClass(), axis.getNumberFormat().getClass());
 assertNull(axis.getLabel());
 }

 @Test
 public void testPlotAxisConstructorWithLabelScaleFormatAndGridStyle() {
 axis = new PlotAxis("TestAxis", new LinearAxisScale(), NumberFormat.getInstance(), 1);
 assertSame("TestAxis", axis.getLabel());
 assertSame(LinearAxisScale.class, axis.getScale().getClass());
 assertSame(NumberFormat.getInstance().getClass(), axis.getNumberFormat().getClass());
 assertSame(1, axis.getGridStyle());
 }

 @Test
 public void testSetLabel() {
 axis = new PlotAxis();
 axis.setLabel("TestLabel");
 assertSame("TestLabel", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 axis = new PlotAxis();
 scale = new LogAxisScale();
 axis.setScale(scale);
 assertSame(scale.getClass(), axis.getScale().getClass());
 }

 @Test
 public void testSetNumberFormat() {
 axis = new PlotAxis();
 numberFormat = new DecimalFormat("#0.##");
 axis.setNumberFormat(numberFormat);
 assertSame(numberFormat.getClass(), axis.getNumberFormat().getClass());
 }

 @Test
 public void testSetGridStyle() {
 axis = new PlotAxis();
 axis.setGridStyle(PlotAxis.kMajorGrid);
 assertSame(PlotAxis.kMajorGrid, axis.getGridStyle());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetScaleNull() {
 axis = new PlotAxis();
 axis.setScale(null);
 }

 @Test
 public void testManualLowerBounds() {
 axis = new PlotAxis();
 axis.manualLowerBounds(true);
 assertTrue(axis.manualLB);
 axis.manualLowerBounds(false);
 assertFalse(axis.manualLB);
 }

 @Test
 public void testSetLowerBounds() {
 axis = new PlotAxis();
 axis.setLowerBounds(5.0);
 assertEquals(5.0, axis.getLowerBounds(), 0.001);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis = new PlotAxis();
 axis.setLowerBounds(5.0);
 final String expected = "5.0";
 assertEquals(expected, axis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 axis = new PlotAxis();
 axis.manualUpperBounds(true);
 assertTrue(axis.manualUB);
 axis.manualUpperBounds(false);
 assertFalse(axis.manualUB);
 }

 @Test
 public void testSetUpperBounds() {
 axis = new PlotAxis();
 axis.setUpperBounds(10.0);
 assertEquals(10.0, axis.getUpperBounds(), 0.001);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis = new PlotAxis();
 axis.setUpperBounds(10.0);
 final String expected = "10.0";
 assertEquals(expected, axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 axis = new PlotAxis();
 axis.manualTickStepSize(true);
 assertTrue(axis.manualQuant);
 axis.manualTickStepSize(false);
 assertFalse(axis.manualQuant);
 }

 @Test
 public void testSetTickStepSize() {
 axis = new PlotAxis();
 axis.setTickStepSize(2.5);
 assertEquals(2.5, axis.getTickStepSize(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTickStepSizeNegative() {
 axis = new PlotAxis();
 axis.setTickStepSize(-1.0);
 }

 @Test
 public void testSetAxisBounds() {
 // Abstract method, cannot be tested directly.
 // This test should be moved to a higher level, e.g. in the test of a class that uses PlotAxis.
 }

 @Test
 public void testSetSize() {
 // Abstract method, cannot be tested directly.
 // This test should be moved to a higher level, e.g. in the test of a class that uses PlotAxis.
 }

 @Test
 public void testResizeAxis() {
 // Abstract method, cannot be tested directly.
 // This test should be moved to a test of a concrete subclass of PlotAxis.
 }

 @Test
 public void testDraw() throws IOException {
 final Graphics g = mock(Graphics.class);
 final Component component = mock(Component.class);
 final Rectangle rectangle = new Rectangle(50, 50, 100, 100);

 // Abstract method, cannot be tested directly.
 // This test should be moved to a test of a concrete subclass of PlotAxis.
 // In the test, create a mock of a concrete subclass and call the draw method with mocks of Graphics, Component and Rectangle.
 }

 @Test
 public void testScaleCoord() {
 axis = new PlotAxis();
 axis.setAxisBounds(null);
 axis.setLowerBounds(0.0);
 axis.setUpperBounds(10.0);
 axis.setTickStepSize(1.0);
 final double input = 5.0;
 final int output = axis.scaleCoord(input);
 assertEquals(3, output);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 final PlotAxis original = new PlotAxis();
 final PlotAxis cloned = (PlotAxis)original.clone();
 assertNotSame(original, cloned);
 assertSame(original.getClass(), cloned.getClass());
 }
}