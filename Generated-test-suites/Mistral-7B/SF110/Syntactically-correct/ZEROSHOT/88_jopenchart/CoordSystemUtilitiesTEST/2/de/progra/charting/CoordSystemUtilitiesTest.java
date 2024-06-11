package de.progra.charting.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.*;
import de.progra.charting. CoordSystem;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utils;
 private CoordSystem coordSystem;
 private ChartDataModel dataModel;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 coordSystem = new CoordSystem(new java.awt.Dimension(600, 400), new java.awt.Insets(10, 10, 10, 10));
 dataModel = new ChartDataModel();
 constraints = new ChartDataModelConstraints(new DataColumn("Column1", Double.class), 0.0, 10.0, 1.0);
 constraints2 = new ChartDataModelConstraints(new DataColumn("Column2", Double.class), 5.0, -5.0, 1.0);
 utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, dataModel);
 }

 @Test
 public void testComputeLeftMargin() {
 double xmin = constraints.getMinimumColumnValue();
 double xmax = constraints.getMaximumColumnValue();
 double ymin = constraints.getMinimumValue().doubleValue();
 double ymax = constraints.getMaximumValue().doubleValue();
 DecimalFormat df = coordSystem.getXDecimalFormat();
 TextLayout layout = new TextLayout(coordSystem.getYAxisUnit(), coordSystem.getFont(), coordSystem.getFontRenderContext());
 int expectedLeftMargin = 20;
 int actualLeftMargin = utils.computeLeftMargin();
 assertEquals(expectedLeftMargin, actualLeftMargin);

 xmin = -5.0;
 xmax = 5.0;
 int maxlmargin = utils.computeYAxisLabelWidth() + 6;
 maxlmargin = Math.max(maxlmargin, (int)layout.getBounds().getWidth() + 6);
 double newXmin = xmin / (xmin + xmax) * (coordSystem.getBounds().getWidth() - coordSystem.getRightMargin());
 int newLeftMargin = (int)Math.ceil(maxlmargin - newXmin);
 int margin = utils.computeLeftMargin();
 assertEquals(newLeftMargin, margin);
 }

 @Test
 public void testComputeRightMargin() {
 TextLayout layout = new TextLayout(coordSystem.getXAxisUnit(), coordSystem.getFont(), coordSystem.getFontRenderContext());
 int expectedRightMargin = 15;
 int actualRightMargin = utils.computeRightMargin();
 assertEquals(expectedRightMargin, actualRightMargin);
 }

 @Test
 public void testComputeTopMargin() {
 TextLayout layout = new TextLayout(coordSystem.getYAxisUnit(), coordSystem.getFont(), coordSystem.getFontRenderContext());
 int expectedTopMargin = 15;
 int actualTopMargin = utils.computeTopMargin();
 assertEquals(expectedTopMargin, actualTopMargin);
 }

 @Test
 public void testComputeBottomMargin() {
 double xmin = constraints.getMinimumColumnValue();
 double xmax = constraints.getMaximumColumnValue();
 double ymin = constraints.getMinimumValue().doubleValue();
 double ymax = constraints.getMaximumValue().doubleValue();
 DecimalFormat df = coordSystem.getXDecimalFormat();
 TextLayout layout = new TextLayout(coordSystem.getXAxisUnit(), coordSystem.getFont(), coordSystem.getFontRenderContext());
 int expectedBottomMargin = 20;
 int actualBottomMargin = utils.computeBottomMargin();
 assertEquals(expectedBottomMargin, actualBottomMargin);

 xmin = -5.0;
 xmax = 5.0;
 int maxbmargin = utils.computeXAxisLabelHeight() + 6;
 maxbmargin = Math.max(maxbmargin, (int)layout.getBounds().getHeight() + 6);
 double newYmin = ymin / (ymin + ymax) * (coordSystem.getBounds().getHeight() - coordSystem.getTopMargin());
 int newBottomMargin = Math.max(maxbmargin, 20);
 int margin = utils.computeBottomMargin();
 assertEquals(newBottomMargin, margin);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 double min = constraints.getMinimumColumnValue();
 double max = constraints.getMaximumColumnValue();
 double tick = ChartUtilities.calculateTickSpacing(min, max);
 double ypt = 0.0;
 boolean paint = false;
 DecimalFormat df = coordSystem.getXDecimalFormat();
 FontRenderContext frc = coordSystem.getFontRenderContext();
 Font f = coordSystem.getFont();
 int expectedXAxisLabelHeight = 15;
 int actualXAxisLabelHeight = utils.computeXAxisLabelHeight();
 assertEquals(expectedXAxisLabelHeight, actualXAxisLabelHeight);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 double min = constraints.getMinimumValue().doubleValue();
 double max = constraints.getMaximumValue().doubleValue();
 double tick = ChartUtilities.calculateTickSpacing(min, max);
 double xpt = 0.0;
 boolean paint = false;
 DecimalFormat df = coordSystem.getYDecimalFormat();
 FontRenderContext frc = coordSystem.getFontRenderContext();
 Font f = coordSystem.getFont();
 int expectedYAxisLabelWidth = 15;
 int actualYAxisLabelWidth = utils.computeYAxisLabelWidth();
 assertEquals(expectedYAxisLabelWidth, actualYAxisLabelWidth);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.isPaintLabels()).thenReturn(true);
 Point2D p = new Point2D.Double(0.0, 0.0);
 Line2D ticks = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
 DecimalFormat df = coordSystem.getXDecimalFormat();
 FontRenderContext frc = coordSystem.getFontRenderContext();
 Font f = coordSystem.getFont();
 utils.drawNumericalXAxisTicks(g);
 verify(g).draw(ticks);
 }

 @Test
 public void testDrawXAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.isPaintLabels()).thenReturn(true);
 int min = (int)constraints.getMinimumColumnValue();
 int max = (int)constraints.getMaximumColumnValue();
 Line2D ticks = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
 DecimalFormat df = coordSystem.getXDecimalFormat();
 FontRenderContext frc = coordSystem.getFontRenderContext();
 Font f = coordSystem.getFont();
 utils.drawXAxisTicks(g);
 verify(g).draw(ticks);
 }

 @Test
 public void testDrawYAxisTicks() {
 Graphics2D g = mock(Graphics2D.class);
 when(g.isPaintLabels()).thenReturn(true);
 double min = constraints.getMinimumValue().doubleValue();
 double max = constraints.getMaximumValue().doubleValue();
 Line2D ticks = new Line2D.Double(0.0, 0.0, 0.0, 0.0);
 DecimalFormat df = coordSystem.getYDecimalFormat();
 FontRenderContext frc = coordSystem.getFontRenderContext();
 Font f = coordSystem.getFont();
 utils.drawYAxisTicks(g);
 verify(g).draw(ticks);
 }
}