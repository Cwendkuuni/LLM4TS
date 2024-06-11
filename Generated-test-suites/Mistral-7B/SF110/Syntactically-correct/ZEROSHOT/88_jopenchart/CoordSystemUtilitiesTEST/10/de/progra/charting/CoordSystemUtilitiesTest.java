package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.CoordSystem;
import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystemUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 coordSystem = new CoordSystem(500, 500);
 model = new ChartDataModel();
 constraints = new ChartDataModelConstraints();
 constraints2 = new ChartDataModelConstraints();
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test with positive minimum and maximum values
 constraints.setMinimumColumnValue(10.0);
 constraints.setMaximumColumnValue(20.0);
 Double xmin = constraints.getMinimumColumnValue();
 Double xmax = constraints.getMaximumColumnValue();
 DecimalFormat decimalFormat = coordSystem.getYAxisUnitFormat();
 TextLayout textLayout = new TextLayout(decimalFormat.format(xmin), coordSystem.getFont(), coordSystem.getFontRenderContext());
 int maxlmargin = utilities.computeYAxisLabelWidth() + 6;
 int margin = utilities.computeLeftMargin();
 assertEquals(16 + maxlmargin, margin);

 // Test with negative minimum and positive maximum values
 constraints.setMinimumColumnValue(-5.0);
 constraints.setMaximumColumnValue(20.0);
 xmin = constraints.getMinimumColumnValue();
 xmax = constraints.getMaximumColumnValue();
 textLayout = new TextLayout(decimalFormat.format(xmin), coordSystem.getFont(), coordSystem.getFontRenderContext());
 maxlmargin = utilities.computeYAxisLabelWidth() + 6;
 margin = utilities.computeLeftMargin();
 assertEquals(16 + maxlmargin, margin);
 }

 @Test
 public void testComputeRightMargin() {
 // Test with positive minimum and maximum values
 constraints.setMinimumColumnValue(10.0);
 constraints.setMaximumColumnValue(20.0);
 TextLayout textLayout = new TextLayout(coordSystem.getXAxisUnit(), coordSystem.getFont(), coordSystem.getFontRenderContext());
 int width = (int)(textLayout.getBounds().getWidth() + 15.0 / 3.0);
 int margin = utilities.computeRightMargin();
 assertEquals(Math.max(width, 15), margin);

 // Test with negative minimum and positive maximum values
 constraints.setMinimumColumnValue(-10.0);
 constraints.setMaximumColumnValue(20.0);
 textLayout = new TextLayout(coordSystem.getXAxisUnit(), coordSystem.getFont(), coordSystem.getFontRenderContext());
 width = (int)(textLayout.getBounds().getWidth() + 15.0 / 3.0);
 margin = utilities.computeRightMargin();
 assertEquals(Math.max(width, 15), margin);
 }

 @Test
 public void testComputeTopMargin() {
 // Test with positive minimum and maximum values
 TextLayout textLayout = new TextLayout(coordSystem.getYAxisUnit(), coordSystem.getFont(), coordSystem.getFontRenderContext());
 double height = textLayout.getBounds().getHeight() + textLayout.getDescent();
 int margin = utilities.computeTopMargin();
 assertEquals(Math.max((int)(height + 15.0 / 3.0), 15), margin);

 // Test with negative minimum and positive maximum values
 constraints.setMinimumValue(-10.0);
 constraints.setMaximumValue(10.0);
 textLayout = new TextLayout(coordSystem.getXAxisUnit(), coordSystem.getFont(), coordSystem.getFontRenderContext());
 height = textLayout.getBounds().getHeight() + textLayout.getDescent();
 margin = utilities.computeTopMargin();
 assertEquals(Math.max((int)(height + 15.0 / 3.0), 15), margin);
 }

 @Test
 public void testComputeBottomMargin() {
 // Test with positive minimum and maximum values
 DecimalFormat decimalFormat = coordSystem.getXDecimalFormat();
 constraints.setMinimumValue(10.0);
 constraints.setMaximumValue(20.0);
 Double xmin = constraints.getMinimumColumnValue();
 Double xmax = constraints.getMaximumColumnValue();
 textLayout = new TextLayout(coordSystem.getXAxisUnit(), coordSystem.getFont(), coordSystem.getFontRenderContext());
 int maxbmargin = utilities.computeXAxisLabelHeight() + 6;
 int margin = utilities.computeBottomMargin();
 assertEquals(16 + maxbmargin, margin);

 // Test with negative minimum and positive maximum values
 constraints.setMinimumColumnValue(-5.0);
 constraints.setMaximumColumnValue(20.0);
 xmin = constraints.getMinimumColumnValue();
 xmax = constraints.getMaximumColumnValue();
 textLayout = new TextLayout(coordSystem.getXAxisUnit(), coordSystem.getFont(), coordSystem.getFontRenderContext());
 maxbmargin = utilities.computeXAxisLabelHeight() + 6;
 margin = utilities.computeBottomMargin();
 assertEquals(16 + maxbmargin, margin);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 // Test with positive minimum and maximum values
 constraints.setMinimumColumnValue(10.0);
 constraints.setMaximumColumnValue(20.0);
 int height = utilities.computeXAxisLabelHeight();
 assertEquals(11, height);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 // Test with positive minimum and maximum values
 constraints.setMinimumValue(10.0);
 constraints.setMaximumValue(20.0);
 int width = utilities.computeYAxisLabelWidth();
 assertEquals(4, width);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 // Test drawing ticks and labels
 Graphics2D g = mock(Graphics2D.class);
 utilities.drawNumericalXAxisTicks(g);
 verify(g, times(1)).draw(any(Line2D.class));
 verify(g, times(2)).drawString(anyString(), anyDouble(), anyDouble());
 }

 @Test
 public void testDrawXAxisTicks() {
 // Test drawing ticks and labels
 Graphics2D g = mock(Graphics2D.class);
 utilities.drawXAxisTicks(g);
 verify(g, times(21)).draw(any(Line2D.class)); // 20 ticks + 1 axis line
 verify(g, times(10)).drawString(anyString(), anyDouble(), anyDouble()); // 10 labels
 }

 @Test
 public void testDrawYAxisTicks() {
 // Test drawing ticks and labels
 Graphics2D g = mock(Graphics2D.class);
 utilities.drawYAxisTicks(g);
 verify(g, times(1)).draw(any(Line2D.class)); // Axis line
 verify(g, times(11)).drawString(anyString(), anyDouble(), anyDouble()); // 10 labels + 1 tick
 }
}