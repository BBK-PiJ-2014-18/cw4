import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class ContactManagerTest {

	@Before
	public void setUpBeforeEachTest() {
		//delete contacts.txt if it exists
		File file = new File("./cw4/contacts.txt");
        if(file.exists()) {
        	try {
                Files.deleteIfExists(file.toPath());
            } catch (IOException | SecurityException ex) {
                ex.printStackTrace();
            }	
        }	
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	//helper methods to add contacts and meetings
	
	public void helpAddContactsAndMeetings(ContactManager cm) {
		cm.addNewContact("Anna Kingsbury", "ak notes");
		cm.addNewContact("Brian Kingsbury", "bk notes");
		cm.addNewContact("Cathy Kingsbury", "ck notes");
		cm.addNewContact("Anna Jones", "aj notes");
		cm.addNewContact("Brian Jones", "bj notes");
		cm.addNewContact("Cathy Jones", "cj notes");
		Set<Contact> kMtgContacts = new HashSet<Contact>();
		kMtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		kMtgContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		kMtgContacts.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));
		Calendar kMtgDate = new GregorianCalendar(2015, 8, 14, 11, 2);
		cm.addFutureMeeting(kMtgContacts, kMtgDate);
		Set<Contact> jMtgContacts = new HashSet<Contact>();
		jMtgContacts.add(new ContactImpl(4, "Anna Jones", "aj notes"));
		jMtgContacts.add(new ContactImpl(5, "Brian Jones", "bj notes"));
		jMtgContacts.add(new ContactImpl(6, "Cathy Jones", "cj notes"));
		Calendar jMtgDate = new GregorianCalendar(2014, 8, 15, 11, 2);
		cm.addNewPastMeeting(jMtgContacts, jMtgDate, "New Past Meeting Notes");
	}
	
	public void helpAddMoreFutureMeetings(ContactManager cm) {
		Set<Contact> aMtgContacts = cm.getContacts(1,4);
		Set<Contact> bMtgContacts = cm.getContacts(2,5);
		Set<Contact> cMtgContacts = cm.getContacts(3,6);
		Calendar aMtgDate = new GregorianCalendar(2015, 8, 13, 00, 2);
		Calendar bMtgDate = new GregorianCalendar(2015, 8, 13, 10, 0);
		Calendar cMtgDate = new GregorianCalendar(2015, 8, 13, 23, 59);
		cm.addFutureMeeting(aMtgContacts, aMtgDate);
		cm.addFutureMeeting(bMtgContacts, bMtgDate);
		cm.addFutureMeeting(cMtgContacts, cMtgDate);
	}
	
	public void helpAddMorePastMeetings(ContactManager cm) {
		Set<Contact> aMtgContacts = cm.getContacts(1,4);
		Set<Contact> bMtgContacts = cm.getContacts(2,5);
		Set<Contact> cMtgContacts = cm.getContacts(3,6);
		Calendar aMtgDate = new GregorianCalendar(2000, 8, 13, 00, 2);
		Calendar bMtgDate = new GregorianCalendar(2000, 8, 13, 10, 0);
		Calendar cMtgDate = new GregorianCalendar(2000, 8, 13, 23, 59);
		cm.addNewPastMeeting(aMtgContacts, aMtgDate, "notes");
		cm.addNewPastMeeting(bMtgContacts, bMtgDate, "notes");
		cm.addNewPastMeeting(cMtgContacts, cMtgDate, "notes");
	}
	
	public void helpAddTodayMeetings(ContactManager cm) {
		Set<Contact> aMtgContacts = cm.getContacts(1,4);
		Set<Contact> bMtgContacts = cm.getContacts(2,5);
		Set<Contact> cMtgContacts = cm.getContacts(3,6);
		Calendar today = new GregorianCalendar();
		today = Calendar.getInstance();
		Calendar aMtgDate = new GregorianCalendar(today.get(Calendar.YEAR), today.get(Calendar.MONTH), 
				today.get(Calendar.DAY_OF_MONTH), 23, 59);
		Calendar bMtgDate = new GregorianCalendar(today.get(Calendar.YEAR), today.get(Calendar.MONTH), 
				today.get(Calendar.DAY_OF_MONTH), 0, 02);
		Calendar cMtgDate = new GregorianCalendar(today.get(Calendar.YEAR), today.get(Calendar.MONTH), 
				today.get(Calendar.DAY_OF_MONTH), 0, 01);
		cm.addFutureMeeting(aMtgContacts, aMtgDate);
		cm.addNewPastMeeting(bMtgContacts, bMtgDate, "notes");
		cm.addNewPastMeeting(cMtgContacts, cMtgDate, "notes");
	}
	
	//CONTACT TESTS START HERE
	
	//testing the basics of adding and getting back contacts
	
	@Test
	public void testAddFirstContactAndGetItBack() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Mark Kingsbury", "mk notes");
		Contact [] result = cm.getContacts(1).toArray(new Contact[0]);
		assertEquals(1, result.length);
		assertEquals("Mark Kingsbury", result[0].getName());
		assertEquals("mk notes", result[0].getNotes());
	}

	@Test(expected = NullPointerException.class)
	public void testAddNullNameContactThrowsNullPointerException() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact(null, "mk notes");
	}
	
	@Test(expected = NullPointerException.class)
	public void testAddNullNotesContactThrowsNullPointerException() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Mark Kingsbury", null);
	}	
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddEmptyStringNameContactThrowsNullPointerException() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("", "mk notes");
	}
	
	@Test
	public void testAddThreeContactsAndGetTwoBack() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		cm.addNewContact("Brian Kingsbury", "bk notes");
		cm.addNewContact("Cathy Kingsbury", "ck notes");
		Set<Contact> expected = new HashSet<Contact>();
		expected.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		expected.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));
		Set<Contact> actual = cm.getContacts(1, 3);
		assertEquals(2, actual.size());		
		assertEquals(expected, actual);	
	}
	
	//tests relating to notes field of Contact
	
	@Test
	public void testContactNotesCanBeEmptyString() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "");
		Set<Contact> expected = new HashSet<Contact>();
		expected.add(new ContactImpl(1, "Anna Kingsbury", ""));
		Set<Contact> actual = cm.getContacts(1);
		assertEquals(1, actual.size());		
		assertEquals(expected, actual);	
	}
	
	@Test
	public void testAddingNotesToContact() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "");
		Set<Contact> anna = cm.getContacts(1);
		for(Contact a: anna) {
			a.addNotes("Anna added Notes");
		}
		Set<Contact> expected = new HashSet<Contact>();
		expected.add(new ContactImpl(1, "Anna Kingsbury", "Anna added Notes"));
		Set<Contact> actual = cm.getContacts(1);
		assertEquals(1, actual.size());		
		assertEquals(expected, actual);	
	}

	@Test
	public void testAddingMoreNotesToContact() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "First Notes.");
		Set<Contact> anna = cm.getContacts(1);
		for(Contact a: anna) {
			a.addNotes("Second Notes.");
		}
		Set<Contact> expected = new HashSet<Contact>();
		expected.add(new ContactImpl(1, "Anna Kingsbury", "First Notes. Second Notes."));
		Set<Contact> actual = cm.getContacts(1);
		assertEquals(1, actual.size());		
		assertEquals(expected, actual);	
	}
	
	//tests relating to getting contacts back
	
	@Test(expected = IllegalArgumentException.class) 
	public void testContactIdDoesNotCorrespondToARealContact() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		cm.addNewContact("Brian Kingsbury", "bk notes");
		cm.addNewContact("Cathy Kingsbury", "ck notes");
		Set<Contact> actual = cm.getContacts(4);
	}
	
	@Test
	public void testGetAnEmptySetBackIfPassNoIds() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		cm.addNewContact("Brian Kingsbury", "bk notes");
		cm.addNewContact("Cathy Kingsbury", "ck notes");
		Set<Contact> expected = new HashSet<Contact>();
		Set<Contact> actual = cm.getContacts();
		assertEquals(0, actual.size());		
		assertEquals(expected, actual);	
	}
	
	//tests for getContacts by string in name
	
	@Test
	public void testGetContactsByNameSingleContact() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		cm.addNewContact("Brian Kingsbury", "bk notes");
		cm.addNewContact("Cathy Smith", "cs notes");
		Set<Contact> expected = new HashSet<Contact>();
		expected.add(new ContactImpl(3, "Cathy Smith", "cs notes"));
		Set<Contact> actual = cm.getContacts("Smith");
		assertEquals(expected, actual);	
	}	
	
	@Test
	public void testGetContactsByNameMulitpleContacts() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		cm.addNewContact("Brian Kingsbury", "bk notes");
		cm.addNewContact("Cathy Smith", "cs notes");
		Set<Contact> expected = new HashSet<Contact>();
		expected.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		expected.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		Set<Contact> actual = cm.getContacts("Kingsbury");
		assertEquals(expected, actual);	
	}	

	@Test
	public void testGetContactsByNameThatIsNotPresent() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		cm.addNewContact("Brian Kingsbury", "bk notes");
		cm.addNewContact("Cathy Smith", "cs notes");
		Set<Contact> expected = new HashSet<Contact>();
		Set<Contact> actual = cm.getContacts("Jones");
		assertEquals(0, actual.size());		
		assertEquals(expected, actual);	
	}
	
	@Test
	public void testGetBothContactsByNameThatIsDuplicated() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Kingsbury", "k notes");
		cm.addNewContact("Cathy Smith", "cs notes");
		cm.addNewContact("Kingsbury", "k notes");
		Set<Contact> expected = new HashSet<Contact>();
		expected.add(new ContactImpl(1, "Kingsbury", "k notes"));
		expected.add(new ContactImpl(3, "Kingsbury", "k notes"));
		Set<Contact> actual = cm.getContacts("Kingsbury");
		assertEquals(2, actual.size());		
		assertEquals(expected, actual);	
	}

	@Test(expected = NullPointerException.class)
	public void testErrorHandleGetContactsByNullParameter () {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Kingsbury", "k notes");
		String nullString = null;
		Set<Contact> actual = cm.getContacts(nullString);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testErrorHandleGetContactsByEmptyString () {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Kingsbury", "k notes");
		Set<Contact> actual = cm.getContacts("");
	}
	
	//MEETING TESTS START HERE
	
	//add and get back first future meeting - initial elements
	
	@Test
	public void testAddAndGetBackFirstFutureMeeting() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		cm.addNewContact("Brian Kingsbury", "bk notes");
		cm.addNewContact("Cathy Kingsbury", "ck notes");
		Set<Contact> expectedContacts = new HashSet<Contact>();
		expectedContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		expectedContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		expectedContacts.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));
		Calendar expectedDate = new GregorianCalendar(2015, 8, 14, 11, 2);
		int actualMtgId = cm.addFutureMeeting(expectedContacts, expectedDate);
		assertEquals(1, actualMtgId);
		FutureMeeting actual = cm.getFutureMeeting(1);
		assertEquals(expectedContacts, actual.getContacts());
		assertEquals(expectedDate, actual.getDate());
	}
	
	//add and get back first past meeting - initial elements
	
	@Test
	public void testAddAndGetBackFirstPastMeeting() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Jones", "aj notes");
		cm.addNewContact("Brian Jones", "bj notes");
		cm.addNewContact("Cathy Jones", "cj notes");
		Set<Contact> expectedContacts = new HashSet<Contact>();
		expectedContacts.add(new ContactImpl(1, "Anna Jones", "aj notes"));
		expectedContacts.add(new ContactImpl(2, "Brian Jones", "bj notes"));
		expectedContacts.add(new ContactImpl(3, "Cathy Jones", "cj notes"));
		Calendar expectedDate = new GregorianCalendar(2014, 8, 15, 11, 2);
		cm.addNewPastMeeting(expectedContacts, expectedDate, "New Past Meeting Notes");
		PastMeeting actual = cm.getPastMeeting(1);
		assertEquals(expectedContacts, actual.getContacts());
		assertEquals(expectedDate, actual.getDate());
		assertEquals("New Past Meeting Notes", actual.getNotes());
	}

	//uses helper method to add contacts and meetings and checks type of the three
	//different getMeeting(id) methods - i.e. Meeting, FutureMeeting & PastMeeting.

	@Test
	public void testGetFutureMtgAsFutureMtgUsingHelperMethodToAddContacts() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		Set<Contact> expectedContacts = cm.getContacts("Kingsbury");
		Calendar expectedDate = new GregorianCalendar(2015, 8, 14, 11, 2);
		FutureMeeting actual = cm.getFutureMeeting(1);
		assertEquals(expectedContacts, actual.getContacts());
		assertEquals(expectedDate, actual.getDate());
	}
	
	@Test
	public void testGetPastMtgAsPastMtg() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		Set<Contact> expectedContacts = cm.getContacts("Jones");
		Calendar expectedDate = new GregorianCalendar(2014, 8, 15, 11, 2);
		PastMeeting actual = cm.getPastMeeting(2);
		assertEquals(expectedContacts, actual.getContacts());
		assertEquals(expectedDate, actual.getDate());
		assertEquals("New Past Meeting Notes", actual.getNotes());
	}	
	
	@Test
	public void testGetMtgAsMtgOnFutureMeeting() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		Set<Contact> expectedContacts = cm.getContacts("Kingsbury");
		Calendar expectedDate = new GregorianCalendar(2015, 8, 14, 11, 2);
		Meeting actual = cm.getMeeting(1);
		assertEquals(expectedContacts, actual.getContacts());
		assertEquals(expectedDate, actual.getDate());
	}
	
	@Test
	public void testGetPastMtgAsMtgOnPastMeeting() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		Set<Contact> expectedContacts = cm.getContacts("Jones");
		Calendar expectedDate = new GregorianCalendar(2014, 8, 15, 11, 2);
		Meeting actual = cm.getMeeting(2);
		assertEquals(expectedContacts, actual.getContacts());
		assertEquals(expectedDate, actual.getDate());
		PastMeeting castActual = (PastMeeting) cm.getMeeting(2);
		assertEquals("New Past Meeting Notes", castActual.getNotes());
	}

	//tests for 3 methods where get meeting by id
	
	@Test
	public void testGetMeetingReturnsNullIfNoMtgWithThatID() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		assertNull(cm.getMeeting(3));
	}
	
	@Test
	public void testGetPastMeetingReturnsNullIfNoMtgWithThatID() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		assertNull(cm.getPastMeeting(3));
	}
	
	@Test
	public void testGetFutureMeetingReturnsNullIfNoMtgWithThatID() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		assertNull(cm.getFutureMeeting(3));
	}

	@Test
	public void testGetPastMeetingExceptionWhenMtgIdIsMtgIsAN_ACTUALFutureMeeting() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Meeting with that ID is a FutureMeeting");			
		cm.getPastMeeting(1);
	}
	
	@Test
	public void testGetFutureMeetingExceptionWhenMtgIdIsAN_ACTUALPastMeeting() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Meeting with that ID is a PastMeeting");	
		cm.getFutureMeeting(2);
	}	
	
	//is this logic required?  or can return a mtg as long as type is OK (regardless of date)
	@Ignore @Test
	public void testGetFutureMeetingExceptionWhenMtgIdIsMtgWITH_DATEInPast() {
		//need contact.txt in place to build this test
	}

	//is this logic required?  or can return a mtg as long as type is OK
	@Ignore @Test
	public void testGetPastMeetingExceptionWhenMtgIdIsMtgWITH_DATEInFuture() {
		//need contact.txt in place to build this test
	}
	
	//exception handling on addFutureMeeting
	
	@Test
	public void testAddFutureMeetingExceptionWhenDateInPast() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		Set<Contact> expectedContacts = new HashSet<Contact>();
		expectedContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		Calendar pastDate = new GregorianCalendar(1999, 8, 14, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Date is in the past");
		cm.addFutureMeeting(expectedContacts, pastDate);
	}
	
	@Test
	public void testAddFutureMeetingExceptionWhenContactNotKnown() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		Set<Contact> contactsWithAnUnknown = new HashSet<Contact>();
		contactsWithAnUnknown.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		contactsWithAnUnknown.add(new ContactImpl(2, "Elvis", "smooth notes"));
		Calendar date = new GregorianCalendar(2016, 8, 14, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Contact unknown");
		cm.addFutureMeeting(contactsWithAnUnknown, date);
	}
	
	@Test
	public void testAddFutureMeetingExceptionWhenContactNotKnownAndNoneinCM() {
		ContactManager cm = new ContactManagerImpl();
		Set<Contact> contactsWithAnUnknown = new HashSet<Contact>();
		contactsWithAnUnknown.add(new ContactImpl(2, "Elvis", "smooth notes"));
		Calendar date = new GregorianCalendar(2016, 8, 14, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Contact unknown");
		cm.addFutureMeeting(contactsWithAnUnknown, date);
	}
	
	@Test
	public void testAddFutureMeetingExceptionWhenSetOfContactsEmpty() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		Set<Contact> emptyContactSet = new HashSet<Contact>();
		Calendar date = new GregorianCalendar(2016, 8, 14, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Contact Set Empty");
		cm.addFutureMeeting(emptyContactSet, date);
	}
	@Test
	public void testAddFutureMeetingExceptionWhenSetOfContactsEmptyAndNoneInCM() {
		ContactManager cm = new ContactManagerImpl();
		Set<Contact> emptyContactSet = new HashSet<Contact>();
		Calendar date = new GregorianCalendar(2016, 8, 14, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Contact Set Empty");
		cm.addFutureMeeting(emptyContactSet, date);
	}	
	
	@Test
	public void testAddFutureMeetingExceptionOnContactsNull() {
		ContactManager cm = new ContactManagerImpl();
		Set<Contact> nullContactSet = null; 
		Calendar date = new GregorianCalendar(2016, 8, 14, 11, 2);
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("AddFutureMeeting arguments may not be null");
		cm.addFutureMeeting(nullContactSet, date);
	}
	
	@Test
	public void testAddFutureMeetingExceptionOnDateNull() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		Set<Contact> contacts =  new HashSet<Contact>();
		contacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		Calendar date = null;
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("AddFutureMeeting arguments may not be null");
		cm.addFutureMeeting(contacts, date);
	}
	
	//exception handling on addNewPastMeeting
	
	@Test
	public void testAddNewPastMeetingExceptionWhenSetOfContactsContainsAnUnknown() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		Set<Contact> contactsWithAnUnknown = new HashSet<Contact>();
		contactsWithAnUnknown.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		contactsWithAnUnknown.add(new ContactImpl(2, "Elvis", "smooth notes"));
		Calendar date = new GregorianCalendar(2014, 8, 14, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Contact unknown");
		cm.addNewPastMeeting(contactsWithAnUnknown, date, "notes on new past meeting");
	}
	
	@Test
	public void testAddNewPastMeetingExceptionWhenSetOfContactsContainsAnUnknownAndCMEmpty() {
		ContactManager cm = new ContactManagerImpl();
		Set<Contact> contactsWithAnUnknown = new HashSet<Contact>();
		contactsWithAnUnknown.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		contactsWithAnUnknown.add(new ContactImpl(2, "Elvis", "smooth notes"));
		Calendar date = new GregorianCalendar(2014, 8, 14, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Contact unknown");
		cm.addNewPastMeeting(contactsWithAnUnknown, date, "notes on new past meeting");
	}
	
	@Test
	public void testNewPastMeetingExceptionWhenSetOfContactsEmpty() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		Set<Contact> emptyContactSet = new HashSet<Contact>();
		Calendar date = new GregorianCalendar(2014, 8, 14, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Contact Set Empty");
		cm.addNewPastMeeting(emptyContactSet, date, "notes");
	}
	
	@Test
	public void testAddNewPastMeetingExceptionWhenSetOfContactsEmptyAndNoneInCM() {
		ContactManager cm = new ContactManagerImpl();
		Set<Contact> emptyContactSet = new HashSet<Contact>();
		Calendar date = new GregorianCalendar(2014, 8, 14, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Contact Set Empty");
		cm.addNewPastMeeting(emptyContactSet, date, "notes");
	}
	
	@Test
	public void testAddNewPastMeetingExceptionOnContactsNull() {
		ContactManager cm = new ContactManagerImpl();
		Set<Contact> nullContactSet = null; 
		Calendar date = new GregorianCalendar(2016, 8, 14, 11, 2);
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("NewPastMeeting arguments may not be null");
		cm.addNewPastMeeting(nullContactSet, date, "notes");
	}
	
	@Test
	public void testAddNewPastMeetingExceptionOnDateNull() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		Set<Contact> contacts =  new HashSet<Contact>();
		contacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		Calendar date = null;
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("NewPastMeeting arguments may not be null");
		cm.addNewPastMeeting(contacts, date, "notes");
	}
	
	@Test
	public void testAddNewPastMeetingExceptionOnDateInFuture() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		Set<Contact> contacts =  new HashSet<Contact>();
		contacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		Calendar date = new GregorianCalendar(2019, 8, 15, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Date may not be in future");
		cm.addNewPastMeeting(contacts, date, "notes");
	}
	
	
	
	@Test
	public void testAddNewPastMeetingExceptionOnNotesNull() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		Set<Contact> contacts =  new HashSet<Contact>();
		contacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		Calendar date = new GregorianCalendar(2016, 8, 14, 11, 2);
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("NewPastMeeting arguments may not be null");
		cm.addNewPastMeeting(contacts, date, null);
	}
	

	//tests for getFutureMeetingDate(Calendar)
	
	@Test
	public void testFutureMeetingDateReturnsEmptyListWhenNoMeetingsOnThatDate() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		Calendar noMtgsDate = new GregorianCalendar(2015, 8, 13, 11, 2);
		List<Meeting> actual = cm.getFutureMeetingList(noMtgsDate);	
		assertEquals(0, actual.size());
	}

	@Test
	public void testFutureMeetingDateReturnsSortedListForADate() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		helpAddMoreFutureMeetings(cm);
		Set<Contact> firstContacts = new HashSet<Contact>();
		Set<Contact> secondContacts = new HashSet<Contact>();
		Set<Contact> thirdContacts = new HashSet<Contact>();
		firstContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		secondContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		thirdContacts.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));		
		firstContacts.add(new ContactImpl(4, "Anna Jones", "aj notes"));
		secondContacts.add(new ContactImpl(5, "Brian Jones", "bj notes"));
		thirdContacts.add(new ContactImpl(6, "Cathy Jones", "cj notes"));
		Calendar testDate = new GregorianCalendar(2015, 8, 13);
		List<Meeting> actual = cm.getFutureMeetingList(testDate);
		assertEquals(3, actual.size());
		assertEquals(firstContacts, actual.get(0).getContacts());
		assertEquals(secondContacts, actual.get(1).getContacts());
		assertEquals(thirdContacts, actual.get(2).getContacts());
	}
	
	@Test
	public void testGetFutureMeetingListWithDayAndTimeJustWorksOnDay() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		helpAddMoreFutureMeetings(cm);
		Set<Contact> firstContacts = new HashSet<Contact>();
		Set<Contact> secondContacts = new HashSet<Contact>();
		Set<Contact> thirdContacts = new HashSet<Contact>();
		firstContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		secondContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		thirdContacts.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));		
		firstContacts.add(new ContactImpl(4, "Anna Jones", "aj notes"));
		secondContacts.add(new ContactImpl(5, "Brian Jones", "bj notes"));
		thirdContacts.add(new ContactImpl(6, "Cathy Jones", "cj notes"));
		Calendar testDate = new GregorianCalendar(2015, 8, 13, 12, 12);
		List<Meeting> actual = cm.getFutureMeetingList(testDate);
		assertEquals(3, actual.size());
		assertEquals(firstContacts, actual.get(0).getContacts());
		assertEquals(secondContacts, actual.get(1).getContacts());
		assertEquals(thirdContacts, actual.get(2).getContacts());
	}
	
	@Test
	public void testGetFutureMeetingListWithDateInPast() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		helpAddMoreFutureMeetings(cm);
		helpAddMorePastMeetings(cm);
		Set<Contact> firstContacts = new HashSet<Contact>();
		Set<Contact> secondContacts = new HashSet<Contact>();
		Set<Contact> thirdContacts = new HashSet<Contact>();
		firstContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		secondContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		thirdContacts.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));		
		firstContacts.add(new ContactImpl(4, "Anna Jones", "aj notes"));
		secondContacts.add(new ContactImpl(5, "Brian Jones", "bj notes"));
		thirdContacts.add(new ContactImpl(6, "Cathy Jones", "cj notes"));
		Calendar testDate = new GregorianCalendar(2000, 8, 13, 12, 12);
		List<Meeting> actual = cm.getFutureMeetingList(testDate);
		assertEquals(3, actual.size());
		assertEquals(firstContacts, actual.get(0).getContacts());
		assertEquals(secondContacts, actual.get(1).getContacts());
		assertEquals(thirdContacts, actual.get(2).getContacts());
	}


	@Test
	public void testGetFutureMeetingListWithTodayDate() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		helpAddTodayMeetings(cm);
		Set<Contact> firstContacts = new HashSet<Contact>();
		Set<Contact> secondContacts = new HashSet<Contact>();
		Set<Contact> thirdContacts = new HashSet<Contact>();
		firstContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		secondContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		thirdContacts.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));		
		firstContacts.add(new ContactImpl(4, "Anna Jones", "aj notes"));
		secondContacts.add(new ContactImpl(5, "Brian Jones", "bj notes"));
		thirdContacts.add(new ContactImpl(6, "Cathy Jones", "cj notes"));
		Calendar today = new GregorianCalendar();
		today = Calendar.getInstance();
		Calendar testDate = new GregorianCalendar(today.get(Calendar.YEAR), today.get(Calendar.MONTH), 
				today.get(Calendar.DAY_OF_MONTH));
		List<Meeting> actual = cm.getFutureMeetingList(testDate);
		assertEquals(3, actual.size());
		assertEquals(firstContacts, actual.get(2).getContacts());
		assertEquals(secondContacts, actual.get(1).getContacts());
		assertEquals(thirdContacts, actual.get(0).getContacts());
	}
	
	@Test
	public void testsThatCanNotAddTwoFutureMeetingsWithSameTime() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		cm.addNewContact("Brian Kingsbury", "bk notes");
		cm.addNewContact("Cathy Kingsbury", "ck notes");
		Set<Contact> expectedContacts = new HashSet<Contact>();
		expectedContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		expectedContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		expectedContacts.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));
		Calendar expectedDate = new GregorianCalendar(2015, 8, 14, 11, 2);
		cm.addFutureMeeting(expectedContacts, expectedDate);
		Set<Contact> moreExpectedContacts = new HashSet<Contact>();
		moreExpectedContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		moreExpectedContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		Calendar secondExpectedDate = new GregorianCalendar(2015, 8, 14, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Meeting already exists at that date/time");
		cm.addFutureMeeting(moreExpectedContacts, secondExpectedDate);
	}
	
	@Test
	public void testsThatCanNotAddTwoNewPastMeetingsWithSameTime() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		cm.addNewContact("Brian Kingsbury", "bk notes");
		cm.addNewContact("Cathy Kingsbury", "ck notes");
		Set<Contact> expectedContacts = new HashSet<Contact>();
		expectedContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		expectedContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		expectedContacts.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));
		Calendar expectedDate = new GregorianCalendar(2000, 8, 14, 11, 2);
		cm.addNewPastMeeting(expectedContacts, expectedDate, "notes");
		Set<Contact> moreExpectedContacts = new HashSet<Contact>();
		moreExpectedContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		moreExpectedContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		Calendar secondExpectedDate = new GregorianCalendar(2000, 8, 14, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Meeting already exists at that date/time");
		cm.addNewPastMeeting(moreExpectedContacts, secondExpectedDate, "notes");
	}
	
	//testing writing and reading contacts.txt
	
	@Test
	public void testFirstElementsOfWriteAndReadContactsTxt() {
		ContactManager firstCM = new ContactManagerImpl();
		helpAddContactsAndMeetings(firstCM);
		firstCM.flush();
		ContactManager secondCM = new ContactManagerImpl();
		Set<Contact> kMtgContacts = new HashSet<Contact>();
		kMtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		kMtgContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		kMtgContacts.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));
		Calendar kMtgDate = new GregorianCalendar(2015, 8, 14, 11, 2);
		assertEquals(kMtgContacts, secondCM.getFutureMeeting(1).getContacts());
		assertEquals(kMtgDate, secondCM.getFutureMeeting(1).getDate());
		Set<Contact> jMtgContacts = new HashSet<Contact>();
		jMtgContacts.add(new ContactImpl(4, "Anna Jones", "aj notes"));
		jMtgContacts.add(new ContactImpl(5, "Brian Jones", "bj notes"));
		jMtgContacts.add(new ContactImpl(6, "Cathy Jones", "cj notes"));
		Calendar jMtgDate = new GregorianCalendar(2014, 8, 15, 11, 2);
		assertEquals(jMtgContacts, secondCM.getPastMeeting(2).getContacts());
		assertEquals(jMtgDate, secondCM.getPastMeeting(2).getDate());
		assertEquals("New Past Meeting Notes", secondCM.getPastMeeting(2).getNotes());
	}

	@Test
	public void testCanAddNewContactAfterWriteAndReadContactsTxt() {
		ContactManager firstCM = new ContactManagerImpl();
		helpAddContactsAndMeetings(firstCM);
		firstCM.flush();
		ContactManager secondCM = new ContactManagerImpl();
		secondCM.addNewContact("Garry Gibbon", "gg notes");
		Contact expected = new ContactImpl(7, "Garry Gibbon", "gg notes");
		Set<Contact> expectedContacts = new HashSet<Contact>();
		expectedContacts.add(expected);
		assertEquals(expectedContacts, secondCM.getContacts(7));
		secondCM.flush();
	}
	
	// tests for AddMeetingNotes
	@Test
	public void testAddMeetingNotesConvertsFutureMtgToPastMtg() {
		//make a normal contact manager with date as 2015
		ContactManager cm2015 = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm2015);
		cm2015.flush();
		//make a contact manager with date as 2016
		ContactManager cm2016 = new ContactManagerImpl(365);
		//date of meeting "1" is now 'in the past' and so it can be changed into a past meeting
		cm2016.addMeetingNotes(1, "Adding Meeting Notes to Meeting One");
		Set<Contact> expectedContacts = cm2015.getContacts("Kingsbury");
		Calendar expectedDate = new GregorianCalendar(2015, 8, 14, 11, 2);
		PastMeeting actual = cm2016.getPastMeeting(1);
		assertEquals(expectedContacts, actual.getContacts());
		assertEquals(expectedDate, actual.getDate());
	}
	
	@Test
	public void testAddMeetingNotesExceptionWhenDateInFuture() {
		ContactManager cm2015 = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm2015);
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Meeting is in the future");
		cm2015.addMeetingNotes(1, "Adding Meeting Notes to Meeting One");
	}

	@Test
	public void testAddMeetingNotesExceptionWhenMtgDoesNotExist() {
		ContactManager cm2015 = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm2015);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Meeting does not exist");
		cm2015.addMeetingNotes(3, "Adding Meeting Notes to Meeting Three (doesn't exist)");
	}

	@Test
	public void testAddMeetingNotesExceptionWhenNotesAreNull() {
		//make a normal contact manager with date as 2015
		ContactManager cm2015 = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm2015);
		cm2015.flush();
		//make a contact manager with date as 2016 so mtg 1 is now in past
		ContactManager cm2016 = new ContactManagerImpl(365);
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Notes may not be null");
		String nullString = null;
		cm2016.addMeetingNotes(1, nullString);
	}

	@Test
	public void testAddMeetingNotesCanBeEmptyString() {
		//make a normal contact manager with date as 2015
		ContactManager cm2015 = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm2015);
		cm2015.flush();
		//make a contact manager with date as 2016 so mtg 1 is now in past
		ContactManager cm2016 = new ContactManagerImpl(365);
		String emptyString = "";
		cm2016.addMeetingNotes(1, emptyString);
		PastMeeting mtgActual = (PastMeeting) cm2016.getMeeting(1); 
		assertEquals("", mtgActual.getNotes());
	}

	@Test
	public void testAddMeetingNotesCanAddActualNotes() {
		//make a normal contact manager with date as 2015
		ContactManager cm2015 = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm2015);
		cm2015.flush();
		//make a contact manager with date as 2016 so mtg 1 is now in past
		ContactManager cm2016 = new ContactManagerImpl(365);
		cm2016.addMeetingNotes(1, "First notes.");
		PastMeeting mtgActual = (PastMeeting) cm2016.getMeeting(1); 
		assertEquals("First notes.", mtgActual.getNotes());
	}
	
	@Test
	public void testAddMeetingNotesCanAddNotesToEmptyStringNotes() {
		//make a normal contact manager with date as 2015
		ContactManager cm2015 = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm2015);
		cm2015.flush();
		//make a contact manager with date as 2016 so mtg 1 is now in past
		ContactManager cm2016 = new ContactManagerImpl(365);
		String emptyString = "";
		cm2016.addMeetingNotes(1, emptyString);
		cm2016.addMeetingNotes(1, "Some more notes.");
		PastMeeting mtgActual = (PastMeeting) cm2016.getMeeting(1); 
		assertEquals("Some more notes.", mtgActual.getNotes());
	}	
	
	@Test
	public void testAddMeetingNotesCanMoreNotesToExistingNotes() {
		//make a normal contact manager with date as 2015
		ContactManager cm2015 = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm2015);
		cm2015.flush();
		//make a contact manager with date as 2016 so mtg 1 is now in past
		ContactManager cm2016 = new ContactManagerImpl(365);
		cm2016.addMeetingNotes(1, "First notes.");
		cm2016.addMeetingNotes(1, " Second notes.");
		PastMeeting mtgActual = (PastMeeting) cm2016.getMeeting(1); 
		assertEquals("First notes. Second notes.", mtgActual.getNotes());
	}	
	
	@Test
	public void testGetFutureMeetingListByContactFirstElements() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		Set<Contact> k1MtgContacts = new HashSet<Contact>();
		k1MtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		k1MtgContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		Calendar k1MtgDate = new GregorianCalendar(2015, 8, 15, 00, 22);
		cm.addFutureMeeting(k1MtgContacts, k1MtgDate);
		Set<Contact> k2MtgContacts = new HashSet<Contact>();
		k2MtgContacts.add(new ContactImpl(6, "Cathy Jones", "cj notes"));
		k2MtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		Calendar k2MtgDate = new GregorianCalendar(2016, 8, 15, 00, 22);
		cm.addFutureMeeting(k2MtgContacts, k2MtgDate);
		Set<Contact> k3MtgContacts = new HashSet<Contact>();
		k3MtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		Calendar k3MtgDate = new GregorianCalendar(2016, 2, 15, 11, 22);
		cm.addFutureMeeting(k3MtgContacts, k3MtgDate);
		Set<Contact> k4MtgContacts = new HashSet<Contact>();
		k4MtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		k4MtgContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		k4MtgContacts.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));
		Contact anna = new ContactImpl(1, "Anna Kingsbury", "ak notes");
		List<Meeting> actual = cm.getFutureMeetingList(anna);
		assertEquals(4, actual.size());
		assertEquals(k4MtgContacts, actual.get(0).getContacts());
		assertEquals(k1MtgContacts, actual.get(1).getContacts());
		assertEquals(k3MtgContacts, actual.get(2).getContacts());
		assertEquals(k2MtgContacts, actual.get(3).getContacts());
	}
	
	@Test
	public void testGetFutureMeetingListByContactDoesNotReturnMeetingInPast() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		Set<Contact> k1MtgContacts = new HashSet<Contact>();
		k1MtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		k1MtgContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		Calendar k1MtgDate = new GregorianCalendar(2015, 8, 15, 00, 22);
		cm.addFutureMeeting(k1MtgContacts, k1MtgDate);
		Set<Contact> k2MtgContacts = new HashSet<Contact>();
		k2MtgContacts.add(new ContactImpl(6, "Cathy Jones", "cj notes"));
		k2MtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		Calendar k2MtgDate = new GregorianCalendar(2016, 8, 15, 00, 22);
		cm.addFutureMeeting(k2MtgContacts, k2MtgDate);
		Set<Contact> k3MtgContacts = new HashSet<Contact>();
		k3MtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		Calendar k3MtgDate = new GregorianCalendar(2016, 2, 15, 11, 22);
		cm.addFutureMeeting(k3MtgContacts, k3MtgDate);
		Set<Contact> k4MtgContacts = new HashSet<Contact>();
		k4MtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		k4MtgContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		k4MtgContacts.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));
		Contact anna = new ContactImpl(1, "Anna Kingsbury", "ak notes");
		cm.addNewPastMeeting(k3MtgContacts, new GregorianCalendar(1999, 9, 9, 9, 9), "notes");
		List<Meeting> actual = cm.getFutureMeetingList(anna);
		assertEquals(4, actual.size());
		assertEquals(k4MtgContacts, actual.get(0).getContacts());
		assertEquals(k1MtgContacts, actual.get(1).getContacts());
		assertEquals(k3MtgContacts, actual.get(2).getContacts());
		assertEquals(k2MtgContacts, actual.get(3).getContacts());
	}
	
	@Test
	public void testGetFutureMeetingListByContactIncludesMeetingHappeningLaterToday() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		Set<Contact> k1MtgContacts = new HashSet<Contact>();
		k1MtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		k1MtgContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		Calendar k1MtgDate = new GregorianCalendar(2015, 8, 15, 00, 22);
		cm.addFutureMeeting(k1MtgContacts, k1MtgDate);
		Set<Contact> k2MtgContacts = new HashSet<Contact>();
		k2MtgContacts.add(new ContactImpl(6, "Cathy Jones", "cj notes"));
		k2MtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		Calendar k2MtgDate = new GregorianCalendar(2016, 8, 15, 00, 22);
		cm.addFutureMeeting(k2MtgContacts, k2MtgDate);
		Set<Contact> k3MtgContacts = new HashSet<Contact>();
		k3MtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		Calendar k3MtgDate = new GregorianCalendar(2016, 2, 15, 11, 22);
		cm.addFutureMeeting(k3MtgContacts, k3MtgDate);
		Set<Contact> k4MtgContacts = new HashSet<Contact>();
		k4MtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		k4MtgContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		k4MtgContacts.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));
		Contact anna = new ContactImpl(1, "Anna Kingsbury", "ak notes");
		Calendar today = Calendar.getInstance();
		Calendar endOfToday = new GregorianCalendar(today.get(Calendar.YEAR), today.get(Calendar.MONTH), 
				today.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		cm.addFutureMeeting(k3MtgContacts, endOfToday);
		List<Meeting> actual = cm.getFutureMeetingList(anna);
		assertEquals(5, actual.size());
		assertEquals(k3MtgContacts, actual.get(0).getContacts());
		assertEquals(k4MtgContacts, actual.get(1).getContacts());
		assertEquals(k1MtgContacts, actual.get(2).getContacts());
		assertEquals(k3MtgContacts, actual.get(3).getContacts());
		assertEquals(k2MtgContacts, actual.get(4).getContacts());
	}

	@Test
	public void testGetFutureMeetingListReturnsEmptyListIfContactExistsNoMtgs() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		cm.addNewContact("Jane Kingsbury", "jk notes");
		Contact jane = new ContactImpl(7, "Jane Kingsbury", "jk notes");
		List<Meeting> actual =cm.getFutureMeetingList(jane);
		assertEquals(0, actual.size());
	}
	
	@Test
	public void testGetFutureMeetingListReturnsEmptyListIfContactExistsOnlyPastMeetings() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		Contact jane = new ContactImpl(4, "Anna Jones", "aj notes");
		List<Meeting> actual =cm.getFutureMeetingList(jane);
		assertEquals(0, actual.size());
	}
	
	@Test
	public void testGetFutureMeetingListExceptionOnContactThatDoesNotExist() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		Contact jane = new ContactImpl(99, "Jane Kingsbury", "jk notes");
		cm.getFutureMeetingList(jane);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Contact does not exist");
	}
	
	@Test
	public void testGetFutureMeetingListExceptionOnContactIsNull() {
		ContactManager cm = new ContactManagerImpl();
		helpAddContactsAndMeetings(cm);
		Contact nullcontact = null;
		cm.getFutureMeetingList(nullcontact);		
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("Contact may not be null");
	}
}










