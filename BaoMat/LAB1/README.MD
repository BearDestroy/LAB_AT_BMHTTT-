# LAB 1 - Mã hóa đối xứng cổ điển

## 1. Thông tin sinh viên

- **Họ và tên**: Ngô Quang Chương
- **Mã số SV**: 1150080087
- **Lớp**: 11THMT
- **Tên bài thực hành**: LAB1 - Mã hóa đối xứng cổ điển
- **Ngày thực hành**: 10/11/2026

## 2. Máy móc và phần mềm sử dụng

- **Hệ điều hành/máy:**
  - Windows máy cá nhân, thư mục project D:\BaoMat
  - Microsoft SQL Server 2019, instance SQLEXPRESS, cổng TCP 14333

- **Phần mềm sử dụng:** 23, Apache NetBeans, Microsoft SQL Server, SSMS.

## 3. Các việc đã làm và kết quả

- **Chuẩn bị project:** Tạo Maven project LAB1_MaHoaCoDien, xóa lớp main mặc định, cập nhật pom.xml, Clean and Build thành công. Tạo lớp dùng chung FileTextIO cho chức năng Open/Save của các form.
  → **Kết quả: PASS**

- **Bài 1.1 - Caesar:** Tạo lớp CaesarCipher và form frm_Caesar. Kiểm thử HELLO, khóa 3 cho ra KHOOR. Chương trình báo lỗi khi khóa không phải số.
  → **Kết quả: PASS**

- **Bài 1.2 - Vigenère:** Tạo lớp VigenereCipher và form frm_Vigenere. Kiểm thử HELLO, khóa KEY cho ra RIJVS.
  → **Kết quả: PASS**

- **Bài 1.3 - Rail Fence:** Tạo lớp RailFenceCipher và form frm_RailFence. Kiểm thử HELLO WORLD, khóa 3 cho ra HOREL OLLWD.
  → **Kết quả: PASS**

- **Bài 1.4 - Playfair:** Tạo lớp PlayfairCipher và form frm_Playfair. Kiểm thử HELLOWORLD, khóa KEYWORD cho ra GYIZSCOKCFBU.
  → **Kết quả: PASS**

- **Bài 1.5 - Hoán vị cột:** Tạo lớp TranspositionCipher và form frm_Transposition. Kiểm thử BAOMAT, khóa 2,3,1 cho ra AAOTBM.
  → **Kết quả: PASS**

- **Bài 1.6 - Autokey:** Tạo lớp AutokeyCipher và form frm_Autokey. Kiểm thử HELLO, khóa KEY cho ra RIJSS.
  → **Kết quả: PASS**

- **Nâng cao 01 - Chương trình tổng hợp và lưu lịch sử SQL Server:** Tạo CipherTongHop, DbConfig, HistoryDao và form frm_TongHop chạy đủ 6 thuật toán. Cài SQL Server Express và SSMS, bật TCP/IP, đặt cổng 14333, bật Mixed Mode. Chạy script 01_Tao_CSDL.sql và 02_Tao_Login_MAU.sql, đăng nhập thử bằng lab1_user. Tạo form frm_KetNoiSQL và frm_LichSu. Thực hiện 6 lần Encrypt, lịch sử được ghi và đọc lại thành công.
  → **Kết quả: PASS**

- **Nâng cao 02 - Phân tích mật mã cổ điển:** Tạo lớp AttackDemos và form frm_TanCong. Brute Force Caesar với bản mã KHOOR, dòng k = 3 cho ra HELLO. Frequency Analysis với bản mã PHHW PH DIWHU WKH WRJD SDUWB, H và W xuất hiện nhiều nhất, suy ra khóa k = 3. Known Plaintext Vigenère với HELLO và RIJVS, thu được khóa KEYKE.
  → **Kết quả: PASS**

- **Tạo menu chính và chạy toàn bộ chương trình:** Tạo form frm_Menu gồm 9 nút. Chạy Run Project, mỗi nút mở đúng form tương ứng.
  → **Kết quả: PASS**
