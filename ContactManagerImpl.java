import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class ContactManagerImpl implements ContactManager {

	private Set<Contact> contacts;
	private Set<Meeting> meetings;
	private int countContacts;
	private int countMeetings;
	private long daysToAddToClockForTesting;
	
	/**
	 * Normal, in use, constructor. Time will be as 'real world'.
	 * Only use other constructor for special testing requiring 'fake' future scenarios.
	 */	
	
	public ContactManagerImpl() {
		this.contacts = new HashSet<Contact>();
		this.meetings = new HashSet<Meeting>();
		this.countContacts = 0;
		this.countMeetings = 0;
		this.daysToAddToClockForTesting = 0;
	}

	/**
	 * Special time adjusting constructor to allow testing where the contact manager calculates
	 * time is in the future number of days added.
	 * 
	 * @param daysToAddToClockForTesting
	 */
	public ContactManagerImpl(long daysToAddToClockForTesting) {
		this.contacts = new HashSet<Contact>();
		this.meetings = new HashSet<Meeting>();
		this.countContacts = 0;
		this.countMeetings = 0;
		this.daysToAddToClockForTesting = daysToAddToClockForTesting;
	}
	
	@Override
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		if(contacts == null || date == null) {
			throw new NullPointerException("AddFutureMeeting arguments may not be null");
		}
		LocalDateTime mtgDate = convertDateFormat(date);
		if (mtgDate.isBefore(getNow())) {
			throw new IllegalArgumentException("Date is in the past");
		}
		checkContacts(contacts);
		countMeetings++;
		int meetingId = countMeetings;
		meetings.add(new FutureMeetingImpl(meetingId, contacts, date));
		return meetingId;
	}

	@Override
	public PastMeeting getPastMeeting(int id) {
		for(Meeting mtg: meetings) {
			if (mtg.getId() == id) {
				return (PastMeeting) mtg;
			}
		}
		return null;
	}

	@Override
	public FutureMeeting getFutureMeeting(int id) {
		for(Meeting mtg: meetings) {
			if (mtg.getId() == id) {
				return (FutureMeeting) mtg;
			}
		}
		return null;
	}

	@Override
	public Meeting getMeeting(int id) {
		for(Meeting mtg: meetings) {
			if (mtg.getId() == id) {
				return mtg;
			}
		}
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date,
			String text) {
		if (contacts == null || date == null || text == null) {
			throw new NullPointerException("NewPastMeeting arguments may not be null");
		}
		checkContacts(contacts);
		countMeetings++;
		int meetingId = countMeetings;
		meetings.add(new PastMeetingImpl(meetingId, contacts, date, text));
	}

	@Override
	public void addMeetingNotes(int id, String text) {
		Meeting mtg = getMeeting(id);
		meetings.remove(mtg);
		meetings.add(new PastMeetingImpl(id, mtg.getContacts(), mtg.getDate(), text));
	}

	@Override
	public void addNewContact(String name, String notes) {
		if(name == null || notes == null) {
			throw new NullPointerException("Name/Notes may not be null");
		}
		//as some methods search contacts by name do not allow empty string
		//notes may be empty as not searched for (and we have feature to add later)
		if(name == "") {
			throw new IllegalArgumentException("Name may not be empty string"); 
		}
		countContacts++;
		int contactId = countContacts;
		contacts.add(new ContactImpl(contactId, name, notes));
	}

	@Override
	public Set<Contact> getContacts(int... ids) {
		Set<Contact> result = new HashSet<Contact>();
		boolean idFound;
		for(int nextId: ids) {
			idFound = false;
			for (Contact contact: contacts) {
				if(contact.getId() == nextId) {
					result.add(contact);
					idFound = true;
					break;
				}
			}
			if(!idFound) {
				throw new IllegalArgumentException("ID does not correspond to a real contact");
			}
		}
		return result;
	}

	@Override
	public Set<Contact> getContacts(String name) {
		if (name == null) {
			throw new NullPointerException("Parameter may not be null");
		}
		if (name == "") {
			throw new IllegalArgumentException("Parameter may not be empty string");
		}
		Set<Contact> result = new HashSet<Contact>();
		for(Contact contact: contacts) {
			if(contact.getName().contains(name)) {
				result.add(contact);
			}
		}
		return result;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	/**
	 * Returns DateTime of Contact Manager's clock (date & time).
	 * Adjusted 'now' by number of days specified for this instance 
	 * of Contact Manager.  In normal use there will be zero adjustment.
	 * Adjustment is only used for testing functionality under future dates.
	 *
	 *@return LocalDateTime now (according to current instance of ContactManager)
	 */
	private LocalDateTime getNow() {
		return LocalDateTime.now().plusDays(daysToAddToClockForTesting);
	}	
	
	private LocalDateTime convertDateFormat(Calendar date) {
		GregorianCalendar gcDate = (GregorianCalendar) date;
		ZonedDateTime zdtDate = gcDate.toZonedDateTime();
		return zdtDate.toLocalDateTime();
	}
	
	private void checkContacts(Set<Contact> contacts) {
		if(contacts.size() == 0) {
			throw new IllegalArgumentException("Contact Set Empty");
		}
		
		if(!this.contacts.containsAll(contacts)) {
			throw new IllegalArgumentException("Contact unknown");
		}
	}
	
	
	
	
}
