package Scanner;

public class BooleanToken {
	private boolean Boolean;
	public BooleanToken(boolean Boolean) {
		super(TokenType.BOOLEAN);
		this.Boolean = Boolean;
		
	}
	public boolean isValue() {
		return Boolean;
	}
	public void setValue(boolean Boolean) {
		this.Boolean = Boolean;
	}
	@Override
	public String toString() {
		return "BooleanToken [Boolean=" + Boolean + "]";
	}
/*
 *  public String toString() {
        return String.format("<%s, %s>", type, value);
    }
 */
}
