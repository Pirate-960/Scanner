package Scanner;

public class IdentifierToken extends Token {
	private String Identifier;
	
	public IdentifierToken(String Identifier) {
		super(TokenType.IDENTIFIER);
		this.Identifier = Identifier;
	}

	public String getIdentifier() {
		return Identifier;
	}

	public void setIdentifier(String identifier) {
		Identifier = identifier;
	}
	
	public static boolean isIdentifier(String Input) {
		return Input.matches("^(([.+\\-])|([!*:/<=>?a-z]))([a-z0-9.+\\-]*)$");
	}
}
