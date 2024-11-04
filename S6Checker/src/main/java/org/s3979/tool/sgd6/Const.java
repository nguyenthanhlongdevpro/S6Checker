package org.s3979.tool.sgd6;

@SuppressWarnings("all")
public class Const {

    public static String total_num_of_ticket = "TỔNG SỐ VÉ CƯỢC: ";
    public static String total_num_of_winning_ticket = "TỔNG SỐ VÉ TRÚNG: ";
    public static String total_betting = "TỔNG TIỀN CƯỢC TRÚNG";
    public static String total_winning = "TỔNG TIỀN TRÚNG";

    public static String TITLE_PASS = "\n==> ✅";
    public static String TITLE_FAIL = "\n==> \uD83D\uDD34";

    public static String PLEASE_CHECK = "\n-> Please check: ";
    public static String press_any_key = "\nPlease hit ENTER to continue...";
    public static String stop_program = "\n=====Program STOP....!!!=====\n";
    public static String start_program = "\n=====Program RUNNING....!!!=====\n";

    public static String title_step_1 = "\n 1: Kiểm tra toàn bộ thông tin của vé trúng với danh sách vé cược";
    public static String title_step_2 = "\n 2: Kiểm tra Tổng tiền cược trúng và Tổng tiền trúng";
    public static String title_step_3 = "\n 3: Kiểm tra số lô của từng vé trúng";
    public static String[] ignore_winning_text = {"Mã Phiếu", "Thành Viên", "TỔNG VÉ TRÚNG", "TỔNG TIỀN"};
    public static String[] ignore_betting_text = {"Mã Phiếu", "Thành Viên", "TỔNG SỐ VÉ", "TỔNG TIỀN"};

    public static String ticket_not_found = "\n-> Không tìm thấy danh sách vé cược";
    public static String winning_ticket_not_found = "\n-> Không tìm thấy danh sách vé trúng";
    public static String not_found = "\n-> Không tìm thấy vé trúng này trong danh sách vé ban đầu: ";
    public static String ticket_not_correct = "\n-> Thông tin vé trúng không khớp với vé ban đầu: ";
    public static String can_not_get_total_win = "\n-> Không xác định được Tổng tiền cược trúng và Tổng tiền trúng";
    public static String not_support_live_bet = "\n-> Do NOT support LIVE bet. Please check: ";
}
