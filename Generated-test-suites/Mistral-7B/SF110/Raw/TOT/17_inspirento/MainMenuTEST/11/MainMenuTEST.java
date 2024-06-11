package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.util.UIResources;
import com.allenstudio.ir.util.InspirentoUtilities;

public class MainMenuTest {

 @Mock
 private UIResources uiResources;
 @InjectMocks
 private MainMenu mainMenu;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testMainMenuConstructor() {
 String[] menus = {"File", "Edit", "View"};
 InspirentoMediator mediator = mock(InspirentoMediator.class);
 when(uiResources.getString("menu")).thenReturn(String.join(";", menus));
 MainMenu mainMenu = new MainMenu(mediator);
 assertEquals(3, mainMenu.getMenuCount());
 for (int i = 0; i < 3; i++) {
 JMenu menu = mainMenu.getComponent(i);
 assertEquals(menus[i], menu.getName());
 }
 }

 @Test
 public void testCreateMenu() {
 String menuName = "File";
 String[] items = {"New", "Open", "Save", "Save As", "Exit"};
 String[] subMenus = {"Save As", "Export"};
 InspirentoMediator mediator = mock(InspirentoMediator.class);
 when(uiResources.getString(menuName)).thenReturn(menuName + "*");
 when(uiResources.getString(menuName + "Label")).thenReturn(menuName);
 when(uiResources.getString(menuName + "Shortcut")).thenReturn("");
 when(uiResources.getString("New" + "Label")).thenReturn("New");
 when(uiResources.getString("New" + "Shortcut")).thenReturn("Ctrl+N");
 when(uiResources.getString("Open" + "Label")).thenReturn("Open");
 when(uiResources.getString("Open" + "Shortcut")).thenReturn("Ctrl+O");
 when(uiResources.getString("Save" + "Label")).thenReturn("Save");
 when(uiResources.getString("Save" + "Shortcut")).thenReturn("Ctrl+S");
 when(uiResources.getString("Save As" + "Label")).thenReturn("Save As");
 when(uiResources.getString("Save As" + "Shortcut")).thenReturn("F12");
 when(uiResources.getString("Exit" + "Label")).thenReturn("Exit");
 when(uiResources.getString("Export" + "Label")).thenReturn("Export");
 when(uiResources.getString("Export" + "Shortcut")).thenReturn("Ctrl+E");
 when(uiResources.getString("Export" + "Icon")).thenReturn("export.png");
 when(uiResources.getString("Export" + "Icon")).thenReturn(new File("export.png").toURI().toURL());

 JMenu fileMenu = mainMenu.createMenu(menuName);
 assertEquals(menuName, fileMenu.getName());
 assertEquals(items.length + 1, fileMenu.getMenuComponentCount()); // 1 for separator
 for (int i = 0; i < items.length; i++) {
 JMenuItem item = fileMenu.getMenuComponent(i + 1);
 assertEquals(items[i], item.getName());
 if (!items[i].equals("Export")) {
 assertEquals(Item.NO_ICON, item.getIcon());
 assertNull(item.getAcceleratorKeyStroke());
 } else {
 File exportIconFile = new File("export.png");
 assertEquals(new ImageIcon(exportIconFile.toURI().toURL()), item.getIcon());
 assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_F12), item.getAccelerator());
 }
 }
 }

 @Test
 public void testAddShortcutAndIcon() {
 JMenuItem jMenuItem = mock(JMenuItem.class);
 String key = "New";
 when(uiResources.getString(key)).thenReturn(key + "*");
 when(uiResources.getString(key + "Label")).thenReturn("New");
 when(uiResources.getString(key + "Shortcut")).thenReturn("Ctrl+N");
 mainMenu.addShortcutAndIcon(jMenuItem, key);
 assertEquals("New", jMenuItem.getName());
 assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), jMenuItem.getAccelerator());
 assertNull(jMenuItem.getIcon());

 when(uiResources.getString(key + "Icon")).thenReturn("new.png");
 when(uiResources.getString(key + "Icon")).thenReturn(new File("new.png").toURI().toURL());
 mainMenu.addShortcutAndIcon(jMenuItem, key);
 assertEquals("New", jMenuItem.getName());
 assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), jMenuItem.getAccelerator());
 assertEquals(new ImageIcon(new File("new.png").toURI().toURL()), jMenuItem.getIcon());
 }
}