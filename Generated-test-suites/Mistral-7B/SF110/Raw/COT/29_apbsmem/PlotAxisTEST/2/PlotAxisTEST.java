import org.junit.Test;
import static org.junit.Assert.*;
import jahuwaldt.plot.PlotAxis;

public class PlotAxisTest {

 private PlotAxis axis;

 @Before
 public void setup() {
 axis = new PlotAxis();
 }

 @Test
 public void testConstructor() {
 axis = new PlotAxis();
 assertNotNull(axis.getLabel());
 assertNotNull(axis.getScale());
 assertNotNull(axis.getNumberFormat());
 assertSame(axis.getGridStyle(), PlotAxis.kTickMarksOnly);
 }

 @Test
 public void testSetLabel() {
 axis.setLabel("Test Label");
 assertEquals("Test Label", axis.getLabel());
 }

 @Test
 public void testGetLabel() {
 axis.setLabel("Test Label");
 assertEquals("Test Label", axis.getLabel());
 }

 @Test(expected = NullPointerException.class)
 public void testSetScaleNull() {
 axis.setScale(null);
 }

 @Test
 public void testGetScale() {
 axis.setScale(new LinearAxisScale());
 assertEquals(axis.getScale().getClass(), LinearAxisScale.class);
 }

 @Test
 public void testSetNumberFormatNull() {
 axis.setNumberFormat(null);
 }

 @Test
 public void testGetNumberFormat() {
 axis.setNumberFormat(NumberFormat.getInstance());
 assertEquals(axis.getNumberFormat().getClass(), NumberFormat.class);
 }

 @Test
 public void testSetGridStyle() {
 axis.setGridStyle(PlotAxis.kTick);
 assertEquals(axis.getGridStyle(), PlotAxis.kTick);
 }

 @Test
 public void testGetGridStyle() {
 axis.setGridStyle(PlotAxis.kTick);
 assertEquals(axis.getGridStyle(), PlotAxis.kTick);
 }

 @Test
 public void testManualLowerBounds() {
 axis.manualLowerBounds(true);
 assertTrue(axis.isManualLB());
 }

 @Test
 public void testSetLowerBounds() {
 axis.setLowerBounds(5.0);
 assertEquals(5.0, axis.getLowerBounds(), 0.001);
 }

 @Test
 public void testLowerBoundsAsString() {
 axis.setLowerBounds(5.0);
 assertEquals("5.0", axis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 axis.manualUpperBounds(true);
 assertTrue(axis.isManualUB());
 }

 @Test
 public void testSetUpperBounds() {
 axis.setUpperBounds(10.0);
 assertEquals(10.0, axis.getUpperBounds(), 0.001);
 }

 @Test
 public void testUpperBoundsAsString() {
 axis.setUpperBounds(10.0);
 assertEquals("10.0", axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 axis.manualTickStepSize(true);
 assertTrue(axis.isManualQuant());
 }

 @Test
 public void testSetTickStepSize() {
 axis.setTickStepSize(2.5);
 assertEquals(2.5, axis.getTickStepSize(), 0.001);
 }

 @Test
 public void testSetAxisBoundsNull() {
 axis.setAxisBounds(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetSizeNull() {
 axis.setSize(null);
 }

 @Test
 public void testSetSize() {
 // TODO: Implement this test
 }

}