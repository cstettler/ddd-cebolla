package com.github.cstettler.cebolla.tests;

import com.github.cstettler.cebolla.stereotype.ValueObject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValueObjectTests {

    @Test
    void equals_valueObjectWithSameObjectState_returnsTrue() {
        // arrange
        ObjectStateValueObject instanceOne = new ObjectStateValueObject("one", "two");
        ObjectStateValueObject instanceTwo = new ObjectStateValueObject("one", "two");

        // act
        boolean result = instanceOne.equals(instanceTwo);

        // assert
        assertThat(result).isTrue();
    }

    @Test
    void hashCode_valueObjectWithSameObjectState_returnsSameValue() {
        // arrange
        ObjectStateValueObject instanceOne = new ObjectStateValueObject("one", "two");
        ObjectStateValueObject instanceTwo = new ObjectStateValueObject("one", "two");

        // act
        int hashCodeOne = instanceOne.hashCode();
        int hashCodeTwo = instanceTwo.hashCode();

        // assert
        assertThat(hashCodeOne).isEqualTo(hashCodeTwo);
    }

    @ValueObject
    class ObjectStateValueObject {

        private final String valueOne;
        private final String valueTwo;

        ObjectStateValueObject(String valueOne, String valueTwo) {
            this.valueOne = valueOne;
            this.valueTwo = valueTwo;
        }

    }

}
