package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class ApplicationFormTests {
    LocalDate futureDate = LocalDate.now().plusDays(3);

    @Test
    void shouldRegisterIfAllConditionsCorrect() {
        open("http://localhost:9999");
        $("[placeholder='Дата встречи']").setValue(String.valueOf(futureDate));
        $("[placeholder='Город']").setValue("Красноярск");
        $("[name='name']").setValue("Иван Петров-Водкин");
        $("[name='phone']").setValue("+79119418502");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void correctTestCitySelectsByClick() { //Хочу проверить, можно ли выбрать город из списка
        open("http://localhost:9999");
        $("[placeholder='Дата встречи']").setValue(String.valueOf(futureDate));
        $(Selectors.byAttribute("type", "text")).setValue("Мо");
        $(Selectors.byText("Москва")).click();
        $("[name='name']").setValue("Иван Петров-Водкин");
        $("[name='phone']").setValue("+79119418502");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
    }

    //Негативные тесты
    @Test
    void shouldFailWithLatinInName() {
        open("http://localhost:9999");
        $("[placeholder='Дата встречи']").setValue(String.valueOf(futureDate));
        $("[placeholder='Город']").setValue("Красноярск");
        $("[name='name']").setValue("Ivan Petrov");
        $("[name='phone']").setValue("+79119418502");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(Selectors.withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).should(Condition.appear, Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWithLatinInCity() {
        open("http://localhost:9999");
        $("[placeholder='Дата встречи']").setValue(String.valueOf(futureDate));
        $("[placeholder='Город']").setValue("Moscow");
        $("[name='name']").setValue("Иван Петров-Водкин");
        $("[name='phone']").setValue("+79119418502");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(Selectors.withText("Доставка в выбранный город недоступна")).should(Condition.appear, Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWithLongPhoneNumber() {
        open("http://localhost:9999");
        $("[placeholder='Дата встречи']").setValue(String.valueOf(futureDate));
        $("[placeholder='Город']").setValue("Красноярск");
        $("[name='name']").setValue("Иван Петров-Водкин");
        $("[name='phone']").setValue("+791194185021");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(Selectors.withText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")).should(Condition.appear, Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWithShortPhoneNumber() {
        open("http://localhost:9999");
        $("[placeholder='Дата встречи']").setValue(String.valueOf(futureDate));
        $("[placeholder='Город']").setValue("Красноярск");
        $("[name='name']").setValue("Иван Петров-Водкин");
        $("[name='phone']").setValue("+7911");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(Selectors.withText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")).should(Condition.appear, Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWithPhoneNumberInCorrectFormat() {
        open("http://localhost:9999");
        $("[placeholder='Дата встречи']").setValue(String.valueOf(futureDate));
        $("[placeholder='Город']").setValue("Красноярск");
        $("[name='name']").setValue("Иван Петров-Водкин");
        $("[name='phone']").setValue("89119418502");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(Selectors.withText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")).should(Condition.appear, Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWithUncheckedBox() {
        open("http://localhost:9999");
        $("[placeholder='Дата встречи']").setValue(String.valueOf(futureDate));
        $("[placeholder='Город']").setValue("Красноярск");
        $("[name='name']").setValue("Иван Петров-Водкин");
        $("[name='phone']").setValue("+79119418502");
        $("[data-test-id='agreement']").shouldNotHave(cssClass("input_invalid"));
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='agreement']").shouldHave(cssClass("input_invalid"));
        $(Selectors.withText("Я соглашаюсь с условиями обработки и использования моих персональных данных")).should(Condition.appear, Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWithEmptyName() {
        open("http://localhost:9999");
        $("[placeholder='Дата встречи']").setValue(String.valueOf(futureDate));
        $("[placeholder='Город']").setValue("Красноярск");
        $("[name='name']").setValue(" ");
        $("[name='phone']").setValue("+79119418502");
        $$("button").find(exactText("Забронировать")).click();
        $(Selectors.withText("Поле обязательно для заполнения")).should(Condition.appear, Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWithEmptyCity() {
        open("http://localhost:9999");
        $("[placeholder='Дата встречи']").setValue(String.valueOf(futureDate));
        $("[placeholder='Город']").setValue(" ");
        $("[name='name']").setValue("Иван Петров-Водкин");
        $("[name='phone']").setValue("+79119418502");
        $$("button").find(exactText("Забронировать")).click();
        $(Selectors.withText("Поле обязательно для заполнения")).should(Condition.appear, Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWithEmptyPhone() {
        open("http://localhost:9999");
        $("[placeholder='Дата встречи']").setValue(String.valueOf(futureDate));
        $("[placeholder='Город']").setValue("Красноярск");
        $("[name='name']").setValue("Иван Петров-Водкин");
        $("[name='phone']").setValue("  ");
        $$("button").find(exactText("Забронировать")).click();
        $(Selectors.withText("Поле обязательно для заполнения")).should(Condition.appear, Duration.ofSeconds(15));
    }

    @Test
    void shouldFailWithEmptyFields() {
        open("http://localhost:9999");
        $("[placeholder='Дата встречи']").setValue(String.valueOf(futureDate));
        $("[placeholder='Город']").setValue(" ");
        $("[name='name']").setValue(" ");
        $("[name='phone']").setValue(" ");
        $$("button").find(exactText("Забронировать")).click();
        $(Selectors.withText("Поле обязательно для заполнения")).should(Condition.appear, Duration.ofSeconds(15));
    }
}