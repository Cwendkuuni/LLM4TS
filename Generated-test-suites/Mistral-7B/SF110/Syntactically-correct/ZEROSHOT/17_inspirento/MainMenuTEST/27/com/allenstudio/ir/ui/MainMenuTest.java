package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.util.UIResources;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuTest {

 @Mock
 private InspirentoMediator mediator;
 @InjectMocks
 private MainMenu mainMenu;

 @Before
 public void setUp() {
 // Initialize mocks and stubs here if necessary
 }

 @Test
 public void testMainMenuConstruction() {
 MainMenu menu = new MainMenu(mediator);
 assertNotNull(menu);
 }

 @Test
 public void testCreateMenu() {
 String menuName = "TestMenu";
 JMenu expectedMenu = new JMenu(menuName);
 String subMenuName = "SubMenu";
 String itemName = "Item";
 String checkboxItemName = "CheckboxItem";

 Mockito.when(UIResources.getString(menuName + "Label")).thenReturn(menuName);
 Mockito.when(UIResources.getString(menuName + "Label")).thenReturn(menuName + "*");
 Mockito.when(UIResources.getString(menuName)).thenReturn(menuName + "-");

 Mockito.when(UIResources.getString(subMenuName + "Label")).thenReturn(subMenuName + "*");
 Mockito.when(UIResources.getString(subMenuName)).thenReturn(subMenuName + "-");

 Mockito.when(UIResources.getString(itemName + "Label")).thenReturn(itemName + "*");
 Mockito.when(UIResources.getString(itemName)).thenReturn(itemName);
 Mockito.when(UIResources.getString(itemName + "Shortcut")).thenReturn("F1");
 Mockito.when(UIResources.getString(itemName + "Icon")).thenReturn("icon.png");

 Mockito.when(UIResources.getString(checkboxItemName + "Label")).thenReturn(checkboxItemName + "*");
 Mockito.when(UIResources.getString(checkboxItemName)).thenReturn(checkboxItemName);
 Mockito.when(UIResources.getString(checkboxItemName + "Shortcut")).thenReturn("F2");
 Mockito.when(UIResources.getString(checkboxItemName + "Icon")).thenReturn("icon.png");

 String[] menuStrings = UIResources.getString("menu").split("\\s+");
 String[] subMenuStrings = UIResources.getString(menuStrings[0]).split("\\s+");
 String[] itemStrings = UIResources.getString(subMenuStrings[0]).split("\\s+");
 String[] checkboxItemStrings = UIResources.getString(itemStrings[0]).split("\\s+");

 JMenu actualMenu = mainMenu.createMenu(menuName);
 assertNotNull(actualMenu);
 assertEquals(menuName, actualMenu.getText());

 List<JMenuItem> menuItems = Arrays.asList(actualMenu.getMenuComponents());
 assertEquals(2, menuItems.size());
 JMenu subMenu = (JMenu) menuItems.get(0);
 assertEquals(subMenuName, subMenu.getText());

 List<JMenuItem> subMenuItems = Arrays.asList(subMenu.getMenuComponents());
 assertEquals(3, subMenuItems.size());
 JMenuItem item = (JMenuItem) subMenuItems.get(1);
 assertEquals(itemName, item.getText());
 assertTrue(item.isEnabled());
 assertEquals(KeyEvent.VK_F1, item.getAcceleratorKeyEvent().getKeyCode());
 assertEquals(1, item.getIconTextGap());
 assertEquals("icon.png", item.getIcon().toString());

 JCheckBoxMenuItem checkboxItem = (JCheckBoxMenuItem) subMenuItems.get(2);
 assertEquals(checkboxItemName, checkboxItem.getText());
 assertTrue(checkboxItem.isSelected());
 assertTrue(checkboxItem.isToggleState());
 assertTrue(checkboxItem.isArmed());
 assertEquals(KeyEvent.VK_F2, checkboxItem.getAcceleratorKeyEvent().getKeyCode());
 assertEquals("icon.png", checkboxItem.getIcon().toString());
}

 @Test(expected = MissingResourceException.class)
 public void testLoadingMissingMenuLabel() {
 String missingMenuLabel = "MissingLabel";
 mainMenu.createMenu(missingMenuLabel);
}
}