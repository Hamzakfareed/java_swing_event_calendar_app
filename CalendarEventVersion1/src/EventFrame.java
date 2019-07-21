import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;


public class EventFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JLabel nameLabel;
	private JComboBox<String> eventTypeComboBox;
	private JLabel descriptionLabel;
	private JTextField nameField;
	private JTextArea descriptionField;
	private JButton okBtn;
	private FormListener formListener;
	private Event event;
	private JPopupMenu popup;
	private Database database;
	private JPanel panel;

	public EventFrame() {
		setVisible(true);
		setSize(new Dimension(400, 600));

	}

	public EventFrame(Event event, Database database) {
		this();
		
		this.event = event;
		this.database = database;
		panel = new JPanel();
		Dimension dim = getPreferredSize();
		dim.width = 400;
		panel.setPreferredSize(dim);
		panel.setMinimumSize(dim);

		nameLabel = new JLabel("Name: ");
		descriptionLabel = new JLabel("Description: ");

		nameField = new JTextField(30);
		descriptionField = new JTextArea(10, 30);
		eventTypeComboBox = new JComboBox<String>();
		eventTypeComboBox = new JComboBox<String>();
		DefaultComboBoxModel<String> eventModel = new DefaultComboBoxModel<String>();
		eventModel.addElement("Anivarsay");
		eventModel.addElement("Birthday");
		eventModel.addElement("Meeting");
		eventModel.addElement("Appointment");
		eventTypeComboBox.setModel(eventModel);
		eventTypeComboBox.setSelectedIndex(0);

		eventTypeComboBox.setEditable(true);

		if (event.getName() != null && event.getName().length() > 0) {
			nameField.setText(event.getName());
		}

		if (event.getDescription() != null && event.getDescription().length() > 0) {
			descriptionField.setText(event.getDescription());
		}
		if(event.getEventType() !=null) {
			eventTypeComboBox.setSelectedItem(event.getEventType());
		}

		okBtn = new JButton("OK");

		// Set up mnemomics
		okBtn.setMnemonic(KeyEvent.VK_O);

		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String eventType = (String) eventTypeComboBox.getSelectedItem();
				String name = nameField.getText();
				String description = descriptionField.getText();
				event.setEventType(eventType);
				event.setName(name);
				event.setDescription(description);
				try {
					database.save(event);
					
					JOptionPane.showMessageDialog(EventFrame.this,
							"Event successfully added.", "Event Saved",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(EventFrame.this,
							"Unable to saved the event into database.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		

		// set broder for form panel and give it title Add product
		Border innerBorder = BorderFactory.createTitledBorder("Add Event");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		panel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		layoutComponents();

		add(panel);
	}

	// set layout grid baglayout and labels and fields into panel
	public void layoutComponents() {

		panel.setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();

		// ////////// First row ///////////////////////////////////

		gc.gridy = 0;

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		panel.add(nameLabel, gc);

		gc.gridx = 1;
		gc.gridy = 0;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		panel.add(nameField, gc);

		// //////////Second row ///////////////////////////////////

		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		panel.add(descriptionLabel, gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		panel.add(descriptionField, gc);

		// //////////Next row ///////////////////////////////////

		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 0.2;

		gc.gridx = 0;
		gc.insets = new Insets(0, 0, 0, 5);
		gc.anchor = GridBagConstraints.LINE_END;
		panel.add(new JLabel("Event Type"), gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		panel.add(eventTypeComboBox, gc);

		// //////////Next row ///////////////////////////////////

		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 2.0;

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 0, 0, 0);
		panel.add(okBtn, gc);

	}

	public void setFormListener(FormListener listener) {
		this.formListener = listener;
	}

}
