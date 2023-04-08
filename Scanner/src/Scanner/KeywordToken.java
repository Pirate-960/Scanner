package Scanner;

public class KeywordToken extends Token {
	private String keyword;
	
	public KeywordToken(String keyword) {
		super(TokenType.KEYWORD);
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return "KeywordToken [keyword=" + keyword + "]";
	}

}
