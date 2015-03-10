import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class ContactManagerTest {

	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
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
		Calendar date = new GregorianCalendar(2016, 8, 14, 11, 2);
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
		Calendar date = new GregorianCalendar(2016, 8, 14, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Contact unknown");
		cm.addNewPastMeeting(contactsWithAnUnknown, date, "notes on new past meeting");
	}
	
	@Test
	public void testNewPastMeetingExceptionWhenSetOfContactsEmpty() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		Set<Contact> emptyContactSet = new HashSet<Contact>();
		Calendar date = new GregorianCalendar(2016, 8, 14, 11, 2);
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Contact Set Empty");
		cm.addNewPastMeeting(emptyContactSet, date, "notes");
	}
	
	@Test
	public void testAddNewPastMeetingExceptionWhenSetOfContactsEmptyAndNoneInCM() {
		ContactManager cm = new ContactManagerImpl();
		Set<Contact> emptyContactSet = new HashSet<Contact>();
		Calendar date = new GregorianCalendar(2016, 8, 14, 11, 2);
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
		
	
	// TODO: this test ignored for now (need to have Contacts.txt in place before can run).
	@Ignore @Test
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
}
























