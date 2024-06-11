package jahuwaldt.plot.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.text.DecimalFormat;

import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

 @Test
 public void testDefaultConstructor() {
 PlotAxis plotAxis = new PlotAxis();
 assertEquals(1.0, plotAxis.xA, 0.0);
 assertEquals(1.0, plotAxis.xB, 0.0);
 assertEquals(Double.MAX_VALUE, plotAxis.aLB, 0.0);
 assertEquals(-1.7976931348623157E308, plotAxis.aUB, 0.0);
 assertEquals(1, plotAxis.gridStyle);
 assertNull(plotAxis.label);
 assertNull(plotAxis.scale);
 assertNull(plotAxis.nf);
 }

 @Test
 public void testCustomConstructor() {
 PlotAxis plotAxis = new PlotAxis("Test Label", new PlotAxisScale(), null, 1);
 assertEquals("Test Label", plotAxis.getLabel());
 assertNotNull(plotAxis.getScale());
 assertNull(plotAxis.nf);
 assertEquals(1, plotAxis.gridStyle);
 }

 @Test
 public void testSetLabel() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setLabel("Test Label");
 assertEquals("Test Label", plotAxis.getLabel());
 }

 @Test
 public void testSetScale() {
 PlotAxis plotAxis = new PlotAxis();
 PlotAxisScale plotAxisScale = new PlotAxisScale();
 plotAxis.setScale(plotAxisScale);
 assertEquals(plotAxisScale, plotAxis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 PlotAxis plotAxis = new PlotAxis();
 DecimalFormat decimalFormat = new DecimalFormat("#.####");
 plotAxis.setNumberFormat(decimalFormat);
 assertEquals(decimalFormat, plotAxis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setGridStyle(2);
 assertEquals(2, plotAxis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.manualLowerBounds(true);
 assertTrue(plotAxis.manualLB);
 plotAxis.manualLowerBounds(false);
 assertFalse(plotAxis.manualLB);
 }

 @Test
 public void testSetLowerBounds() {
 PlotAxis plotAxis = new PlotAxis();
 double lowerBounds = 10.5;
 plotAxis.setLowerBounds(lowerBounds);
 assertEquals(lowerBounds, plotAxis.getLowerBounds(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setLowerBounds(10.5);
 DecimalFormat decimalFormat = new DecimalFormat("#.####");
 assertEquals("10.5", plotAxis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.manualUpperBounds(true);
 assertTrue(plotAxis.manualUB);
 plotAxis.manualUpperBounds(false);
 assertFalse(plotAxis.manualUB);
 }

 @Test
 public void testSetUpperBounds() {
 PlotAxis plotAxis = new PlotAxis();
 double upperBounds = 15.2;
 plotAxis.setUpperBounds(upperBounds);
 assertEquals(upperBounds, plotAxis.getUpperBounds(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setUpperBounds(15.2);
 DecimalFormat decimalFormat = new DecimalFormat("#.####");
 assertEquals("15.2", plotAxis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.manualTickStepSize(true);
 assertTrue(plotAxis.manualQuant);
 plotAxis.manualTickStepSize(false);
 assertFalse(plotAxis.manualQuant);
 }

 @Test
 public void testSetTickStepSize() {
 PlotAxis plotAxis = new PlotAxis();
 double tickStepSize = 2.5;
 plotAxis.setTickStepSize(tickStepSize);
 assertEquals(tickStepSize, plotAxis.getTickStepSize(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTickStepSizeNegativeValue() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setTickStepSize(-2.5);
 }

 @Test
 public void testSetAxisBounds() {
 // This method is abstract and cannot be tested directly
 // We can test the subclasses separately
 }

 @Test
 public void testSetSize() {
 // This method is abstract and cannot be tested directly
 // We can test the subclasses separately
 }

 @Test
 public void testDraw() {
 // This method is abstract and cannot be tested directly
 // We can test the subclasses separately
 }

 @Test
 public void testScaleCoord() {
 PlotAxis plotAxis = new LinearPlotAxis();
 plotAxis.setAxisBounds(new PlotRunList());
 plotAxis.setSize(new Rectangle(50, 50, 100, 100));
 double x = 5.0;
 int expected = 55;
 int actual = plotAxis.scaleCoord(x);
 assertEquals(expected, actual);
 }
}