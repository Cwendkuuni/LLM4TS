package com.allenstudio.ir.ui.test;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.MainMenu;
import com.allenstudio.ir.util.*;
import org.junit.Test;

import java.awt.*;
import java.net.*;
import java.util.*;

public class MainMenuTest {

    @Test
    public void testMainMenuConstructor() {
        String[] menus = new String[]{"File", "Edit", "View"};
        MainMenu menu = new MainMenu(new InspirentoMediator());
        assertEquals(menus.length, menu.getMenuCount());
        for (String menuName : menus) {
            JMenu menuItem = menu.getComponent(0);
            assertEquals(menuName, menuItem.getText());
            int index = 0;
            for (Component component : menuItem.getMenuComponents()) {
                JMenu subMenu = (JMenu) component;
                String subMenuName = InspirentoUtilities.tokenize(UIResources.getString(menuName))[0];
                assertEquals(subMenuName, subMenu.getText());
                index++;
            }
            assertEquals(menus.length > 1 ? menus.length - 1 : 0, index);
        }
    }

    @Test
    public void testCreateMenuEmpty() {
        String menu = "EmptyMenu";
        JMenu jMenu = MainMenu.createMenu(menu);
        assertNull(jMenu);
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuInvalidMenuLabel() {
        String menu = "InvalidMenuLabel";
        MainMenu.createMenu(menu);
    }

    @Test
    public void testCreateMenuWithItems() {
        String menu = "File";
        MainMenu menuInstance = new MainMenu(new InspirentoMediator());
        JMenu jMenu = menuInstance.createMenu(menu);
        String[] items = new String[]{"New", "Open", "Save", "Save As"};
        String[] expectedSubMenus = new String[]{"New", "Open", "Save", "Save As"};
        for (String item : items) {
            if (item.charAt(item.length() - 1) == '>') {
                JMenu subMenu = (JMenu) jMenu.getComponent(0);
                assertEquals(item.substring(0, item.length() - 1), subMenu.getText());
                String subItem = InspirentoUtilities.tokenize(UIResources.getString(item))[0];
                JMenuItem jMenuItem = (JMenuItem) subMenu.getComponent(0);
                assertEquals(subItem, jMenuItem.getText());
            } else {
                JMenuItem jMenuItem = (JMenuItem) jMenu.getComponent(0);
                String subItem = InspirentoUtilities.tokenize(UIResources.getString(item))[0];
                assertEquals(subItem, jMenuItem.getText());
                String shortcut = UIResources.getString(item + "Shortcut");
                assertEquals(KeyStroke.getKeyStroke(shortcut), jMenuItem.getAccelerator());
                String icon = UIResources.getString(item + "Icon");
                URL url = new URL(icon);
                assertEquals(url, jMenuItem.getIcon().getImage().getSource());
            }
            jMenu.getComponent(0).setEnabled(false);
            assertEquals(false, jMenuItem.isEnabled());
            jMenu.getComponent(1).setEnabled(true);
            assertEquals(true, jMenuItem.isEnabled());
        }
    }

    @Test
    public void testCreateMenuWithSeparator() {
        String menu = "File";
        MainMenu menuInstance = new MainMenu(new InspirentoMediator());
        JMenu jMenu = menuInstance.createMenu(menu);
        String[] items = new String[]{"New", "Open", "Save", "Save As", "", "Exit"};
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals("")) {
                assertEquals(JMenu.separator, jMenu.getComponent(i));
            } else {
                JMenuItem jMenuItem = (JMenuItem) jMenu.getComponent(i);
                String subItem = InspirentoUtilities.tokenize(UIResources.getString(items[i]))[0];
                assertEquals(subItem, jMenuItem.getText());
                String shortcut = UIResources.getString(items[i] + "Shortcut");
                assertEquals(KeyStroke.getKeyStroke(shortcut), jMenuItem.getAccelerator());
                String icon = UIResources.getString(items[i] + "Icon");
                URL url = new URL(icon);
                assertEquals(url, jMenuItem.getIcon().getImage().getSource());
            }
        }
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingResourceException() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
    }
}