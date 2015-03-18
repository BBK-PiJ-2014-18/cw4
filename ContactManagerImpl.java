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

public class ContactManagerImpl implements ContactManager {

	private static final String DATA_FILE_NAME = "./cw4/contacts.txt";
	
	private Set<Contact> contacts;
	private SortedSet<Meeting> meetings;
//	private int countContacts;
	private int countMeetings;
	private long daysToAddToClockForTesting;
	
	/**
	 * Normal, in use, constructor. Time will be as 'real world'.
	 * Only use other constructor for special testing requiring 'fake' future scenarios.
	 */	
	
	public ContactManagerImpl() {
		this.contacts = new HashSet<Contact>();
		this.meetings = new TreeSet<Meeting>();
//		this.countContacts = 0;
		this.countMeetings = 0;
		this.daysToAddToClockForTesting = 0;
		loadRecords();
	}

	/**
	 * Special time adjusting constructor to allow testing where the contact manager calculates
	 * time is in the future number of days added.
	 * 
	 * @param daysToAddToClockForTesting
	 */
	public ContactManagerImpl(long daysToAddToClockForTesting) {
		this.contacts = new HashSet<Contact>();
		this.meetings = new TreeSet<Meeting>();
//		this.countContacts = 0;
		this.countMeetings = 0;
		this.daysToAddToClockForTesting = daysToAddToClockForTesting;
		loadRecords();
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
		Meeting meetingToAdd = new FutureMeetingImpl(meetingId, contacts, date);
		if (meetings.contains(meetingToAdd)) {
			countMeetings--;
			throw new IllegalArgumentException("Meeting already exists at that date/time");
		}
		meetings.add(meetingToAdd);
		return meetingId;
	}

	@Override
	public PastMeeting getPastMeeting(int id) {
		for(Meeting mtg: meetings) {
			if (mtg.getId() == id) {
				if (mtg instanceof FutureMeeting){
					throw new IllegalArgumentException("Meeting with that ID is a FutureMeeting");
				}				
				return (PastMeeting) mtg;
			}
		}
		return null;
	}

