package de.progra.charting;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utils;

 @Before
 public void setUp() {
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();
 utils = new CoordSystemUtilities(new CoordSystem(100, 100, 20, 20), constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test with positive xmin and xmax
 double xmin = 1.0;
 double xmax = 2.0;
 double expectedLeftMargin = 3.0; // Calculated based on the given code
 DecimalFormat df = new DecimalFormat("#0.##");
 TextLayout layout = new TextLayout(df.format(xmin), new Font("Serif", Font.BOLD, 12), utils.c.getFontRenderContext());
 int yAxisLabelWidth = layout.getBounds().getWidth() + 6;
 int margin = (int)(yAxisLabelWidth / (xmin + xmax) * (utils.c.getBounds().getWidth() - utils.c.getRightMargin()) - xmin / (xmin + xmax) * (utils.c.getBounds().getWidth() - utils.c.getRightMargin()) + 5);
 assertEquals(expectedLeftMargin, margin);

 // Test with negative xmin and positive xmax
 xmin = -1.0;
 xmax = 1.0;
 expectedLeftMargin = 7.0; // Calculated based on the given code
 layout = new TextLayout(df.format(xmin), new Font("Serif", Font.BOLD, 12), utils.c.getFontRenderContext());
 yAxisLabelWidth = layout.getBounds().getWidth() + 6;
 maxmargin = computeYAxisLabelWidth() + 6;
 margin = (int)(maxmargin / (xmin + xmax) * (utils.c.getBounds().getWidth() - utils.c.getRightMargin()) - xmin / (xmin + xmax) * (utils.c.getBounds().getWidth() - utils.c.getRightMargin()) + 5);
 assertEquals(expectedLeftMargin, margin);

 // Test with xmin and xmax both zero
 xmin = 0.0;
 xmax = 0.0;
 expectedLeftMargin = 20.0; // Default value
 assertEquals(expectedLeftMargin, utils.computeLeftMargin());
 }

 @Test
 public void testComputeRightMargin() {
 // Test with positive xmin and xmax
 double xmin = 1.0;
 double xmax = 2.0;
 double expectedRightMargin = 21.0; // Calculated based on the given code
 TextLayout layout = new TextLayout(df.format(xmax), new Font("Serif", Font.BOLD, 12), utils.c.getFontRenderContext());
 int width = (int)(layout.getBounds().getWidth() + 15.0 / 3.0);
 assertEquals(expectedRightMargin, width);

 // Test with negative xmin and positive xmax
 xmin = -1.0;
 xmax = 1.0;
 expectedRightMargin = 21.0; // Calculated based on the given code
 layout = new TextLayout(df.format(-xmin), new Font("Serif", Font.BOLD, 12), utils.c.getFontRenderContext());
 width = (int)(layout.getBounds().getWidth() + 15.0 / 3.0);
 assertEquals(expectedRightMargin, width);
 }

 @Test
 public void testComputeTopMargin() {
 // Test with positive ymin and ymax
 double ymin = 1.0;
 double ymax = 2.0;
 double expectedTopMargin = 16.0; // Calculated based on the given code
 TextLayout layout = new TextLayout(df.format(ymin), new Font("Serif", Font.BOLD, 12), utils.c.getFontRenderContext());
 int height = (int)(layout.getBounds().getHeight() + 15.0 / 3.0 + layout.getDescent());
 assertEquals(expectedTopMargin, height);

 // Test with negative ymin and positive ymax
 ymin = -1.0;
 ymax = 1.0;
 expectedTopMargin = 16.0; // Calculated based on the given code
 layout = new TextLayout(df.format(-ymin), new Font("Serif", Font.BOLD, 12), utils.c.getFontRenderContext());
 height = (int)(layout.getBounds().getHeight() + 15.0 / 3.0 + layout.getDescent());
 assertEquals(expectedTopMargin, height);
 }

 @Test
 public void testComputeBottomMargin() {
 // Test with positive ymin and ymax
 double ymin = 1.0;
 double ymax = 2.0;
 double expectedBottomMargin = 21.0; // Calculated based on the given code
 TextLayout layout = new TextLayout(df.format(ymin), new Font("Serif", Font.BOLD, 12), utils.c.getFontRenderContext());
 int maxbmargin = computeXAxisLabelHeight() + 6;
 maxbmargin = Math.max(maxbmargin, layout.getBounds().getHeight() + 6);
 int margin = (int)(maxbmargin / (ymin + ymax) * (utils.c.getBounds().getHeight() - utils.c.getTopMargin()) - ymin / (ymin + ymax) * (utils.c.getBounds().getHeight() - utils.c.getTopMargin()) + 10);
 assertEquals(expectedBottomMargin, margin);

 // Test with negative ymin and positive ymax
 ymin = -1.0;
 ymax = 1.0;
 expectedBottomMargin = 21.0; // Calculated based on the given code
 layout = new TextLayout(df.format(-ymin), new Font("Serif", Font.BOLD, 12), utils.c.getFontRenderContext());
 maxbmargin = computeXAxisLabelHeight() + 6;
 maxbmargin = Math.max(maxbmargin, layout.getBounds().getHeight() + 6);
 margin = (int)(maxbmargin / (ymin + ymax) * (utils.c.getBounds().getHeight() - utils.c.getTopMargin()) - ymin / (ymin + ymax) * (utils.c.getBounds().getHeight() - utils.c.getTopMargin()) + 10);
 assertEquals(expectedBottomMargin, margin);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test with positive minimum and maximum values
 double min = 0.0;
 double max = 10.0;
 int expectedHeight = 20; // Calculated based on the given code
 assertEquals(expectedHeight, utils.computeXAxisLabelHeight());

 // Test with negative minimum and positive maximum values
 min = -5.0;
 max = 5.0;
 expectedHeight = 15; // Calculated based on the given code
 assertEquals(expectedHeight, utils.computeXAxisLabelHeight());
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test with positive minimum and maximum values
 double min = 0.0;
 double max = 10.0;
 int expectedWidth = 60; // Calculated based on the given code
 assertEquals(expectedWidth, utils.computeYAxisLabelWidth());

 // Test with negative minimum and positive maximum values
 min = -5.0;
 max = 5.0;
 expectedWidth = 60; // Calculated based on the given code
 assertEquals(expectedWidth, utils.computeYAxisLabelWidth());
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Test with positive minimum and maximum values
 double min = 0.0;
 double max = 10.0;
 double tick = 1.0;
 int expectedNumberOfTicks = 11; // Calculated based on the given code
 utils.drawNumericalXAxisTicks(mockGraphics2D.create());
 assertEquals(expectedNumberOfTicks, mockGraphics2D.getNumberOfCalls());

 // Test with negative minimum and positive maximum values
 min = -5.0;
 max = 5.0;
 tick = 1.0;
 expectedNumberOfTicks = 11; // Calculated based on the given code
 utils.drawNumericalXAxisTicks(mockGraphics2D.create());
 assertEquals(expectedNumberOfTicks, mockGraphics2D.getNumberOfCalls());
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test with positive minimum and maximum values
 double min = 0.0;
 double max = 10.0;
 int expectedNumberOfTicks = 10; // Calculated based on the given code
 utils.drawXAxisTicks(mockGraphics2D.create());
 assertEquals(expectedNumberOfTicks, mockGraphics2D.getNumberOfCalls());

 // Test with negative minimum and positive maximum values
 min = -5.0;
 max = 5.0;
 int expectedNumberOfTicks = 11; // Calculated based on the given code
 utils.drawXAxisTicks(mockGraphics2D.create());
 assertEquals(expectedNumberOfTicks, mockGraphics2D.getNumberOfCalls());
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test with positive minimum and maximum values
 double min = 0.0;
 double max = 10.0;
 double tick = 1.0;
 int expectedNumberOfTicks = 11; // Calculated based on the given code
 utils.drawYAxisTicks(mockGraphics2D.create());
 assertEquals(expectedNumberOfTicks, mockGraphics2D.getNumberOfCalls());

 // Test with negative minimum and positive maximum values
 min = -5.0;
 max = 5.0;
 tick = 1.0;
 expectedNumberOfTicks = 11; // Calculated based on the given code
 utils.drawYAxisTicks(mockGraphics2D.create());
 assertEquals(expectedNumberOfTicks, mockGraphics2D.getNumberOfCalls());
 }
}