package com.allenstudio.ir.ui;
 import static org.junit.Assert.*;
 import com.allenstudio.ir.core.*;
 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.mockito.Mockito;
 import org.mockito.runners.MockitoJUnitRunner;

 import javax.swing.*;
 import javax.swing.event.ChangeEvent;
 import java.awt.*;
 import java.awt.event.KeyEvent;
 import java.io.IOException;
 import java.net.URL;
 import java.util.Arrays;
 import java.util.List;

 @RunWith(MockitoJUnitRunner.class)
 public class MainMenuTest {

 private MainMenu mainMenu;
 private InspirentoMediator mediatorMock;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 mediatorMock = Mockito.mock(InspirentoMediator.class);
 mainMenu = new MainMenu(mediatorMock);
 }

 @Test
 public void testMainMenuCreation() {
 assertTrue(mainMenu instanceof JMenuBar);
 assertNotNull(mainMenu.mediator);
 }

 @Test
 public void testCreateMenu() {
 String menuLabel = "File";
 String[] menuItems = {"Open", "#OpenFile>", "-", "Save", "#SaveFile", "-", "Exit"};
 JMenu expectedMenu = new JMenu(menuLabel);

 JMenu actualMenu = mainMenu.createMenu(menuLabel);
 assertEquals(expectedMenu, actualMenu);

 List<Component> actualMenuComponents = new ArrayList<>();
 for (Component component : actualMenu.getComponents()) {
 actualMenuComponents.add(component);
 }

 List<Component> expectedMenuComponents = Arrays.asList(
 new JMenuItem("Open"),
 new JCheckBoxMenuItem("Open File"),
 new JSeparator(),
 new JMenuItem("Save"),
 new JCheckBoxMenuItem("Save File"),
 new JSeparator(),
 new JMenuItem("Exit")
 );

 assertEquals(expectedMenuComponents, actualMenuComponents);
 }

 @Test
 public void testCreateMenuWithEmptyMenuItem() {
 String menuLabel = "Edit";
 String[] menuItems = {"", "-"};
 JMenu expectedMenu = new JMenu(menuLabel);
 expectedMenu.addSeparator();

 JMenu actualMenu = mainMenu.createMenu(menuLabel);
 assertEquals(expectedMenu, actualMenu);
 }

 @Test
 public void testAddShortcutAndIcon() throws IOException {
 String menuLabel = "View";
 String key = "ShowToolBar";
 String shortcut = "Ctrl+T";
 String iconPath = "path/to/icon.png";

 JMenuItem jMenuItem = mainMenu.createMenuItem(menuLabel, key);
 assertNull(jMenuItem.getAccelerator());
 assertNull(jMenuItem.getIcon());

 mainMenu.addShortcutAndIcon(jMenuItem, key);
 assertEquals(KeyStroke.getKeyStroke(shortcut), jMenuItem.getAccelerator());
 assertTrue(jMenuItem.getIcon() instanceof ImageIcon);
 URL url = new URL(iconPath);
 assertTrue(jMenuItem.getIcon().getIconData() instanceof ImageIcon.ImageIconData);
 assertEquals(url.openStream(), jMenuItem.getIcon().getImage());
 }

 @Test
 public void testAddShortcutAndIconWithMissingShortcut() {
 String menuLabel = "Tools";
 String key = "MissingShortcut";

 JMenuItem jMenuItem = mainMenu.createMenuItem(menuLabel, key);
 mainMenu.addShortcutAndIcon(jMenuItem, key);
 assertNull(jMenuItem.getAccelerator());
 }

 @Test(expected = MissingResourceException.class)
 public void testCannotLoadMenuLabel() {
 String menuLabel = "NonExistentMenu";
 mainMenu.createMenu(menuLabel);
 }

 @Test
 public void testChanged() {
 mainMenu.changed();
 assertTrue(true);
 }
 }