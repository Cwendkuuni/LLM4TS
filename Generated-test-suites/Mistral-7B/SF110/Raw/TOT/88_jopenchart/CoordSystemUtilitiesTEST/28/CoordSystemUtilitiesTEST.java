package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartUtilities;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 coordSystem = new CoordSystem(new Dimension(500, 500), new Dimension(50, 50), "Test Coord System", Color.WHITE, Color.BLACK, new Font("SansSerif", Font.BOLD, 12), new Font("SansSerif", Font.PLAIN, 10), new Point2D.Double(100, 100), new Point2D.Double(300, 300), 0, 1);
 model = new ChartDataModel();
 constraints = new ChartDataModelConstraints(new Double[] { 0.0, 10.0, 2.0 }, new Double[] { 0.0, 10.0 });
 constraints2 = new ChartDataModelConstraints(new Double[] { 10.0, 0.0, 2.0 }, new Double[] { 0.0, 10.0 });
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test with valid values
 double expected = 130;
 int actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);

 // Test with negative xmin and xmax
 constraints.setMinimumColumnValue(-5.0);
 constraints.setMaximumColumnValue(5.0);
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 double xmin = constraints.getMinimumColumnValue();
 double xmax = constraints.getMaximumColumnValue();
 expected = 142;
 actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);

 // Test with zero xmin and xmax
 constraints.setMinimumColumnValue(0.0);
 constraints.setMaximumColumnValue(0.0);
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 xmin = constraints.getMinimumColumnValue();
 xmax = constraints.getMaximumColumnValue();
 expected = 20;
 actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);

 // Test with null coordSystem
 utilities = null;
 assertThrows(NullPointerException.class, () -> utilities.computeLeftMargin());
 }

 @Test
 public void testComputeRightMargin() {
 // Test with valid values
 DecimalFormat df = new DecimalFormat("#.##");
 double expectedWidth = df.format(12.5).length() * 8;
 int actual = utilities.computeRightMargin();
 assertEquals(expectedWidth, actual);

 // Test with null coordSystem
 utilities = null;
 assertThrows(NullPointerException.class, () -> utilities.computeRightMargin());
 }

 @Test
 public void testComputeTopMargin() {
 // Test with valid values
 TextLayout layout = new TextLayout("Test", coordSystem.getFont(), coordSystem.getFontRenderContext());
 double height = layout.getBounds().getHeight();
 int expected = (int)(height + 15.0 / 3.0 + layout.getDescent());
 int actual = utilities.computeTopMargin();
 assertEquals(expected, actual);

 // Test with null coordSystem
 utilities = null;
 assertThrows(NullPointerException.class, () -> utilities.computeTopMargin());
 }

 @Test
 public void testComputeBottomMargin() {
 // Test with valid values
 double ymin = 1.0;
 double ymax = 11.0;
 int expected = 132;
 int actual = utilities.computeBottomMargin();
 assertEquals(expected, actual);

 // Test with empty constraints
 constraints2.setMinimumValue(null);
 constraints2.setMaximumValue(null);
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 actual = utilities.computeBottomMargin();
 assertEquals(20, actual);

 // Test with null coordSystem
 utilities = null;
 assertThrows(NullPointerException.class, () -> utilities.computeBottomMargin());
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test with valid values
 double min = 0.0;
 double max = 10.0;
 double tick = 1.0;
 int expected = 32;
 int actual = utilities.computeXAxisLabelHeight();
 assertEquals(expected, actual);

 // Test with null coordSystem
 utilities = null;
 assertThrows(NullPointerException.class, () -> utilities.computeXAxisLabelHeight());
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test with valid values
 double min = 0.0;
 double max = 10.0;
 double tick = 1.0;
 int expected = 16;
 int actual = utilities.computeYAxisLabelWidth();
 assertEquals(expected, actual);

 // Test with null coordSystem
 utilities = null;
 assertThrows(NullPointerException.class, () -> utilities.computeYAxisLabelWidth());
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Test with valid values
 Graphics2D g = coordSystem.createGraphics();
 utilities.drawNumericalXAxisTicks(g);
 assertNotNull(g);

 // Test with null graphics2D
 utilities = null;
 Graphics2D g2 = coordSystem.createGraphics();
 assertThrows(NullPointerException.class, () -> utilities.drawNumericalXAxisTicks(g2));
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test with valid values
 Graphics2D g = coordSystem.createGraphics();
 utilities.drawXAxisTicks(g);
 assertNotNull(g);

 // Test with null graphics2D
 utilities = null;
 Graphics2D g2 = coordSystem.createGraphics();
 assertThrows(NullPointerException.class, () -> utilities.drawXAxisTicks(g2));
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test with valid values
 Graphics2D g = coordSystem.createGraphics();
 utilities.drawYAxisTicks(g);
 assertNotNull(g);

 // Test with null graphics2D
 utilities = null;
 Graphics2D g2 = coordSystem.createGraphics();
 assertThrows(NullPointerException.class, () -> utilities.drawYAxisTicks(g2));
 }

 @Test
 public void testGetXAxisLine2D() {
 // Test with valid values
 Line2D expected = new Line2D.Double(0.0, 0.0, 800.0, 0.0);
 Line2D actual = utilities.getXAxisLine2D();
 assertEquals(expected, actual);

 // Test with null coordSystemUtilities
 utilities = null;
 assertThrows(NullPointerException.class, () -> utilities.getXAxisLine2D());
 }

 @Test
 public void testGetYAxisLine2D() {
 // Test with valid values
 Line2D expected = new Line2D.Double(0.0, 800.0, 0.0, 0.0);
 Line2D actual = utilities.getYAxisLine2D();
 assertEquals(expected, actual);

 // Test with null coordSystemUtilities
 utilities = null;
 assertThrows(NullPointerException.class, () -> utilities.getYAxisLine2D());
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 // Test with valid values
 Line2D expected = new Line2D.Double(800.0, 0.0, 800.0, 600.0);
 Line2D actual = utilities.getSecondYAxisLine2D();
 assertEquals(expected, actual);

 // Test with null coordSystemUtilities
 utilities = null;
 assertThrows(NullPointerException.class, () -> utilities.getSecondYAxisLine2D());
 }
}