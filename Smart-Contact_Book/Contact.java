// Contact.java
// This class represents a single contact with its details.
// It demonstrates Object-Oriented Programming (OOP) principles like encapsulation.

import java.io.Serializable; // Used to allow Contact objects to be written to/read from files

public class Contact implements Serializable {
    // Unique identifier for the contact. Using a long for simplicity.
    // In a real application, you might use UUID or database-generated IDs.
    private long id;
    private String name;
    private String phoneNumber; // Using String to handle leading zeros or non-numeric characters
    private String email;

    // Static counter to generate unique IDs for new contacts
    private static long nextId = 1;

    // Constructor to create a new Contact object
    public Contact(String name, String phoneNumber, String email) {
        this.id = nextId++; // Assign unique ID and increment for the next contact
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // --- Getters (to access private fields) ---
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    // --- Setters (to modify private fields) ---
    // Note: ID is typically not set after creation, but included for completeness
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Overriding toString() for a user-friendly display of Contact objects
    @Override
    public String toString() {
        return "ID: " + id +
               ", Name: " + name +
               ", Phone: " + phoneNumber +
               ", Email: " + (email.isEmpty() ? "N/A" : email); // Display N/A if email is empty
    }

    // Overriding equals() and hashCode() is crucial when using Contact objects
    // as keys or values in collections like HashMap or HashSet, or for comparing objects.
    // Two contacts are considered equal if their phone numbers are the same (assuming phone number is unique).
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        // For this application, we'll consider phone number as the primary unique identifier for equality.
        // This is a design choice that can be changed based on requirements (e.g., using ID).
        return phoneNumber.equals(contact.phoneNumber);
    }

    @Override
    public int hashCode() {
        return phoneNumber.hashCode(); // Hash code based on phone number
    }

    // Method to update the nextId counter when loading contacts from a file.
    // This ensures new contacts don't get duplicate IDs after loading.
    public static void updateNextId(long lastId) {
        if (lastId >= nextId) {
            nextId = lastId + 1;
        }
    }
}
