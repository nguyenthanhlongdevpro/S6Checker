package org.s3979.tool.sgd6;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class S6Checker {

    public static void main(String[] args) {
        log("Program RUNNING....!!!\n");

        String workingDir = System.getProperty("user.dir");

        String bettingDir = String.format("%s\\data\\ve_cuoc", workingDir);
        List<S6BettingModel> lstBetData = loadAllBettingData(bettingDir);

        String winningDir = String.format("%s\\data\\ve_thang", workingDir);
        List<S6WinningModel> lstWinningData = loadAllWinningData(winningDir);

        compare(lstBetData, lstWinningData);

        log("\nProgram STOP....!!!");
    }

    private static void compare(List<S6BettingModel> lstBetData, List<S6WinningModel> lstWinningData) {

        int s1 = lstBetData.size();
        if (s1 != 0) {
            log("Betting Size: " + s1);
        }else{
            log("KHONG TIM THAY VE CUOC");
        }

        int s2 = lstWinningData.size();
        if (s2 != 0) {
            log("Winning Size: " + s2);
        }else{
            log("KHONG TIM THAY VE CUOC THANG");
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
                model.betType = data[7];
                model.betKind = data[8];
                model.type = data[9];
                model.channel = data[10];
                model.ip = data[11];
                model.betMessage = data[12];
                model.betDate = data[13];
                model.betCreate = data[14];

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

                model.channel = data[0];
                model.winingChannel = data[1];
                model.betNumber = data[2];
                model.winningAmount = data[3];
                model.betType = data[4];
                model.member = data[5];

                return model;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private static boolean checkIgnoreBettingRow(String data) {
        String[] items = {
        };

        for (String item : items) {
            if (data.isEmpty() || data.toLowerCase().contains(item.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    private static boolean checkIgnoreWinningRow(String data) {
        String[] items = {
        };

        for (String item : items) {
            if (data.isEmpty() || data.toLowerCase().contains(item.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    private static void log(String text){
        if (!text.isEmpty()){
            System.out.println(text);
        }
    }
}
