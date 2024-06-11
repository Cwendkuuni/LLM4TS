package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.MissingResourceException;

import org.junit.Test;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.util.UIResources;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        InspirentoMediator mockMediator = mock(InspirentoMediator.class);
        MainMenu menu = new MainMenu(mockMediator);
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenu() {
        String menuLabel = "File";
        MainMenu menu = new MainMenu(null);
        JMenu resultMenu = menu.createMenu(menuLabel);
        assertNotNull(resultMenu);
        assertEquals(menuLabel, resultMenu.getText());
    }

    @Test
    public void testCreateMenuWithLongLabel() {
        String menuLabel = "File_Import_Data*";
        MainMenu menu = new MainMenu(null);
        JMenu resultMenu = menu.createMenu(menuLabel);
        assertNotNull(resultMenu);
        assertEquals(menuLabel.substring(0, menuLabel.length() - 2), resultMenu.getText());
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuWithMissingLabel() {
        String menuLabel = "MissingMenu";
        MainMenu menu = new MainMenu(null);
        menu.createMenu(menuLabel);
    }

    @Test
    public void testAddMenuItem() {
        String menuLabel = "File";
        String menuItemLabel = "Open";
        MainMenu menu = new MainMenu(null);
        JMenu menuResult = menu.createMenu(menuLabel);
        JMenuItem menuItem = new JMenuItem(menuItemLabel);
        menu.addShortcutAndIcon(menuItem, menuItemLabel);
        menuResult.add(menuItem);
        assertEquals(menuItemLabel, menuResult.getItemAt(0).getText());
    }

    @Test
    public void testAddCheckBoxMenuItem() {
        String menuLabel = "Edit";
        String checkBoxMenuItemLabel = "Toggle";
        MainMenu menu = new MainMenu(null);
        JMenu menuResult = menu.createMenu(menuLabel);
        JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(checkBoxMenuItemLabel);
        menu.addShortcutAndIcon(checkBoxMenuItem, checkBoxMenuItemLabel);
        menuResult.add(checkBoxMenuItem);
        assertEquals(checkBoxMenuItemLabel, menuResult.getItemAt(0).getText());
    }

    @Test
    public void testAddSeparator() {
        String menuLabel = "File";
        MainMenu menu = new MainMenu(null);
        JMenu menuResult = menu.createMenu(menuLabel);
        menuResult.addSeparator();
        assertTrue(menuResult.getComponentCount() > 1);
    }

    @Test
    public void testAddShortcutAndIconWithPresent() {
        String menuLabel = "Tools";
        String menuItemLabel = "Calculator";
        String shortcut = "ctrl C";
        String iconPath = "icons/calculator.png";

        MainMenu menu = new MainMenu(null);
        JMenu menuResult = menu.createMenu(menuLabel);
        JMenuItem menuItem = new JMenuItem(menuItemLabel);
        menu.addShortcutAndIcon(menuItem, menuItemLabel);
        menuResult.add(menuItem);

        assertEquals(shortcut, menuItem.getAccelerator().toString());
        assertNotNull(menuItem.getIcon());
    }

    @Test
    public void testAddShortcutAndIconWithMissingShortcut() {
        String menuLabel = "Tools";
        String menuItemLabel = "Calculator";
        String iconPath = "icons/calculator.png";

        MainMenu menu = new MainMenu(null);
        JMenu menuResult = menu.createMenu(menuLabel);
        JMenuItem menuItem = new JMenuItem(menuItemLabel);
        menuResult.add(menuItem);

        menu.addShortcutAndIcon(menuItem, menuItemLabel);

        assertEquals(null, menuItem.getAccelerator());
        assertNotNull(menuItem.getIcon());
    }

    @Test
    public void testAddShortcutAndIconWithMissingIcon() {
        String menuLabel = "Tools";
        String menuItemLabel = "Calculator";
        String shortcut = "ctrl C";

        MainMenu menu = new MainMenu(null);
        JMenu menuResult = menu.createMenu(menuLabel);
        JMenuItem menuItem = new JMenuItem(menuItemLabel);
        menuResult.add(menuItem);

        menu.addShortcutAndIcon(menuItem, menuItemLabel);

        assertEquals(shortcut, menuItem.getAccelerator().toString());
        assertNull(menuItem.getIcon());
    }

    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIconWithMissingLabel() {
        String menuLabel = "Tools";
        String menuItemLabel = "Missing";
        String shortcut = "ctrl C";
        String iconPath = "icons/calculator.png";

        MainMenu menu = new MainMenu(null);
        JMenu menuResult = menu.createMenu(menuLabel);
        menu.addShortcutAndIcon(menuResult, menuItemLabel);
    }
}