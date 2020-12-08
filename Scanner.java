package jlox;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class Scanner {
	private static final Map<String, TokenType> keywords;
	private final String source;
	private final List<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int line = 1;

	static {
		keywords = new HashMap<>();
		keywords.put("and", TokenType.AND);
		keywords.put("class", TokenType.CLASS);
		keywords.put("else", TokenType.ELSE);
		keywords.put("false", TokenType.FALSE);
		keywords.put("for", TokenType.FOR);
		keywords.put("fun", TokenType.FUN);
		keywords.put("if", TokenType.IF);
		keywords.put("nil", TokenType.NIL);
		keywords.put("or", TokenType.OR);
		keywords.put("print", TokenType.PRINT);
		keywords.put("return", TokenType.RETURN);
		keywords.put("super", TokenType.SUPER);
		keywords.put("this", TokenType.THIS);
		keywords.put("true", TokenType.TRUE);
		keywords.put("var", TokenType.VAR);
		keywords.put("while", TokenType.WHILE);
	}


	Scanner(String source) {
		this.source = source;
	}

	public List<Token> scanTokens() {
		while (!isAtEnd()) {
			this.start = this.current;
			scanToken();
		}
		this.tokens.add(new Token(TokenType.EOF, "", null, this.line));
		return tokens;
	}

	private void scanToken() {
		char c = this.source.charAt(this.current);
		this.current++;
		switch (c) {
			case '(':
				addToken(TokenType.LEFT_PAREN, null);
				break;
			case ')':
				addToken(TokenType.RIGHT_PAREN, null);
				break;
			case '{':
				addToken(TokenType.LEFT_BRACE, null);
				break;
			case '}':
				addToken(TokenType.RIGHT_BRACE, null);
				break;
			case ',':
				addToken(TokenType.COMMA, null);
				break;
			case '.':
				addToken(TokenType.DOT, null);
				break;
			case '-':
				addToken(TokenType.MINUS, null);
				break;
			case '+':
				addToken(TokenType.PLUS, null);
				break;
			case ';':
				addToken(TokenType.SEMICOLON, null);
				break;
			case '*':
				addToken(TokenType.STAR, null);

			case '!':
				addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG, null);
				break;
			case '=':
				addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL, null);
				break;
			case '<':
				addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS, null);
				break;
			case '>':
				addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER, null);
				break;
			case '/':
				if (match('/')) {
					skipRestOfLine();
				} else if (match('*')) {
					skipBlockComment();
				} else {
					addToken(TokenType.SLASH, null);
				}
				break;
			case ' ':
			case '\r':
			case '\t':
				break;
			case '\n':
				line++;
				break;
			case '"':
				parseString();
				break;
			default:
				if (isDigit(c)) {
					parseNumber();
				} else if (isAlpha(c)) {
					parseIdentifier();
				} else {
					Lox.error(line, "Unexpected character.");
				}
				break;
		}
	}

	private void addToken(TokenType type, Object literal) {
		String text = this.source.substring(this.start, this.current);
		tokens.add(new Token(type, text, literal, this.line));
	}

	private void skipRestOfLine() {
		while (peek() != '\n' && !isAtEnd()) {
			this.current++;
		}
	}

	private void skipBlockComment() {
		while (peek() != '*' && peekNext() != '/' && !isAtEnd()) {
			if (peek() == '\n') {
				this.line++;
			}
			this.current++;
		}
		if (!this.isAtEnd()) {
			this.current += 2;
		}
	}


	private void skipRestOfDigits() {
		while (isDigit(this.source.charAt(this.current))) {
			this.current++;
		}
	}

	private void parseNumber() {
		skipRestOfDigits();
		if (peek() == '.' && isDigit(peekNext())) {
			this.current++;
			skipRestOfDigits();
		}
		addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
	}

	private void parseString() {
		while (peek() != '"' && !isAtEnd()) {
			if (peek() == '\n') {
				line++;
			}
			this.current++;
		}
		if (this.current >= this.source.length()){
			Lox.error(line, "Unterminated string.");
			return;
		}
		this.current++;
		// +1 and -1 to trim quotes
		String value = this.source.substring(this.start + 1, this.current - 1);
		addToken(TokenType.STRING, value);
	}

	private void parseIdentifier() {
		while (isAlphaNumeric(peek())) {
			this.current++;
		}
		String text = this.source.substring(this.start, this.current);
		TokenType type = this.keywords.get(text);
		if (type == null) {
			type = TokenType.IDENTIFIER;
		}
		addToken(type, null);
	}

	private boolean isAlpha(char c) {
		Pattern pattern = Pattern.compile("[a-zA-Z]");
		Matcher matcher = pattern.matcher(String.valueOf(c));
		return matcher.find();
	}

	private boolean isDigit(char c) {
		Pattern pattern = Pattern.compile("[0-9]");
		Matcher matcher = pattern.matcher(String.valueOf(c));
		return matcher.find();
	}

	private boolean isAlphaNumeric(char c) {
		return isAlpha(c) || isDigit(c);
	}

	private boolean isAtEnd() {
		return this.current >= this.source.length();
	}

	private char peek() {
		if (isAtEnd()) return '\0';
		return this.source.charAt(this.current);
	}
	
	private char peekNext() {
		if (this.current + 1 >= this.source.length()) return '\0';
		return this.source.charAt(this.current + 1);
	}

	private boolean match(char expected) {
		if (isAtEnd() || this.source.charAt(this.current) != expected) {
			return false;
		}
		current++;
		return true;
	}
}
