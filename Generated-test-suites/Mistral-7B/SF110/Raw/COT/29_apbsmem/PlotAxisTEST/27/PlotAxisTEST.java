import org.junit.Test;
import org.junit.Assert.*;
import jahuwaldt.plot.PlotAxis;
import java.text.DecimalFormat;

public class PlotAxisTest {

 private PlotAxis axis;

 @Test
 public void testConstructor1() {
 axis = new PlotAxis();
 assertNotNull(axis.getLabel());
 assertNotNull(axis.getScale());
 assertNotNull(axis.getNumberFormat());
 assertEquals(1, axis.getGridStyle());
 }

 @Test
 public void testConstructor2() {
 axis = new PlotAxis("Test Axis", null, null, 1);
 assertNotNull(axis.getLabel());
 assertNotNull(axis.getScale());
 assertNull(axis.getNumberFormat());
 assertEquals(1, axis.getGridStyle());
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
 assertSame(axis.getScale(), new LinearAxisScale());
 }

 @Test
 public void testSetNumberFormat() {
 axis = new PlotAxis();
 DecimalFormat format = new DecimalFormat("#.##");
 axis.setNumberFormat(format);
 assertSame(axis.getNumberFormat(), format);
 }

 @Test
 public void testSetGridStyle() {
 axis = new PlotAxis();
 axis.setGridStyle(2);
 assertEquals(2, axis.getGridStyle());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetScaleNull() {
 axis = new PlotAxis();
 axis.setScale(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNumberFormatNull() {
 axis = new PlotAxis();
 axis.setNumberFormat(null);
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
 axis.setLowerBounds(5.5);
 assertEquals(5.5, axis.getLowerBounds(), 0.001);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis = new PlotAxis();
 DecimalFormat format = new DecimalFormat("#.##");
 axis.setLowerBounds(5.5);
 assertEquals("5.50", axis.lowerBoundsAsString());
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
 axis.setUpperBounds(10.5);
 assertEquals(10.5, axis.getUpperBounds(), 0.001);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis = new PlotAxis();
 DecimalFormat format = new DecimalFormat("#.##");
 axis.setUpperBounds(10.5);
 assertEquals("10.50", axis.upperBoundsAsString());
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
 axis.setTickStepSize(2.5);
 assertEquals(2.5, axis.getTickStepSize(), 0.001);
 }

 @Test
 public void testSetAxisBounds() {
 // Abstract method, cannot be tested directly
}

 @Test
 public void testSetSize() {
 // Protected method, cannot be tested directly in this test class
}

 @Test
 public void testScaleCoord() {
 axis = new PlotAxis();
 assertEquals(1, axis.scaleCoord(0.0));
 }

 @Test
 public void testClone() {
 PlotAxis clonedAxis = axis.clone();
 assertNotSame(axis, clonedAxis);
 }

}