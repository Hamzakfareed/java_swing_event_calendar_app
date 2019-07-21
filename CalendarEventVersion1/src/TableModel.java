import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel{
		public boolean isCellEditable(int rowIndex, int mColIndex) {
			return false;
	}
}
