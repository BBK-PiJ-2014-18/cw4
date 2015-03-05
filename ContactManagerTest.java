import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ContactManagerTest {

	@Test
	public void testAddFirstContactAndGetItBack() {
		ContactManager cm = new ContactManagerImpl();
		cm.addNewContact("Mark Kingsbury", "mk notes");
		Contact [] result = cm.getContacts(1).toArray(new Contact[0]);
		assertEquals(1, result.length);
		assertEquals("Mark Kingsbury", result[0].getName());
		assertEquals("mk notes", result[0].getNotes());
	}
}
