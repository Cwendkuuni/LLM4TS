package de.progra.charting.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.ChartDataModel;
import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.ChartUtilities;
import de.progra.charting.CoordSystem;
import de.progra.charting.CoordSystemUtilities;
import de.progra.charting.model.DataColumn;
import de.progra.charting.model.DataPoint;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;
 private CoordSystem coordSystem;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @Before
 public void setUp() {
 final DecimalFormat xFormat = new DecimalFormat("#.##");
 final DecimalFormat yFormat = new DecimalFormat("#.##");
 final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);

 this.model = new ChartDataModel();
 this.model.addDataColumn(new DataColumn("Column1", DataPoint.class));
 this.model.addDataColumn(new DataColumn("Column2", DataPoint.class));
 this.model.addDataPointToColumn(new DataPoint(1.0, 2.0), 0);
 this.model.addDataPointToColumn(new DataPoint(3.0, 4.0), 0);
 this.model.addDataPointToColumn(new DataPoint(5.0, 6.0), 1);
 this.model.addDataPointToColumn(new DataPoint(7.0, 8.0), 1);

 this.constraints = new ChartDataModelConstraints();
 this.constraints.setMinimumColumnValue(-1.0);
 this.constraints.setMaximumColumnValue(9.0);
 this.constraints.setMinimumValue(new Double(-5.0));
 this.constraints.setMaximumValue(new Double(5.0));

 this.constraints2 = new ChartDataModelConstraints();
 this.constraints2.setMinimumColumnValue(5.0);
 this.constraints2.setMaximumColumnValue(9.0);
 this.constraints2.setMinimumValue(new Double(5.0));
 this.constraints2.setMaximumValue(new Double(9.0));

 this.coordSystem = new CoordSystem(200, 400, 20, 20, 20, 20, 0.0, null, null, null);
 this.utilities = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.model);
 }

 @Test
 public void testComputeLeftMargin() {
 final int expected = 162;
 final int actual = this.utilities.computeLeftMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRightMargin() {
 final int expected = 45;
 final int actual = this.utilities.computeRightMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeTopMargin() {
 final int expected = 45;
 final int actual = this.utilities.computeTopMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeBottomMargin() {
 final int expected = 81;
 final int actual = this.utilities.computeBottomMargin();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 final int expected = 63;
 final int actual = this.utilities.computeXAxisLabelHeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 final int expected = 87;
 final int actual = this.utilities.computeYAxisLabelWidth();
 assertEquals(expected, actual);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 final Graphics2D graphics = this.createMockGraphics();
 this.utilities.drawNumericalXAxisTicks(graphics);
 // Add assertions or checks here
 }

 @Test
 public void testDrawXAxisTicks() {
 final Graphics2D graphics = this.createMockGraphics();
 this.utilities.drawXAxisTicks(graphics);
 // Add assertions or checks here
 }

 @Test
 public void testDrawYAxisTicks() {
 final Graphics2D graphics = this.createMockGraphics();
 this.utilities.drawYAxisTicks(graphics);
 // Add assertions or checks here
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructorArguments() {
 this.utilities = new CoordSystemUtilities(null, this.constraints, this.constraints2, this.model);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstraints() {
 this.constraints = null;
 this.utilities = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.model);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstraints2() {
 this.constraints2 = null;
 this.utilities = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, this.model);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidModel() {
 this.model = null;
 this.utilities = new CoordSystemUtilities(this.coordSystem, this.constraints, this.constraints2, null);
 }

 private Graphics2D createMockGraphics() {
 return null; // Replace this with the actual implementation of a mock graphical context
 }
}