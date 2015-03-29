import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * An implementation of ContactManager.  Generates IDs for Contacts and Meetings.
 * @author markkingsbury
 */

public class ContactManagerImpl implements ContactManager {

	private static final String DATA_FILE_NAME = "./cw4/contacts.txt";
	private static final String CSV_SPLIT_STRING = "\",\"";
	
	private Set<Contact> contacts;
	private SortedSet<Meeting> meetings;
	private int countContacts;
	private int countMeetings;
	private long daysToAddToClockForTesting;
	
	/**
	 * Normal, in use, constructor. Time will be as 'real world'.
	 */	
	
	public ContactManagerImpl() {
		this.contacts = new HashSet<Contact>();
		this.meetings = new TreeSet<Meeting>();
		this.countContacts = 0;
		this.countMeetings = 0;
		this.daysToAddToClockForTesting = 0;
		loadRecords();
	}

	/**
	 * Special time adjusting constructor to allow testing where the contact manager calculates
	 * time is in the future number of days added.
	 * 
	 * @param daysToAddToClockForTesting the number of days to be added to the ContactManager clock
	 */
	public ContactManagerImpl(long daysToAddToClockForTesting) {
		this.contacts = new HashSet<Contact>();
		this.meetings = new TreeSet<Meeting>();
		this.countContacts = 0;
		this.countMeetings = 0;
		this.daysToAddToClockForTesting = daysToAddToClockForTesting;
		loadRecords();
	}
	

