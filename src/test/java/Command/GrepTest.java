package Command;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

public class GrepTest {
    private static final byte[] TEXT =
            (" Forms FORM-29827281-12:\n" +
                    "Test Assessment Report\n" +
                    "\n" +
                    "This was a triumph.\n" +
                    "I'm making a note here:\n" +
                    "HUGE SUCCESS.\n" +
                    "It's hard to overstate\n" +
                    "my satisfaction.\n" +
                    "Aperture Science\n" +
                    "We do what we must\n" +
                    "because we can.\n" +
                    "For the good of all of us.\n" +
                    "Except the ones who are dead.\n" +
                    "\n" +
                    "But there's no sense crying\n" +
                    "over every mistake.\n" +
                    "You just keep on trying\n" +
                    "till you run out of cake.\n" +
                    "And the Science gets done.\n" +
                    "And you make a neat gun.\n" +
                    "For the people who are\n" +
                    "still alive.\n" +
                    "\n" +
                    "Forms FORM-55551-5:\n" +
                    "Personnel File Addendum:\n" +
                    "\n" +
                    "Dear <<Subject Name Here>>,\n" +
                    "\n" +
                    "I'm not even angry.\n" +
                    "I'm being so sincere right now.\n" +
                    "Even though you broke my heart.\n" +
                    "And killed me.\n" +
                    "And tore me to pieces.\n" +
                    "And threw every piece into a fire.\n" +
                    "As they burned it hurt because\n" +
                    "I was so happy for you!\n" +
                    "Now these points of data\n" +
                    "make a beautiful line.\n" +
                    "And we're out of beta.\n" +
                    "We're releasing on time.\n" +
                    "So I'm GLaD. I got burned.\n" +
                    "Think of all the things we learned\n" +
                    "for the people who are\n" +
                    "still alive.\n" +
                    "\n" +
                    "Forms FORM-55551-6:\n" +
                    "Personnel File Addendum Addendum:\n" +
                    "\n" +
                    "One last thing:\n" +
                    "\n" +
                    "Go ahead and leave me.\n" +
                    "I think I prefer to stay inside.\n" +
                    "Maybe you'll find someone else\n" +
                    "to help you.\n" +
                    "Maybe Black Mesa...\n" +
                    "THAT WAS A JOKE. HA HA. FAT CHANCE.\n" +
                    "Anyway, this cake is great.\n" +
                    "It's so delicious and moist.\n" +
                    "Look at me still talking\n" +
                    "when there's Science to do.\n" +
                    "When I look out there,\n" +
                    "it makes me GLaD I'm not you.\n" +
                    "I've experiments to run.\n" +
                    "There is research to be done.\n" +
                    "On the people who are\n" +
                    "still alive.\n" +
                    "\n" +
                    "PS: And believe me I am\n" +
                    "still alive.\n" +
                    "PPS: I'm doing Science and I'm\n" +
                    "still alive.\n" +
                    "PPPS: I feel FANTASTIC and I'm\n" +
                    "still alive.\n" +
                    "\n" +
                    "FINAL THOUGHT:\n" +
                    "While you're dying I'll be\n" +
                    "still alive.\n" +
                    "\n" +
                    "FINAL THOUGHT PS:\n" +
                    "And when you're dead I will be\n" +
                    "still alive.\n" +
                    "\n" +
                    "STILL ALIVE\n" +
                    "\n" +
                    "Still alive.\n" +
                    "\n" +
                    "\n").getBytes();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private File file;

    @Before
    public void setUp() throws Exception {
        file = folder.newFile();
        new DataOutputStream(new FileOutputStream(file)).write(TEXT);
    }

    @Test
    public void execute() throws Exception {
        assertEquals("It's hard to overstate\n" + "over every mistake.",
                new Grep().execute(Arrays.asList("over", file.getPath())));
        assertEquals("over every mistake.", new Grep().execute(Arrays.asList("-w", "over", file.getPath())));
        assertEquals("It's hard to overstate\n" +
                "my satisfaction.\n" +
                "Aperture Science\n" +
                "over every mistake.\n" +
                "You just keep on trying\n" +
                "till you run out of cake.", new Grep().execute(Arrays.asList("-A", "2", "over", file.getPath())));
        assertEquals("still alive.\n" +
                "still alive.\n" +
                "still alive.\n" +
                "still alive.\n" +
                "still alive.\n" +
                "still alive.\n" +
                "still alive.\n" +
                "still alive.\n" +
                "STILL ALIVE\n" +
                "Still alive.", new Grep().execute(Arrays.asList("-i", "alive", file.getPath())));
        assertEquals("This was a triumph.\n" +
                "I'm making a note here:\n" +
                "HUGE SUCCESS.\n" +
                "And you make a neat gun.\n" +
                "For the people who are\n" +
                "And threw every piece into a fire.\n" +
                "As they burned it hurt because\n" +
                "make a beautiful line.\n" +
                "And we're out of beta.\n" +
                "THAT WAS A JOKE. HA HA. FAT CHANCE.\n" +
                "Anyway, this cake is great.",
                new Grep().execute(Arrays.asList("-i", "-w", "-A", "1", "a", file.getPath())));
    }

}