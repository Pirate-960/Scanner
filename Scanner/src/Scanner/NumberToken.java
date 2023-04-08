package Scanner;

public class NumberToken extends Token {
	
	private String Number;
	public NumberToken(String number) {
		super(TokenType.NUMBER);
		this.Number = number;
	}
	
	public String getNumber() {
		return Number;
	}
	public void setNumber(String number) {
		this.Number = number;
	}
	
	public static NumberToken fromString(String input) {
		return new NumberToken(input);
	}
	
	public boolean isIntegerLiteral() {
		// Regular expression for integer literals
		String IntegerRegex = "[-+e]?\\d+|[+-]?0[xX][\\da-fA-F]+|0[bB][01]+";
        return Number.matches(IntegerRegex);
	}
	
	public boolean isFloatingPointLiteral() {
        // Regular expression for floating-point literals		
        String FloatingPointRegex = "[-+e]?\\d*\\.\\d+([eE][-+e]?\\d+)?|[+-]?\\d+[eE][-+]?\\d+";
        return Number.matches(FloatingPointRegex);		
	}

	@Override
	public String toString() {
		return "NumberToken [Number=" + Number + "]";
	}
}

/*
*
*
*
*import java.util.ArrayList;
import java.util.List;

public class NumberLexer {
    private String input;
    private int currentPosition;

    public NumberLexer(String input) {
        this.input = input;
        this.currentPosition = 0;
    }

    private char peek() {
        if (currentPosition >= input.length()) {
            return '\0';
        }
        return input.charAt(currentPosition);
    }

    private char next() {
        char currentChar = peek();
        currentPosition++;
        return currentChar;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isHexDigit(char c) {
        return isDigit(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    private boolean isBinaryDigit(char c) {
        return c == '0' || c == '1';
    }

    private boolean isSign(char c) {
        return c == '+' || c == '-';
    }

    private boolean isExponent(char c) {
        return c == 'e' || c == 'E';
    }

    public List<Token> tokenize() throws LexerException {
        List<Token> tokens = new ArrayList<>();

        while (currentPosition < input.length()) {
            char currentChar = peek();
            if (isDigit(currentChar) || isSign(currentChar)) {
                boolean isDecimal = true;
                boolean isExponential = false;
                StringBuilder numberBuilder = new StringBuilder();
                numberBuilder.append(next());
                while (isDigit(peek()) || peek() == '.') {
                    if (peek() == '.') {
                        isDecimal = false;
                    } else if (isExponent(peek())) {
                        isExponential = true;
                        isDecimal = false;
                    }
                    numberBuilder.append(next());
                }
                if (isExponential) {
                    if (isSign(peek())) {
                        numberBuilder.append(next());
                        while (isDigit(peek())) {
                            numberBuilder.append(next());
                        }
                    } else {
                        throw new LexerException("Invalid number format");
                    }
                }
                if (isDecimal) {
                    tokens.add(new Token(TokenType.NUMBER, Double.parseDouble(numberBuilder.toString())));
                } else {
                    tokens.add(new Token(TokenType.NUMBER, Long.decode(numberBuilder.toString())));
                }
            } else if (currentChar == '0' && (peek(1) == 'x' || peek(1) == 'X')) {
                next();
                next();
                StringBuilder hexBuilder = new StringBuilder();
                while (isHexDigit(peek())) {
                    hexBuilder.append(next());
                }
                tokens.add(new Token(TokenType.NUMBER, Long.decode("0x" + hexBuilder.toString())));
            } else if (currentChar == '0' && (peek(1) == 'b' || peek(1) == 'B')) {
                next();
                next();
                StringBuilder binaryBuilder = new StringBuilder();
                while (isBinaryDigit(peek())) {
                    binaryBuilder.append(next());
                }
                tokens.add(new Token(TokenType.NUMBER, Long.parseLong(binaryBuilder.toString(), 2)));
            } else {
                throw new LexerException("Invalid input");
            }
        }

        return tokens;
    }

    private char peek(int offset) {
        int index = currentPosition + offset;
        if (index >= input.length()) {
            return '\0';
        }
        return input.charAt(index);
    }
}

*/