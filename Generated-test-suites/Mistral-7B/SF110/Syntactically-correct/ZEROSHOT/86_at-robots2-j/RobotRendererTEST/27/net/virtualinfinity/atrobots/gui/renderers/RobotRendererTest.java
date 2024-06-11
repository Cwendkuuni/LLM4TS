package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import net.virtualinfinity.atrobots.measures.Length;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class RobotRendererTest {

 private Graphics2D g2d;
 private RobotRenderer renderer;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setup() {
 g2d = mock(Graphics2D.class);
 renderer = new RobotRenderer();
 robotSnapshot = new RobotSnapshot(1, new Point2D.Double(10.0, 10.0), new Point2D.Double(0.0, 0.0), new Length(100.0), 50.0, 100.0, true, false, false, 1.0, 0.5, new Point2D.Double(0.0, 0.0), new Point2D.Double(0.0, 0.0), 0.0, 0.0, "TestBot");
 }

 @Test
 public void testRender() {
 renderer.render(g2d, robotSnapshot, Collections.emptySet());

 // Add checks for paintBody, paintShield, paintTurret, paintArmor, paintHeat, paintName, and paintSelection here
 }

 @Test
 public void testIsRenderDead() {
 renderer.setRenderDead(false);
 assertFalse(renderer.isRenderDead());

 renderer.setRenderDead(true);
 assertTrue(renderer.isRenderDead());
 }

 @Test
 public void testIsShowStatusBars() {
 renderer.setShowStatusBars(false);
 assertFalse(renderer.isShowStatusBars());

 renderer.setShowStatusBars(true);
 assertTrue(renderer.isShowStatusBars());
 }

 @Test
 public void testIsShowName() {
 renderer.setShowName(false);
 assertFalse(renderer.isShowName());

 renderer.setShowName(true);
 assertTrue(renderer.isShowName());
 }

 @Test
 public void testIsFillShield() {
 renderer.setFillShield(false);
 assertFalse(renderer.isFillShield());

 renderer.setFillShield(true);
 assertTrue(renderer.isFillShield());
 }
}