package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

 private PlotAxis axis;

 @Test
 public void testPlotAxisConstructor() {
     axis = new PlotAxis();
     assertNotNull(axis);
     assertEquals("", axis.getLabel());
     assertSame(LinearAxisScale.class, axis.getScale().getClass());
     assertInstanceOf(NumberFormat.class, axis.getNumberFormat());
     assertEquals(1, axis.getGridStyle());
     assertFalse(axis.isManualLB());
     assertFalse(axis.isManualUB());
     assertFalse(axis.isManualQuant());
     assertEquals(1.0, axis.getxA(), 0.0);
     assertEquals(1.0, axis.getxB(), 0.0);
     assertEquals(Double.MAX_VALUE, axis.getLowerBounds(), 0.0);
     assertEquals(-1.7976931348623157E308, axis.getUpperBounds(), 0.0);
     assertEquals(1.0, axis.getTickStepSize(), 0.0);
 }

 @Test
 public void testPlotAxisLabelConstructor() {
     axis = new PlotAxis("TestAxisLabel", null, null, 1);
     assertNotNull(axis);
     assertEquals("TestAxisLabel", axis.getLabel());
 }

 @Test
 public void testSetLabel() {
     axis = new PlotAxis();
     axis.setLabel("TestLabel");
     assertEquals("TestLabel", axis.getLabel());
 }

 @Test
 public void testSetScale() {
     axis = new PlotAxis();
     PlotAxisScale scale = new LogAxisScale();
     axis.setScale(scale);
     assertSame(scale.getClass(), axis.getScale().getClass());
 }

 @Test
 public void testSetNumberFormat() {
     axis = new PlotAxis();
     DecimalFormat decimalFormat = new DecimalFormat("#.##");
     axis.setNumberFormat(decimalFormat);
     assertInstanceOf(DecimalFormat.class, axis.getNumberFormat());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNumberFormatNull() {
     axis = new PlotAxis();
     axis.setNumberFormat(null);
 }

 @Test
 public void testSetGridStyle() {
     axis = new PlotAxis();
     axis.setGridStyle(2);
     assertEquals(2, axis.getGridStyle());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetGridStyleNegative() {
     axis = new PlotAxis();
     axis.setGridStyle(-1);
 }

 @Test
 public void testManualLowerBounds() {
     axis = new PlotAxis();
     axis.manualLowerBounds(true);
     assertTrue(axis.isManualLB());
     axis.manualLowerBounds(false);
     assertFalse(axis.isManualLB());
 }

 @Test
 public void testSetLowerBounds() {
     axis = new PlotAxis();
     axis.setLowerBounds(1.0);
     assertEquals(1.0, axis.getLowerBounds(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
     axis = new PlotAxis();
     axis.setLowerBounds(1.0);
     assertEquals("1.0", axis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
     axis = new PlotAxis();
     axis.manualUpperBounds(true);
     assertTrue(axis.isManualUB());
     axis.manualUpperBounds(false);
     assertFalse(axis.isManualUB());
 }

 @Test
 public void testSetUpperBounds() {
     axis = new PlotAxis();
     axis.setUpperBounds(1.0);
     assertEquals(1.0, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
     axis = new PlotAxis();
     axis.setUpperBounds(1.0);
     assertEquals("1.0", axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
     axis = new PlotAxis();
     axis.manualTickStepSize(true);
     assertTrue(axis.isManualQuant());
     axis.manualTickStepSize(false);
     assertFalse(axis.isManualQuant());
 }

 @Test
 public void testSetTickStepSize() {
     axis = new PlotAxis();
     axis.setTickStepSize(1.0);
     assertEquals(1.0, axis.getTickStepSize(), 0.0);
 }

 @Test
 public void testSetAxisBounds() {
     // Abstract method, cannot be tested directly
     fail("setAxisBounds is an abstract method");
 }

 @Test
 public void testSetSize() {
     // Abstract method, cannot be tested directly
     fail("setSize is an abstract method");
 }

 @Test
 public void testScaleCoord() {
     axis = new PlotAxis();
     assertEquals(0, axis.scaleCoord(0.0));
 }

 @Test
 public void testClone() {
     axis = new PlotAxis();
     PlotAxis clone = (PlotAxis)axis.clone();
     assertNotSame(axis, clone);
     assertEquals(axis.getClass(), clone.getClass());
 }

}