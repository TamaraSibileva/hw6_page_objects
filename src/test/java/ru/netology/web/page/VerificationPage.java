package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeField = $("[data-test-id='code'] input");
    private final SelenideElement verifyButton = $("[data-test-id='action-verify']");

    public VerificationPage() {
        codeField.should(Condition.visible);
    }

    public DashBoardPage validVerify(DataHelper.Verificationcode verificationcode) {
        codeField.setValue(verificationcode.getCode());
        verifyButton.click();
        return new DashBoardPage();
    }
}
