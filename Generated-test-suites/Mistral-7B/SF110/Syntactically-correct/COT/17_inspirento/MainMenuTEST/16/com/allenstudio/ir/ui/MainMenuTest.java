package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.KeyEvent;
import java.awt.KeyStroke;
import java.awt.event.InputEvent;
import java.io.File;
import java.net.URL;
import java.util.MissingResourceException;

import org.junit.Before;
import org.junit.Test;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.ui.MainMenuTest;
import com.allenstudio.ir.util.UIResources;
import com.allenstudio.ir.util.UIResourcesLoader;

public class MainMenuTest {

 private MainMenu menu;
 private InspirentoMediator mediator;
 private UIResourcesLoader resourcesLoader;

 @Before
 public void setUp() {
 try {
 resourcesLoader = new UIResourcesLoader();
 resourcesLoader.load();
 } catch (Exception e) {
 e.printStackTrace();
 }
 mediator = new InspirentoMediator();
 menu = new MainMenu(mediator);
 }

 @Test
 public void testMainMenuConstruction() {
 assertNotNull(menu);
 }

 @Test
 public void testCreateMenuMethod() {
 String menuLabel = "TestMenu";
 String expectedMenuLabel = "TestMenu";
 JMenu actualMenu = menu.createMenu(menuLabel);
 assertEquals(expectedMenuLabel, actualMenu.getText());
 }

 @Test(expected = MissingResourceException.class)
 public void testCreateMenuMethodWithMissingLabel() {
 String menuLabel = "MissingLabel";
 menu.createMenu(menuLabel);
 fail("MissingResourceException expected but not thrown");
 }

 @Test(expected = MissingResourceException.class)
 public void testCreateMenuMethodWithMissingLabelAndIcon() {
 String menuLabel = "MissingLabel";
 String iconKey = "MissingIcon";
 menu.createMenu(menuLabel);
 menu.addShortcutAndIcon(menu.getItem(0), iconKey);
 fail("MissingResourceException expected but not thrown");
 }

 @Test
 public void testAddShortcutAndIconMethodWithValidKeyAndIcon() {
 JMenuItem item = menu.getItem(0);
 String key = "ValidKey";
 String iconKey = "ValidIcon";
 menu.addShortcutAndIcon(item, key);
 assertNotNull(item.getAccelerator());
 assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), item.getAccelerator());
 assertNotNull(item.getIcon());
 File iconFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "ValidIcon.png");
 URL url = new URL("file:" + iconFile.getAbsolutePath());
 assertEquals(new ImageIcon(url), item.getIcon());
 }

 @Test(expected = MissingResourceException.class)
 public void testAddShortcutAndIconMethodWithMissingShortcutKey() {
 JMenuItem item = menu.getItem(0);
 String key = "MissingShortcutKey";
 menu.addShortcutAndIcon(item, key);
 fail("MissingResourceException expected but not thrown");
 }

 @Test(expected = MissingResourceException.class)
 public void testAddShortcutAndIconMethodWithMissingIconKey() {
 JMenuItem item = menu.getItem(0);
 String iconKey = "MissingIconKey";
 menu.addShortcutAndIcon(item, iconKey);
 fail("MissingResourceException expected but not thrown");
 }

 @Test
 public void testAddSeparator() {
 int numItemsBeforeSeparator = menu.getItemCount();
 menu.addSeparator();
 int numItemsAfterSeparator = menu.getItemCount();
 assertEquals(numItemsBeforeSeparator + 1, numItemsAfterSeparator);
 }

 @Test
 public void testMenuItemsAreEnabledByDefault() {
 JMenuItem enabledMenuItem = (JMenuItem) menu.getItem(0);
 assertTrue(enabledMenuItem.isEnabled());
 }

 @Test
 public void testMenuItemsCanBeDisabled() {
 JMenuItem disabledMenuItem = (JMenuItem) menu.getItem(1);
 disabledMenuItem.setEnabled(false);
 assertFalse(disabledMenuItem.isEnabled());
 }

 @Test
 public void testMenuItemsCanBeChecked() {
 JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) menu.getItem(2);
 checkboxMenuItem.setSelected(true);
 assertTrue(checkboxMenuItem.isSelected());
 }

 @Test
 public void testMenuItemsCanBeUnchecked() {
 JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) menu.getItem(2);
 checkboxMenuItem.setSelected(false);
 assertFalse(checkboxMenuItem.isSelected());
 }

 @Test
 public void testMenuItemsCanBeCheckedAndUncheckedMultipleTimes() {
 JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) menu.getItem(2);
 checkboxMenuItem.setSelected(true);
 assertTrue(checkboxMenuItem.isSelected());
 checkboxMenuItem.setSelected(false);
 assertFalse(checkboxMenuItem.isSelected());
 checkboxMenuItem.setSelected(true);
 assertTrue(checkboxMenuItem.isSelected());
 }

 @Test
 public void testMenuCanHandleNullMediator() {
 try {
 new MainMenu(null);
 fail("NullPointerException expected but not thrown");
 } catch (NullPointerException e) {
 // Expected
 }
}

 @Test
 public void testMenuCanHandleMissingResourceExceptionInConstructor() {
 try {
 String missingLabel = "MissingLabel";
 new MainMenu(mediator);
 fail("MissingResourceException expected but not thrown");
 } catch (MissingResourceException e) {
 // Expected
 }
}

 @Test
 public void testMenuCanHandleMissingResourceExceptionInCreateMenuMethod() {
 String missingMenuLabel = "MissingMenuLabel";
 try {
 menu.createMenu(missingMenuLabel);
 fail("MissingResourceException expected but not thrown");
 } catch (MissingResourceException e) {
 // Expected
 }
}

 @Test
 public void testMenuCanHandleMissingResourceExceptionInAddShortcutAndIconMethod() {
 String missingShortcutKey = "MissingShortcutKey";
 String missingIconKey = "MissingIconKey";
 JMenuItem item = menu.getItem(0);
 try {
 menu.addShortcutAndIcon(item, missingShortcutKey);
 fail("MissingResourceException expected but not thrown");
 } catch (MissingResourceException e) {
 // Expected
 }
 try {
 menu.addShortcutAndIcon(item, missingIconKey);
 fail("MissingResourceException expected but not thrown");
 } catch (MissingResourceException e) {
 // Expected
 }
}

}