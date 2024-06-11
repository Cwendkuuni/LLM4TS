import org.junit.Test;
import static org.junit.Assert.*;
import de.progra.charting.*;
import de.progra.charting.model.*;
import java.text.DecimalFormat;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.*;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utilities;

 @SuppressWarnings("deprecation")
 @Test
 public void testComputeLeftMargin() {
 CoordSystem coord = new CoordSystem(100, 100, 200, 200);
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

 assertEquals(20, utilities.computeLeftMargin());

 constraints.setMinimumColumnValue(-2.0);
 constraints.setMaximumColumnValue(2.0);
 constraints2.setMaximumColumnValue(1.0);

 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

 TextLayout layout = new TextLayout("Test", coord.getFont(), coord.getFontRenderContext());
 int maxlmargin = utilities.computeYAxisLabelWidth() + 6;
 maxlmargin = Math.max(maxlmargin, (int)layout.getBounds().getWidth() + 6);
 int margin = (int)(maxlmargin - (-2.0) / (2.0) * (coord.getBounds().getWidth() - coord.getRightMargin()));
 margin += 5;
 assertEquals(31, utilities.computeLeftMargin());
 }

 @Test
 public void testComputeRightMargin() {
 CoordSystem coord = new CoordSystem(100, 100, 200, 200);
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

 assertEquals(15, utilities.computeRightMargin());
 }

 @Test
 public void testComputeTopMargin() {
 CoordSystem coord = new CoordSystem(100, 100, 200, 200);
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

 TextLayout layout = new TextLayout("Test", coord.getFont(), coord.getFontRenderContext());
 int height = (int)(layout.getBounds().getHeight() + 15.0 / 3.0 + layout.getDescent());
 assertEquals(30, utilities.computeTopMargin());
 }

 @Test
 public void testComputeBottomMargin() {
 CoordSystem coord = new CoordSystem(100, 100, 200, 200);
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

 assertEquals(20, utilities.computeBottomMargin());

 DecimalFormat df = utilities.c.getXDecimalFormat();
 constraints.setMinimumValue(-1.0);
 constraints.setMaximumValue(1.0);

 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

 TextLayout xAxisLabelLayout = new TextLayout(df.format(-1.0), coord.getFont(), coord.getFontRenderContext());
 int maxbmargin = utilities.computeXAxisLabelHeight() + 6;
 maxbmargin = Math.max(maxbmargin, (int)xAxisLabelLayout.getBounds().getHeight() + 6);
 int margin = (int)(maxbmargin - (-1.0) / (1.0) * (coord.getBounds().getHeight() - coord.getTopMargin()));
 margin += 10;
 assertEquals(46, utilities.computeBottomMargin());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullCoordSystem() {
 utilities = new CoordSystemUtilities(null, constraints, constraints2, model);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullConstraints() {
 utilities = new CoordSystemUtilities(coord, null, constraints2, model);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullConstraints2() {
 utilities = new CoordSystemUtilities(coord, constraints, null, model);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithNullModel() {
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, null);
 }

 @Test
 public void testComputeXAxisLabelHeight() {
 CoordSystem coord = new CoordSystem(100, 100, 200, 200);
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

 DecimalFormat df = utilities.c.getXDecimalFormat();
 constraints.setMinimumColumnValue(-5.0);
 constraints.setMaximumColumnValue(5.0);
 constraints2.setMinimumValue(-2.0);
 constraints2.setMaximumValue(2.0);
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 double min = constraints.getMinimumColumnValue();
 double max = constraints.getMaximumColumnValue();
 double tick = ChartUtilities.calculateTickSpacing(min, max);
 double ypt = 0.0;
 if (constraints.getMinimumValue().doubleValue() > 0.0) {
 ypt = constraints.getMinimumValue().doubleValue();
 }
 else if (constraints.getMaximumValue().doubleValue() < 0.0) {
 ypt = constraints.getMaximumValue().doubleValue();
 }
 boolean paint = false;
 FontRenderContext frc = utilities.c.getFontRenderContext();
 Font f = utilities.c.getFont();
 int height = 0;
 for (double d = min; d <= max; d += tick) {
 if (paint) {
 String sb = df.format(d);
 Rectangle2D r = f.getStringBounds(sb, frc);
 height = Math.max(height, (int)r.getHeight());
 }
 paint = !paint;
 }
 assertEquals(41, height);
 }

 @Test
 public void testComputeYAxisLabelWidth() {
 CoordSystem coord = new CoordSystem(100, 100, 200, 200);
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

 DecimalFormat df = utilities.c.getYDecimalFormat();
 constraints.setMinimumValue(-5.0);
 constraints.setMaximumValue(5.0);
 constraints2.setMinimumColumnValue(-2.0);
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
 double min = constraints.getMinimumValue().doubleValue();
 double max = constraints.getMaximumValue().doubleValue();
 double tick = ChartUtilities.calculateTickSpacing(min, max);
 double xpt = 0.0;
 if (constraints2.getMinimumColumnValue() > 0.0) {
 xpt = constraints2.getMinimumColumnValue();
 }
 else if (constraints2.getMaximumColumnValue() < 0.0) {
 xpt = constraints2.getMaximumColumnValue();
 }
 boolean paint = false;
 FontRenderContext frc = utilities.c.getFontRenderContext();
 Font f = utilities.c.getFont();
 int width = 0;
 for (double d = min; d <= max; d += tick) {
 if (paint) {
 String sb = df.format(d);
 Rectangle2D r = f.getStringBounds(sb, frc);
 width = Math.max(width, (int)r.getWidth());
 }
 paint = !paint;
 }
 assertEquals(45, width);
 }

 @Test
 public void testDrawNumericalXAxisTicks() {
 CoordSystem coord = new CoordSystem(100, 100, 200, 200);
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

 Graphics2D g = new StrokeTest().getGraphics();
 utilities.drawNumericalXAxisTicks(g);

 int tickCount = 0;
 for (Shape shape : g.getShapes()) {
 if (shape.getClass() == Line.Class) {
 tickCount++;
 }
 }
 assertEquals(5, tickCount);
 }

 @Test
 public void testDrawXAxisTicks() {
 CoordSystem coord = new CoordSystem(100, 100, 200, 200);
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

 Graphics2D g = new StrokeTest().getGraphics();
 utilities.drawXAxisTicks(g);

 int tickCount = 0;
 for (Shape shape : g.getShapes()) {
 if (shape.getClass() == Line.Class) {
 tickCount++;
 }
 }
 assertEquals(11, tickCount);
 }

 @Test
 public void testDrawYAxisTicks() {
 CoordSystem coord = new CoordSystem(100, 100, 200, 200);
 ChartDataModelConstraints constraints = new ChartDataModelConstraints();
 ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
 ChartDataModel model = new ChartDataModel();
 utilities = new CoordSystemUtilities(coord, constraints, constraints2, model);

 Graphics2D g = new StrokeTest().getGraphics();
 utilities.drawYAxisTicks(g);

 int tickCount = 0;
 for (Shape shape : g.getShapes()) {
 if (shape.getClass() == Line.Class) {
 tickCount++;
 }
 }
 assertEquals(6, tickCount);
 }

 @Test
 public void testGetXAxisLine2D() {
 CoordSystem coord = new CoordSystem(100, 100, 200, 200);
 utilities = new CoordSystemUtilities(coord, new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
 Line2D xAxisLine2D = utilities.getXAxisLine2D();
 assertEquals(-50.0, xAxisLine2D.getX1(), 0.01);
 assertEquals(50.0, xAxisLine2D.getX2(), 0.01);
 assertEquals(0.0, xAxisLine2D.getY1(), 0.01);
 assertEquals(0.0, xAxisLine2D.getY2(), 0.01);
 }

 @Test
 public void testGetYAxisLine2D() {
 CoordSystem coord = new CoordSystem(100, 100, 200, 200);
 utilities = new CoordSystemUtilities(coord, new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
 Line2D yAxisLine2D = utilities.getYAxisLine2D();
 assertEquals(0.0, yAxisLine2D.getX1(), 0.01);
 assertEquals(0.0, yAxisLine2D.getX2(), 0.01);
 assertEquals(-50.0, yAxisLine2D.getY1(), 0.01);
 assertEquals(50.0, yAxisLine2D.getY2(), 0.01);
 }

 @Test
 public void testGetSecondYAxisLine2D() {
 CoordSystem coord = new CoordSystem(100, 100, 200, 200);
 utilities = new CoordSystemUtilities(coord, new ChartDataModelConstraints(), new ChartDataModelConstraints(1), new ChartDataModel());
 Line2D secondYAxisLine2D = utilities.getSecondYAxisLine2D();
 assertEquals(-50.0, secondYAxisLine2D.getX1(), 0.01);
 assertEquals(50.0, secondYAxisLine2D.getX2(), 0.01);
 assertEquals(0.0, secondYAxisLine2D.getY1(), 0.01);
 assertEquals(200.0, secondYAxisLine2D.getY2(), 0.01);
 }

}