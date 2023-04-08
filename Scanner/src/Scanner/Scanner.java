package Scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Scanner.Token.TokenType;

public class Scanner {
	
	
	private String FileName;
	private BufferedReader Reader;
	private int CurrentLine = 1;
	private int CurrentColumn = 0;
	private char CurrentChar;
	private boolean EOF = false;
	
	
	public Scanner(String fileName) throws IOException {
		this.FileName = fileName;
		this.Reader = new BufferedReader(new FileReader(FileName));
		Advance();
	}
	
	public Scanner(BufferedReader bufferReader) {
		
	}

	private void Advance() throws IOException {
		int Next_Char = Reader.read();
		
		if (Next_Char == -1) {
			EOF = true;
		} else {
			CurrentChar = (char)Next_Char;
			CurrentColumn++;
			
			if (CurrentChar == '\n') {
				CurrentLine++;
				CurrentColumn++;
			}
		}
	}
	
	
	private void Skip_WhiteSpaces_And_Comments() throws IOException {
		while (!EOF && (Character.isWhitespace(CurrentChar) || CurrentChar == '~')) {
			if (CurrentChar == '~') {
				do {
					Advance();
				} while (!EOF && CurrentChar != '\n');
			}
			Advance();
		}
	}
	
	
	private String Get_Lexeme() throws InvalidTokenException, IOException {
		Token Token = null;
		if (!EOF) {
			if (Token == new Token(TokenType.BRACKET, FileName)) {
				BracketToken(FileName);
			} else if (Token == new Token(TokenType.NUMBER, FileName)) {
				NumberToken(FileName);
			} else if (Token == new Token(TokenType.BOOLEAN, FileName)) {
				BooleanToken(FileName);
			} else if (Token == new Token(TokenType.CHAR, FileName)) {
				CharacterToken(FileName);
			} else if (Token == new Token(TokenType.STRING)) {
				StringToken(FileName);
			} else if (Token == new Token(TokenType.KEYWORD, FileName)) {
				KeywordToken(FileName);
			} else if (Token == new Token(TokenType.IDENTIFIER, FileName)) {
				IdentifierToken(FileName);
			} else if (Token == new Token(null, FileName)) {
				
			}
		}

		//Token Token = new Token(null, FileName);
		return FileName + ":" + CurrentLine + ":" + (CurrentColumn - 1);
	}
	
	
	public Token BracketToken(String inputString) throws InvalidTokenException, IOException {
	    char c = getCurrentChar();
	    if (c == '(') {
	        Advance();
	        return new Token(TokenType.LEFTPAR, "(");
	    } else if (c == ')') {
	        Advance();
	        return new Token(TokenType.RIGHTPAR, ")");
	    } else if (c == '[') {
	        Advance();
	        return new Token(TokenType.LEFTSQUAREB, "[");
	    } else if (c == ']') {
	        Advance();
	        return new Token(TokenType.RIGHTSQUAREB, "]");
	    } else if (c == '{') {
	        Advance();
	        return new Token(TokenType.LEFTCURLYB, "{");
	    } else if (c == '}') {
	        Advance();
	        return new Token(TokenType.RIGHTCURLYB, "}");
	    } else {
	        throw new InvalidTokenException("Invalid bracket character: " + c);
	    }
	}

	
	public static Token NumberToken(String inputString) throws InvalidTokenException {
	    StringBuilder numberString = new StringBuilder();
	    boolean hasDecimal = false;
	    boolean hasExponent = false;

	    for (int i = 0; i < inputString.length(); i++) {
	        char currentChar = inputString.charAt(i);

	        if (Character.isDigit(currentChar)) {
	            numberString.append(currentChar);
	        } else if (currentChar == '+' || currentChar == '-') {
	            if (i == 0 || inputString.charAt(i-1) == 'e' || inputString.charAt(i-1) == 'E') {
	                numberString.append(currentChar);
	            } else {
	                throw new InvalidTokenException("Invalid number literal");
	            }
	        } else if (currentChar == '.') {
	            if (!hasDecimal && !hasExponent) {
	                numberString.append(currentChar);
	                hasDecimal = true;
	            } else {
	                throw new InvalidTokenException("Invalid number literal");
	            }
	        } else if (currentChar == 'e' || currentChar == 'E') {
	            if (!hasExponent) {
	                numberString.append(currentChar);
	                hasExponent = true;
	            } else {
	                throw new InvalidTokenException("Invalid number literal");
	            }
	        } else {
	            throw new InvalidTokenException("Invalid character in number literal");
	        }
	    }

	    return new Token(TokenType.NUMBER, numberString.toString());
	}
	
