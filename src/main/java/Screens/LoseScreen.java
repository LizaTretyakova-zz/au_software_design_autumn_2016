package Screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

/**
 * Shown to the user when he lost.
 */
public class LoseScreen implements Screen {

    /// Explain that he lost.
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("You've lost, young padawan. But remember, that patience", 1, 1, AsciiPanel.brightYellow);
        terminal.write("is the truly Jedi way, so let the Force guide you, and try again.", 1, 2,
                AsciiPanel.brightYellow);
        terminal.writeCenter("-- press [enter] to restart --", 22);
    }

    /// Restart on Enter.
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}
