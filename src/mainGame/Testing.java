package mainGame;

public class Testing {

	public static void main(String[] args) {
		int[] range = new int[11];
		for(int i = 0; i < 100; i++) {
			range[(int) (GameApp.clampGaus()*5+5)]++;
		}
		for(int i = 0; i<11;i++)System.out.printf("%2d - %2d  %s%n",i,range[i], bar(range[i]));
	}
	private static String bar(int x) {
		String out = "";
		for(int i = 0; i < x; i++) out = out +"#";
		return out;
	}
}
