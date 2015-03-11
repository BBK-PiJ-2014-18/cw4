import java.util.Calendar;
import java.util.Set;


public class MeetingImpl implements Meeting, Comparable<Meeting> {

	private int meetingId;
	private Calendar scheduledDate;
	private Set<Contact> meetingContacts;
	
	public MeetingImpl(int meetingId, Set<Contact> meetingContacts, Calendar scheduledDate) {
		this.meetingContacts = meetingContacts;
		this.scheduledDate = scheduledDate;
		this.meetingId = meetingId;
	}
	
	@Override
	public int getId() {
		return meetingId;
	}

	@Override
	public Calendar getDate() {
		return scheduledDate;
	}

	@Override
	public Set<Contact> getContacts() {
		return meetingContacts;
	}

	@Override
	public int compareTo(Meeting other) {
		//will need to test & fix for same datetime
		if (this.scheduledDate.before(other.getDate())) {
			return -1;
		}
		return 1;
	}
}
