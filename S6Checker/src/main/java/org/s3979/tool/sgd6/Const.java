package org.s3979.tool.sgd6;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class Const {

    public static String total_num_of_ticket = "TỔNG SỐ VÉ CƯỢC: ";
    public static String total_num_of_winning_ticket = "TỔNG SỐ VÉ TRÚNG: ";
    public static String total_betting = "TỔNG TIỀN CƯỢC TRÚNG";
    public static String total_winning = "TỔNG TIỀN TRÚNG";

    public static String TITLE_PASS = "\n==> PASS";
    public static String TITLE_FAIL = "\n==> FAIL";

    public static String PLEASE_CHECK = "-> Please check: ";
    public static String press_any_key = "\nPlease hit ENTER to continue...";
    public static String stop_program = "\n=====Program STOP....!!!=====\n";
    public static String start_program = "\n=====Program RUNNING....!!!=====\n";

    public static String title_step_1 = "\n 1: Kiểm tra toàn bộ thông tin của vé trúng với danh sách vé cược";
    public static String title_step_2 = "\n 2: Kiểm tra Tổng tiền cược trúng và Tổng tiền trúng";
    public static String title_step_3 = "\n 3: Kiểm tra số lô của từng vé trúng";
    public static String[] ignore_winning_text = {"Mã Phiếu", "Thành Viên", "TỔNG VÉ TRÚNG", "TỔNG TIỀN"};
    public static String[] ignore_betting_text = {"Mã Phiếu", "Thành Viên", "TỔNG SỐ VÉ", "TỔNG TIỀN"};

    public static String ticket_not_found = "-> Không tìm thấy danh sách vé cược";
    public static String winning_ticket_not_found = "-> Không tìm thấy danh sách vé trúng";
    public static String not_found = "-> Không tìm thấy vé trúng này trong danh sách vé ban đầu: ";
    public static String ticket_not_correct = "-> Thông tin vé trúng không khớp với vé ban đầu: ";
    public static String can_not_get_total_win = "-> Không xác định được Tổng tiền cược trúng và Tổng tiền trúng";
    public static String not_support_live_bet = "-> Do NOT support LIVE bet. Please check: ";

    public static Map<String, String> hashChannel;

    static {
        hashChannel = new HashMap<>();

        hashChannel.put("Khanh Hoa", "Khánh Hoà");
        hashChannel.put("Da Nang", "Đà Nẵng");
        hashChannel.put("Tay Ninh", "Tây Ninh");

        // T2
        hashChannel.put("Ho Chi Minh, Dong Thap, Ca Mau", "Hồ Chí Minh, Đồng Tháp, Cà Mau");
        hashChannel.put("Phu Yen, Thua Thien Hue", "Phú Yên, Thừa Thiên Huế");

        // T3
        hashChannel.put("Dak Lak, Quang Nam", "Đăk Lăk, Quảng Nam");

        // T4
        hashChannel.put("Dong Nai, Can Tho, Soc Trang", "Đồng Nai, Cần Thơ, Sóc Trăng");

        hashChannel.put("Da Nang, Khanh Hoa", "Đà Nẵng, Khánh Hoà");
        hashChannel.put("Khanh Hoa, Da Nang", "Khánh Hoà, Đà Nẵng");

        // T7
        hashChannel.put("Ho Chi Minh, Binh Phuoc, Hau Giang", "Hồ Chí Minh, Bình Phước, Hậu Giang");
        hashChannel.put("Tra Vinh", "Trà Vinh");
        hashChannel.put("Ho Chi Minh, Long An, Binh Phuoc, Hau Giang", "Hồ Chí Minh, Long An, Bình Phước, Hậu Giang");
        hashChannel.put("Hau Giang, Long An, Binh Phuoc", "Hậu Giang, Long An, Bình Phước");
        hashChannel.put("Binh Phuoc, Hau Giang", "Bình Phước, Hậu Giang");

        // CN
        hashChannel.put("Tien Giang, Kien Giang, Da Lat", "Tiền Giang, Kiên Giang, Đà Lạt");
        hashChannel.put("Kon Tum, Khanh Hoa, Thua Thien Hue", "Kon Tum, Khánh Hoà, Thừa Thiên Huế");

    }
}
