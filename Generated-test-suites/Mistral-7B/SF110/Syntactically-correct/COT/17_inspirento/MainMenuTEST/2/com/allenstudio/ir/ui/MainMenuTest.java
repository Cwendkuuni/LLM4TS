package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.util.UIResources;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import org.junit.*;

public class MainMenuTest {

 private static final String TEST_RESOURCE_DIR = "/test/resources/";
 private static final String UI_RESOURCES_PACKAGE = "com.allenstudio.ir.util.ui.";

 @BeforeClass
 public static void setUpClass() {
 UIManager.put("Menu.background", Color.WHITE); // For better test visibility
 }

 @AfterClass
 public static void tearDownClass() {
 // Restore default look and feel
 UIManager.put("Menu.background", null);
 }

 @Before
 public void setUp() {
 // Initialize test resources
 UIResources.setResourceBase(TEST_RESOURCE_DIR);
 }

 @After
 public void tearDown() {
 // Reset test resources
 UIResources.setResourceBase(UIResources.class.getPackage().getName());
 }

 @Test
 public void testMainMenuConstruction() {
 // Test constructor
 final String mediatorClassName = "test.Mediator";
 final Class<?> mediatorClass = Class.forName(mediatorClassName);
 final InspirentoMediator mediator = (InspirentoMediator) ReflectionTestUtils.newInstance(mediatorClass);
 final MainMenu menu = new MainMenu(mediator);
 assertNotNull(menu);
 }

 @Test
 public void testCreateMenu() {
 // Test createMenu method
 final String menuName = "TestMenu";
 final MainMenu menu = new MainMenu(null);
 final JMenu testMenu = menu.createMenu(menuName);
 assertNotNull(testMenu);
 }

 @Test
 public void testCreateMenuWithLabelAndShortcut() {
 // Test createMenu method with label and shortcut
 final String menuName = "TestMenu";
 final String label = "TestLabel";
 final String shortcut = "TestShortcut";
 final MainMenu menu = new MainMenu(null);
 final JMenuItem testMenuItem = (JMenuItem) menu.createMenu(menuName + "MenuItem")
 .add(menuName + "Submenu")
 .add(new JLabel(label))
 .addSeparator();
 assertEquals(label, testMenuItem.getText());
 assertTrue(testMenuItem.isEnabled());
 final KeyStroke testShortcut = KeyStroke.getKeyStroke(shortcut);
 assertEquals(testShortcut, testMenuItem.getAccelerator());
 }

 @Test
 public void testCreateMenuWithLabelAndIcon() {
 // Test createMenu method with label and icon
 final String menuName = "TestMenu";
 final String label = "TestLabel";
 final String iconPath = TEST_RESOURCE_DIR + "testicon.png";
 final MainMenu menu = new MainMenu(null);
 final JMenuItem testMenuItem = (JMenuItem) menu.createMenu(menuName + "MenuItem")
 .add(menuName + "Submenu")
 .add(new JLabel(label))
 .addSeparator();
 assertNull(testMenuItem.getIcon());
 final URL iconUrl = new File(iconPath).toURI().toURL();
 menu.addShortcutAndIcon(testMenuItem, menuName + "MenuItem");
 assertEquals(iconUrl, testMenuItem.getIcon().getImage().getSource());
 }

 @Test
 public void testCreateMenuWithLabelAndCheckbox() {
 // Test createMenu method with label and checkbox
 final String menuName = "TestMenu";
 final String label = "TestLabel";
 final String checkboxKey = "TestCheckbox";
 final MainMenu menu = new MainMenu(null);
 final JCheckBoxMenuItem testCheckboxMenuItem = (JCheckBoxMenuItem) menu.createMenu(menuName + "MenuItem")
 .add(menuName + "Submenu")
 .add(new JLabel(label))
 .addSeparator();
 assertFalse(testCheckboxMenuItem.isSelected());
 assertEquals(label, testCheckboxMenuItem.getText());
 menu.addShortcutAndIcon(testCheckboxMenuItem, checkboxKey);
 }

 @Test(expected = MissingResourceException.class)
 public void testCreateMenuWithMissingLabel() {
 // Test createMenu method with missing label
 final String menuName = "TestMenu";
 final MainMenu menu = new MainMenu(null);
 menu.createMenu(menuName + "MissingLabelMenuItem");
 fail("Exception expected: MissingResourceException");
 }

 @Test
 public void testAddShortcutAndIcon() {
 // Test addShortcutAndIcon method
 final String key = "TestKey";
 final MainMenu menu = new MainMenu(null);
 final JMenuItem testMenuItem = new JMenuItem(key);
 final String label = "TestLabel";
 final String iconPath = TEST_RESOURCE_DIR + "testicon.png";
 menu.addShortcutAndIcon(testMenuItem, key);
 assertNull(testMenuItem.getIcon());
 final URL iconUrl = new File(iconPath).toURI().toURL();
 assertEquals(iconUrl, testMenuItem.getIcon().getImage().getSource());
 // Test addShortcutAndIcon method with checkbox
 final String checkboxKey = "TestCheckbox";
 final JCheckBoxMenuItem testCheckboxMenuItem = new JCheckBoxMenuItem(checkboxKey);
 menu.addShortcutAndIcon(testCheckboxMenuItem, checkboxKey);
 assertNull(testCheckboxMenuItem.getIcon());
 }

 @Test
 public void testAddShortcutAndIconWithMissingIcon() {
 // Test addShortcutAndIcon method with missing icon
 final String key = "TestKey";
 final MainMenu menu = new MainMenu(null);
 final JMenuItem testMenuItem = new JMenuItem(key);
 final String label = "TestLabel";
 final String iconPath = TEST_RESOURCE_DIR + "missingicon.png";
 menu.addShortcutAndIcon(testMenuItem, key);
 assertNull(testMenuItem.getIcon());
 }
}