package Screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

/**
 * Shown to the user in the beginning of the game.
 */
public class StartScreen implements Screen {

    /// Explain what's going on.
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("Welcome to to the Crystal Caves of the ice planet of Ilum, young padawan!", 1, 1,
                AsciiPanel.brightYellow);
        terminal.write("", 1, 2, AsciiPanel.brightYellow);
        terminal.write("You were sent to accomplish an essential part of your Jedi training -- ", 1, 3,
                AsciiPanel.brightYellow);
        terminal.write("to construct your own lightsaber. In order to do this you need to", 1, 4,
                AsciiPanel.brightYellow);
        terminal.write("harvest some kyber crystals, which you'll find in the Caves.", 1, 5, AsciiPanel.brightYellow);
        terminal.write("", 1, 6, AsciiPanel.brightYellow);
        terminal.write("Beware of the imperial troopers, they are always around. And mind it", 1, 7,
                AsciiPanel.brightYellow);
        terminal.write("that you can always dig into the Caves' walls, though it will cost you", 1, 8,
                AsciiPanel.brightYellow);
        terminal.write("some Force.", 1, 9, AsciiPanel.brightYellow);
        terminal.write("", 1, 10, AsciiPanel.brightYellow);
        terminal.write("Good luck. And may the Force be with you!", 1, 11, AsciiPanel.brightYellow);
        terminal.writeCenter("-- press [enter] to start --", 22);
    }

    /// Restart on Enter.
    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}