	public static Token BooleanToken(String inputString) throws InvalidTokenException {
	    if (inputString.equals("true") || inputString.equals("false")) {
	        return new Token(TokenType.BOOLEAN, inputString);
	    } else {
	        throw new InvalidTokenException("Invalid boolean literal");
	    }
	}
	
	
	public static Token CharacterToken(String inputString) throws InvalidTokenException {
	    if (inputString.length() == 3 && inputString.charAt(0) == '\'' && inputString.charAt(2) == '\'') {
	        char c = inputString.charAt(1);
	        if (c == '\\') {
	            throw new InvalidTokenException("Invalid character literal");
	        } else {
	            return new Token(TokenType.CHAR, String.valueOf(c));
	        }
	    } else {
	        throw new InvalidTokenException("Invalid character literal");
	    }
	}
	
	
	public static Token StringToken(String inputString) throws InvalidTokenException {
	    if (inputString.length() >= 2 && inputString.charAt(0) == '\"' && inputString.charAt(inputString.length() - 1) == '\"') {
	        StringBuilder sb = new StringBuilder();
	        for (int i = 1; i < inputString.length() - 1; i++) {
	            char c = inputString.charAt(i);
	            if (c == '\\') {
	                i++;
	                if (i >= inputString.length() - 1) {
	                    throw new InvalidTokenException("Invalid string literal");
	                }
	                char escape = inputString.charAt(i);
	                if (escape != '\\' && escape != '\"') {
	                    throw new InvalidTokenException("Invalid string literal");
	                }
	                sb.append(escape);
	            } else {
	                sb.append(c);
	            }
	        }
	        return new Token(TokenType.STRING, sb.toString());
	    } else {
	        throw new InvalidTokenException("Invalid string literal");
	    }
	}
	
	public static Token KeywordToken(String inputString) {
	    Map<String, TokenType> keywords = new HashMap<>();
	    keywords.put("define", TokenType.DEFINE);
	    keywords.put("let", TokenType.LET);
	    keywords.put("cond", TokenType.COND);
	    keywords.put("if", TokenType.IF);
	    keywords.put("begin", TokenType.BEGIN);

	    TokenType tokenType = keywords.get(inputString);
	    if (tokenType != null) {
	        return new Token(tokenType, inputString);
	    } else {
	        return null;
	    }
	}
	/*
	public Token identifierToken() {
	    StringBuilder lexeme = new StringBuilder();
	    char c = input.peek();

	    // Check if the first character is a valid starting character for an identifier
	    if (!isValidIdentifierStartingChar(c)) {
	        return null;
	    }

	    // Add all subsequent characters that are part of the identifier
	    while (!input.isEmpty() && isValidIdentifierChar(input.peek())) {
	        lexeme.append(input.next());
	    }

	    // Check if the identifier is a keyword
	    String identifierString = lexeme.toString();
	    if (isKeyword(identifierString)) {
	        return new Token(keywordTokenType(identifierString), identifierString);
	    }

	    // Otherwise, it is a regular identifier token
	    return new Token(TokenType.IDENTIFIER, identifierString);
	}
	*/
	
	private Token IdentifierToken(String FileName) {
	    StringBuilder lexeme = new StringBuilder();
	    char c = input.peek();

	    // Check if the first character is a valid starting character for an identifier
	    if (!isValidIdentifierStartingChar(c)) {
	        return null;
	    }

	    // Add all subsequent characters that are part of the identifier
	    while (!input.isEmpty() && isValidIdentifierChar(input.peek())) {
	        lexeme.append(input.next());
	    }

	    // Check if the identifier is a keyword
	    String identifierString = lexeme.toString();
	    if (isKeyword(identifierString)) {
	        return new Token(keywordTokenType(identifierString), identifierString);
	    }

	    // Otherwise, it is a regular identifier token
	    return new Token(TokenType.IDENTIFIER, identifierString);
	}

	private boolean isValidIdentifierStartingChar(char c) {
	    return (c >= 'a' && c <= 'z') || (c == '!' || c == '*' || c == '/' || c == ':' || c == '<' || c == '=' || c == '>' || c == '?');
	}

