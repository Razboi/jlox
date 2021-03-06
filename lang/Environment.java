package jlox.lang;

import java.util.Map;
import java.util.HashMap;

class Environment {
	private final Environment enclosing;
	private final Map<String, Object> values = new HashMap<>();

	Environment() {
		this.enclosing = null;
	}

	Environment(Environment enclosing) {
		this.enclosing = enclosing;
	}

	public void define(String name, Object value) {
		values.put(name, value);
	}

	public void assign(Token name, Object value) {
		if (values.containsKey(name.lexeme)) {
			values.put(name.lexeme, value);
			return;
		}
		if (enclosing != null) {
			enclosing.assign(name, value);
			return;
		}
		throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
	}

	public Object get(Token name) {
		if (values.containsKey(name.lexeme)) {
			return values.get(name.lexeme);
		}
		if (enclosing != null) return enclosing.get(name);
		throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
	}
}
