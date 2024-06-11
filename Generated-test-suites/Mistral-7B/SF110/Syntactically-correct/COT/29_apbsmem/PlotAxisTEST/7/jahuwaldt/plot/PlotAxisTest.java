package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

 // Test constructor with no arguments
 @Test
 public void testPlotAxisDefaultConstructor() {
 PlotAxis axis = new PlotAxis();
 assertNotNull(axis);
 assertEquals(1.0, axis.getxA());
 assertEquals(1.0, axis.getxB());
 assertFalse(axis.isManualLB());
 assertFalse(axis.isManualUB());
 assertFalse(axis.isManualQuant());
 assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
 assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
 assertEquals(1, axis.getGridStyle());
 assertNotNull(axis.getScale());
 assertInstanceOf(LinearAxisScale.class, axis.getScale());
 assertNotNull(axis.getNumberFormat());
 assertInstanceOf(DecimalFormat.class, axis.getNumberFormat());
 }

 // Test constructor with label, scale, number format, and grid style
 @Test
 public void testPlotAxisWithLabelAndArgsConstructor() {
 String label = "TestAxis";
 NumberFormat numberFormat = new DecimalFormat("#.##");
 PlotAxisAxisScale axisScale = new PlotAxisAxisScale();
 PlotAxis axis = new PlotAxis(label, axisScale, numberFormat, 1);
 assertNotNull(axis);
 assertEquals(label, axis.getLabel());
 assertInstanceOf(PlotAxisAxisScale.class, axis.getScale());
 assertSame(numberFormat, axis.getNumberFormat());
 assertEquals(1, axis.getGridStyle());
 }

 // Test getLabel() and setLabel() methods
 @Test
 public void testGetLabelAndSetLabel() {
 String label = "TestAxis";
 PlotAxis axis = new PlotAxis();
 axis.setLabel(label);
 assertEquals(label, axis.getLabel());
 }

 // Test getScale() and setScale() methods
 @Test
 public void testGetScaleAndSetScale() {
 PlotAxisAxisScale axisScale = new PlotAxisAxisScale();
 PlotAxis axis = new PlotAxis();
 axis.setScale(axisScale);
 assertSame(axisScale, axis.getScale());
 }

 // Test getNumberFormat() and setNumberFormat() methods with null and valid number formats
 @Test(expected = IllegalArgumentException.class)
 public void testSetNumberFormatWithNullFormat() {
 PlotAxis axis = new PlotAxis();
 axis.setNumberFormat(null);
 }

 @Test
 public void testSetNumberFormatWithValidFormat() {
 DecimalFormat numberFormat = new DecimalFormat("#.##");
 PlotAxis axis = new PlotAxis();
 axis.setNumberFormat(numberFormat);
 assertSame(numberFormat, axis.getNumberFormat());
 }

 // Test getNumberFormat() and setNumberFormat() methods with different number formats
 @Test
 public void testSetNumberFormatWithDifferentFormats() {
 DecimalFormat decimalFormat = new DecimalFormat("#.##");
 NumberFormat percentageFormat = NumberFormat.getPercentInstance();
 PlotAxis axis = new PlotAxis();
 axis.setNumberFormat(decimalFormat);
 assertSame(decimalFormat, axis.getNumberFormat());
 axis.setNumberFormat(percentageFormat);
 assertSame(percentageFormat, axis.getNumberFormat());
 }

 // Test getGridStyle() and setGridStyle() methods
 @Test
 public void testGetGridStyleAndSetGridStyle() {
 PlotAxis axis = new PlotAxis();
 axis.setGridStyle(1);
 assertEquals(1, axis.getGridStyle());
 axis.setGridStyle(2);
 assertEquals(2, axis.getGridStyle());
 }

 // Test manualLowerBounds(boolean) and setLowerBounds(double) methods
 @Test
 public void testManualLowerBoundsAndSetLowerBounds() {
 PlotAxis axis = new PlotAxis();
 axis.setLowerBounds(10.0);
 assertEquals(10.0, axis.getLowerBounds(), 0.0);
 axis.manualLowerBounds(true);
 assertFalse(axis.isManualLB());
 axis.manualLowerBounds(false);
 assertFalse(axis.isManualLB());
 axis.setLowerBounds(5.0);
 assertEquals(5.0, axis.getLowerBounds(), 0.0);
 axis.manualLowerBounds(true);
 assertTrue(axis.isManualLB());
 }

 // Test lowerBoundsAsString() method
 @Test
 public void testLowerBoundsAsString() {
 PlotAxis axis = new PlotAxis();
 axis.setLowerBounds(10.0);
 assertEquals("10.0", axis.lowerBoundsAsString());
 axis.manualLowerBounds(true);
 axis.setLowerBounds(5.0);
 assertEquals("5.0", axis.lowerBoundsAsString());
 }

 // Test manualUpperBounds(boolean) and setUpperBounds(double) methods
 @Test
 public void testManualUpperBoundsAndSetUpperBounds() {
 PlotAxis axis = new PlotAxis();
 axis.setUpperBounds(10.0);
 assertEquals(10.0, axis.getUpperBounds(), 0.0);
 axis.manualUpperBounds(true);
 assertFalse(axis.isManualUB());
 axis.manualUpperBounds(false);
 assertFalse(axis.isManualUB());
 axis.setUpperBounds(5.0);
 assertEquals(5.0, axis.getUpperBounds(), 0.0);
 axis.manualUpperBounds(true);
 assertTrue(axis.isManualUB());
 }

 // Test upperBoundsAsString() method
 @Test
 public void testUpperBoundsAsString() {
 PlotAxis axis = new PlotAxis();
 axis.setUpperBounds(10.0);
 assertEquals("10.0", axis.upperBoundsAsString());
 axis.manualUpperBounds(true);
 axis.setUpperBounds(5.0);
 assertEquals("5.0", axis.upperBoundsAsString());
 }

 // Test manualTickStepSize(boolean) and setTickStepSize(double) methods
 @Test
 public void testManualTickStepSizeAndSetTickStepSize() {
 PlotAxis axis = new PlotAxis();
 axis.setTickStepSize(1.0);
 assertEquals(1.0, axis.getTickStepSize(), 0.0);
 axis.manualTickStepSize(true);
 assertFalse(axis.isManualQuant());
 axis.manualTickStepSize(false);
 assertFalse(axis.isManualQuant());
 axis.setTickStepSize(0.5);
 assertEquals(0.5, axis.getTickStepSize(), 0.0);
 axis.manualTickStepSize(true);
 assertTrue(axis.isManualQuant());
 }

 // Test setAxisBounds(PlotRunList) method
 // This method is abstract, so we cannot test it directly in this class
 // Create a new test class for the subclass that overrides this method

 // Test setSize(Rectangle) method
 @Test
 public void testSetSize() {
 Rectangle plotFrame = new Rectangle(10, 10, 200, 200);
 PlotAxis axis = new PlotAxis();
 axis.setSize(plotFrame);
 // Add assertions for the axis's properties after the setSize() method call
 }

 // Test scaleCoord(double) method
 @Test
 public void testScaleCoord() {
 PlotAxis axis = new PlotAxis();
 double x = 5.0;
 int expected = (int)(axis.getxA() * axis.scale.func(x) + axis.getxB());
 int actual = axis.scaleCoord(x);
 assertEquals(expected, actual);
 }

 // Test clone() method
 @Test
 public void testClone() {
 PlotAxis originalAxis = new PlotAxis();
 PlotAxis clonedAxis = originalAxis.clone();
 assertNotSame(originalAxis, clonedAxis);
 }

 // Test findGoodLimits() method with different input values
 @Test
 public void testFindGoodLimitsWithDifferentValues() {
 PlotAxisAxisScale axisScale = new PlotAxisAxisScale();
 PlotAxis axis = new PlotAxis();
 double lb = -10.0;
 double ub = 10.0;
 AxisLimitData limData = axis.scale.findGoodLimits(lb, ub);
 assertEquals(-5.0, limData.lb, 0.0);
 assertEquals(5.0, limData.ub, 0.0);
 assertEquals(1.0, limData.quantum, 0.0);
 }

 // Test findGoodLimits() method with negative input values
 @Test(expected = IllegalArgumentException.class)
 public void testFindGoodLimitsWithNegativeValues() {
 PlotAxisAxisScale axisScale = new PlotAxisAxisScale();
 PlotAxis axis = new PlotAxis();
 double lb = -5.0;
 double ub = -10.0;
 axis.scale.findGoodLimits(lb, ub);
 }

 // Test findGoodLimits() method with large input values
 @Test
 public void testFindGoodLimitsWithLargeValues() {
 PlotAxisAxisScale axisScale = new PlotAxisAxisScale();
 PlotAxis axis = new PlotAxis();
 double lb = 1000000.0;
 double ub = 2000000.0;
 AxisLimitData limData = axis.scale.findGoodLimits(lb, ub);
 assertEquals(lb, limData.lb, 0.0);
 assertEquals(ub, limData.ub, 0.0);
 assertEquals(1000000.0, limData.quantum, 0.0);
 }

 // Test resizeAxis(Rectangle) method
 // This method is abstract, so we cannot test it directly in this class
 // Create a new test class for the subclass that overrides this method

 // Test draw(Graphics, Component, Rectangle) method
 // This method is abstract, so we cannot test it directly in this class
 // Create a new test class for the subclass that overrides this method

}