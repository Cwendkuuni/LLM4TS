import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import com.allenstudio.ir.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import org.junit.*;
import org.mockito.*;

public class MainMenuTest {

 private static final String LABEL_SUFFIX = "Label";
 private static final String ICON_SUFFIX = "Icon";
 private static final String SHORTCUT_SUFFIX = "Shortcut";

 @Mock
 private InspirentoMediator mediator;

 @Test
 public void MainMenuTestCase_01_Constructor() {
 MainMenu menu = new MainMenu(mediator);
 assertNotNull(menu);
 verify(mediator).setMenuBar(menu);
 }

 @Test
 public void MainMenuTestCase_02_CreateMenu_ValidInput() {
 String menuLabel = "TestMenu";
 String[] menuItems = {"Item1", "#Item2>SubMenu", "Item3"};
 MainMenu menu = new MainMenu(mediator);
 JMenu testMenu = menu.createMenu(menuLabel);
 assertNotNull(testMenu);
 assertEquals(menuLabel, testMenu.getText());
 assertFalse(testMenu.isVisible());
 for (String menuItem : menuItems) {
 if (menuItem.startsWith("#")) {
 JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) testMenu.getItem(0);
 assertEquals(menuItem.substring(1), checkboxMenuItem.getText());
 } else {
 JMenuItem menuItemButton = (JMenuItem) testMenu.getItem(0);
 assertEquals(menuItem, menuItemButton.getText());
 }
 testMenu.setVisible(true);
 }
 }

 @Test(expected = MissingResourceException.class)
 public void MainMenuTestCase_03_CreateMenu_EmptyMenu() {
 MainMenu menu = new MainMenu(mediator);
 menu.createMenu("");
 }

 @Test(expected = MissingResourceException.class)
 public void MainMenuTestCase_04_CreateMenu_InvalidMenu() {
 MainMenu menu = new MainMenu(mediator);
 menu.createMenu("InvalidMenu");
 }

 @Test
 public void MainMenuTestCase_05_AddShortcutAndIcon_ValidKey() {
 MainMenu menu = new MainMenu(mediator);
 JMenuItem menuItem = mock(JMenuItem.class);
 String key = "TestKey";
 menu.addShortcutAndIcon(menuItem, key);
 assertEquals(key, menuItem.getAccelerator().toString());
 }

 @Test
 public void MainMenuTestCase_06_AddShortcutAndIcon_EmptyKey() {
 MainMenu menu = new MainMenu(mediator);
 JMenuItem menuItem = mock(JMenuItem.class);
 menu.addShortcutAndIcon(menuItem, "");
 assertNull(menuItem.getAccelerator());
 }

 @Test
 public void MainMenuTestCase_07_AddShortcutAndIcon_MissingKey() {
 MainMenu menu = new MainMenu(mediator);
 JMenuItem menuItem = mock(JMenuItem.class);
 menu.addShortcutAndIcon(menuItem, "MissingKey");
 assertNull(menuItem.getAccelerator());
 }

 @After
 public void tearDown() {
 MockitoAnnotations.initMocks(this);
 }

}