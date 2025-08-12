// ContactApp.java
// This is the main class for the console-based Smart Contact Book application.
// It handles user interaction and calls methods from ContactManager.

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ContactApp {

    private static ContactManager contactManager = new ContactManager(); // Manages all contacts
    private static Scanner scanner = new Scanner(System.in); // For reading user input

    public static void main(String[] args) {
        System.out.println("Welcome to the Smart Contact Book!");
        int choice;
        do {
            displayMenu(); // Show menu options to the user
            choice = getUserChoice(); // Get user's menu choice

            switch (choice) {
                case 1:
                    addContact();
                    break;
                case 2:
                    viewAllContacts();
                    break;
                case 3:
                    searchContact();
                    break;
                case 4:
                    updateContact();
                    break;
                case 5:
                    deleteContact();
                    break;
                case 6:
                    System.out.println("Exiting Contact Book. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
            System.out.println(); // Add a new line for better readability
        } while (choice != 6); // Continue loop until user chooses to exit

        scanner.close(); // Close the scanner to release system resources
    }

    /**
     * Displays the main menu options to the user.
     */
    private static void displayMenu() {
        System.out.println("--- Menu ---");
        System.out.println("1. Add Contact");
        System.out.println("2. View All Contacts");
        System.out.println("3. Search Contact");
        System.out.println("4. Update Contact");
        System.out.println("5. Delete Contact");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Gets the user's integer choice from the console.
     * Handles invalid input (non-numeric).
     * @return The user's choice, or -1 if input is invalid.
     */
    private static int getUserChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Consume the invalid input to prevent infinite loop
            return -1; // Return a value that will trigger the default case in switch
        } finally {
            scanner.nextLine(); // Consume the rest of the line after reading the integer
        }
    }

    /**
     * Prompts the user for contact details and adds a new contact.
     */
    private static void addContact() {
        System.out.println("\n--- Add New Contact ---");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim(); // Use trim() to remove leading/trailing spaces
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty. Contact not added.");
            return;
        }

        System.out.print("Enter Phone Number (unique): ");
        String phoneNumber = scanner.nextLine().trim();
        if (phoneNumber.isEmpty()) {
            System.out.println("Phone number cannot be empty. Contact not added.");
            return;
        }

        System.out.print("Enter Email (optional): ");
        String email = scanner.nextLine().trim();

        Contact newContact = new Contact(name, phoneNumber, email);
        if (contactManager.addContact(newContact)) {
            System.out.println("Contact added successfully: " + newContact);
        }
    }

    /**
     * Displays all contacts currently in the system.
     */
    private static void viewAllContacts() {
        System.out.println("\n--- All Contacts ---");
        Collection<Contact> allContacts = contactManager.getAllContacts();
        if (allContacts.isEmpty()) {
            System.out.println("No contacts available.");
        } else {
            // Iterate and print each contact
            allContacts.forEach(System.out::println);
        }
    }

    /**
     * Prompts for a search query and displays matching contacts.
     */
    private static void searchContact() {
        System.out.println("\n--- Search Contact ---");
        System.out.print("Enter name or phone number to search: ");
        String query = scanner.nextLine().trim();
        if (query.isEmpty()) {
            System.out.println("Search query cannot be empty.");
            return;
        }

        Collection<Contact> foundContacts = contactManager.searchContacts(query);
        if (foundContacts.isEmpty()) {
            System.out.println("No contacts found matching '" + query + "'.");
        } else {
            System.out.println("Found Contacts:");
            foundContacts.forEach(System.out::println);
        }
    }

    /**
     * Prompts for a contact to update and new details.
     */
    private static void updateContact() {
        System.out.println("\n--- Update Contact ---");
        System.out.print("Enter the Phone Number of the contact to update: ");
        String oldPhoneNumber = scanner.nextLine().trim();
        if (oldPhoneNumber.isEmpty()) {
            System.out.println("Phone number cannot be empty. Update cancelled.");
            return;
        }

        Contact existingContact = contactManager.getContact(oldPhoneNumber);
        if (existingContact == null) {
            System.out.println("Contact with phone number " + oldPhoneNumber + " not found.");
            return;
        }

        System.out.println("Current Contact Details: " + existingContact);

        System.out.print("Enter New Name (leave blank to keep current: " + existingContact.getName() + "): ");
        String newName = scanner.nextLine().trim();
        if (newName.isEmpty()) {
            newName = existingContact.getName(); // Keep old name if new is blank
        }

        System.out.print("Enter New Phone Number (leave blank to keep current: " + existingContact.getPhoneNumber() + "): ");
        String newPhoneNumber = scanner.nextLine().trim();
        if (newPhoneNumber.isEmpty()) {
            newPhoneNumber = existingContact.getPhoneNumber(); // Keep old phone if new is blank
        }

        System.out.print("Enter New Email (leave blank to keep current: " + (existingContact.getEmail().isEmpty() ? "N/A" : existingContact.getEmail()) + "): ");
        String newEmail = scanner.nextLine().trim();
        if (newEmail.isEmpty()) {
            newEmail = existingContact.getEmail(); // Keep old email if new is blank
        }

        // Create a new Contact object with updated details, but retain the original ID
        // This is important for consistency, especially if IDs are used elsewhere.
        Contact updatedContact = new Contact(newName, newPhoneNumber, newEmail);
        updatedContact.setId(existingContact.getId()); // Preserve the original ID

        if (contactManager.updateContact(oldPhoneNumber, updatedContact)) {
            System.out.println("Contact updated successfully.");
        }
    }

    /**
     * Prompts for a contact to delete.
     */
    private static void deleteContact() {
        System.out.println("\n--- Delete Contact ---");
        System.out.print("Enter the Phone Number of the contact to delete: ");
        String phoneNumber = scanner.nextLine().trim();
        if (phoneNumber.isEmpty()) {
            System.out.println("Phone number cannot be empty. Deletion cancelled.");
            return;
        }

        if (contactManager.deleteContact(phoneNumber)) {
            System.out.println("Contact with phone number " + phoneNumber + " deleted successfully.");
        }
    }
}
