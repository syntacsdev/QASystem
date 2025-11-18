package application.util;

public class LogUtil {

	public static final boolean DO_DEBUG_LOGS = true;

	public static void info(String s) {
		System.out.println("[INFO] " + s);
	}

	public static void debug(String s) {
		if (!LogUtil.DO_DEBUG_LOGS)
			return;
		System.out.println("[DEBUG] " + s);
	}

	public static void error(String s) {
		System.err.println("[ERROR] " + s);
	}
}
