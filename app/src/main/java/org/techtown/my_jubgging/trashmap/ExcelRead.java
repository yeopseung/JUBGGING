//package org.techtown.my_jubgging.trashmap;
//
//import android.content.Context;
//
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExcelRead {
//
//    public static void readExcelAndSave(Context context) throws IOException {
//         DBHelper dbHelper = new DBHelper(context);
//        String fileLocation = "엑셀 파일 경로";
//        FileInputStream file = new FileInputStream(fileLocation);
//        Workbook workbook = new XSSFWorkbook(file);
//        Sheet sheet = workbook.getSheetAt(0);
//
//
//        // Sheet 로 읽어들인 데이터를 다시 Row 와 Cell 로 나눈다.
//        List<PublicTrashAddress> list = new ArrayList<>();
//        for (int j = 2; j < 5376; j++) {
//            Row row = sheet.getRow(j);
//
//            // set id
//
//
//            // set address
//            String address = "서울 ";
//            address += row.getCell(2).toString() + " ";
//            address += row.getCell(3).toString() + " ";
//            address += row.getCell(4).toString();
//
//            //set kind
//            String kind = divideKind(row.getCell(6).toString());
//
//            //set longitude and latitude
//
//
//            //set spec
//            String spec = row.getCell(5).toString();
//
//            PublicTrashAddress publicTrashAddress = new PublicTrashAddress(address,kind,spec);
//
//            // save
//        }
//
//    }
//    private static String divideKind(String kind) {
//        String answer = "";
//        if (kind.contains("일반"))
//            answer = "General";
//        else if (kind.contains("재활용"))
//            answer = "Recycle";
//        else if (kind.contains("담배"))
//            answer = "Smoking";
//
//        // error detection
//        if (answer.equals(""))
//            System.out.println(kind + "is Wrong!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//        return answer;
//    }
//}
