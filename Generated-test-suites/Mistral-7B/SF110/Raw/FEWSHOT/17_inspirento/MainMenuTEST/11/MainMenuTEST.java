package com.allenstudio.ir.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.ui.*;
import com.allenstudio.ir.util.*;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        assertNotNull(menu);
    }

    @Test
    public void testCreateMenu() {
        final String menuName = "menu1";
        final MainMenu menu = new MainMenu(null);
        final JMenu createdMenu = menu.createMenu(menuName);
        assertNotNull(createdMenu);
        assertTrue(createdMenu.getText().equals(UIResources.getString(menuName + LABEL_SUFFIX)));
    }

    @Test
    public void testCreateMenuWithSubMenus() {
        final String menuName = "menu1";
        final String subMenuName = "submenu1";
        final MainMenu menu = new MainMenu(null);
        final JMenu createdMenu = menu.createMenu(menuName);
        final JMenu subMenu = menu.createMenu(subMenuName);
        createdMenu.add(subMenu);
        assertTrue(createdMenu.getText().equals(UIResources.getString(menuName + LABEL_SUFFIX)));
        assertTrue(subMenu.getText().equals(UIResources.getString(subMenuName + LABEL_SUFFIX)));
    }

    @Test
    public void testCreateCheckboxMenuItem() {
        final String menuName = "menu1";
        final String checkboxItemName = "checkboxItem1";
        final MainMenu menu = new MainMenu(null);
        final JMenu createdMenu = menu.createMenu(menuName);
        final JCheckBoxMenuItem createdCheckboxMenuItem = (JCheckBoxMenuItem) menu.createMenuItem(checkboxItemName, createdMenu);
        assertNotNull(createdCheckboxMenuItem);
        assertTrue(createdCheckboxMenuItem.getText().equals(UIResources.getString(checkboxItemName + LABEL_SUFFIX)));
    }

    @Test
    public void testCreateMenuItem() {
        final String menuName = "menu1";
        final String itemName = "item1";
        final MainMenu menu = new MainMenu(null);
        final JMenu createdMenu = menu.createMenu(menuName);
        final JMenuItem createdMenuItem = menu.createMenuItem(itemName, createdMenu);
        assertNotNull(createdMenuItem);
        assertTrue(createdMenuItem.getText().equals(UIResources.getString(itemName + LABEL_SUFFIX)));
    }

    @Test
    public void testAddShortcutAndIcon() {
        final String menuName = "menu1";
        final String itemName = "item1";
        final String iconPath = "icon1.png";
        final String shortcut = "Ctrl+I";
        final MainMenu menu = new MainMenu(null);
        final JMenu createdMenu = menu.createMenu(menuName);
        final JMenuItem createdMenuItem = menu.createMenuItem(itemName, createdMenu);
        menu.addShortcutAndIcon(createdMenuItem, itemName);
        assertTrue(createdMenuItem.getAccelerator().toString().equals(shortcut));
        assertTrue(createdMenuItem.getIcon() instanceof ImageIcon);
        try {
            final URL url = new URL(new File(".").toURI().toURL(), iconPath);
            assertTrue(((ImageIcon) createdMenuItem.getIcon()).getImage().getWidth(null) > 0);
            assertTrue(((ImageIcon) createdMenuItem.getIcon()).getImage().getHeight(null) > 0);
        } catch (MalformedURLException | IOException e) {
            // Handle exceptions
        }
    }
}