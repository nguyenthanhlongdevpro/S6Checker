package org.s3979.tool.sgd6;

import com.google.gson.Gson;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class S6Checker {

    public static List<String> listRefs = new ArrayList<>();
    public static String total_betting_amount_text = "";
    public static String total_winning_amount_text = "";

    public static void main(String[] args) {
        log(Const.start_program);

        String workingDir = System.getProperty("user.dir");

        String bettingDir = String.format("%s\\data\\ve_cuoc", workingDir);
        List<S6BettingModel> lstBetData = loadAllBettingData(bettingDir);

        String winningDir = String.format("%s\\data\\ve_thang", workingDir);
        List<S6WinningModel> lstWinningData = loadAllWinningData(winningDir);

        // 1. So sanh thong tin ve trung vs ve cuoc ban dau
        compare(lstBetData, lstWinningData);

        // 2. So sanh so tien trung
        checkWinningAmount(lstWinningData);

        // 3. Kiem tra tung ve trung la DUNG
        checkWinTicketCorrect(lstWinningData);

        log(Const.stop_program);

        try {
            log(Const.press_any_key);
            System.in.read();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void compare(List<S6BettingModel> lstBetData, List<S6WinningModel> lstWinningData) {
        log(Const.title_step_1);
        // get list refId
        listRefs.clear();
        for (S6BettingModel bettingModel : lstBetData) {
            listRefs.add(bettingModel.refId);
        }

        int s1 = lstBetData.size();
        int s2 = lstWinningData.size();
        if (s1 != 0 && s2 != 0) {
            for (int i = 0; i < s2; i++) {
                S6WinningModel model = lstWinningData.get(i);
                String refId = model.refId;
                if (listRefs.contains(refId)) {
                    int index = listRefs.indexOf(refId);
                    S6BettingModel bettingModel = lstBetData.get(index);
                    boolean flag = checkTicket(bettingModel, model);

                    if (flag) {
                        log("==> Pass");
                        return;
                    }
                } else {
                    log(Const.not_found + refId);
                }
            }

        } else {
            if (s1 == 0) log(Const.ticket_not_found);
            else log(Const.winning_ticket_not_found);
        }

        log("==> Fail");
    }

    private static void checkWinningAmount(List<S6WinningModel> lstWinningData) {
        log(Const.title_step_2);
        if (total_betting_amount_text.isEmpty() || total_winning_amount_text.isEmpty()) {
            log(Const.can_not_get_total_win);
            return;
        }

        int start = total_betting_amount_text.indexOf(":");
        int end = total_betting_amount_text.indexOf("(");
        String text = total_betting_amount_text.substring(start + 1, end).trim().replaceAll(",", "");
        double totalBet = Double.parseDouble(text);

        start = total_winning_amount_text.indexOf(":");
        end = total_winning_amount_text.indexOf("(");
        text = total_winning_amount_text.substring(start + 1, end).trim().replaceAll(",", "");
        double totalWin = Double.parseDouble(text);

        double totalWinningAmount = 0;
        double totalBettingAmount = 0;

        for (S6WinningModel model : lstWinningData) {
            String s = model.betAmount.replaceAll(",", "");
            double betAmount = Double.parseDouble(s);

            s = model.winningPrice.replaceAll(",", "");
            double winningPrice = Double.parseDouble(s);
            double numofwin = Double.parseDouble(model.numofwin);

            totalWinningAmount += (betAmount * numofwin * winningPrice);
            totalBettingAmount += betAmount;
        }

        if (Math.abs(totalWinningAmount - totalWin) < 1 && Math.abs(totalBettingAmount - totalBet) < 1) {
            log("==> Pass");

        } else {

            log(totalBet + " vs " + totalBettingAmount + " ==> " + (totalBettingAmount - totalBet));
            log(totalWin + " vs " + totalWinningAmount + " ==> " + (totalWinningAmount - totalWin));

            log("==> Fail");
        }
    }

    public static void checkWinTicketCorrect(List<S6WinningModel> lstWinningData) {
        log(Const.title_step_3);

        List<RssItemModel> items = RssReader.read(Const.RSS_KQXS_MT_URL);
        for (RssItemModel item : items) {
            log(item.title + item.description + "\n");
        }
    }

    private static boolean checkTicket(S6BettingModel bettingModel, S6WinningModel winningModel) {
        boolean flag = true;
        Map<String, String> hashChannel = Const.hashChannel;

        if (bettingModel.refId.equals(winningModel.refId)) {

            if (!bettingModel.member.equals(winningModel.member)) {
                flag = false;
            }

            String channel = bettingModel.channel;
            if (hashChannel.containsKey(channel)) {
                channel = hashChannel.get(channel);
            }
            if (!channel.equals(winningModel.channel)) {
                flag = false;
            }

            if (!bettingModel.betNumber.equals(winningModel.betNumber)) {
                flag = false;
            }

            if (!bettingModel.betAmount.equals(winningModel.betAmount)) {
                flag = false;
            }

            if (!bettingModel.betType.equals(winningModel.betType)) {
                flag = false;
            }

            if (!bettingModel.betKind.equals(winningModel.betKind)) {
                flag = false;
            }

        } else {
            flag = false;
        }

        if (!flag) {
            log(Const.ticket_not_correct + winningModel.refId);
        }

        return flag;
    }

    private static List<S6BettingModel> loadAllBettingData(String directory) {
        List<S6BettingModel> listAll = new ArrayList<>();

        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String fileName = listOfFiles[i].getName();
                    if (fileName.contains(".csv")) {
                        String path = String.format("%s/%s", directory, fileName);
                        List<S6BettingModel> models = loadBettingData(path);
                        if (models != null) {
                            listAll.addAll(models);
                        }
                    }
                }
            }
        }

        return listAll;
    }

    private static List<S6WinningModel> loadAllWinningData(String directory) {
        List<S6WinningModel> listAll = new ArrayList<>();

        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String fileName = listOfFiles[i].getName();
                    if (fileName.contains(".csv")) {
                        String path = String.format("%s/%s", directory, fileName);
                        List<S6WinningModel> models = loadWinningData(path);
                        if (models != null) {
                            listAll.addAll(models);
                        }
                    }
                }
            }
        }

        return listAll;
    }

    private static List<S6BettingModel> loadBettingData(String fileLocation) {
        try (CSVReader reader = new CSVReader(new FileReader(fileLocation))) {
            List<S6BettingModel> lstBetData = new ArrayList<>();

            List<String[]> rows = reader.readAll();
            for (String[] row : rows) {
                String data = row[0];
                if (checkIgnoreBettingRow(data)) {
                    continue;
                } else {
                    S6BettingModel model = parseBettingModel(row);
                    if (model != null) {
                        lstBetData.add(model);
                    }
                }
            }

            return lstBetData;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static List<S6WinningModel> loadWinningData(String fileLocation) {
        try (CSVReader reader = new CSVReader(new FileReader(fileLocation))) {
            List<S6WinningModel> lstWinningModel = new ArrayList<>();

            List<String[]> rows = reader.readAll();
            for (String[] row : rows) {
                String data = row[0];
                if (checkIgnoreWinningRow(data)) {
                    continue;
                } else {
                    S6WinningModel model = parseWinningModel(row);
                    if (model != null) {
                        lstWinningModel.add(model);
                    }
                }
            }

            return lstWinningModel;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static S6BettingModel parseBettingModel(String[] data) {
        if (data.length != 0) {
            try {
                S6BettingModel model = new S6BettingModel();

                model.refId = data[0];
                model.member = data[1];
                model.betNumber = data[2];
                model.betAmount = data[3];
                model.betAmountBeforeComm = data[4];
                model.betAmountAfterComm = data[5];
                model.betComm = data[6];
                model.betType = data[8];
                model.betKind = data[9];
                model.type = data[10];
                model.channel = data[11];
                model.ip = data[12];
                model.betMessage = data[13];
                model.betDate = data[14];
                model.betCreate = data[15];

                return model;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private static S6WinningModel parseWinningModel(String[] data) {
        if (data.length != 0) {
            try {
                S6WinningModel model = new S6WinningModel();

                model.refId = data[0];
                model.channel = data[1];
                model.winingChannel = data[2];
                model.betNumber = data[3];
                model.betAmount = data[4];
                model.winningPrice = data[5];
                model.numofwin = data[6];
                model.winningAmount = data[7];
                model.betType = data[8];
                model.betKind = data[9];
                model.member = data[10];

                return model;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private static boolean checkIgnoreBettingRow(String data) {
        String[] items = Const.ignore_betting_text;

        for (String item : items) {
            if (data.isEmpty() || data.toLowerCase().contains(item.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    private static boolean checkIgnoreWinningRow(String data) {
        String[] items = Const.ignore_winning_text;

        for (String item : items) {
            if (data.isEmpty() || data.toLowerCase().contains(item.toLowerCase())) {

                if (data.contains(Const.total_betting))
                    total_betting_amount_text = data;
                if (data.contains(Const.total_winning))
                    total_winning_amount_text = data;

                return true;
            }
        }

        return false;
    }

    private static void log(String text) {
        if (!text.isEmpty()) {
            System.out.println(text);
        }
    }

    private static void log(Object object) {
        String text = new Gson().toJson(object);
        log(text);
    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
