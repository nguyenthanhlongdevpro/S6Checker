package org.s3979.tool.sgd6;

import com.google.gson.Gson;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.*;

@SuppressWarnings("all")
public class S6Checker {

    public static List<String> listRefs = new ArrayList<>();

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

        // 3. Kiem tra tung ve trung la DUNG

        log(Const.stop_program);

        try {
            log(Const.press_any_key);
            System.in.read();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void compare(List<S6BettingModel> lstBetData, List<S6WinningModel> lstWinningData) {

        // get list refId
        listRefs.clear();
        for (S6BettingModel bettingModel : lstBetData){
            listRefs.add(bettingModel.refId);
        }

        int s1 = lstBetData.size();
        int s2 = lstWinningData.size();
        if (s1 != 0 && s2 != 0) {
            log(Const.total_num_of_ticket + s1);
            log(Const.total_num_of_winning_ticket + s2);

            for (int i = 0; i < s2; i++) {
                S6WinningModel model = lstWinningData.get(i);
                String refId = model.refId;
                if (listRefs.contains(refId)) {
                    int index = listRefs.indexOf(refId);
                    S6BettingModel bettingModel = lstBetData.get(index);
                    checkTicket(bettingModel, model);
                } else {
                    log(Const.not_found + refId);
                }
            }

        } else {
            if (s1 == 0) log(Const.ticket_not_found);
            else log(Const.winning_ticket_not_found);
        }
    }

    private static void checkTicket(S6BettingModel bettingModel, S6WinningModel winningModel) {
        boolean flag = true;
        Map<String, String> hashChannel = Const.hashChannel;

        if (bettingModel.refId.equals(winningModel.refId)){

            if (!bettingModel.member.equals(winningModel.member)){
                flag = false;
            }

            String channel = bettingModel.channel;
            if (hashChannel.containsKey(channel)){
                channel = hashChannel.get(channel);
            }
            if (!channel.equals(winningModel.channel)){
                flag = false;
            }

            if (!bettingModel.betNumber.equals(winningModel.betNumber)){
                flag = false;
            }

            if (!bettingModel.betAmount.equals(winningModel.betAmount)){
                flag = false;
            }

            if (!bettingModel.betType.equals(winningModel.betType)){
                flag = false;
            }

            if (!bettingModel.betKind.equals(winningModel.betKind)){
                flag = false;
            }

        }else{
            flag = false;
        }

        if (!flag){
            log(Const.ticket_not_correct + winningModel.refId);
        }
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
                model.winningAmount = data[6];
                model.betType = data[7];
                model.betKind = data[8];
                model.member = data[9];

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
}
