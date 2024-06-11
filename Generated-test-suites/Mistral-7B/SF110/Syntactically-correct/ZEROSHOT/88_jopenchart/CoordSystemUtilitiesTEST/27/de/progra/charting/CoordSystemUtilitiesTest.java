package de.progra.charting.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;

public class CoordSystemUtilitiesTest {

 private CoordSystem c;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @SuppressWarnings("deprecation")
 @Before
 public void setup() {
 c = new CoordSystem(new java.awt.Dimension(300, 400), new java.awt.geom.Path2D.Double());
 model = new ChartDataModel();
 constraints = new ChartDataModelConstraints(model, 0.0, 10.0, 1.0);
 constraints2 = new ChartDataModelConstraints(model, 10.0, 20.0, 1.0);
 }

 @Test
 public void testComputeLeftMargin() {
 CoordSystemUtilities utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 assertEquals(20, utils.computeLeftMargin());

 double xmin = 5.0;
 double xmax = 15.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 assertEquals(61, utils.computeLeftMargin());
 }

 @Test
 public void testComputeRightMargin() {
 CoordSystemUtilities utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 assertEquals(15, utils.computeRightMargin());
 }

 @Test
 public void testComputeTopMargin() {
 CoordSystemUtilities utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 assertEquals(15, utils.computeTopMargin());
 }

 @Test
 public void testComputeBottomMargin() {
 CoordSystemUtilities utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 assertEquals(20, utils.computeBottomMargin());

 double ymin = 5.0;
 double ymax = 15.0;
 constraints.setMinimumValue(ymin);
 constraints.setMaximumValue(ymax);
 utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 assertEquals(81, utils.computeBottomMargin());
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 CoordSystemUtilities utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 assertEquals(12, utils.computeXAxisLabelHeight());
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 CoordSystemUtilities utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 assertEquals(34, utils.computeYAxisLabelWidth());
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getFont()).thenReturn(c.getFont());
 when(g.isPaintLabels()).thenReturn(true);
 when(g.getColor()).thenReturn(Color.BLACK);

 CoordSystemUtilities utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 utils.drawNumericalXAxisTicks(g);

 verify(g, times(5)).draw(any(Shape.class));
 verify(g, times(5)).drawString(eq("5.0"), anyString(), anyFloat(), anyFloat());
 verify(g, times(5)).setColor(Color.BLACK);
 }

 @Test
 public void testDrawXAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getFont()).thenReturn(c.getFont());
 when(g.isPaintLabels()).thenReturn(true);
 when(g.getColor()).thenReturn(Color.BLACK);

 CoordSystemUtilities utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 utils.drawXAxisTicks(g);

 verify(g, times(11)).draw(any(Shape.class));
 verify(g, times(11)).drawString(anyString(), anyFloat(), anyFloat());
 }

 @Test
 public void testDrawYAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.getFont()).thenReturn(c.getFont());
 when(g.isPaintLabels()).thenReturn(true);
 when(g.getColor()).thenReturn(Color.BLACK);

 CoordSystemUtilities utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 utils.drawYAxisTicks(g);

 verify(g, times(10)).draw(any(Shape.class));
 verify(g, times(10)).drawString(eq("5.0"), anyString(), anyFloat(), anyFloat());
 }

 @Test
 public void testGetXAxisLine2D() {
 CoordSystemUtilities utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 Line2D line = utils.getXAxisLine2D();
 assertEquals(0.0, line.getX1(), 0.01);
 assertEquals(0.0, line.getY1(), 0.01);
 assertEquals(c.getBounds().getWidth(), line.getX2(), 0.01);
 assertEquals(0.0, line.getY2(), 0.01);
 }

 @Test
 public void testGetYAxisLine2D() {
 CoordSystemUtilities utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 Line2D line = utils.getYAxisLine2D();
 assertEquals(0.0, line.getX1(), 0.01);
 assertEquals(c.getBounds().getHeight(), line.getY1(), 0.01);
 assertEquals(0.0, line.getX2(), 0.01);
 assertEquals(0.0, line.getY2(), 0.01);
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 CoordSystemUtilities utils = new CoordSystemUtilities(c, constraints, constraints2, model);
 Line2D line = utils.getSecondYAxisLine2D();
 assertEquals(c.getBounds().getWidth(), line.getX1(), 0.01);
 assertEquals(0.0, line.getY1(), 0.01);
 assertEquals(c.getBounds().getWidth(), line.getX2(), 0.01);
 assertEquals(c.getBounds().getHeight(), line.getY2(), 0.01);
 }
}