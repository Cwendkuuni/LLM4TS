package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {

 private PlotAxis axis;

 @Before
 public void setUp() {
 axis = new PlotAxis() {
 @Override
 protected void resizeAxis(Rectangle rectangle) {
 // empty implementation for testing
 }

 @Override
 public void draw(Graphics g, Component component, Rectangle rectangle) {
 // empty implementation for testing
 }
 };
 }

 @Test
 public void testConstructor() {
 axis = new PlotAxis();
 assertNotNull(axis.getLabel());
 assertNull(axis.getScale());
 assertNull(axis.getNumberFormat());
 assertEquals(1, axis.getGridStyle());
 assertTrue(axis.manualLB);
 assertTrue(axis.manualUB);
 assertTrue(axis.manualQuant);
 assertEquals(1.0, axis.xA, 0.0);
 assertEquals(1.0, axis.xB, 0.0);
 assertEquals(Double.MAX_VALUE, axis.aLB, 0.0);
 assertEquals(-1.7976931348623157E308, axis.aUB, 0.0);
 assertEquals(0.0, axis.quantum, 0.0);
 }

 @Test
 public void testConstructorWithLabelScaleFormatAndGridStyle() {
 axis = new PlotAxis("TestLabel", null, null, 1);
 assertNotNull(axis.getLabel());
 assertNull(axis.getScale());
 assertNull(axis.getNumberFormat());
 assertEquals("TestLabel", axis.getLabel());
 assertEquals(1, axis.getGridStyle());
 assertFalse(axis.manualLB);
 assertFalse(axis.manualUB);
 assertFalse(axis.manualQuant);
 }

 @Test
 public void testSetLabel() {
 axis.setLabel("NewLabel");
 assertEquals("NewLabel", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 final PlotAxisScale scale = new PlotAxisScale() {
 @Override
 public double func(double input) {
 return input * 2.0;
 }
 };
 axis.setScale(scale);
 assertEquals(scale, axis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 final NumberFormat numberFormat = NumberFormat.getPercentInstance();
 axis.setNumberFormat(numberFormat);
 assertEquals(numberFormat, axis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 axis.setGridStyle(2);
 assertEquals(2, axis.getGridStyle());
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
 axis.setLowerBounds(5.0);
 assertEquals(5.0, axis.getLowerBounds(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis.setLowerBounds(5.0);
 assertEquals("5.0%", axis.lowerBoundsAsString());
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
 axis.setUpperBounds(10.0);
 assertEquals(10.0, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis.setUpperBounds(10.0);
 assertEquals("10.0%", axis.upperBoundsAsString());
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
 axis.setTickStepSize(3.0);
 assertEquals(3.0, axis.getTickStepSize(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTickStepSizeNegativeValue() {
 axis.setTickStepSize(-3.0);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testGetAxisBounds() {
 // TODO: implement test for abstract method setAxisBounds(PlotRunList p0)
 fail("Method setAxisBounds(PlotRunList p0) not stubbed");
 }

 @Test
 public void testSetSize() {
 // TODO: implement test for abstract method resizeAxis(Rectangle p0)
 fail("Method resizeAxis(Rectangle p0) not stubbed");

 final Rectangle rectangle = new Rectangle(0, 0, 10, 10);
 axis.setSize(rectangle);
 }

 @Test
 public void testScaleCoord() {
 axis.setLowerBounds(0.0);
 axis.setUpperBounds(2.0);
 assertEquals(1, axis.scaleCoord(0.0));
 assertEquals(3, axis.scaleCoord(1.0));
 }

 @Test
 public void testClone() {
 final PlotAxis clonedAxis = (PlotAxis)axis.clone();
 assertNotNull(clonedAxis);
 assertNotSame(axis, clonedAxis);
 }
}