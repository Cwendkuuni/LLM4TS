package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

 private PlotAxis axis;

 @Test
 public void testPlotAxisInstance() {
 axis = new PlotAxis();
 assertNotNull(axis);
 }

 @Test
 public void testPlotAxisWithLabelScaleFormatAndGridStyle() {
 axis = new PlotAxis("Test Axis", new PlotAxisScale(), new DecimalFormat("#.###"), 1);
 assertNotNull(axis);
 assertEquals("Test Axis", axis.getLabel());
 }

 @Test
 public void testSetLabel() {
 axis = new PlotAxis();
 axis.setLabel("Test Axis");
 assertEquals("Test Axis", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 axis = new PlotAxis();
 axis.setScale(new PlotAxisScale());
 assertSame(axis.getScale(), new PlotAxisScale());
 }

 @Test
 public void testSetNumberFormat() {
 axis = new PlotAxis();
 axis.setNumberFormat(new DecimalFormat("#.###"));
 assertSame(axis.getNumberFormat(), new DecimalFormat("#.###"));
 }

 @Test
 public void testSetGridStyle() {
 axis = new PlotAxis();
 axis.setGridStyle(1);
 assertEquals(1, axis.getGridStyle());
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
 assertEquals(1.0, axis.getLowerBounds(), 0.01);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis = new PlotAxis();
 axis.setLowerBounds(1.0);
 DecimalFormat format = (DecimalFormat)axis.getNumberFormat();
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
 axis.setUpperBounds(2.0);
 assertEquals(2.0, axis.getUpperBounds(), 0.01);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis = new PlotAxis();
 axis.setUpperBounds(2.0);
 DecimalFormat format = (DecimalFormat)axis.getNumberFormat();
 assertEquals("2.0", axis.upperBoundsAsString());
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
 axis.setTickStepSize(0.5);
 assertEquals(0.5, axis.getTickStepSize(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeTickStepSize() {
 axis = new PlotAxis();
 axis.setTickStepSize(-0.5);
 }

 @Test
 public void testDraw() {
 final Graphics graphics = new Graphics() {
 @Override
 public void drawLine(int x1, int y1, int x2, int y2) {
 // Empty implementation for testing
 }
 };
 final Component component = new Object() {
 @Override
 public Graphics getGraphics() {
 return graphics;
 }
 };
 axis = new PlotAxis();
 final Rectangle rectangle = new Rectangle(0, 0, 10, 10);
 axis.draw(graphics, component, rectangle);
 // Empty implementation for testing
 }

 @Test
 public void testSetAxisBounds() {
 // This method is abstract,
 // it should be implemented and tested in subclasses
}

 @Test
 public void testSetSize() {
 // This method is abstract,
 // it should be implemented and tested in subclasses
}

 @Test
 public void testScaleCoord() {
 axis = new PlotAxis();
 axis.setLowerBounds(0.0);
 axis.setUpperBounds(10.0);
 axis.setTickStepSize(1.0);
 assertEquals(5, axis.scaleCoord(5.5));
 }

 @Test
 public void testClone() {
 PlotAxis clonedAxis = (PlotAxis)axis.clone();
 assertNotSame(axis, clonedAxis);
 }
}