	@Override
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		if(contacts == null || date == null) {
			throw new NullPointerException("AddFutureMeeting arguments may not be null");
		}
		if(dateIsInPast(date)) {
			throw new IllegalArgumentException("Date is in the past");
		}
		checkContacts(contacts);
		countMeetings++;
		int meetingId = countMeetings;
		Meeting meetingToAdd = new FutureMeetingImpl(meetingId, contacts, date);
		//contains uses compareTo() which is overridden so based on date
		if (meetings.contains(meetingToAdd)) {
			countMeetings--;
			throw new IllegalArgumentException("Meeting already exists at that date/time");
		}
		meetings.add(meetingToAdd);
		return meetingId;
	}

	@Override
	public PastMeeting getPastMeeting(int id) {
		//any FutureMeetings with a date in past are migrated to PastMeetings
		migrateFutureMeetings();
		for(Meeting mtg: meetings) {
			if (mtg.getId() == id) {
				if (mtg instanceof FutureMeeting){
					//if this is a FutureMeeting the date must be in the future
					throw new IllegalArgumentException("Meeting with that ID is in the future");
				}				
				return (PastMeeting) mtg;
			}
		}
		return null;
	}

	@Override
	public FutureMeeting getFutureMeeting(int id) {
		//any FutureMeetings with a date in past are migrated to PastMeetings
		migrateFutureMeetings();
		for(Meeting mtg: meetings) {
			if (mtg.getId() == id) {
				if (mtg instanceof PastMeeting){
					//if this is a PastMeeting the date must be in the past
					throw new IllegalArgumentException("Meeting with that ID is in the past");
				}
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

	/**
	* Returns meetings that happen at a time in the future, only future meetings can have
	* a date in the future. Any CM FutureMeetings with a date in the past are migrated to PastMeetings. 
	*/	
	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {
		if(!this.contacts.contains(contact)) {
			throw new IllegalArgumentException("Contact unknown");
		}
		//any FutureMeetings with a date in past are migrated to PastMeetings
		migrateFutureMeetings();
		List<Meeting> result = new LinkedList<Meeting>();
		for (Meeting mtg: meetings) {
			//as meetings is a SortedSet with compareTo overridden for date order the meetings
			//are added to result in chronological order.
			if(mtg.getContacts().contains(contact) && !dateIsInPast(mtg.getDate())) {
				result.add(mtg);
			}	
		}
		return result;
	}

	/**
	* Meetings that happen on this day (which may be past or future) may be of the type FutureMeeting
	* or PastMeeting (the method returns meetings on the specified day regardless of type). 
	*/	
	
	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {
		//any FutureMeetings with a date in past are migrated to PastMeetings
		migrateFutureMeetings();
		List<Meeting> result = new LinkedList<Meeting>();
		for (Meeting mtg: meetings) {
			//as meetings is a SortedSet with compareTo overridden for date order the meetings
			//are added to result in chronological order.
			if(mtg.getDate().get(Calendar.YEAR) == date.get(Calendar.YEAR)
					&& mtg.getDate().get(Calendar.MONTH) == date.get(Calendar.MONTH)
					&& mtg.getDate().get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
				result.add(mtg);
			}	
		}
		return result;
	}

	
	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		if(!this.contacts.contains(contact)) {
			throw new IllegalArgumentException("Contact unknown");
		}
		//any FutureMeetings with a date in past are migrated to PastMeetings
		migrateFutureMeetings();
		List<PastMeeting> result = new LinkedList<PastMeeting>();
		for (Meeting mtg: meetings) {
			//as meetings is a SortedSet with compareTo overridden for date order the meetings
			//are added to result in chronological order.
			if(mtg.getContacts().contains(contact) && dateIsInPast(mtg.getDate())) {
				result.add((PastMeeting) mtg);
			}	
		}
		return result;
	}

	@Override
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
		if (contacts == null || date == null || text == null) {
			throw new NullPointerException("NewPastMeeting arguments may not be null");
		}
		if(!dateIsInPast(date)) {
			throw new IllegalArgumentException("Date may not be in future");
		}
		checkContacts(contacts);
		countMeetings++;
		int meetingId = countMeetings;
		Meeting meetingToAdd = new PastMeetingImpl(meetingId, contacts, date, text);
		if (meetings.contains(meetingToAdd)) {
			countMeetings--;
			throw new IllegalArgumentException("Meeting already exists at that date/time");
		}
		meetings.add(meetingToAdd);
	}

	@Override
	public void addMeetingNotes(int id, String text) {
		if(text == null) {
			throw new NullPointerException("Notes may not be null");
		}
		Meeting mtg = getMeeting(id);
		if (mtg == null) {
			throw new IllegalArgumentException("Meeting does not exist");
		}
		if(!dateIsInPast(mtg.getDate())) {
			throw new IllegalStateException("Meeting is in the future");
		}
		//remove the meeting from CM
		meetings.remove(mtg);
		//if the meeting is already a PastMeeting, add the text to any existing notes
		if (mtg instanceof PastMeeting){
			text = ((PastMeeting) mtg).getNotes() + text;
		}
		//the add it back as a PastMeeting
		meetings.add(new PastMeetingImpl(id, mtg.getContacts(), mtg.getDate(), text));
	}

	@Override
	public void addNewContact(String name, String notes) {
		if(name == null || notes == null) {
			throw new NullPointerException("Name/Notes may not be null");
		}
		//as some methods search contacts by name do not allow empty string however
		//notes may be empty string as not searched for (and we have feature to add later)
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
		for(int id: ids) {
			idFound = false;
			for (Contact contact: contacts) {
				if(contact.getId() == id) {
					idFound = true;
					result.add(contact);
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
		PrintWriter out = null;
		File file = new File(DATA_FILE_NAME);
		try {
			out = new PrintWriter(file);
			out.println("contacts");
			for(Contact contact: contacts) {
				out.println(contact.toString());			
			}
			out.println("meetings");
			for(Meeting meeting: meetings) {
				out.println(meeting.toString());
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	private void loadRecords() {
		File file = new File(DATA_FILE_NAME);
        if(!file.exists()) {
        	return;	
        }	
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
			String line;
			if(!(line = in.readLine()).equals("contacts")) {
				throw new IllegalStateException("Data File has error");
			};
			//load the contacts
			String[] contactToLoad;
			while((line = in.readLine()) != null && !line.equals("meetings")) {
				contactToLoad = line.split(CSV_SPLIT_STRING, -1);
				createContactFromString(contactToLoad);
			}
			//load the meetings
			String[] meetingToLoad;
			while((line = in.readLine()) != null) {
				meetingToLoad = line.split(CSV_SPLIT_STRING, -1);
				createMeetingFromString(meetingToLoad);
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			closeReader(in);
		}
	}
	
	/**
	 *Creates a contact in ContactManager based on the array 
	 * @param contactToLoad
	 */
	
	private void createContactFromString(String[] contactToLoad) {
		int contactId = Integer.parseInt(contactToLoad[0]);
		countContacts++;
		contacts.add(new ContactImpl(contactId, contactToLoad[1], contactToLoad[2]));		
	}
	
	/**
	*Creates a meeting in ContactManager based on the array 
	*@param meetingToLoad 
	**/
	private void createMeetingFromString(String[] meetingToLoad) {
		int meetingId = Integer.parseInt(meetingToLoad[0]);
		Calendar meetingDate = new GregorianCalendar(
				Integer.parseInt(meetingToLoad[1]),
				Integer.parseInt(meetingToLoad[2]),
				Integer.parseInt(meetingToLoad[3]),
				Integer.parseInt(meetingToLoad[4]),
				Integer.parseInt(meetingToLoad[5]));
		String meetingNotes = meetingToLoad[6];
		int numberOfContactsAtMtg = meetingToLoad.length - 8;
		int[] meetingContactIds = new int[numberOfContactsAtMtg];
		for(int i = 0; i < numberOfContactsAtMtg; i++) {
			meetingContactIds[i] = Integer.parseInt(meetingToLoad[i+7]); 
		}
		Set<Contact> meetingContacts = getContacts(meetingContactIds);
		countMeetings++;
		if (dateIsInPast(meetingDate)) {
			meetings.add(new PastMeetingImpl(meetingId, meetingContacts, meetingDate, meetingNotes));
		} else {
			meetings.add(new FutureMeetingImpl(meetingId, meetingContacts, meetingDate));
		}		
	}
	
	/**
	 * Closes the reader
	 * @param reader
	 */
	
	private void closeReader(Reader reader) {
		try {
			if(reader != null) {
				reader.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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
	
	/**
	 * Transforms the format of date
	 * @param date in Calendar format
	 * @return the same date in LocalDateTime format
	 */
	
	private LocalDateTime convertDateFormat(Calendar date) {
		GregorianCalendar gcDate = (GregorianCalendar) date;
		ZonedDateTime zdtDate = gcDate.toZonedDateTime();
		return zdtDate.toLocalDateTime();
	}
	
	/**
	 * Check whether the date provided is in the past
	 * @param date to be checked
	 * @return true if date is in past, otherwise false
	 */
	private boolean dateIsInPast(Calendar date) {
		LocalDateTime checkDate = convertDateFormat(date);
		return (checkDate.isBefore(getNow()));		
	}
	
	/**
	 * Checks that at least one contact has been provided and that all contacts provided
	 * exist in ContactManager.
	 * @param contacts, the set of contacts to be checked
	 */
	private void checkContacts(Set<Contact> contacts) {
		if(contacts.size() == 0) {
			throw new IllegalArgumentException("Contact Set Empty");
		}	
		if(!this.contacts.containsAll(contacts)) {
			throw new IllegalArgumentException("Contact unknown");
		}
	}
	
	/**
	* Migrates any FutureMeetings with a date in the past into PastMeetings  
	*/
	private void migrateFutureMeetings() {
		List<FutureMeeting> migrateList = new LinkedList<FutureMeeting>();
		for (Meeting mtg: meetings) {
			if(mtg instanceof FutureMeeting && dateIsInPast(mtg.getDate())) {
				migrateList.add((FutureMeeting) mtg);
			}	
		}
		while (migrateList.size() != 0) {
			addMeetingNotes(migrateList.remove(0).getId(), "");
		}
	}
}
