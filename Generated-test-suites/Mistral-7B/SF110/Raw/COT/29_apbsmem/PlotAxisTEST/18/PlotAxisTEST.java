import org.junit.Test;
import static org.junit.Assert.*;
import jahuwaldt.plot.PlotAxis;

public class PlotAxisTest {

 private PlotAxis axis;

 @Test
 public void testConstructor1() {
 axis = new PlotAxis();
 assertNotNull(axis);
 assertEquals("", axis.getLabel());
 assertNull(axis.getScale());
 assertNull(axis.getNumberFormat());
 assertEquals(1, axis.getGridStyle());
 assertFalse(axis.isManualLB());
 assertFalse(axis.isManualUB());
 assertFalse(axis.isManualQuant());
 assertEquals(1.0, axis.getxA(), 0.0);
 assertEquals(1.0, axis.getxB(), 0.0);
 assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
 assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
 }

 @Test
 public void testConstructor2() {
 axis = new PlotAxis("Test Label", null, null, 0);
 assertNotNull(axis);
 assertEquals("Test Label", axis.getLabel());
 assertNull(axis.getScale());
 assertNull(axis.getNumberFormat());
 assertEquals(0, axis.getGridStyle());
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

 @Test(expected = NullPointerException.class)
 public void testSetScaleNull() {
 axis = new PlotAxis();
 axis.setScale(null);
 }

 @Test
 public void testSetNumberFormat() {
 axis = new PlotAxis();
 axis.setNumberFormat(NumberFormat.getInstance());
 assertSame(axis.getNumberFormat(), NumberFormat.getInstance());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNumberFormatNull() {
 axis = new PlotAxis();
 axis.setNumberFormat(null);
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
 public void testSetLowerBounds() {
 axis = new PlotAxis();
 axis.setLowerBounds(10.0);
 assertEquals(10.0, axis.getLowerBounds(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis = new PlotAxis();
 axis.setLowerBounds(10.0);
 assertEquals("10.0", axis.lowerBoundsAsString());
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
 axis.setUpperBounds(20.0);
 assertEquals(20.0, axis.getUpperBounds(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis = new PlotAxis();
 axis.setUpperBounds(20.0);
 assertEquals("20.0", axis.upperBoundsAsString());
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
 axis.setTickStepSize(5.5);
 assertEquals(5.5, axis.getTickStepSize(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTickStepSizeNegative() {
 axis = new PlotAxis();
 axis.setTickStepSize(-5.5);
 }

 @Test
 public void testSetAxisBoundsNull() {
 axis = new PlotAxis();
 assertNull(axis.getScale());
 assertNull(axis.getNumberFormat());
 try {
 axis.setAxisBounds(null);
 } catch (NullPointerException e) {
 // expected
 }
 }
}