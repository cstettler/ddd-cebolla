package com.github.cstettler.cebolla.tests;

import com.github.cstettler.cebolla.stereotype.Aggregate;
import com.github.cstettler.cebolla.stereotype.AggregateId;
import com.github.cstettler.cebolla.stereotype.ValueObject;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
class AggregateTests {

    @Nested
    class ObjectTypeAggregateIdAggregateTests {

        @Test
        void equals_aggregateWithSameObjectTypeAggregateId_returnsTrue() {
            // arrange
            ObjectTypeAggregateIdAggregate instanceOne = new ObjectTypeAggregateIdAggregate("id-one", "two");
            ObjectTypeAggregateIdAggregate instanceTwo = new ObjectTypeAggregateIdAggregate("id-one", "two");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isTrue();
        }

        @Test
        void equals_aggregateWithDifferentObjectTypeAggregateId_returnsFalse() {
            // arrange
            ObjectTypeAggregateIdAggregate instanceOne = new ObjectTypeAggregateIdAggregate("id-one", "two");
            ObjectTypeAggregateIdAggregate instanceTwo = new ObjectTypeAggregateIdAggregate("id-two", "two");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isFalse();
        }

        @Test
        void hashCode_aggregateWithSameObjectTypeAggregateId_returnsSameValue() {
            // arrange
            ObjectTypeAggregateIdAggregate instanceOne = new ObjectTypeAggregateIdAggregate("id-one", "two");
            ObjectTypeAggregateIdAggregate instanceTwo = new ObjectTypeAggregateIdAggregate("id-one", "two");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isEqualTo(hashCodeTwo);
        }

        @Test
        void hashCode_aggregateWithDifferentObjectTypeAggregateId_returnsDifferentValues() {
            // arrange
            ObjectTypeAggregateIdAggregate instanceOne = new ObjectTypeAggregateIdAggregate("id-one", "two");
            ObjectTypeAggregateIdAggregate instanceTwo = new ObjectTypeAggregateIdAggregate("id-two", "two");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isNotEqualTo(hashCodeTwo);
        }

        @Test
        void equalsAndHashCode_objectTypeAggregateIdAggregate_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(ObjectTypeAggregateIdAggregate.class)
                    .usingGetClass()
                    .withIgnoredFields("value")
                    .verify();
        }

