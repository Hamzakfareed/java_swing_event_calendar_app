import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class CalendarTable extends JTable {

	public CalendarTable(DefaultTableModel model) {
		super(model);
		//this.getParent().setBackground(getBackground()); // Set
		// background

		// No resize/reorder
		getTableHeader().setResizingAllowed(false);
		getTableHeader().setReorderingAllowed(false);

		// Single cell selection
		setColumnSelectionAllowed(true);
		setRowSelectionAllowed(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Set row/column count
		setRowHeight(38);
	}
}
