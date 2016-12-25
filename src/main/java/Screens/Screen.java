package Screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

/**
 * User interface.
 */
public interface Screen {
    void displayOutput(AsciiPanel terminal);

    Screen respondToUserInput(KeyEvent key);
}
