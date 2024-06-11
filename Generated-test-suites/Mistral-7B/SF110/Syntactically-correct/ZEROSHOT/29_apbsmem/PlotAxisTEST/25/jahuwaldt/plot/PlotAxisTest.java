package jahuwaldt.plot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.*;
import java.text.DecimalFormat;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;
import jahuwaldt.plot.PlotRunList;

public class PlotAxisTest {

 private PlotAxis axis;
 private PlotAxis axis2;
 private PlotAxisScale scale;
 private PlotAxisScale scale2;
 private PlotRunList runList;

 @Before
 public void setUp() {
 axis = new PlotAxis() {
 @Override
 protected void resizeAxis(Rectangle rectangle) {}
 };
 axis2 = new PlotAxis() {
 @Override
 protected void resizeAxis(Rectangle rectangle) {}
 };
 scale = new LinearAxisScale();
 scale2 = new LinearAxisScale();
 runList = new PlotRunList();
 }

 @Test
 public void testConstructor() {
 axis.setLabel("Axis Label");
 axis.setScale(scale);
 axis.setNumberFormat(new DecimalFormat("#.##"));
 axis.setGridStyle(PlotAxis.kMajorGrid);
 axis.setAxisBounds(runList);

 assertEquals("Axis Label", axis.getLabel());
 assertEquals(scale, axis.getScale());
 assertNotNull(axis.getNumberFormat());
 assertEquals(PlotAxis.kMajorGrid, axis.getGridStyle());
 }

 @Test
 public void testGettersAndSetters() {
 axis.setLabel("Axis Label");
 axis.setScale(scale);
 axis.setNumberFormat(new DecimalFormat("#.##"));
 axis.setGridStyle(PlotAxis.kMajorGrid);

 assertEquals("Axis Label", axis.getLabel());
 axis.setLabel("New Axis Label");
 assertEquals("New Axis Label", axis.getLabel());

 assertEquals(scale, axis.getScale());
 axis.setScale(scale2);
 assertNotSame(scale, axis.getScale());
 assertEquals(scale2, axis.getScale());

 assertNotNull(axis.getNumberFormat());
 axis.setNumberFormat(null);
 assertNull(axis.getNumberFormat());

 assertEquals(PlotAxis.kMajorGrid, axis.getGridStyle());
 axis.setGridStyle(PlotAxis.kTickMarksOnly);
 assertEquals(PlotAxis.kTickMarksOnly, axis.getGridStyle());
 }

 @Test
 public void testManualBounds() {
 axis.manualLowerBounds(true);
 axis.setLowerBounds(1.0);
 assertEquals(1.0, axis.getLowerBounds(), 0.001);
 axis.manualLowerBounds(false);
 axis.setLowerBounds(Double.NaN);
 assertFalse(Double.isNaN(axis.getLowerBounds()));

 axis.manualUpperBounds(true);
 axis.setUpperBounds(2.0);
 assertEquals(2.0, axis.getUpperBounds(), 0.001);
 axis.manualUpperBounds(false);
 axis.setUpperBounds(Double.POSITIVE_INFINITY);
 assertFalse(Double.isInfinite(axis.getUpperBounds()));
 }

 @Test
 public void testManualTickStepSize() {
 axis.manualTickStepSize(true);
 axis.setTickStepSize(1.5);
 assertEquals(1.5, axis.getTickStepSize(), 0.001);
 axis.manualTickStepSize(false);
 axis.setTickStepSize(Double.NaN);
 assertFalse(Double.isNaN(axis.getTickStepSize()));
 }

 @Test
 public void testSetAxisBounds() {
 axis.manualLowerBounds(false);
 axis.manualUpperBounds(false);
 axis.setAxisBounds(runList);

 axis2.manualLowerBounds(true);
 axis2.manualUpperBounds(true);
 axis2.setAxisBounds(runList);

 assertEquals(axis.getLowerBounds(), axis2.getLowerBounds(), 0.001);
 assertEquals(axis.getUpperBounds(), axis2.getUpperBounds(), 0.001);
 assertEquals(axis.getTickStepSize(), axis2.getTickStepSize(), 0.001);
 }

 @Test
 public void testClone() {
 axis.setLabel("Axis Label");
 axis.setScale(scale);
 axis.setNumberFormat(new DecimalFormat("#.##"));
 axis.setGridStyle(PlotAxis.kMajorGrid);
 axis.setAxisBounds(runList);

 axis2 = (PlotAxis)axis.clone();

 assertEquals("Axis Label", axis2.getLabel());
 assertNotSame(axis, axis2);
 assertEquals(axis.getScale(), axis2.getScale());
 assertNotNull(axis2.getNumberFormat());
 assertEquals(PlotAxis.kMajorGrid, axis2.getGridStyle());
 assertEquals(axis.getLowerBounds(), axis2.getLowerBounds(), 0.001);
 assertEquals(axis.getUpperBounds(), axis2.getUpperBounds(), 0.001);
 assertEquals(axis.getTickStepSize(), axis2.getTickStepSize(), 0.001);
 }

 @Test(expected = CloneNotSupportedException.class)
 public void testCloneException() {
 axis.setLabel("Axis Label");
 axis.setScale(null);
 axis.setNumberFormat(new DecimalFormat("#.##"));
 axis.setGridStyle(PlotAxis.kMajorGrid);
 axis.setAxisBounds(runList);

 try {
 axis2 = (PlotAxis)axis.clone();
 } catch (CloneNotSupportedException e) {
 throw e;
 }
 }
}