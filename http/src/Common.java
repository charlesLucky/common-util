
public class Common {

	public static boolean isEmpty(String s) {
	    if (null == s || "".equals(s) || "".equals(s.trim()) || "null".equalsIgnoreCase(s)) {
            return true;
        } else {
            return false;
        }
	}

}
