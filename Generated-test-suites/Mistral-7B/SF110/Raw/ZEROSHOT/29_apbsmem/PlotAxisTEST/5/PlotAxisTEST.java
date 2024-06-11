import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PlotAxisTest {

 private PlotAxis plotAxis;

 @Before
 public void setUp() {
 plotAxis = new PlotAxis() {
 @Override
 public void setAxisBounds(PlotRunList p0) {
 // no implementation needed for testing
 }

 @Override
 protected void resizeAxis(Rectangle p0) {
 // no implementation needed for testing
 }

 @Override
 public void draw(Graphics p0, Component p1, Rectangle p2) {
 // no implementation needed for testing
 }

 @Override
 public int scaleCoord(double x) {
 return (int)(super.xA * x + super.xB);
 }
 };
 }

 @Test
 public void testConstructor() {
 PlotAxis axis = new PlotAxis();
 assertNotNull(axis.getLabel());
 assertNull(axis.getScale());
 assertNull(axis.getNumberFormat());
 assertEquals(1, axis.getGridStyle());
 }

 @Test
 public void testConstructorWithLabelScaleFormatGridStyle() {
 PlotAxis axis = new PlotAxis("Test Label", new LinearAxisScale(), NumberFormat.getInstance(), 2);
 assertNotNull(axis.getLabel());
 assertNotNull(axis.getScale());
 assertNotNull(axis.getNumberFormat());
 assertEquals(2, axis.getGridStyle());
 }

 @Test
 public void testSetLabel() {
 plotAxis.setLabel("Test Label");
 assertEquals("Test Label", plotAxis.getLabel());
 }

 @Test
 public void testSetScale() {
 PlotAxisScale scale = new LogAxisScale();
 plotAxis.setScale(scale);
 assertNotNull(plotAxis.getScale());
 assertEquals(scale.getClass(), plotAxis.getScale().getClass());
 }

 @Test
 public void testSetNumberFormat() {
 NumberFormat numberFormat = NumberFormat.getNumberInstance();
 plotAxis.setNumberFormat(numberFormat);
 assertNotNull(plotAxis.getNumberFormat());
 assertEquals(numberFormat.getClass(), plotAxis.getNumberFormat().getClass());
 }

 @Test
 public void testSetGridStyle() {
 plotAxis.setGridStyle(3);
 assertEquals(3, plotAxis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 plotAxis.manualLowerBounds(true);
 assertTrue(plotAxis.isManualLB());
 }

 @Test
 public void testSetLowerBounds() {
 plotAxis.setLowerBounds(1.0);
 assertEquals(1.0, plotAxis.getLowerBounds(), 0.001);
 }

 @Test
 public void testLowerBoundsAsString() {
 plotAxis.setLowerBounds(1.0);
 assertEquals("1.0", plotAxis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 plotAxis.manualUpperBounds(true);
 assertTrue(plotAxis.isManualUB());
 }

 @Test
 public void testSetUpperBounds() {
 plotAxis.setUpperBounds(2.0);
 assertEquals(2.0, plotAxis.getUpperBounds(), 0.001);
 }

 @Test
 public void testUpperBoundsAsString() {
 plotAxis.setUpperBounds(2.0);
 assertEquals("2.0", plotAxis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 plotAxis.manualTickStepSize(true);
 assertTrue(plotAxis.isManualQuant());
 }

 @Test
 public void testSetTickStepSize() {
 plotAxis.setTickStepSize(0.5);
 assertEquals(0.5, plotAxis.getTickStepSize(), 0.001);
 }

}