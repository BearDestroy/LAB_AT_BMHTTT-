## 1. Thông tin sinh viên

- **Họ và tên**: Ngô Quang Chương
- **Mã số SV**: 1150080087
- **Tên bài thực hành**: LAB3 - Mấy cái vụ tài sản, rủi ro và các kiểu tấn công mạng
- **Tên file Word gốc**: 11_ĐH_THMT-LAB3_1150080087-NgoQuangChuong.docx

## 2. Máy móc và phần mềm sử dụng

- **Hệ điều hành**: Em xài máy ảo Windows 11 bản 25H2 x64.
- **Phần mềm chạy máy ảo**: VMware Workstation Pro 26H1.
- **Cấu hình máy ảo**: Cho nó 2 nhân CPU, RAM 6GB, ổ cứng 64GB, chỉnh mạng kiểu "Host-only" để nó không nối ra Internet bên ngoài.
- **Phần mềm cài thêm**: Python (bản 3.14.7) và phần mềm soi mạng Wireshark (bản 4.6.8).
- **Đồ nghề của Microsoft**: Em tải thêm Sysmon 15.22, Autoruns 14.3 với Process Explorer 17.14 để soi xem máy có gì bất thường không.

## 3. Các bước chuẩn bị công cụ

- **Bước 1**: Tạo cái máy ảo Win 11 xong, cập nhật nó lên, rồi chụp lại một cái ảnh tình trạng máy lúc đang sạch (snapshot tên LAB3_CLEAN_20260914) để lỡ vọc hư thì quay lại cho nhanh.
- **Bước 2**: Mở cái bảng lệnh màu xanh (PowerShell) bằng quyền Admin, gõ mấy dòng lệnh để tạo thư mục `C:\LAB3` chia ra các ngăn để chứa file bài làm.
- **Bước 3**: Lấy cái file nén bài tập thầy cho (`LAB3_Threats_Assets.zip`) bỏ vô thư mục Downloads, giải nén nó ra.
- **Bước 4**: Cài Python với cài Wireshark vào máy. Nhớ là lúc cài Wireshark phải giữ cái dấu tick ở chỗ "Npcap" thì tí nữa mới soi được mạng của cục bộ máy Em.
- **Bước 5**: Dùng lệnh tải mấy cái tool quản lý của Microsoft về, giải nén ra thư mục Tools.

## 4. Các việc đã làm và kết quả

- **Tình huống 1**: Ngồi lập bảng liệt kê xem máy Em có dữ liệu gì quan trọng, rủi ro bị mất mát ra sao -> **Kết quả: PASS**
- **Tình huống 2**: Em tạo một cái file virus giả (tên EICAR) để thử xem thằng diệt virus Windows Defender có nhạy không, có bắt được không -> **Kết quả: PASS**
- **Tình huống 3**: Tạo một người dùng ảo tên `lab3user`, cố tình gõ sai mật khẩu vài lần để xem máy tính có ghi sổ lại mấy lần đăng nhập trật lất không -> **Kết quả: PASS**
- **Tình huống 4**: Cài thử một chương trình cho nó tự động chạy ngầm trên máy lúc mới mở lên (kiểu như bắt chước hành vi của mã độc) rồi dùng tool dò xem nó giấu ở đâu -> **Kết quả: PASS**
- **Tình huống 5**: Dùng phần mềm Wireshark nghe lén thử xem nếu lướt web không có bảo mật (HTTP) thì bị lộ chữ gì so với web có khóa bảo mật (HTTPS) -> **Kết quả: PASS**
- **Tình huống 6**: Chạy một đoạn code Python để làm máy bị quá tải một xíu (kiểu giả vờ bị tấn công sập web), rồi đọc thêm mấy cái file thống kê xem tụi nó hay khủng bố email kiểu gì -> **Kết quả: PASS**
- **Tình huống 7**: Mở mấy cái thư lừa đảo offline lên xem tụi nó hay dùng chữ gì, mạo danh ai để dụ người ta bấm vô link -> **Kết quả: PASS**
- **Cuối cùng**: Dọn dẹp rác, tắt hết mấy cái chạy ngầm nãy giờ làm để gom bằng chứng nộp -> **Kết quả: PASS**

## 5. Lỗi vặt lúc làm và cách sửa

- **Lỗi 1**: Gõ lệnh tạo thư mục hay cài phần mềm mà máy cứ báo lỗi màu đỏ lè.
  - **Cách sửa**: Là do quên mở cái cửa sổ lệnh bằng quyền quản trị cao nhất (Run as administrator). Tắt đi mở lại chuột phải chọn cái đó là hết.
- **Lỗi 2**: Vừa tạo cái file virus giả (EICAR) xong, quay đi quay lại thấy nó biến mất tiêu.
  - **Cách sửa**: Cái này bình thường, do thằng diệt virus (Defender) của Win nó hốt lẹ quá thôi. Chỉ cần mở phần lịch sử bị chặn của nó ra chụp hình lại nộp là có điểm, cấm đi khôi phục lại file đó.
- **Lỗi 3**: Lúc tính soi mạng cục bộ bằng Wireshark mà không thấy cái cổng vòng lặp (loopback) đâu cả.
  - **Cách sửa**: Do lúc cài Wireshark bấm Next nhanh quá quên tick vô cài thằng hỗ trợ mạng Npcap. Cài lại tick vô cái đó là dò ra.
- **Lỗi 4**: Đang mở mạng ảo nội bộ mà máy báo lỗi cổng 8080 bị thằng khác xài rồi.
  - **Cách sửa**: Tìm xem thằng nào đang xài cổng 8080 (tìm mã số PID của nó) rồi dùng lệnh `Stop-Process` ép tắt nó đi, xong chạy lại là ngon lành.
