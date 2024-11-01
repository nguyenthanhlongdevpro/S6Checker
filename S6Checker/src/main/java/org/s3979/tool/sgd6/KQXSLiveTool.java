package org.s3979.tool.sgd6;

import com.google.gson.Gson;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class KQXSLiveTool {

    static final int NUM_OF_SIZE_MN_MT = 18;
    static final int NUM_OF_SIZE_MB = 27;
    private static final String URL_KQXS = "https://xosothantai.mobi/";
    static StringBuilder message = new StringBuilder();

    static List<ResultLogModel> KQXS_MB_LIVE = new ArrayList<>();
    static List<List<ResultLogModel>> KQXS_MN_LIVE = new ArrayList<>();
    static List<List<ResultLogModel>> KQXS_MT_LIVE = new ArrayList<>();

    public static void main(String[] args) {
        String text = String.format("Start: %s\n", getCurentTime());
        log(text);

        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = Calendar.getInstance().getTime();
            String today = format.format(date);

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            String startMN = String.format("%s 16:10:00", today);
            String endMN = String.format("%s 16:40:00", today);
            Date sMN = dateFormat.parse(startMN);
            Date eMN = dateFormat.parse(endMN);

            String startMT = String.format("%s 17:10:00", today);
            String endMT = String.format("%s 17:40:00", today);
            Date sMT = dateFormat.parse(startMT);
            Date eMT = dateFormat.parse(endMT);

            String startMB = String.format("%s 18:10:00", today);
            String endMB = String.format("%s 18:40:00", today);
            Date sMB = dateFormat.parse(startMB);
            Date eMB = dateFormat.parse(endMB);

            do {
                logTime();

                int flag = -1;

                long currentTime = Calendar.getInstance().getTimeInMillis();

                if (sMN.getTime() < currentTime && eMN.getTime() > currentTime) {
                    flag = 1;
                }

                if (sMN.getTime() < currentTime && eMT.getTime() > currentTime) {
                    flag = 2;
                }

                if (sMB.getTime() < currentTime && eMB.getTime() > currentTime) {
                    flag = 3;
                }

                if (flag != -1) {

                    loadKQXS_LIVE(flag);

                    if (flag == 1) {
                        boolean isBreak = true;
                        int size = KQXS_MN_LIVE.size();
                        for (int i = 0; i < size; i++) {
                            if (KQXS_MN_LIVE.get(i).size() != NUM_OF_SIZE_MN_MT) isBreak = false;
                        }
                        if (isBreak) {
                            log("KQXS Mien Nam: ");
                            for (int i = 0; i < size; i++) {
                                log(KQXS_MN_LIVE.get(i));
                            }
                            break;
                        }

                    } else if (flag == 2) {
                        boolean isBreak = true;
                        int size = KQXS_MT_LIVE.size();
                        for (int i = 0; i < size; i++) {
                            if (KQXS_MT_LIVE.get(i).size() != NUM_OF_SIZE_MN_MT) isBreak = false;
                        }
                        if (isBreak) {
                            log("KQXS Mien Trung: ");
                            for (int i = 0; i < size; i++) {
                                log(KQXS_MT_LIVE.get(i));
                            }
                            break;
                        }

                    } else {
                        if (KQXS_MB_LIVE.size() == NUM_OF_SIZE_MB) {
                            log("KQXS Mien Bac: ");
                            log(KQXS_MB_LIVE);
                            break;
                        }
                    }

                }

                // Sleep 5s
                Thread.sleep(5000);

            } while (true);

            // Send result to telegram
            String m = message.toString();
            if (!m.isEmpty()) {
                TelegramSender.sendMessage(m);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getCurentTime() {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        return format.format(date);
    }

    private static int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    private static void log(String text) {
        if (!text.isEmpty()) {
            System.out.println(text);
            message.append("\n").append(text);
        }
    }

    private static void log(Object object) {
        String text = new Gson().toJson(object);
        log(text);
    }

    private static void loadKQXS_LIVE(int region) {
        Document document = JsoupUtil.load(URL_KQXS);
        if (document != null) parseKQXS_LIVE(document, region);
    }

    public static void parseKQXS_LIVE(Document document, int flag) {
        switch (flag) {
            case 1:
            case 2:
                int maxMN = 4;
                int maxMT = 3;

                int today = getDayOfWeek();
                if (today == 7) {
                    maxMN = 5;
                }
                if (today == 5 || today == 7 || today == 1) { // 1: CN
                    maxMT = 4;
                }

                int max = flag == 1 ? maxMN : maxMT;
                String className = flag == 1 ? "load_kq_mn_0" : "load_kq_mt_0";

                int maxLen = 0;
                for (int colIndex = 2; colIndex <= max; colIndex++) {
                    String format = "//*[@id='%s']/*[@data-id='kq']//tbody/tr[%s]/td[%s]/*[@data-nc]";
                    List<ResultLogModel> list = flag == 1 ? KQXS_MN_LIVE.get(colIndex) : KQXS_MT_LIVE.get(colIndex);
                    for (int i = 2; i <= 10; i++) {
                        maxLen = getMaxLex(flag, i);
                        String path = String.format(format, className, i, colIndex);
                        Elements elements = document.selectXpath(path);
                        int sz = elements.size();
                        for (int row = 0; row < sz; row++) {
                            String text = elements.get(row).text();
                            String textSaved = list.get(row).number;
                            if (textSaved.isEmpty() && text.length() == maxLen) {

                                ResultLogModel resultLogModel = new ResultLogModel();
                                resultLogModel.number = text;
                                resultLogModel.time = getCurentTime();

                                list.add(resultLogModel);
                                log(String.format("\n ==> %s\n", text));
                            }
                        }
                    }
                }
                break;

            case 3:
                Elements elements = document.selectXpath("//*[@id='load_kq_mb_0']//*[contains(@class,'v-giai number')]/*[@data-nc]");
                int sz = elements.size();
                for (int row = 0; row < sz; row++) {
                    maxLen = getMaxLex(flag, row);
                    String text = elements.get(row).text();
                    String textSaved = KQXS_MB_LIVE.get(row).number;
                    if (textSaved.isEmpty() && text.length() == maxLen) {

                        ResultLogModel resultLogModel = new ResultLogModel();
                        resultLogModel.number = text;
                        resultLogModel.time = getCurentTime();

                        KQXS_MB_LIVE.add(resultLogModel);
                        log(String.format("\n ==> %s\n", text));
                    }

                }
                break;
        }
    }

    private static int getMaxLex(int region, int index) {
        int max = -1;
        switch (region) {
            case 1:
            case 2:

                switch (index) {
                    case 2:
                        max = 2;
                        break;

                    case 3:
                        max = 3;
                        break;

                    case 4:
                        max = 4;
                        break;

                    case 5:
                        max = 4;
                        break;

                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        max = 5;
                        break;

                    case 10:
                        max = 6;
                        break;
                }
                break;

            case 3:

                switch (index) {
                    case 0:
                        max = 6;
                        break;

                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        max = 5;
                        break;

                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                        max = 4;
                        break;

                    case 20:
                    case 21:
                    case 22:
                        max = 3;
                        break;

                    case 23:
                    case 24:
                    case 25:
                    case 26:
                        max = 2;
                        break;
                }
                break;
        }

        return max;
    }

    private static void logTime() {
        String text = String.format(" -> %s", getCurentTime());
        log(text);
    }
}
