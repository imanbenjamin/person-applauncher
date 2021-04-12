
package person;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 * @author Chi
 */
public class Person implements Comparable<Person>, Serializable {
    
    private String firstName;
    private String lastName;
    private int age; // years
    private int height; // cm
    private int weight; // kg
    private Gender gender;
    private Person spouse;
    
    // constants (to improve maintenance)
    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 125;
    public static final int MIN_HEIGHT = 30;
    public static final int MAX_HEIGHT = 250;
    public static final int MIN_WEIGHT = 3;
    public static final int MAX_WEIGHT = 500;

    public Person(String firstName, String lastName, 
            int age, int height, int weight, Gender gender) {
        
        this.firstName = firstName; // Consider a null check
        this.lastName = lastName;   // Consider a null check
        this.gender = gender;       // Consider a null check
        
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new IllegalArgumentException("Invalid age!");
        }
        this.age = age;
        
        if (height < MIN_HEIGHT || height > MAX_HEIGHT) {
            throw new IllegalArgumentException("Invalid height!");
        }
        this.height = height;
        
        if (weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
            throw new IllegalArgumentException("Invalid weight!");
        }
        this.weight = weight;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) throws IllegalArgumentException {
        if (age < this.age || age > MAX_AGE) {
            throw new IllegalArgumentException("Invalid age!");
        }
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) throws IllegalArgumentException {
        if (height < MIN_HEIGHT || height > MAX_HEIGHT || height < this.height - 1) {
            throw new IllegalArgumentException("Invalid height!");
        }
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) throws IllegalArgumentException {
        if (weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
            throw new IllegalArgumentException("Invalid weight!");
        }
        this.weight = weight;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public double getBMI() {
        double heightm = (double) height / 100.0;
        return (double) weight / (heightm * heightm);
    }
    
    public String getBMICat() {
        double BMI = getBMI();
        if (BMI >= 30) {
            return "Obese";
        } else if (BMI >= 25) {
            return "Overweight";
        } else if (BMI >= 18) {
            return "Healthy weight";
        } else {
            return "Underweight";
        }
    }
    
    public boolean isMarried() {
        return spouse != null;
    }

    public Person getSpouse() {
        return spouse;
    }

    public boolean marry(Person suitor) {
        if (this.spouse != null) {
            throw new IllegalStateException(this.firstName + " is married!");
        } else if (suitor.spouse != null) {
            throw new IllegalStateException(suitor.firstName + " is married!");
        } else if (this.age < 18) {
            throw new IllegalStateException(this.firstName + " is too young!");
        } else if (suitor.age < 18) {
            throw new IllegalStateException(suitor.firstName + " is too young!");
        }
        this.spouse = suitor;
        suitor.spouse = this;
        return true;
    }
    
    public void divorce() throws IllegalStateException {
        if (this.spouse != null) {
            this.spouse.spouse = null;
            this.spouse = null;
        } else {
            throw new IllegalStateException(this.firstName + " is not married!");
        }
    }
    
    public Person procreate() throws IllegalStateException {
        return procreate("Baby");
    }

    public Person procreate(String babyFirstName) throws IllegalStateException {
        if (this.gender != Gender.Female) {
            throw new IllegalStateException("You are not female!");
        } else if (this.spouse == null) {
            throw new IllegalStateException("You are not married!");
        }
        int baby = new Random().nextInt(2);
        return new Person(babyFirstName, lastName, MIN_AGE, MIN_HEIGHT, MIN_WEIGHT, 
                baby == 0 ? Gender.Female : Gender.Male);
    }

    @Override
    public String toString() {
        return getFullName() + " (" + gender 
                + ")\n→ Age: " + age 
                + ", Height: " + height 
                + ", Weight: " + weight 
                + (isMarried() ? ("\n→ (Married to " + spouse.getFullName() + ")") : (""));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.firstName);
        hash = 41 * hash + Objects.hashCode(this.lastName);
        hash = 41 * hash + this.age;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (this.age != other.age) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Person o) {
        if (!this.lastName.equals(o.lastName)) {
            return this.lastName.compareTo(o.lastName);
        } else if (!this.firstName.equals(o.firstName)) {
            return this.firstName.compareTo(o.firstName);
        } else {
            return o.age - this.age; // Ensures that older persons come first
        }
    }

    public static enum Gender {
        Male, Female;
    }
    
}
