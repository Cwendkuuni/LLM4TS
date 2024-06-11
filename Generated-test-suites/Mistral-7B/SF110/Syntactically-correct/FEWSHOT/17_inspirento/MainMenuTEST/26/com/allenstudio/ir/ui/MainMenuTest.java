package com.allenstudio.ir.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
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
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu")).clone();

        for (String menu : menus) {
            JMenu testMenu = menu.createMenu(menu);
            assertNotNull(testMenu);
        }
    }

    @Test
    public void testCreateMenuWithLabelAndShortcut() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);

        JMenu testMenu = menu.createMenu("File");
        String[] items = InspirentoUtilities.tokenize(UIResources.getString("File"));

        for (String item : items) {
            if (!"-".equals(item)) {
                if (item.length() > 1 && item.charAt(item.length() - 1) == '>') {
                    JMenu subMenu = menu.createMenu(item.substring(0, item.length() - 1));
                    assertNotNull(subMenu);
                    testMenu.add(subMenu);
                } else if (item.charAt(0) == '#') {
                    JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) testMenu.add(menu.createMenuItem(item.substring(1)));
                    assertNotNull(checkboxMenuItem);
                    assertTrue(checkboxMenuItem.isSelected());
                } else {
                    JMenuItem jMenuItem = menu.createMenuItem(item);
                    assertNotNull(jMenuItem);
                    assertNull(jMenuItem.getAccelerator());
                    assertNull(jMenuItem.getIcon());
                    final String key = item;
                    menu.addShortcutAndIcon(jMenuItem, key);
                    assertNotNull(jMenuItem.getAccelerator());
                    assertNull(jMenuItem.getIcon());
                    testMenu.add(jMenuItem);
                }
            } else {
                testMenu.addSeparator();
            }
        }
    }

    @Test
    public void testCreateMenuWithLabelAndShortcutAndIcon() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);

        JMenu testMenu = menu.createMenu("File");
        String[] items = InspirentoUtilities.tokenize(UIResources.getString("File"));

        for (String item : items) {
            if (!"-".equals(item)) {
                if (item.length() > 1 && item.charAt(item.length() - 1) == '>') {
                    JMenu subMenu = menu.createMenu(item.substring(0, item.length() - 1));
                    assertNotNull(subMenu);
                    testMenu.add(subMenu);
                } else if (item.charAt(0) == '#') {
                    JCheckBoxMenuItem checkboxMenuItem = (JCheckBoxMenuItem) testMenu.add(menu.createMenuItem(item.substring(1)));
                    assertNotNull(checkboxMenuItem);
                    assertTrue(checkboxMenuItem.isSelected());
                } else {
                    JMenuItem jMenuItem = menu.createMenuItem(item);
                    assertNotNull(jMenuItem);
                    String key = item;
                    menu.addShortcutAndIcon(jMenuItem, key);
                    assertNotNull(jMenuItem.getAccelerator());
                    assertNotNull(jMenuItem.getIcon());
                    testMenu.add(jMenuItem);
                }
            } else {
                testMenu.addSeparator();
            }
        }
    }

    @Test
    public void testAddShortcutAndIcon() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        JMenuItem testMenuItem = menu.createMenuItem("Test");
        menu.addShortcutAndIcon(testMenuItem, "Test");
        assertNotNull(testMenuItem.getAccelerator());
        assertNotNull(testMenuItem.getIcon());
    }

    @Test
    public void testGetResource() {
        final String key = "Test";
        final String expected = "test.png";
        final String result = UIResources.getString(key + "Icon");
        assertTrue(result.equals(expected));
    }

    @Test
    public void testMissingResource() {
        final String key = "MissingResource";
        final String result = UIResources.getString(key + "Icon");
        assertNull(result);
    }

    @Test
    public void testCreateMenuItem() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        JMenuItem testMenuItem = menu.createMenuItem("");
        assertNotNull(testMenuItem);
    }
}