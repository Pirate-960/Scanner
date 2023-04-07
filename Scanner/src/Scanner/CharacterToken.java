package Scanner;

public class CharacterToken extends Token {
	private char value;
	public CharacterToken(char value) {
		super(TokenType.CHAR);
		this.value = value;
	}
	
	public char getValue() {
		return value;
	}

	public void setValue(char value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "CharacterToken [value=" + value + "]";
	}
	
}
