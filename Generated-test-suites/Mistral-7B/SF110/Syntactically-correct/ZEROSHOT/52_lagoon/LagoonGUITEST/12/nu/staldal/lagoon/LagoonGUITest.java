package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.xtree.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;
 @Before
 public void setUp() {
 lagoonGUI = new LagoonGUI();
 lagoonGUI.addWindowListener(new WindowAdapter() {
 @Override
 public void windowClosed(WindowEvent e) {
 lagoonGUI = null;
 }
 });
 lagoonGUI.show();
 }

 @After
 public void tearDown() {
 if (lagoonGUI != null) {
 lagoonGUI.exit();
 }
 }

 @Test
 public void testConstructor() {
 assertEquals("Lagoon", lagoonGUI.getTitle());
 assertTrue(lagoonGUI.isFocusable());
 }

 @Test
 public void testLoadProperties() {
 File file = new File("testProperties.properties");
 lagoonGUI.loadProperties(file);
 assertEquals("testProperties.properties", lagoonGUI.sitemapFile.getValue());
 assertEquals(".", lagoonGUI.sourceDir.getValue());
 assertEquals(".", lagoonGUI.targetURL.getValue());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadPropertiesWithInvalidFile() {
 lagoonGUI.loadProperties(new File("nonExistentFile.properties"));
 }

 @Test
 public void testSaveProperties() {
 File file = new File("testProperties.properties");
 lagoonGUI.savePropertyFile();
 assertTrue(file.exists());
 }

 @Test
 public void testBuild() {
 assertEquals("Building...", lagoonGUI.statusLabel.getText());
 lagoonGUI.build(false);
 assertEquals("Ready", lagoonGUI.statusLabel.getText());
 }

 @Test
 public void testBuildWithForce() {
 assertEquals("Building...", lagoonGUI.statusLabel.getText());
 lagoonGUI.build(true);
 assertEquals("Ready", lagoonGUI.statusLabel.getText());
 }

 @Test
 public void testInit() {
 assertNull(lagoonGUI.processor);
 assertEquals("Not initialized", lagoonGUI.statusLabel.getText());
 lagoonGUI.init();
 assertNotNull(lagoonGUI.processor);
 assertEquals("Initializing...", lagoonGUI.statusLabel.getText());
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInitWithAuthenticationMissingException() {
 lagoonGUI.init();
 final PasswordDialog pwd = new PasswordDialog(lagoonGUI, "Password", "Password needed to access target");
 pwd.show();
 lagoonGUI.password = pwd.getPassword();
 lagoonGUI.init();
 }

 @Test(expected = AuthenticationException.class)
 public void testInitWithAuthenticationException() {
 lagoonGUI.init();
 final PasswordDialog pwd = new PasswordDialog(lagoonGUI, "Password", "Invalid password, try again");
 pwd.show();
 lagoonGUI.password = pwd.getPassword();
 lagoonGUI.init();
 }

 @Test(expected = FileNotFoundException.class)
 public void testInitWithFileNotFoundException() {
 lagoonGUI.init();
 }

 @Test(expected = IOException.class)
 public void testInitWithIOException() {
 lagoonGUI.init();
 }

 @Test(expected = LagoonException.class)
 public void testInitWithLagoonException() {
 lagoonGUI.init();
 }

 @Test
 public void testExit() {
 assertEquals("Exiting...", lagoonGUI.statusLabel.getText());
 lagoonGUI.exit();
 assertEquals("Not initialized", lagoonGUI.statusLabel.getText());
 }
}