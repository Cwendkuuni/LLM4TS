import org.junit.Test;
import static org.junit.Assert.*;
import jahuwaldt.plot.PlotAxis;

public class PlotAxisTest {

 private PlotAxis axis;

 @Before
 public void setup() {
 axis = new PlotAxis() {{
 setLabel("TestAxis");
 setScale(new LinearAxisScale());
 setNumberFormat(NumberFormat.getInstance());
 setGridStyle(PlotAxis.kTickMarksOnly);
 }};
 }

 @Test
 public void testConstructor() {
 PlotAxis axis = new PlotAxis();
 assertNotNull(axis);
 }

 @Test
 public void testSetLabel() {
 axis.setLabel("TestLabel");
 assertEquals("TestLabel", axis.getLabel());
 }

 @Test
 public void testSetScale() {
 PlotAxisScale scale = new LogAxisScale();
 axis.setScale(scale);
 assertEquals(scale, axis.getScale());
 }

 @Test
 public void testSetNumberFormat() {
 NumberFormat nf = NumberFormat.getNumberInstance();
 axis.setNumberFormat(nf);
 assertEquals(nf, axis.getNumberFormat());
 }

 @Test
 public void testSetGridStyle() {
 axis.setGridStyle(PlotAxis.kMajorGrid);
 assertEquals(PlotAxis.kMajorGrid, axis.getGridStyle());
 }

 @Test
 public void testManualLowerBounds() {
 axis.manualLowerBounds(true);
 assertTrue(axis.isManualLB());
 axis.manualLowerBounds(false);
 assertFalse(axis.isManualLB());
 }

 @Test
 public void testSetLowerBounds() {
 axis.setLowerBounds(0.0);
 assertEquals(0.0, axis.getLowerBounds(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetLowerBoundsNegative() {
 axis.setLowerBounds(-1.0);
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test
 public void testLowerBoundsAsString() {
 axis.setLowerBounds(1.23);
 assertEquals("1.23", axis.lowerBoundsAsString());
 }

 @Test
 public void testManualUpperBounds() {
 axis.manualUpperBounds(true);
 assertTrue(axis.isManualUB());
 axis.manualUpperBounds(false);
 assertFalse(axis.isManualUB());
 }

 @Test
 public void testSetUpperBounds() {
 axis.setUpperBounds(2.46);
 assertEquals(2.46, axis.getUpperBounds(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetUpperBoundsNegative() {
 axis.setUpperBounds(-1.23);
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test
 public void testUpperBoundsAsString() {
 axis.setUpperBounds(3.21);
 assertEquals("3.21", axis.upperBoundsAsString());
 }

 @Test
 public void testManualTickStepSize() {
 axis.manualTickStepSize(true);
 assertTrue(axis.isManualQuant());
 axis.manualTickStepSize(false);
 assertFalse(axis.isManualQuant());
 }

 @Test
 public void testSetTickStepSize() {
 axis.setTickStepSize(0.5);
 assertEquals(0.5, axis.getTickStepSize(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTickStepSizeNegative() {
 axis.setTickStepSize(-0.5);
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test
 public void testSetAxisBounds() {
 fail("Abstract method cannot be tested directly");
 }

 @Test
 public void testSetSize() {
 Rectangle plotFrame = new Rectangle(10, 10, 200, 200);
 axis.setSize(plotFrame);
 }

 @Test
 public void testSetAxisTickLimits() {
 axis.setAxisTickLimits();
 }

 @Test
 public void testResizeAxis() {
 fail("Abstract method cannot be tested directly");
 }

 @Test
 public void testDraw() {
 fail("Abstract method cannot be tested directly");
 }

 @Test
 public void testScaleCoord() {
 double x = 1.5;
 int expected = (int)(axis.xA * axis.scale.func(x) + axis.xB);
 int result = axis.scaleCoord(x);
 assertEquals(expected, result);
 }

 @Test
 public void testClone() {
 PlotAxis clonedAxis = (PlotAxis)axis.clone();
 assertEquals(axis, clonedAxis);
 }
}