	private boolean isValidIdentifierChar(char c) {
	    return isValidIdentifierStartingChar(c) || (c >= '0' && c <= '9') || c == '.' || c == '+' || c == '-';
	}

	private boolean isKeyword(String identifierString) {
	    switch (identifierString) {
	        case "define":
	            return true;
	        case "let":
	            return true;
	        case "cond":
	            return true;
	        case "if":
	            return true;
	        case "begin":
	            return true;
	        default:
	            return false;
	    }
	}

	private TokenType keywordTokenType(String keywordString) {
	    switch (keywordString) {
	        case "define":
	            return TokenType.DEFINE;
	        case "let":
	            return TokenType.LET;
	        case "cond":
	            return TokenType.COND;
	        case "if":
	            return TokenType.IF;
	        case "begin":
	            return TokenType.BEGIN;
	        default:
	            throw new IllegalArgumentException("Invalid keyword string: " + keywordString);
	    }
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public BufferedReader getReader() {
		return Reader;
	}

	public void setReader(BufferedReader reader) {
		Reader = reader;
	}

	public int getCurrentLine() {
		return CurrentLine;
	}

	public void setCurrentLine(int currentLine) {
		CurrentLine = currentLine;
	}

	public int getCurrentColumn() {
		return CurrentColumn;
	}

	public void setCurrentColumn(int currentColumn) {
		CurrentColumn = currentColumn;
	}

	public char getCurrentChar() {
		return CurrentChar;
	}

	public void setCurrentChar(char currentChar) {
		CurrentChar = currentChar;
	}

	public boolean isEOF() {
		return EOF;
	}

	public void setEOF(boolean eOF) {
		EOF = eOF;
	}

	public Token getNextToken() {
		return null;
	}
	
	
	
	
}















/*
 *
 *     private final BufferedReader reader;
    private final Pattern whitespacePattern = Pattern.compile("\\s+");
    private final Pattern identifierPattern = Pattern.compile("((\\.|-|\\+)|(!|\\*|/|:|<|=|>|\\?)|\\p{L})(\\p{L}|\\d|(\\.|-|\\+))*");
    private final Pattern keywordPattern = Pattern.compile("(define|let|cond|if|begin)\\b");
    private final Pattern numberPattern = Pattern.compile("(-|\\+|e)?(0x[\\da-fA-F]+|0b[01]+|\\d+(\\.\\d+)?([eE](-|\\+)?\\d+)?|\\.\\d+([eE](-|\\+)?\\d+)?)");
    private final Pattern booleanPattern = Pattern.compile("true|false");
    private final Pattern characterPattern = Pattern.compile("'(\\\\.|[^'\\\\])'");
    private final Pattern stringPattern = Pattern.compile("\"(\\\\.|[^\"])*\"");
    private final Pattern bracketPattern = Pattern.compile("[()\\[\\]{}]");
    private List<Token> TokenList = null;
    private int index;

    public Scanner(Reader reader) {
        this.reader = new BufferedReader(reader);
        this.TokenList = new ArrayList<>();
        this.index = 0;
    }

    public List<Token> scan() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            scanLine(line);
        }
        return TokenList;
    }

    private void scanLine(String line) {
        String[] tokens = whitespacePattern.split(line);
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            if (isIdentifier(token)) {
                TokenList.add(new IdentifierToken(token));
            } else if (isKeyword(token)) {
                TokenList.add(new KeywordToken(token));
            } else if (isNumber(token)) {
                TokenList.add(new NumberToken(token));
            } else if (isBoolean(token)) {
                TokenList.add(new BooleanToken(token));
            } else if (isCharacter(token)) {
                TokenList.add(new CharacterToken(token));
            } else if (isString(token)) {
                TokenList.add(new StringToken(token));
            } else if (isBracket(token)) {
                TokenList.add(new BracketToken(token));
            } else {
                // invalid token
            }
        }
    }
    
    private boolean isIdentifier(String token) {
        Matcher matcher = identifierPattern.matcher(token);
        return matcher.matches();
    }

    private boolean isKeyword(String token) {
        Matcher matcher = keywordPattern.matcher(token);
        return matcher.matches();
    }

    private boolean isNumber(String token) {
        Matcher matcher = numberPattern.matcher(token);
        return matcher.matches();
    }

    private boolean isBoolean(String token) {
        Matcher matcher = booleanPattern.matcher(token);
        return matcher.matches();
    }

    private boolean isCharacter(String token) {
        Matcher matcher = characterPattern.matcher(token);
        return matcher.matches();
    }

    private boolean isString(String token) {
        Matcher matcher = stringPattern.matcher(token);
        return matcher.matches();
    }

    private boolean isBracket(String token) {
        Matcher matcher = bracketPattern.matcher(token);
        return matcher.matches();
    }

	public BufferedReader getReader() {
		return reader;
	}
	
	public Pattern getWhitespacePattern() {
		return whitespacePattern;
	}

	public Pattern getIdentifierPattern() {
		return identifierPattern;
	}

	public Pattern getKeywordPattern() {
		return keywordPattern;
	}

	public Pattern getNumberPattern() {
		return numberPattern;
	}

	public Pattern getBooleanPattern() {
		return booleanPattern;
	}

	public Pattern getCharacterPattern() {
		return characterPattern;
	}

	public Pattern getStringPattern() {
		return stringPattern;
	}

	public Pattern getBracketPattern() {
		return bracketPattern;
	}

	public List<Token> getTokenList() {
		return TokenList;
	}

	public void setTokenList(List<Token> tokenList) {
		this.TokenList = tokenList;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "Scanner [reader=" + reader + ", whitespacePattern=" + whitespacePattern + ", identifierPattern="
				+ identifierPattern + ", keywordPattern=" + keywordPattern + ", numberPattern=" + numberPattern
				+ ", booleanPattern=" + booleanPattern + ", characterPattern=" + characterPattern + ", stringPattern="
				+ stringPattern + ", bracketPattern=" + bracketPattern + ", tokenList=" + TokenList + ", index=" + index
				+ "]";
	}
////////////////////////
	public Token getNextToken() {
		// TODO Auto-generated method stub
		return null;
	}
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * --------------------------------------------------------------
import java.io.*;
import java.util.*;

public class Scanner {
	
	private final String FileName;
	private final BufferedReader Reader;
	private int CurrentLine = 1;
	private int CurrentColumn = 0;
	private char CurrentChar;
	private boolean EOF = false;
	
	
	public Scanner(String fileName) throws IOException {
		this.FileName = fileName;
		this.Reader = new BufferedReader(new FileReader(FileName));
		Advance();
	}
	
	private void Advance() throws IOException {
		int Next_Char = Reader.read();
		
		if (Next_Char == -1) {
			EOF = true;
		} else {
			CurrentChar = (char)Next_Char;
			CurrentColumn++;
			
			if (CurrentChar == '\n') {
				CurrentLine++;
				CurrentColumn++;
			}
		}
	}
	
	
	private void Skip_WhiteSpaces_And_Comments() throws IOException {
		while (!EOF && (Character.isWhitespace(CurrentChar) || CurrentChar == '~')) {
			if (CurrentChar == '~') {
				do {
					Advance();
				} while (!EOF && CurrentChar != '\n');
			}
			Advance();
		}
	}
	
	
	private String Get_Lexeme() {
		return FileName + ":" + CurrentLine + ":" + (CurrentColumn - 1);
	}
	
	
	private Token Brackets(char c, int line, int position) throws IOException {
	    switch (c) {
	    case '(':
            return new Token(Token.TokenType.LEFTPAR, "(", line, position);
        case ')':
            return new Token(Token.TokenType.RIGHTPAR, ")", line, position);
        case '[':
            return new Token(Token.TokenType.LEFTSQUAREB, "[", line, position);
        case ']':
            return new Token(Token.TokenType.RIGHTSQUAREB, "]", line, position);
        case '{':
            return new Token(Token.TokenType.LEFTCURLYB, "{", line, position);
        case '}':
            return new Token(Token.TokenType.RIGHTCURLYB, "}", line, position);
        default:
            return null;
		}
	}
	
	private Token Number_Literals() throws IOException {
		return null;
		// EOF Check -> Token -> Number
	}
	
	private Token Boolean_Literals() throws IOException {
		return null;
		// EOF Check -> Token -> Boolean (1 || 2) 
	}
	
	private Token Character_Literals() throws IOException {
		return null;
		// EOF Check -> Token -> Character
	}
	
	private Token String_Literals() throws IOException {
		return null;
		// EOF Check -> Token -> String
	}
	
	private Token Keywords() throws IOException {
		return null;
		// EOF Check -> Token -> Keyword
	}
	
	private Token Identifiers() throws IOException {
		return null;
		// EOF Check -> Token -> Identifier
	}
	
}
 */


