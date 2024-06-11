import org.junit.Test;
import static org.junit.Assert.*;
import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.LinearAxisScale;

public class PlotAxisTest {

 @Test
 public void testDefaultConstructor() {
 PlotAxis axis = new PlotAxis();
 assertNotNull(axis);
 assertEquals("", axis.getLabel());
 assertSame(LinearAxisScale.class, axis.getScale().getClass());
 assertSame(NumberFormat.getInstance().getClass(), axis.getNumberFormat().getClass());
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
 public void testCustomConstructor() {
 PlotAxis axis = new PlotAxis("Test Axis", new LinearAxisScale(), null, 1);
 assertNotNull(axis);
 assertEquals("Test Axis", axis.getLabel());
 assertSame(LinearAxisScale.class, axis.getScale().getClass());
 assertNull(axis.getNumberFormat());
 assertEquals(1, axis.getGridStyle());
 }

 @Test
 public void testSetLabel() {
 PlotAxis axis = new PlotAxis();
 axis.setLabel("Test Axis");
 assertEquals("Test Axis", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 PlotAxis axis = new PlotAxis();
 axis.setScale(new LinearAxisScale());
 assertSame(LinearAxisScale.class, axis.getScale().getClass());
 }

 @Test
 public void testSetNumberFormat() {
 PlotAxis axis = new PlotAxis();
 NumberFormat numberFormat = NumberFormat.getNumberInstance();
 axis.setNumberFormat(numberFormat);
 assertSame(numberFormat.getClass(), axis.getNumberFormat().getClass());
 }

 @Test
 public void testSetGridStyle() {
 PlotAxis axis = new PlotAxis();
 axis.setGridStyle(2);
 assertEquals(2, axis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 PlotAxis axis = new PlotAxis();
 axis.setLowerBounds(10.0);
 assertTrue(axis.isManualLB());
 assertEquals(10.0, axis.getALB(), 0.0);
 }

 @Test
 public void testSetLowerBoundsWithScale() {
 PlotAxis axis = new PlotAxis("Test Axis", null, null, 1);
 axis.setLowerBounds(10.0);
 assertTrue(axis.isManualLB());
 assertEquals(10.0, axis.getALB(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLowerBounds() {
 PlotAxis axis = new PlotAxis();
 axis.setLowerBounds(-10.0);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullLowerBounds() {
 PlotAxis axis = new PlotAxis();
 try {
 axis.setLowerBounds(null);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testManualUpperBounds() {
 PlotAxis axis = new PlotAxis();
 axis.setUpperBounds(20.0);
 assertTrue(axis.isManualUB());
 assertEquals(20.0, axis.getAUB(), 0.0);
 }

 @Test
 public void testSetUpperBoundsWithScale() {
 PlotAxis axis = new PlotAxis("Test Axis", null, null, 1);
 axis.setUpperBounds(20.0);
 assertTrue(axis.isManualUB());
 assertEquals(20.0, axis.getAUB(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeUpperBounds() {
 PlotAxis axis = new PlotAxis();
 axis.setUpperBounds(-10.0);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullUpperBounds() {
 PlotAxis axis = new PlotAxis();
 try {
 axis.setUpperBounds(null);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testManualTickStepSize() {
 PlotAxis axis = new PlotAxis();
 axis.setTickStepSize(2.5);
 assertTrue(axis.isManualQuant());
 assertEquals(2.5, axis.getTickStepSize(), 0.0);
 }

 @Test
 public void testSetTickStepSizeWithScale() {
 PlotAxis axis = new PlotAxis("Test Axis", null, null, 1);
 axis.setTickStepSize(2.5);
 assertTrue(axis.isManualQuant());
 assertEquals(2.5, axis.getTickStepSize(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeTickStepSize() {
 PlotAxis axis = new PlotAxis();
 axis.setTickStepSize(-2.5);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTickStepSize() {
 PlotAxis axis = new PlotAxis();
 try {
 axis.setTickStepSize(null);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 fail("Should throw IllegalArgumentException");
 }
}