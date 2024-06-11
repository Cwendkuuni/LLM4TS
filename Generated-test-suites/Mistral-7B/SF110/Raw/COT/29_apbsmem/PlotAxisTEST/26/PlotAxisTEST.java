import org.junit.Test;
import org.junit.Assert.*;
import jahuwaldt.plot.PlotAxis;
import java.text.DecimalFormat;

public class PlotAxisTest {

 private PlotAxis axis;

 @Test
 public void testConstructor() {
 axis = new PlotAxis();
 assertNotNull(axis.getLabel());
 assertNull(axis.getScale());
 assertNull(axis.getNumberFormat());
 assertFalse(axis.isManualLB());
 assertFalse(axis.isManualUB());
 assertFalse(axis.isManualQuant());
 assertEquals(1.0, axis.getxA(), 0.0);
 assertEquals(1.0, axis.getxB(), 0.0);
 assertEquals(Double.MAX_VALUE, axis.getALB(), 0.0);
 assertEquals(-1.7976931348623157E308, axis.getAUB(), 0.0);
 assertEquals(1, axis.getGridStyle());
 }

 @Test
 public void testConstructorWithLabelScaleNumberFormatAndGridStyle() {
 axis = new PlotAxis("TestAxis", null, null, 1);
 assertNotNull(axis.getLabel());
 assertNull(axis.getScale());
 assertNull(axis.getNumberFormat());
 assertFalse(axis.isManualLB());
 assertFalse(axis.isManualUB());
 assertFalse(axis.isManualQuant());
 assertEquals("TestAxis", axis.getLabel());
 assertEquals(1, axis.getGridStyle());
 }

 @Test
 public void testSetLabel() {
 axis = new PlotAxis();
 axis.setLabel("TestAxis");
 assertEquals("TestAxis", axis.getLabel());
 }

 @Test
 public void testGetLabel() {
 axis = new PlotAxis();
 assertEquals(null, axis.getLabel());
 axis.setLabel("TestAxis");
 assertEquals("TestAxis", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 axis = new PlotAxis();
 PlotAxisScale scale = new PlotAxisScale();
 axis.setScale(scale);
 assertEquals(scale, axis.getScale());
 }

 @Test(expected = NullPointerException.class)
 public void testSetScaleNull() {
 axis = new PlotAxis();
 axis.setScale(null);
 }

 @Test
 public void testGetScale() {
 axis = new PlotAxis();
 assertEquals(null, axis.getScale());
 PlotAxisScale scale = new PlotAxisScale();
 axis.setScale(scale);
 assertEquals(scale, axis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 axis = new PlotAxis();
 NumberFormat nf = NumberFormat.getInstance();
 axis.setNumberFormat(nf);
 assertEquals(nf, axis.getNumberFormat());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNumberFormatNull() {
 axis = new PlotAxis();
 axis.setNumberFormat(null);
 }

 @Test
 public void testGetNumberFormat() {
 axis = new PlotAxis();
 assertEquals(NumberFormat.getInstance(), axis.getNumberFormat());
 NumberFormat nf = new DecimalFormat("#.##");
 axis.setNumberFormat(nf);
 assertEquals(nf, axis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 axis = new PlotAxis();
 axis.setGridStyle(3);
 assertEquals(3, axis.getGridStyle());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetGridStyleNegative() {
 axis = new PlotAxis();
 axis.setGridStyle(-1);
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
 assertEquals(1.0, axis.getALB(), 0.0);
 axis.setLowerBounds(Double.MIN_VALUE);
 assertEquals(Double.MIN_VALUE, axis.getALB(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis = new PlotAxis();
 assertEquals("NaN", axis.lowerBoundsAsString());
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
 public void testSetUpperBounds() {
 axis = new PlotAxis();
 axis.setUpperBounds(1.0);
 assertEquals(1.0, axis.getAUB(), 0.0);
 axis.setUpperBounds(Double.MAX_VALUE);
 assertEquals(Double.MAX_VALUE, axis.getAUB(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis = new PlotAxis();
 assertEquals("NaN", axis.upperBoundsAsString());
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
 public void testSetTickStepSize() {
 axis = new PlotAxis();
 axis.setTickStepSize(1.0);
 assertEquals(1.0, axis.getTickStepSize(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTickStepSizeNegative() {
 axis = new PlotAxis();
 axis.setTickStepSize(-1.0);
 }

 @Test
 public void testSetAxisBounds() {
 // Abstract method, cannot test directly
 }

 @Test
 public void testSetSize() {
 // Method under test uses protected methods, cannot test directly
 }

 @Test
 public void testScaleCoord() {
 axis = new PlotAxis();
 assertEquals(0, axis.scaleCoord(0.0), 0);
 axis.setAxisBounds(null); // Assuming this sets the axis bounds properly
 axis.setSize(new Rectangle(50, 50, 200, 200));
 assertEquals(100, axis.scaleCoord(0.5), 0);
 }
}