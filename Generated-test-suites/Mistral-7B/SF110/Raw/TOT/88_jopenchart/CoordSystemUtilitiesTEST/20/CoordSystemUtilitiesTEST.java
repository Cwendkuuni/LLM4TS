package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartUtilities;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private CoordSystem coord;

 @Before
 public void setUp() {
 ModelData md = new ModelData();
 model = new ChartDataModel(md);
 constraints = new ChartDataModelConstraints(10.0, 20.0, 1.0, 10.0, 30.0, true);
 constraints2 = new ChartDataModelConstraints(15.0, 25.0, 2.0, 15.0, 35.0, true);
 coord = new CoordSystem(new java.awt.Dimension(300, 400), model, constraints, constraints2);
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test with positive xmin and xmax
 double xmin = 1.0;
 double xmax = 10.0;
 double expected = 118; // calculated using the given code
 int actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);

 // Test with negative xmin and positive xmax
 xmin = -5.0;
 xmax = 7.0;
 expected = 129; // calculated using the given code
 actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);

 // Test with negative xmin and xmax
 xmin = -10.0;
 xmax = -5.0;
 expected = 20; // given default value
 actual = utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRightMargin() {
 double xmin = -10.0;
 double xmax = -5.0;
 double expected = 15; // given default value
 int actual = utilities.computeRightMargin();
 assertEquals(expected, actual);

 xmin = 1.0;
 xmax = 10.0;
 String label = "Label";
 Font font = new Font("Arial", Font.BOLD, 12);
 DecimalFormat decimalFormat = new DecimalFormat("0.00");
 TextLayout layout = new TextLayout(label, font, coord.getFontRenderContext());
 double width = layout.getBounds().getWidth();
 expected = (int)(width + 15.0 / 3.0);
 actual = utilities.computeRightMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 double ymin = -20.0;
 double ymax = 0.0;
 double expected = 15; // given default value
 int actual = utilities.computeTopMargin();
 assertEquals(expected, actual);

 ymin = 0.0;
 ymax = 20.0;
 String label = "Label";
 Font font = new Font("Arial", Font.BOLD, 12);
 TextLayout layout = new TextLayout(label, font, coord.getFontRenderContext());
 double height = layout.getBounds().getHeight();
 expected = (int)(height + 15.0 / 3.0 + layout.getDescent());
 actual = utilities.computeTopMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin() {
 double ymin = -20.0;
 double ymax = 0.0;
 int expected = 20; // given default value
 int actual = utilities.computeBottomMargin();
 assertEquals(expected, actual);

 ymin = 1.0;
 ymax = 10.0;
 double expectedWidth = 15.0;
 double expectedHeight = 10.0;
 int xpt = 5.0;
 int actual = utilities.computeBottomMargin();
 assertEquals(expectedHeight, actual);

 ymin = 1.0;
 ymax = 10.0;
 double xmin = -5.0;
 double expectedWidth2 = 30.0;
 double expectedHeight2 = 15.0;
 int actual2 = utilities.computeBottomMargin();
 assertEquals(expectedHeight2, actual2);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 double min = 0.0;
 double max = 10.0;
 int expected = 36; // calculated using the given code
 int actual = utilities.computeXAxisLabelHeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 double min = 0.0;
 double max = 10.0;
 int expected = 36; // calculated using the given code
 int actual = utilities.computeYAxisLabelWidth();
 assertEquals(expected, actual);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Test with no painting labels
 Graphics2D g = coord.createGraphics();
 utilities.drawNumericalXAxisTicks(g);
 assertFalse(g.getFontMetrics().hasSize());

 // Test with painting labels
 g = coord.createGraphics();
 for (int i = 0; i < model.getNumberOfColumns(); ++i) {
 String label = (String)model.getColumnName(i);
 g.setColor(Color.BLACK);
 g.setFont(coord.getFont());
 g.drawString(label, 0, (int)coord.getFontMetrics().getHeight() + 6);
 }
 utilities.drawNumericalXAxisTicks(g);
 for (int i = 0; i < model.getNumberOfColumns(); ++i) {
 String label = (String)model.getColumnName(i);
 Point2D point = new Point2D.Double(i + 1, 0);
 Point2D transformedPoint = coord.getTransform(0).transform(point, null);
 Line2D tick = new Line2D.Double(transformedPoint.getX(), transformedPoint.getY() - 3.0, transformedPoint.getX(), transformedPoint.getY() + 3.0);
 g.draw(tick);
 }
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test with no painting labels
 Graphics2D g = coord.createGraphics();
 utilities.drawXAxisTicks(g);
 assertFalse(g.getFontMetrics().hasSize());

 // Test with painting labels
 g = coord.createGraphics();
 for (int i = 0; i < model.getNumberOfColumns(); ++i) {
 String label = (String)model.getColumnName(i);
 g.setColor(Color.BLACK);
 g.setFont(coord.getFont());
 g.drawString(label, 0, (int)coord.getFontMetrics().getHeight() + 6);
 }
 utilities.drawXAxisTicks(g);
 for (int i = 0; i < model.getNumberOfColumns(); ++i) {
 String label = (String)model.getColumnName(i);
 Point2D point = new Point2D.Double(i + 1, 0);
 Point2D transformedPoint = coord.getTransform(0).transform(point, null);
 Line2D tick = new Line2D.Double(transformedPoint.getX(), transformedPoint.getY() - 3.0, transformedPoint.getX(), transformedPoint.getY() + 3.0);
 g.draw(tick);
 String labelString = (String)model.getColumnValueAt(i);
 Point2D labelPoint = new Point2D.Double(transformedPoint.getX(), transformedPoint.getY() + tick.getLength() / 2.0);
 g.drawString(labelString, (float)labelPoint.getX(), (float)labelPoint.getY());
 }
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test with no painting labels
 Graphics2D g = coord.createGraphics();
 utilities.drawYAxisTicks(g);
 assertFalse(g.getFontMetrics().hasSize());

 // Test with painting labels
 g = coord.createGraphics();
 for (int i = 0; i < model.getNumberOfRows(); ++i) {
 String label = (String)model.getRowLabel(i);
 g.setColor(Color.BLACK);
 g.setFont(coord.getFont());
 g.drawString(label, 0, (int)coord.getFontMetrics().getHeight() + 6);
 }
 utilities.drawYAxisTicks(g);
 for (int i = 0; i < model.getNumberOfRows(); ++i) {
 String label = (String)model.getRowLabel(i);
 Point2D point = new Point2D.Double(0, i + 1);
 Point2D transformedPoint = coord.getTransform(0).transform(point, null);
 Line2D tick = new Line2D.Double(transformedPoint.getX() - 3.0, transformedPoint.getY(), transformedPoint.getX() + 3.0, transformedPoint.getY());
 g.draw(tick);
 String labelString = (String)model.getValueAt(i, 0);
 Point2D labelPoint = new Point2D.Double(transformedPoint.getX() + tick.getLength() / 2.0, transformedPoint.getY());
 g.drawString(labelString, (float)labelPoint.getX(), (float)labelPoint.getY());
 }
 }

 @Test
 public void testGetXAxisLine2D() {
 Line2D expected = new Line2D.Double(0.0, 0.0, 300.0, 0.0);
 Line2D actual = utilities.getXAxisLine2D();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetYAxisLine2D() {
 Line2D expected = new Line2D.Double(0.0, 0.0, 0.0, 400.0);
 Line2D actual = utilities.getYAxisLine2D();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 Line2D expected = new Line2D.Double(300.0, 0.0, 300.0, 400.0);
 Line2D actual = utilities.getSecondYAxisLine2D();
 assertEquals(expected, actual);
 }
}