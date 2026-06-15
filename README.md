# 🎬 FlickFind — Ứng dụng tìm kiếm & tra cứu phim

![Android](https://img.shields.io/badge/Platform-Android-green) ![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue) ![TMDB](https://img.shields.io/badge/API-TMDB-orange)

## ✨ Tính năng

| Màn hình | Tính năng |
|----------|-----------|
| 🌊 **Splash** | Logo animation, auto-navigate |
| 🔐 **Đăng nhập/Đăng ký** | Validate email/password, ẩn/hiện mật khẩu |
| 🏠 **Trang chủ** | Các dải phim trượt ngang: Thịnh hành, Phim mới nhất, Đánh giá cao, Phổ biến |
| 🔍 **Tìm kiếm** | Bộ lọc theo Thể loại và Năm, real-time TMDB search |
| ❤️ **Yêu thích** | Lưu offline phim yêu thích (sử dụng DataStore) |
| 🎬 **Chi tiết phim** | Backdrop, poster, cast, trailer link, similar movies |
| 👤 **Hồ sơ** | Thống kê, cài đặt chế độ Tối (Dark Mode), đổi mật khẩu |

## ⚡ Hướng dẫn sử dụng ứng dụng (User Manual)

Dưới đây là luồng trải nghiệm các tính năng chính của ứng dụng:

### 1. Đăng ký & Đăng nhập
- Ở màn hình khởi động, bạn có thể chọn **Đăng nhập** nếu đã có tài khoản (hoặc dùng tài khoản Demo: `demo@flickfind.com` / `123456`).
- Nếu chưa có, chọn **Đăng ký**, điền tên, email và mật khẩu (có thể bấm biểu tượng con mắt để **hiện/ẩn** mật khẩu). 

### 2. Khám phá Phim (Trang chủ)
- Ở trang chủ, bạn có thể vuốt màn hình lên/xuống để xem các mục: **Thịnh hành**, **Phim mới nhất**, **Đánh giá cao**, **Phổ biến**.
- Tại mỗi mục, bạn có thể vuốt ngang sang trái/phải để lướt xem các bộ phim trong danh sách.

### 3. Tìm kiếm Phim
- Chuyển sang thẻ **Tìm kiếm** (biểu tượng Kính lúp ở thanh điều hướng bên dưới).
- Gõ tên phim bạn muốn tìm vào ô tìm kiếm ở trên cùng.
- Hoặc sử dụng các **nút bấm lọc thông minh**: Bấm vào nút *Hành động*, *Kinh dị*,... để lọc phim theo **Thể loại**. Kéo thanh cuộn bên dưới để chọn lọc thêm theo **Năm phát hành** (từ 2010 - 2025).

### 4. Xem chi tiết & Lưu yêu thích
- Tại bất kỳ đâu, khi bấm vào poster của một bộ phim, bạn sẽ được đưa vào trang **Chi tiết phim**.
- Ở đây hiển thị ảnh bìa lớn, thông tin mô tả, dàn diễn viên (Cast) cuộn ngang.
- Bấm vào nút **Trái tim (Yêu thích)** góc trên cùng bên phải để lưu phim vào máy để tiện xem lại sau này (bấm lại lần nữa để hủy).

### 5. Xem danh sách Yêu thích
- Bấm vào thẻ **Yêu thích** (biểu tượng Trái tim) ở thanh điều hướng dưới đáy.
- Màn hình này sẽ hiển thị toàn bộ những tựa phim bạn đã thả tim. Dữ liệu này được lưu ngay trong máy nên kể cả khi bạn thoát app và vào lại thì nó vẫn giữ nguyên.

### 6. Hồ sơ & Cài đặt giao diện
- Chuyển sang thẻ **Cá nhân** (biểu tượng Hình người).
- Ở mục Cài đặt, bạn có thể bật công tắc **Giao diện tối (Dark Mode)** để ứng dụng tự động đổi toàn bộ sang tông màu Đen điện ảnh rạp chiếu phim, rất dịu mắt khi xem đêm.
- Bạn cũng có thể bấm vào **Đổi mật khẩu** hoặc **Đăng xuất** tài khoản.

---

## 🛠 Tech Stack

- **Ngôn ngữ:** Kotlin
- **Giao diện:** Jetpack Compose (Material Design 3)
- **Kiến trúc:** MVVM (Model-View-ViewModel) + StateFlow
- **Mạng & API:** Retrofit2 + OkHttp3 + Gson
- **Tải ảnh:** Coil Compose
- **Lưu trữ nội bộ:** DataStore Preferences
- **Xử lý bất đồng bộ:** Coroutines

## ⚡ Hướng dẫn cài đặt & Chạy ứng dụng

Nếu bạn nhận được file nén `.rar` hoặc `.zip` của mã nguồn này, vui lòng làm theo các bước sau để mở:

### 1. Giải nén và mở bằng Android Studio
- **Giải nén** file nén ra một thư mục cố định.
- Bật **Android Studio** (Khuyến nghị phiên bản Iguana, Jellyfish, Koala trở lên để hỗ trợ tốt nhất Jetpack Compose).
- Chọn **File → Open...**
- Dẫn tới đúng thư mục `FlickFind` vừa giải nén (có biểu tượng con robot Android màu xanh) và nhấn **OK**.
- **Tuyệt đối không** dùng tính năng `Import Project`.

### 2. Chờ Gradle Sync (Quan trọng)
- Máy tính **bắt buộc phải có kết nối Internet**.
- Android Studio sẽ tự động tải các thư viện cần thiết. Bạn hãy nhìn xuống thanh trạng thái ở góc dưới cùng màn hình và đợi cho đến khi tiến trình báo **`Gradle sync finished`** (thường mất 3 - 5 phút cho lần chạy đầu tiên).

### 3. Thêm API Key của TMDB (Tùy chọn nếu đã có sẵn)
Mở file `app/build.gradle.kts` (hoặc `build.gradle`), tìm dòng:
```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"4287ad07e2dc8f9f57e4ae1310df2b11\"")
```
Nếu bạn có API Key riêng, hãy thay chuỗi mã số ở trên bằng API Key của bạn. Nếu không, bạn cứ giữ nguyên.

### 4. Chạy ứng dụng (Run App)
- Nhấn tổ hợp phím **Shift + F10** hoặc bấm nút **▶ Run** màu xanh lá cây ở thanh công cụ phía trên.
- **Lưu ý:** Hãy dùng máy ảo (Emulator) chạy **Android 8.0 (API 26)** trở lên và đảm bảo máy ảo có kết nối mạng internet để tải hình ảnh và dữ liệu phim.

## 🔑 Tài khoản Demo

App hỗ trợ tính năng đăng ký và đăng nhập. Bạn có thể tự tạo tài khoản mới hoặc dùng tài khoản có sẵn dưới đây:
- **Email:** `demo@flickfind.com`
- **Password:** `123456`

---
*Made with ❤️ by FlickFind Team*
