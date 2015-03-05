public class ContactImpl implements Contact {
	
	private int contactId;
	private String contactName;
	private String contactNotes;
	
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
		// TODO Auto-generated method stub
	}
}
