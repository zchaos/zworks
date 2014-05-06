package zchaos.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.jface.dialogs.MessageDialog;

import zchaos.plugins.dependencies.sort.actions.DependenciesSortAction;

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class SampleAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	private Menu menu;

	private IAction configAction;

	private IAction dependenciesSortAction;

	private List<IAction> actionList = new ArrayList<IAction>();

	/**
	 * The constructor.
	 */
	public SampleAction() {
	}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		// MessageDialog.openInformation(window.getShell(), "Zplugins",
		// "Hello, Eclipse world");
		int size = actionList.size();
		for (int i = 0; i < size; i++) {
			IAction ac = actionList.get(i);
			ac.run();
		}
	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
		// configAction = new ConfigurationAction("zchaos configurate");
		dependenciesSortAction = new DependenciesSortAction(
				"zchaos dependencies sort");

		// actionList.add(configAction);
		actionList.add(dependenciesSortAction);
	}

	public Menu getMenu(Control parent) {
		if (menu == null) {
			MenuManager mm = new MenuManager();
			menu = mm.createContextMenu(parent);

			int size = actionList.size();
			for (int i = 0; i < size; i++) {
				IAction ac = actionList.get(i);
				mm.add(ac);
			}
		}
		return menu;
	}
}