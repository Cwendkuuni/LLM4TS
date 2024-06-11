package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("java.lang.reflect.AccessibleObject")
public class RobotRendererTest {

 private RobotRenderer renderer;
 private Graphics2D g2dMock;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setup() {
 renderer = new RobotRenderer();
 g2dMock = Mockito.mock(Graphics2D.class);
 robotSnapshot = Mockito.spy(new RobotSnapshot());
 }

 @Test
 public void testRender_deadRobot_notRendered() {
 PowerMockito.when(robotSnapshot.isDead()).thenReturn(true);
 PowerMockito.when(robotSnapshot.getX()).thenReturn(10.0);
 PowerMockito.when(robotSnapshot.getY()).thenReturn(10.0);
 PowerMockito.when(robotSnapshot.getId()).thenReturn(1);

 renderer.render(g2dMock, robotSnapshot, Collections.emptySet());

 PowerMockito.verifyNoMoreInteractions(g2dMock);
 }

 @Test
 public void testRender_notDeadRobot_allMethodsCalled() {
 PowerMockito.when(robotSnapshot.isDead()).thenReturn(false);
 PowerMockito.when(robotSnapshot.getX()).thenReturn(10.0);
 PowerMockito.when(robotSnapshot.getY()).thenReturn(10.0);
 PowerMockito.when(robotSnapshot.getId()).thenReturn(1);
 PowerMockito.when(robotSnapshot.getName()).thenReturn("RobotName");
 PowerMockito.when(robotSnapshot.getArmor()).thenReturn(100.0);
 PowerMockito.when(robotSnapshot.getTemperature()).thenReturn(new Temperature(100.0));
 PowerMockito.when(robotSnapshot.isActiveShield()).thenReturn(true);
 PowerMockito.when(robotSnapshot.getHeading()).thenReturn(new AbsoluteAngle(Math.PI / 2.0));
 PowerMockito.when(robotSnapshot.getTurretHeading()).thenReturn(new AbsoluteAngle(Math.PI / 4.0));
 PowerMockito.when(robotSnapshot.isRenderDead()).thenReturn(renderer.isRenderDead());
 PowerMockito.when(robotSnapshot.isShowStatusBars()).thenReturn(renderer.isShowStatusBars());
 PowerMockito.when(robotSnapshot.isShowName()).thenReturn(renderer.isShowName());
 PowerMockito.when(robotSnapshot.isFillShield()).thenReturn(renderer.isFillShield());

 renderer.render(g2dMock, robotSnapshot, Collections.emptySet());

 verify(g2dMock, times(2)).setPaint(any(Paint.class));
 verify(g2dMock, times(1)).setStroke(any(Stroke.class));
 verify(g2dMock, times(1)).draw(any(Shape.class));
 verify(g2dMock, times(1)).fill(any(Shape.class));

 PowerMockito.verify(g2dMock).setPaint(eq(Color.yellow));
 PowerMockito.verify(g2dMock).drawString(eq("RobotName"), anyFloat(), anyFloat());

 PowerMockito.verify(g2dMock).setPaint(eq(new Color(0.5f, 0.5f, 0.0f, 0.8f)));
 PowerMockito.verify(g2dMock).fill(any(Ellipse2D.Double.class));
 PowerMockito.verify(g2dMock).fill(any(Ellipse2D.Double.class));

 PowerMockito.verifyNoMoreInteractions(g2dMock);
 }
}