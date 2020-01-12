package com.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("自己文書化されたPersonのテスト")
class PersonSelfDocumentingTest {

    private Person taroBornOn20190503;
    private Person jiroBirthdayUnknown;

    @BeforeAll
    void setup() {
        taroBornOn20190503 = new Person("太郎", "山田", LocalDate.of(2019, 5, 3));
        jiroBirthdayUnknown = new Person("次郎", "佐藤");
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("年齢")
    class GetAge {

        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        @Nested
        @DisplayName("正常系")
        class HappyCases {

            @ParameterizedTest(name = "{index} {3} {0}-{1}-{2} => {4}歳")
            @MethodSource("prepArguments")
            void 年齢が正しいこと(int y, int m, int d, String description, int expectedAge) {
                // Arrange
                Person person = taroBornOn20190503;
                LocalDate date = LocalDate.of(y, m, d);

                // Act
                Optional<Integer> age = person.getAgeAt(date);

                // Assert
                assertThat(age.isPresent(), is(true));
                assertThat(age.get(), is(expectedAge));
            }

            Stream<Arguments> prepArguments() {
                return Stream.of(
                        arguments(2019, 5, 3, "生まれた日", 0),
                        arguments(2020, 5, 2, "生まれた翌年の誕生日前日", 0),
                        arguments(2020, 5, 3, "生まれた翌年の誕生日", 1)
                );
            }

        }

        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        @Nested
        @DisplayName("異常系")
        class UnhappyCases {

            @Test
            void 誕生日不明の場合は年齢を取得できないこと() {
                // Arrange
                Person person = jiroBirthdayUnknown;
                LocalDate anyDate = LocalDate.of(2020, 1, 1);

                // Act
                Optional<Integer> age = person.getAgeAt(anyDate);

                //Assert
                assertThat(age.isPresent(), is(false));
            }

            @Test
            void 生まれる以前の日付に対しては年齢を取得できないこと() {
                // Arrange
                Person person = taroBornOn20190503;
                LocalDate dateBeforeBirthday = LocalDate.of(2019, 5, 2);

                // Act
                Optional<Integer> age = person.getAgeAt(dateBeforeBirthday);

                // Assert
                assertThat(age.isPresent(), is(false));
            }
        }

        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        @Nested
        @DisplayName("例外系")
        class InvalidCases {

            @Test
            void 日付がnullの場合は例外が発生すること_誕生日既知の人の場合() {
                // Arrange
                Person person = taroBornOn20190503;

                // Act, Assert
                assertThrows(IllegalArgumentException.class, () -> {
                    person.getAgeAt(null);
                });
            }

            @Test
            void 日付がnullの場合は例外が発生すること_誕生日不明の人の場合() {
                // Arrange
                Person person = jiroBirthdayUnknown;

                // Act, Assert
                assertThrows(IllegalArgumentException.class, () -> {
                    person.getAgeAt(null);
                });
            }

        }

    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("年齢（数え年）")
    class GetKazoedoshi {

        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        @Nested
        @DisplayName("正常系")
        class HappyCases {

            @ParameterizedTest(name = "{index} {3} {0}-{1}-{2} => {4}歳")
            @MethodSource("prepArguments")
            void 年齢が正しいこと(int y, int m, int d, String description, int expectedAge) {
                // Arrange
                Person person = taroBornOn20190503;
                LocalDate date = LocalDate.of(y, m, d);

                // Act
                Optional<Integer> age = person.getKazoedoshiAt(date);

                // Assert
                assertThat(age.isPresent(), is(true));
                assertThat(age.get(), is(expectedAge));
            }

            Stream<Arguments> prepArguments() {
                return Stream.of(
                        arguments(2019, 5, 3, "生まれた日", 1),
                        arguments(2019, 12, 31, "生まれた年の大晦日", 1),
                        arguments(2020, 1, 1, "生まれた翌年の元日", 2),
                        arguments(2020, 5, 3, "生まれた翌年の誕生日", 2)
                );
            }
        }

        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        @Nested
        @DisplayName("異常系")
        class UnhappyCases {

            @Test
            void 誕生日不明の場合は年齢を取得できないこと() {
                // Arrange
                Person person = jiroBirthdayUnknown;
                LocalDate anyDate = LocalDate.of(2020, 1, 1);

                // Act
                Optional<Integer> age = person.getKazoedoshiAt(anyDate);

                // Assert
                assertThat(age.isPresent(), is(false));
            }

            @Test
            void 生まれる以前の日付に対しては年齢を取得できないこと() {
                // Arrange
                Person person = taroBornOn20190503;
                LocalDate dateBeforeBirthday = LocalDate.of(2019, 5, 2);

                // Act
                Optional<Integer> age = person.getKazoedoshiAt(dateBeforeBirthday);

                // Assert
                assertThat(age.isPresent(), is(false));
            }

        }

        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        @Nested
        @DisplayName("例外系")
        class InvalidCases {

            @Test
            void 日付がnullの場合は例外が発生すること_誕生日が既知の人の場合() {
                // Arrange
                Person person = taroBornOn20190503;

                // Act, Assert
                assertThrows(IllegalArgumentException.class, () -> {
                    person.getKazoedoshiAt(null);
                });
            }

            @Test
            void 日付がnullの場合は例外が発生すること_誕生日が不明の人の場合() {
                // Arrange
                Person person = jiroBirthdayUnknown;

                // Act, Assert
                assertThrows(IllegalArgumentException.class, () -> {
                    person.getKazoedoshiAt(null);
                });
            }

        }

    }

}