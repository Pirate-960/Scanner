package Scanner;

public class BracketToken extends Token {
	public static final int LEFT_PAR = 1;
	public static final int RIGHT_PAR = 2;
	public static final int LEFT_SQUARE_B = 3;
	public static final int RIGHT_SQUARE_B = 4;
	public static final int LEFT_CURLY_B = 5;
	public static final int RIGHT_CURLY_B = 6;
	
	private TokenType type;
	public BracketToken(int type) {
		super(TokenType.BRACKET);
		this.type = type;
	}
	
	public TokenType getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		switch(type) {
        case LEFTPAR:
            return "(";
        case RIGHTPAR:
            return ")";
        case LEFTSQUAREB:
            return "[";
        case RIGHTSQUAREB:
            return "]";
        case LEFTCURLYB:
            return "{";
        case RIGHTCURLYB:
            return "}";
        default:
            return "";
		}
	}
}
