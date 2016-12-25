package Screens;

import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.Assert.assertTrue;

/**
 * Created by liza on 25.12.16.
 */
public class ScreenTest {

    @Test
    public void respondToUserInput() throws Exception {
        KeyEvent escapePress = new KeyEvent(new Button("meow"), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                KeyEvent.VK_ESCAPE, KeyEvent.CHAR_UNDEFINED);
        // The two following actions are together pretty complicated, so provide a nice check.
        PlayScreen screen = new PlayScreen();
        Screen result = screen.respondToUserInput(escapePress);

        assertTrue(result.getClass() == LoseScreen.class);
    }

}