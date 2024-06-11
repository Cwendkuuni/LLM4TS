package com.allenstudio.ir.ui;

import static org.junit.Assert.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.net.*;

public class MainMenuTest {

    @Test
    public void testMainMenuCreation() {
        String uiResourcesString = "menu=File,Edit,View,Help*File=New,Open,Save,Save As,Import,Export,Exit*Edit=Cut,Copy,Paste,Find,Replace*View=Show Toolbar,Show Status Bar,Zoom In,Zoom Out*Help=About";
        UIResources mockUiResources = new UIResources() {
            @Override
            public String getString(String key) {
                return uiResourcesString.contains(key) ? key.substring(0, key.lastIndexOf('=')) : "";
            }
        };

        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediator);

        // Test menu creation
        assertEquals(4, mainMenu.getMenuCount());
        String[] expectedMenuLabels = {"File", "Edit", "View", "Help"};
        for (int i = 0; i < expectedMenuLabels.length; i++) {
            JMenu menu = (JMenu) mainMenu.getComponent(i);
            String menuLabel = menu.getText();
            assertEquals(expectedMenuLabels[i], menuLabel);
        }

        // Test menu items creation
        String menuItemTexts[][] = {{"New", null}, {"Open", null}, {"Save", null}, {"Save As", null}, {"Import", null}, {"Export", null}, {"Exit", null},
                {"Cut", null}, {"Copy", null}, {"Paste", null}, {"Find", null}, {"Replace", null}, {"Show Toolbar", null}, {"Show Status Bar", null},
                {"Zoom In", null}, {"Zoom Out", null}, {"About", null}};
        for (int i = 0; i < mainMenu.getMenuCount(); i++) {
            JMenu menu = (JMenu) mainMenu.getComponent(i);
            for (int j = 0; j < menu.getMenuComponentCount(); j++) {
                JMenuItem menuItem = (JMenuItem) menu.getMenuComponent(j);
                String menuItemText = menuItem.getText();
                String expectedMenuItemText = menuItemTexts[i][j];
                assertTrue(menuItemText.equals(expectedMenuItemText) || (expectedMenuItemText.length() > 1 && menuItemText.startsWith(expectedMenuItemText.substring(0, expectedMenuItemText.length() - 1))));
            }
        }

        // Test shortcut keys
        String expectedShortcutKeys[][] = {{"New", "N"}, {"Open", "O"}, {"Save", "S"}, {"Save As", "Shift+S"}, {"Import", "Ctrl+I"}, {"Export", "Ctrl+E"},
                {"Export", "Ctrl+Shift+E"}, {"Exit", "Alt+F4"}, {"Cut", "Ctrl+X"}, {"Copy", "Ctrl+C"}, {"Paste", "Ctrl+V"}, {"Find", "Ctrl+F"},
                {"Replace", "Ctrl+H"}, {"Show Toolbar", "Ctrl+B"}, {"Show Status Bar", "Ctrl+Shift+B"}, {"Zoom In", "Ctrl++"}, {"Zoom Out", "Ctrl+-"}};

        for (int i = 0; i < mainMenu.getMenuCount(); i++) {
            JMenu menu = (JMenu) mainMenu.getComponent(i);
            for (int j = 0; j < menu.getMenuComponentCount(); j++) {
                JMenuItem menuItem = (JMenuItem) menu.getMenuComponent(j);
                String menuItemText = menuItem.getText();
                String expectedMenuItemText = menuItemTexts[i][j];
                if (menuItem instanceof JCheckBoxMenuItem) {
                    // JCheckBoxMenuItem does not have shortcut keys
                    continue;
                }
                KeyStroke expectedShortcutKey = null;
                String shortcutKeyString = "";
                if (expectedMenuItemText.length() > 1 && expectedMenuItemText.charAt(expectedMenuItemText.length() - 1) == '>') {
                    expectedShortcutKey = menu.getComponent(j + 1).getAccelerator();
                    shortcutKeyString = KeyEvent.getKeyText(expectedShortcutKey.getKeyCode());
                }
                else {
                    expectedShortcutKey = menuItem.getAccelerator();
                    shortcutKeyString = KeyEvent.getKeyText(expectedShortcutKey.getKeyCode());
                }

                String menuItemShortcutKey = menuItem.getActionCommand();
                String expectedMenuItemShortcutKey = menuItemText.substring(0, menuItemText.lastIndexOf('=')) + "Shortcut";
                String expectedShortcutKeyString = uiResourcesString.substring(uiResourcesString.indexOf(expectedMenuItemShortcutKey) + expectedMenuItemShortcutKey.length()).split("=")[0];
                assertEquals(expectedShortcutKeyString, shortcutKeyString);
                assertEquals(expectedShortcutKeys[i][j], new Object[]{menuItemText, expectedShortcutKeyString});
            }
        }

