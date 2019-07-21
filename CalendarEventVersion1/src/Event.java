
public class Event {

	private int id;
	private String name;
	private String description;
	private int day;
	private int year;
	private int month;
	private String eventType;
	
	
	public Event() {
		
	}
	
	public Event(String name , String description , int day , int month , int year , String eventType) {
		this.name = name;
		this.description = description;
		this.day = day;
		this.month = month;
		this.year = year;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public int getDay() {
		return day;
	}


	public void setDay(int day) {
		this.day = day;
	}


	public int getYear() {
		return year;
	}


	public void setYear(int year) {
		this.year = year;
	}


	public int getMonth() {
		return month;
	}


	public void setMonth(int month) {
		this.month = month;
	}




	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", name=" + name + ", description=" + description + ", day=" + day + ", year=" + year
				+ ", month=" + month +  ", eventType=" + eventType + "]";
	}
	
	
}
