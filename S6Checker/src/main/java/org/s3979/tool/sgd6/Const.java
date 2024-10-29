package org.s3979.tool.sgd6;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class Const {

    public static String ticket_not_found = "Không tìm thấy danh sách vé cược";
    public static String winning_ticket_not_found = "Không tìm thấy danh sách vé trúng";
    public static String total_num_of_ticket = "TỔNG SỐ VÉ CƯỢC: ";
    public static String total_num_of_winning_ticket = "TỔNG SỐ VÉ TRÚNG: ";
    public static String not_found = "==> Không tìm thấy vé trúng này trong danh sách vé ban đầu: ";
    public static String press_any_key = "\nPlease hit ENTER to continue...";
    public static String stop_program = "\n=====Program STOP....!!!=====\n";
    public static String start_program = "\n=====Program RUNNING....!!!=====\n";
    public static String ticket_not_correct = "==> Thông tin vé trúng không giống với vé ban đầu: ";
    public static String can_not_get_total_win = "Không xác định được Tổng tiền cược trúng và Tổng tiền trúng";
    public static String total_betting = "TỔNG TIỀN CƯỢC TRÚNG";
    public static String total_winning = "TỔNG TIỀN TRÚNG";
    public static String not_support_live_bet = "Do NOT support LIVE bet: ";
    public static String title_step_1 = "\n 1: Kiểm tra toàn bộ thông tin của vé trúng với danh sách vé cược";
    public static String title_step_2 = "\n 2: Kiểm tra Tổng tiền cược trúng và Tổng tiền trúng";
    public static String title_step_3 = "\n 3: Kiểm tra số lô của từng vé trúng";
    public static String[] ignore_winning_text = {"Mã Phiếu", "Thành Viên", "TỔNG VÉ TRÚNG", "TỔNG TIỀN"};
    public static String[] ignore_betting_text = {"Mã Phiếu", "Thành Viên", "TỔNG SỐ VÉ", "TỔNG TIỀN"};
    public static Map<String, String> hashChannel;

    static {
        hashChannel = new HashMap<>();

        hashChannel.put("Khanh Hoa", "Khánh Hoà");
        hashChannel.put("Da Nang", "Đà Nẵng");
        hashChannel.put("Da Nang, Khanh Hoa", "Đà Nẵng, Khánh Hoà");
        hashChannel.put("Khanh Hoa, Da Nang", "Khánh Hoà, Đà Nẵng");
        hashChannel.put("Ho Chi Minh, Dong Thap, Ca Mau", "Hồ Chí Minh, Đồng Tháp, Cà Mau");
        hashChannel.put("Phu Yen, Thua Thien Hue", "Phú Yên, Thừa Thiên Huế");
        hashChannel.put("Ho Chi Minh, Binh Phuoc, Hau Giang", "Hồ Chí Minh, Bình Phước, Hậu Giang");
        hashChannel.put("Tra Vinh", "Trà Vinh");
        hashChannel.put("Tien Giang, Kien Giang, Da Lat", "Tiền Giang, Kiên Giang, Đà Lạt");
        hashChannel.put("Kon Tum, Khanh Hoa, Thua Thien Hue", "Kon Tum, Khánh Hoà, Thừa Thiên Huế");
        hashChannel.put("Dak Lak, Quang Nam","Đăk Lăk, Quảng Nam");
    }
}