        // Test icons
        String expectedIconsPaths[][] = {{"New", "icons/new.png"}, {"Open", "icons/open.png"}, {"Save", "icons/save.png"}, {"Save As", "icons/saveas.png"},
                {"Import", "icons/import.png"}, {"Export", "icons/export.png"}, {"Exit", "icons/exit.png"}, {"Cut", "icons/cut.png"}, {"Copy", "icons/copy.png"},
                {"Paste", "icons/paste.png"}, {"Find", "icons/find.png"}, {"Replace", "icons/replace.png"}, {"Show Toolbar", "icons/toolbar.png"},
                {"Show Status Bar", "icons/statusbar.png"}, {"Zoom In", "icons/zoomIn.png"}, {"Zoom Out", "icons/zoomOut.png"}, {"About", "icons/about.png"}};

        for (int i = 0; i < mainMenu.getMenuCount(); i++) {
            JMenu menu = (JMenu) mainMenu.getComponent(i);
            for (int j = 0; j < menu.getMenuComponentCount(); j++) {
                JMenuItem menuItem = (JMenuItem) menu.getMenuComponent(j);
                String menuItemText = menuItem.getText();
                String expectedMenuItemText = menuItemTexts[i][j];
                if (menuItem instanceof JCheckBoxMenuItem) {
                    // JCheckBoxMenuItem does not have icons
                    continue;
                }
                Image expectedIconImage = null;
                String expectedIconPath = null;
                if (expectedMenuItemText.length() > 1 && expectedMenuItemText.charAt(expectedMenuItemText.length() - 1) == '>') {
                    expectedIconPath = ((JMenu) menu.getComponent(j + 1)).getIcon().toString();
                }
                else {
                    expectedIconPath = menuItem.getIcon().toString();
                }

                String menuItemIconPath = expectedIconsPaths[i][j];
                String expectedIconPathString = uiResourcesString.substring(uiResourcesString.indexOf(menuItemIconPath) + menuItemIconPath.length()).split("=")[0];
                assertEquals(expectedIconPathString, expectedIconPath);
                String expectedIconFileName = expectedIconPath.substring(expectedIconPath.lastIndexOf('/') + 1);
                assertEquals(expectedIconFileName, menuItem.getIcon().toString().substring(menuItem.getIcon().toString().lastIndexOf('/') + 1));
            }
        }
    }

    @Test
    public void testMenuCreationWithMissingResources() {
        String uiResourcesString = "menu=File,Edit,View,Help*File=New,Open,Save,Save As,Import,Export,Exit*Edit=Cut,Copy,Paste,Find,Replace*View=Show Toolbar,Show Status Bar,Zoom In,Zoom Out*Help=About,MissingMenu";
        UIResources mockUiResources = new UIResources() {
            @Override
            public String getString(String key) {
                return uiResourcesString.contains(key) ? key.substring(0, key.lastIndexOf('=')) : "";
            }
        };

        InspirentoMediator mediator = Mockito.mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediator);

        try {
            mainMenu.createMenu("MissingMenu");
            fail("Expected MissingResourceException");
        } catch (MissingResourceException e) {
            // Expected
        }
    }
}