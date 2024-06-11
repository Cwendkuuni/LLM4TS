import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.model.ChartDataModelConstraints;
import de.progra.charting.model.CoordSystem;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utils;
 private CoordSystem coordSystem;
 private ChartDataModel model;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 coordSystem = new CoordSystem(new java.awt.geom.Path2D.Double(), new java.awt.geom.Path2D.Double(), new java.awt.Color(255, 255, 255), 50, 50, 50, 50);
 model = new ChartDataModel();
 constraints = new ChartDataModelConstraints();
 constraints2 = new ChartDataModelConstraints();
 utils = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 double xmin = -10.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new Double(ymin));
 constraints.setMaximumValue(new Double(ymax));
 model.setData(new double[][]{
 new double[]{1.0, 2.0, 3.0, 4.0, 5.0},
 new double[]{6.0, 7.0, 8.0, 9.0, 10.0}
 });

 assertEquals(23, utils.computeLeftMargin());
 }

 @Test
 public void testComputeRightMargin() {
 double xmin = -10.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new Double(ymin));
 constraints.setMaximumValue(new Double(ymax));
 model.setData(new double[][]{
 new double[]{1.0, 2.0, 3.0, 4.0, 5.0},
 new double[]{6.0, 7.0, 8.0, 9.0, 10.0}
 });

 Font font = new Font("Serif", Font.BOLD, 12);
 TextLayout labelLayout = new TextLayout("Test Label", font, coordSystem.getFontRenderContext());

 assertEquals(118 + labelLayout.getBounds().getWidth(), utils.computeRightMargin());
 }

 @Test
 public void testComputeTopMargin() {
 double xmin = -10.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new Double(ymin));
 constraints.setMaximumValue(new Double(ymax));
 model.setData(new double[][]{
 new double[]{1.0, 2.0, 3.0, 4.0, 5.0},
 new double[]{6.0, 7.0, 8.0, 9.0, 10.0}
 });

 TextLayout yAxisLabelLayout = new TextLayout("Test Y-Axis Label", font, coordSystem.getFontRenderContext());

 assertEquals(43 + yAxisLabelLayout.getBounds().getHeight(), utils.computeTopMargin());
 }

 @Test
 public void testComputeBottomMargin() {
 double xmin = -10.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new Double(ymin));
 constraints.setMaximumValue(new Double(ymax));
 model.setData(new double[][]{
 new double[]{1.0, 2.0, 3.0, 4.0, 5.0},
 new double[]{6.0, 7.0, 8.0, 9.0, 10.0}
 });

 TextLayout xAxisLabelLayout = new TextLayout("Test X-Axis Label", font, coordSystem.getFontRenderContext());

 assertEquals(43 + xAxisLabelLayout.getBounds().getHeight(), utils.computeBottomMargin());
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new Double(ymin));
 constraints.setMaximumValue(new Double(ymax));
 model.setData(new double[][]{
 new double[]{1.0, 2.0, 3.0, 4.0, 5.0},
 new double[]{6.0, 7.0, 8.0, 9.0, 10.0}
 });

 assertEquals(74, utils.computeXAxisLabelHeight());
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new Double(ymin));
 constraints.setMaximumValue(new Double(ymax));
 model.setData(new double[][]{
 new double[]{1.0, 2.0, 3.0, 4.0, 5.0},
 new double[]{6.0, 7.0, 8.0, 9.0, 10.0}
 });

 TextLayout yAxisLabelLayout = new TextLayout("Test Y-Axis Label", font, coordSystem.getFontRenderContext());

 assertEquals(65, utils.computeYAxisLabelWidth());
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new Double(ymin));
 constraints.setMaximumValue(new Double(ymax));
 model.setData(new double[][]{
 new double[]{1.0, 2.0, 3.0, 4.0, 5.0},
 new double[]{6.0, 7.0, 8.0, 9.0, 10.0}
 });

 Graphics2D g = mock(Graphics2D.class);

 utils.drawNumericalXAxisTicks(g);

 verify(g).draw(argThat(line -> line.getLineEndX() - line.getLineStartX() > 0));
 }

 @Test
 public void testDrawXAxisTicks() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new Double(ymin));
 constraints.setMaximumValue(new Double(ymax));
 model.setData(new double[][]{
 new double[]{1.0, 2.0, 3.0, 4.0, 5.0},
 new double[]{6.0, 7.0, 8.0, 9.0, 10.0}
 });

 Graphics2D g = mock(Graphics2D.class);

 utils.drawXAxisTicks(g);

 verify(g).draw(argThat(line -> line.getLineEndX() - line.getLineStartX() > 0));
 }

 @Test
 public void testDrawYAxisTicks() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new Double(ymin));
 constraints.setMaximumValue(new Double(ymax));
 model.setData(new double[][]{
 new double[]{1.0, 2.0, 3.0, 4.0, 5.0},
 new double[]{6.0, 7.0, 8.0, 9.0, 10.0}
 });

 Graphics2D g = mock(Graphics2D.class);

 utils.drawYAxisTicks(g);

 verify(g).draw(argThat(line -> line.getLineEndY() - line.getLineStartY() > 0));
 }

 @Test
 public void testGetXAxisLine2D() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new Double(ymin));
 constraints.setMaximumValue(new Double(ymax));

 Line2D line = utils.getXAxisLine2D();

 assertEquals(coordSystem.getBounds().getMinX(), line.getX1(), 0.01);
 assertEquals(coordSystem.getBounds().getMinY(), line.getY1(), 0.01);
 assertEquals(coordSystem.getBounds().getMaxX(), line.getX2(), 0.01);
 assertEquals(coordSystem.getBounds().getMinY(), line.getY2(), 0.01);
 }

 @Test
 public void testGetYAxisLine2D() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new Double(ymin));
 constraints.setMaximumValue(new Double(ymax));

 Line2D line = utils.getYAxisLine2D();

 assertEquals(coordSystem.getBounds().getMinX(), line.getX1(), 0.01);
 assertEquals(coordSystem.getBounds().getMinY(), line.getY1(), 0.01);
 assertEquals(coordSystem.getBounds().getMinX(), line.getX2(), 0.01);
 assertEquals(coordSystem.getBounds().getMaxY(), line.getY2(), 0.01);
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 double secondYmin = 10.0;
 double secondYmax = 0.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints2.setMinimumColumnValue(xmin);
 constraints2.setMaximumColumnValue(xmax);
 constraints2.setMinimumValue(new Double(secondYmin));
 constraints2.setMaximumValue(new Double(secondYmax));
 model.setSecondYAxisData(new double[][]{
 new double[]{1.0, 2.0, 3.0, 4.0, 5.0},
 new double[]{6.0, 7.0, 8.0, 9.0, 10.0}
 });

 Line2D line = utils.getSecondYAxisLine2D();

 assertEquals(coordSystem.getBounds().getMinX(), line.getX1(), 0.01);
 assertEquals(coordSystem.getBounds().getMaxY(), line.getY1(), 0.01);
 assertEquals(coordSystem.getBounds().getMinX(), line.getX2(), 0.01);
 assertEquals(coordSystem.getBounds().getMinY(), line.getY2(), 0.01);
 }
}