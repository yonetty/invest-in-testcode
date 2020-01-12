package com.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonTest {

    private Person person;
    private Person personBirthDateNull;

    @BeforeAll
    void setup() {
        person = new Person("太郎", "山田", LocalDate.of(2019, 5, 3));
        personBirthDateNull = new Person("太郎", "山田");
    }

    @Test
    void 生まれた日は0歳であること() {
        Optional<Integer> age = person.getAgeAt(LocalDate.of(2019, 5, 3));
        assertThat(age.isPresent(), is(true));
        assertThat(age.get(), is(0));
    }

    @Test
    void 生まれた翌年の誕生日前日は0歳であること() {
        Optional<Integer> age = person.getAgeAt(LocalDate.of(2020, 5, 2));
        assertThat(age.isPresent(), is(true));
        assertThat(age.get(), is(0));
    }

    @Test
    void 生まれた翌年の誕生日は1歳であること() {
        Optional<Integer> age = person.getAgeAt(LocalDate.of(2020, 5, 3));
        assertThat(age.isPresent(), is(true));
        assertThat(age.get(), is(1));
    }

    @Test
    void 日付がnullの場合は例外が発生すること() {
        assertThrows(IllegalArgumentException.class, () -> {
            person.getAgeAt(null);
        });
    }

    @Test
    void 日付がnullの場合は例外が発生すること2() {
        assertThrows(IllegalArgumentException.class, () -> {
            personBirthDateNull.getAgeAt(null);
        });
    }

    @Test
    void 誕生日不明の場合は年齢を取得できないこと() {
        Optional<Integer> age = personBirthDateNull.getAgeAt(LocalDate.of(2020, 1, 1));
        assertThat(age.isPresent(), is(false));
    }

    @Test
    void 生まれる以前の日付に対しては年齢を取得できないこと() {
        Optional<Integer> age = person.getAgeAt(LocalDate.of(2019, 5, 2));
        assertThat(age.isPresent(), is(false));
    }

    @Test
    void 数え年_生まれた日は1歳であること() {
        Optional<Integer> age = person.getKazoedoshiAt(LocalDate.of(2019, 5, 3));
        assertThat(age.isPresent(), is(true));
        assertThat(age.get(), is(1));
    }

    @Test
    void 数え年_生まれた年の大晦日までは1歳であること() {
        Optional<Integer> age = person.getKazoedoshiAt(LocalDate.of(2019, 12, 31));
        assertThat(age.isPresent(), is(true));
        assertThat(age.get(), is(1));
    }

    @Test
    void 数え年_生まれた翌年の元日は2歳であること() {
        Optional<Integer> age = person.getKazoedoshiAt(LocalDate.of(2020, 1, 1));
        assertThat(age.isPresent(), is(true));
        assertThat(age.get(), is(2));
    }

    @Test
    void 数え年_誕生日には歳を取らないこと() {
        Optional<Integer> age = person.getKazoedoshiAt(LocalDate.of(2020, 5, 3));
        assertThat(age.isPresent(), is(true));
        assertThat(age.get(), is(2));
    }

    @Test
    void 数え年_誕生日不明の場合は年齢を取得できないこと() {
        Optional<Integer> age = personBirthDateNull.getKazoedoshiAt(LocalDate.of(2020, 1, 1));
        assertThat(age.isPresent(), is(false));
    }

    @Test
    void 数え年_日付がnullの場合は例外が発生すること() {
        assertThrows(IllegalArgumentException.class, () -> {
            person.getKazoedoshiAt(null);
        });
    }

    @Test
    void 数え年_日付がnullの場合は例外が発生すること2() {
        assertThrows(IllegalArgumentException.class, () -> {
            personBirthDateNull.getKazoedoshiAt(null);
        });
    }

    @Test
    void 数え年_生まれる以前の日付に対しては年齢を取得できないこと() {
        Optional<Integer> age = person.getKazoedoshiAt(LocalDate.of(2019, 5, 2));
        assertThat(age.isPresent(), is(false));
    }

}