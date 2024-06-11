package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;
import jahuwaldt.plot.LinearAxisScale;

import org.junit.Test;

public class PlotAxisTest {

 private PlotAxis axis;
 private PlotAxisScale scale;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 axis = new PlotAxis() {
 @Override
 protected void resizeAxis(Rectangle p0) {
 // implementation not relevant for testing
 }

 @Override
 public void setAxisBounds(PlotRunList p0) {
 // implementation not relevant for testing
 }

 @Override
 public void draw(Graphics p0, Component p1, Rectangle p2) {
 // implementation not relevant for testing
 }
 };
 scale = new LinearAxisScale();
 }

 @Test
 public void testConstructor() {
 axis = new PlotAxis();
 assertNotNull(axis.getLabel());
 assertNull(axis.getScale());
 assertNotNull(axis.getNumberFormat());
 assertEquals(1, axis.getGridStyle());
 }

 @Test
 public void testConstructorWithParameters() {
 axis = new PlotAxis("Test Axis", scale, new DecimalFormat("#.##"), 2);
 assertNotNull(axis.getLabel());
 assertSame(scale, axis.getScale());
 assertNotNull(axis.getNumberFormat());
 assertEquals(2, axis.getGridStyle());
 }

 @Test
 public void testSetLabel() {
 axis.setLabel("New Label");
 assertEquals("New Label", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 axis.setScale(scale);
 assertSame(scale, axis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 axis.setNumberFormat(new DecimalFormat("#,##.##"));
 assertSame(new DecimalFormat("#,##.##"), axis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 axis.setGridStyle(3);
 assertEquals(3, axis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 axis.manualLowerBounds(true);
 assertTrue(axis.isManualLB());
 axis.manualLowerBounds(false);
 assertFalse(axis.isManualLB());
 }

 @Test
 public void testSetLowerBounds() {
 axis.setLowerBounds(0.5);
 assertEquals(0.5, axis.getLowerBounds(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis.setLowerBounds(0.5);
 assertEquals("0.5", axis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 axis.manualUpperBounds(true);
 assertTrue(axis.isManualUB());
 axis.manualUpperBounds(false);
 assertFalse(axis.isManualUB());
 }

 @Test
 public void testSetUpperBounds() {
 axis.setUpperBounds(1.5);
 assertEquals(1.5, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis.setUpperBounds(1.5);
 assertEquals("1.5", axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 axis.manualTickStepSize(true);
 assertTrue(axis.isManualQuant());
 axis.manualTickStepSize(false);
 assertFalse(axis.isManualQuant());
 }

 @Test
 public void testSetTickStepSize() {
 axis.setTickStepSize(0.1);
 assertEquals(0.1, axis.getTickStepSize(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTickStepSizeNegative() {
 axis.setTickStepSize(-0.1);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testGetTickStepSize() {
 assertEquals(1.0, axis.getTickStepSize(), 0.0);
 }

 @Test
 public void testFindGoodLimits() {
 final double minVal = -1.0;
 final double maxVal = 1.0;
 final AxisLimitData limData = scale.findGoodLimits(minVal, maxVal);
 assertTrue(limData.lb <= limData.ub);
 assertTrue(minVal <= limData.lb);
 assertTrue(maxVal >= limData.ub);
 }

 @Test
 public void testSetAxisTickLimits() {
 final double minVal = -1.0;
 final double maxVal = 1.0;
 axis.setAxisTickLimits();
 assertEquals(minVal, axis.getLowerBounds(), 0.0);
 assertEquals(maxVal, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testScaleCoord() {
 axis.setAxisTickLimits();
 assertEquals(1, axis.scaleCoord(0.0));
 assertEquals(2, axis.scaleCoord(0.2));
 assertEquals(3, axis.scaleCoord(0.4));
 }

 @Test
 public void testClone() {
 final PlotAxis clonedAxis = (PlotAxis)axis.clone();
 assertNotSame(axis, clonedAxis);
 assertSame(axis.getLabel(), clonedAxis.getLabel());
 assertSame(axis.getScale(), clonedAxis.getScale());
 assertSame(axis.getNumberFormat(), clonedAxis.getNumberFormat());
 assertSame(axis.getGridStyle(), clonedAxis.getGridStyle());
 assertSame(axis.isManualLB(), clonedAxis.isManualLB());
 assertSame(axis.getLowerBounds(), clonedAxis.getLowerBounds(), 0.0);
 assertSame(axis.isManualUB(), clonedAxis.isManualUB());
 assertSame(axis.getUpperBounds(), clonedAxis.getUpperBounds(), 0.0);
 assertSame(axis.isManualQuant(), clonedAxis.isManualQuant());
 assertSame(axis.getTickStepSize(), clonedAxis.getTickStepSize(), 0.0);
 }
}