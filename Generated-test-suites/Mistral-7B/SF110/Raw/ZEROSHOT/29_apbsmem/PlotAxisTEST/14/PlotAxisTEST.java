package jahuwaldt.plot.test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;
import jahuwaldt.plot.LinearAxisScale;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

 @Test
 public void testPlotAxisConstructor() {
 PlotAxis plotAxis = new PlotAxis();
 assertNotNull(plotAxis);
 }

 @Test
 public void testPlotAxisWithLabelScaleAndNumberFormat() {
 PlotAxis plotAxis = new PlotAxis("Test Label", new LinearAxisScale(), NumberFormat.getInstance(), 1);
 assertNotNull(plotAxis);
 assertSame(NumberFormat.getInstance(), plotAxis.getNumberFormat());
 assertSame(LinearAxisScale.class, plotAxis.getScale().getClass());
 assertNull(plotAxis.getLabel());
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
 PlotAxisScale scale = new LinearAxisScale();
 plotAxis.setScale(scale);
 assertSame(scale, plotAxis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 PlotAxis plotAxis = new PlotAxis();
 NumberFormat nf = NumberFormat.getInstance();
 plotAxis.setNumberFormat(nf);
 assertSame(nf, plotAxis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setGridStyle(1);
 assertEquals(1, plotAxis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.manualLowerBounds(true);
 assertTrue(plotAxis.isManualLB());
 plotAxis.manualLowerBounds(false);
 assertFalse(plotAxis.isManualLB());
 }

 @Test
 public void testSetLowerBounds() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setLowerBounds(1.0);
 assertEquals(1.0, plotAxis.getLowerBounds(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setLowerBounds(1.0);
 NumberFormat nf = NumberFormat.getInstance();
 plotAxis.setNumberFormat(nf);
 assertEquals("1.0", plotAxis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.manualUpperBounds(true);
 assertTrue(plotAxis.isManualUB());
 plotAxis.manualUpperBounds(false);
 assertFalse(plotAxis.isManualUB());
 }

 @Test
 public void testSetUpperBounds() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setUpperBounds(2.0);
 assertEquals(2.0, plotAxis.getUpperBounds(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setUpperBounds(2.0);
 NumberFormat nf = NumberFormat.getInstance();
 plotAxis.setNumberFormat(nf);
 assertEquals("2.0", plotAxis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.manualTickStepSize(true);
 assertTrue(plotAxis.isManualQuant());
 plotAxis.manualTickStepSize(false);
 assertFalse(plotAxis.isManualQuant());
 }

 @Test
 public void testSetTickStepSize() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setTickStepSize(0.5);
 assertEquals(0.5, plotAxis.getTickStepSize(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTickStepSizeNegativeValue() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setTickStepSize(-0.5);
 }

 @Test
 public void testGetAxisBounds() {
 // Abstract method, cannot test directly
 }

 @Test
 public void testSetSize() {
 // Abstract method, cannot test directly
 }

 @Test
 public void testScaleCoord() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setAxisBounds(new PlotRunList());
 plotAxis.setAxisTickLimits();
 plotAxis.resizeAxis(new Rectangle(0, 0, 100, 100));
 assertEquals(50, plotAxis.scaleCoord(0.5));
 }

 @Test
 public void testClone() {
 PlotAxis original = new PlotAxis();
 PlotAxis clone = original.clone();
 assertNotSame(original, clone);
 }
}