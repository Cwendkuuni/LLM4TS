package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private CoordSystem coordSystem;

 @Before
 public void setUp() {
 model = new ChartDataModel();
 constraints = new ChartDataModelConstraints(new DecimalFormat("#.##"), new DecimalFormat("#.##"), 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, new Color(0, 0, 0), new Color(255, 255, 255), 15.0, 15.0, 20.0, 20.0, false);
 constraints2 = new ChartDataModelConstraints(new DecimalFormat("#.##"), new DecimalFormat("#.##"), 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, new Color(255, 0, 0), new Color(0, 0, 255), 15.0, 15.0, 20.0, 20.0, true);
 coordSystem = new CoordSystem(500, 500, 100.0, 100.0);
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 double xmin = -10.0;
 double xmax = 10.0;
 DecimalFormat xFormat = new DecimalFormat("#.##");
 DecimalFormat yFormat = new DecimalFormat("#.##");
 ChartDataModelConstraints constraintsNegativeX = new ChartDataModelConstraints(xFormat, yFormat, 1.0, 1.0, -10.0, -5.0, -5.0, -5.0, -5.0, new Color(0, 0, 0), new Color(255, 255, 255), 15.0, 15.0, 20.0, 20.0, false);
 coordSystem.setConstraints(constraintsNegativeX);
 ChartDataModelConstraints constraintsPositiveX = new ChartDataModelConstraints(xFormat, yFormat, 1.0, 1.0, 10.0, 5.0, 5.0, 5.0, 5.0, new Color(0, 0, 0), new Color(255, 255, 255), 15.0, 15.0, 20.0, 20.0, false);
 coordSystem.setConstraints2(constraintsPositiveX);
 model.setDataModelConstraints(constraintsPositiveX);
 model.setSecondDataModelConstraints(constraintsNegativeX);
 int expectedLeftMargin = 114; // 20 + (6 + computeYAxisLabelWidth()) + (6 + computeXAxisLabelWidth())
 int result = utilities.computeLeftMargin();
 assertEquals(expectedLeftMargin, result);

 xmin = -30.0;
 xmax = 30.0;
 constraintsNegativeX.setMinimumColumnValue(-30.0);
 constraintsPositiveX.setMinimumColumnValue(30.0);
 coordSystem.setConstraints(constraintsPositiveX);
 coordSystem.setConstraints2(constraintsNegativeX);
 model.setDataModelConstraints(constraintsPositiveX);
 model.setSecondDataModelConstraints(constraintsNegativeX);
 expectedLeftMargin = 146; // 20 + (6 + computeYAxisLabelWidth()) + (6 + computeXAxisLabelWidth()) + 22 (for xmin)
 result = utilities.computeLeftMargin();
 assertEquals(expectedLeftMargin, result);
 }

 @Test
 public void testComputeRightMargin() {
 double xmin = -10.0;
 double xmax = 10.0;
 DecimalFormat xFormat = new DecimalFormat("#.##");
 DecimalFormat yFormat = new DecimalFormat("#.##");
 ChartDataModelConstraints constraintsNegativeX = new ChartDataModelConstraints(xFormat, yFormat, 1.0, 1.0, -10.0, -5.0, -5.0, -5.0, -5.0, new Color(0, 0, 0), new Color(255, 255, 255), 15.0, 15.0, 20.0, 20.0, false);
 coordSystem.setConstraints(constraintsNegativeX);
 ChartDataModelConstraints constraintsPositiveX = new ChartDataModelConstraints(xFormat, yFormat, 1.0, 1.0, 10.0, 5.0, 5.0, 5.0, 5.0, new Color(0, 0, 0), new Color(255, 255, 255), 15.0, 15.0, 20.0, 20.0, false);
 coordSystem.setConstraints2(constraintsPositiveX);
 model.setDataModelConstraints(constraintsPositiveX);
 model.setSecondDataModelConstraints(constraintsNegativeX);
 int expectedRightMargin = 31; // 15 + (width of "10.00")
 int result = utilities.computeRightMargin();
 assertEquals(expectedRightMargin, result);
 }

 @Test
 public void testComputeTopMargin() {
 double ymin = -10.0;
 double ymax = 10.0;
 DecimalFormat xFormat = new DecimalFormat("#.##");
 DecimalFormat yFormat = new DecimalFormat("#.##");
 ChartDataModelConstraints constraintsNegativeY = new ChartDataModelConstraints(xFormat, yFormat, 1.0, 1.0, -10.0, -5.0, -5.0, -5.0, -5.0, new Color(0, 0, 0), new Color(255, 255, 255), 15.0, 15.0, 20.0, 20.0, false);
 coordSystem.setConstraints(constraintsNegativeY);
 ChartDataModelConstraints constraintsPositiveY = new ChartDataModelConstraints(xFormat, yFormat, 1.0, 1.0, 10.0, 5.0, 5.0, 5.0, 5.0, new Color(0, 0, 0), new Color(255, 255, 255), 15.0, 15.0, 20.0, 20.0, false);
 coordSystem.setConstraints2(constraintsPositiveY);
 model.setDataModelConstraints(constraintsPositiveY);
 model.setSecondDataModelConstraints(constraintsNegativeY);
 int expectedTopMargin = 48; // 15 + (height of "10.00") + 15
 int result = utilities.computeTopMargin();
 assertEquals(expectedTopMargin, result);
 }

 @Test
 public void testComputeBottomMargin() {
 double ymin = -10.0;
 double ymax = 10.0;
 DecimalFormat xFormat = new DecimalFormat("#.##");
 DecimalFormat yFormat = new DecimalFormat("#.##");
 ChartDataModelConstraints constraintsNegativeY = new ChartDataModelConstraints(xFormat, yFormat, 1.0, 1.0, -10.0, -5.0, -5.0, -5.0, -5.0, new Color(0, 0, 0), new Color(255, 255, 255), 15.0, 15.0, 20.0, 20.0, false);
 coordSystem.setConstraints(constraintsNegativeY);
 ChartDataModelConstraints constraintsPositiveY = new ChartDataModelConstraints(xFormat, yFormat, 1.0, 1.0, 10.0, 5.0, 5.0, 5.0, 5.0, new Color(0, 0, 0), new Color(255, 255, 255), 15.0, 15.0, 20.0, 20.0, false);
 coordSystem.setConstraints2(constraintsPositiveY);
 model.setDataModelConstraints(constraintsPositiveY);
 model.setSecondDataModelConstraints(constraintsNegativeY);
 int expectedBottomMargin = 48; // 15 + (height of "10.00") + 15
 int result = utilities.computeBottomMargin();
 assertEquals(expectedBottomMargin, result);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 double min = -10.0;
 double max = 10.0;
 DecimalFormat xFormat = new DecimalFormat("#.##");
 DecimalFormat yFormat = new DecimalFormat("#.##");
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(xFormat, yFormat, 1.0, 1.0, min, max, 1.0, 1.0, 1.0, 1.0, new Color(0, 0, 0), new Color(255, 255, 255), 15.0, 15.0, 20.0, 20.0, false);
 coordSystem.setConstraints(constraints);
 int expectedHeight = 21; // 15 + 6
 int result = utilities.computeXAxisLabelHeight();
 assertEquals(expectedHeight, result);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 double min = -10.0;
 double max = 10.0;
 DecimalFormat xFormat = new DecimalFormat("#.##");
 DecimalFormat yFormat = new DecimalFormat("#.##");
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(xFormat, yFormat, 1.0, 1.0, min, max, 1.0, 1.0, 1.0, 1.0, new Color(0, 0, 0), new Color(255, 255, 255), 15.0, 15.0, 20.0, 20.0, false);
 coordSystem.setConstraints(constraints);
 int expectedWidth = 81; // 15 + 62 (width of "10.00")
 int result = utilities.computeYAxisLabelWidth();
 assertEquals(expectedWidth, result);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 AffineTransform at = new AffineTransform();
 Graphics2D g = null;
 int min = -10;
 int max = 10;
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, null, 1.0, 1.0, min, max, 1.0, 1.0, 1.0, 1.0, new Color(0, 0, 0), new Color(255, 255, 255), 15.0, 15.0, 20.0, 20.0, false);
 coordSystem.setConstraints(constraints);
 Line2D xAxisLine = utilities.getXAxisLine2D();
 Point2D p = new Point2D.Double(0.0, 0.0);
 for (int i = min; i <= max; i++) {
 p.setLocation(i, 0.0);
 Point2D v = at.transform(p, null);
 Line2D ticks = new Line2D.Double(v.getX(), v.getY() - 3.0, v.getX(), v.getY() + 3.0);
 utilities.drawNumericalXAxisTicks(g);
 assertEquals(1, g.getNumberOfPaths());
 }
 }

 @Test
 public void testDrawXAxisTicks() {
 AffineTransform at = new AffineTransform();
 Graphics2D g = null;
 int min = -10;
 int max = 10;
 ChartDataModelConstraints constraints = new ChartDataModelConstraints(null, null, 1.0, 1.0, min, max, 1.0, 1.0, 1.0, 1.0, new Color(0, 0, 0), new Color(255, 255, 255), 15.0, 15.0, 20.0, 20.0, false);
 coordSystem.setConstraints(constraints);
 Line2D xAxisLine = utilities.getXAxisLine2D();
 Point2D p = new Point2D.Double(0.0, 0.0);
 Point2D v = null;
 Point2D oldv = null;
 for (int i = min - 1; i < max; ++i) {
 p.setLocation(i + 1, 0.0);
 v = at.transform(p, null);
 Line2D ticks = new Line2D.Double(v.getX(), v.getY() - 3.0, v.getX(), v.getY() + 3.0);
 utilities.drawXAxisTicks(g);
 assertEquals(1, g.getNumberOfPaths());
 if (oldv != null) {
 String value = "TestValue";
 g.setPaint(new GradientPaint(oldv.getX(), v.getY(), new Color(0, 0, 0), v.getX(), v.getY(), new Color(255, 255, 255)));
 g.fillRect((int) oldv.getX() - 10, (int) oldv.getY() - 10, 20, 20);
 g.setPaint(Color.BLACK);
 g.drawString(value, (int) (v.getX() - 10), (int) (v.getY() + 12));
 }
 oldv = v;
 }
 }
}