package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.allenstudio.ir.core.InspirentoMediator;
import com.allenstudio.ir.util.UIResources;
import com.allenstudio.ir.util.InspirentoUtilities;
import javax.swing.*;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuTest {

 @Mock
 private InspirentoMediator mediator;
 @Mock
 private UIResources uiResources;
 @Mock
 private InspirentoUtilities inspirentoUtilities;

 @InjectMocks
 private MainMenu mainMenu;

 @Before
 public void setUp() {
 JVMSystem.init();
 when(uiResources.getString(anyString())).then(invocation -> {
 String key = invocation.getArgument(0);
 switch (key) {
 case "menu":
 return "Menu1,Menu2,Menu3";
 case "Menu1":
 return "Menu1Label,Menu1Item1,Menu1Item2,-";
 case "Menu1Item1":
 return "#Icon1,Menu1Item1Label,Menu1Item1Shortcut";
 case "Menu1Label":
 return "Menu1Label";
 case "Menu1Item2":
 return "Menu1Item2Label,Menu1Item2Shortcut";
 case "Menu1Item2Label":
 return "Menu1Item2Label";
 default:
 return "";
 }
 });
 when(uiResources.getString(anyString().startsWith("#"))).then(invocation -> {
 String key = invocation.getArgument(0);
 return "Icon" + key.substring(1);
 });
 when(uiResources.getString(anyString().endsWith("Shortcut"))).then(invocation -> {
 String key = invocation.getArgument(0);
 return Character.toString(key.charAt(key.length() - 2)); + KeyEvent.VK_F1;
 });
 when(uiResources.getString(anyString())).thenThrow(new MissingResourceException("", "", ""));
 when(inspirentoUtilities.tokenize(anyString())).then(invocation -> {
 String input = invocation.getArgument(0);
 return input.split(",");
 });
 }

 @Test
 public void testMainMenuConstruction() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
 MainMenu mainMenu = new MainMenu(mediator);
 assertEquals("Menu1,Menu2,Menu3", mainMenu.getJMenuBar().getComponentCount());
 JMenu menu1 = (JMenu) mainMenu.getJMenuBar().getComponent(0);
 assertEquals("Menu1Label", menu1.getText());
 JMenuItem menu1Item1 = (JMenuItem) menu1.getComponent(0);
 assertEquals("Menu1Item1Label", menu1Item1.getText());
 assertEquals('F1', menu1Item1.getAcceleratorKeyChar());
 Image icon = menu1Item1.getIcon().getImage();
 URL url = new URL("Icon1");
 assertEquals(url.toString(), icon.toString());
 JCheckBoxMenuItem menu1Item2 = (JCheckBoxMenuItem) menu1.getComponent(1);
 assertEquals("Menu1Item2Label", menu1Item2.getText());
 assertFalse(menu1Item2.isSelected());
 assertEquals(0, menu1Item2.getIcon().getImage().getWidth(null));
 assertEquals(0, menu1Item2.getIcon().getImage().getHeight(null));
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingMenuLabel() {
 MainMenu mainMenu = new MainMenu(mediator);
 }

 @Test
 public void testCreateMenu() {
 String menu = "Menu4";
 JMenu expectedMenu = new JMenu(menu);
 expectedMenu.setText("Menu4Label");
 expectedMenu.setMnemonic('4');
 String[] items = {"Item4.1>", "Item4.2>", "#Item4.3,Item4.3Label,Item4.3Shortcut", "Item4.4", "-"};
 JMenu actualMenu = mainMenu.createMenu(menu);
 assertEquals(expectedMenu, actualMenu);
 assertEquals(5, actualMenu.getComponentCount());
 JMenu subMenu = (JMenu) actualMenu.getComponent(0);
 assertEquals("Item4.1", subMenu.getText());
 assertEquals("Item4.1Label", subMenu.getComponent(0).getText());
 assertEquals('4', subMenu.getComponent(0).getMnemonic());
 JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) actualMenu.getComponent(1);
 assertEquals("Item4.3", checkBoxMenuItem.getText());
 assertEquals("Item4.3Label", checkBoxMenuItem.getLabelForComponent());
 assertFalse(checkBoxMenuItem.isSelected());
 assertEquals(0, checkBoxMenuItem.getIcon().getImage().getWidth(null));
 assertEquals(0, checkBoxMenuItem.getIcon().getImage().getHeight(null));
 assertEquals('4', checkBoxMenuItem.getMnemonic());
 JMenuItem item4_4 = (JMenuItem) actualMenu.getComponent(2);
 assertEquals("Item4.4Label", item4_4.getText());
 assertEquals('4', item4_4.getMnemonic());
 assertEquals(KeyEvent.VK_F1, item4_4.getAcceleratorKeyEvent().getKeyChar());
 }
}