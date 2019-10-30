package com.github.cstettler.cebolla.tests;

import com.github.cstettler.cebolla.stereotype.ValueObject;
import nl.jqno.equalsverifier.EqualsVerifier;
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
    void equals_valueObjectWithDifferentObjectState_returnsFalse() {
        // arrange
        ObjectStateValueObject instanceOne = new ObjectStateValueObject("one", "two");
        ObjectStateValueObject instanceTwo = new ObjectStateValueObject("one", "three");

        // act
        boolean result = instanceOne.equals(instanceTwo);

        // assert
        assertThat(result).isFalse();
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

    @Test
    void hashCode_valueObjectWithDifferentObjectState_returnsDifferentValues() {
        // arrange
        ObjectStateValueObject instanceOne = new ObjectStateValueObject("one", "two");
        ObjectStateValueObject instanceTwo = new ObjectStateValueObject("one", "three");

        // act
        int hashCodeOne = instanceOne.hashCode();
        int hashCodeTwo = instanceTwo.hashCode();

        // assert
        assertThat(hashCodeOne).isNotEqualTo(hashCodeTwo);
    }

    @Test
    void equalsAndHashCode_objectStateValueObject_areFormallyCorrect() {
        EqualsVerifier
                .forClass(ObjectStateValueObject.class)
                .usingGetClass()
                .verify();
    }

    @Test
    void equalsAndHashCode_primitiveStateValueObject_areFormallyCorrect() {
        EqualsVerifier
                .forClass(PrimitiveStateValueObject.class)
                .usingGetClass()
                .verify();
    }

    @Test
    void equalsAndHashCode_arrayStateValueObject_areFormallyCorrect() {
        EqualsVerifier
                .forClass(ArrayStateValueObject.class)
                .usingGetClass()
                .verify();
    }

    @Test
    void equalsAndHashCode_nestedValueObject_areFormallyCorrect() {
        EqualsVerifier
                .forClass(NestedValueObject.class)
                .usingGetClass()
                .verify();
    }

    @Test
    void equalsAndHashCode_mixedTypeStateValueObject_areFormallyCorrect() {
        EqualsVerifier
                .forClass(MixedTypeStateValueObject.class)
                .usingGetClass()
                .verify();
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


    @ValueObject
    class PrimitiveStateValueObject {

        private final boolean booleanValue;
        private final byte byteValue;
        private final char charValue;
        private final short shortValue;
        private final int intValue;
        private final long longValue;
        private final double doubleValue;
        private final float floatValue;

        PrimitiveStateValueObject(boolean booleanValue, byte byteValue, char charValue, short shortValue, int intValue, long longValue, double doubleValue, float floatValue) {
            this.booleanValue = booleanValue;
            this.byteValue = byteValue;
            this.charValue = charValue;
            this.shortValue = shortValue;
            this.intValue = intValue;
            this.longValue = longValue;
            this.doubleValue = doubleValue;
            this.floatValue = floatValue;
        }

    }


    @ValueObject
    class ArrayStateValueObject {

        private final String[] stringArrayValue;
        private final int[] intArrayValue;
        private final Object[] objectArrayValue;

        ArrayStateValueObject(String[] stringArrayValue, int[] intArrayValue, Object[] objectArrayValue) {
            this.stringArrayValue = stringArrayValue;
            this.intArrayValue = intArrayValue;
            this.objectArrayValue = objectArrayValue;
        }

    }


    @ValueObject
    class MixedTypeStateValueObject {

        private final String stringValue;
        private final int intValue;
        private final Object[] objectArrayValue;

        MixedTypeStateValueObject(String stringValue, int intValue, Object[] objectArrayValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
            this.objectArrayValue = objectArrayValue;
        }

    }


    @ValueObject
    class NestedValueObject {

        private final ObjectStateValueObject valueObject;

        NestedValueObject(ObjectStateValueObject valueObject) {
            this.valueObject = valueObject;
        }

    }

}
