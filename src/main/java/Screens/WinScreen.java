package Screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

/**
 * Shown to the user if the game is won.
 */
public class WinScreen implements Screen {

    /// Explain that he won.
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("Congratulations, my friend, your mission is succeeded!", 1, 1, AsciiPanel.brightYellow);
        terminal.writeCenter("-- press [enter] to restart --", 22);
    }

    /// Restart on Enter.
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}
