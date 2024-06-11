package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

 private PlotAxis axis;

 @Test
 public void testDefaultConstructor() {
 axis = new PlotAxis();
 assertEquals(1.0, axis.getxA(), 0.0);
 assertEquals(1.0, axis.getxB(), 0.0);
 assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
 assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
 assertEquals(1, axis.getGridStyle());
 assertNull(axis.getLabel());
 assertNull(axis.getScale());
 assertNull(axis.getNumberFormat());
 }

 @Test
 public void testCustomConstructor() {
 axis = new PlotAxis("TestAxis", new PlotAxisScale(), new DecimalFormat("#.##"), 2);
 assertEquals("TestAxis", axis.getLabel());
 assertSame(new PlotAxisScale(), axis.getScale());
 assertSame(new DecimalFormat("#.##"), axis.getNumberFormat());
 assertEquals(2, axis.getGridStyle());
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
 PlotAxisScale scale = new PlotAxisScale();
 axis.setScale(scale);
 assertSame(scale, axis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 axis = new PlotAxis();
 DecimalFormat format = new DecimalFormat("#.##");
 axis.setNumberFormat(format);
 assertSame(format, axis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 axis = new PlotAxis();
 axis.setGridStyle(4);
 assertEquals(4, axis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 axis = new PlotAxis();
 axis.manualLowerBounds(true);
 assertTrue(axis.isManualLB());
 }

 @Test
 public void testSetLowerBounds() {
 axis = new PlotAxis();
 axis.setLowerBounds(-1.0);
 assertEquals(-1.0, axis.getALB(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis = new PlotAxis();
 axis.setLowerBounds(-1.0);
 DecimalFormat format = (DecimalFormat)axis.getNumberFormat();
 assertEquals("-1.0", format.format(axis.getALB()));
 }

 @Test
 public void testManualUpperBounds() {
 axis = new PlotAxis();
 axis.manualUpperBounds(true);
 assertTrue(axis.isManualUB());
 }

 @Test
 public void testSetUpperBounds() {
 axis = new PlotAxis();
 axis.setUpperBounds(1.0);
 assertEquals(1.0, axis.getAUB(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis = new PlotAxis();
 axis.setUpperBounds(1.0);
 DecimalFormat format = (DecimalFormat)axis.getNumberFormat();
 assertEquals("1.0", format.format(axis.getAUB()));
 }

 @Test
 public void testManualTickStepSize() {
 axis = new PlotAxis();
 axis.manualTickStepSize(true);
 assertTrue(axis.isManualQuant());
 }

 @Test
 public void testSetTickStepSize() {
 axis = new PlotAxis();
 axis.setTickStepSize(0.5);
 assertEquals(0.5, axis.getTickStepSize(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeTickStepSize() {
 axis = new PlotAxis();
 axis.setTickStepSize(-0.5);
 fail("Should throw IllegalArgumentException for negative tick step size.");
 }

 @Test
 public void testSetAxisBounds() {
 fail("Abstract method 'setAxisBounds' cannot be tested directly.");
 }

 @Test
 public void testSetSize() {
 fail("Abstract method 'resizeAxis' cannot be tested directly.");
 }

 @Test
 public void testDraw() {
 fail("Abstract method 'draw' cannot be tested directly.");
 }

 @Test
 public void testScaleCoord() {
 axis = new PlotAxis();
 assertEquals(0, axis.scaleCoord(0.0), 0);
 }

 @Test
 public void testClone() {
 axis = new PlotAxis();
 PlotAxis clonedAxis = (PlotAxis)axis.clone();
 assertNotSame(axis, clonedAxis);
 }

}