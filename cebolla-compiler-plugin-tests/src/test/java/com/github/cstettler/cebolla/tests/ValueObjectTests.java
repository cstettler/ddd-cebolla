package com.github.cstettler.cebolla.tests;

import com.github.cstettler.cebolla.stereotype.ValueObject;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
class ValueObjectTests {

    @Nested
    class ObjectStateValueObjectTests {

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
            // act + assert
            EqualsVerifier
                    .forClass(ObjectStateValueObject.class)
                    .usingGetClass()
                    .verify();
        }

        @Test
        void newInstanceViaDefaultConstructor_objectStateValueObject_succeeds() throws Exception {
            // arrange
            Constructor<ObjectStateValueObject> constructor = defaultConstructorFor(ObjectStateValueObject.class);

            // act
            ObjectStateValueObject instance = constructor.newInstance();

            // assert
            assertThat(instance).isNotNull();
        }

    }


    @Nested
    class PrimitiveStateValueObjectTests {

        @Test
        void equals_valueObjectWithSamePrimitiveState_returnsTrue() {
            // arrange
            PrimitiveStateValueObject instanceOne = new PrimitiveStateValueObject(true, (byte) 0, 'a', (short) 1, 2, 3L, 1.23d, 2.34f);
            PrimitiveStateValueObject instanceTwo = new PrimitiveStateValueObject(true, (byte) 0, 'a', (short) 1, 2, 3L, 1.23d, 2.34f);

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isTrue();
        }

        @Test
        void equals_valueObjectWithDifferentPrimitiveState_returnsFalse() {
            // arrange
            PrimitiveStateValueObject instanceOne = new PrimitiveStateValueObject(false, (byte) 0, 'a', (short) 1, 2, 3L, 1.23d, 2.34f);
            PrimitiveStateValueObject instanceTwo = new PrimitiveStateValueObject(true, (byte) 0, 'a', (short) 1, 2, 3L, 1.23d, 2.34f);

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isFalse();
        }

        @Test
        void hashCode_valueObjectWithSamePrimitiveState_returnsSameValue() {
            // arrange
            PrimitiveStateValueObject instanceOne = new PrimitiveStateValueObject(true, (byte) 0, 'a', (short) 1, 2, 3L, 1.23d, 2.34f);
            PrimitiveStateValueObject instanceTwo = new PrimitiveStateValueObject(true, (byte) 0, 'a', (short) 1, 2, 3L, 1.23d, 2.34f);

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isEqualTo(hashCodeTwo);
        }

        @Test
        void hashCode_valueObjectWithDifferentPrimitiveState_returnsDifferentValues() {
            // arrange
            PrimitiveStateValueObject instanceOne = new PrimitiveStateValueObject(false, (byte) 0, 'a', (short) 1, 2, 3L, 1.23d, 2.34f);
            PrimitiveStateValueObject instanceTwo = new PrimitiveStateValueObject(true, (byte) 0, 'a', (short) 1, 2, 3L, 1.23d, 2.34f);

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isNotEqualTo(hashCodeTwo);
        }

        @Test
        void equalsAndHashCode_primitiveStateValueObject_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(PrimitiveStateValueObject.class)
                    .usingGetClass()
                    .verify();
        }

        @Test
        void newInstanceViaDefaultConstructor_primitiveStateValueObject_succeeds() throws Exception {
            // arrange
            Constructor<PrimitiveStateValueObject> constructor = defaultConstructorFor(PrimitiveStateValueObject.class);

            // act
            PrimitiveStateValueObject instance = constructor.newInstance();

            // assert
            assertThat(instance).isNotNull();
        }

    }


    @Nested
    class ArrayStateValueObjectTests {

        @Test
        void equals_valueObjectWithSameArrayState_returnsTrue() {
            // arrange
            ArrayStateValueObject instanceOne = new ArrayStateValueObject(new Object[]{"one", "two"}, new String[]{"three", "four"}, new int[]{1, 2});
            ArrayStateValueObject instanceTwo = new ArrayStateValueObject(new Object[]{"one", "two"}, new String[]{"three", "four"}, new int[]{1, 2});

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isTrue();
        }

        @Test
        void equals_valueObjectWithDifferentArrayState_returnsFalse() {
            // arrange
            ArrayStateValueObject instanceOne = new ArrayStateValueObject(new Object[]{"one", "two"}, new String[]{"three", "four"}, new int[]{1, 2});
            ArrayStateValueObject instanceTwo = new ArrayStateValueObject(new Object[]{"one", "three"}, new String[]{"three", "four"}, new int[]{1, 2});

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isFalse();
        }

        @Test
        void hashCode_valueObjectWithSameArrayState_returnsSameValue() {
            // arrange
            ArrayStateValueObject instanceOne = new ArrayStateValueObject(new Object[]{"one", "two"}, new String[]{"three", "four"}, new int[]{1, 2});
            ArrayStateValueObject instanceTwo = new ArrayStateValueObject(new Object[]{"one", "two"}, new String[]{"three", "four"}, new int[]{1, 2});

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isEqualTo(hashCodeTwo);
        }

        @Test
        void hashCode_valueObjectWithDifferentArrayState_returnsDifferentValues() {
            // arrange
            ArrayStateValueObject instanceOne = new ArrayStateValueObject(new Object[]{"one", "two"}, new String[]{"three", "four"}, new int[]{1, 2});
            ArrayStateValueObject instanceTwo = new ArrayStateValueObject(new Object[]{"one", "three"}, new String[]{"three", "four"}, new int[]{1, 2});

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isNotEqualTo(hashCodeTwo);
        }

        @Test
        void equalsAndHashCode_arrayStateValueObject_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(ArrayStateValueObject.class)
                    .usingGetClass()
                    .verify();
        }

        @Test
        void newInstanceViaDefaultConstructor_arrayStateValueObject_succeeds() throws Exception {
            // arrange
            Constructor<ArrayStateValueObject> constructor = defaultConstructorFor(ArrayStateValueObject.class);

            // act
            ArrayStateValueObject instance = constructor.newInstance();

            // assert
            assertThat(instance).isNotNull();
        }

    }


    @Nested
    class NestedValueObjectStateValueObjectTests {

        @Test
        void equals_valueObjectWithSameNestedValueObjectState_returnsTrue() {
            // arrange
            NestedValueObjectStateValueObject instanceOne = new NestedValueObjectStateValueObject(new InnerValueObject("one"));
            NestedValueObjectStateValueObject instanceTwo = new NestedValueObjectStateValueObject(new InnerValueObject("one"));

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isTrue();
        }

        @Test
        void equals_valueObjectWithDifferentNestedValueObjectState_returnsFalse() {
            // arrange
            NestedValueObjectStateValueObject instanceOne = new NestedValueObjectStateValueObject(new InnerValueObject("one"));
            NestedValueObjectStateValueObject instanceTwo = new NestedValueObjectStateValueObject(new InnerValueObject("two"));

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isFalse();
        }

        @Test
        void hashCode_valueObjectWithSameNestedValueObjectState_returnsSameValue() {
            // arrange
            NestedValueObjectStateValueObject instanceOne = new NestedValueObjectStateValueObject(new InnerValueObject("one"));
            NestedValueObjectStateValueObject instanceTwo = new NestedValueObjectStateValueObject(new InnerValueObject("one"));

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isEqualTo(hashCodeTwo);
        }

        @Test
        void hashCode_valueObjectWithDifferentNestedValueObjectState_returnsDifferentValues() {
            // arrange
            NestedValueObjectStateValueObject instanceOne = new NestedValueObjectStateValueObject(new InnerValueObject("one"));
            NestedValueObjectStateValueObject instanceTwo = new NestedValueObjectStateValueObject(new InnerValueObject("two"));

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isNotEqualTo(hashCodeTwo);
        }

        @Test
        void equalsAndHashCode_nestedValueObject_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(NestedValueObjectStateValueObject.class)
                    .usingGetClass()
                    .verify();
        }

        @Test
        void newInstanceViaDefaultConstructor_nestedValueObjectStateValueObject_succeeds() throws Exception {
            // arrange
            Constructor<NestedValueObjectStateValueObject> constructor = defaultConstructorFor(NestedValueObjectStateValueObject.class);

            // act
            NestedValueObjectStateValueObject instance = constructor.newInstance();

            // assert
            assertThat(instance).isNotNull();
        }

    }


    @Nested
    class MixedTypeStateValueObjectTests {

        @Test
        void equals_valueObjectWithSameMixedTypeState_returnsTrue() {
            // arrange
            MixedTypeStateValueObject instanceOne = new MixedTypeStateValueObject("one", 1, new Object[]{"two", "three"});
            MixedTypeStateValueObject instanceTwo = new MixedTypeStateValueObject("one", 1, new Object[]{"two", "three"});

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isTrue();
        }

        @Test
        void equals_valueObjectWithDifferentMixedTypeState_returnsFalse() {
            // arrange
            MixedTypeStateValueObject instanceOne = new MixedTypeStateValueObject("one", 1, new Object[]{"two", "three"});
            MixedTypeStateValueObject instanceTwo = new MixedTypeStateValueObject("two", 1, new Object[]{"two", "three"});

            // act
            boolean result = instanceOne.equals(instanceTwo);

            // assert
            assertThat(result).isFalse();
        }

        @Test
        void hashCode_valueObjectWithSameMixedTypeState_returnsSameValue() {
            // arrange
            MixedTypeStateValueObject instanceOne = new MixedTypeStateValueObject("one", 1, new Object[]{"two", "three"});
            MixedTypeStateValueObject instanceTwo = new MixedTypeStateValueObject("one", 1, new Object[]{"two", "three"});

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isEqualTo(hashCodeTwo);
        }

        @Test
        void hashCode_valueObjectWithDifferentMixedTypeState_returnsDifferentValues() {
            // arrange
            MixedTypeStateValueObject instanceOne = new MixedTypeStateValueObject("one", 1, new Object[]{"two", "three"});
            MixedTypeStateValueObject instanceTwo = new MixedTypeStateValueObject("two", 1, new Object[]{"two", "three"});

            // act
            int hashCodeOne = instanceOne.hashCode();
            int hashCodeTwo = instanceTwo.hashCode();

            // assert
            assertThat(hashCodeOne).isNotEqualTo(hashCodeTwo);
        }

        @Test
        void equalsAndHashCode_mixedTypeStateValueObject_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(MixedTypeStateValueObject.class)
                    .usingGetClass()
                    .verify();
        }

        @Test
        void newInstanceViaDefaultConstructor_mixedTypeStateValueObject_succeeds() throws Exception {
            // arrange
            Constructor<MixedTypeStateValueObject> constructor = defaultConstructorFor(MixedTypeStateValueObject.class);

            // act
            MixedTypeStateValueObject instance = constructor.newInstance();

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


    @ValueObject
    static class ObjectStateValueObject {

        private final String valueOne;
        private final String valueTwo;

        ObjectStateValueObject(String valueOne, String valueTwo) {
            this.valueOne = valueOne;
            this.valueTwo = valueTwo;
        }

    }


    @ValueObject
    static class PrimitiveStateValueObject {

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
    static class ArrayStateValueObject {

        private final Object[] objectArrayValue;
        private final String[] stringArrayValue;
        private final int[] intArrayValue;

        ArrayStateValueObject(Object[] objectArrayValue, String[] stringArrayValue, int[] intArrayValue) {
            this.objectArrayValue = objectArrayValue;
            this.stringArrayValue = stringArrayValue;
            this.intArrayValue = intArrayValue;
        }

    }


    @ValueObject
    static class InnerValueObject {

        private final String value;

        InnerValueObject(String value) {
            this.value = value;
        }

    }


    @ValueObject
    static class NestedValueObjectStateValueObject {

        private final InnerValueObject innerValueObject;

        NestedValueObjectStateValueObject(InnerValueObject innerValueObject) {
            this.innerValueObject = innerValueObject;
        }

    }


    @ValueObject
    static class MixedTypeStateValueObject {

        private final String stringValue;
        private final int intValue;
        private final Object[] objectArrayValue;

        MixedTypeStateValueObject(String stringValue, int intValue, Object[] objectArrayValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
            this.objectArrayValue = objectArrayValue;
        }

    }

}
