package Scanner;

public class StringToken extends Token {
	private String value;
	
	public StringToken(String value) {
		super(TokenType.STRING);
		this.value = value;
	}
	
	
	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	@Override
	public String toString() {
		return "StringToken [value=" + value + "]";
	}
}
