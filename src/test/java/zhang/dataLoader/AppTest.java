package zhang.dataLoader;

/**
 * Unit test for simple App.
 */
public class AppTest {
	
	public static void main(String args[]){
		String x = "\\\\n";
		x=x.replace("\\\\n", "<br>");
		System.out.println(x);
	}
}
