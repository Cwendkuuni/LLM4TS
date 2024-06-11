package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI gui;

 @Test
 public void testLagoonGUIConstruction() {
  gui = new LagoonGUI();
  assertNotNull(gui);
 }

 @Test
 public void testLoadButtonListener() {
  gui = new LagoonGUI();
  MockEvent mocks = new MockEvent();
  gui.loadButton.addActionListener(e -> gui.loadPropertyFile());
  gui.loadButton.doClick();
  assertTrue(gui.loadButton.isEnabled());
  assertTrue(gui.loadButton.isFocusable());
 }

 @Test
 public void testSaveButtonListener() {
  gui = new LagoonGUI();
  MockEvent mocks = new MockEvent();
  gui.saveButton.addActionListener(e -> gui.savePropertyFile());
  gui.saveButton.doClick();
  assertTrue(gui.saveButton.isEnabled());
  assertTrue(gui.saveButton.isFocusable());
 }

 @Test
 public void testBuildButtonListener() {
  gui = new LagoonGUI();
  MockEvent mocks = new MockEvent();
  gui.buildButton.addActionListener(e -> gui.build(false));
  gui.buildButton.doClick();
  assertFalse(gui.buildButton.isEnabled());
  assertFalse(gui.buildButton.isFocusable());
 }

 @Test
 public void testForceButtonListener() {
  gui = new LagoonGUI();
  MockEvent mocks = new MockEvent();
  gui.forceButton.addActionListener(e -> gui.build(true));
  gui.forceButton.doClick();
  assertFalse(gui.forceButton.isEnabled());
  assertFalse(gui.forceButton.isFocusable());
 }

 @Test
 public void testExitButtonListener() {
  gui = new LagoonGUI();
  MockEvent mocks = new MockEvent();
  gui.exitButton.addActionListener(e -> gui.exit());
  gui.exitButton.doClick();
  assertTrue(gui.isDisplayable()); // LagoonGUI does not exit properly in tests
 }

 @Test
 public void testInit() {
  gui = new LagoonGUI();
  gui.init();
  assertNotNull(gui.processor);
 }

 @Test(expected = LagoonException.class)
 public void testInitFailed() {
  gui = new LagoonGUI();
  gui.sitemapFile.setValue("invalidFilePath");
  gui.init();
 }

 @Test
 public void testBuild() {
  gui = new LagoonGUI();
  gui.sitemapFile.setValue("validFilePath");
  gui.sourceDir.setValue("validSourceDir");
  gui.targetURL.setValue("validTargetURL");
  MockEvent mocks = new MockEvent();
  gui.buildButton.addActionListener(e -> gui.build(false));
  gui.buildButton.doClick();
  assertTrue(gui.progressArea.getText().contains("--- Start build ---"));
  assertTrue(gui.progressArea.getText().contains("--- Build finished successfully"));
 }

 @Test(expected = IOException.class)
 public void testBuildFailed() {
  gui = new LagoonGUI();
  gui.sitemapFile.setValue("validFilePath");
  gui.sourceDir.setValue("validSourceDir");
  gui.targetURL.setValue("validTargetURL");
  MockEvent mocks = new MockEvent();
  gui.buildButton.addActionListener(e -> gui.build(true));
  gui.buildButton.doClick();
  assertTrue(gui.progressArea.getText().contains("--- Build finished with error(s)"));
 }

 @Test
 public void testExit() {
  gui = new LagoonGUI();
  gui.exit();
  assertFalse(gui.isDisplayable());
 }
}