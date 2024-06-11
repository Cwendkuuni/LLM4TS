package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import org.junit.Test;

import jahuwaldt.plot.LinearAxisScale;
import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

 private PlotAxis axis;

 @Test
 public void testDefaultConstructor() {
 axis = new PlotAxis();
 assertNotNull(axis);
 assertEquals(1.0, axis.getxA(), 0.0);
 assertEquals(1.0, axis.getxB(), 0.0);
 assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
 assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
 assertEquals(1, axis.getGridStyle());
 assertNull(axis.getLabel());
 assertNull(axis.getScale());
 assertNotNull(axis.getNumberFormat());
 }

 @Test
 public void testCustomConstructor() {
 axis = new PlotAxis("Test Label", new LinearAxisScale(), new DecimalFormat("#.##"), 2);
 assertNotNull(axis);
 assertEquals("Test Label", axis.getLabel());
 assertSame(new LinearAxisScale(), axis.getScale());
 assertSame(new DecimalFormat("#.##"), axis.getNumberFormat());
 assertEquals(2, axis.getGridStyle());
 }

 @Test
 public void testSetLabel() {
 axis = new PlotAxis();
 axis.setLabel("Test Label");
 assertEquals("Test Label", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 axis = new PlotAxis();
 axis.setScale(new LinearAxisScale());
 assertSame(new LinearAxisScale(), axis.getScale());
 axis.setScale(new LogAxisScale());
 assertSame(new LogAxisScale(), axis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 axis = new PlotAxis();
 axis.setNumberFormat(NumberFormat.getInstance());
 assertSame(NumberFormat.getInstance(), axis.getNumberFormat());
 axis.setNumberFormat(new DecimalFormat("#,##"));
 assertSame(new DecimalFormat("#,##"), axis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 axis = new PlotAxis();
 axis.setGridStyle(1);
 assertEquals(1, axis.getGridStyle());
 axis.setGridStyle(3);
 assertEquals(3, axis.getGridStyle());
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
 axis.setLowerBounds(0.0);
 assertEquals(0.0, axis.getLowerBounds(), 0.0);
 axis.setLowerBounds(10.5);
 assertEquals(10.5, axis.getLowerBounds(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis = new PlotAxis();
 axis.setLowerBounds(12.3);
 assertEquals("12.3", axis.lowerBoundsAsString());
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
 axis.setUpperBounds(0.0);
 assertEquals(0.0, axis.getUpperBounds(), 0.0);
 axis.setUpperBounds(10.5);
 assertEquals(10.5, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis = new PlotAxis();
 axis.setUpperBounds(12.3);
 assertEquals("12.3", axis.upperBoundsAsString());
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
 axis.setTickStepSize(0.1);
 assertEquals(0.1, axis.getTickStepSize(), 0.0);
 axis.setTickStepSize(1.5);
 assertEquals(1.5, axis.getTickStepSize(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeTickStepSize() {
 axis = new PlotAxis();
 axis.setTickStepSize(-0.5);
 }

 // Add test methods for setAxisBounds, setSize, draw, and scaleCoord methods here

 @Test
 public void testClone() {
 axis = new PlotAxis();
 axis.setLabel("Test Label");
 axis.setScale(new LinearAxisScale());
 axis.setNumberFormat(new DecimalFormat("#.##"));
 axis.setGridStyle(2);
 axis.manualLowerBounds(true);
 axis.setLowerBounds(12.3);
 axis.manualUpperBounds(true);
 axis.setUpperBounds(45.6);
 axis.manualTickStepSize(true);
 axis.setTickStepSize(0.7);
 PlotAxis clone = (PlotAxis)axis.clone();
 assertNotNull(clone);
 assertEquals("Test Label", clone.getLabel());
 assertSame(new LinearAxisScale(), clone.getScale());
 assertSame(new DecimalFormat("#.##"), clone.getNumberFormat());
 assertEquals(2, clone.getGridStyle());
 assertTrue(clone.isManualLB());
 assertEquals(12.3, clone.getLowerBounds(), 0.0);
 assertTrue(clone.isManualUB());
 assertEquals(45.6, clone.getUpperBounds(), 0.0);
 assertTrue(clone.isManualQuant());
 assertEquals(0.7, clone.getTickStepSize(), 0.0);
 }
}