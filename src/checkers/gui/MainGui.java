package checkers.gui;

import org.eclipse.swt.widgets.*;

public class MainGui {

	
	
	public MainGui(){
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.open();
		
		while (!shell.isDisposed()) {
		    // read the next OS event queue and transfer it to a SWT event 
		  if (!display.readAndDispatch())
		   {
		  // if there are currently no other OS event to process
		  // sleep until the next OS event is available 
		    display.sleep();
		   }
		}

	}
}
