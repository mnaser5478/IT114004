public class MyArrays {
	public static void main(String[] args) {
		System.out.println("Java [IT114-004-S21] Exercise [#1]");
		String[] cars = { "Volvo", "BMW", "Ford" };
		System.out.println("Java [IT114-004-S21] Exercise [#2]");
		String[] cars2 = { "Volvo", "BMW", "Ford" };
		System.out.println(cars2[1]);
		System.out.println("Java [IT114-004-S21] Exercise [#3]");
		String[] cars3 = { "Volvo", "BMW", "Ford" };
		cars3[0] = "Opel";
		System.out.println(cars3[0]);
		System.out.println("Java [IT114-004-S21] Exercise [#4]");
		String[] cars4 = { "Volvo", "BMW", "Ford" };
		System.out.println(cars4.length);
		System.out.println("Java [IT114-004-S21] Exercise [#5]");
		String[] cars5 = { "Volvo", "BMW", "Ford" };
		for (String i : cars5) {
			System.out.println(i);
		}
		System.out.println("Java [IT114-004-S21] Exercise [#6]");
		int[][] myNumbers = { { 1, 2, 3, 4 }, { 5, 6, 7 } };
	}
}