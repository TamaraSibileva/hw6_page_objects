package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashBoardPage {
    private final SelenideElement header = $("[data-test-id='dashboard']");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStartText = "баланс: ";
    private final String balanceFinishText = " р.";
    private final SelenideElement reloadButton = $("[data-test-id='action-reload']");

    public DashBoardPage() {
        header.should(Condition.visible);
    }

    private SelenideElement getCard(DataHelper.CardInfo cardInfo) {
        return cards.findBy(Condition.attribute("data-test-id", cardInfo.getTestId()));
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        var text = getCard(cardInfo).getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStartText);
        var finish = text.indexOf(balanceFinishText);
        var value = text.substring(start + balanceStartText.length(), finish);
        return Integer.parseInt(value);
    }

    public  TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
        getCard(cardInfo).$("button").click();
        return new TransferPage();
    }

    public void reloadDashBoardPage() {
        reloadButton.click();
        header.should(Condition.visible);
    }

    public void checkCardBalance(DataHelper.CardInfo cardinfo, int expectedBalance) {
        getCard(cardinfo).should(Condition.visible).should(Condition.text(balanceStartText + expectedBalance + balanceFinishText));
    }
}
