import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;

public class MainFrame extends JFrame implements ActionListener {

	private JLabel month_label, year_label;
	private JButton previous_button, next_button;
	private CalendarTable calendar_table;
	private JComboBox year_combobox;
	private Container container;
	private TableModel table_model; // Table model
	private JScrollPane scroll_pane; // The scrollpane
	private JPanel calendar_panel;
	private int expected_year, expected_month, expected_day, current_year, current_month;
	private Database database = new Database();
	private JPopupMenu popup;
	private String[] months_list = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December" };

	private int row;
	private int col;

	public MainFrame() {

		connectToDatabase();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (UnsupportedLookAndFeelException e) {
		}

		// Prepare frame
		setSize(330, 375); // Set size to 400x400 pixels
		container = getContentPane(); // Get content pane
		container.setLayout(null); // Apply null layout
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Close when X
																// is clicked

		// Create controls
		month_label = new JLabel("January");
		year_label = new JLabel("Change year:");
		year_combobox = new JComboBox();
		previous_button = new JButton("&lt;&lt;");
		next_button = new JButton("&gt;&gt;");
		previous_button.addActionListener(this);
		next_button.addActionListener(this);
		year_combobox.addActionListener(this);

		
		
		
		table_model = new TableModel();
		calendar_table = new CalendarTable(table_model);
		scroll_pane = new JScrollPane(calendar_table);
		calendar_panel = new JPanel(null);

		// Set border
		calendar_panel.setBorder(BorderFactory.createTitledBorder("Calendar"));

	
		
		popup = new JPopupMenu();
		JMenuItem addNewEvent = new JMenuItem("Add New Event");
		popup.add(addNewEvent);

		JMenuItem updateItem = new JMenuItem("View Event");
		popup.add(updateItem);

		JMenuItem removeItem = new JMenuItem("Delete Event");
		popup.add(removeItem);
		
		container.add(calendar_panel);
		calendar_panel.add(month_label);
		calendar_panel.add(year_label);
		calendar_panel.add(year_combobox);
		calendar_panel.add(previous_button);
		calendar_panel.add(next_button);
		calendar_panel.add(scroll_pane);

		previous_button.setActionCommand("previous_button");
		next_button.setActionCommand("next_button");
		year_combobox.setActionCommand("comboBox_button");
		// Set bounds
		calendar_panel.setBounds(0, 0, 320, 335);
		month_label.setBounds(160 - month_label.getPreferredSize().width / 2, 25, 100, 25);
		year_label.setBounds(10, 305, 80, 20);
		year_combobox.setBounds(230, 305, 80, 20);
		previous_button.setBounds(10, 25, 50, 25);
		next_button.setBounds(260, 25, 50, 25);
		scroll_pane.setBounds(10, 50, 300, 250);

		// Make frame visible
		setResizable(false);
		setVisible(true);

		// Get real month/year
		GregorianCalendar cal = new GregorianCalendar(); // Create calendar
		expected_day = cal.get(GregorianCalendar.DAY_OF_MONTH); // Get day
		expected_month = cal.get(GregorianCalendar.MONTH); // Get month
		expected_year = cal.get(GregorianCalendar.YEAR); // Get year
		current_month = expected_month; // Match month and year
		current_year = expected_year;

		// Add headers
		String[] headers = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" }; // All
																				// headers
		for (int i = 0; i < 7; i++) {
			table_model.addColumn(headers[i]);
		}

		table_model.setColumnCount(7);
		table_model.setRowCount(6);

		calendar_table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				int row = calendar_table.rowAtPoint(e.getPoint());
				int col = calendar_table.columnAtPoint(e.getPoint());
				setRow(row);
				setCol(col);
				calendar_table.getSelectionModel().setSelectionInterval(row, col);

				if (e.getButton() == MouseEvent.BUTTON3) {
					if (e.getButton() == MouseEvent.BUTTON3) {
						popup.show(calendar_table, e.getX(), e.getY());
					}
				}
			}
		});

		removeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int day = Integer.parseInt(calendar_table.getValueAt(getRow(), getCol()).toString());
				int year = Integer.parseInt(year_combobox.getSelectedItem().toString());
				int month = -1;
				for (int i = 0; i < months_list.length; i++) {
					if (months_list[i].equalsIgnoreCase(month_label.getText().toString())) {
						month = i;
					}
				}

				int action = JOptionPane.showConfirmDialog(MainFrame.this, "Do you really want to delete the event?",
						"Confirm Delete", JOptionPane.OK_CANCEL_OPTION);

				if (action == JOptionPane.OK_OPTION) {
					database.deleteEvent(day, month, year);
					revalidate();
					repaint();
				}

			}
		});

		addNewEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int day = Integer.parseInt(calendar_table.getValueAt(getRow(), getCol()).toString());
				int year = Integer.parseInt(year_combobox.getSelectedItem().toString());
				int month = -1;
				for (int i = 0; i < months_list.length; i++) {
					if (months_list[i].equalsIgnoreCase(month_label.getText().toString())) {
						month = i;
					}
				}

				Event event = new Event();
				event.setDay(day);
				event.setYear(year);
				event.setMonth(month);
				EventFrame frame = new EventFrame(event,database);

				frame.addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent e) {
						// TODO Auto-generated method stub
						super.windowClosing(e);
						revalidate();
						repaint();
					}

				});

			}
		});

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

				int action = JOptionPane.showConfirmDialog(MainFrame.this,
						"Do you really want to exit the application?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);

				if (action == JOptionPane.OK_OPTION) {
					dispose();
					windowClosed(e);
				}

			}

		});

		updateItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int day = Integer.parseInt(calendar_table.getValueAt(getRow(), getCol()).toString());
				int year = Integer.parseInt(year_combobox.getSelectedItem().toString());
				int month = -1;
				for (int i = 0; i < months_list.length; i++) {
					if (months_list[i].equalsIgnoreCase(month_label.getText().toString())) {
						month = i;
					}
				}
				Event event = null;
				try {
					event = database.retreiveEvent(day, month, year);
				} catch (Exception exception) {
					exception.printStackTrace();
				}

				
				if(event == null) {
					JOptionPane.showMessageDialog(MainFrame.this, "Sorry you don't have any event on this date");
				}else {
				EventFrame frame = new EventFrame(event,database);
				revalidate();
				repaint();
				}

			}
		});

		// Populate table
		for (int i = expected_year - 100; i <= expected_year + 100; i++) {
			year_combobox.addItem(String.valueOf(i));
		}

		// Refresh calendar
		refresh(expected_month, expected_year); // Refresh calendar
	}

	private void connectToDatabase() {
		// Look and feel

				try {
					database.configure(3306, "root", "khan");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					database.connect();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}		
	}

	public void refresh(int month, int year) {
		// Variables
		int nod, som; // Number Of Days, Start Of Month

		// Allow/disallow buttons
		previous_button.setEnabled(true);
		next_button.setEnabled(true);
		if (month == 0 && year <= expected_year - 10) {
			previous_button.setEnabled(false);
		} // Too early
		if (month == 11 && year >= expected_year + 100) {
			next_button.setEnabled(false);
		} // Too late
		month_label.setText(months_list[month]); // Refresh the month label (at the top)
		month_label.setBounds(160 - month_label.getPreferredSize().width / 2, 25, 180, 25); // Re-align
																						// label
																						// with
																						// calendar
		year_combobox.setSelectedItem(String.valueOf(year)); // Select the correct
														// year in the combo box

		// Clear table
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				table_model.setValueAt(null, i, j);
			}
		}

		// Get first day of month and number of days
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);

		// Draw calendar
		for (int i = 1; i <= nod; i++) {
			int row = new Integer((i + som - 2) / 7);
			int column = (i + som - 2) % 7;
			table_model.setValueAt(i, row, column);
		}

		// Apply renderers
		calendar_table.setDefaultRenderer(calendar_table.getColumnClass(0), new tblCalendarRenderer());
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getRow() {
		return row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getCol() {
		return col;
	}

	class tblCalendarRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, selected, focused, row, column);

			if (calendar_table.getValueAt(row, column) != null) {
				int day = Integer.parseInt(calendar_table.getValueAt(row, column).toString());
				String month = month_label.getText();
				int monthIndex = -1;
				for (int i = 0; i < months_list.length; i++) {
					String item = months_list[i];

					if (month.equals(item)) {
						monthIndex = i;
					}
				}
				int year = Integer.parseInt(year_combobox.getSelectedItem().toString());

				Event event = null;
				try {
					event = database.retreiveEvent(day, monthIndex, year);

					if (event != null) {
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (event != null) {
					if (event.getDay() == Integer.parseInt(calendar_table.getValueAt(row, column).toString())
							&& monthIndex == event.getMonth()
							&& year == Integer.parseInt(year_combobox.getSelectedItem().toString())) {
						setBackground(Color.GREEN);

					}
				} else {
					setBackground(new Color(255, 255, 255));
				}

				setBorder(null);
				setForeground(Color.black);
			}
			return this;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("previous_button")) {
			if (current_month == 0) { // Back one year
				current_month = 11;
				current_year -= 1;
			} else { // Back one month
				current_month -= 1;
			}
		} else if (command.equals("next_button")) {
			if (current_month == 11) { // Foward one year
				current_month = 0;
				current_year += 1;
			} else { // Foward one month
				current_month += 1;
			}

		} else if (command.equals("comboBox_button")) {
			if (year_combobox.getSelectedItem() != null) {
				String b = year_combobox.getSelectedItem().toString();
				current_year = Integer.parseInt(b);

			}
		}

		refresh(current_month, current_year);
	}

}
