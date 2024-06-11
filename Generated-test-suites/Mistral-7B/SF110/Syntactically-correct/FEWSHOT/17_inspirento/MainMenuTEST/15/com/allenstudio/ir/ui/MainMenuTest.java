package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediatorMock);
        verify(mediatorMock).setMenuBar(mainMenu);

        String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
        int expectedNumberOfMenus = menus.length;
        assertEquals("Number of menus created should match number of menu entries.", expectedNumberOfMenus, mainMenu.getMenuCount());
    }

    @Test
    public void testCreateMenu() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediatorMock);

        String menuString = "Menu1";
        JMenu menu = mainMenu.createMenu(menuString);

        String tempLabel = UIResources.getString(menuString + "Label");

        if (tempLabel.length() > 2 && tempLabel.charAt(tempLabel.length() - 2) == '*') {
            String expectedLabel = tempLabel.substring(0, tempLabel.length() - 2);
            String expectedMnemonic = Character.toString(tempLabel.charAt(tempLabel.length() - 1));
            assertEquals("Created menu label doesn't match expected label.", expectedLabel, menu.getText());
            assertEquals("Mnemonic for menu doesn't match expected value.", expectedMnemonic, String.valueOf(menu.getMnemonicKey()));
        } else {
            assertEquals("Created menu label doesn't match expected value.", menuString, menu.getText());
        }

        // Verify that the menu is added to the MainMenu instance
        JMenuBar jMenuBar = mainMenu.getJMenuBar();
        assertEquals("The menu was not added to the MainMenu instance.", menu, jMenuBar.getComponent(0));
    }

    @Test
    public void testCreateSubMenu() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediatorMock);

        String menuString = "Menu1>SubMenu";
        JMenu menu = mainMenu.createMenu(menuString);

        String tempLabel = UIResources.getString(menuString.substring(0, menuString.indexOf(">")) + "Label");

        if (tempLabel.length() > 2 && tempLabel.charAt(tempLabel.length() - 2) == '*') {
            String expectedLabel = tempLabel.substring(0, tempLabel.length() - 2);
            String expectedMnemonic = Character.toString(tempLabel.charAt(tempLabel.length() - 1));
            assertEquals("Created menu label doesn't match expected label.", expectedLabel, menu.getText());
            assertEquals("Mnemonic for menu doesn't match expected value.", expectedMnemonic, String.valueOf(menu.getMnemonicKey()));
        } else {
            assertEquals("Created menu label doesn't match expected value.", menuString.substring(0, menuString.indexOf(">")), menu.getText());
        }

        // Verify that the submenu is added to the parent menu
        JMenu parentMenu = mainMenu.getJMenuBar().getComponent(0);
        assertEquals("The submenu was not added to the parent menu.", menu, parentMenu.getComponent(0));
    }

    @Test
    public void testCreateCheckBoxMenuItem() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediatorMock);

        String menuString = "Menu1#CheckBoxMenuItem";
        JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) mainMenu.createMenu(menuString).getComponent(0);

        String itemLabel = UIResources.getString(menuString.substring(1) + "Label");

        if (itemLabel.length() > 2 && itemLabel.charAt(itemLabel.length() - 2) == '*') {
            String expectedLabel = itemLabel.substring(0, itemLabel.length() - 2);
            String expectedMnemonic = Character.toString(itemLabel.charAt(itemLabel.length() - 1));
            assertEquals("Created checkbox menu item label doesn't match expected label.", expectedLabel, checkBoxMenuItem.getText());
            assertEquals("Mnemonic for checkbox menu item doesn't match expected value.", expectedMnemonic, String.valueOf(checkBoxMenuItem.getMnemonicKey()));
        } else {
            assertEquals("Created checkbox menu item label doesn't match expected value.", menuString.substring(1), checkBoxMenuItem.getText());
        }

        // Verify that the checkbox menu item is added to the parent menu
        JMenu parentMenu = mainMenu.createMenu(menuString.substring(0, menuString.indexOf("#"))).getComponent(0);
        assertEquals("The checkbox menu item was not added to the parent menu.", checkBoxMenuItem, parentMenu.getComponent(0));
    }

    @Test
    public void testCreateMenuItem() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediatorMock);

        String menuString = "Menu1#MenuItem";
        JMenuItem menuItem = mainMenu.createMenu(menuString).getComponent(1);

        String itemLabel = UIResources.getString(menuString.substring(1) + "Label");

        if (itemLabel.length() > 2 && itemLabel.charAt(itemLabel.length() - 2) == '*') {
            String expectedLabel = itemLabel.substring(0, itemLabel.length() - 2);
            String expectedMnemonic = Character.toString(itemLabel.charAt(itemLabel.length() - 1));
            assertEquals("Created menu item label doesn't match expected label.", expectedLabel, menuItem.getText());
            assertEquals("Mnemonic for menu item doesn't match expected value.", expectedMnemonic, String.valueOf(menuItem.getMnemonicKey()));
        } else {
            assertEquals("Created menu item label doesn't match expected value.", menuString.substring(1), menuItem.getText());
        }

        // Verify that the menu item is added to the parent menu
        JMenu parentMenu = mainMenu.createMenu(menuString.substring(0, menuString.indexOf("#"))).getComponent(1);
        assertEquals("The menu item was not added to the parent menu.", menuItem, parentMenu.getComponent(1));
    }

    @Test
    public void testAddShortcutAndIcon() {
        InspirentoMediator mediatorMock = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediatorMock);

        String menuString = "Menu1#MenuItem";
        JMenuItem menuItem = mainMenu.createMenu(menuString).getComponent(1);

        String shortcut = UIResources.getString(menuString + "Shortcut");
        assertEquals("Shortcut key for menu item doesn't match expected value.", shortcut, menuItem.getAccelerator().toString());

        String iconPath = UIResources.getString(menuString + "Icon");
        Image expectedIcon = ImageIO.read(new File(iconPath));
        Image actualIcon = menuItem.getIcon().getImage();
        assertEquals("Icon for menu item doesn't match expected value.", expectedIcon, actualIcon);

        menuString = "Menu1#CheckBoxMenuItem";
        JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) mainMenu.createMenu(menuString).getComponent(0);

        shortcut = UIResources.getString(menuString + "Shortcut");
        assertEquals("Shortcut key for checkbox menu item doesn't match expected value.", shortcut, checkBoxMenuItem.getAccelerator().toString());

        iconPath = UIResources.getString(menuString + "Icon");
        expectedIcon = ImageIO.read(new File(iconPath));
        actualIcon = checkBoxMenuItem.getIcon().getImage();
        assertEquals("Icon for checkbox menu item doesn't match expected value.", expectedIcon, actualIcon);
    }
}