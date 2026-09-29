## 1. Thông tin sinh viên

- **Họ và tên**: Ngô Quang Chương
- **Mã số SV**: 1150080087
- **Tên bài thực hành**: LAB3 - KHẢO SÁT VÀ ĐÁNH GIÁ BỀ MẶT MẠNG BẰNG NMAP
- **Tên file Word gốc**: 11_ĐH_THMT-LAB4_1150080087-NgoQuangChuong.docx

## 2. Máy móc và phần mềm sử dụng

- **Hệ điều hành/máy ảo:**
  - Windows 11 25H2 x64
  - Kali Linux
  - Metasploitable 2

- **Phần mềm chạy máy ảo:** VMware Workstation Pro 26H1.

- **Cấu hình mạng:** Các máy ảo được kết nối bằng **Host-only**.

- **Phần mềm sử dụng:** Nmap.

## 3. Các việc đã làm và kết quả

- **Tình huống 1:** Thiết lập môi trường thực hành gồm Windows 11, Kali Linux và Metasploitable 2 trên VMware Workstation Pro, sử dụng mạng **Host-only** để cô lập môi trường thực hành.
  → **Kết quả: PASS**

- **Tình huống 2:** Kiểm tra và cài đặt Nmap trên Kali Linux, sau đó kiểm tra phiên bản Nmap để bảo đảm công cụ hoạt động bình thường.
  → **Kết quả: PASS**

- **Tình huống 3:** Kiểm tra địa chỉ IP của các máy ảo và thực hiện kiểm tra kết nối giữa Kali Linux và Metasploitable 2 trước khi tiến hành quét.
  → **Kết quả: PASS**

- **Tình huống 4:** Sử dụng Nmap để phát hiện các host đang hoạt động trong mạng Host-only và xác định Metasploitable 2 làm máy đích thực hành.
  → **Kết quả: PASS**

- **Tình huống 5:** Thực hiện TCP Connect scan (`-sT`) và SYN scan (`-sS`) trên Metasploitable 2, sau đó so sánh trạng thái `open`, `closed`, `filtered` và thời gian quét.
  → **Kết quả: PASS**

- **Tình huống 6:** Thực hiện FIN scan, Xmas scan và NULL scan để quan sát cách mục tiêu phản hồi với các gói TCP bất thường.
  → **Kết quả: PASS**

- **Tình huống 7:** Thực hiện ACK scan (`-sA`) để quan sát khả năng lọc gói tin của firewall và phân biệt trạng thái `filtered` và `unfiltered`.
  → **Kết quả: PASS**

- **Tình huống 8:** Thực hiện UDP scan đối với một số cổng phổ biến, quan sát các trạng thái `open`, `closed` và `open|filtered` và xác định dịch vụ tương ứng.
  → **Kết quả: PASS**

- **Tình huống 9:** Sử dụng `-sV`, `-O` và `-A` để nhận diện phiên bản dịch vụ, hệ điều hành và thu thập thêm thông tin về máy đích.
  → **Kết quả: PASS**

- **Tình huống 10:** Sử dụng NSE để thu thập thông tin SMB và kiểm tra dấu hiệu liên quan đến MS17-010 trên máy đích trong môi trường lab.
  → **Kết quả: PASS**

- **Tình huống 11:** Xuất kết quả Nmap dưới các định dạng text, XML và grepable; thực hiện lọc thông tin từ kết quả quét và chuyển kết quả XML sang HTML để lưu trữ làm bằng chứng.
  → **Kết quả: PASS**

- **Tình huống 12:** Thực hiện quét trước và sau khi hardening, so sánh trạng thái port và dịch vụ để đánh giá hiệu quả của biện pháp phòng thủ.
  → **Kết quả: PASS**

- **Cuối cùng:** Kiểm tra lại kết quả, lưu các ảnh chụp màn hình và tệp kết quả Nmap làm bằng chứng thực hành; khôi phục môi trường nếu cần thiết.
  → **Kết quả: PASS**

## 4. Lỗi vặt lúc làm và cách sửa

- **Lỗi 1:** Kali Linux bị lỗi khi chạy `sudo apt update`, hệ thống báo lỗi liên quan đến HTTP repository hoặc kích thước gói tải về không khớp với yêu cầu.
  - **Cách sửa:** Em tiến hành đợi 3 tiếng và quay trở lại thực hiện và thành công.

- **Lỗi 2:** Kali Linux không kết nối được với Metasploitable 2 khi sử dụng mạng Host-only.
  - **Cách sửa:** Kiểm tra lại cấu hình mạng của hai máy ảo, bảo đảm cả Kali Linux và Metasploitable 2 cùng sử dụng **Host-only Network**, kiểm tra địa chỉ IP và subnet mask.

- **Lỗi 3:** Cấu hình Host-only bị sai hoặc các máy ảo nhận địa chỉ IP không đúng dải mạng.
  - **Cách sửa:** Kiểm tra lại Host-only Adapter trên VMware, bảo đảm các máy sử dụng cùng một mạng Host-only và không bị trùng IP. Sau khi chỉnh sửa, kiểm tra lại địa chỉ IP trên Kali và Metasploitable 2.

- **Lỗi 4:** Chuyển kết quả Nmap từ XML sang HTML trên Kali Linux bị lỗi hoặc không thực hiện được.
  - **Cách sửa:** Kiểm tra xem đã cài `xsltproc` hay chưa. Nếu chưa có thì cài đặt bằng APT, sau đó sử dụng `xsltproc` để chuyển file XML của Nmap sang HTML. Cài được nhưng bị lỗi như trình bày trong Video báo cáo.
