package net.virtualinfinity.atrobots.gui.renderers.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.*;

public class RobotRendererTest {

 private RobotRenderer renderer;
 private Graphics2D g2d;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 this.renderer = new RobotRenderer();
 this.g2d = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 this.robotSnapshot = new RobotSnapshot(1, new PositionVector(0.0, 0.0), new VelocityVector(0.0, 0.0), new Angle(0.0), new AngularVelocity(0.0), new Battery(100.0), new Temperature(50.0), new Armor(100.0), new Shield(true), new EnergyCell(100.0, 100.0), "RobotName", new AbsoluteAngle(0.0), new AbsoluteAngle(0.0), new TurretHeading(0.0), new TurretHeading(0.0), new Turret(new Dimension(10.0, 10.0), new PositionVector(0.0, 0.0), new VelocityVector(0.0, 0.0), new Angle(0.0), new AngularVelocity(0.0), new Dimension(5.0, 5.0), new Angle(0.0), new AngularVelocity(0.0), new Dimension(10.0, 10.0), new ArrayList<>() {
 {
 add(1);
 }
 }, new ArrayList<>() {
 {
 add(1);
 }
 }));
 }

 @Test
 public void testRender() {
 this.renderer.render(this.g2d, this.robotSnapshot, new HashSet<Integer>() {
 {
 add(1);
 }
 });
 assertFalse(this.robotSnapshot.isDead());
 assertTrue(this.renderer.isShowStatusBars());
 assertTrue(this.renderer.isFillShield());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRenderWithNullGraphics2D() {
 this.renderer.render(null, this.robotSnapshot, new HashSet<Integer>() {
 {
 add(1);
 }
 });
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRenderWithNullRobotSnapshot() {
 this.renderer.render(this.g2d, null, new HashSet<Integer>() {
 {
 add(1);
 }
 });
 }

 @Test
 public void testIsRenderDead() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setRenderDead(false);
 assertFalse(renderer.isRenderDead());
 renderer.setRenderDead(true);
 assertTrue(renderer.isRenderDead());
 }

 @Test
 public void testSetRenderDead() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setRenderDead(false);
 renderer.setRenderDead(true);
 assertTrue(renderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setShowStatusBars(false);
 assertFalse(renderer.isShowStatusBars());
 renderer.setShowStatusBars(true);
 assertTrue(renderer.isShowStatusBars());
 }

 @Test
 public void testSetShowStatusBars() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setShowStatusBars(false);
 renderer.setShowStatusBars(true);
 assertTrue(renderer.isShowStatusBars());
 }

 @Test
 public void testIsShowName() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setShowName(false);
 assertFalse(renderer.isShowName());
 renderer.setShowName(true);
 assertTrue(renderer.isShowName());
 }

 @Test
 public void testSetShowName() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setShowName(false);
 renderer.setShowName(true);
 assertTrue(renderer.isShowName());
 }

 @Test
 public void testIsFillShield() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setFillShield(false);
 assertFalse(renderer.isFillShield());
 renderer.setFillShield(true);
 assertTrue(renderer.isFillShield());
 }

 @Test
 public void testSetFillShield() {
 RobotRenderer renderer = new RobotRenderer();
 renderer.setFillShield(false);
 renderer.setFillShield(true);
 assertTrue(renderer.isFillShield());
 }

 @Test
 public void testPaintSelection() {
 Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 this.renderer.paintSelection(g, this.robotSnapshot);
 Point2D center = new Point2D.Double(this.robotSnapshot.getX(), this.robotSnapshot.getY());
 Shape shape = new Ellipse2D.Double(center.getX() - 15.5, center.getY() - 15.5, 30.0, 30.0);
 assertTrue(g.getClip().intersects(shape));
 }

 @Test
 public void testPaintShield() {
 Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 this.renderer.setFillShield(true);
 this.renderer.paintShield(g, this.robotSnapshot);
 Point2D center = new Point2D.Double(this.robotSnapshot.getX(), this.robotSnapshot.getY());
 Shape shape = new Ellipse2D.Double(center.getX() - 7.5, center.getY() - 7.5, 15.0, 15.0);
 assertTrue(g.getClip().intersects(shape));
 }

 @Test
 public void testPaintName() {
 Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 this.renderer.paintName(g, this.robotSnapshot);
 String name = this.robotSnapshot.getName();
 Point2D point = new Point2D.Double(this.robotSnapshot.getX() - g.getFontMetrics().getStringBounds(name, g).getWidth() / 2.0, this.robotSnapshot.getY() - 10.0);
 assertEquals(name, g.getFontMetrics().getStringPropertyName(g.getFont().getFontName()));
 assertEquals(point.getX(), g.getClipBounds().getMinX() + g.getFontMetrics().getStringBounds(name, g).getBounds().getMinX());
 assertEquals(point.getY(), g.getClipBounds().getMinY() + g.getFontMetrics().getStringBounds(name, g).getBounds().getMinY());
 }

 @Test
 public void testPaintHeat() {
 Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 this.renderer.paintHeat(g, this.robotSnapshot);
 Rectangle2D rect = new Rectangle2D.Double(this.robotSnapshot.getX() - 50.0, this.robotSnapshot.getY() + 35.0, 100.0, 10.0);
 assertEquals(RobotRenderer.HEAT_COLOR1, g.getPaint());
 assertEquals(RobotRenderer.HEAT_COLOR2, g.getPaint().getPaint2DInstance());
 Shape shape = new Rectangle2D.Double(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
 assertEquals(rect.getWidth(), g.getClip().getBounds().getWidth());
 assertEquals(rect.getHeight(), g.getClip().getBounds().getHeight());
 assertTrue(g.getClip().intersects(shape));
 }

 @Test
 public void testPaintArmor() {
 Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 this.renderer.paintArmor(g, this.robotSnapshot);
 Rectangle2D rect = new Rectangle2D.Double(this.robotSnapshot.getX() - 50.0, this.robotSnapshot.getY() + 20.0, this.robotSnapshot.getArmor(), 10.0);
 assertEquals(new Color(0.0f, 0.0f, 1.0f, 0.6f), g.getPaint());
 Shape shape = new Rectangle2D.Double(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
 assertEquals(rect.getWidth(), g.getClip().getBounds().getWidth());
 assertEquals(rect.getHeight(), g.getClip().getBounds().getHeight());
 assertTrue(g.getClip().intersects(shape));
 }

 @Test
 public void testPaintTurret() {
 Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 this.renderer.paintTurret(g, this.robotSnapshot);
 Line2D line = new Line2D.Double(this.robotSnapshot.getX(), this.robotSnapshot.getY(), this.robotSnapshot.getX() + this.robotSnapshot.getTurretHeading().cosine() * 5.0, this.robotSnapshot.getY() + this.robotSnapshot.getTurretHeading().sine() * 5.0);
 assertEquals(Color.white, g.getPaint());
 assertTrue(g.getClip().intersects(line));
 }

 @Test
 public void testPaintBody() {
 Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 this.renderer.setRenderDead(true);
 this.renderer.paintBody(g, this.robotSnapshot);
 BasicStroke stroke = new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f);
 assertEquals(Color.orange.darker().darker(), g.getPaint());
 Shape shape = new GeneralPath();
 shape.moveTo(this.robotSnapshot.getX() + this.robotSnapshot.getHeading().cosine() * 15.0, this.robotSnapshot.getY() + this.robotSnapshot.getHeading().sine() * 15.0);
 AbsoluteAngle cc = this.robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160));
 AbsoluteAngle c = this.robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160));
 shape.lineTo(this.robotSnapshot.getX() + cc.cosine() * 9.0, this.robotSnapshot.getY() + cc.sine() * 9.0);
 shape.lineTo(this.robotSnapshot.getX(), this.robotSnapshot.getY());
 shape.lineTo(this.robotSnapshot.getX() + c.cosine() * 9.0, this.robotSnapshot.getY() + c.sine() * 9.0);
 shape.closePath();
 assertEquals(stroke, g.getStroke());
 assertTrue(g.getClip().intersects(shape));
 }

 @Test
 public void testPaintBodyWithActiveShield() {
 Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
 this.renderer.setRenderDead(false);
 this.robotSnapshot.setActiveShield(true);
 this.renderer.paintBody(g, this.robotSnapshot);
 RadialGradientPaint gradient = new RadialGradientPaint(this.robotSnapshot.getPositionVector().toPoint2D(), 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), Color.white });
 assertEquals(gradient, g.getPaint());
 Shape shape = new Ellipse2D.Double(this.robotSnapshot.getX() - 7.5, this.robotSnapshot.getY() - 7.5, 15.0, 15.0);
 assertEquals(15.0, g.getClip().getBounds().getWidth());
 assertEquals(15.0, g.getClip().getBounds().getHeight());
 assertTrue(g.getClip().intersects(shape));
 }

}