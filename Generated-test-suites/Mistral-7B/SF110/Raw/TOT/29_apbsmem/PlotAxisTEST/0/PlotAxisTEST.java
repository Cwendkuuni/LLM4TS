import org.junit.Test;
import static org.junit.Assert.*;
import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.LinearAxisScale;

public class PlotAxisTest {

 @Test
 public void testDefaultConstructor() {
 PlotAxis plotAxis = new PlotAxis();
 assertEquals("", plotAxis.getLabel());
 assertNull(plotAxis.getScale());
 assertNotNull(plotAxis.getNumberFormat());
 assertEquals(1.0, plotAxis.getxA(), 0.0);
 assertEquals(1.0, plotAxis.getxB(), 0.0);
 assertEquals(Double.MAX_VALUE, plotAxis.getALB(), 0.0);
 assertEquals(-1.7976931348623157E308, plotAxis.getAUB(), 0.0);
 assertEquals(1, plotAxis.getGridStyle());
 assertEquals(LinearAxisScale.class, plotAxis.getScale().getClass());
 }

 @Test
 public void testCustomConstructor() {
 PlotAxis plotAxis = new PlotAxis("Test Label", new LinearAxisScale(), null, 1);
 assertEquals("Test Label", plotAxis.getLabel());
 assertSame(plotAxis.getScale(), new LinearAxisScale());
 assertNull(plotAxis.getNumberFormat());
 assertEquals(1.0, plotAxis.getxA(), 0.0);
 assertEquals(1.0, plotAxis.getxB(), 0.0);
 assertEquals(Double.MAX_VALUE, plotAxis.getALB(), 0.0);
 assertEquals(-1.7976931348623157E308, plotAxis.getAUB(), 0.0);
 assertEquals(1, plotAxis.getGridStyle());
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
 assertEquals(scale, plotAxis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 PlotAxis plotAxis = new PlotAxis();
 NumberFormat format = NumberFormat.getNumberInstance();
 plotAxis.setNumberFormat(format);
 assertEquals(format, plotAxis.getNumberFormat());
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
 assertTrue(plotAxis.isManualLB());
 }

 @Test
 public void testSetLowerBounds() {
 PlotAxis plotAxis = new PlotAxis();
 double lowerBounds = 10.0;
 plotAxis.setLowerBounds(lowerBounds);
 assertEquals(lowerBounds, plotAxis.getALB(), 0.0);
 }

 @Test
 public void testLowerBoundsAsString() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setLowerBounds(10.0);
 assertEquals("10.0", plotAxis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.manualUpperBounds(true);
 assertTrue(plotAxis.isManualUB());
 }

 @Test
 public void testSetUpperBounds() {
 PlotAxis plotAxis = new PlotAxis();
 double upperBounds = 20.0;
 plotAxis.setUpperBounds(upperBounds);
 assertEquals(upperBounds, plotAxis.getAUB(), 0.0);
 }

 @Test
 public void testUpperBoundsAsString() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setUpperBounds(20.0);
 assertEquals("20.0", plotAxis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.manualTickStepSize(true);
 assertTrue(plotAxis.isManualQuant());
 }

 @Test
 public void testSetTickStepSize() {
 PlotAxis plotAxis = new PlotAxis();
 double tickStepSize = 5.0;
 plotAxis.setTickStepSize(tickStepSize);
 assertEquals(tickStepSize, plotAxis.getQuantum(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidGridStyle() {
 PlotAxis plotAxis = new PlotAxis();
 plotAxis.setGridStyle(-1);
 }
}