package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

 private PlotAxis axis;
 private PlotAxisScale scale;

 @Before
 public void setUp() {
 axis = new PlotAxis() {
 @Override
 protected void resizeAxis(Rectangle rectangle) {
 // no implementation needed for testing
 }

 @Override
 public void draw(Graphics graphics, Component component, Rectangle rectangle) {
 // no implementation needed for testing
 }

 @Override
 public void setAxisBounds(PlotRunList plotRunList) {
 // no implementation needed for testing
 }
 };
 scale = new PlotAxisScale() {
 @Override
 public double func(double x) {
 return x;
 }
 };
 }

 @Test
 public void testConstructor() {
 PlotAxis axis = new PlotAxis();
 assertNotNull(axis);
 }

 @Test
 public void testSetLabel() {
 axis.setLabel("Test label");
 assertEquals("Test label", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 axis.setScale(scale);
 assertEquals(scale, axis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 DecimalFormat format = new DecimalFormat("#.##");
 axis.setNumberFormat(format);
 assertEquals(format, axis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 axis.setGridStyle(PlotAxis.kMajorGrid);
 assertEquals(PlotAxis.kMajorGrid, axis.getGridStyle());
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
 assertEquals(0.5, axis.getLowerBounds(), 0.01);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis.setLowerBounds(0.5);
 DecimalFormat format = new DecimalFormat("#.##");
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
 assertEquals(1.5, axis.getUpperBounds(), 0.01);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis.setUpperBounds(1.5);
 DecimalFormat format = new DecimalFormat("#.##");
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
 assertEquals(0.1, axis.getTickStepSize(), 0.01);
 }

 @Test(expected = NullPointerException.class)
 public void testSetScaleWithNullScale() {
 axis.setScale(null);
 fail("Null pointer exception expected");
 }

 @Test(expected = NullPointerException.class)
 public void testSetNumberFormatWithNullFormat() {
 axis.setNumberFormat(null);
 fail("Null pointer exception expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetLowerBoundsWithNegativeValue() {
 axis.setLowerBounds(-1.0);
 fail("Illegal argument exception expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetUpperBoundsWithNegativeValue() {
 axis.setUpperBounds(-1.0);
 fail("Illegal argument exception expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTickStepSizeWithNegativeValue() {
 axis.setTickStepSize(-0.1);
 fail("Illegal argument exception expected");
 }
}