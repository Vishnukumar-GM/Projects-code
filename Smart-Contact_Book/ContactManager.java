// ContactManager.java
// This class manages the collection of Contact objects.
// It demonstrates the application of Data Structures (HashMap) for efficient operations
// and File I/O for data persistence.

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors; // For stream API usage in search

public class ContactManager {
    // Using HashMap for efficient storage and retrieval of contacts.
    // Key: Phone Number (String) - chosen as a unique identifier for quick lookups.
    // Value: Contact object.
    // DSA Choice Explanation:
    // - HashMap provides average O(1) time complexity for add, search (get), and delete operations.
    // - If we used an ArrayList, search and delete would be O(N) in the worst case,
    //   requiring iteration through the entire list. HashMap makes these operations much faster,
    //   making the "Contact Book" truly "Smart" for these common tasks.
    private Map<String, Contact> contacts;
    private final String FILENAME = "contacts.dat"; // File to store serialized contact data

    // Constructor
    public ContactManager() {
        this.contacts = new HashMap<>();
        loadContacts(); // Load contacts from file when manager is initialized
    }

    /**
     * Adds a new contact to the manager.
     * @param contact The Contact object to add.
     * @return true if the contact was added successfully, false if a contact with the same phone number already exists.
     */
    public boolean addContact(Contact contact) {
        // Check if a contact with this phone number already exists
        if (contacts.containsKey(contact.getPhoneNumber())) {
            System.out.println("Error: A contact with this phone number already exists.");
            return false;
        }
        contacts.put(contact.getPhoneNumber(), contact); // Add to HashMap
        saveContacts(); // Save changes to file
        return true;
    }

    /**
     * Retrieves a contact by its phone number.
     * @param phoneNumber The phone number of the contact to retrieve.
     * @return The Contact object if found, null otherwise.
     */
    public Contact getContact(String phoneNumber) {
        return contacts.get(phoneNumber); // O(1) average time complexity for lookup
    }

    /**
     * Updates an existing contact.
     * @param oldPhoneNumber The current phone number of the contact to update.
     * @param updatedContact The Contact object with updated details (including new phone number if changed).
     * @return true if the contact was updated successfully, false if the old contact was not found or new phone number conflicts.
     */
    public boolean updateContact(String oldPhoneNumber, Contact updatedContact) {
        if (!contacts.containsKey(oldPhoneNumber)) {
            System.out.println("Error: Contact with phone number " + oldPhoneNumber + " not found.");
            return false;
        }

        // If phone number is changed, ensure the new one doesn't conflict with existing contacts
        if (!oldPhoneNumber.equals(updatedContact.getPhoneNumber()) && contacts.containsKey(updatedContact.getPhoneNumber())) {
            System.out.println("Error: New phone number " + updatedContact.getPhoneNumber() + " already exists for another contact.");
            return false;
        }

        // Remove old entry and add new entry (important if phone number is the key)
        contacts.remove(oldPhoneNumber);
        contacts.put(updatedContact.getPhoneNumber(), updatedContact);
        saveContacts(); // Save changes to file
        return true;
    }

    /**
     * Deletes a contact by its phone number.
     * @param phoneNumber The phone number of the contact to delete.
     * @return true if the contact was deleted successfully, false if not found.
     */
    public boolean deleteContact(String phoneNumber) {
        if (contacts.containsKey(phoneNumber)) {
            contacts.remove(phoneNumber); // O(1) average time complexity for deletion
            saveContacts(); // Save changes to file
            return true;
        }
        System.out.println("Error: Contact with phone number " + phoneNumber + " not found.");
        return false;
    }

    /**
     * Searches for contacts whose name or phone number contains the given query string (case-insensitive).
     * @param query The string to search for.
     * @return A Collection of matching Contact objects.
     */
    public Collection<Contact> searchContacts(String query) {
        String lowerCaseQuery = query.toLowerCase();
        // Using Java Stream API for filtering - a modern Java feature
        return contacts.values().stream()
                .filter(contact -> contact.getName().toLowerCase().contains(lowerCaseQuery) ||
                                   contact.getPhoneNumber().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList()); // Collect results into a List
    }

    /**
     * Returns a collection of all contacts.
     * @return A Collection of all Contact objects.
     */
    public Collection<Contact> getAllContacts() {
        return contacts.values();
    }

    /**
     * Saves all contacts to a file using ObjectOutputStream (Serialization).
     * This allows saving complex Java objects directly.
     * Demonstrates File I/O and Exception Handling.
     */
    private void saveContacts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(contacts); // Write the entire HashMap object to file
            System.out.println("Contacts saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving contacts: " + e.getMessage());
        }
    }

    /**
     * Loads contacts from a file using ObjectInputStream (Deserialization).
     * Demonstrates File I/O and Exception Handling.
     */
    private void loadContacts() {
        File file = new File(FILENAME);
        if (!file.exists() || file.length() == 0) {
            System.out.println("No existing contacts file found or file is empty. Starting with empty contact book.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            // Read the HashMap object from file and cast it back
            contacts = (HashMap<String, Contact>) ois.readObject();
            System.out.println("Contacts loaded successfully.");

            // After loading, update the nextId counter in the Contact class
            // to prevent ID conflicts with newly added contacts.
            // Find the maximum ID among loaded contacts.
            long maxId = 0;
            for (Contact contact : contacts.values()) {
                if (contact.getId() > maxId) {
                    maxId = contact.getId();
                }
            }
            Contact.updateNextId(maxId); // Update the static counter
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading contacts: " + e.getMessage());
            // If there's an error loading, ensure contacts map is initialized to avoid NullPointerException
            this.contacts = new HashMap<>();
        }
    }
}
