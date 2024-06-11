package jahuwaldt.plot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

 private PlotAxis axis;
 private PlotAxisScale scale;
 private NumberFormat nf;

 @Before
 public void setUp() {
 axis = new PlotAxis() {
 @Override
 protected void resizeAxis(Rectangle plotFrame) {
 // empty implementation
 }

 @Override
 public void draw(Graphics g, Component c, Rectangle rect) {
 // empty implementation
 }
 };
 scale = new PlotAxisScale() {
 @Override
 public AxisLimitData findGoodLimits(double lower, double upper) {
 return new AxisLimitData(1.0, lower, upper, 1.0);
 }
 };
 nf = NumberFormat.getInstance();
 }

 @Test
 public void testEmptyConstructor() {
 PlotAxis axis = new PlotAxis();
 assertEquals(1.0, axis.xA, 0.001);
 assertEquals(1.0, axis.xB, 0.001);
 assertEquals(Double.MAX_VALUE, axis.aLB, 0.001);
 assertEquals(-1.7976931348623157E308, axis.aUB, 0.001);
 assertEquals(1, axis.gridStyle);
 assertNull(axis.label);
 assertSame(new LinearAxisScale(), axis.scale);
 assertSame(NumberFormat.getInstance(), axis.nf);
 }

 @Test
 public void testConstructorWithLabelScaleAndFormat() {
 axis = new PlotAxis("TestAxis", scale, nf, 1);
 assertEquals("TestAxis", axis.getLabel());
 assertSame(scale, axis.getScale());
 assertSame(nf, axis.getNumberFormat());
 assertEquals(1, axis.getGridStyle());
 }

 @Test
 public void testConstructorWithNullLabelAndFormat() {
 axis = new PlotAxis(null, scale, null, 1);
 assertNull(axis.getLabel());
 assertSame(scale, axis.getScale());
 assertSame(NumberFormat.getInstance(), axis.getNumberFormat());
 assertEquals(1, axis.getGridStyle());
 }

 @Test
 public void testConstructorWithNullScale() {
 axis = new PlotAxis("TestAxis", null, nf, 1);
 assertSame(new LinearAxisScale(), axis.getScale());
 }

 @Test
 public void testConstructorWithNullFormat() {
 axis = new PlotAxis("TestAxis", scale, null, 1);
 assertSame(NumberFormat.getInstance(), axis.getNumberFormat());
 }

 @Test
 public void testSetLabel() {
 axis.setLabel("NewLabel");
 assertEquals("NewLabel", axis.getLabel());
 }

 @Test
 public void testGetLabel() {
 assertEquals("TestAxis", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 axis.setScale(new LogAxisScale());
 assertSame(new LogAxisScale(), axis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 DecimalFormat df = new DecimalFormat("#.###");
 axis.setNumberFormat(df);
 assertSame(df, axis.getNumberFormat());
 }

 @Test
 public void testGetNumberFormat() {
 assertEquals(NumberFormat.getInstance().getClass(), axis.getNumberFormat().getClass());
 }

 @Test
 public void testSetGridStyle() {
 axis.setGridStyle(PlotAxis.kTickMarksOnly);
 assertEquals(PlotAxis.kTickMarksOnly, axis.getGridStyle());
 }

 @Test
 public void testGetGridStyle() {
 assertEquals(1, axis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 axis.manualLowerBounds(true);
 assertTrue(axis.manualLB);
 axis.manualLowerBounds(false);
 assertFalse(axis.manualLB);
 }

 @Test
 public void testSetLowerBounds() {
 axis.setLowerBounds(2.5);
 assertEquals(2.5, axis.getLowerBounds(), 0.001);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis.setLowerBounds(2.5);
 assertEquals("2.5", axis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 axis.manualUpperBounds(true);
 assertTrue(axis.manualUB);
 axis.manualUpperBounds(false);
 assertFalse(axis.manualUB);
 }

 @Test
 public void testSetUpperBounds() {
 axis.setUpperBounds(7.5);
 assertEquals(7.5, axis.getUpperBounds(), 0.001);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis.setUpperBounds(7.5);
 assertEquals("7.5", axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 axis.manualTickStepSize(true);
 assertTrue(axis.manualQuant);
 axis.manualTickStepSize(false);
 assertFalse(axis.manualQuant);
 }

 @Test
 public void testSetTickStepSize() {
 axis.setTickStepSize(0.5);
 assertEquals(0.5, axis.getTickStepSize(), 0.001);
 }

 @Test
 public void testGetTickStepSize() {
 assertEquals(1.0, axis.getTickStepSize(), 0.001);
 }

 @Test
 public void testSetAxisBounds() {
 // This method is abstract, cannot be tested directly
 }

 @Test
 public void testSetSize() {
 // This method is abstract, cannot be tested directly
 }

 @Test
 public void testScaleCoord() {
 assertEquals(1, axis.scaleCoord(-1.0));
 assertEquals(2, axis.scaleCoord(-0.5));
 assertEquals(3, axis.scaleCoord(0.0));
 assertEquals(4, axis.scaleCoord(0.5));
 assertEquals(5, axis.scaleCoord(1.0));
 }

 @Test
 public void testClone() {
 PlotAxis clonedAxis = (PlotAxis)axis.clone();
 assertNotEquals(axis, clonedAxis);
 assertEquals("TestAxis", clonedAxis.getLabel());
 assertEquals(scale, clonedAxis.getScale());
 assertEquals(nf, clonedAxis.getNumberFormat());
 assertEquals(1, clonedAxis.getGridStyle());
 }

}