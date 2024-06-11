package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartDataModel;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.DataColumn;
import de.progra.charting.model.DataSeries;
import de.progra.charting.model.DataSeriesCollection;
import de.progra.charting.model.XYChartModel;
import de.progra.charting.util.ChartUtilities;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel dataModel;
 private XYChartModel chartModel;

 @Before
 public void setUp() {
 coordinatesAndModelSetup();
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, dataModel);
 }

 // Basic test cases for each public method

 @Test
 public void testComputeLeftMargin() {
 final int result = utilities.computeLeftMargin();
 assertTrue(result > 0);
 }

 @Test
 public void testComputeRightMargin() {
 final int result = utilities.computeRightMargin();
 assertTrue(result > 0);
 }

 @Test
 public void testComputeTopMargin() {
 final int result = utilities.computeTopMargin();
 assertTrue(result > 0);
 }

 @Test
 public void testComputeBottomMargin() {
 final int result = utilities.computeBottomMargin();
 assertTrue(result > 0);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 final int result = utilities.computeXAxisLabelHeight();
 assertTrue(result > 0);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 final int result = utilities.computeYAxisLabelWidth();
 assertTrue(result > 0);
 }

 // Edge cases and exception handling test cases

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithInvalidCoordSystem() {
 utilities = new CoordSystemUtilities(null, constraints, constraints2, dataModel);
 utilities.computeLeftMargin();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullConstraints() {
 utilities = new CoordSystemUtilities(coordSystem, null, constraints2, dataModel);
 utilities.computeLeftMargin();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullConstraints2() {
 utilities = new CoordSystemUtilities(coordSystem, constraints, null, dataModel);
 utilities.computeLeftMargin();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullDataModel() {
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, null);
 utilities.computeLeftMargin();
 }

 @Test
 public void testComputeLeftMarginWithInvalidMinValue() {
 constraints.setMinimumColumnValue(-1);
 final int result = utilities.computeLeftMargin();
 assertTrue(result == 20);
 }

 @Test
 public void testComputeLeftMarginWithInvalidMaxValue() {
 constraints.setMaximumColumnValue(-1);
 final int result = utilities.computeLeftMargin();
 assertTrue(result == 20);
 }

 @Test
 public void testComputeLeftMarginWithZeroMinAndMaxValues() {
 constraints.setMinimumColumnValue(0);
 constraints.setMaximumColumnValue(0);
 final int result = utilities.computeLeftMargin();
 assertTrue(result == 20);
 }

 @Test
 public void testComputeLeftMarginWithMinValueGreaterThanMaxValue() {
 constraints.setMinimumColumnValue(1);
 constraints.setMaximumColumnValue(0);
 final int result = utilities.computeLeftMargin();
 assertTrue(result == 20);
 }

 @Test
 public void testComputeRightMarginWithEmptyFont() {
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, dataModel) {
 @Override
 public Font getFont() {
 return null;
 }
 };
 final int result = utilities.computeRightMargin();
 assertTrue(result == 15);
 }

 @Test
 public void testComputeRightMarginWithNullFontRenderContext() {
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, dataModel) {
 @Override
 public FontRenderContext getFontRenderContext() {
 return null;
 }
 };
 final int result = utilities.computeRightMargin();
 assertTrue(result == 15);
 }

 @Test
 public void testComputeRightMarginWithNullYAxisUnit() {
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, dataModel) {
 @Override
 public String getYAxisUnit() {
 return null;
 }
 };
 final int result = utilities.computeRightMargin();
 assertTrue(result == 15);
 }

 @Test
 public void testDrawNumericalXAxisTicksWithNullGraphics2D() {
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, dataModel);
 utilities.drawNumericalXAxisTicks(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDrawNumericalXAxisTicksWithNullYAxisUnit() {
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, dataModel) {
 @Override
 public String getYAxisUnit() {
 return null;
 }
 };
 utilities.drawNumericalXAxisTicks(new Graphics2D() {
 @Override
 public void draw(Graphics2D graphics) {
 // empty implementation
 }
 });
 }

 // Test for getXAxisLine2D() method

 @Test
 public void testGetXAxisLine2D() {
 final Line2D result = utilities.getXAxisLine2D();
 assertNotNull(result);
 }

 // Test for getYAxisLine2D() method

 @Test
 public void testGetYAxisLine2D() {
 final Line2D result = utilities.getYAxisLine2D();
 assertNotNull(result);
 }

 // Test for getSecondYAxisLine2D() method

 @Test
 public void testGetSecondYAxisLine2D() {
 final Line2D result = utilities.getSecondYAxisLine2D();
 assertNotNull(result);
 }

 // Test for drawYAxisTicks() method

 @Test
 public void testDrawYAxisTicks() {
 final DataSeriesCollection dataSeriesCollection = createDataSeriesCollection();
 final XYChartModel chartModel = new XYChartModel(dataSeriesCollection);
 final ChartDataModel chartDataModel = chartModel.createDefaultChartDataModel();
 final CoordSystem coordSystem = new CoordSystem(500, 400, chartDataModel);
 final int marginOffset = 6;
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, chartDataModel);

 final Graphics2D g = createMockGraphics2D();
 utilities.drawYAxisTicks(g);

 verify(g, times(dataSeriesCollection.size())).draw(anyLine());
 }

 // Test for drawXAxisTicks() method

 @Test
 public void testDrawXAxisTicks() {
 final DataSeriesCollection dataSeriesCollection = createDataSeriesCollection();
 final XYChartModel chartModel = new XYChartModel(dataSeriesCollection);
 final ChartDataModel chartDataModel = chartModel.createDefaultChartDataModel();
 final CoordSystem coordSystem = new CoordSystem(500, 400, chartDataModel);
 final int marginOffset = 6;
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, chartDataModel);

 final Graphics2D g = createMockGraphics2D();
 utilities.drawXAxisTicks(g);

 verify(g, times(dataSeriesCollection.size())).draw(anyLine());
 }

 // Test for drawNumericalXAxisTicks() method

 @Test
 public void testDrawNumericalXAxisTicks() {
 final DataSeriesCollection dataSeriesCollection = createDataSeriesCollection();
 final XYChartModel chartModel = new XYChartModel(dataSeriesCollection);
 final ChartDataModel chartDataModel = chartModel.createDefaultChartDataModel();
 final CoordSystem coordSystem = new CoordSystem(500, 400, chartDataModel);
 final int marginOffset = 6;
 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, chartDataModel);

 final Graphics2D g = createMockGraphics2D();
 utilities.drawNumericalXAxisTicks(g);

 verify(g, times(dataSeriesCollection.size())).draw(anyLine());
 }

 private void coordinatesAndModelSetup() {
 final double minX = -10.0;
 final double maxX = 10.0;
 final double minY = 0.0;
 final double maxY = 10.0;

 final DataColumn xAxisLabelColumn = new DataColumn("X-axis", Double.class, null, null, null, null, null);
 final DataColumn yAxisLabelColumn = new DataColumn("Y-axis", Double.class, null, null, null, null, null);

 final DataSeries xAxisLabelSeries = new DataSeries("X-axis label", Double.class, Arrays.asList(minX, maxX), null, null);
 final DataSeries yAxisLabelSeries = new DataSeries("Y-axis label", Double.class, Arrays.asList(minY, maxY), null, null);

 final DataSeriesCollection dataSeriesCollection = new DataSeriesCollection(Arrays.asList(xAxisLabelSeries, yAxisLabelSeries));
 chartModel = new XYChartModel(dataSeriesCollection);

 constraints = new ChartDataModelConstraints();
 constraints.setMinimumColumnValue(minX);
 constraints.setMaximumColumnValue(maxX);
 constraints2 = new ChartDataModelConstraints();
 constraints2.setMaximumColumnValue(minY);
 constraints2.setMinimumColumnValue(maxY);

 dataModel = chartDataModel;
 coordSystem = new CoordSystem(500, 400, chartDataModel);
 }

 private Graphics2D createMockGraphics2D() {
 return mock(Graphics2D.class);
 }
}