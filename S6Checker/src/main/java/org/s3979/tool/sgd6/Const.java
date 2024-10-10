package org.s3979.tool.sgd6;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class Const {

    public static String ticket_not_found = "KHÔNG TÌM THẤY VÉ CƯỢC";
    public static String winning_ticket_not_found = "KHÔNG TÌM THẤY VÉ TRÚNG";
    public static String total_num_of_ticket = "TỔNG SỐ VÉ CƯỢC: ";
    public static String total_num_of_winning_ticket = "TỔNG SỐ VÉ TRÚNG: ";
    public static String not_found = "===> KHÔNG TÌM THẤY VÉ TRÚNG NÀY TRONG DANH SÁCH VÉ BAN ĐẦU ";
    public static String press_any_key = "\nPlease hit ENTER to continue...";
    public static String stop_program = "\n=====Program STOP....!!!=====\n";
    public static String start_program = "\n=====Program RUNNING....!!!=====\n";
    public static String ticket_not_correct = "==> THÔNG TIN VÉ TRÚNG KHÔNG ĐÚNG: ";

    public static String[] ignore_winning_text = {"Mã Phiếu", "Thành Viên", "TỔNG VÉ TRÚNG", "TỔNG TIỀN"};
    public static String[] ignore_betting_text = {"Mã Phiếu", "Thành Viên", "TỔNG SỐ VÉ", "TỔNG TIỀN"};

    public static Map<String, String> hashChannel;
    static {
        hashChannel = new HashMap<>();

        /*
        T4
        * */
        // MN

        // MT
        hashChannel.put("Khanh Hoa", "Khánh Hoà");
        hashChannel.put("Da Nang", "Đà Nẵng");
        hashChannel.put("Da Nang, Khanh Hoa", "Đà Nẵng, Khánh Hoà");
        hashChannel.put("Khanh Hoa, Da Nang", "Khánh Hoà, Đà Nẵng");

        // MB
    }
}
