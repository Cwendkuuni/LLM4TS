import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.text.DecimalFormat;

public class PlotAxisTest {

 private PlotAxis axis;

 @Test
 public void testPlotAxisConstructor1() {
 axis = new PlotAxis();
 assertEquals(1.0, axis.xA, 0.0);
 assertEquals(1.0, axis.xB, 0.0);
 assertNull(axis.label);
 assertNotNull(axis.scale);
 assertNotNull(axis.nf);
 assertFalse(axis.manualLB);
 assertFalse(axis.manualUB);
 assertFalse(axis.manualQuant);
 assertEquals(1, axis.gridStyle);
 }

 @Test
 public void testPlotAxisConstructor2() {
 axis = new PlotAxis("Test Label", null, null, 1);
 assertEquals("Test Label", axis.getLabel());
 assertNull(axis.label);
 assertNotNull(axis.scale);
 assertNotNull(axis.nf);
 assertFalse(axis.manualLB);
 assertFalse(axis.manualUB);
 assertFalse(axis.manualQuant);
 assertEquals(1, axis.gridStyle);
 }

 @Test(expected = NullPointerException.class)
 public void testPlotAxisConstructor3_scaleNull() {
 axis = new PlotAxis("Test Label", null, null, 1);
 axis.setScale(null);
 }

 @Test(expected = NullPointerException.class)
 public void testPlotAxisConstructor3_nfNull() {
 axis = new PlotAxis("Test Label", null, null, 1);
 axis.setNumberFormat(null);
 }

 @Test
 public void testSetLabel() {
 axis = new PlotAxis();
 axis.setLabel("Test Label");
 assertEquals("Test Label", axis.getLabel());
 }

 @Test
 public void testGetLabel() {
 axis = new PlotAxis();
 assertNull(axis.getLabel());
 axis.setLabel("Test Label");
 assertEquals("Test Label", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 axis = new PlotAxis();
 axis.setScale(new LinearAxisScale());
 assertNotNull(axis.getScale());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetScaleNull() {
 axis = new PlotAxis();
 axis.setScale(null);
 }

 @Test
 public void testGetScale() {
 axis = new PlotAxis();
 assertNotNull(axis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 axis = new PlotAxis();
 DecimalFormat nf = new DecimalFormat("#.##");
 axis.setNumberFormat(nf);
 assertNotNull(axis.getNumberFormat());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNumberFormatNull() {
 axis = new PlotAxis();
 axis.setNumberFormat(null);
 }

 @Test
 public void testGetNumberFormat() {
 axis = new PlotAxis();
 assertNotNull(axis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 axis = new PlotAxis();
 axis.setGridStyle(2);
 assertEquals(2, axis.getGridStyle());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetGridStyleNegative() {
 axis = new PlotAxis();
 axis.setGridStyle(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetGridStyleZero() {
 axis = new PlotAxis();
 axis.setGridStyle(0);
 }

 @Test
 public void testManualLowerBounds() {
 axis = new PlotAxis();
 axis.manualLowerBounds(true);
 assertTrue(axis.manualLB);
 axis.manualLowerBounds(false);
 assertFalse(axis.manualLB);
 }

 @Test
 public void testSetLowerBounds() {
 axis = new PlotAxis();
 axis.setLowerBounds(10.5);
 assertEquals(10.5, axis.getLowerBounds(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis = new PlotAxis();
 axis.setLowerBounds(10.5);
 assertEquals("10.5", axis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 axis = new PlotAxis();
 axis.manualUpperBounds(true);
 assertTrue(axis.manualUB);
 axis.manualUpperBounds(false);
 assertFalse(axis.manualUB);
 }

 @Test
 public void testSetUpperBounds() {
 axis = new PlotAxis();
 axis.setUpperBounds(20.5);
 assertEquals(20.5, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis = new PlotAxis();
 axis.setUpperBounds(20.5);
 assertEquals("20.5", axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 axis = new PlotAxis();
 axis.manualTickStepSize(true);
 assertTrue(axis.manualQuant);
 axis.manualTickStepSize(false);
 assertFalse(axis.manualQuant);
 }

 @Test
 public void testSetTickStepSize() {
 axis = new PlotAxis();
 axis.setTickStepSize(5.5);
 assertEquals(5.5, axis.getTickStepSize(), 0.0);
 }

 @Test
 public void testScaleCoord() {
 axis = new PlotAxis();
 axis.setAxisBounds(new PlotRunList());
 axis.setAxisTickLimits();
 axis.setSize(new Rectangle(10, 10, 100, 100));
 assertEquals(51, axis.scaleCoord(10.0));
 }

 @Test(expected = RuntimeException.class)
 public void testSetAxisBoundsNull() {
 axis = new PlotAxis();
 axis.setAxisBounds(null);
 }

 @Test
 public void testClone() {
 axis = new PlotAxis();
 PlotAxis clonedAxis = (PlotAxis)axis.clone();
 assertNotNull(clonedAxis);
 }
}