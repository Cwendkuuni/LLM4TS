package net.virtualinfinity.atrobots.gui.renderers.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.*;
import org.junit.Test;

import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;

public class RobotRendererTest {

 private RobotRenderer renderer;
 private Graphics2D graphics2d;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setUp() {
 this.renderer = new RobotRenderer();
 this.graphics2d = Mockito.mock(Graphics2D.class);
 this.robotSnapshot = new RobotSnapshot(1, new PositionVector(10.0, 10.0), new Angle(Math.PI / 2), true, 50, 100, 50.0, new Color("Red"), new Color("Red"), new Color("Red"), new Color("Red"), new Color("Red"), new Color("Red"), new Color("Red"), new Color("Red"), new Color("Red"), new Color("Red"), new Color("Red"), new Color("Red"), new Color("Red"), new Color("Red"), new Color("Red"), new Color("Red"));
 }

 @After
 public void tearDown() {
 this.renderer = null;
 this.graphics2d = null;
 this.robotSnapshot = null;
 }

 @Test
 public void testRender() {
 this.renderer.render(this.graphics2d, this.robotSnapshot, new HashSet<Integer>());
 // Add assertions for the rendered robot here
 }

 @Test
 public void testIsRenderDead() {
 assertTrue(this.renderer.isRenderDead());
 this.renderer.setRenderDead(false);
 assertFalse(this.renderer.isRenderDead());
 }

