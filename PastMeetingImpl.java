import java.util.Calendar;
import java.util.Set;


public class PastMeetingImpl extends MeetingImpl implements PastMeeting {

	private String meetingNotes;
	
	public PastMeetingImpl (int meetingId, Set<Contact> contacts, Calendar date,
			String text) {
		super(meetingId, contacts, date);
		this.meetingNotes = text;
	}
	
	@Override
	public String getNotes() {
		return meetingNotes;
	}
}
