public class ContactImpl implements Contact {
	
	private static final String CSV_SPLIT_STRING = "\",\"";
	
	private int contactId;
	private String contactName;
	private String contactNotes;
	
	/**
	 * Constructor that deals with ID, name and notes.
	 * Contacts can only be added to ContactManager by methods that generate a unique CM ID using
	 * the appropriate method in ContactManager.  It is that ID that is passed to this constructor. 
	 * @param contactId
	 * @param name
	 * @param notes
	 */
	public ContactImpl(int contactId, String name, String notes) {
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
	
	@Override
	public String toString() {
		return contactId + CSV_SPLIT_STRING + contactName + 
				CSV_SPLIT_STRING + contactNotes + CSV_SPLIT_STRING;

	}
}