 @Test
 public void testSetRenderDead() {
 this.renderer.setRenderDead(true);
 assertTrue(this.renderer.isRenderDead());
 this.renderer.setRenderDead(false);
 assertFalse(this.renderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars() {
 assertTrue(this.renderer.isShowStatusBars());
 this.renderer.setShowStatusBars(false);
 assertFalse(this.renderer.isShowStatusBars());
 }

 @Test
 public void testSetShowStatusBars() {
 this.renderer.setShowStatusBars(true);
 assertTrue(this.renderer.isShowStatusBars());
 this.renderer.setShowStatusBars(false);
 assertFalse(this.renderer.isShowStatusBars());
 }

 @Test
 public void testIsShowName() {
 assertTrue(this.renderer.isShowName());
 this.renderer.setShowName(false);
 assertFalse(this.renderer.isShowName());
 }

 @Test
 public void testSetShowName() {
 this.renderer.setShowName(true);
 assertTrue(this.renderer.isShowName());
 this.renderer.setShowName(false);
 assertFalse(this.renderer.isShowName());
 }

 @Test
 public void testIsFillShield() {
 assertTrue(this.renderer.isFillShield());
 this.renderer.setFillShield(false);
 assertFalse(this.renderer.isFillShield());
 }

 @Test
 public void testSetFillShield() {
 this.renderer.setFillShield(true);
 assertTrue(this.renderer.isFillShield());
 this.renderer.setFillShield(false);
 assertFalse(this.renderer.isFillShield());
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullGraphics2D() {
 this.renderer.render(null, this.robotSnapshot, new HashSet<Integer>());
 }

 @Test(expected = NullPointerException.class)
 public void testRenderWithNullRobotSnapshot() {
 this.renderer.render(this.graphics2d, null, new HashSet<Integer>());
 }

 @Test
 public void testPaintSelection() {
 final Rectangle2D.Double selectionBoundingBox = new Rectangle2D.Double();
 this.renderer.paintSelection(this.graphics2d, this.robotSnapshot);
 selectionBoundingBox.setFrameFromCenter(this.robotSnapshot.getX(), this.robotSnapshot.getY(), 30.0, 30.0);
 assertTrue(selectionBoundingBox.intersects(new Rectangle2D.Double(0.0, 0.0, 1.0, 1.0)));
 }

 @Test
 public void testPaintShield() {
 final Color fillColor1 = new Color(0.5f, 0.5f, 0.0f, 0.8f);
 final Color fillColor2 = new Color(0.0f, 0.0f, 0.5f, 0.1f);
 final Color borderColor = Color.white;
 final Ellipse2D.Double shieldBoundingBox = new Ellipse2D.Double();
 final Shape expectedShape = new Ellipse2D.Double();
 this.renderer.paintShield(this.graphics2d, this.robotSnapshot);
 this.renderer.setFillShield(false);
 this.renderer.paintShield(this.graphics2d, this.robotSnapshot);
 // Assertions for the painted shield go here
 }

 @Test
 public void testPaintName() {
 final String robotName = "TestRobot";
 final FontMetrics fontMetrics = Mockito.mock(FontMetrics.class);
 final String robotNameWithTrimmedLength = robotName.substring(0, 8);
 this.graphics2d.setFont(Mockito.mock(Font.class));
 this.graphics2d.setFont(Mockito.mock(Font.class));
 this.renderer.paintName(this.graphics2d, this.robotSnapshot);
 assertEquals(robotNameWithTrimmedLength, this.graphics2d.getFontMetrics().getStringBounds(robotName, this.graphics2d).toString().substring(1, 10));
 }

 @Test
 public void testPaintHeat() {
 final int heatLevel = 50;
 final int maxHeatLevel = 100;
 final int logScale = 2;
 final double heatPercentage = (double)heatLevel / maxHeatLevel;
 final int heatWidth = (int)(heatPercentage * 100.0);
 final int heatHeight = 10;
 final Rectangle2D.Double heatBoundingBox = new Rectangle2D.Double();
 final Shape expectedShape = new Rectangle2D.Double(0.0, 0.0, heatWidth, heatHeight);
 this.renderer.paintHeat(this.graphics2d, this.robotSnapshot);
 this.renderer.setRenderDead(true);
 this.renderer.paintHeat(this.graphics2d, this.robotSnapshot);
 // Assertions for the painted heat go here
 }

 @Test
 public void testPaintArmor() {
 final int armorLevel = 100;
 final int armorWidth = 100;
 final int armorHeight = 10;
 final double armorPercentage = (double)armorLevel / 100.0;
 final Color armorColor1 = new Color(0.0f, 0.0f, 1.0f, 0.6f);
 final Color armorColor2 = new Color(0.0f, 1.0f, 0.0f, 0.6f);
 final Shape expectedShape = new Rectangle2D.Double(0.0, 0.0, armorWidth, armorHeight);
 this.renderer.paintArmor(this.graphics2d, this.robotSnapshot);
 // Assertions for the painted armor go here
 }

 @Test
 public void testPaintTurret() {
 final int turretLength = 5;
 final double turretHeadingRadians = Math.PI / 4;
 final double turretHeadingDegrees = Math.toDegrees(turretHeadingRadians);
 final double turretX = this.robotSnapshot.getX();
 final double turretY = this.robotSnapshot.getY();
 final Line2D.Double expectedLine = new Line2D.Double(turretX, turretY, turretX + Math.cos(turretHeadingRadians) * turretLength, turretY + Math.sin(turretHeadingRadians) * turretLength);
 this.renderer.paintTurret(this.graphics2d, this.robotSnapshot);
 // Assertions for the painted turret go here
 }

 @Test
 public void testPaintBody() {
 final PositionVector robotPosition = new PositionVector(10.0, 10.0);
 final Angle robotHeading = new Angle(Math.PI / 2);
 final double bodyWidth = 30.0;
 final double bodyHeight = 30.0;
 final double robotX = robotPosition.getX();
 final double robotY = robotPosition.getY();
 final double robotHeadingRadians = robotHeading.getRadians();
 final double robotHeadingCounterClockwiseRadians = robotHeading.counterClockwise(new RelativeAngle(Math.PI / 2)).getRadians();
 final double robotHeadingClockwiseRadians = robotHeading.clockwise(new RelativeAngle(Math.PI / 2)).getRadians();
 final GeneralPath expectedPath = new GeneralPath();
 expectedPath.moveTo(robotX + Math.cos(robotHeadingRadians) * 15.0, robotY + Math.sin(robotHeadingRadians) * 15.0);
 expectedPath.lineTo(robotX + Math.cos(robotHeadingCounterClockwiseRadians) * 9.0, robotY + Math.sin(robotHeadingCounterClockwiseRadians) * 9.0);
 expectedPath.lineTo(robotX, robotY);
 expectedPath.lineTo(robotX + Math.cos(robotHeadingClockwiseRadians) * 9.0, robotY + Math.sin(robotHeadingClockwiseRadians) * 9.0);
 expectedPath.closePath();
 this.renderer.paintBody(this.graphics2d, this.robotSnapshot);
 // Assertions for the painted body go here
 }
}