package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.KeyEventDispatcher.KeyEventDispatcherEvent;
import java.awt.KeyEventDispatcher.KeyEventDispatcherListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Keybindings;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {

 private JFrame frame;
 private MainMenu mainMenu;
 private InspirentoMediator mediator = new MockInspirentoMediator();

 @Before
 public void setUp() {
 frame = new JFrame();
 mainMenu = new MainMenu(mediator);
 frame.setJMenuBar(mainMenu);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(() -> {
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 });
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testMainMenuConstruction() {
 MainMenu menu = new MainMenu(mediator);
 assertNotNull(menu);
 }

 @Test
 public void testCreateMenu() {
 String[] menus = {"File", "Edit", "View", "Help"};
 for (String menu : menus) {
 JMenu jMenu = mainMenu.createMenu(menu);
 assertNotNull(jMenu);
 assertEquals(menu, jMenu.getText());
 }
 }

 @Test
 public void testCreateSubMenu() {
 String[] menu = {"File", "Open"};
 String[] subMenu = {"Open", "From Local", "From Network"};
 JMenu jMenu = mainMenu.createMenu(menu[0]);
 JMenu subMenuJMenu = (JMenu) jMenu.getComponent(0);
 assertEquals(menu[0], subMenuJMenu.getText());
 for (String item : subMenu) {
 JMenuItem jMenuItem = (JMenuItem) subMenuJMenu.getComponent(Arrays.asList(subMenu).indexOf(item));
 assertEquals(item, jMenuItem.getText());
 }
 }

 @Test
 public void testCreateCheckBoxMenuItem() {
 String[] menu = {"File", "Save As"};
 String[] checkBoxMenuItem = {"Save As", "Save As Template"};
 JMenu jMenu = mainMenu.createMenu(menu[0]);
 JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) jMenu.getComponent(1);
 assertEquals(menu[0], jCheckBoxMenuItem.getText());
 assertEquals("Save As Template", jCheckBoxMenuItem.getActionCommand());
 }

 @Test
 public void testCreateMenuItem() {
 String[] menu = {"File", "Exit"};
 String[] item = {"Exit"};
 JMenu jMenu = mainMenu.createMenu(menu[0]);
 JMenuItem jMenuItem = (JMenuItem) jMenu.getComponent(1);
 assertEquals(menu[0], jMenu.getText());
 assertEquals(item[0], jMenuItem.getText());
 }

 @Test
 public void testCreateMenuItemWithSeparator() {
 String[] menu = {"File", "Exit", "-", "New"};
 JMenu jMenu = mainMenu.createMenu(menu[0]);
 int size = jMenu.getComponentCount();
 assertEquals(menu.length, size);
 JMenuItem jMenuItem1 = (JMenuItem) jMenu.getComponent(1);
 JMenuItem jMenuItem2 = (JMenuItem) jMenu.getComponent(size - 1);
 assertEquals("Exit", jMenuItem1.getText());
 assertEquals("New", jMenuItem2.getText());
 }

 @Test
 public void testAddShortcutAndIconToMenuItem() {
 String[] menu = {"File", "Open"};
 String key = "Open";
 JMenu jMenu = mainMenu.createMenu(menu[0]);
 JMenuItem jMenuItem = (JMenuItem) jMenu.getComponent(0);
 String shortcut = "Ctrl O";
 char shortcutKey = shortcut.charAt(0);
 assertEquals("Open", jMenuItem.getText());
 assertNull(jMenuItem.getIcon());
 assertNull(jMenuItem.getAcceleratorKeyEvent(shortcutKey));
 mainMenu.addShortcutAndIcon(jMenuItem, key);
 Image icon = new ImageIcon(MainMenu.class.getResource(UIResources.getString(key + "Icon"))).getImage();
 assertEquals("Open", jMenuItem.getText());
 assertEquals(icon, jMenuItem.getIcon());
 assertEquals(KeyEvent.VK_O, jMenuItem.getAcceleratorKeyEvent(shortcutKey).getKeyChar());
 }

 @Test
 public void testAddShortcutAndIconToCheckBoxMenuItem() {
 String[] menu = {"File", "Save As"};
 String key = "Save As";
 JMenu jMenu = mainMenu.createMenu(menu[0]);
 JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) jMenu.getComponent(1);
 String shortcut = "Ctrl S";
 char shortcutKey = shortcut.charAt(0);
 assertEquals("Save As", jCheckBoxMenuItem.getText());
 assertNull(jCheckBoxMenuItem.getIcon());
 assertNull(jCheckBoxMenuItem.getAcceleratorKeyEvent(shortcutKey));
 mainMenu.addShortcutAndIcon(jCheckBoxMenuItem, key);
 Image icon = new ImageIcon(MainMenu.class.getResource(UIResources.getString(key + "Icon"))).getImage();
 assertEquals("Save As", jCheckBoxMenuItem.getText());
 assertEquals(icon, jCheckBoxMenuItem.getIcon());
 assertEquals(KeyEvent.VK_S, jCheckBoxMenuItem.getAcceleratorKeyEvent(shortcutKey).getKeyChar());
 }

 @Test
 public void testSetMnemonic() {
 String[] menu = {"File", "Open", "Save As"};
 JMenu jMenu = mainMenu.createMenu(menu[0]);
 JMenuItem jMenuItem1 = (JMenuItem) jMenu.getComponent(0);
 JMenuItem jMenuItem2 = (JCheckBoxMenuItem) jMenu.getComponent(1);
 assertEquals(0, jMenuItem1.getMnemonic());
 assertEquals(KeyEvent.VK_S, jMenuItem2.getMnemonic());
 }

 @Test
 public void testSetLabel() {
 String[] menu = {"File", "Open"};
 String label = "Open File";
 JMenu jMenu = mainMenu.createMenu(menu[0]);
 JMenuItem jMenuItem = (JMenuItem) jMenu.getComponent(0);
 assertEquals("Open", jMenuItem.getText());
 jMenu.setLabel(label);
 assertEquals(label, jMenu.getLabel());
 assertEquals(label.substring(0, label.length() - 3), jMenuItem.getText());
 assertEquals(label.charAt(label.length() - 1), jMenuItem.getMnemonic());
 }

 @Test
 public void testInvokeMenuAction() {
 String[] menu = {"File", "Exit"};
 JMenu jMenu = mainMenu.createMenu(menu[0]);
 JMenuItem jMenuItem = (JMenuItem) jMenu.getComponent(1);
 ActionEvent actionEvent = new ActionEvent(jMenuItem, 0, jMenuItem.getActionCommand());
 mediator.onMenuAction(actionEvent);
 assertTrue(frame.isVisible() == false);
 }

 @Test
 public void testInvokeCheckBoxMenuItemAction() {
 String[] menu = {"File", "Save As", "Save As Template"};
 JMenu jMenu = mainMenu.createMenu(menu[0]);
 JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) jMenu.getComponent(1);
 ActionEvent actionEvent = new ActionEvent(jCheckBoxMenuItem, 0, jCheckBoxMenuItem.getActionCommand());
 mediator.onMenuAction(actionEvent);
 }

 @Test
 public void testKeyBindings() {
 final String[] keys = {"Control O", "Control S", "Control X"};
 final String[] menus = {"File", "File", "File"};
 for (int i = 0; i < keys.length; i++) {
 String key = keys[i];
 String menu = menus[i];
 JMenu jMenu = mainMenu.createMenu(menu);
 JMenuItem jMenuItem = (JMenuItem) jMenu.getComponent(0);
 mainMenu.addShortcutAndIcon(jMenuItem, menu);
 Keybindings keybindings = jMenuItem.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
 assertTrue(keybindings.size() > 0);
 assertEquals(KeyEvent.VK_O, keybindings.get(new KeyStroke(KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_O)).getKeyCode());
 assertEquals(keys[i], keybindings.get(new KeyStroke(KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_O)).toString());
 }
 }

 @Test
 public void testKeyEventDispatcherListener() {
 final int initialSize = frame.getComponentCount();
 JFrame frame = this.frame;
 JMenuBar menuBar = (JMenuBar) frame.getJMenuBar();
 menuBar.addKeyEventDispatcherListener(new KeyEventDispatcherListener() {
 @Override
 public void keyPressed(KeyEventDispatcherEvent e) {
 if (e.getID() == KeyEventDispatcherEvent.KEY_PRESSED) {
 JMenuItem clickedMenuItem = null;
 SwingUtilities.invokeLater(() -> {
 for (int i = 0; i < menuBar.getComponentCount(); i++) {
 JComponent component = menuBar.getComponent(i);
 if (component instanceof JMenuItem) {
 JMenuItem jMenuItem = (JMenuItem) component;
 if (jMenuItem.getBounds().contains(e.getPoint())) {
 clickedMenuItem = jMenuItem;
 break;
 }
 }
 }
 });
 assertEquals("Save As", clickedMenuItem.getText());
 assertEquals(KeyEvent.VK_S, e.getKeyCode());
 assertEquals(KeyEvent.CTRL_DOWN_MASK, e.getModifiers());
 }
 }

 @Override
 public boolean postKeyEvent(KeyEvent e) {
 return false;
 }
 });
 SwingUtilities.invokeLater(() -> {
 try {
 Thread.sleep(100);
 } catch (InterruptedException e1) {
 e1.printStackTrace();
 }
 JMenuItem menuItem = (JMenuItem) menuBar.getComponent(0);
 menuItem.doClick();
 Robot robot = new Robot();
 robot.keyPress(KeyEvent.VK_S);
 robot.keyPress(KeyEvent.VK_CONTROL);
 robot.keyRelease(KeyEvent.VK_S);
 robot.keyRelease(KeyEvent.VK_CONTROL);
 });
 }

 class MockInspirentoMediator implements InspirentoMediator {
 @Override
 public void onMenuAction(ActionEvent e) {
 }
 }
}