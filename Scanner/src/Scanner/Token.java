package Scanner;

public class Token {
	
	public enum TokenType {
		LEFTPAR, RIGHTPAR, LEFTSQUAREB, RIGHTSQUAREB, LEFTCURLYB, RIGHTCURLYB, // Bracket tokens
        NUMBER, BOOLEAN, CHAR, STRING, // Other tokens
        DEFINE, LET, COND, IF, BEGIN, // Keyword tokens
        IDENTIFIER, // Identifier token
        ERROR, 
        BRACKET, KEYWORD,
	}

	
	private TokenType type;
	private String Lexeme;
	private int line;
	private int position;
	
	public Token(TokenType type, String Lexeme) {
		this.type = type;
		this.Lexeme = Lexeme;
	}

	public Token(TokenType Token) {
	}

	public TokenType getType() {
		return type;
	}

	public void setType(TokenType type) {
		this.type = type;
	}

	public String getLexeme() {
		return Lexeme;
	}

	public void setLexeme(String lexeme) {
		Lexeme = lexeme;
	}

	@Override
	public String toString() {
		return "Token [type=" + type + ", Lexeme=" + Lexeme + ", line=" + line + ", position=" + position + "]";
	}

	public Token getNextToken() {
		return null;
	}
	
}
