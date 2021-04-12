
package person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import uod.gla.io.File;
import uod.gla.menu.Finalisable;
import uod.gla.menu.MenuBuilder;
import uod.gla.menu.MenuItem;
import uod.gla.util.CollectionUtils;
import uod.gla.util.Reader;

/**
 * @author Chi
 */
public class AppLauncher implements Finalisable {
    
    // A list that holds the Person objects we create
    private static List<Person> personList = new ArrayList<>();


    // This is used to "serialise" the list of persons
    private static File personListFile = new File("data", "contactList");



    // The object on which the name of the method to be invoked is searched for
    // Also used to provide access to the non-static methods e.g. finalise
    private static AppLauncher appObject = new AppLauncher();
    
    public static void main(String[] args) {
        
        // Welcome message
        System.out.println("Welcome to the Persons Management App");
        
        // retrieve data from disk at start up
        List<Person> retrievedPersonList = personListFile.<List<Person>>retrieve(true);
        if (retrievedPersonList != null) {
            personList = retrievedPersonList;
        }
        
        // Menu options
        MenuItem c = new MenuItem("C", "Create a new person", appObject, "create");
        MenuItem d = new MenuItem("D", "Display all persons", appObject, "display");
        MenuItem e = new MenuItem("E", "Edit a person's details", appObject, "edit");
        MenuItem m = new MenuItem("M", "Marry two persons", appObject, "marry");
        MenuItem p = new MenuItem("P", "Procreate a new person", appObject, "procreate");
        MenuItem v = new MenuItem("V", "Divorce two persons", appObject, "divorce");
        MenuBuilder.displayMenu(appObject, c, d, e, m, p, v);
        
        // save to disk at shutdown
        appObject.finalise();
        System.out.println("Thanks for using this app!");
    }

    @Override
    public void finalise() {
        personListFile.save((Serializable)personList);
    }
    
    // public static void methods from here on...
    
    public static void create() {
        String f = Reader.readName("First Name: ");
        String l = Reader.readName("Last Name: ");
        int a = Reader.readInt("Age (Years): ", Person.MIN_AGE, Person.MAX_AGE);
        int h = Reader.readInt("Height (cm): ", Person.MIN_HEIGHT, Person.MAX_HEIGHT);
        int w = Reader.readInt("Weight (kg): ", Person.MIN_WEIGHT, Person.MAX_WEIGHT);
        Person.Gender g = Reader.readEnum("Please select a gender: ", Person.Gender.class);
        Person person = new Person(f, l, a, h, w, g);
        personList.add(person);
        System.out.println("Person details saved!");
    }
    
    public static void display() {
        Collections.sort(personList);
        for (Person p : personList) {
            System.out.println(p + "\n");
        }
        System.out.println(personList.size() + " person(s) displayed!\n");
    }
    
    // Notice that this method is private. It is only used internally
    private static Person search(String key) {
        Collection<Person> results = CollectionUtils.search(key, personList);
        if (results == null || results.isEmpty()) {
            return null;
        }
        return Reader.readObject("Please select a person from the list", results);
    }
    
    public static void edit() {
        String key = Reader.readLine("Enter a person's detail: ");
        Person person = search(key);
        if (person == null) {
            System.out.println("No person found!");
            return;
        }
        System.out.println(person);
        boolean edited = false;
        if (Reader.readBoolean("Do you want to change the first name?")) {
            person.setFirstName(Reader.readName("New first name: "));
            edited = true;
        }
        if (Reader.readBoolean("Do you want to change the last name?")) {
            person.setLastName(Reader.readName("New last name: "));
            edited = true;
        }
        if (Reader.readBoolean("Do you want to change the age?")) {
            person.setAge(Reader.readInt("New age (years): ", Person.MIN_AGE, Person.MAX_AGE));
            edited = true;
        }
        if (Reader.readBoolean("Do you want to change the height?")) {
            person.setHeight(Reader.readInt("New height (cm): ", Person.MIN_HEIGHT, Person.MAX_HEIGHT));
            edited = true;
        }
        if (Reader.readBoolean("Do you want to change the weight?")) {
            person.setHeight(Reader.readInt("New weight (cm): ", Person.MIN_WEIGHT, Person.MAX_WEIGHT));
            edited = true;
        }
        if (edited) {
            System.out.println("Person successfully updated!");
        } else {
            System.out.println("No detail was changed!");
        }
    }
    
    public static void marry() {
        String key = Reader.readLine("Enter a person's detail: ");
        Person person = search(key);
        if (person == null) {
            System.out.println("No person found!");
            return;
        }
        key = Reader.readLine("Enter " + person.getFirstName() 
                + "'s intended partner's detail: ");
        Person suitor = search(key);
        if (suitor == null) {
            System.out.println("No person found!");
            return;
        }
        person.marry(suitor);
        System.out.println("Marriage successful!");
    }
    
    public static void divorce() {
        String key = Reader.readLine("Enter a person's detail: ");
        Person person = search(key);
        if (person == null) {
            System.out.println("No person found!");
            return;
        }
        if (!person.isMarried()) {
            System.out.println(person.getFirstName() + " is not married!");
        } else {
            person.divorce();
            System.out.println("Divorce successful!");
        }
    }
    
    public static void procreate() {
        String key = Reader.readLine("Enter a person's detail: ");
        Person person = search(key);
        if (person == null) {
            System.out.println("No person found!");
            return;
        }
        if (!person.isMarried()) {
            System.out.println(person.getFirstName() + " is not married!");
        } else if (person.getGender() != Person.Gender.Female) {
            System.out.println(person.getFirstName() + " is not female!");
        } else {
            Person baby;
            if (Reader.readBoolean("Do you want to provide a baby name?")) {
                baby = person.procreate(Reader.readName("Baby name: "));
            } else {
                baby = person.procreate();
            }
            personList.add(baby);
            System.out.println(person.getFirstName() + " now has a new baby " 
                    + (baby.getGender() == Person.Gender.Male ? "boy!" : "girl!"));
        }
    }
    
}
