import java.util.Calendar;
import java.util.Set;

public abstract class MeetingImpl implements Meeting, Comparable<Meeting> {

	private static final String CSV_SPLIT_STRING = "\",\"";
	
	private int meetingId;
	private Calendar date;
	private Set<Contact> meetingContacts;
	
	/**
	 * Constructor in Abstract Class deals with ID, Contacts and Date for all types of meeting.
	 * Meetings can only be added to ContactManager by methods that generate a unique CM ID using
	 * the appropriate method in ContactManager.  It is that ID that is passed to this constructor. 
	 * 
	 * @param meetingId
	 * @param meetingContacts
	 * @param scheduledDate
	 */
	public MeetingImpl(int meetingId, Set<Contact> meetingContacts, Calendar scheduledDate) {
		this.meetingContacts = meetingContacts;
		this.date = scheduledDate;
		this.meetingId = meetingId;
	}
	
	@Override
	public int getId() {
		return meetingId;
	}

	@Override
	public Calendar getDate() {
		return date;
	}

	@Override
	public Set<Contact> getContacts() {
		return meetingContacts;
	}

	/**
	 * Meetings are compared by chronological date.
	 */	
	@Override
	public int compareTo(Meeting other) {
		if(this.getDate().equals(other.getDate())) {
			return 0;
		}
		if (this.date.before(other.getDate())) {
			return -1;
		}
		return 1;
	}
	
	/**
	 * @Return a string in the appropriate format for saving in the ContactManger csv file.
	 */
	@Override
	public String toString() {
		String dateStr = date.get(Calendar.YEAR) + CSV_SPLIT_STRING
				+ date.get(Calendar.MONTH) + CSV_SPLIT_STRING
				+ date.get(Calendar.DAY_OF_MONTH) + CSV_SPLIT_STRING
				+ date.get(Calendar.HOUR_OF_DAY) + CSV_SPLIT_STRING
				+ date.get(Calendar.MINUTE);
		String result = meetingId + CSV_SPLIT_STRING + dateStr + CSV_SPLIT_STRING;	
		if (this instanceof PastMeeting){
			PastMeeting pastMeeting = (PastMeeting) this;
			result += pastMeeting.getNotes() + CSV_SPLIT_STRING;
		} else {
			result += "" + CSV_SPLIT_STRING;
		}
		for (Contact contact: this.getContacts()) {
			result += contact.getId() + CSV_SPLIT_STRING;
		}
		return result;
	}
}
