import java.io.FileNotFoundException;
import java.io.IOException;

import com.excilys.cdb.service.DataPresenter;
import com.excilys.cdb.service.SQLDataPresenter;
import com.excilys.cdb.ui.ConsoleUserInterface;
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
		DataPresenter dp = null;
		try {
			dp = new SQLDataPresenter();
			new ConsoleUserInterface(dp).mainLoop(args);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
