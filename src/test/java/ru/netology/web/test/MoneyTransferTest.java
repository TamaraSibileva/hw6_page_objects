package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashBoardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.netology.web.data.DataHelper.*;

public class MoneyTransferTest {

    DashBoardPage dashBoardPage;
    CardInfo firstCardInfo;
    CardInfo secondCardInfo;
    int firstCardBalance;
    int secondCardBalance;

    @BeforeEach
    void setUp() {
        var loginPage = Selenide.open("http://localhost:9999", LoginPage.class);
        var info = getAuthInfo();
        var verificationCode = getVerificationCode(info);
        var verificationPage = loginPage.validLogin(info);
        dashBoardPage = verificationPage.validVerify(verificationCode);
        firstCardInfo = getFirstCardInfo();
        firstCardBalance = dashBoardPage.getCardBalance(firstCardInfo);
        secondCardInfo = getSecondCardInfo();
        secondCardBalance = dashBoardPage.getCardBalance(secondCardInfo);
    }

    @Test
    void shouldTransferMoneyFromFirstCardToSecondCard() {
        var amount = generateValidAmount(firstCardBalance);
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;
        var transferPage = dashBoardPage.selectCardToTransfer(secondCardInfo);
        dashBoardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        dashBoardPage.reloadDashBoardPage();
        assertAll(() -> dashBoardPage.checkCardBalance(firstCardInfo, expectedFirstCardBalance),
                () -> dashBoardPage.checkCardBalance(secondCardInfo, expectedSecondCardBalance));
    }

    @Test
    void shouldTransferMoneyFromSecondCardToFirstCard() {
        var amount = generateValidAmount(secondCardBalance);
        var expectedFirstCardBalance = firstCardBalance + amount;
        var expectedSecondCardBalance = secondCardBalance - amount;
        var transferPage = dashBoardPage.selectCardToTransfer(firstCardInfo);
        dashBoardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCardInfo);
        dashBoardPage.reloadDashBoardPage();
        assertAll(() -> dashBoardPage.checkCardBalance(firstCardInfo, expectedFirstCardBalance),
                () -> dashBoardPage.checkCardBalance(secondCardInfo, expectedSecondCardBalance));
    }

    @Test
    void shouldTransferWhenAmountMoreThanBalance() {
        var amount = generateInvalidBalance(firstCardBalance);
        var transferPage = dashBoardPage.selectCardToTransfer(secondCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), firstCardInfo);
        assertAll(() -> transferPage.findErrorMessage("Выполнена попытка перевода суммы, превышающей остаток на карте списания"),
                () -> dashBoardPage.reloadDashBoardPage(),
                () -> dashBoardPage.checkCardBalance(firstCardInfo, firstCardBalance),
                () -> dashBoardPage.checkCardBalance(secondCardInfo, secondCardBalance));
    }
}
