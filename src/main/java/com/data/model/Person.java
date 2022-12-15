package com.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Person.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    private String name;
    private Integer age;

    /**
     * Equals boolean.
     * @param o the o
     * @return the boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Person)) {
            return false;
        } else {
            Person other = (Person) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$name = this.getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }

                Object this$age = this.getAge();
                Object other$age = other.getAge();
                if (this$age == null) {
                    if (other$age != null) {
                        return false;
                    }
                } else if (!this$age.equals(other$age)) {
                    return false;
                }

                return true;
            }
        }
    }

    /**
     * Can equal boolean.
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(Object other) {
        return other instanceof Person;
    }

    /**
     * Hash code int.
     * @return the int
     */
    @Override
    public int hashCode() {
        Object $name = this.getName();
        int result = 1 * 59 + ($name == null ? 43 : $name.hashCode());
        Object $age = this.getAge();
        result = result * 59 + ($age == null ? 43 : $age.hashCode());
        return result;
    }

    /**
     * To string string.
     * @return the string
     */
    @Override
    public String toString() {
        return "Person(name=" + this.getName() + ", age=" + this.getAge() + ")";
    }
}