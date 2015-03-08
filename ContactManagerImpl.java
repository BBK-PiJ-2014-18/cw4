import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


public class ContactManagerImpl implements ContactManager {

	private Set<Contact> contacts;
	private Set<Meeting> meetings;
	int countContacts;
	int countMeetings;
	
	public ContactManagerImpl() {
		this.contacts = new HashSet<Contact>();
		this.meetings = new HashSet<Meeting>();
		this.countContacts = 0;
		this.countMeetings = 0;
	}
	
	
	@Override
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
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
		countMeetings++;
		int meetingId = countMeetings;
		meetings.add(new PastMeetingImpl(meetingId, contacts, date, text));
	}

	@Override
	public void addMeetingNotes(int id, String text) {
		// TODO Auto-generated method stub

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

}