	@Override
	public FutureMeeting getFutureMeeting(int id) {
		for(Meeting mtg: meetings) {
			if (mtg.getId() == id) {
				if (mtg instanceof PastMeeting){
					throw new IllegalArgumentException("Meeting with that ID is a PastMeeting");
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
	* Returns meetings that happen at a time in the future, they may be of the 
	* type FutureMeeting or PastMeeting. 
	*/	
	
	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	* Meetings that happen on this day may be of the type FutureMeeting
	* or PastMeeting (the method returns meetings on the specified day 
	* regardless of type). 
	* 
	* @throws IllegalArgumentException if date does not include a specific day.
	*/	
	
	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {
		List<Meeting> result = new LinkedList<Meeting>();
		for (Meeting mtg: meetings) {
			if(mtg.getDate().get(Calendar.YEAR) == date.get(Calendar.YEAR)
					&& mtg.getDate().get(Calendar.MONTH) == date.get(Calendar.MONTH)
					&& mtg.getDate().get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
				result.add(mtg);
			}	
		}
		return result;
	}

	/**
	* Returned list includes meetings of the type PastMeeting, even if they happen on a future date. 
	* List does not include meetings of the type FutureMeeting, even if they have a date in the past. 
	*/
	
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
		LocalDateTime mtgDate = convertDateFormat(mtg.getDate());
		if(mtgDate.isAfter(getNow())) {
			throw new IllegalStateException("Meeting is in the future");
		}
		meetings.remove(mtg);
		if (mtg instanceof PastMeeting){
			text = ((PastMeeting) mtg).getNotes() + text;
		}
		meetings.add(new PastMeetingImpl(id, mtg.getContacts(), mtg.getDate(), text));
	}

	@Override
	public void addNewContact(String name, String notes) {
		if(name == null || notes == null) {
			throw new NullPointerException("Name/Notes may not be null");
		}
		//as some methods search contacts by name do not allow empty string
		//notes may be empty string as not searched for (and we have feature to add later)
		if(name == "") {
			throw new IllegalArgumentException("Name may not be empty string"); 
		}
//		countContacts++;
//		int contactId = countContacts;
//		contacts.add(new ContactImpl(contactId, name, notes));
		contacts.add(new ContactImpl(name, notes));
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
		PrintWriter out = null;
		File file = new File(DATA_FILE_NAME);
		try {
			out = new PrintWriter(file);
			//TEST FOR PUTTING SOMETHING OTHER THAN "contacts" BREAKS CHECK IN LOADRECORDS()
			out.println("contacts");
			for(Contact contact: contacts) {
				out.println(contact.getId() + "," + contact.getName() + 
						","+ contact.getNotes() + ",");
			}
			out.println("meetings");
			
			for(Meeting meeting: meetings) {
				String dateStr = meeting.getDate().get(Calendar.YEAR) + ","
						+ meeting.getDate().get(Calendar.MONTH) + ","
						+ meeting.getDate().get(Calendar.DAY_OF_MONTH) + ","
						+ meeting.getDate().get(Calendar.HOUR_OF_DAY) + ","
						+ meeting.getDate().get(Calendar.MINUTE);
				String str = meeting.getId() + "," + dateStr + ",";	
				if (meeting instanceof PastMeeting){
					PastMeeting pastMeeting = (PastMeeting) meeting;
					str = str + pastMeeting.getNotes() + ",";
				} else {
					str = str + "" + ",";
				}
				for (Contact contact: meeting.getContacts()) {
					//do this the cool way...
					str = str + contact.getId() + ",";
				};
	
				out.println(str);
			}
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot write to file " + file + ".");
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
				// FIX THIS...
				System.out.println("we have a problem - do something smarter here");
			};
			String[] contactToLoad;
			while((line = in.readLine()) != null && !line.equals("meetings")) {
				contactToLoad = line.split(",", -1);
				int contactId = Integer.parseInt(contactToLoad[0]);
//				countContacts++;
				contacts.add(new ContactImpl(contactId, contactToLoad[1], contactToLoad[2]));
//				contacts.add(new ContactImpl(contactToLoad[1], contactToLoad[2]));
	//			}
			}
			String[] meetingToLoad;
			while((line = in.readLine()) != null) {
				meetingToLoad = line.split(",", -1);
				int meetingId = Integer.parseInt(meetingToLoad[0]);
				Calendar meetingDate = new GregorianCalendar(
						Integer.parseInt(meetingToLoad[1]),
						Integer.parseInt(meetingToLoad[2]),
						Integer.parseInt(meetingToLoad[3]),
						Integer.parseInt(meetingToLoad[4]),
						Integer.parseInt(meetingToLoad[5]));
				String meetingNotes = meetingToLoad[6];
				int[] meetingContactIds = new int[meetingToLoad.length -8];
				// 6 would make sense, 7 is a magic number - todo with the -1 in line.split(",", -1)
				for(int i = 0; i < meetingToLoad.length - 8; i++) {
					meetingContactIds[i] = Integer.parseInt(meetingToLoad[i+7]); 
				}
				Set<Contact> meetingContacts = getContacts(meetingContactIds);
				countMeetings++;
				LocalDateTime mtgDate = convertDateFormat(meetingDate);
				if (mtgDate.isAfter(getNow())) {
					meetings.add(new FutureMeetingImpl(meetingId, meetingContacts, meetingDate));
				} else {
					meetings.add(new PastMeetingImpl(meetingId, meetingContacts, meetingDate, meetingNotes));
				}
			}
		} catch (FileNotFoundException ex) {
			// so we want to have something earlier that skips the reading gig if file not found
			// rather than have it as an exception... FIX THIS
			System.out.println("File " + file + " does not exist. This is first ever use of CM");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			closeReader(in);
		}
	}
	
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
