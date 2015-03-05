import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


public class ContactManagerImpl implements ContactManager {

	private Set<Contact> contacts;
	int countContacts;
	
	public ContactManagerImpl() {
		this.contacts = new HashSet<Contact>();
		this.countContacts = 0;
	}
	
	
	@Override
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PastMeeting getPastMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FutureMeeting getFutureMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meeting getMeeting(int id) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub

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
		for(int nextId: ids) {
			for (Contact contact: contacts) {
				if(contact.getId() == nextId) {
					result.add(contact);
					break;
				}
			}
		}
		return result;
	}

	@Override
	public Set<Contact> getContacts(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

}
