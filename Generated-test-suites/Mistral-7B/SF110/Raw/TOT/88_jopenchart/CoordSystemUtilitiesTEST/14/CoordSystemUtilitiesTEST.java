package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.DataColumn;
import de.progra.charting.model.DataSeries;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utils;
 private CoordSystem coord;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setup() {
 coordinates = new CoordSystem(new java.awt.geom.Rectangle2D.Double(10, 10, 500, 500), new java.awt.geom.AffineTransform());
 model = new ChartDataModel();
 constraints = new ChartDataModelConstraints();
 constraints2 = new ChartDataModelConstraints();
 utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin_ZeroXMinAndXMax() {
 double xmin = 0.0;
 double xmax = 0.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 int actual = utils.computeLeftMargin();
 int expected = 20;
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeLeftMargin_PositiveXMinAndXMax() {
 double xmin = 10.0;
 double xmax = 20.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 int actual = utils.computeLeftMargin();
 int expected = 24; // 20 base margin + label width

 TextLayout labelLayout = new TextLayout("Test Unit", coord.getFont(), coord.getFontRenderContext());
 int labelWidth = (int)labelLayout.getBounds().getWidth();
 assertEquals(expected, actual + labelWidth);
 }

 @Test
 public void testComputeLeftMargin_NegativeXMinAndXMax() {
 double xmin = -10.0;
 double xmax = -20.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 int actual = utils.computeLeftMargin();
 int expected = 20;
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRightMargin() {
 DecimalFormat decimalFormat = new DecimalFormat("#.##");
 double width = decimalFormat.format(15.0).length();
 int actual = utils.computeRightMargin();
 int expected = (int)(15.0 + 15.0 / 3.0) + (int)width;
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 double height = 30.0;
 TextLayout labelLayout = new TextLayout("Test Unit", coord.getFont(), coord.getFontRenderContext());
 int actual = utils.computeTopMargin();
 int expected = (int)(height + 15.0 / 3.0 + labelLayout.getDescent());
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin_ZeroYMinAndYMax() {
 double ymin = 0.0;
 double ymax = 0.0;
 constraints.setMinimumValue(ymin);
 constraints.setMaximumValue(ymax);
 int actual = utils.computeBottomMargin();
 int expected = 20;
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin_PositiveYMinAndYMax() {
 double ymin = 10.0;
 double ymax = 20.0;
 constraints.setMinimumValue(ymin);
 constraints.setMaximumValue(ymax);
 int actual = utils.computeBottomMargin();
 int expected = 24; // 20 base margin + label height

 TextLayout labelLayout = new TextLayout("Test Unit", coord.getFont(), coord.getFontRenderContext());
 int labelHeight = (int)labelLayout.getBounds().getHeight();
 assertEquals(expected, actual + labelHeight);
 }

 @Test
 public void testComputeBottomMargin_NegativeYMinAndYMax() {
 double ymin = -10.0;
 double ymax = -20.0;
 constraints.setMinimumValue(ymin);
 constraints.setMaximumValue(ymax);
 int actual = utils.computeBottomMargin();
 int expected = 20;
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 DataColumn xAxisColumn = new DataColumn("Test Column X");
 DataSeries xAxisSeries = new DataSeries("Test Series X", Arrays.asList(10.0, 20.0, 30.0, 40.0, 50.0));
 xAxisSeries.setXColumn(xAxisColumn);
 model.addSeries(xAxisSeries);
 constraints.setMinimumValue(Double.MIN_VALUE);
 constraints.setMaximumValue(Double.MAX_VALUE);
 int actual = utils.computeXAxisLabelHeight();
 int expected = 3 * (int)coord.getFont().getSize2D() + 3; // 3 lines of text with 3 pixels gap between them
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeXAxisLabelWidth() {
 DataColumn xAxisColumn = new DataColumn("Test Column X");
 DataSeries xAxisSeries = new DataSeries("Test Series X", Arrays.asList(10.0, 20.0, 30.0, 40.0, 50.0));
 xAxisSeries.setXColumn(xAxisColumn);
 model.addSeries(xAxisSeries);
 constraints.setMinimumColumnValue(-5.0);
 constraints.setMaximumColumnValue(5.0);
 int actual = utils.computeXAxisLabelWidth();
 int expected = 5 * (int)Math.max(coord.getFont().getStringBounds("50.0", coord.getFontRenderContext()).getBounds().getWidth(), coord.getFont().getStringBounds("10.0", coord.getFontRenderContext()).getBounds().getWidth());
 assertEquals(expected, actual);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 DataColumn xAxisColumn = new DataColumn("Test Column X");
 DataSeries xAxisSeries = new DataSeries("Test Series X", Arrays.asList(10.0, 20.0, 30.0, 40.0, 50.0));
 xAxisSeries.setXColumn(xAxisColumn);
 model.addSeries(xAxisSeries);

 Graphics2D graphics = mock(Graphics2D.class);
 when(graphics.isPaintLabels()).thenReturn(true);
 when(graphics.getFont()).thenReturn(coord.getFont());

 utils.drawNumericalXAxisTicks(graphics);

 verify(graphics, times(5)).drawString(eq("10.0"), anyDouble(), anyDouble());
 verify(graphics, times(5)).drawString(eq("20.0"), anyDouble(), anyDouble());
 verify(graphics, times(5)).drawString(eq("30.0"), anyDouble(), anyDouble());
 verify(graphics, times(5)).drawString(eq("40.0"), anyDouble(), anyDouble());
 verify(graphics, times(5)).drawString(eq("50.0"), anyDouble(), anyDouble());
 }

}