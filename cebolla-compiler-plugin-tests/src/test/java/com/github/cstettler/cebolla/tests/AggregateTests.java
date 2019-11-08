package com.github.cstettler.cebolla.tests;

import com.github.cstettler.cebolla.stereotype.Aggregate;
import com.github.cstettler.cebolla.stereotype.AggregateId;
import com.github.cstettler.cebolla.stereotype.ValueObject;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
class AggregateTests {

    @Nested
    class ObjectTypeAggregateIdAggregateTests {
        // arrange
        @Aggregate
        class ObjectTypeAggregateIdAggregate {
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

    }


    @Nested
    class BooleanTypeAggregateIdAggregateTests {
        // arrange
        @Aggregate
        class BooleanTypeAggregateIdAggregate {
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
        void equalsAndHashCode_primitiveTypeAggregateIdAggregate_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(BooleanTypeAggregateIdAggregate.class)
                    .usingGetClass()
                    .withIgnoredFields("value")
                    .verify();
        }

    }


    @Nested
    class IntTypeAggregateIdAggregateTests {
        // arrange
        @Aggregate
        class IntTypeAggregateIdAggregate {
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
        void equalsAndHashCode_primitiveTypeAggregateIdAggregate_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(IntTypeAggregateIdAggregate.class)
                    .usingGetClass()
                    .withIgnoredFields("value")
                    .verify();
        }

    }


    @Nested
    class DoubleTypeAggregateIdAggregateTests {
        // arrange
        @Aggregate
        class DoubleTypeAggregateIdAggregate {
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
        void equalsAndHashCode_primitiveTypeAggregateIdAggregate_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(DoubleTypeAggregateIdAggregate.class)
                    .usingGetClass()
                    .withIgnoredFields("value")
                    .verify();
        }

    }


    @Nested
    class FloatTypeAggregateIdAggregateTests {
        // arrange
        @Aggregate
        class FloatTypeAggregateIdAggregate {
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
        void equalsAndHashCode_primitiveTypeAggregateIdAggregate_areFormallyCorrect() {
            // act + assert
            EqualsVerifier
                    .forClass(FloatTypeAggregateIdAggregate.class)
                    .usingGetClass()
                    .withIgnoredFields("value")
                    .verify();
        }

    }


    @Nested
    class ArrayTypeAggregateIdAggregateTests {
        // arrange
        @Aggregate
        class ArrayTypeAggregateIdAggregate {
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

    }


    @Nested
    class ValueObjectTypeAggregateIdAggregateTests {
        // arrange
        @ValueObject
        class ValueObjectType {
            private final String value;

            ValueObjectType(String value) {
                this.value = value;
            }
        }


        @Aggregate
        class ValueObjectTypeAggregateIdAggregate {
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

    }

}
