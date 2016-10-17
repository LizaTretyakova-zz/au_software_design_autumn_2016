package Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class Parser {
    private static final String DELIMITER = " ";
    private static final char QUOTING_CHAR = '"';
    private static final String EMPTY_BUF = "";
    private static final char DEREF_VAR = '$';

    private static class ParseError {
        private IOException e = null;

        void setE(String msg) {
            e = e == null ? new IOException(msg) : e;
        }

        IOException getE() {
            return e;
        }
    }

    private static Parser instance = null;

    private Parser() {}

    public static Parser getInstance() {
        if(instance == null) {
            instance = new Parser();
        }
        return instance;
    }

    private List<String> tokenize(String line, ParseError e) {
        String[] raw = line.trim().split(DELIMITER);
        String buf = EMPTY_BUF;
        boolean accumulating = false;
        ArrayList<String> result = new ArrayList<>();

        for(String token: raw) {
            if(!accumulating) {
                if(token.charAt(0) == QUOTING_CHAR && token.charAt(token.length() - 1) != QUOTING_CHAR) {
                    accumulating = true;
                    buf = token; // Assuming, as discussed, that there are no such cases as `echo "abc def "123`.
                } else {
                    result.add(token);
                }
            } else {
                buf += DELIMITER + token;
                if(token.charAt(token.length() - 1) == QUOTING_CHAR) {
                    result.add(buf);
                    buf = EMPTY_BUF;
                    accumulating = false;
                }
            }
        }

        if (accumulating) {
            e.setE("Error while parsing: incorrect input.");
        }

        return result;
    }

    private String substitute(String token, HashMap<String, String> envVars, ParseError e) {
        if(token.charAt(0) == DEREF_VAR) {
            String variable = token.substring(1);
            if(!envVars.containsKey(variable)) {
                e.setE("Error while parsing: unknown variable.");
            } else {
                variable = envVars.get(variable);
            }
            return variable;
        }
        return token;
    }

    private String substituteInline(String token, HashMap<String, String> envVars, ParseError e) {
        if(token.charAt(0) == QUOTING_CHAR) {
            String rawToken = token.substring(1, token.length() - 1);
            return tokenize(rawToken, e).stream()
                        .map(subToken -> substitute(subToken, envVars, e))
                        .collect(Collectors.joining(DELIMITER));
        } else {
            return token;
        }
    }

    public List<String> parse(String line, HashMap<String, String> envVars) throws IOException {
        ParseError e = new ParseError();
        List<String> result = tokenize(line, e).stream()
                                        .flatMap(token -> tokenize(substitute(token, envVars, e), e).stream())
                                        .map(token -> substituteInline(token, envVars, e))
                                        .collect(Collectors.toList());


        if(e.getE() != null) {
            throw e.getE();
        }

        return result;
    }
}