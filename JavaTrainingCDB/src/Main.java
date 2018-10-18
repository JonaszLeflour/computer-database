import ui.ConsoleUserInterface;
import service.SQLDataPresenter;
/**
 * Program main entry point
 * @author Jonasz Leflour
 * @version %I%
 *
 */
public class Main {

	/**
	 * Creates user interface object
	 * @param args
	 */
	public static void main(String[] args) {
		new ConsoleUserInterface(new SQLDataPresenter()).mainLoop(args);
	}
}
