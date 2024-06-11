import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PlotAxisTest {

 private PlotAxis axis;

 @Test
 public void testInitialization() {
 axis = new PlotAxis();
 assertEquals(1.0, axis.getxA(), 0.0);
 assertEquals(1.0, axis.getxB(), 0.0);
 assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
 assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
 assertEquals(1, axis.getGridStyle());
 assertNull(axis.getLabel());
 assertSame(new LinearAxisScale(), axis.getScale());
 assertSame(NumberFormat.getInstance(), axis.getNumberFormat());
 }

 @Test
 public void testInitializationWithLabelScaleAndNumberFormat() {
 axis = new PlotAxis("Test Label", new LinearAxisScale(), null, 1);
 assertEquals("Test Label", axis.getLabel());
 assertSame(new LinearAxisScale(), axis.getScale());
 assertSame(NumberFormat.getInstance(), axis.getNumberFormat());
 }

 @Test
 public void testInitializationWithScaleAndNumberFormat() {
 axis = new PlotAxis(null, new LinearAxisScale(), new DecimalFormat("#.##"), 1);
 assertNull(axis.getLabel());
 assertSame(new LinearAxisScale(), axis.getScale());
 assertSame(new DecimalFormat("#.##"), axis.getNumberFormat());
 }

 @Test
 public void testInitializationWithLabelNumberFormatAndGridStyle() {
 axis = new PlotAxis("Test Label", null, new DecimalFormat("#.##"), 1);
 assertEquals("Test Label", axis.getLabel());
 assertSame(new LinearAxisScale(), axis.getScale());
 assertSame(new DecimalFormat("#.##"), axis.getNumberFormat());
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
 PlotAxisScale scale = new LogAxisScale();
 axis.setScale(scale);
 assertSame(scale, axis.getScale());
 }

 @Test
 public void testGetScale() {
 axis = new PlotAxis();
 PlotAxisScale scale = new LinearAxisScale();
 axis.setScale(scale);
 assertSame(scale, axis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 axis = new PlotAxis();
 NumberFormat format = new DecimalFormat("#.##");
 axis.setNumberFormat(format);
 assertSame(format, axis.getNumberFormat());
 }

 @Test
 public void testGetNumberFormat() {
 axis = new PlotAxis();
 NumberFormat format = new DecimalFormat("#.##");
 axis.setNumberFormat(format);
 assertSame(format, axis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 axis = new PlotAxis();
 axis.setGridStyle(2);
 assertEquals(2, axis.getGridStyle());
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
 public void testLowerBounds() {
 axis = new PlotAxis();
 axis.setLowerBounds(1.0);
 assertEquals(1.0, axis.getLowerBounds(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis = new PlotAxis();
 axis.setLowerBounds(1.0);
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
 public void testUpperBounds() {
 axis = new PlotAxis();
 axis.setUpperBounds(1.0);
 assertEquals(1.0, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis = new PlotAxis();
 axis.setUpperBounds(1.0);
 assertEquals("1.0", axis.upperBoundsAsString());
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
 public void testTickStepSize() {
 axis = new PlotAxis();
 axis.setTickStepSize(1.0);
 assertEquals(1.0, axis.getTickStepSize(), 0.0);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testSetTickStepSizeWithNegativeValue() {
 axis = new PlotAxis();
 axis.setTickStepSize(-1.0);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testSetAxisBounds() {
 fail("Abstract method, cannot test");
 }

 @Test
 public void testSetSize() {
 fail("Abstract method, cannot test");
 }

 @Test
 public void testScaleCoord() {
 axis = new PlotAxis();
 assertEquals(1, axis.scaleCoord(0.0));
 }

 @Test
 public void testClone() {
 PlotAxis cloneAxis = axis.clone();
 assertNotEquals(axis, cloneAxis);
 }
}