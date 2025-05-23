package org.s3979.tool.sgd6;

@SuppressWarnings("all")
public class CopyBet {

    public static void main(String[] args) {
        TelegramSender.sendMessage("START !!!!");
        Ag3in1Page ag3in1Page = new Ag3in1Page();
        try {
            ag3in1Page.init();
            ag3in1Page.login();
            ag3in1Page.passSecurityCode();
            ag3in1Page.closeDialogIfDisplay();
            ag3in1Page.clickOnReportMenu();
            ag3in1Page.clickOnOutStandingButton();

            while (true) {
                ag3in1Page.refresh();
                boolean isChanged = ag3in1Page.isOutStdChanged();
                if (isChanged) {
                    ag3in1Page.doScan();
                }
                Thread.sleep(10000);
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            // TelegramSender.sendMessage(ex.getMessage());
            ExceptionLogger.log(ex);
        } finally {
            ag3in1Page.quit();
            TelegramSender.sendMessage("STOP !!!!");
        }
    }
}
