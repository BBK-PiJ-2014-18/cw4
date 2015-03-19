public class ContactImpl implements Contact {
	
	private static int countContacts;
	private int contactId;
	private String contactName;
	private String contactNotes;
	
	static {
		countContacts = 0;
	}
	
	/**
	 * Constructor that resets countContacts for JUnit tests
	 * TODO: finish these notes
	 */	
	
	public ContactImpl(int resetCountContacts) {
		countContacts = 0;
	}
	
	/**
	 * Constructor that generates ID, used by ContactManager.
	 * TODO: finish these notes
	 */	
	
	public ContactImpl(String name, String notes) {
		countContacts++;
		this.contactId = countContacts;
		this.contactName = name;
		this.contactNotes = notes;
	}
		
	/**
	 * Constructor that accepts ID as parameter used to create duplicate contacts
	 * to enable testing of ContactManager.
	 */	
	
	public ContactImpl(int contactId, String name, String notes) {
		countContacts++;
		this.contactId = contactId;
		this.contactName = name;
		this.contactNotes = notes;
	}
	
	@Override
	public int getId() {
		return contactId;
	}

	@Override
	public String getName() {
		return contactName;
	}

	@Override
	public String getNotes() {
		return contactNotes;
	}

	@Override
	public void addNotes(String note) {
		if(this.contactNotes == "") {
			this.contactNotes = note;
		} else {
			this.contactNotes += " " + note;
		}	
	}
	
	@Override
	public boolean equals(Object other) {
		if(this == other) return true;
		if(other == null || (this.getClass() != other.getClass())){
			return false;
		}
		Contact contact = (Contact) other;
		return (this.getId() == contact.getId()
				&& this.getName().equals(contact.getName())
				&& this.getNotes().equals(contact.getNotes()));
	}
	
	@Override
	public int hashCode() {
		int result = 0;
		result = 31 * result + contactId;
		result = 31 * result + contactName.hashCode();		
		result = 31 * result + contactNotes.hashCode();
		return result;
	}
}
