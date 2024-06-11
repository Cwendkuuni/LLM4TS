package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.DataColumn;
import de.progra.charting.model.DataPoint;
import de.progra.charting.model.XYChartData;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel dataModel;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setup() {
 // Initialize coordSystem, constraints, constraints2, and dataModel for testing
 // You may initialize them based on your specific test data and requirements
 // For example, you can create a sample CoordSystem, ChartDataModelConstraints, and ChartDataModel objects here

 coordSystem = new CoordSystem(new java.awt.Dimension(500, 500), new java.awt.geom.Path2D.Double(), new java.awt.Font("Arial", java.awt.Font.BOLD, 12));

 dataModel = new XYChartData("Test Data", Arrays.asList(new DataColumn("Column 1", Arrays.asList(new DataPoint(1.0, 2.0), new DataPoint(2.0, 3.0), new DataPoint(3.0, 4.0)))));

 constraints = new ChartDataModelConstraints(new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), new java.awt.Font("Arial", java.awt.Font.BOLD, 12), dataModel.getColumnCount(), dataModel.getRowCount());
 constraints2 = new ChartDataModelConstraints(new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), new java.awt.Font("Arial", java.awt.Font.BOLD, 12), dataModel.getColumnCount(), dataModel.getRowCount());

 utilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, dataModel);
 }

 @Test
 public void testComputeLeftMargin() {
 // Test case for computeLeftMargin() method with positive minimum value and positive maximum value
 double xmin = 0.0;
 double xmax = 10.0;
 Format decimalFormat = new DecimalFormat("#.##");
 DecimalFormat xAxisDecimalFormat = new DecimalFormat("#.##");
 DecimalFormat yAxisDecimalFormat = new DecimalFormat("#.##");

 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);

 int expectedLeftMargin = 52; // Calculate expected left margin based on method implementation and test data
 int actualLeftMargin = utilities.computeLeftMargin();

 assertEquals("Test for positive minimum value and positive maximum value", expectedLeftMargin, actualLeftMargin);

 // Test case for computeLeftMargin() method with negative minimum value and positive maximum value
 xmin = -5.0;
 xmax = 10.0;

 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);

 int expectedNegativeMinLeftMargin = 67; // Calculate expected left margin based on method implementation and test data
 int actualNegativeMinLeftMargin = utilities.computeLeftMargin();

 assertEquals("Test for negative minimum value and positive maximum value", expectedNegativeMinLeftMargin, actualNegativeMinLeftMargin);

 // Test case for computeLeftMargin() method with negative minimum value and negative maximum value
 xmin = -5.0;
 xmax = -10.0;

 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);

 int expectedNegativeMinNegativeMaxLeftMargin = 60; // Calculate expected left margin based on method implementation and test data
 int actualNegativeMinNegativeMaxLeftMargin = utilities.computeLeftMargin();

 assertEquals("Test for negative minimum value and negative maximum value", expectedNegativeMinNegativeMaxLeftMargin, actualNegativeMinNegativeMaxLeftMargin);
 }

 @Test
 public void testComputeRightMargin() {
 // Test case for computeRightMargin() method with positive minimum value and positive maximum value
 double xmin = 0.0;
 double xmax = 10.0;

 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);

 int expectedRightMargin = 30; // Calculate expected right margin based on method implementation and test data
 int actualRightMargin = utilities.computeRightMargin();

 assertEquals("Test for positive minimum value and positive maximum value", expectedRightMargin, actualRightMargin);
}

 // You can add more test cases here for other methods in the CoordSystemUtilities class, such as computeTopMargin(), computeBottomMargin(), computeXAxisLabelHeight(), computeYAxisLabelWidth(), drawNumericalXAxisTicks(), drawXAxisTicks(), drawYAxisTicks(), getXAxisLine2D(), getYAxisLine2D(), and getSecondYAxisLine2D()

}