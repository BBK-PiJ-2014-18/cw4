import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {
	
	/**
	 * This constructor uses the constructor of the abstract class MeetingImpl.
	 * @param meetingId
	 * @param meetingContacts
	 * @param scheduledDate
	 */
	public FutureMeetingImpl(int meetingId, Set<Contact> meetingContacts, Calendar scheduledDate) {
		super(meetingId, meetingContacts, scheduledDate);
	}
	
	// No methods here, this is just a naming interface
	// (i.e. only necessary for type checking and/or downcasting)
}
