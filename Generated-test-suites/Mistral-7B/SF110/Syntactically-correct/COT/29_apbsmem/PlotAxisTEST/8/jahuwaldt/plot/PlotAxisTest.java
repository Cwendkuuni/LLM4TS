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

 @Test
 public void testPlotAxisConstructor() {
 axis = new PlotAxis();
 assertNotNull(axis);
 }

 @Test
 public void testPlotAxisWithLabelScaleFormatAndGridStyleConstructor() {
 axis = new PlotAxis("TestAxis", new LinearAxisScale(), null, 1);
 assertNotNull(axis);
 assertEquals("TestAxis", axis.getLabel());
 }

 @Test
 public void testSetLabelMethod() {
 axis = new PlotAxis();
 axis.setLabel("TestAxis");
 assertEquals("TestAxis", axis.getLabel());
 }

 @Test
 public void testSetScaleMethod() {
 axis = new PlotAxis();
 PlotAxisScale scale = new LinearAxisScale();
 axis.setScale(scale);
 assertEquals(scale, axis.getScale());
 }

 @Test
 public void testSetNumberFormatMethod() {
 NumberFormat format = NumberFormat.getInstance();
 axis = new PlotAxis();
 axis.setNumberFormat(format);
 assertEquals(format, axis.getNumberFormat());
 }

 @Test
 public void testSetGridStyleMethod() {
 axis = new PlotAxis();
 axis.setGridStyle(2);
 assertEquals(2, axis.getGridStyle());
 }

 @Test
 public void testManualLowerBoundsMethod() {
 axis = new PlotAxis();
 axis.manualLowerBounds(true);
 assertTrue(axis.manualLB);
 }

 @Test
 public void testSetLowerBoundsMethod() {
 axis = new PlotAxis();
 axis.setLowerBounds(1.0);
 assertEquals(1.0, axis.getLowerBounds(), 0.001);
 }

 @Test
 public void testLowerBoundsAsStringMethod() {
 NumberFormat format = NumberFormat.getInstance();
 axis = new PlotAxis();
 axis.setLowerBounds(1.0);
 assertEquals("1.0", axis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBoundsMethod() {
 axis = new PlotAxis();
 axis.manualUpperBounds(true);
 assertTrue(axis.manualUB);
 }

 @Test
 public void testSetUpperBoundsMethod() {
 axis = new PlotAxis();
 axis.setUpperBounds(2.0);
 assertEquals(2.0, axis.getUpperBounds(), 0.001);
 }

 @Test
 public void testUpperBoundsAsStringMethod() {
 NumberFormat format = NumberFormat.getInstance();
 axis = new PlotAxis();
 axis.setUpperBounds(2.0);
 assertEquals("2.0", axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSizeMethod() {
 axis = new PlotAxis();
 axis.manualTickStepSize(true);
 assertTrue(axis.manualQuant);
 }

 @Test
 public void testSetTickStepSizeMethod() {
 axis = new PlotAxis();
 axis.setTickStepSize(1.0);
 assertEquals(1.0, axis.getTickStepSize(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetScaleWithNullScale() {
 axis = new PlotAxis();
 axis.setScale(null);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNumberFormatWithNullFormat() {
 axis = new PlotAxis();
 axis.setNumberFormat(null);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetGridStyleWithNegativeGridStyle() {
 axis = new PlotAxis();
 axis.setGridStyle(-1);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testSetAxisBoundsMethod() {
 fail("Abstract method, cannot test directly");
 }

 @Test
 public void testSetSizeMethod() {
 Component component = new Component() {
 @Override
 public void paint(Graphics g) {}
 };
 Rectangle plotFrame = new Rectangle(0, 0, 100, 100);
 axis = new PlotAxis();
 axis.setSize(plotFrame);
 assertNotNull(axis);
 }

 @Test
 public void testScaleCoordMethod() {
 axis = new PlotAxis();
 assertEquals(0, axis.scaleCoord(0.0));
 }

 @Test
 public void testCloneMethod() {
 axis = new PlotAxis();
 PlotAxis clonedAxis = (PlotAxis)axis.clone();
 assertNotNull(clonedAxis);
 assertNotSame(axis, clonedAxis);
 }

 @Test
 public void testDrawMethod() {
 Graphics graphics = new Graphics() {
 @Override
 public void drawLine(int x1, int y1, int x2, int y2) {}
 };
 Component component = new Component() {
 @Override
 public void paint(Graphics g) {
 this.graphics = g;
 }
 };
 Rectangle rectangle = new Rectangle(0, 0, 100, 100);
 axis = new PlotAxis();
 axis.draw(graphics, component, rectangle);
 assertTrue(axis.getLabel() != null);
 assertTrue(axis.getScale() != null);
 }

}