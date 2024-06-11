package com.allenstudio.ir.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.net.*;
import java.util.*;

public class MainMenuTest {

    @Test
    public void testMainMenuCreation() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        assertNotNull(menu);
    }

    @Test
    public void testEmptyMenuCreation() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        assertTrue(menu.getComponentCount() > 0);
    }

    @Test
    public void testCreateMenuWithLabel() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testLabel = "Test Menu";
        final JMenu testMenu = menu.createMenu(testLabel);
        assertNotNull(testMenu);
        assertEquals(testLabel, testMenu.getText());
    }

    @Test
    public void testCreateMenuWithLabelAndMnemonic() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testLabel = "Test Menu*";
        final JMenu testMenu = menu.createMenu(testLabel);
        assertNotNull(testMenu);
        assertEquals(testLabel.substring(0, testLabel.length() - 2), testMenu.getText());
        assertTrue(testMenu.isMnemonicSet());
        assertEquals(testLabel.charAt(testLabel.length() - 1), testMenu.getMnemonic());
    }

    @Test
    public void testCreateSubMenu() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testSubMenuLabel = "Test Submenu";
        final JMenu testSubMenu = menu.createMenu(testSubMenuLabel + ">");
        assertNotNull(testSubMenu);
        assertEquals(testSubMenuLabel.substring(0, testSubMenuLabel.length() - 2), testSubMenu.getText());
    }

    @Test
    public void testCreateSubMenuWithLabelAndMnemonic() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testSubMenuLabel = "Test Submenu*";
        final JMenu testSubMenu = menu.createMenu(testSubMenuLabel + ">");
        assertNotNull(testSubMenu);
        assertEquals(testSubMenuLabel.substring(0, testSubMenuLabel.length() - 2), testSubMenu.getText());
        assertTrue(testSubMenu.isMnemonicSet());
        assertEquals(testSubMenuLabel.charAt(testSubMenuLabel.length() - 1), testSubMenu.getMnemonic());
    }

    @Test
    public void testCreateCheckboxMenuItem() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testItemLabel = "Test CheckboxMenuItem";
        final JCheckBoxMenuItem testCheckboxMenuItem = (JCheckBoxMenuItem) menu.createMenu("" + testItemLabel + ">")[0];
        assertNotNull(testCheckboxMenuItem);
        assertEquals(testItemLabel.substring(0, testItemLabel.length() - 2), testCheckboxMenuItem.getText());
    }

    @Test
    public void testAddShortcutAndIconToMenuItem() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testMenuItemLabel = "Test MenuItem";
        final String testShortcut = "Test Shortcut";
        final String testIconPath = "TestIcon.png";
        JMenuItem testMenuItem = menu.createMenu("#" + testMenuItemLabel)[0];
        menu.addShortcutAndIcon(testMenuItem, testMenuItemLabel);
        assertNotNull(testMenuItem.getAccelerator());
        assertNotNull(testMenuItem.getIcon());
        assertEquals(testShortcut, testMenuItem.getAccelerator().getKeyStroke().getKeyText(0));
        assertFalse(new File(testIconPath).exists()); // Assuming File is not available for testing
    }

    @Test
    public void testAddShortcutAndIconToCheckboxMenuItem() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testCheckboxMenuItemLabel = "Test CheckboxMenuItem";
        final String testShortcut = "Test Shortcut";
        final String testIconPath = "TestIcon.png";
        JCheckBoxMenuItem testCheckboxMenuItem = (JCheckBoxMenuItem) menu.createMenu("" + testCheckboxMenuItemLabel + ">")[0];
        menu.addShortcutAndIcon(testCheckboxMenuItem, testCheckboxMenuItemLabel);
        assertNotNull(testCheckboxMenuItem.getAccelerator());
        assertNotNull(testCheckboxMenuItem.getIcon());
        assertEquals(testShortcut, testCheckboxMenuItem.getAccelerator().getKeyStroke().getKeyText(0));
        assertFalse(new File(testIconPath).exists()); // Assuming File is not available for testing
    }

    @Test
    public void testGetMenuLabel() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testLabel = "Test Menu";
        final JMenu testMenu = menu.createMenu(testLabel);
        assertEquals(testLabel, menu.getMenuLabel(testMenu));
    }

    @Test
    public void testGetMenuItemLabel() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testLabel = "Test MenuItem";
        JMenuItem testMenuItem = menu.createMenu("#" + testLabel)[0];
        assertEquals(testLabel, menu.getMenuItemLabel(testMenuItem));
    }

    @Test
    public void testGetCheckboxMenuItemLabel() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testLabel = "Test CheckboxMenuItem";
        JCheckBoxMenuItem testCheckboxMenuItem = (JCheckBoxMenuItem) menu.createMenu("" + testLabel + ">")[0];
        assertEquals(testLabel, menu.getCheckboxMenuItemLabel(testCheckboxMenuItem));
    }

    @Test
    public void testGetShortcutFromMenuItemLabel() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testLabel = "Test MenuItem";
        JMenuItem testMenuItem = menu.createMenu("#" + testLabel)[0];
        assertEquals("Test Shortcut", menu.getShortcutFromMenuItemLabel(testMenuItem));
    }

    @Test
    public void testTokenizeMenuString() {
        final String testMenuString = "Test Menu1*|Test Menu2>|Test Menu3#Test MenuItem1|Test MenuItem2*|Test MenuItem3";
        final String[] tokens = InspirentoUtilities.tokenize(testMenuString);
        assertEquals(6, tokens.length);
        assertEquals("Test Menu1", tokens[0]);
        assertEquals("Test Menu2>", tokens[1]);
        assertEquals("Test Menu3", tokens[2]);
        assertEquals("Test MenuItem1", tokens[3]);
        assertEquals("Test MenuItem2*", tokens[4]);
        assertEquals("Test MenuItem3", tokens[5]);
    }

    @Test
    public void testCreateMenuWithEmptyLabel() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testLabel = "";
        final JMenu testMenu = menu.createMenu(testLabel);
        assertNull(testMenu);
    }

    @Test(expected = MissingResourceException.class)
    public void testCreateMenuWithMissingLabel() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testLabel = "MissingLabel";
        menu.createMenu(testLabel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMenuWithInvalidToken() {
        final InspirentoMediator mediator = new InspirentoMediator();
        final MainMenu menu = new MainMenu(mediator);
        final String testLabel = "Test Menu|";
        menu.createMenu(testLabel);
    }
}