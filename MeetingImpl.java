import java.util.Calendar;
import java.util.Set;


public class MeetingImpl implements Meeting {

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

}
