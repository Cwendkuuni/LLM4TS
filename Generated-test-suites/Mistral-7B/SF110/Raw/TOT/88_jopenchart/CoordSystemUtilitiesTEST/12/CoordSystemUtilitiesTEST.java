package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setup() {
 coordSystem = new CoordSystem(new java.awt.Dimension(600, 600), new java.awt.geom.Path2D.Double());
 constraints = new ChartDataModelConstraints(new double[]{-10.0, 10.0}, new double[]{0.0, 10.0});
 constraints2 = new ChartDataModelConstraints(new double[]{10.0, -10.0}, new double[]{0.0, 0.0});
 model = new ChartDataModel();
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test with positive xmin and xmax
 double xmin = 1.0;
 double xmax = 5.0;
 assertEquals(18, utilities.computeLeftMargin());

 // Test with negative xmin and positive xmax
 xmin = -2.0;
 xmax = 3.0;
 assertEquals(18, utilities.computeLeftMargin());

 // Test with negative xmin and negative xmax
 xmin = -4.0;
 xmax = -1.0;
 assertEquals(18, utilities.computeLeftMargin());

 // Test with zero xmin and positive xmax
 xmin = 0.0;
 xmax = 5.0;
 assertEquals(18, utilities.computeLeftMargin());

 // Test with zero xmin and zero xmax
 xmin = 0.0;
 xmax = 0.0;
 assertEquals(15, utilities.computeLeftMargin());
 }

 @Test
 public void testComputeRightMargin() {
 // Test with positive xmin and xmax
 double xmin = 1.0;
 double xmax = 5.0;
 assertEquals(21, utilities.computeRightMargin());

 // Test with negative xmin and positive xmax
 xmin = -2.0;
 xmax = 3.0;
 assertEquals(21, utilities.computeRightMargin());

 // Test with negative xmin and negative xmax
 xmin = -4.0;
 xmax = -1.0;
 assertEquals(15, utilities.computeRightMargin());
 }

 @Test
 public void testComputeTopMargin() {
 // Test with positive ymin and ymax
 double ymin = 1.0;
 double ymax = 5.0;
 assertEquals(18, utilities.computeTopMargin());

 // Test with negative ymin and positive ymax
 ymin = -2.0;
 ymax = 3.0;
 assertEquals(18, utilities.computeTopMargin());

 // Test with negative ymin and negative ymax
 ymin = -4.0;
 ymax = -1.0;
 assertEquals(18, utilities.computeTopMargin());

 // Test with zero ymin and positive ymax
 ymin = 0.0;
 ymax = 5.0;
 assertEquals(18, utilities.computeTopMargin());

 // Test with zero ymin and zero ymax
 ymin = 0.0;
 ymax = 0.0;
 assertEquals(15, utilities.computeTopMargin());
 }

 @Test
 public void testComputeBottomMargin() {
 // Test with positive ymin and ymax
 double ymin = 1.0;
 double ymax = 5.0;
 assertEquals(18, utilities.computeBottomMargin());

 // Test with negative ymin and positive ymax
 ymin = -2.0;
 ymax = 3.0;
 assertEquals(18, utilities.computeBottomMargin());

 // Test with negative ymin and negative ymax
 ymin = -4.0;
 ymax = -1.0;
 assertEquals(18, utilities.computeBottomMargin());

 // Test with zero ymin and positive ymax
 ymin = 0.0;
 ymax = 5.0;
 assertEquals(18, utilities.computeBottomMargin());

 // Test with zero ymin and zero ymax
 ymin = 0.0;
 ymax = 0.0;
 assertEquals(15, utilities.computeBottomMargin());
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test with positive minimum and maximum
 double min = 1.0;
 double max = 5.0;
 assertEquals(10, utilities.computeXAxisLabelHeight());

 // Test with negative minimum and positive maximum
 min = -2.0;
 max = 3.0;
 assertEquals(10, utilities.computeXAxisLabelHeight());

 // Test with negative minimum and negative maximum
 min = -4.0;
 max = -1.0;
 assertEquals(10, utilities.computeXAxisLabelHeight());

 // Test with zero minimum and positive maximum
 min = 0.0;
 max = 5.0;
 assertEquals(10, utilities.computeXAxisLabelHeight());

 // Test with zero minimum and zero maximum
 min = 0.0;
 max = 0.0;
 assertEquals(0, utilities.computeXAxisLabelHeight());
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test with positive minimum and maximum
 double min = 1.0;
 double max = 5.0;
 NumberFormat df = new DecimalFormat("#.##");
 assertEquals(5, utilities.computeYAxisLabelWidth());

 // Test with negative minimum and positive maximum
 min = -2.0;
 max = 3.0;
 df = new DecimalFormat("#.##");
 assertEquals(5, utilities.computeYAxisLabelWidth());

 // Test with negative minimum and negative maximum
 min = -4.0;
 max = -1.0;
 df = new DecimalFormat("#.##");
 assertEquals(5, utilities.computeYAxisLabelWidth());

 // Test with zero minimum and positive maximum
 min = 0.0;
 max = 5.0;
 df = new DecimalFormat("#.##");
 assertEquals(5, utilities.computeYAxisLabelWidth());

 // Test with zero minimum and zero maximum
 min = 0.0;
 max = 0.0;
 df = new DecimalFormat("#.##");
 assertEquals(0, utilities.computeYAxisLabelWidth());
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Test with positive minimum and maximum
 double min = 1.0;
 double max = 5.0;
 List<Double> expectedTickValues = Arrays.asList(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 utilities.drawNumericalXAxisTicks(mockGraphics2DWithPredefinedRenderingHints());
 verifyTickValues(expectedTickValues, mockGraphics2DWithPredefinedRenderingHints());

 // Test with negative minimum and positive maximum
 min = -2.0;
 max = 3.0;
 expectedTickValues = Arrays.asList(new Double[]{-1.0, 0.0, 1.0, 2.0, 3.0});
 utilities.drawNumericalXAxisTicks(mockGraphics2DWithPredefinedRenderingHints());
 verifyTickValues(expectedTickValues, mockGraphics2DWithPredefinedRenderingHints());

 // Test with negative minimum and negative maximum
 min = -4.0;
 max = -1.0;
 expectedTickValues = Arrays.asList(new Double[]{ -1.0, -2.0, -3.0, -4.0 });
 utilities.drawNumericalXAxisTicks(mockGraphics2DWithPredefinedRenderingHints());
 verifyTickValues(expectedTickValues, mockGraphics2DWithPredefinedRenderingHints());
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test with positive minimum and maximum
 double min = 1.0;
 double max = 5.0;
 List<Double> expectedTickValues = Arrays.asList(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 utilities.drawXAxisTicks(mockGraphics2DWithPredefinedRenderingHints());
 verifyTickValues(expectedTickValues, mockGraphics2DWithPredefinedRenderingHints());

 // Test with negative minimum and positive maximum
 min = -2.0;
 max = 3.0;
 expectedTickValues = Arrays.asList(new Double[]{0.0, 1.0, 2.0, 3.0, 4.0});
 utilities.drawXAxisTicks(mockGraphics2DWithPredefinedRenderingHints());
 verifyTickValues(expectedTickValues, mockGraphics2DWithPredefinedRenderingHints());

 // Test with negative minimum and negative maximum
 min = -4.0;
 max = -1.0;
 expectedTickValues = Arrays.asList(new Double[]{-3.0, -2.0, -1.0});
 utilities.drawXAxisTicks(mockGraphics2DWithPredefinedRenderingHints());
 verifyTickValues(expectedTickValues, mockGraphics2DWithPredefinedRenderingHints());
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test with positive minimum and maximum
 double min = 1.0;
 double max = 5.0;
 List<Double> expectedTickValues = Arrays.asList(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 utilities.drawYAxisTicks(mockGraphics2DWithPredefinedRenderingHints());
 verifyTickValues(expectedTickValues, mockGraphics2DWithPredefinedRenderingHints());

 // Test with negative minimum and positive maximum
 min = -2.0;
 max = 3.0;
 expectedTickValues = Arrays.asList(new Double[]{2.0, 1.0, 0.0, -1.0, -2.0});
 utilities.drawYAxisTicks(mockGraphics2DWithPredefinedRenderingHints());
 verifyTickValues(expectedTickValues, mockGraphics2DWithPredefinedRenderingHints());

 // Test with negative minimum and negative maximum
 min = -4.0;
 max = -1.0;
 expectedTickValues = Arrays.asList(new Double[]{-3.0, -2.0, -1.0});
 utilities.drawYAxisTicks(mockGraphics2DWithPredefinedRenderingHints());
 verifyTickValues(expectedTickValues, mockGraphics2DWithPredefinedRenderingHints());
 }

 private Graphics2D mockGraphics2DWithPredefinedRenderingHints() {
 GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
 Graphics2D g = gc.createGraphics();
 g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
 g.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, RenderingHints.VALUE_TEXT_LCD_HIGH_CONTRAST_ON);
 g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
 g.setRenderingHint(RenderingHints.KEY_LINE_ANTIALIAS, RenderingHints.VALUE_LINE_ANTIALIAS_ON);
 return g;
 }

 private void verifyTickValues(List<Double> expectedTickValues, Graphics2D graphics2D) {
 for (int i = 0; i < expectedTickValues.size(); ++i) {
 assertEquals("Incorrect tick value at index " + i, expectedTickValues.get(i), getLastDrawnTickValue(graphics2D));
 }
 }

 private double getLastDrawnTickValue(Graphics2D graphics2D) {
 double lastDrawnTickValue = Double.NaN;
 if (graphics2D != null) {
 Object lastObjectDrawn = graphics2D.getLastDrawnObject();
 if (lastObjectDrawn instanceof Point2D) {
 lastDrawnTickValue = ((Point2D)lastObjectDrawn).getX();
 }
 }
 return lastDrawnTickValue;
 }
}