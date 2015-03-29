import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting {

	private String meetingNotes;

	/**
	 * This constructor uses the constructor of the abstract class MeetingImpl.
	 * Additional functionality to add meeting note text is included here. 
	 * @param meetingId
	 * @param contacts
	 * @param date
	 * @param text, the notes to be added about the meeting
	 */
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
