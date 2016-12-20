package Utils;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseCorrectInput() throws Exception {
        Parser parser = Parser.getInstance();
        HashMap<String, String> env = new HashMap<>();
        env.put("x", "Meow");

        List<String> parsed = parser.parse("echo $x | echo", env);
        parsed.forEach(System.out::println);
        assertArrayEquals(new Object[]{"echo", "Meow", "|", "echo"}, parsed.toArray());

        parsed = parser.parse("echo \"$x\" | echo", env);
        parsed.forEach(System.out::println);
        assertArrayEquals(new Object[]{"echo", "Meow", "|", "echo"}, parsed.toArray());

        env.put("y", "$x");
        parsed = parser.parse("echo \"$x $y\" | echo", env);
        parsed.forEach(System.out::println);
        assertArrayEquals(new Object[]{"echo", "Meow $x", "|", "echo"}, parsed.toArray());
    }

    @Test(expected = IOException.class)
    public void testParseIncorrectInput() throws Exception {
        Parser parser = Parser.getInstance();
        HashMap<String, String> env = new HashMap<>();
        env.put("x", "Meow");
        parser.parse("echo \"$y\" | echo", env).forEach(System.out::println);
    }
}