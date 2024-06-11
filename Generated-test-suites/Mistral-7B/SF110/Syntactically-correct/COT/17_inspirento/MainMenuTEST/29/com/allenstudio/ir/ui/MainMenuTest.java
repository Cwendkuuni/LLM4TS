package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.net.URL;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuTest {

 private static final String TEST_RESOURCES_FOLDER = "test/resources/";
 private static final String UI_RESOURCES_PACKAGE = "com.allenstudio.ir.util.";
 private static final String MENU_KEY = "File";
 private static final String FILE_MENU_KEY = "FileMenu";
 private static final String NEW_ITEM_KEY = "#New";
 private static final String NEW_ITEM_LABEL = "New";
 private static final String NEW_ITEM_SHORTCUT = "Ctrl N";
 private static final String NEW_ITEM_ICON_PATH = "icon/new.png";
 private static final String OPEN_ITEM_KEY = "#Open";
 private static final String OPEN_ITEM_LABEL = "Open";
 private static final String OPEN_ITEM_SHORTCUT = "Ctrl O";
 private static final String OPEN_ITEM_ICON_PATH = "icon/open.png";

 @InjectMocks
 private MainMenu mainMenu;

 @Before
 public void setUp() {
 InspirentoMediator mediatorMock = Mockito.mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediatorMock);
 }

 @Test
 public void testMainMenuConstruction() throws Exception {
 // Basic functionality test
 assertNotNull(mainMenu);
 assertTrue(mainMenu instanceof JMenuBar);

 // Test menu creation
 String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
 assertEquals(menus.length, mainMenu.getMenuCount());

 String menuName = menus[0];
 JMenu menu = mainMenu.getMenu(0);
 assertEquals(menuName, menu.getName());
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() throws Exception {
 // Test exception handling for missing menu label
 String nonExistentMenu = "NonExistentMenu";
 mainMenu = new MainMenu(Mockito.mock(InspirentoMediator.class));
 mainMenu.createMenu(nonExistentMenu);
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuItemLabel() throws Exception {
 // Test exception handling for missing item label
 String nonExistentItemKey = "#MissingItem";
 mainMenu = new MainMenu(Mockito.mock(InspirentoMediator.class));
 mainMenu.createMenu(UIResources.getString("menu"));
 JMenuItem nonExistentMenuItem = mainMenu.getItem(nonExistentItemKey);
 assertNull(nonExistentMenuItem);
 }

 @Test
 public void testCreateFileMenu() throws Exception {
 // Test creating menu for the "File" menu
 String fileMenuName = "File";
 JMenu fileMenu = mainMenu.createMenu(fileMenuName);

 // Test menu name and mnemonic
 assertEquals(fileMenuName, fileMenu.getName());
 assertFalse(fileMenu.isRolloverEnabled());

 // Test "New" menu item
 String newItemLabel = "New";
 JMenuItem newItem = mainMenu.getItem(NEW_ITEM_KEY);
 assertEquals(newItemLabel, newItem.getText());
 assertEquals(KeyStroke.getKeyStroke(NEW_ITEM_SHORTCUT), newItem.getAccelerator());
 assertNull(newItem.getIcon());

 // Test "Open" menu item
 String openItemLabel = "Open";
 JMenuItem openItem = mainMenu.getItem(OPEN_ITEM_KEY);
 assertEquals(openItemLabel, openItem.getText());
 assertEquals(KeyStroke.getKeyStroke(OPEN_ITEM_SHORTCUT), openItem.getAccelerator());
 assertNotNull(openItem.getIcon());

 // Test "New" and "Open" submenus
 assertEquals(2, fileMenu.getMenuCount());
 JMenu newSubmenu = (JMenu) fileMenu.getComponent(0);
 assertEquals("File > New", newSubmenu.getName());
 JMenu openSubmenu = (JMenu) fileMenu.getComponent(1);
 assertEquals("File > Open", openSubmenu.getName());
 }

 @Test
 public void testItemEvent() throws Exception {
 // Test item event handling
 ItemEvent event = new ItemEvent(Mockito.mock(JMenuItem.class), ItemEvent.SELECTED, 0, 0);
 mainMenu.changed();
 mainMenu.getItem(NEW_ITEM_KEY).doClick();
 mainMenu.changed();
 mainMenu.getItem(OPEN_ITEM_KEY).doClick();
 mainMenu.changed();

 // Verify that the event is handled correctly
 Mockito.verify(Mockito.mock(InspirentoMediator.class), Mockito.times(2)).handleEvent(Mockito.eq(EventType.NEW_FILE), Mockito.any());
 Mockito.verify(Mockito.mock(InspirentoMediator.class), Mockito.times(1)).handleEvent(Mockito.eq(EventType.OPEN_FILE), Mockito.any());
 }

 @Test
 public void testGetClassResource() throws Exception {
 // Test getting class resource
 URL newIconUrl = mainMenu.getClass().getResource(NEW_ITEM_ICON_PATH);
 File newIconFile = new File(newIconUrl.getPath());
 assertTrue(newIconFile.exists());
 }

 @Test
 public void testMissingIconResource() throws Exception {
 // Test missing icon file
 String missingIconPath = "non_existent_icon.png";
 JMenuItem missingIconMenuItem = new JMenuItem("Missing Icon");
 mainMenu.addShortcutAndIcon(missingIconMenuItem, MENU_KEY + "MissingIcon");
 assertNull(missingIconMenuItem.getIcon());
 }
}