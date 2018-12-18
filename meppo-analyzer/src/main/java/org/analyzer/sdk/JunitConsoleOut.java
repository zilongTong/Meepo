package org.analyzer.sdk;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 *
 * @author Leo
 * @version 2015-10-30
 */
public class JunitConsoleOut {
	
	public static void console(Object obj) {
		System.out.println("[jUnit] " + obj);
	}
	protected static void console() {
		console("");
	}
	protected static void console(String line, Object...objects) {
		FormattingTuple ft = MessageFormatter.arrayFormat(line, objects);
		console(ft.getMessage());
	}

	public static String consoleLine(String line, Object...objects) {
		FormattingTuple ft = MessageFormatter.arrayFormat(line, objects);
		return ft.getMessage();
	}
}
