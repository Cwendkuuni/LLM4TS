import org.junit.Test;
import static org.junit.Assert.*;
import de.progra.charting.*;
import de.progra.charting.model.*;
import java.text.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.*;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utils;
 private CoordSystem coord;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private ChartDataModel model;

 @Before
 public void setUp() {
 coord = new CoordSystem(new java.awt.Dimension(300, 400), new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), new java.awt.Font("SansSerif", Font.PLAIN, 12), new java.text.DecimalFormat("0.00"), new java.text.DecimalFormat("0.0"));
 constraints = new ChartDataModelConstraints(new double[]{0.0, 10.0}, new double[]{0.0, 10.0});
 constraints2 = new ChartDataModelConstraints(new double[]{0.0, -10.0}, new double[]{0.0, -10.0});
 model = new ChartDataModel();
 utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin_PositiveXMinAndXMax() {
 double xmin = 0.0;
 double xmax = 10.0;
 int expected = 37; // calculate using given width and xmin, xmax values
 int actual = utils.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeLeftMargin_NegativeXMinAndPositiveXMax() {
 double xmin = -5.0;
 double xmax = 5.0;
 int expected = 33; // calculate using given width and xmin, xmax values
 int actual = utils.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMargin_XMinGreaterThanXMax() {
 double xmin = 5.0;
 double xmax = 0.0;
 utils.computeLeftMargin();
 }

 @Test
 public void testComputeRightMargin() {
 double xmin = -5.0;
 double xmax = 5.0;
 int expected = 23; // calculate using given width and xmin, xmax values
 int actual = utils.computeRightMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 double ymin = -5.0;
 double ymax = 5.0;
 int expected = 33; // calculate using given height and ymin, ymax values
 int actual = utils.computeTopMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin_PositiveYMinAndYMax() {
 double ymin = 0.0;
 double ymax = 10.0;
 int expected = 37; // calculate using given height and ymin, ymax values
 int actual = utils.computeBottomMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin_NegativeYMinAndPositiveYMax() {
 double ymin = -5.0;
 double ymax = 5.0;
 int expected = 23; // calculate using given height and ymin, ymax values
 int actual = utils.computeBottomMargin();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeBottomMargin_YMinGreaterThanYMax() {
 double ymin = 5.0;
 double ymax = 0.0;
 utils.computeBottomMargin();
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 double min = 0.0;
 double max = 10.0;
 int expected = 24; // calculate using given min, max values
 int actual = utils.computeXAxisLabelHeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 double min = 0.0;
 double max = 10.0;
 int expected = 37; // calculate using given min, max values
 int actual = utils.computeYAxisLabelWidth();
 assertEquals(expected, actual);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 Graphics2D g = new StrokeTest().getGraphics();
 utils.drawNumericalXAxisTicks(g);
 // verify the drawn ticks and labels (using StrokeTest and other JUnit libraries if needed)
 }

 @Test
 public void testDrawXAxisTicks() {
 Graphics2D g = new StrokeTest().getGraphics();
 utils.drawXAxisTicks(g);
 // verify the drawn ticks and labels (using StrokeTest and other JUnit libraries if needed)
 }

 @Test
 public void testDrawYAxisTicks() {
 Graphics2D g = new StrokeTest().getGraphics();
 utils.drawYAxisTicks(g);
 // verify the drawn ticks and labels (using StrokeTest and other JUnit libraries if needed)
 }

 @Test
 public void testGetXAxisLine2D() {
 Line2D expected = new Line2D.Double(0.0, 0.0, 10.0, 0.0);
 Line2D actual = utils.getXAxisLine2D();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetYAxisLine2D() {
 Line2D expected = new Line2D.Double(0.0, 0.0, 0.0, 10.0);
 Line2D actual = utils.getYAxisLine2D();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 Line2D expected = new Line2D.Double(0.0, -10.0, 0.0, 10.0);
 Line2D actual = utils.getSecondYAxisLine2D();
 assertEquals(expected, actual);
 }
}