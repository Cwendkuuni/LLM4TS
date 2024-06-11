package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import java.awt.Image;
import java.awt.KeyEvent;
import java.awt.event.KeyEventConstants;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import org.junit.Test;

public class MainMenuTest {

 @Test
 public void testCreateMenu() {
 String[] menus = {"File", "Edit", "Help"};
 MainMenu menu = new MainMenu(null);
 JMenu fileMenu = menu.createMenu("File");
 assertNotNull(fileMenu);
 assertEquals("File", fileMenu.getText());

 for (String menuItem : menus) {
 JMenu subMenu = null;
 if (menuItem.length() > 1 && menuItem.charAt(menuItem.length() - 1) == '>') {
 subMenu = menu.createMenu(menuItem.substring(0, menuItem.length() - 1));
 }
 assertNotNull(subMenu);
 }
 }

 @Test
 public void testAddShortcutAndIcon() throws Exception {
 MainMenu menu = new MainMenu(null);
 JMenuItem item = new JMenuItem("Test");
 String key = "Test";
 menu.addShortcutAndIcon(item, key);

 assertEquals("Test", item.getText());
 assertNull(item.getIcon());

 String shortcut = "Ctrl+T";
 KeyStroke keyStroke = KeyStroke.getKeyStroke(shortcut);
 assertEquals(keyStroke, item.getAccelerator());

 String iconPath = "test.png";
 File file = new File(iconPath);
 URL url = new File(".").toURI().toURL();
 assertEquals(iconPath, url.toExternalForm());
 URL resource = menu.getClass().getResource(iconPath);
 Image expectedIcon = ImageIO.read(file);
 BufferedImage actualIcon = (BufferedImage) item.getIcon().getImage();
 assertEquals(expectedIcon.getWidth(), actualIcon.getWidth());
 assertEquals(expectedIcon.getHeight(), actualIcon.getHeight());
 assertEquals(expectedIcon.getType(), actualIcon.getType());
 }

 @Test
 public void testCreateCheckboxMenu() throws Exception {
 MainMenu menu = new MainMenu(null);
 JCheckBoxMenuItem item = new JCheckBoxMenuItem("Test");
 String key = "Test";
 menu.addShortcutAndIcon(item, key);

 assertEquals("Test", item.getText());
 assertNull(item.getIcon());

 String shortcut = "Ctrl+T";
 KeyStroke keyStroke = KeyStroke.getKeyStroke(shortcut);
 assertEquals(keyStroke, item.getAccelerator());

 String iconPath = "test.png";
 File file = new File(iconPath);
 URL url = new File(".").toURI().toURL();
 assertEquals(iconPath, url.toExternalForm());
 URL resource = menu.getClass().getResource(iconPath);
 Image expectedIcon = ImageIO.read(file);
 BufferedImage actualIcon = (BufferedImage) item.getIcon().getImage();
 assertEquals(expectedIcon.getWidth(), actualIcon.getWidth());
 assertEquals(expectedIcon.getHeight(), actualIcon.getHeight());
 assertEquals(expectedIcon.getType(), actualIcon.getType());

 assertTrue(item.isArmed());
 assertFalse(item.isSelected());
 }

 @Test
 public void testCreateSeparator() {
 MainMenu menu = new MainMenu(null);
 JMenu separator = menu.createMenu("Separator");
 assertNull(separator.getText());
 assertTrue(separator.isSeparator());
 }

 @Test
 public void testCreateMenuItemWithNoShortcutOrIcon() throws Exception {
 MainMenu menu = new MainMenu(null);
 JMenuItem item = new JMenuItem("Test");
 String key = "Test";
 menu.addShortcutAndIcon(item, key);
 item = new JMenuItem("Test2");
 menu.createMenu("TestMenu").add(item);

 assertEquals("Test", item.getText());
 assertNull(item.getIcon());

 assertNull(item.getAccelerator());
 }

 @Test
 public void testCreateMenuItemWithShortcutOnly() throws Exception {
 MainMenu menu = new MainMenu(null);
 JMenuItem item = new JMenuItem("Test");
 String key = "Test";
 menu.addShortcutAndIcon(item, key);
 item = new JMenuItem("Test2");
 menu.createMenu("TestMenu").add(item);

 item.setAccelerator(KeyStroke.getKeyStroke("Ctrl+T"));
 assertEquals("Test2", item.getText());
 assertNull(item.getIcon());

 assertEquals(KeyEvent.VK_T, item.getAccelerator().getKeyCode());
 assertEquals(KeyEventConstants.CTRL_MASK, item.getAccelerator().getModifiers());
 }

 @Test
 public void testCreateMenuItemWithIconOnly() throws Exception {
 MainMenu menu = new MainMenu(null);
 JMenuItem item = new JMenuItem("Test");
 String key = "Test";
 menu.addShortcutAndIcon(item, key);
 item = new JMenuItem("Test2");
 menu.createMenu("TestMenu").add(item);

 String iconPath = "test.png";
 File file = new File(iconPath);
 URL url = new File(".").toURI().toURL();
 assertEquals(iconPath, url.toExternalForm());
 URL resource = menu.getClass().getResource(iconPath);
 Image expectedIcon = ImageIO.read(file);
 BufferedImage actualIcon = (BufferedImage) item.getIcon().getImage();
 assertEquals(expectedIcon.getWidth(), actualIcon.getWidth());
 assertEquals(expectedIcon.getHeight(), actualIcon.getHeight());
 assertEquals(expectedIcon.getType(), actualIcon.getType());

 assertNull(item.getAccelerator());
 }

 @Test
 public void testCreateMenuItemWithShortcutAndIcon() throws Exception {
 MainMenu menu = new MainMenu(null);
 JMenuItem item = new JMenuItem("Test");
 String key = "Test";
 menu.addShortcutAndIcon(item, key);
 item = new JMenuItem("Test2");
 menu.createMenu("TestMenu").add(item);

 item.setAccelerator(KeyStroke.getKeyStroke("Ctrl+T"));
 assertEquals("Test2", item.getText());
 assertNotNull(item.getIcon());

 assertEquals(KeyEvent.VK_T, item.getAccelerator().getKeyCode());
 assertEquals(KeyEventConstants.CTRL_MASK, item.getAccelerator().getModifiers());
 }
}