public class JavaSwitch {
	public static void main(String[] args) {
		System.out.println("Java [IT114-004-S21] Exercise [#1]");
		int day = 2;
		switch (day) {
		case 1:
			System.out.println("Saturday");
			break;
		case 2:
			System.out.println("Sunday");
			break;
		}
		System.out.println("Java [IT114-004-S21] Exercise [#2]");
		int day1 = 4;
		switch (day1) {
		case 1:
			System.out.println("Saturday");
			break;
		case 2:
			System.out.println("Sunday");
			break;
		default:
			System.out.println("Weekend");
		}
	}
}