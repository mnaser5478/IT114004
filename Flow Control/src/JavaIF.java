public class JavaIF {
	public static void main(String[] args) {
		System.out.println("Java [IT114-004-S21] Exercise [#1]");
		int xx = 50;
		int yy = 10;
		if (xx > yy) {
			System.out.println("Hello World");
		}
		System.out.println("Java [IT114-004-S21] Exercise [#2]");
		int xb = 50;
		int yb = 50;
		if (xb == yb) {
			System.out.println("Hello World");
		}
		System.out.println("Java [IT114-004-S21] Exercise [#3]");
		int xc = 50;
		int yc = 50;
		if (xc == yc) {
			System.out.println("Yes");
		} else {
			System.out.println("No");
		}
		System.out.println("Java [IT114-004-S21] Exercise [#4]");
		int x = 50;
		int y = 50;
		if (x == y) {
			System.out.println("1");
		} else if (x > y) {
			System.out.println("2");
		} else {
			System.out.println("3");
		}
		System.out.println("Java [IT114-004-S21] Exercise [#5]");
		int time = 20;
		String result = (time < 18) ? "Good day." : "Good evening.";
		System.out.println(result);
	}
}