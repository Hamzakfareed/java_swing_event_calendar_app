
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	private Connection con;

	private int port;
	private String user;
	private String password;

	public Database() {
	}

	public void configure(int port, String user, String password) throws Exception {
		this.port = port;
		this.user = user;
		this.password = password;

		if (con != null) {
			disconnect();
			connect();
		}
	}

	public void connect() throws Exception {

		if (con != null)
			return;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new Exception("Driver not found");
		}

		String url = String.format("jdbc:mysql://localhost:3306/Calendar", port);
		con = DriverManager.getConnection(url, user, password);
	}

	// "g+64vethU8%hfg"

	public void disconnect() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("Can't close connection");
			}
		}

		con = null;
	}

	public void save(Event event) throws SQLException {
		if (event.getId() > 0) {
			update(event);
			return;
		}
		String insertSql = "insert into Event (name, description, day, month,year,event_type) values (?, ?,  ?, ?, ?,?)";
		PreparedStatement insertStatement = con.prepareStatement(insertSql);


		String name = event.getName();
		String description = event.getDescription();
		int day = event.getDay();
		int month = event.getMonth();
		int year = event.getYear();
		String type = event.getEventType();

		int col = 1;

		insertStatement.setString(col++, name);
		insertStatement.setString(col++, description);
		insertStatement.setInt(col++, day);
		insertStatement.setDouble(col++, month);
		insertStatement.setInt(col++, year);
		insertStatement.setString(col++,type);

		insertStatement.executeUpdate();

		insertStatement.close();
	}

	public void update(Event event) throws SQLException {

		String updateSql = "update event set name=?, description=?, day=?, month=?, year=? , event_type =? where id=?";
		PreparedStatement updateStatement = con.prepareStatement(updateSql);
		String name = event.getName();
		String description = event.getDescription();
		int day = event.getDay();
		int month = event.getMonth();
		int year = event.getYear();
		String type = event.getEventType();

		int col = 1;

		updateStatement.setString(col++, name);
		updateStatement.setString(col++, description);
		updateStatement.setInt(col++, day);
		updateStatement.setDouble(col++, month);
		updateStatement.setInt(col++, year);
		updateStatement.setString(col++, type);
		updateStatement.setInt(col++, event.getId());
		

		updateStatement.executeUpdate();
		updateStatement.close();
	}

	public Event retreiveEvent(int day, int month, int year) throws SQLException {
		// products.clear();

		String sql = "select id, name, description, day, month, year, event_type from Event where day = " + day
				+ "&& month = " + month + " && year = " + year;
		Statement selectStatement = con.createStatement();

		ResultSet results = selectStatement.executeQuery(sql);
		Event event = null;
		while (results.next()) {
			int id = results.getInt("id");
			String name = results.getString("name");
			String description = results.getString("description");
			String type = results.getString("event_type");
			event = new Event(name, description, day, month, year,type);
			event.setId(id);

		}
		results.close();
		selectStatement.close();
		return event;
	}

	public void deleteEvent(int day, int month, int year) {
		String deleteSql = "delete from Event where day =? && month = ? && year =?";
		try {
			PreparedStatement deleteStatement = con.prepareStatement(deleteSql);

			int col = 1;
			deleteStatement.setInt(col++, day);
			deleteStatement.setInt(col++, month);
			deleteStatement.setInt(col++, year);
			deleteStatement.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
