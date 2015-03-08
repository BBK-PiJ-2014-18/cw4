import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ContactManagerTest {

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
	
	//add and get back first future meeting - initial elements
	
	@Test
	public void testAddAndGetBackFirstFutureMeeting() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Kingsbury", "ak notes");
		cm.addNewContact("Brian Kingsbury", "bk notes");
		cm.addNewContact("Cathy Kingsbury", "ck notes");
		Set<Contact> kMtgContacts = new HashSet<Contact>();
		kMtgContacts.add(new ContactImpl(1, "Anna Kingsbury", "ak notes"));
		kMtgContacts.add(new ContactImpl(2, "Brian Kingsbury", "bk notes"));
		kMtgContacts.add(new ContactImpl(3, "Cathy Kingsbury", "ck notes"));
		Calendar kMtgDate = new GregorianCalendar(2015, 8, 14, 11, 2);
		int kMtgId = cm.addFutureMeeting(kMtgContacts, kMtgDate);
		assertEquals(1, kMtgId);
		FutureMeeting actual = cm.getFutureMeeting(1);
		assertEquals(kMtgContacts, actual.getContacts());
		assertEquals(kMtgDate, actual.getDate());
	}
	
	//add and get back first past meeting - initial elements
	
	@Test
	public void testAddAndGetBackFirstPastMeeting() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Anna Jones", "aj notes");
		cm.addNewContact("Brian Jones", "bj notes");
		cm.addNewContact("Cathy Jones", "cj notes");
		Set<Contact> jMtgContacts = new HashSet<Contact>();
		jMtgContacts.add(new ContactImpl(1, "Anna Jones", "aj notes"));
		jMtgContacts.add(new ContactImpl(2, "Brian Jones", "bj notes"));
		jMtgContacts.add(new ContactImpl(3, "Cathy Jones", "cj notes"));
		Calendar jMtgDate = new GregorianCalendar(2015, 8, 15, 11, 2);
		cm.addNewPastMeeting(jMtgContacts, jMtgDate, "New Past Meeting Notes");
		PastMeeting actual = cm.getPastMeeting(1);
		assertEquals(jMtgContacts, actual.getContacts());
		assertEquals(jMtgDate, actual.getDate());
	}

	
	
}
