        @Test
        void newInstanceViaDefaultConstructor_objectTypeAggregateIdAggregate_succeeds() throws Exception {
            // arrange
            Constructor<ObjectTypeAggregateIdAggregate> constructor = defaultConstructorFor(ObjectTypeAggregateIdAggregate.class);

            // act
            ObjectTypeAggregateIdAggregate instance = constructor.newInstance();

            // assert
            assertThat(instance).isNotNull();
        }

    }


    @Nested
    class BooleanTypeAggregateIdAggregateTests {

        @Test
        void equals_aggregateWithSameBooleanTypeAggregateId_returnsTrue() {
            // arrange
            BooleanTypeAggregateIdAggregate instanceOne = new BooleanTypeAggregateIdAggregate(true, "value-one");
            BooleanTypeAggregateIdAggregate instanceTwo = new BooleanTypeAggregateIdAggregate(true, "value-two");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isTrue();
        }

        @Test
        void equals_aggregateWithDifferentBooleanTypeAggregateId_returnsFalse() {
            // arrange
            BooleanTypeAggregateIdAggregate instanceOne = new BooleanTypeAggregateIdAggregate(false, "value-one");
            BooleanTypeAggregateIdAggregate instanceTwo = new BooleanTypeAggregateIdAggregate(true, "value-one");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isFalse();
        }

        @Test
        void hashCode_aggregateWithSameBooleanTypeAggregateId_returnsSameValue() {
            // arrange
            BooleanTypeAggregateIdAggregate instanceOne = new BooleanTypeAggregateIdAggregate(true, "value-one");
            BooleanTypeAggregateIdAggregate instanceTwo = new BooleanTypeAggregateIdAggregate(true, "value-two");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isEqualTo(hashCodeTwo);
        }

        @Test
        void hashCode_aggregateWithDifferentBooleanTypeAggregateId_returnsDifferentValues() {
            // arrange
            BooleanTypeAggregateIdAggregate instanceOne = new BooleanTypeAggregateIdAggregate(false, "value-one");
            BooleanTypeAggregateIdAggregate instanceTwo = new BooleanTypeAggregateIdAggregate(true, "value-one");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isNotEqualTo(hashCodeTwo);
        }

        @Test
        void equalsAndHashCode_booleanTypeAggregateIdAggregate_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(BooleanTypeAggregateIdAggregate.class)
                    .usingGetClass()
                    .withIgnoredFields("value")
                    .verify();
        }

        @Test
        void newInstanceViaDefaultConstructor_booleanTypeAggregateIdAggregate_succeeds() throws Exception {
            // arrange
            Constructor<BooleanTypeAggregateIdAggregate> constructor = defaultConstructorFor(BooleanTypeAggregateIdAggregate.class);

            // act
            BooleanTypeAggregateIdAggregate instance = constructor.newInstance();

            // assert
            assertThat(instance).isNotNull();
        }

    }


    @Nested
    class IntTypeAggregateIdAggregateTests {

        @Test
        void equals_aggregateWithSameIntTypeAggregateId_returnsTrue() {
            // arrange
            IntTypeAggregateIdAggregate instanceOne = new IntTypeAggregateIdAggregate(1, "value-one");
            IntTypeAggregateIdAggregate instanceTwo = new IntTypeAggregateIdAggregate(1, "value-two");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isTrue();
        }

        @Test
        void equals_aggregateWithDifferentIntTypeAggregateId_returnsFalse() {
            // arrange
            IntTypeAggregateIdAggregate instanceOne = new IntTypeAggregateIdAggregate(1, "value-one");
            IntTypeAggregateIdAggregate instanceTwo = new IntTypeAggregateIdAggregate(2, "value-one");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isFalse();
        }

        @Test
        void hashCode_aggregateWithSameIntTypeAggregateId_returnsSameValue() {
            // arrange
            IntTypeAggregateIdAggregate instanceOne = new IntTypeAggregateIdAggregate(1, "value-one");
            IntTypeAggregateIdAggregate instanceTwo = new IntTypeAggregateIdAggregate(1, "value-two");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isEqualTo(hashCodeTwo);
        }

        @Test
        void hashCode_aggregateWithDifferentIntTypeAggregateId_returnsDifferentValues() {
            // arrange
            IntTypeAggregateIdAggregate instanceOne = new IntTypeAggregateIdAggregate(1, "value-one");
            IntTypeAggregateIdAggregate instanceTwo = new IntTypeAggregateIdAggregate(2, "value-one");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isNotEqualTo(hashCodeTwo);
        }

        @Test
        void equalsAndHashCode_intTypeAggregateIdAggregate_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(IntTypeAggregateIdAggregate.class)
                    .usingGetClass()
                    .withIgnoredFields("value")
                    .verify();
        }

        @Test
        void newInstanceViaDefaultConstructor_intTypeAggregateIdAggregate_succeeds() throws Exception {
            // arrange
            Constructor<IntTypeAggregateIdAggregate> constructor = defaultConstructorFor(IntTypeAggregateIdAggregate.class);

            // act
            IntTypeAggregateIdAggregate instance = constructor.newInstance();

            // assert
            assertThat(instance).isNotNull();
        }

    }


    @Nested
    class DoubleTypeAggregateIdAggregateTests {

        @Test
        void equals_aggregateWithSameDoubleTypeAggregateId_returnsTrue() {
            // arrange
            DoubleTypeAggregateIdAggregate instanceOne = new DoubleTypeAggregateIdAggregate(1.23, "value-one");
            DoubleTypeAggregateIdAggregate instanceTwo = new DoubleTypeAggregateIdAggregate(1.23, "value-two");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isTrue();
        }

        @Test
        void equals_aggregateWithDifferentDoubleTypeAggregateId_returnsFalse() {
            // arrange
            DoubleTypeAggregateIdAggregate instanceOne = new DoubleTypeAggregateIdAggregate(1.23, "value-one");
            DoubleTypeAggregateIdAggregate instanceTwo = new DoubleTypeAggregateIdAggregate(2.34, "value-one");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isFalse();
        }

        @Test
        void hashCode_aggregateWithSameDoubleTypeAggregateId_returnsSameValue() {
            // arrange
            DoubleTypeAggregateIdAggregate instanceOne = new DoubleTypeAggregateIdAggregate(1.23, "value-one");
            DoubleTypeAggregateIdAggregate instanceTwo = new DoubleTypeAggregateIdAggregate(1.23, "value-two");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isEqualTo(hashCodeTwo);
        }

        @Test
        void hashCode_aggregateWithDifferentDoubleTypeAggregateId_returnsDifferentValues() {
            // arrange
            DoubleTypeAggregateIdAggregate instanceOne = new DoubleTypeAggregateIdAggregate(1.23, "value-one");
            DoubleTypeAggregateIdAggregate instanceTwo = new DoubleTypeAggregateIdAggregate(2.34, "value-one");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isNotEqualTo(hashCodeTwo);
        }

        @Test
        void equalsAndHashCode_doubleTypeAggregateIdAggregate_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(DoubleTypeAggregateIdAggregate.class)
                    .usingGetClass()
                    .withIgnoredFields("value")
                    .verify();
        }

        @Test
        void newInstanceViaDefaultConstructor_doubleTypeAggregateIdAggregate_succeeds() throws Exception {
            // arrange
            Constructor<DoubleTypeAggregateIdAggregate> constructor = defaultConstructorFor(DoubleTypeAggregateIdAggregate.class);

            // act
            DoubleTypeAggregateIdAggregate instance = constructor.newInstance();

            // assert
            assertThat(instance).isNotNull();
        }

    }


    @Nested
    class FloatTypeAggregateIdAggregateTests {

        @Test
        void equals_aggregateWithSameFloatTypeAggregateId_returnsTrue() {
            // arrange
            FloatTypeAggregateIdAggregate instanceOne = new FloatTypeAggregateIdAggregate(1.23f, "value-one");
            FloatTypeAggregateIdAggregate instanceTwo = new FloatTypeAggregateIdAggregate(1.23f, "value-two");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isTrue();
        }

        @Test
        void equals_aggregateWithDifferentFloatTypeAggregateId_returnsFalse() {
            // arrange
            FloatTypeAggregateIdAggregate instanceOne = new FloatTypeAggregateIdAggregate(1.23f, "value-one");
            FloatTypeAggregateIdAggregate instanceTwo = new FloatTypeAggregateIdAggregate(2.34f, "value-one");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isFalse();
        }

        @Test
        void hashCode_aggregateWithSameFloatTypeAggregateId_returnsSameValue() {
            // arrange
            FloatTypeAggregateIdAggregate instanceOne = new FloatTypeAggregateIdAggregate(1.23f, "value-one");
            FloatTypeAggregateIdAggregate instanceTwo = new FloatTypeAggregateIdAggregate(1.23f, "value-two");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isEqualTo(hashCodeTwo);
        }

        @Test
        void hashCode_aggregateWithDifferentFloatTypeAggregateId_returnsDifferentValues() {
            // arrange
            FloatTypeAggregateIdAggregate instanceOne = new FloatTypeAggregateIdAggregate(1.23f, "value-one");
            FloatTypeAggregateIdAggregate instanceTwo = new FloatTypeAggregateIdAggregate(2.34f, "value-one");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isNotEqualTo(hashCodeTwo);
        }

        @Test
        void equalsAndHashCode_floatTypeAggregateIdAggregate_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(FloatTypeAggregateIdAggregate.class)
                    .usingGetClass()
                    .withIgnoredFields("value")
                    .verify();
        }

        @Test
        void newInstanceViaDefaultConstructor_floatTypeAggregateIdAggregate_succeeds() throws Exception {
            // arrange
            Constructor<FloatTypeAggregateIdAggregate> constructor = defaultConstructorFor(FloatTypeAggregateIdAggregate.class);

            // act
            FloatTypeAggregateIdAggregate instance = constructor.newInstance();

            // assert
            assertThat(instance).isNotNull();
        }

    }


    @Nested
    class ArrayTypeAggregateIdAggregateTests {

        @Test
        void equals_aggregateWithSameArrayTypeAggregateId_returnsTrue() {
            // arrange
            ArrayTypeAggregateIdAggregate instanceOne = new ArrayTypeAggregateIdAggregate(new Object[]{"id-one", "id-two"}, "value-one");
            ArrayTypeAggregateIdAggregate instanceTwo = new ArrayTypeAggregateIdAggregate(new Object[]{"id-one", "id-two"}, "value-two");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isTrue();
        }

        @Test
        void equals_aggregateWithDifferentArrayTypeAggregateId_returnsFalse() {
            // arrange
            ArrayTypeAggregateIdAggregate instanceOne = new ArrayTypeAggregateIdAggregate(new Object[]{"id-one", "id-two"}, "value-one");
            ArrayTypeAggregateIdAggregate instanceTwo = new ArrayTypeAggregateIdAggregate(new Object[]{"id-one", "id-three"}, "value-one");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isFalse();
        }

        @Test
        void hashCode_aggregateWithSameArrayTypeAggregateId_returnsSameValue() {
            // arrange
            ArrayTypeAggregateIdAggregate instanceOne = new ArrayTypeAggregateIdAggregate(new Object[]{"id-one", "id-two"}, "value-one");
            ArrayTypeAggregateIdAggregate instanceTwo = new ArrayTypeAggregateIdAggregate(new Object[]{"id-one", "id-two"}, "value-two");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isEqualTo(hashCodeTwo);
        }

        @Test
        void hashCode_aggregateWithDifferentArrayTypeAggregateId_returnsDifferentValues() {
            // arrange
            ArrayTypeAggregateIdAggregate instanceOne = new ArrayTypeAggregateIdAggregate(new Object[]{"id-one", "id-two"}, "value-one");
            ArrayTypeAggregateIdAggregate instanceTwo = new ArrayTypeAggregateIdAggregate(new Object[]{"id-one", "id-three"}, "value-one");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isNotEqualTo(hashCodeTwo);
        }

        @Test
        void equalsAndHashCode_arrayTypeAggregateIdAggregate_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(ArrayTypeAggregateIdAggregate.class)
                    .usingGetClass()
                    .withIgnoredFields("value")
                    .verify();
        }

        @Test
        void newInstanceViaDefaultConstructor_arrayTypeAggregateIdAggregate_succeeds() throws Exception {
            // arrange
            Constructor<ArrayTypeAggregateIdAggregate> constructor = defaultConstructorFor(ArrayTypeAggregateIdAggregate.class);

            // act
            ArrayTypeAggregateIdAggregate instance = constructor.newInstance();

            // assert
            assertThat(instance).isNotNull();
        }

    }


    @Nested
    class ValueObjectTypeAggregateIdAggregateTests {

        @Test
        void equals_aggregateWithSameNestedAggregateTypeAggregateId_returnsTrue() {
            // arrange
            ValueObjectTypeAggregateIdAggregate instanceOne = new ValueObjectTypeAggregateIdAggregate(new ValueObjectType("id-one"), "value-one");
            ValueObjectTypeAggregateIdAggregate instanceTwo = new ValueObjectTypeAggregateIdAggregate(new ValueObjectType("id-one"), "value-two");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isTrue();
        }

        @Test
        void equals_aggregateWithDifferentNestedAggregateTypeAggregateId_returnsFalse() {
            // arrange
            ValueObjectTypeAggregateIdAggregate instanceOne = new ValueObjectTypeAggregateIdAggregate(new ValueObjectType("id-one"), "value-one");
            ValueObjectTypeAggregateIdAggregate instanceTwo = new ValueObjectTypeAggregateIdAggregate(new ValueObjectType("id-two"), "value-one");

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isFalse();
        }

        @Test
        void hashCode_aggregateWithSameNestedAggregateTypeAggregateId_returnsSameValue() {
            // arrange
            ValueObjectTypeAggregateIdAggregate instanceOne = new ValueObjectTypeAggregateIdAggregate(new ValueObjectType("id-one"), "value-one");
            ValueObjectTypeAggregateIdAggregate instanceTwo = new ValueObjectTypeAggregateIdAggregate(new ValueObjectType("id-one"), "value-two");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isEqualTo(hashCodeTwo);
        }

        @Test
        void hashCode_aggregateWithDifferentNestedAggregateTypeAggregateId_returnsDifferentValues() {
            // arrange
            ValueObjectTypeAggregateIdAggregate instanceOne = new ValueObjectTypeAggregateIdAggregate(new ValueObjectType("id-one"), "value-one");
            ValueObjectTypeAggregateIdAggregate instanceTwo = new ValueObjectTypeAggregateIdAggregate(new ValueObjectType("id-two"), "value-one");

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isNotEqualTo(hashCodeTwo);
        }

        @Test
        void equalsAndHashCode_nestedAggregate_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(ValueObjectTypeAggregateIdAggregate.class)
                    .usingGetClass()
                    .withIgnoredFields("value")
                    .verify();
        }

        @Test
        void newInstanceViaDefaultConstructor_valueObjectTypeAggregateIdAggregate_succeeds() throws Exception {
            // arrange
            Constructor<ValueObjectTypeAggregateIdAggregate> constructor = defaultConstructorFor(ValueObjectTypeAggregateIdAggregate.class);

            // act
            ValueObjectTypeAggregateIdAggregate instance = constructor.newInstance();

            // assert
            assertThat(instance).isNotNull();
        }

    }


    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> defaultConstructorFor(Class<T> clazz) {
        return stream(clazz.getDeclaredConstructors())
                .filter((constructor) -> constructor.getParameterCount() == 0)
                .map((constructor) -> (Constructor<T>) constructor)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("no default constructor found in class '" + clazz.getName() + "'"));
    }


    @Aggregate
    static class ObjectTypeAggregateIdAggregate {

        private final String objectTypeId;
        private final String value;

        ObjectTypeAggregateIdAggregate(String objectTypeId, String value) {
            this.objectTypeId = objectTypeId;
            this.value = value;
        }

        @AggregateId
        String objectTypeId() {
            return objectTypeId;
        }

    }


    @Aggregate
    static class BooleanTypeAggregateIdAggregate {

        private final boolean booleanTypeId;
        private final String value;

        BooleanTypeAggregateIdAggregate(boolean booleanTypeId, String value) {
            this.booleanTypeId = booleanTypeId;
            this.value = value;
        }

        @AggregateId
        boolean booleanTypeId() {
            return booleanTypeId;
        }

    }


    @Aggregate
    static class IntTypeAggregateIdAggregate {

        private final int intTypeId;
        private final String value;

        IntTypeAggregateIdAggregate(int intTypeId, String value) {
            this.intTypeId = intTypeId;
            this.value = value;
        }

        @AggregateId
        int intTypeId() {
            return intTypeId;
        }

    }


    @Aggregate
    static class DoubleTypeAggregateIdAggregate {

        private final double doubleTypeId;
        private final String value;

        DoubleTypeAggregateIdAggregate(double doubleTypeId, String value) {
            this.doubleTypeId = doubleTypeId;
            this.value = value;
        }

        @AggregateId
        double doubleTypeId() {
            return doubleTypeId;
        }

    }


    @Aggregate
    static class FloatTypeAggregateIdAggregate {

        private final float floatTypeId;
        private final String value;

        FloatTypeAggregateIdAggregate(float floatTypeId, String value) {
            this.floatTypeId = floatTypeId;
            this.value = value;
        }

        @AggregateId
        float floatTypeId() {
            return floatTypeId;
        }

    }


    @Aggregate
    static class ArrayTypeAggregateIdAggregate {

        private final Object[] arrayTypeId;
        private final String value;

        ArrayTypeAggregateIdAggregate(Object[] arrayTypeId, String value) {
            this.arrayTypeId = arrayTypeId;
            this.value = value;
        }

        @AggregateId
        Object[] arrayTypeId() {
            return arrayTypeId;
        }

    }


    @ValueObject
    static class ValueObjectType {

        private final String value;

        ValueObjectType(String value) {
            this.value = value;
        }

    }


    @Aggregate
    static class ValueObjectTypeAggregateIdAggregate {

        private final ValueObjectType valueObjectTypeId;
        private final String value;

        ValueObjectTypeAggregateIdAggregate(ValueObjectType valueObjectTypeId, String value) {
            this.valueObjectTypeId = valueObjectTypeId;
            this.value = value;
        }

        @AggregateId
        ValueObjectType valueObjectTypeId() {
            return valueObjectTypeId;
        }

    }

}
