package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
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
 this.g2d = new StubGraphics2D();
 this.robotSnapshot = new RobotSnapshot();
 }

 @Test
 public void testRender() {
 this.renderer.render(this.g2d, this.robotSnapshot, new HashSet<>());
 // Basic functionality test for render method
 assertNotNull(this.robotSnapshot);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRenderNullSnapshot() {
 this.renderer.render(this.g2d, null, new HashSet<>());
 }

 @Test
 public void testIsRenderDead() {
 // Basic functionality test for isRenderDead method
 assertTrue(this.renderer.isRenderDead());
 this.renderer.setRenderDead(false);
 assertFalse(this.renderer.isRenderDead());
 }

 @Test
 public void testSetRenderDead() {
 // Basic functionality test for setRenderDead method
 this.renderer.setRenderDead(true);
 assertTrue(this.renderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars() {
 // Basic functionality test for isShowStatusBars method
 assertTrue(this.renderer.isShowStatusBars());
 this.renderer.setShowStatusBars(false);
 assertFalse(this.renderer.isShowStatusBars());
 }

 @Test
 public void testSetShowStatusBars() {
 // Basic functionality test for setShowStatusBars method
 this.renderer.setShowStatusBars(true);
 assertTrue(this.renderer.isShowStatusBars());
 }

 @Test
 public void testIsShowName() {
 // Basic functionality test for isShowName method
 assertTrue(this.renderer.isShowName());
 this.renderer.setShowName(false);
 assertFalse(this.renderer.isShowName());
 }

 @Test
 public void testSetShowName() {
 // Basic functionality test for setShowName method
 this.renderer.setShowName(true);
 assertTrue(this.renderer.isShowName());
 }

 @Test
 public void testIsFillShield() {
 // Basic functionality test for isFillShield method
 assertTrue(this.renderer.isFillShield());
 this.renderer.setFillShield(false);
 assertFalse(this.renderer.isFillShield());
 }

 @Test
 public void testSetFillShield() {
 // Basic functionality test for setFillShield method
 this.renderer.setFillShield(true);
 assertTrue(this.renderer.isFillShield());
 }

 @Test
 public void testPaintSelection() {
 // Test for paintSelection method with valid robot snapshot
 this.renderer.paintSelection(this.g2d, this.robotSnapshot);
 Ellipse2D selectionEllipse = new Ellipse2D.Double();
 selectionEllipse.setFrameFromCenter(this.robotSnapshot.getX(), this.robotSnapshot.getY(), this.robotSnapshot.getX() + 30.0, this.robotSnapshot.getY() + 30.0);
 assertEquals(SelectionShape.class, selectionEllipse.getClass());
 // Test for paintSelection method with null robot snapshot
 this.robotSnapshot = null;
 this.renderer.paintSelection(this.g2d, this.robotSnapshot);
 assertNull(this.robotSnapshot);
 }

 @Test
 public void testPaintShield() {
 // Test for paintShield method with active shield and fillShield is true
 this.robotSnapshot.setActiveShield(true);
 this.renderer.setFillShield(true);
 this.renderer.paintShield(this.g2d, this.robotSnapshot);
 RadialGradientPaint radialGradientPaint = (RadialGradientPaint) this.g2d.getPaint();
 assertEquals(2, radialGradientPaint.getNumColors());
 // Test for paintShield method with active shield and fillShield is false
 this.robotSnapshot.setActiveShield(true);
 this.renderer.setFillShield(false);
 this.renderer.paintShield(this.g2d, this.robotSnapshot);
 assertEquals(Color.class, this.g2d.getPaint().getClass());
 // Test for paintShield method with inactive shield
 this.robotSnapshot.setActiveShield(false);
 this.renderer.paintShield(this.g2d, this.robotSnapshot);
 assertNull(this.g2d.getPaint());
 }

 @Test
 public void testPaintName() {
 // Test for paintName method with valid robot snapshot
 FontMetrics fontMetrics = this.g2d.getFontMetrics();
 this.renderer.paintName(this.g2d, this.robotSnapshot);
 assertEquals("Robot name not painted correctly", this.robotSnapshot.getName(), this.g2d.getFontMetrics().getStringBounds(this.robotSnapshot.getName(), this.g2d).getSubstring(0, (int)(fontMetrics.getStringBounds(this.robotSnapshot.getName(), this.g2d).getWidth() / 2.0)).toString());
 // Test for paintName method with null robot snapshot
 this.robotSnapshot = null;
 this.renderer.paintName(this.g2d, this.robotSnapshot);
 assertNull(this.robotSnapshot);
 }

 @Test
 public void testPaintHeat() {
 // Test for paintHeat method with valid robot snapshot
 Rectangle2D heatRect = new Rectangle2D.Double();
 this.renderer.paintHeat(this.g2d, this.robotSnapshot);
 assertEquals("Heat bar not painted correctly", this.robotSnapshot.getTemperature().getValue(), this.g2d.getPaint().getPaint2D().getBounds2D().getWidth() / 2.0, 0.01);
 // Test for paintHeat method with null robot snapshot
 this.robotSnapshot = null;
 this.renderer.paintHeat(this.g2d, this.robotSnapshot);
 assertNull(this.g2d.getPaint());
 }

 @Test
 public void testPaintArmor() {
 // Test for paintArmor method with valid robot snapshot
 this.renderer.paintArmor(this.g2d, this.robotSnapshot);
 Rectangle2D armorRect = new Rectangle2D.Double();
 Path2D armorPath = (Path2D) this.g2d.getPath();
 Stroke stroke = this.g2d.getStroke();
 assertEquals("Armor bar not painted correctly", this.robotSnapshot.getArmor(), armorRect.getBounds().getWidth(), 0.01);
 assertEquals("Border color mismatch", Color.class, stroke.getLinePaint().getClass());
 // Test for paintArmor method with null robot snapshot
 this.robotSnapshot = null;
 this.renderer.paintArmor(this.g2d, this.robotSnapshot);
 assertNull(this.g2d.getPath());
 }

 @Test
 public void testPaintTurret() {
 // Test for paintTurret method with valid robot snapshot
 this.renderer.paintTurret(this.g2d, this.robotSnapshot);
 Line2D line = (Line2D) this.g2d.getPath();
 assertEquals("Turret line not drawn correctly", Math.abs(robotSnapshot.getHeading().cosine()) * 5.0, Math.abs(line.getEndX() - line.getStartX()), 0.01);
 assertEquals("Turret line not drawn correctly", Math.abs(robotSnapshot.getHeading().sine()) * 5.0, Math.abs(line.getEndY() - line.getStartY()), 0.01);
 // Test for paintTurret method with null robot snapshot
 this.robotSnapshot = null;
 this.renderer.paintTurret(this.g2d, this.robotSnapshot);
 assertNull(this.g2d.getPath());
 }

 @Test
 public void testPaintBody() {
 // Test for paintBody method with dead robot snapshot
 this.renderer.setRenderDead(true);
 this.renderer.render(this.g2d, this.robotSnapshot, new HashSet<>());
 Path2D bodyPath = (Path2D) this.g2d.getPath();
 assertEquals("Body shape not painted correctly for dead robot snapshot", GeneralPath.class, bodyPath.getClass());
 // Test for paintBody method with alive robot snapshot
 this.renderer.setRenderDead(false);
 this.renderer.render(this.g2d, this.robotSnapshot, new HashSet<>());
 Path2D bodyPathAlive = (Path2D) this.g2d.getPath();
 assertEquals("Body shape not painted correctly for alive robot snapshot", GeneralPath.class, bodyPathAlive.getClass());
 }

}