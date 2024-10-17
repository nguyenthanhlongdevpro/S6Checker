package org.s3979.tool.sgd6;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class S6Checker {

    public static List<String> listRefs = new ArrayList<>();
    public static String total_betting_amount_text = "";
    public static String total_winning_amount_text = "";

    static List<String> KQXS_MB;
    static List<List<String>> KQXS_MN;
    static List<List<String>> KQXS_MT;

    static int region = 3;
    static boolean isDebug = true;
    static int NUM_OF_SIZE_MN_MT = 18;

    private static final String URL_KQXS = "https://xosothantai.mobi/xsmb-thu-6.html";

    public static void main(String[] args) {
        log(Const.start_program);

        if (!isDebug) region = getParam(args);
        if (region != 0) {
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
        }

        log(Const.stop_program);

        try {
            log(Const.press_any_key);
            System.in.read();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static int getParam(String[] args) {
        if (args.length != 0) {
            String param = args[0];
            return Integer.parseInt(param);
        }
        return 0;
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
        boolean f = loadKQXS(region);
        if (f) checkKQXS(region, lstWinningData);
        else {
            log("[Error] GET KQXS ... !!!");
        }
    }

    private static void checkKQXS(int region, List<S6WinningModel> lstWinningData) {
        boolean flag = true;
        switch (region) {
            case 1:
            case 2:
            case 3:
                for (S6WinningModel model : lstWinningData) {

                    if (model.betKind.equals("Đánh LIVE")) {
                        log("Do NOT support checking LIVE bet : " + model.refId);
                        continue;
                    }

                    double numfowin = 0;

                    String channel = model.channel;
                    String[] arrayChannel = channel.split(",");

                    List<List<String>> list = region == 1 ? KQXS_MN : KQXS_MT;

                    if (model.betType.trim().equals("Đá Xiên")) {
                        if (arrayChannel.length == 2) {
                            int index1 = getIndexByChannelName(arrayChannel[0]);
                            int index2 = getIndexByChannelName(arrayChannel[1]);
                            int count = countOfWin(list.get(index1), list.get(index2), model.betNumber, model.betType);
                            numfowin += count;
                        }

                    } else {
                        for (String ch : arrayChannel) {
                            int index = getIndexByChannelName(ch.trim());
                            if (index != -1) {
                                List<String> results = region == 3 ? KQXS_MB : list.get(index);
                                double count = countOfWin(results, model.betNumber, model.betType, model.refId);
                                numfowin += count;
                            } else {
                                log("[Error] Not found Index of Channel: " + ch);
                                break;
                            }
                        }
                    }

                    if (Double.parseDouble(model.numofwin) != numfowin) {
                        log(model.refId);
                        log(model);
                        flag = false;
                    }
                }
                break;

            default:
                break;
        }
        if (flag) {
            log("==> Pass");
        } else {
            log("==> Fail");
        }
    }

    private static boolean loadKQXS(int region) {
        Document document = JsoupUtil.load(URL_KQXS);
        if (document != null) parseKQXS(document, region);

        switch (region) {
            case 1:
                return KQXS_MN.size() > 0;
            case 2:
                return KQXS_MT.size() > 0;
            case 3:
                return KQXS_MB.size() > 0;
        }

        return false;
    }

    private static int getIndexByChannelName(String channel) {
        int index = -1;
        switch (channel.trim()) {
            case "Hồ Chí Minh":
            case "Bến Tre":
            case "Đồng Nai":
            case "Tây Ninh":
            case "Vĩnh Long":
            case "Tiền Giang":

            case "Đăk Lăk":
            case "Bình Định":
            case "Gia Lai":
            case "Đà Nẵng":

            case "MB":
                index = 0;
                break;

            case "Long An":
            case "Đông Tháp":
            case "Vũng Tàu":
            case "Cần Thơ":
            case "An Giang":
            case "Bình Dương":
            case "Kiên Giang":

            case "Kon Tum":
            case "Phú Yên":
            case "Quảng Nam":
            case "Quảng Trị":
            case "Ninh Thuận":
            case "Quảng Ngãi":
                index = 1;
                break;

            case "Bình Phước":
            case "Cà Mau":
            case "Bạc Liêu":
            case "Sóc Trăng":
            case "Bình Thuận":
            case "Trà Vinh":
            case "Đà Lạt":
            case "Quảng Bình":
            case "Đăk Nông":
                index = 2;
                break;

            case "Hậu Giang":
                index = 3;
                break;
        }

        int today = getDayOfWeek();
        if (channel.trim().equals("Thừa Thiên Huế")) {
            if (today == 2) { // T2
                index = 0;
            }
            if (today == 1) { // CN
                index = 2;
            }
        }

        if (channel.trim().equals("Khánh Hòa")) {
            if (today == 4) { // T4
                index = 1;
            }
            if (today == 1) { // CN
                index = 0;
            }
        }

        return index;
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

                if (data.contains(Const.total_betting)) total_betting_amount_text = data;
                if (data.contains(Const.total_winning)) total_winning_amount_text = data;

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

    public static void parseKQXS(Document document, int flag) {
        switch (flag) {
            case 1:
            case 2:
                int maxMN = 4;
                int maxMT = 3;

                int today = getDayOfWeek();
                today = 7; // test
                if (today == 7) {
                    maxMN = 5;
                }
                if (today == 5 || today == 7 || today == 1) { // 1: CN
                    maxMT = 4;
                }

                int max = flag == 1 ? maxMN : maxMT;
                String className = flag == 1 ? "load_kq_mn_0" : "load_kq_mt_0";

                List<List<String>> results = new ArrayList<>();
                for (int colIndex = 2; colIndex <= max; colIndex++) {
                    String format = "//*[@id='%s']/*[@data-id='kq']//tbody/tr[%s]/td[%s]/*[@data-nc]";
                    List<String> list = new ArrayList<>();
                    for (int i = 2; i <= 10; i++) {
                        String path = String.format(format, className, i, colIndex);
                        Elements elements = document.selectXpath(path);
                        for (Element element : elements) {
                            list.add(element.text());
                        }
                    }
                    results.add(list);
                }

                if (flag == 1) {
                    KQXS_MN = new ArrayList<>();
                    KQXS_MN.addAll(results);

                } else {
                    KQXS_MT = new ArrayList<>();
                    KQXS_MT.addAll(results);
                }

                break;

            case 3:
                KQXS_MB = new ArrayList<>();
                Elements elements = document.selectXpath("//*[@id='load_kq_mb_0']//*[contains(@class,'v-giai number')]/*[@data-nc]");
                for (Element element : elements) {
                    String text = element.text();
                    KQXS_MB.add(text);
                }
                break;
        }
    }

    private static int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    private static int countOfWin(List<String> result1, List<String> result2, String betNumber, String betType) {
        int count = 0;
        String[] arr = betNumber.split(",");
        if (arr.length == 2) {
            String text0 = arr[0].trim();
            String text1 = arr[1].trim();

            int n1 = 0;
            int n2 = 0;

            for (String text : result1) {
                String t = text.substring(Math.max(text.length() - 2, 0));
                if (t.equals(text0)) n1++;
            }

            for (String text : result2) {
                String t = text.substring(Math.max(text.length() - 2, 0));
                if (t.equals(text0)) n1++;
            }

            for (String text : result1) {
                String t = text.substring(Math.max(text.length() - 2, 0));
                if (t.equals(text1)) n2++;
            }

            for (String text : result2) {
                String t = text.substring(Math.max(text.length() - 2, 0));
                if (t.equals(text1)) n2++;
            }

            if (n1 == n2) count = n1;
            else {
                if (n1 > n2) count = n2;
                if (n1 < n2) count = n1;
            }
        }
        return count;
    }

    private static double countOfWin(List<String> results, String betNumber, String betType, String refId) {
        double count = 0;

        List<String> res = new ArrayList<>();

        int digit = 2;
        if (betType.contains("XC")) digit = 3;
        if (betType.equals("Bao Lô")) digit = betNumber.length();

        for (String text : results) {
            if (text.length() >= digit) {
                String t = text.substring(Math.max(text.length() - digit, 0));
                res.add(t);
            }
        }

        switch (betType) {
            case "Bao Lô": // // MN MT MB
                for (String text : res) {
                    if (text.equals(betNumber)) count++;
                }
                break;

            case "Đá": // MN MT MB
            case "Xiên 2":
                String[] arr = betNumber.split(",");
                if (arr.length == 2) {
                    String text0 = arr[0].trim();
                    String text1 = arr[1].trim();

                    int n1 = 0;
                    int n2 = 0;

                    for (String text : res) {
                        if (text.equals(text0)) n1++;
                    }

                    for (String text : res) {
                        if (text.equals(text1)) n2++;
                    }

                    if (n1 == n2) count = n1;
                    else {
                        if (results.size() == NUM_OF_SIZE_MN_MT) { // MN , MT
                            if (n1 > n2) count = n2 + 0.5;
                            if (n1 < n2) count = n1 + 0.5;
                        } else { // MB
                            if (n1 > n2) count = n2;
                            if (n1 < n2) count = n1;
                        }
                    }
                }
                break;

            case "Bảy Lô Đầu": // MN MT
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    int c = 0;
                    for (int i = 0; i < 7; i++) {
                        if (res.get(i).equals(betNumber)) c++;
                    }
                    count = c;
                } else {
                    log("Do Not handle yet !!!");
                }
                break;

            case "Bảy Lô Đuôi": // MN MT\
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    int c1 = 0;
                    int size = res.size();
                    for (int i = size - 1; i > size - 8; i--) {
                        String num = res.get(i);
                        if (num.equals(betNumber)) c1++;
                    }
                    count = c1;
                } else {
                    log("Do Not handle yet !!!");
                }
                break;

            case "Bảy Lô Giữa": // MN MT
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    int c2 = 0;
                    int size2 = res.size();
                    int start = 6;
                    for (int i = start; i < start + 7; i++) {
                        String number = res.get(i);
                        if (number.equals(betNumber)) c2++;
                    }
                    count = c2;
                } else {
                    log("Do Not handle yet !!!");
                }
                break;

            case "Đầu": // MN MT MB
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    if (res.get(0).equals(betNumber)) count = 1;
                } else {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 4, 0);
                }
                break;

            case "Đuôi": // MN MT
            case "Đuôi (Đề)": // MB
            case "Đề":
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    if (res.get(res.size() - 1).equals(betNumber)) count = 1;
                } else {
                    if (res.get(0).equals(betNumber)) count = 1;
                }
                break;

            case "Đầu Đuôi": // MN MT MB
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    if (res.get(0).equals(betNumber)) count++;
                    if (res.get(res.size() - 1).equals(betNumber)) count++;
                } else {
                    // Count Dau
                    count = countOfWin_MB_By_Price(results, res, betNumber, 4, 0);
                    // Count Duoi
                    if (res.get(0).equals(betNumber)) count = 1;
                }
                break;

            case "XC Đầu": // MN MT MB
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    if (res.get(0).equals(betNumber)) count = 1;
                } else {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 3, 4);
                }

                break;

            case "XC Đuôi": // MN MT MB
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    if (res.get(res.size() - 1).equals(betNumber)) count = 1;
                } else {
                    if (res.get(0).equals(betNumber)) count = 1;
                }

                break;

            case "XC":
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    if (res.get(0).equals(betNumber)) count++;
                    if (res.get(res.size() - 1).equals(betNumber)) count++;
                } else {
                    // Count XC Dau
                    count = countOfWin_MB_By_Price(results, res, betNumber, 3, 4);
                    // Count XC Duoi
                    if (res.get(0).equals(betNumber)) count++;
                }
                break;

            case "G1":
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 1, 1);
                } else {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 25, 1);
                }
                break;

            case "G2":
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 1, 2);
                } else {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 2, 23);
                }
                break;

            case "G3":
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 2, 3);
                } else {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 6, 17);
                }
                break;

            case "G4":
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 7, 5);
                } else {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 4, 13);
                }
                break;

            case "G5":
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 1, 12);
                } else {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 6, 7);
                }
                break;

            case "G6":
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 3, 13);
                } else {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 3, 4);
                }
                break;

            case "G7":
                if (results.size() == NUM_OF_SIZE_MN_MT) {
                    count = countOfWin_MB_By_Price(results, res, betNumber, 1, 16);
                } else {
                    log("Please check: " + refId);
                }
                break;

            default:
                log("Please check: " + refId);
                break;
        }
        return count;
    }

    private static int countOfWin_MB_By_Price(List<String> results, List<String> res, String betNumber, int sizeofprice, int off) {
        int n = sizeofprice;
        int ofset = off;
        int sz = results.size();
        int c = 0;
        for (int i = sz - 1 - ofset; i > sz - 1 - ofset - n; i--) {
            String number = res.get(i);
            if (number.equals(betNumber)) c++;
        }
        return c;
    }


}
