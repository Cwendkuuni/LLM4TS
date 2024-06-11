package net.virtualinfinity.atrobots.gui.renderers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.virtualinfinity.atrobots.measures.Length;
import net.virtualinfinity.atrobots.measures.Temperature;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;

public class RobotRendererTest {

 private Graphics2D g2d;
 private RobotRenderer renderer;

 @Before
 public void setUp() {
 g2d = new StubGraphics2D();
 renderer = new RobotRenderer();
 }

 @Test
 public void testRenderWithDefaultSettingsAndLiveRobot() {
 // Create a robot snapshot with default values
 RobotSnapshot robotSnapshot = new RobotSnapshot(1, new PositionVector(100.0, 100.0), new Length(50.0), new Length(50.0), new Temperature(25.0), new Angle(0.0), new Angle(0.0), true, false, null);

 // Render the robot
 renderer.render(g2d, robotSnapshot, Collections.emptySet());

 // Verify that the body is painted
 assertEquals("Body not painted correctly", new Line2D.Double(100.0, 100.0, 100.0 + Math.cos(0.0) * 15.0, 100.0 + Math.sin(0.0) * 15.0), g2d.getPath());

 // Verify that the turret is painted
 assertEquals("Turret not painted correctly", new Line2D.Double(100.0, 100.0, 100.0 + Math.cos(-Math.PI/2.0) * 5.0, 100.0 + Math.sin(-Math.PI/2.0) * 5.0), g2d.getPath().getSubpath(1, 2));

 // Verify that the armor is painted
 assertEquals("Armor not painted correctly", new Rectangle2D.Double(100.0 - 50.0, 100.0 + 20.0, 50.0, 10.0), g2d.getPath().getBounds2D());

 // Verify that the name is displayed
 assertEquals("Name not displayed correctly", "Robot 1", g2d.getFontMetrics().getStringBounds("Robot 1", g2d).getBounds2D().getCenterX(), g2d.getFontMetrics().getStringBounds("Robot 1", g2d).getBounds2D().getCenterY());
 }

 @Test
 public void testRenderWithDeadRobot() {
 // Create a robot snapshot that is dead
 RobotSnapshot deadRobotSnapshot = new RobotSnapshot(1, new PositionVector(100.0, 100.0), new Length(50.0), new Length(50.0), null, new Angle(0.0), new Angle(0.0), false, false, null);

 // Render the dead robot
 renderer.render(g2d, deadRobotSnapshot, Collections.emptySet());

 // Verify that nothing is painted
 assertNull("Something was painted when it shouldn't have been", g2d.getPath());
 }

 @Test
 public void testRenderWithSelectedRobot() {
 // Create a robot snapshot with a selected ID
 RobotSnapshot selectedRobotSnapshot = new RobotSnapshot(1, new PositionVector(100.0, 100.0), new Length(50.0), new Length(50.0), new Temperature(25.0), new Angle(0.0), new Angle(0.0), true, false, null);
 Set<Integer> selectedRobots = new HashSet<>();
 selectedRobots.add(selectedRobotSnapshot.getId());

 // Render the selected robot
 renderer.render(g2d, selectedRobotSnapshot, selectedRobots);

 // Verify that a selection ellipse is drawn
 Path2D selectionEllipse = (Path2D) g2d.getPath();
 assertEquals("Selection ellipse not drawn correctly", new Ellipse2D.Double(100.0 - 15.0, 100.0 - 15.0, 30.0 * 2.0, 30.0 * 2.0), selectionEllipse);
 }

 @Test
 public void testRenderWithRenderDeadSetToFalse() {
 // Set renderDead to false
 renderer.setRenderDead(false);
 // Create a robot snapshot that is dead
 RobotSnapshot deadRobotSnapshot = new RobotSnapshot(1, new PositionVector(100.0, 100.0), new Length(50.0), new Length(50.0), null, new Angle(0.0), new Angle(0.0), false, false, null);

 // Render the dead robot
 renderer.render(g2d, deadRobotSnapshot, Collections.emptySet());

 // Verify that the dead robot is ignored
 assertNull("Dead robot was painted when it shouldn't have been", g2d.getPath());
 }

 @Test
 public void testRenderWithShowStatusBarsSetToFalse() {
 // Set showStatusBars to false
 renderer.setShowStatusBars(false);
 // Create a living robot snapshot
 RobotSnapshot livingRobotSnapshot = new RobotSnapshot(1, new PositionVector(100.0, 100.0), new Length(50.0), new Length(50.0), new Temperature(25.0), new Angle(0.0), new Angle(0.0), true, false, null);

 // Render the living robot
 renderer.render(g2d, livingRobotSnapshot, Collections.emptySet());

 // Verify that the armor and heat are not painted
 assertNull("Armor or heat were painted when they shouldn't have been", g2d.getPath().getSubpath(0, 1));
 assertNull("Armor or heat were painted when they shouldn't have been", g2d.getPath().getSubpath(2, 3));
 }
}